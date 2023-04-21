package com.netx.ebs;

abstract class SecurityCheckException extends SecurityException {

    public SecurityCheckException() {
        super();
    }

    public SecurityCheckException(String msg) {
        super(msg);
    }
}
