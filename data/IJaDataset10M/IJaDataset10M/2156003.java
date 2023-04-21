package whf.framework.meta.util;

import whf.framework.exception.AppException;

/**
 * @author wanghaifeng
 *
 */
public class MetaInitException extends AppException {

    public MetaInitException(Throwable cause) {
        super(cause);
    }

    public MetaInitException(String msg) {
        super(msg);
    }
}
