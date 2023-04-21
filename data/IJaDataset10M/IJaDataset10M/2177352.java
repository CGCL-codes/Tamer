package com.mytaobao.exception;

/**
 * 
 * 普通异常类
 * 
 * @author Administrator
 *
 */
public class CommonException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    public CommonException() {
        super();
    }

    public CommonException(String code, String message) {
        super(code, message);
    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonException(String message) {
        super(message);
    }

    public CommonException(Throwable cause) {
        super(cause);
    }
}
