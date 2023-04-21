package com.quikj.ace.web.client.comm;

import com.quikj.ace.messages.vo.app.WebMessage;

public interface Server {

    public abstract void connect();

    public abstract void disconnect();

    public abstract void setRequestListener(RequestListener requestListener);

    public abstract int sendRequest(WebMessage request, String contentType, boolean multipleResponse, long timeout, ResponseListener responseListener);

    public abstract void cancelRequest(int requestId);

    public abstract void sendResponse(int requestId, int status, String reason, String contentType, WebMessage response);

    public abstract boolean isConnected();

    public abstract void changeTimeout(int requestId, long timeout);
}
