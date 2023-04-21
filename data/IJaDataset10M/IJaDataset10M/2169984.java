package abbot.swt.hierarchy;

import java.util.Collection;

/**
 * A specialization of {@link Visitable} that has one or more root nodes.
 * 
 * @param <T>
 *            the type of the nodes in the {@link Hierarchy}
 */
public interface Hierarchy<T> extends Visitable<T> {

    /**
	 * @return the root nodes of the receiver
	 */
    Collection<T> getRoots();

    /**
	 * Gets the parent of a node.
	 * 
	 * @param node
	 *            a node in the receiver
	 * @return the node's parent node
	 */
    T getParent(T node);

    /**
	 * Determines if a {@code T} is present in the receiver.
	 * 
	 * @param object
	 *            a {@code T}
	 * @return <code>true</code> if <code>object</code> is present in the receiver,
	 *         <code>false</code> otherwise.
	 */
    boolean contains(T object);

    /**
	 * Visits the nodes in the receiver (using the <a
	 * href="http://en.wikipedia.org/wiki/Visitor_pattern">Visitor</a> pattern) starting at the
	 * roots.
	 * 
	 * @param visitor
	 *            a {@code Visitor}
	 * @see Visitor
	 */
    void accept(Visitor<T> visitor);
}
