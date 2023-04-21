package org.dita2indesign.util;

public class DataUtilException extends Exception {

    public DataUtilException(Throwable e) {
        super(e);
    }

    public DataUtilException(String msg, Throwable e) {
        super(msg, e);
    }

    public DataUtilException(String msg) {
        super(msg);
    }
}
