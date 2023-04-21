package com.ibm.wala.shrikeCT;

/**
 * This exception is thrown when we detect that the incoming class file data was not a valid class file.
 */
public class InvalidClassFileException extends Exception {

    private static final long serialVersionUID = -6224203694783674259L;

    private final int offset;

    /**
   * The incoming class file is invalid.
   * 
   * @param offset the offset within the data where the invalidity was detected
   * @param s the reason the data is invalid
   */
    public InvalidClassFileException(int offset, String s) {
        super("Class file invalid at " + offset + ": " + s);
        this.offset = offset;
    }

    /**
   * @return the offset within the data where the problem was detected
   */
    public int getOffset() {
        return offset;
    }
}
