package org.json.rpc.client;

public interface JsonRpcClientTransport {

    String call(String requestData) throws Exception;
}
