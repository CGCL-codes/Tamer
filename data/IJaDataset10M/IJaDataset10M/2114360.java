package net.sf.joafip.java.util.support;

import java.io.Serializable;
import java.util.ListIterator;
import net.sf.joafip.AssertNotNull;
import net.sf.joafip.StoreNotUseStandardSerialization;

/**
 * minimum for linked list management<br>
 * make iterator able to support list concurent modification<br>
 * 
 * @author luc peuvrier
 * 
 * @param <E>
 *            the linked list element type
 */
@StoreNotUseStandardSerialization
public class LinkedListSupport<E> implements IListSupport<E>, Iterable<E>, ILinkedListSupportNodeManager<E>, Cloneable, Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7113927870997361921L;

    @AssertNotNull
    private LinkedListSupportManager<E> listManager;

    /**
	 * the root node, null for empty list<br>
	 * not transient for persistence<br>
	 */
    private ILinkedListSupportNode<E> root;

    /**
	 * last node of the list, null for empty list<br>
	 * not transient for persistence<br>
	 */
    private ILinkedListSupportNode<E> lastNode;

    /**
	 * not transient for persistence<br>
	 */
    private int size = 0;

    public LinkedListSupport() {
        super();
        listManager = new LinkedListSupportManager<E>(this);
    }

    public E addAtEnd(final E element) {
        listManager.addAtEnd(element);
        return null;
    }

    /**
	 * add node at end of this list<br>
	 * package visibility, should be used only internaly and by
	 * {@link LinkedListSupportIterator}<br>
	 * 
	 * @param toAdd
	 *            node to add
	 */
    public void addAtEnd(final LinkedListSupportNode<E> toAdd) {
        listManager.addAtEnd(toAdd);
    }

    public E addAtBegin(final E element) {
        listManager.addAtBegin(element);
        return null;
    }

    /**
	 * add node at begin of this list<br>
	 * package visibility, should be used only internaly and by
	 * {@link LinkedListSupportIterator}<br>
	 * 
	 * @param toAdd
	 *            node to add
	 */
    public void addAtBegin(final ILinkedListSupportNode<E> toAdd) {
        listManager.addAtBegin(toAdd);
    }

    /**
	 * add a node before reference node<br>
	 * 
	 * @param reference
	 *            reference node for add
	 * @param toAdd
	 *            the node to add
	 */
    public void addBefore(final ILinkedListSupportNode<E> reference, final ILinkedListSupportNode<E> toAdd) {
        listManager.addBefore(reference, toAdd);
    }

    public E add(final int index, final E element) {
        listManager.add(index, element);
        return null;
    }

    public E addReplace(final E element) {
        return listManager.addReplace(element).getPreviousElement();
    }

    public E remove(final Object object) {
        final ILinkedListSupportNode<E> removed = listManager.remove(object);
        return removed == null ? null : removed.getElement();
    }

    public E remove(final int index) {
        return listManager.remove(index);
    }

    public E removeFirst() {
        return listManager.removeFirst();
    }

    public E removeLast() {
        return listManager.removeLast();
    }

    /**
	 * remove a node from the list<br>
	 * must not change next and previous link in node to remove because this is
	 * used by {@link LinkedListSupportIterator} to restore its position in list<br>
	 * 
	 * @param toRemove
	 *            the node to remove
	 */
    public void remove(final ILinkedListSupportNode<E> toRemove) {
        listManager.remove(toRemove);
    }

    public E getFirst() {
        return listManager.getFirst();
    }

    public E getLast() {
        return listManager.getLast();
    }

    public E get(final Object object) {
        return listManager.get(object);
    }

    public E get(final int index) {
        return listManager.get(index).getElement();
    }

    public E set(final E element, final int index) {
        return listManager.set(element, index);
    }

    public boolean contains(final Object object) {
        return listManager.contains(object) != null;
    }

    public void clear() {
        listManager.clear();
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void incrementSize() {
        size++;
    }

    public void decrementSize() {
        size--;
    }

    public void resetSize() {
        size = 0;
    }

    public void setSize(final int newSize) {
        throw new UnsupportedOperationException();
    }

    public ListIterator<E> iterator() {
        return new LinkedListSupportIterator<E>(listManager);
    }

    public ListIterator<E> iterator(final int index) {
        return new LinkedListSupportIterator<E>(listManager, index);
    }

    public ListIterator<E> descendingIterator() {
        return new LinkedListSupportDescendingIterator<E>(listManager);
    }

    public ILinkedListSupportNode<E> getRoot() {
        return root;
    }

    public void setRoot(final ILinkedListSupportNode<E> rootNode) {
        this.root = rootNode;
    }

    public ILinkedListSupportNode<E> getLastNode() {
        return lastNode;
    }

    public ILinkedListSupportNode<E> getFirstNode() {
        return root;
    }

    public void setLastNode(final ILinkedListSupportNode<E> lastNode) {
        this.lastNode = lastNode;
    }

    public ILinkedListSupportNode<E> newLinkedListNode() {
        return new LinkedListSupportNode<E>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public LinkedListSupport clone() {
        try {
            final LinkedListSupport<E> linkedList = (LinkedListSupport<E>) super.clone();
            linkedList.listManager = new LinkedListSupportManager<E>(linkedList);
            linkedList.clear();
            ILinkedListSupportNode<E> current = root;
            while (current != null) {
                linkedList.addAtEnd(current.getElement());
                current = current.getNext();
            }
            return linkedList;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(final Object object) {
        boolean equals;
        if (object == this) {
            equals = true;
        } else {
            if (object.getClass().isAssignableFrom(LinkedListSupport.class)) {
                final LinkedListSupport<E> other = (LinkedListSupport<E>) object;
                ILinkedListSupportNode<E> current = getRoot();
                ILinkedListSupportNode<E> otherCurrent = other.getRoot();
                equals = true;
                while (equals && current != null && otherCurrent != null) {
                    final E element = current.getElement();
                    final E otherElement = otherCurrent.getElement();
                    if (element == null) {
                        equals = otherElement == null;
                    } else {
                        equals = element.equals(otherElement);
                    }
                    current = current.getNext();
                    otherCurrent = otherCurrent.getNext();
                }
                if (current != null || otherCurrent != null) {
                    equals = false;
                }
            } else {
                equals = false;
            }
        }
        return equals;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        ILinkedListSupportNode<E> current = root;
        while (current != null) {
            final E element = current.getElement();
            if (element != null) {
                result = PRIME * result + element.hashCode();
            }
            current = current.getNext();
        }
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('[');
        ILinkedListSupportNode<E> current = root;
        while (current != null) {
            stringBuffer.append(current.getElement());
            current = current.getNext();
            if (current != null) {
                stringBuffer.append(',');
            }
        }
        stringBuffer.append(']');
        return stringBuffer.toString();
    }
}
