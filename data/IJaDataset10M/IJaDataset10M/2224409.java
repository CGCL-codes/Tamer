package com.hardcode.gdbms.engine.internalExceptions;

/**
 * @author Fernando Gonz�lez Cort�s
 */
public interface InternalExceptionListener {

    public void exceptionRaised(InternalExceptionEvent event);
}
