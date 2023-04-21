package hexxa.structs.list;

import java.util.Iterator;

/**
 * This class extends the Java Iterator interface, so
 * my list will be able to use the java for each iteration.
 * @author David Ach
 *
 * @param <E> Generic item used in the list
 */
public class ListIterator<E> implements Iterator<E> {

    /**
	 * Position node of the iterator
	 */
    private ListNode<E> _node;

    /**
	 * Constructor to assign the node
	 * @param node The node to start iterating from, usually it's the start.
	 */
    public ListIterator(ListNode<E> node) {
        this._node = node;
    }

    @Override
    public boolean hasNext() {
        if (_node == null) {
            return false;
        }
        return true;
    }

    @Override
    public E next() {
        E ret = _node.getItem();
        _node = _node.getNext();
        return ret;
    }

    @Override
    public void remove() {
    }
}
