package com.android.cts;

/**
 * Exception reporting that the device is invalid.
 *
 */
public class InvalidDeviceException extends Exception {

    private static final long serialVersionUID = 0L;

    public InvalidDeviceException() {
        super();
    }

    public InvalidDeviceException(final String msg) {
        super(msg);
    }
}
