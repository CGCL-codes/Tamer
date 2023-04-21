package de.ifgi.simcat2.plugin.reasoner.tableau;

/**
 * An exception that is thrown if a role edge is created without declaring names.
 * 
 * @author Christoph M�lligann
 */
public class InvalidEdgeTypeException extends Exception {

    private static final long serialVersionUID = 554807782901107451L;

    private long n1;

    private long n2;

    /**
	 * @param n1 first node that should have been related by the edge
	 * @param n2 second node that should have been related by the edge
	 */
    public InvalidEdgeTypeException(long n1, long n2) {
        this.n1 = n1;
        this.n2 = n2;
    }

    public String getMessage() {
        String message = "EdgeType ROLE requires names. Occured while connecting node #" + n1 + " and #" + n2 + ".";
        return message;
    }
}
