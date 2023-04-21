package com.tinywebgears.simplegae.core.service;

public class ServiceException extends RuntimeException {

    public ServiceException(String userMessage) {
        super(userMessage);
    }

    public ServiceException(String userMessage, Throwable cause) {
        super(userMessage, cause);
    }
}
