package org.frameworkset.web.demo;

/**
 * <p>Title: UserManagerException.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2011-1-9
 * @author biaoping.yin
 * @version 1.0
 */
public class UserManagerException extends Exception {

    public UserManagerException() {
        super();
    }

    public UserManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserManagerException(String message) {
        super(message);
    }

    public UserManagerException(Throwable cause) {
        super(cause);
    }
}
