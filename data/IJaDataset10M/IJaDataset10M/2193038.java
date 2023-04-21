package com.j2biz.blogunity.exception;

import com.j2biz.blogunity.i18n.I18N;
import com.j2biz.blogunity.i18n.I18NStatusFactory;

/**
 * @author michelson
 * @version $$
 * @since 0.1
 * 
 * 
 */
public class SystemException extends BlogunityException {

    /**
     * 
     */
    private static final long serialVersionUID = 3616447891057816371L;

    public SystemException() {
        super(I18NStatusFactory.create(I18N.ERRORS.SYSTEM_ERROR));
    }

    public SystemException(Throwable t) {
        super(I18NStatusFactory.create(I18N.ERRORS.SYSTEM_ERROR, t));
    }
}
