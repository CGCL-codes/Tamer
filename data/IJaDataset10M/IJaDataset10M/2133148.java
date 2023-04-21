package com.m4f.business.service.exception;

public class VideoNotRemovableException extends Exception {

    public VideoNotRemovableException() {
        super();
    }

    public VideoNotRemovableException(String message, Throwable cause) {
        super(message, cause);
    }

    public VideoNotRemovableException(String message) {
        super(message);
    }

    public VideoNotRemovableException(Throwable cause) {
        super(cause);
    }
}
