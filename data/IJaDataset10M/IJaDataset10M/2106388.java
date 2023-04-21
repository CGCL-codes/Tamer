package com.dmoving.log.exceptions;

/**
 * @author ���ú����
 * @version 0.1
 * The exception means that the log digger can't be used anymore.It
 * will hang or be terminated.
 *  
 *
 */
public class FatalErrException extends AppBaseException {

    public FatalErrException(Throwable cause) {
        super(cause);
    }

    public FatalErrException() {
        super();
    }

    public FatalErrException(String msgKey, Object[] args) {
        super(msgKey, args);
    }

    public FatalErrException(String detailMsg) {
        super(detailMsg);
    }
}
