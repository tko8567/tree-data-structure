package com.daniily.util;

import java.util.ArrayList;

/**
 * Class which implements tree data-structure.
 * <p> Tree data-structure means that every object put on it has its children
 * and one parent except for the root, which has no parents. Is constructed from
 * root object or empty, having a null object then. Navigation is performed in
 * the following way:
 * <p> For example we have a tree with 5 objects:
 * <pre>
 *     String a = "root";
 *     String b = "root-leave1";
 *     String c = "root-leave2";
 *     String d = "root-leave1-a";
 *     String e = "root-leave1-b";
 * </pre>
 * Let's put it in a new tree:
 * <pre>
 *     Tree&lt;String&gt; luckyTree = new Tree&lt;&gt;(a);
 *     luckyTree.add(b);
 *     luckyTree.add(c);
 *     luckyTree.add(0, d);
 *     luckyTree.add(0, e);
 * </pre>
 * Thus, we made a tree:
 * <pre>
 *     - - a - -
 *       /  \
 *      b    c
 *     / \
 *   d    e
 * </pre>
 * Each 'Node' (an object in a tree) has its index, which is represented by a
 * sequence of integer numbers. Each number indicates which child from current
 * node should be used to move. Children are enumerated from 0 as was added.
 * Root's index is always 0, so this number is hidden and not used in index
 * sequence. Thus, c's index is {1}, e's - {0, 1}. Deleting a node will shift
 * next children to the left, so deleting 'd' will put 'e' on position 0.
 * <p>It could be noticed that Tree class is generic. Use wanted class when
 * creating an instance of Tree, so methods as {@link #add(Object)} and {@link #get}
 * will use objects of wanted classes.
 * <p>
 * <p> This version is not steady against exceptions like NullPointerException
 * or IndexOutOfBoundsException
 * @param <E> class for nodes to use
 * @version 1 - structure realized, methods add, get, remove
 * @author Daniil Y
 */
public class Tree<E> {

	/** Root element */
	private Node<E> root;

	/** Constructor form null object */
	public Tree() {
		root = new Node<>();
	}

	/** Constructor from given object */
	public Tree(E value) {
		root = new Node<>(value);
	}

	/** Returns a root node */
	public Node<E> getRoot() {
		return root;
	}

	/** Method for getting object of root */
	public E get() {
		return root.getValue();
	}

	/** Method from getting object of indexed node */
	public E get(int... index) {
		return find(index, 0, root).getValue();
	}

	/** Method for adding a child to root */
	public void add(E e) {
		root.addChild(new Node<E>(e));
	}

	/** Method for adding a child to indexed node */
	public void add(E e, int... index) {
		find(index, 0, root).addChild(new Node<E>(e));
	}

	/** Removes a node with given index */
	public void remove(int... index) {
		find(index, 0, root).getParent()
				.removeChild(index[index[index.length - 1]]);
	}

	/**
	 * Changes the value of indexed node
	 * @param value a value to be written in node
	 * @param index index of node
	 */
	public void changeValue(E value, int... index) {
		find(index, 0, root).putValue(value);
	}

	/** Inner  method for searching for a Node. Call find(index, 0, root) */
	private Node<E> find(int[] index, int current, Node<E> node) {
		if (current == index.length) return node;
		return find(index, current + 1, node.getChild(index[current]));
	}

	// TODO: steady against exceptions -> return boolean/null or throw exception?
	// TODO: getPath(int... index)
	// TODO: clone()
	// TODO: move(int[] indexFrom, int[] indexTo)
	// TODO: Leave int[] index OR new class NodeIndex?

	/**
	 * Inner class which implements a Node.
	 * <p> Node is a 'cell' which has an object in it and links with some other
	 * nodes: each node has parent (except for root) and can have children.
	 * Nodes without children are called 'leaves', first node without a parent
	 * is 'root', children of the same node are 'siblings'.
	 * @param <E> - class to use
	 */
	public class Node<E> {

		/** List of children */
		private ArrayList<Node<E>> children;

		/** Stored value */
		private E value;

		/** Parent node */
		private Node<E> parent;

		/** Creates an empty node */
		Node() {
			children = new ArrayList<>();
		}

		/** Creates a node with given object */
		private Node(E value) {
			this();
			this.value = value;
		}

		/** Changes its value to given */
		private void putValue(E e) {
			value = e;
		}

		/** Returns its value */
		public E getValue() {
			return value;
		}

		/** Adds a child node */
		void addChild(Node<E> node) {
			node.setParent(this);
			children.add(node);
		}

		/** Returns a child with given index */
		public Node<E> getChild(int index) {
			return children.get(index);
		}

		/** Removes child with all the children below */
		private void removeChild(int index) {
			children.remove(index);
		}

		/** Returns parent node */
		public Node<E> getParent() {
			return parent;
		}

		/** Sets parent node */
		private void setParent(Node<E> parent) {
			this.parent = parent;
		}

		/**
		 * Check whether node is a leaf. 'Leaf' means node has no children
		 * @return true if leaf
		 */
		public boolean isLeaf() {
			return children.size() == 0;
		}

		/**
		 * Check whether node is a root. 'Root' means node has no parent
		 * @return true if root
		 */
		public boolean isRoot() {
			return parent == null;
		}

		/**
		 * Returns node's root. Technically, it goes up to tree while next node
		 * is not root.
		 * @return Node, root of tree, where this node is
		 */
		public Node<E> getRoot() {
			return getRoot(this);
		}

		/**
		 * Inner method for finding root recursively.
		 * @param node current node
		 * @return parent node or root
		 */
		private Node<E> getRoot(Node<E> node) {
			if (node.isRoot()) return node;
			return getRoot(node.getParent());
		}

		/**
		 * Returns all the siblings including this node as
		 * ArrayList&lt;Node&lt;E&gt;&gt;
		 * @return ArrayList of siblings
		 */
		public ArrayList<Node<E>> getSiblings() {
			if (this.isRoot()) {
				ArrayList<Node<E>> siblings = new ArrayList<>();
				siblings.add(this);
				return siblings;
			}
			return this.getParent().getChildren();
		}

		/**
		 * Returns ArrayList&lt;Node&lt;E&gt;&gt; with all the children if node
		 * is not a leaf, null otherwise
		 * @return ArrayList of child nodes
		 */
		public ArrayList<Node<E>> getChildren() {
			if (!this.isLeaf())
				return children;
			return null;
		}
	}
}
