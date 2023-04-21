package com.jxva.i18n;

import com.jxva.exception.NestableRuntimeException;

/**
 *
 * @author  The Jxva Framework Foundation
 * @since   1.0
 * @version 2009-02-18 09:07:18 by Jxva
 */
public class I18NException extends NestableRuntimeException {

    private static final long serialVersionUID = -5356341184035030569L;

    public I18NException() {
        super();
    }

    public I18NException(Throwable root) {
        super(root);
    }

    public I18NException(String string, Throwable root) {
        super(string, root);
    }

    public I18NException(String s) {
        super(s);
    }
}
