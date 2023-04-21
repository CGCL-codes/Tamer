package org.gbif.portal.harvest.workflow.activity;

/**
 * @author trobertson
 */
public class ErrorCountException extends Exception {

    /**
	 * Generated
	 */
    private static final long serialVersionUID = 2155043493099670283L;

    /**
	 * @param message To use
	 */
    public ErrorCountException(String message) {
        super(message);
    }
}
