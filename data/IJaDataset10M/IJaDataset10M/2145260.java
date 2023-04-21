package org.gwanted.gwt.core.client.logger;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ResponseTextHandler;

/**
 * @author David Sanchez Fernandez
 */
public class HTTPRequestImpl extends com.google.gwt.user.client.impl.HTTPRequestImpl {

    public boolean asyncGet(final String user, final String pwd, final String url, final ResponseTextHandler handler) {
        return super.asyncGet(user, pwd, url, new ProxyResponseTextHandler(handler, TimeLogger.getInstance().getCurrentWidgetName()));
    }

    public boolean asyncPost(final String user, final String pwd, final String url, final String postData, final ResponseTextHandler handler) {
        return super.asyncPost(user, pwd, url, postData, new ProxyResponseTextHandler(handler, TimeLogger.getInstance().getCurrentWidgetName()));
    }
}

class ProxyResponseTextHandler implements ResponseTextHandler {

    private ResponseTextHandler handler = null;

    private final JavaScriptObject timer;

    private final String widgetName;

    /**
     * @param handler2
     *            The handler class that covers the original handler
     * @param time
     *            Start time of the server request
     */
    public ProxyResponseTextHandler(ResponseTextHandler handler2, String widgetName) {
        timer = TimeLogger.getInstance().getTimer();
        this.widgetName = widgetName;
        this.handler = handler2;
    }

    public void onCompletion(final String responseText) {
        TimeLogger.getInstance().endRequest(widgetName, timer);
        handler.onCompletion(responseText);
    }
}
