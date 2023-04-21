package com.liferay.portlet.shopping;

import com.liferay.portal.PortalException;

/**
 * <a href="NoSuchItemException.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class NoSuchItemException extends PortalException {

    public NoSuchItemException() {
        super();
    }

    public NoSuchItemException(String msg) {
        super(msg);
    }

    public NoSuchItemException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public NoSuchItemException(Throwable cause) {
        super(cause);
    }
}
