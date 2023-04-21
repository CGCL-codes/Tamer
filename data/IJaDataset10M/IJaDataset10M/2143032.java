package com.liferay.portal.webdav;

import com.liferay.portal.PortalException;

/**
 * <a href="WebDAVException.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class WebDAVException extends PortalException {

    public WebDAVException() {
        super();
    }

    public WebDAVException(String msg) {
        super(msg);
    }

    public WebDAVException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public WebDAVException(Throwable cause) {
        super(cause);
    }
}
