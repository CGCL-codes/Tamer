package com.google.code.javascribd.connection;

import java.io.IOException;
import java.net.Proxy;

public interface ScribdConnection {

    public void setProxy(Proxy proxy);

    public void setUrl(String apiUrl);

    public Proxy getProxy();

    public String getUrl();

    public <T extends ScribdResponse> T postRequest(ScribdMethod<T> method, ScribdResponseParser interpretor) throws ScribdConnectionException, IOException;

    public <T extends ScribdResponse> T getRequest(ScribdMethod<T> method, ScribdResponseParser interpretor) throws ScribdConnectionException, IOException;
}
