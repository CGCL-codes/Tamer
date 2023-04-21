package org.webgraphlab.algorithm;

/**
 *	Generated from IDL definition of exception "GraphParseException"
 *	@author JacORB IDL compiler 
 */
public final class GraphParseException extends org.omg.CORBA.UserException {

    public GraphParseException() {
        super(org.webgraphlab.algorithm.GraphParseExceptionHelper.id());
    }

    public GraphParseException(String value) {
        super(value);
    }

    /** convenience method, not per IDL mapping */
    public String toString() {
        return "struct GraphParseException[ " + " ] ";
    }
}
