package com.google.code.javascribd.connection;

import java.util.Map;

public interface ScribdMethod<T extends ScribdResponse> {

    public String getMethodName();

    public Class<T> getResponseType();

    public String getGETParametersForURL();

    public boolean hasPOSTParameters();

    public Map<String, StreamableData> getPOSTParameters();
}
