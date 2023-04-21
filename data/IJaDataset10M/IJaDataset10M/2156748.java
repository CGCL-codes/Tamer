package org.simpleframework.http.core;

import java.util.Map;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.StatusLine;

public interface Analyser {

    /**
    * This is used to compose a reques which will be used to send to the
    * container to handle. This is ultimately what ends up the the 
    * handle method for the analyser instance. It can be evaluated in
    * the handle method for correctness and used to check that all of 
    * the headers and data have been sent and parsed correctly.
    * 
    * @param address this is the request URI used to locate this  
    * @param header this is the header values sent in the request
    * @param body this is the body to be sent with the request
    */
    public void request(StringBuilder address, Message header, StringBuilder body) throws Exception;

    /** 
    * Here the internal server makes a callback to the analyser to handle 
    * a request. This should be teated like a real request in that there 
    * should be a reasonable response. The resulting response can then 
    * be analysed when the client receives it and gives it for analysis. 
    * 
    * @param req the request read from the network 
    * @param resp the response to send back to the client 
    * @param map this is used to share information across methods 
    */
    public void handle(Request req, Response resp, Map map) throws Exception;

    /** 
    * This is the actual response back over the network, rather than the 
    * details of the in server response object. This can be used to ensure 
    * that the server is sending back reasonable data over the network. 
    * 
    * @param status this is the status line of the response 
    * @param resp this is the response message 
    * @param body this is the body if any was send with the response 
    * @param map this is the map populated by the handle method 
    */
    public void analyse(StatusLine status, Message resp, Body body, Map map) throws Exception;
}
