package org.gingkgo.core;

/**
 * 
 * @author <a herf="mailto:zhangsitao@gmail.com">Steven Zhang</a>
 * @version $Revision: 1.1 $ $Date: 2007/05/17 05:20:55 $
 */
public class GingkgoException extends Exception {

    private static final long serialVersionUID = 5373936974632127167L;

    protected Exception exception;

    public GingkgoException(String message) {
        super(message);
    }

    public GingkgoException(Exception exception) {
        super(exception.getMessage());
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }
}
