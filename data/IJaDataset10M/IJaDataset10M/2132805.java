package com.nubotech.gwt.oss.client.s3;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Command;

/**
 *
 * @author jonnakkerud
 */
public class ChainedCallbackHandler extends AwsS3CallbackHandler {

    protected Command nextCommand;

    private RequestCallback wrappedCallback;

    public ChainedCallbackHandler(RequestCallback wrappedCallback) {
        this.wrappedCallback = wrappedCallback;
    }

    public void onResponseReceived(Request request, Response response) {
        wrappedCallback.onResponseReceived(request, response);
        if (hasError(response) == false && nextCommand != null) nextCommand.execute();
    }

    public void onError(Request request, Throwable exception) {
        wrappedCallback.onError(request, exception);
    }
}
