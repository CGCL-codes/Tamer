package com.csam.yocto.parser;

/**
 *  Root exception for all parser errors
 *
 * @author Nathan Crause
 */
public class SyntaxException extends ParserException {

    /**
     * Constructs an instance of <code>ParserException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SyntaxException(String msg, ParseProgressInfo debugInfo) {
        super(msg, debugInfo);
    }
}
