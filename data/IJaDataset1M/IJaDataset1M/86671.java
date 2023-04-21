package org.json.rpc.server;

public interface JsonRpcServerTransport {

    String readRequest() throws Exception;

    void writeResponse(String responseData) throws Exception;
}
