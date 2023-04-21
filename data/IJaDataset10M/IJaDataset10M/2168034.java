package uk.ac.rdg.resc.grex.exceptions;

/**
 * Exception that is thrown when the user attempts to start a service instance
 * that is not ready (e.g. because a required parameter has not been set).
 *
 * @author Jon Blower
 * $Revision$
 * $Date$
 * $Log$
 */
public class InstanceNotReadyException extends GRexException {

    /** Creates a new instance of InstanceNotReadyException */
    public InstanceNotReadyException(String message) {
        super(message);
    }
}
