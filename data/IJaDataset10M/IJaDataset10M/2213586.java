package org.i0o.webplus.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * 检查异常
 * 
 * @author <a href="mail:i0o@live.cn">i0o</a>
 */
public class BaseUnCheckedException extends NestedRuntimeException {

    public BaseUnCheckedException(String msg) {
        super(msg);
    }

    public BaseUnCheckedException(String msg, Throwable ex) {
        super(msg, ex);
    }

    private static final long serialVersionUID = 5095938390022719812L;
}
