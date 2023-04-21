package net.sf.xclh.xcl;

import net.sf.xclh.XclhRuntimeException;

public class CacheException extends XclhRuntimeException {

    private static final long serialVersionUID = -6057277582500894840L;

    public CacheException() {
    }

    public CacheException(String message) {
        super(message);
    }

    public CacheException(Throwable cause) {
        super(cause);
    }

    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }
}
