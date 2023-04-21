package com.liferay.portlet.mail;

import com.liferay.portal.PortalException;

/**
 * <a href="StoreException.java.html"><b><i>View Source</i></b></a>
 *
 * @author Alexander Chow
 *
 */
public class StoreException extends PortalException {

    public StoreException() {
        super();
    }

    public StoreException(String msg) {
        super(msg);
    }

    public StoreException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public StoreException(Throwable cause) {
        super(cause);
    }
}
