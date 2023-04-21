package org.uoa.eolus.vm;

public class UnknownVMException extends Exception {

    private static final long serialVersionUID = 3917373596854507147L;

    public UnknownVMException(String msg) {
        super(msg);
    }

    public UnknownVMException(String msg, Throwable e) {
        super(msg, e);
    }
}
