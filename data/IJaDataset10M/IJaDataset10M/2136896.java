package net.sourceforge.combean.graph.except;

/**
 * An exception to be thrown when a graph is set to a GraphAlgorithm with
 * setGraph and does not support the graph properties needed by the algorithm.
 * 
 * @author schickin
 *
 */
public class UnsupportedGraphProperty extends RuntimeException {

    private static final long serialVersionUID = 1;

    /**
	 * constructor
	 */
    public UnsupportedGraphProperty() {
        super();
    }

    /**
	 * @param message
	 */
    public UnsupportedGraphProperty(String message) {
        super(message);
    }
}
