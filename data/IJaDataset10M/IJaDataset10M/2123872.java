package com.liferay.portlet.softwarecatalog;

import com.liferay.portal.PortalException;

/**
 * <a href="NoSuchProductEntryException.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class NoSuchProductEntryException extends PortalException {

    public NoSuchProductEntryException() {
        super();
    }

    public NoSuchProductEntryException(String msg) {
        super(msg);
    }

    public NoSuchProductEntryException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public NoSuchProductEntryException(Throwable cause) {
        super(cause);
    }
}
