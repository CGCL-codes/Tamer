package com.csam.yocto;

/**
 *
 * @author Nathan Crause
 */
public class FunctionException extends OperationException {

    /**
     * Creates a new instance of <code>FunctionException</code> without detail message.
     */
    public FunctionException(SourceInfo sourceInfo) {
        super(sourceInfo);
    }

    /**
     * Constructs an instance of <code>FunctionException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public FunctionException(String msg, SourceInfo sourceInfo) {
        super(msg, sourceInfo);
    }
}
