package org.xito.httpservice.gentool;

public class GenerateException extends Exception {

    public GenerateException(String msg) {
        super(msg);
    }

    public GenerateException(String msg, Throwable exp) {
        super(msg, exp);
    }
}
