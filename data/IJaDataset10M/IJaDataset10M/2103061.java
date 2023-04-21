package com.teracode.prototipogwt.frontend.newclient.support.rpc;

import org.fusesource.restygwt.client.JsonEncoderDecoder;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;

public abstract class RequestCallback<T> implements com.google.gwt.http.client.RequestCallback {

    private JsonEncoderDecoder<T> encoderdecoder;

    public RequestCallback(JsonEncoderDecoder<T> encoderdecoder) {
        this.encoderdecoder = encoderdecoder;
    }

    @Override
    public void onResponseReceived(Request request, Response response) {
        if (response.getStatusCode() == 0) {
            onError(request, new RuntimeException("Communication Error"));
        } else {
            JSONValue jsonValue = JSONParser.parseStrict(response.getText());
            onResponse(encoderdecoder.decode(jsonValue));
        }
    }

    @Override
    public void onError(Request request, Throwable exception) {
        Window.alert("Error:" + exception.getMessage());
    }

    public abstract void onResponse(T object);
}
