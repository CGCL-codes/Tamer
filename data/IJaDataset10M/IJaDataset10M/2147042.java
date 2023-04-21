package com.jetter.gomp.util;

/**
 * ObjectCreator不存在时发生的异常
 * 
 * @author Fellow
 *
 */
public class GOMObjectCreatorNotExistException extends GOMException {

    public GOMObjectCreatorNotExistException() {
        super();
    }

    public GOMObjectCreatorNotExistException(String msg) {
        super(msg);
    }

    public GOMObjectCreatorNotExistException(String msg, Throwable cause) {
        super(msg);
    }

    public GOMObjectCreatorNotExistException(Throwable cause) {
        super(cause);
    }
}
