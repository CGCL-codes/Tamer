package org.addsimplicity.anicetus.hibernate;

import org.addsimplicity.anicetus.io.ExceptionHandler;
import org.junit.runner.notification.StoppedByUserException;

/**
 * @author Dan Pritchett
 * 
 */
public class FailModeExceptionHandler implements ExceptionHandler {

    public void exceptionCaught(Throwable exception) {
        StoppedByUserException stop = new StoppedByUserException();
        stop.initCause(exception);
        exception.printStackTrace();
        throw stop;
    }
}
