package net.sf.gratepic.gui;

import net.sf.gratepic.gui.comp.StatusMessage;
import flickr.response.Error;
import flickr.service.FlickrException;
import java.util.MissingResourceException;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.util.actions.SystemAction;

/**
 * @author leon
 */
public class FlickrErrorHandler {

    private FlickrErrorHandler() {
    }

    private static void showMessage(String message, Exception exception, String messageKey) throws MissingResourceException {
        StatusMessage.show(NbBundle.getMessage(FlickrErrorHandler.class, messageKey, new Object[] { message, exception.getMessage() }));
    }

    public static void handle(String message, Exception exception) {
        if (exception instanceof FlickrException) {
            Error err = ((FlickrException) exception).getError();
            if (err == null) {
                showMessage(message, exception, "unknownErrorOccured");
            } else {
                String code = err.getCode();
                if (Error.NOT_LOGGED_IN.equals(code) || Error.USER_NOT_FOUND.equals(code) || Error.LOGIN_FAILED.equals(code)) {
                    ((CallableSystemAction) SystemAction.get(AuthorizeAction.class)).performAction();
                } else {
                    showMessage(message, exception, "flickrErrorOccured");
                }
            }
        } else {
            showMessage(message, exception, "unknownErrorOccured");
        }
    }
}
