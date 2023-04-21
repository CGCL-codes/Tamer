package com.csam.yocto;

/**
 *
 * @author Nathan Crause
 */
public class UnknownFunctionException extends FunctionException {

    /**
     * Creates a new instance of <code>UnknownFunctionException</code> without detail message.
     */
    public UnknownFunctionException(SourceInfo sourceInfo) {
        super(sourceInfo);
    }

    /**
     * Constructs an instance of <code>UnknownFunctionException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public UnknownFunctionException(String msg, SourceInfo sourceInfo) {
        super(msg, sourceInfo);
    }
}
