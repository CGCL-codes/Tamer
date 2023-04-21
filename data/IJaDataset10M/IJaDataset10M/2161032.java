package javax.validation;

/**
 * Represent the navigation path from an object to another
 * in an object graph.
 * Each path element is represented by a <code>Node</code>.
 *
 * The path corresponds to the succession of nodes
 * in the order they are returned by the <code>Iterator</code>
 *
 * @author Emmanuel Bernard
 */
public interface Path extends Iterable<Path.Node> {

    /**
	 * Represents an element of a navigation path
	 */
    interface Node {

        /**
		 * Property name the node represents
		 * or null if representing an entity on the leaf node
		 * (in particular the node in a <code>Path</code> representing
		 * the root object has its name null).
		 *
		 * @return property name the node represents
		 */
        String getName();

        /**
		 * @return true if the node represents an object contained in an Iterable
		 * or in a Map.
		 */
        boolean isInIterable();

        /**
		 * @return The index the node is placed in if contained
		 * in an array or List. Null otherwise.
		 */
        Integer getIndex();

        /**
		 * @return The key the node is placed in if contained
		 * in a Map. Null otherwise.
		 */
        Object getKey();
    }
}
