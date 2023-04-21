package org.openremote.controller.exception;

import org.openremote.controller.Constants;

/**
 * TODO
 * 
 * @author Dan 2009-4-30
 */
@SuppressWarnings("serial")
public class NoSuchPanelException extends ControlCommandException {

    public NoSuchPanelException() {
        super();
        setErrorCode(Constants.HTTP_RESPONSE_PANEL_ID_NOT_FOUND);
    }

    public NoSuchPanelException(String message) {
        super(message);
        setErrorCode(Constants.HTTP_RESPONSE_PANEL_ID_NOT_FOUND);
    }

    public NoSuchPanelException(Throwable cause) {
        super(cause);
        setErrorCode(Constants.HTTP_RESPONSE_PANEL_ID_NOT_FOUND);
    }

    public NoSuchPanelException(String message, Throwable cause) {
        super(message, cause);
        setErrorCode(Constants.HTTP_RESPONSE_PANEL_ID_NOT_FOUND);
    }
}
