package org.openremote.web.console.client.rpc;

import org.openremote.web.console.client.utils.ORRoundRobin;
import org.openremote.web.console.client.window.LoginWindow;
import org.openremote.web.console.exception.ControllerExceptionMessage;
import org.openremote.web.console.exception.NotAuthenticatedException;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Inherited from {@link AsyncCallback}. For global error handle.
 * 
 * @param <T> 
 * 
 */
public abstract class AsyncSuccessCallback<T> implements AsyncCallback<T> {

    /**
    * If not authenticated, show login window. If controller occured unknown error, switch to a group member.
    * Otherwise alert the error information.
    * If you want to custom the failure, you can just override this method in its subclass.
    * 
    * @param caught the caught
    * 
    * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
    */
    public void onFailure(Throwable caught) {
        if (caught instanceof NotAuthenticatedException) {
            new LoginWindow();
        } else {
            if (ControllerExceptionMessage.exceptionMessageOfCode(506).equals(caught.getMessage())) {
                MessageBox.alert("ERROR", caught.getMessage(), new Listener<MessageBoxEvent>() {

                    public void handleEvent(MessageBoxEvent be) {
                        Window.Location.reload();
                    }
                });
            } else if (ControllerExceptionMessage.exceptionMessageOfCode(504).equals(caught.getMessage())) {
                MessageBox.alert("ERROR", caught.getMessage(), null);
            } else {
                MessageBox.alert("ERROR", caught.getMessage(), new Listener<MessageBoxEvent>() {

                    public void handleEvent(MessageBoxEvent be) {
                        ORRoundRobin.doSwitch();
                    }
                });
            }
        }
    }

    /**
    * onSuccess.
    * 
    * @param result the result
    * 
    * @see com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object)
    */
    public abstract void onSuccess(T result);
}
