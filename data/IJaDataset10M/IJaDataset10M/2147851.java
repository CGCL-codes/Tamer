package org.t2framework.commons.transaction.exception;

import javax.transaction.SystemException;
import org.t2framework.commons.transaction.util.MessageFormatUtil;

public class CommonsSystemException extends SystemException {

    /**
	 * 
	 * {@.en }
	 * 
	 * <br />
	 * 
	 * {@.ja }
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public CommonsSystemException(String pattern) {
        super(MessageFormatUtil.getMessage(pattern));
    }
}
