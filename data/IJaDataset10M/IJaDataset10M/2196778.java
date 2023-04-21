package org.acid3lib;

/**
 * Thrown to indicate that a tag's version isn't supported by this library.
 * 
 * @author Jascha Ulrich
 */
public class ID3v2UnsupportedVersionException extends ID3v2ParseException {

    public ID3v2UnsupportedVersionException() {
        super();
    }

    public ID3v2UnsupportedVersionException(String s) {
        super(s);
    }
}
