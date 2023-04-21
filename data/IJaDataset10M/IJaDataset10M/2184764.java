package org.brainypdm.security.exception;

import org.brainypdm.exceptions.BaseException;
import org.brainypdm.main.msg.MessageCode;

/**
 * the brainy security exception
 * 
 * @author <a href="mailto:nico@brainypdm.org">Nico Bagari</a>
 * 
 */
public class BrainySecurityException extends BaseException {

    private static final long serialVersionUID = -7396350455786427075L;

    public BrainySecurityException(BaseException bex) {
        super(bex);
    }

    public BrainySecurityException(MessageCode aCode, Object... obj) {
        super(aCode, obj);
    }

    public BrainySecurityException(MessageCode aCode, String... obj) {
        super(aCode, obj);
    }

    public BrainySecurityException(MessageCode aCode, Throwable ex) {
        super(aCode, ex);
    }

    public BrainySecurityException(MessageCode aCode) {
        super(aCode);
    }
}
