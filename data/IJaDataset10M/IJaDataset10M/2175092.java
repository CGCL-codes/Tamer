package com.liferay.portal;

import org.apache.commons.lang.exception.NestableException;

/**
 * <a href="PortalException.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.4 $
 *
 */
public class PortalException extends NestableException {

    public PortalException() {
        super();
    }

    public PortalException(String msg) {
        super(msg);
    }

    public PortalException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public PortalException(Throwable cause) {
        super(cause);
    }
}
