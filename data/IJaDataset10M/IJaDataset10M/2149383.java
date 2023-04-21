package org.jdj.exception;

/**
 * 过滤系统根异常
 * @author gavin
 *
 */
public class FilterException extends JDException {

    private static final long serialVersionUID = 1L;

    public FilterException() {
        super();
    }

    public FilterException(String message, Throwable cause) {
        super(message, cause);
    }

    public FilterException(String message) {
        super(message);
    }
}
