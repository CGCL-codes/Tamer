package org.asoft.sapiente.data.tree;

import java.util.List;

public interface TreeNode<T> {

    /**
	 * Return the children of Node<T>. The Tree<T> is represented by a single
	 * root Node<T> whose children are represented by a List<Node<T>>. Each of
	 * these Node<T> elements in the List can have children. The getChildren()
	 * method will return the children of a Node<T>.
	 * @return the children of Node<T>
	 */
    public abstract List<TreeNode<T>> getChildren();

    /**
	 * Returns the number of immediate children of this Node<T>.
	 * @return the number of immediate children.
	 */
    public abstract int getNumberOfChildren();

    /**
	 * Adds a child to the list of children for this Node<T>. The addition of
	 * the first child will create a new List<Node<T>>.
	 * @param child a Node<T> object to set.
	 */
    public abstract void addChild(TreeNode<T> child);

    /**
	 * Inserts a Node<T> at the specified position in the child list. Will     * throw an ArrayIndexOutOfBoundsException if the index does not exist.
	 * @param index the position to insert at.
	 * @param child the Node<T> object to insert.
	 * @throws IndexOutOfBoundsException if thrown.
	 */
    public abstract void insertChildAt(int index, TreeNode<T> child) throws IndexOutOfBoundsException;

    /**
	 * Remove the Node<T> element at index index of the List<Node<T>>.
	 * @param index the index of the element to delete.
	 * @throws IndexOutOfBoundsException if thrown.
	 */
    public abstract void removeChildAt(int index) throws IndexOutOfBoundsException;

    public abstract T getValue();

    public abstract void setValue(T data);
}
