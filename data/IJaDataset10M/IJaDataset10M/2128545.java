package com.flagstone.transform.coder;

import java.io.IOException;

/**
 * CoderException is thrown when errors occur while encoding or decoding
 * objects. Errors are reported as either underflow or overflow errors
 * indicating that the class used to encode/decode a particular data structure
 * did not encode or decode the expected number of bytes. This allows the
 * majority of software errors and errors due to improperly encoded flash files
 * to be detected.
 */
public final class CoderException extends IOException {

    /** Serial number identifying the version of the object. */
    private static final long serialVersionUID = 1;

    /** Format string used in toString() method. */
    private static final String FORMAT = "CoderException: { " + "location=%s; length=%d; delta=%d; message=%s}";

    /**
     * The location of the start of the object being encoded/decoded
     * when the error occurred.
     */
    private final transient int start;

    /** The expected length of the encoded object. */
    private final transient int length;

    /**
     * The difference between the expected and actual number of bytes encoded
     * or decoded.
     */
    private final transient int delta;

    /**
     * Creates a CoderException to report where a problem occurred when encoding
     * or decoding a Flash (.swf) file.
     *
     * @param location
     *            the address in the file where the data structure being
     *            encoded/decoded is located. This is only valid for files being
     *            decoded since the encoded file will not be written if an
     *            exception occurs.
     *
     * @param message
     *            a short description of the error.
     */
    public CoderException(final int location, final String message) {
        super(message);
        start = location;
        length = 0;
        delta = 0;
    }

    /**
     * Creates a CoderException to report where a problem occurred when encoding
     * or decoding a Flash (.swf) file.
     *
     * @param location
     *            the address in the file where the data structure being
     *            encoded/decoded is located. This is only valid for files being
     *            decoded since the encoded file will not be written if an
     *            exception occurs.
     *
     * @param size
     *            the number of bytes that were expected to be encoded or
     *            decoded.
     *
     * @param difference
     *            the difference between the expected number of bytes and the
     *            actual number encoded or decoded.
     */
    public CoderException(final int location, final int size, final int difference) {
        super();
        start = location;
        length = size;
        delta = difference;
    }

    /**
     * Get the byte address of the start of the object that caused the
     * error.
     *
     * @return the location of the start of the encoded object when the error
     * occurred.
     */
    public int getStart() {
        return start;
    }

    /**
     * Get number of bytes the object was expected to occupy when encoded.
     *
     * @return get the number of bytes expected to be encoded or decoded.
     */
    public int getLength() {
        return length;
    }

    /**
     * Get the difference between the expected number of bytes and the
     * actual number of bytes encoded or decoded.
     *
     * @return the difference from the expected number of bytes.
     */
    public int getDelta() {
        return delta;
    }

    /**
     * Get a string representation of the error.
     *
     * @return the string describing the error.
     */
    @Override
    public String toString() {
        return String.format(FORMAT, Integer.toHexString(start), length, delta, getMessage());
    }
}
