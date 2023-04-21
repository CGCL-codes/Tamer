package com.liferay.portlet.tags;

import com.liferay.portal.PortalException;

/**
 * <a href="DuplicateEntryException.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class DuplicateEntryException extends PortalException {

    public DuplicateEntryException() {
        super();
    }

    public DuplicateEntryException(String msg) {
        super(msg);
    }

    public DuplicateEntryException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public DuplicateEntryException(Throwable cause) {
        super(cause);
    }
}
