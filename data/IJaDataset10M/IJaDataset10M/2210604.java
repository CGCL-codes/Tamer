package com.liferay.portlet.polls;

import com.liferay.portal.PortalException;

/**
 * <a href="QuestionExpiredException.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class QuestionExpiredException extends PortalException {

    public QuestionExpiredException() {
        super();
    }

    public QuestionExpiredException(String msg) {
        super(msg);
    }

    public QuestionExpiredException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public QuestionExpiredException(Throwable cause) {
        super(cause);
    }
}
