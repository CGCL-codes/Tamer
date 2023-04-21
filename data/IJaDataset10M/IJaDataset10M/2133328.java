package edu.thu.tsinghua.iw.service.scheduler.exception;

/**
 * ����ķ��ؽ��淶
 * @author Panda
 *
 */
public class IllegalServiceResultException extends FlowException {

    private static final long serialVersionUID = 1L;

    public IllegalServiceResultException() {
        super();
    }

    public IllegalServiceResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalServiceResultException(String message) {
        super(message);
    }

    public IllegalServiceResultException(Throwable cause) {
        super(cause);
    }
}
