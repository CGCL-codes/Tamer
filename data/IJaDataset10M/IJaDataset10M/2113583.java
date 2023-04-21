package com.tinywebgears.tuatara.core.service;

import com.tinywebgears.tuatara.framework.common.ApplicationException;

public class ServiceException extends ApplicationException {

    public ServiceException(String userMessage) {
        super(userMessage);
    }

    public ServiceException(String userMessage, Throwable cause) {
        super(userMessage, cause);
    }
}
