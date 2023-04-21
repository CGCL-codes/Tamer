package com.jxva.cache;

import com.jxva.exception.NestableRuntimeException;

/**
 *
 * @author  The Jxva Framework Foundation
 * @since   1.0
 * @version 2009-01-23 15:06:07 by Jxva
 */
public class CacheException extends NestableRuntimeException {

    private static final long serialVersionUID = -5356341184035030569L;

    public CacheException() {
        super();
    }

    public CacheException(Throwable root) {
        super(root);
    }

    public CacheException(String string, Throwable root) {
        super(string, root);
    }

    public CacheException(String s) {
        super(s);
    }
}
