package com.liferay.portlet.tasks;

import com.liferay.portal.PortalException;

/**
 * <a href="DuplicateReviewUserIdException.java.html"><b><i>View Source</i></b>
 * </a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class DuplicateReviewUserIdException extends PortalException {

    public DuplicateReviewUserIdException() {
        super();
    }

    public DuplicateReviewUserIdException(String msg) {
        super(msg);
    }

    public DuplicateReviewUserIdException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public DuplicateReviewUserIdException(Throwable cause) {
        super(cause);
    }
}
