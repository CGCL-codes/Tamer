package com.google.gwt.ricordo.client.exception;

@SuppressWarnings("serial")
public class RemoteOntologyServiceException extends RicordoException {

    public RemoteOntologyServiceException() {
    }

    public RemoteOntologyServiceException(String displayMessage, String errorMessage) {
        super(displayMessage, errorMessage);
    }
}
