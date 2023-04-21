package org.jwebsocket.plugins.rpc.util;

/**
 * Exception when a client try to call a method without the right
 * @author Quentin Ambard
 */
public class RPCRightNotGrantedException extends Exception {

    private String mMethod;

    private String mParameters;

    public RPCRightNotGrantedException() {
    }

    public RPCRightNotGrantedException(String aMethod, String aParameters) {
        mMethod = aMethod;
        mParameters = aParameters;
    }

    @Override
    public String getMessage() {
        return "the user does not have the right to call the rpc method " + mMethod + "(" + mParameters + ").";
    }
}
