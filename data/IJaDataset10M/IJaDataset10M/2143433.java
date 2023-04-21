package org.columba.api.exception;

import java.util.EventListener;

/**
 * Method is executed if exception occured.
 * 
 * @author Frederik Dietz
 */
public interface IExceptionListener extends EventListener {

    public void exceptionOccured(Exception e);
}
