package com.liferay.portlet.polls;

import com.liferay.portal.PortalException;

/**
 * <a href="QuestionExpirationDateException.java.html"><b><i>View Source</i></b>
 * </a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.4 $
 *
 */
public class QuestionExpirationDateException extends PortalException {

    public QuestionExpirationDateException() {
        super();
    }

    public QuestionExpirationDateException(String msg) {
        super(msg);
    }

    public QuestionExpirationDateException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public QuestionExpirationDateException(Throwable cause) {
        super(cause);
    }
}
