package com.persistent.appfabric.sample.salesdataclient.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map.Entry;
import com.persistent.appfabric.acs.Credentials;
import com.persistent.appfabric.common.AppFabricException;
import com.persistent.appfabric.servicebus.MessageBuffer;
import com.persistent.appfabric.servicebus.MessageBufferPolicy;

/**
 * This class sends HTTP GET, PUT and POST requests to message buffer
 */
public class ServiceBusRequest {

    String messageBufferName;

    MessageBufferPolicy messageBufferPolicy;

    private String proxyHost;

    private int proxyPort;

    private String solutionName;

    Credentials credentials;

    public static enum HttpVerbs {

        GET {

            public String toString() {
                return "get";
            }
        }
        , PUT {

            public String toString() {
                return "put";
            }
        }
        , POST {

            public String toString() {
                return "post";
            }
        }
        , DELETE {

            public String toString() {
                return "delete";
            }
        }

    }

    /**
	  * 	Constructor
     * Initializes the ServiceBusRequest class
     * @param messageBufferPolicy
     * @param proxyHost
     * @param proxyPort
     * @param solutionName
     * @param credentials
     * 
     * */
    public ServiceBusRequest(String messageBufferName, MessageBufferPolicy messageBufferPolicy, String proxyHost, int proxyPort, String solutionName, Credentials credentials) {
        super();
        this.messageBufferName = messageBufferName;
        this.messageBufferPolicy = messageBufferPolicy;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.solutionName = solutionName;
        this.credentials = credentials;
    }

    /**
     *Used to fire a GET request to message buffer
     *@param url 
     *@param httpverb
     *@param sessionId
     *@param headers
     *@param body 
     * */
    public PollingServiceResponse fireGetRequest(String url, HttpVerbs httpVerb, String sessionId, HashMap<String, String> headers, String body) throws AppFabricException {
        MessageBuffer msgBuffer;
        PollingServiceResponse responseObj = null;
        try {
            if (proxyHost != null) msgBuffer = new MessageBuffer(proxyHost, proxyPort, credentials, solutionName); else msgBuffer = new MessageBuffer(credentials, solutionName);
            String messageStr = xmlForPollingService(url, httpVerb, sessionId, headers, body, false);
            msgBuffer.sendMessage(messageBufferName, messageStr.toString());
            String retrievedMessage = msgBuffer.retrieveMessage(sessionId);
            responseObj = new PollingServiceResponse();
            String responseArray[] = retrievedMessage.split("<responseCode>");
            String response[] = responseArray[1].split("</responseCode>");
            responseObj.setResponseCode(Integer.parseInt(response[0]));
            String responseXML[] = response[1].split("</string>");
            responseObj.setResponse(responseXML[0]);
            return responseObj;
        } catch (AppFabricException e) {
            throw e;
        }
    }

    /**
     *Used to fire a POST request to message buffer
     *@param url 
     *@param httpverb
     *@param sessionId
     *@param headers
     *@param body 
     * */
    public String sendPostOrPutRequest(String url, HttpVerbs httpVerb, String sessionId, HashMap<String, String> headers, String body) throws AppFabricException {
        MessageBuffer msgBuffer;
        String retrievedMessage = "";
        try {
            if (proxyHost != null) msgBuffer = new MessageBuffer(proxyHost, proxyPort, credentials, solutionName); else msgBuffer = new MessageBuffer(credentials, solutionName);
            String messageStr = xmlForPollingService(url, httpVerb, sessionId, headers, body, false);
            msgBuffer.sendMessage(messageBufferName, messageStr.toString());
            retrievedMessage = msgBuffer.retrieveMessage(sessionId);
            return retrievedMessage;
        } catch (AppFabricException e) {
            throw e;
        }
    }

    /**
     *Used to format the message in xml format
     *@param url 
     *@param verb
     *@param messageBufferName
     *@param headers
     *@param body 
     * */
    public String xmlForPollingService(String url, HttpVerbs verb, String messageBufferName, HashMap<String, String> headers, String body, boolean isAuditLoggingXML) throws AppFabricException {
        String httpVerb = verb.toString();
        StringBuilder messageStr = new StringBuilder();
        messageStr.append("<request>");
        try {
            messageStr.append("<url>" + URLEncoder.encode(url, "UTF-8") + "</url>");
            messageStr.append("<verb>" + URLEncoder.encode(httpVerb, "UTF-8") + "</verb>");
            messageStr.append("<messageBufferName>" + URLEncoder.encode(messageBufferName, "UTF-8") + "</messageBufferName>");
            if (headers != null) {
                messageStr.append("<headers>");
                for (Entry<String, String> e : headers.entrySet()) {
                    messageStr.append("<header>");
                    messageStr.append("<name>" + e.getKey() + "</name>");
                    if (isAuditLoggingXML) {
                        messageStr.append("<value>" + e.getValue() + "</value>");
                    } else {
                        messageStr.append("<value>" + URLEncoder.encode(e.getValue(), "UTF-8") + "</value>");
                    }
                    messageStr.append("</header>");
                }
                messageStr.append("</headers>");
            }
            if (body != null) {
                messageStr.append("<body>" + URLEncoder.encode(body, "UTF-8") + "</body>");
            }
            messageStr.append("</request>");
        } catch (UnsupportedEncodingException e1) {
            throw new AppFabricException(e1.getMessage());
        }
        String xmlString = messageStr.toString();
        return xmlString;
    }
}
