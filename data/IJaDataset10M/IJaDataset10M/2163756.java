package com.liferay.portlet.shopping;

import com.liferay.portal.PortalException;

/**
 * <a href="NoSuchCartException.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class NoSuchCartException extends PortalException {

    public NoSuchCartException() {
        super();
    }

    public NoSuchCartException(String msg) {
        super(msg);
    }

    public NoSuchCartException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public NoSuchCartException(Throwable cause) {
        super(cause);
    }
}
