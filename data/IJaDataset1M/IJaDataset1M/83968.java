package com.ldl.bigframe.web.exception;

/**
 * ������Action���׳���ʾ��Ϣ�쳣����ת��info.jspҳ��
 * @author nandi.ldl
 *
 */
public class InfoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InfoException() {
        super();
    }

    public InfoException(String message) {
        super(message);
    }

    public InfoException(String message, Throwable cause) {
        super(message, cause);
    }

    public InfoException(Throwable cause) {
        super(cause);
    }
}
