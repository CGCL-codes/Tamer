package issrg.utils.xml;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.w3c.dom.Node;

/**
 * Class implementing the Iterator interface to allow fast iterating 
 * over the child nodes of an element.
 * 
 * @author sfl
 *
 */
public final class ChildIterator implements Iterator<Node> {

    /**
	 * The current node, in other words the one that will be returned
	 * by a call to next.
	 */
    private Node current;

    /**
	 * Construct the iterator.
	 * 
	 * @param parent a Node whose children will be iterated over. Should not
	 * be null. 
	 */
    public ChildIterator(Node parent) {
        current = parent.getFirstChild();
    }

    /**
	 * Checks whether this iterator can return an additional element.
	 * 
	 * @return true if the iterator has an additional element, false otherwise.
	 */
    public boolean hasNext() {
        return current != null;
    }

    /**
	 * Returns the next child of the given parent node.
	 * 
	 * @return the next child of the parent node.
	 */
    public Node next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        Node next = current;
        current = current.getNextSibling();
        return next;
    }

    /**
	 * The remove operation is not supported.
	 * 
	 * @throws UnsupportedOperationException
	 */
    public void remove() {
        throw new UnsupportedOperationException("Remove operation not supported by ChildIterator");
    }
}
