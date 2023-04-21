package ch.olsen.servicecontainer.service;

public class StartServiceException extends Exception {

    private static final long serialVersionUID = 1L;

    public StartServiceException(String message) {
        super(message);
    }

    public StartServiceException(String message, Exception e) {
        super(message, e);
    }
}
