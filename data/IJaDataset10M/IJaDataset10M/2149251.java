package org.openjf.container;

import org.openjf.exception.BoardRuntimeException;

public class BoardContainerException extends BoardRuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8905822215132891929L;

    public BoardContainerException() {
        super();
    }

    public BoardContainerException(String message) {
        super(message);
    }

    public BoardContainerException(Throwable root) {
        super(root);
    }

    public BoardContainerException(String message, Throwable root) {
        super(message, root);
    }

    public BoardContainerException(String message, Object[] args, Throwable root) {
        super(message, args, root);
    }

    public BoardContainerException(String message, Object arg0, Throwable root) {
        super(message, arg0, root);
    }

    public BoardContainerException(String message, Object arg0, Object arg1, Throwable root) {
        super(message, arg0, arg1, root);
    }

    public BoardContainerException(String message, Object arg0, Object arg1, Object arg2, Throwable root) {
        super(message, arg0, arg1, arg2, root);
    }

    public BoardContainerException(String message, Object arg0, Object arg1, Object arg2, Object arg3, Throwable root) {
        super(message, arg0, arg1, arg2, arg3, root);
    }

    public BoardContainerException(String message, Object[] args) {
        super(message, args);
    }

    public BoardContainerException(String message, Object arg0) {
        super(message, arg0);
    }

    public BoardContainerException(String message, Object arg0, Object arg1) {
        super(message, arg0, arg1);
    }

    public BoardContainerException(String message, Object arg0, Object arg1, Object arg2) {
        super(message, arg0, arg1, arg2);
    }

    public BoardContainerException(String message, Object arg0, Object arg1, Object arg2, Object arg3) {
        super(message, arg0, arg1, arg2, arg3);
    }
}
