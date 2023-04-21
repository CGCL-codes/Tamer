package com.liferay.portlet.shopping;

import com.liferay.portal.PortalException;

/**
 * <a href="CartMinOrderException.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class CartMinOrderException extends PortalException {

    public CartMinOrderException() {
        super();
    }

    public CartMinOrderException(String msg) {
        super(msg);
    }

    public CartMinOrderException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CartMinOrderException(Throwable cause) {
        super(cause);
    }
}
