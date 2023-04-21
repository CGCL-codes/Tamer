package net.cyan.activex;

/**
 * <p>Title: activeX异常</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author ccs
 * @version 1.0
 */
public class ActiveXException extends Exception {

    public ActiveXException() {
    }

    public ActiveXException(String message) {
        super(message);
    }

    public ActiveXException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActiveXException(Throwable cause) {
        super(cause);
    }
}
