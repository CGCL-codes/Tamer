package com.rbnb.compat;

/** SocketException
 */
public class SocketException extends java.io.IOException {

    /** Basic constructor
	 */
    public SocketException() {
        super();
    }

    /** Constructor with error string
	 */
    public SocketException(String s) {
        super(s);
    }
}
