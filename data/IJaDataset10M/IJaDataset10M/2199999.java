package com.peterhi.io;

/**
 * @author Administrator
 *
 */
public class ByteArrayOutputStream extends java.io.ByteArrayOutputStream {

    /**
	 * 
	 */
    public ByteArrayOutputStream() {
    }

    /**
	 * @param arg0
	 */
    public ByteArrayOutputStream(int arg0) {
        super(arg0);
    }

    public ByteArrayOutputStream(byte[] borrow) {
        buf = borrow;
    }

    public synchronized void swap(byte[] newBorrow) {
        buf = newBorrow;
        count = 0;
    }
}
