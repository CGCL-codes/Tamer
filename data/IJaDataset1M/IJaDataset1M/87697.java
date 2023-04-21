package org.regilo.core.model;

public class NoSuchConnectorException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7140935462694401773L;

    private String type;

    public NoSuchConnectorException(String type) {
        this.type = type;
    }

    @Override
    public String getMessage() {
        return "No connector found for type: " + type;
    }
}
