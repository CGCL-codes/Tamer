package com.intelerad.tools.lib.concurrent;

/**
 * A handy utility call that allows a sub class to over-ride any one of the
 * methods below without having to over-ride all of them.
 * 
 * @see CallListener
 */
public class CallAdapter implements CallListener {

    public void handleSuccess(Object result) {
    }

    public void handleException(Exception exception) {
    }

    public void handleCancel() {
    }

    public void handleFinally() {
    }
}
