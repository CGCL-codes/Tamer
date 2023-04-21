package com.liferay.portal;

/**
 * <a href="NoSuchCompanyException.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.5 $
 *
 */
public class NoSuchCompanyException extends PortalException {

    public NoSuchCompanyException() {
        super();
    }

    public NoSuchCompanyException(String msg) {
        super(msg);
    }

    public NoSuchCompanyException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public NoSuchCompanyException(Throwable cause) {
        super(cause);
    }
}
