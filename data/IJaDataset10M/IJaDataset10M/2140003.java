package com.beanstalktech.couplet;

import java.lang.Exception;

/**
 * Exception indicating a problem with finding a couplet
 */
public class CoupletNotFoundException extends Exception {

    public CoupletNotFoundException(String message) {
        super(message);
    }
}
