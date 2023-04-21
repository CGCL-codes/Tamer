package com.liferay.portal.upgrade;

import com.liferay.portal.PortalException;

/**
 * <a href="StagnantRowException.java.html"><b><i>View Source</i></b></a>
 *
 * @author Alexander Chow
 *
 */
public class StagnantRowException extends PortalException {

    public StagnantRowException(String msg) {
        super(msg);
    }

    public StagnantRowException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
