package org.slasoi.gslam.commons.plan.graph;

/**
 * Exception to be thrown when an edge already exists in a graph.
 * 
 * @author Beatriz Fuentes
 * 
 */
public class EdgeFoundException extends Exception {

    /**
     * serialversiouid.
     */
    private static final long serialVersionUID = 11111L;

    /**
     * Default message attached to this exception.
     */
    private static final String DEFAULT_MESSAGE = "Edge already exists in the graph";

    /**
     * Default constructor.
     */
    public EdgeFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
