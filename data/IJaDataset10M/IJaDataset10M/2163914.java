package com.timk.goserver.client.sgf;

/**
 * Used to signal problems while parsing an SGF file.
 * @author TKington
 *
 */
public class SGFParseException extends Exception {

    /** 
     * Creates a new instance of SGFParseException
     * @param msg the message
     */
    public SGFParseException(String msg) {
        super(msg);
    }

    /**
     * Creates an SGFParseException
     * @param msg the message
     * @param cause the root exception
     */
    public SGFParseException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
