package com.liferay.portlet.expando;

import com.liferay.portal.PortalException;

/**
 * <a href="NoSuchColumnException.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class NoSuchColumnException extends PortalException {

    public NoSuchColumnException() {
        super();
    }

    public NoSuchColumnException(String msg) {
        super(msg);
    }

    public NoSuchColumnException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public NoSuchColumnException(Throwable cause) {
        super(cause);
    }
}
