package com.vinay.ui.vinui.fw.core;

/**
 * A generic framework exception
 * 
 * @author Vinay Nath
 *
 */
public class VinUIException extends Exception {

    public VinUIException() {
        super();
    }

    public VinUIException(String desc) {
        super(desc);
    }

    public VinUIException(Throwable e) {
        super(e);
    }

    public VinUIException(String desc, Throwable e) {
        super(desc, e);
    }
}
