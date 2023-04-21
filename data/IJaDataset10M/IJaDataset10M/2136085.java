package edu.thu.tsinghua.iw.service.scheduler.exception;

/**
 * ����ִ��ʧ���쳣
 * @author Panda
 *
 */
public class ServiceExecFailException extends FlowException {

    private static final long serialVersionUID = 1L;

    public ServiceExecFailException() {
        super();
    }

    public ServiceExecFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceExecFailException(String message) {
        super(message);
    }

    public ServiceExecFailException(Throwable cause) {
        super(cause);
    }
}
