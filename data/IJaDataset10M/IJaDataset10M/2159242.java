package org.columba.core.gui.exception;

import java.beans.ExceptionListener;

public class UIExceptionListener implements ExceptionListener {

    public UIExceptionListener() {
        super();
    }

    public void exceptionThrown(Exception e) {
        new ExceptionHandler().processException(e);
    }
}
