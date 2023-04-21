package com.jetter.gomp.util;

/**
 * DataSource不存在时时发生的异常
 * 
 * @author Fellow
 *
 */
public class GOMDataSourceNotExistException extends GOMDataSourceException {

    public GOMDataSourceNotExistException() {
        super();
    }

    public GOMDataSourceNotExistException(String msg) {
        super(msg);
    }

    public GOMDataSourceNotExistException(String msg, Throwable cause) {
        super(msg);
    }

    public GOMDataSourceNotExistException(Throwable cause) {
        super(cause);
    }
}
