package org.sempere.gwt.toolbox.remoting.client.rpc.callback;

import org.sempere.gwt.toolbox.core.client.common.ExceptionHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

public class CallbackExceptionHandler implements ExceptionHandler {

    /**
	 * Default class constructor
	 */
    public CallbackExceptionHandler() {
    }

    public void handleException(String message) {
        Window.alert(message);
    }

    public void handleException(Throwable throwable) {
        GWT.log("Handled exception.", throwable);
    }

    public void handleException(String message, Throwable throwable) {
        this.handleException(throwable);
        this.handleException(message);
    }
}
