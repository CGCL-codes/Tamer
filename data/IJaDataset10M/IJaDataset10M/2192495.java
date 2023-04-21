package com.teletalk.jserver.tcp.http.proxy;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import com.teletalk.jserver.SubSystem;
import com.teletalk.jserver.tcp.TcpEndPointIdentifier;
import com.teletalk.jserver.tcp.http.HttpConstants;
import com.teletalk.jserver.tcp.http.HttpRequest;
import com.teletalk.jserver.tcp.http.HttpResponse;
import com.teletalk.jserver.tcp.messaging.Message;
import com.teletalk.jserver.tcp.messaging.MessageDispatchFailedException;
import com.teletalk.jserver.tcp.messaging.MessagingManager;

/**
 * Client interface class used to communicate with a {@link HttpProxy}. To use this class a subclass must be created, 
 * which implements the method {@link #handleRequest(HttpRequest)}. This method is called every time a request is 
 * received from the proxy.
 *  
 * @since 1.2
 * 
 * @author Tobias L�fstrand
 */
public class HttpProxyClient extends MessagingManager {

    /**
	 * Creates a new HttpProxyClient.
	 * 
	 * @param parent the parent subsystem of this HttpProxyClient.
	 */
    public HttpProxyClient(SubSystem parent) {
        this(parent, "HttpProxyClient");
    }

    /**
    * Creates a new HttpProxyClient.
    * 
    * @param parent the parent subsystem of this HttpProxyClient.
    * @param messagingPort the port number of the HTTP proxy.
    */
    public HttpProxyClient(SubSystem parent, int messagingPort) {
        this(parent, "HttpProxyClient", messagingPort);
    }

    /**
	 * Creates a new HttpProxyClient.
	 * 
	 * @param parent the parent subsystem of this HttpProxyClient.
	 * @param name the name of this HttpProxyClient.
	 */
    public HttpProxyClient(SubSystem parent, String name) {
        this(parent, name, (TcpEndPointIdentifier[]) null);
    }

    /**
    * Creates a new HttpProxyClient that will attempt to connect to a proxy at the specified address.
    * 
    * @param parent the parent subsystem of this HttpProxyClient.
    * @param name the name of this HttpProxyClient.
    * @param messagingPort the port number of the HTTP proxy.
    */
    public HttpProxyClient(SubSystem parent, String name, int messagingPort) {
        this(parent, name, new TcpEndPointIdentifier[] { new TcpEndPointIdentifier(messagingPort) });
    }

    /**
	 * Creates a new HttpProxyClient that will attempt to connect to a proxy at the specified address.
	 * 
	 * @param parent the parent subsystem of this HttpProxyClient.
	 * @param name the name of this HttpProxyClient.
	 * @param proxyAddress an address to a HTTP proxy.
	 */
    public HttpProxyClient(SubSystem parent, String name, TcpEndPointIdentifier proxyAddress) {
        this(parent, name, new TcpEndPointIdentifier[] { proxyAddress });
    }

    /**
	 * Creates a new HttpProxyClient that will attempt to connect to proxies at the specified addresses.
	 * 
	 * @param parent the parent subsystem of this HttpProxyClient.
	 * @param name the name of this HttpProxyClient.
	 * @param proxyAddresses addresses to HTTP proxies.
	 */
    public HttpProxyClient(SubSystem parent, String name, TcpEndPointIdentifier[] proxyAddresses) {
        super(parent, name, proxyAddresses, null);
    }

    /**
	 * Called when a message is received from the proxy. This method extracts a HttpRequest from the message and 
	 * calls the method {@link #handleRequest(HttpRequest)}, and finally returns the response to the proxy.
	 * 
	 * @param message the received message.
	 */
    public final void messageReceived(final Message message) {
        HttpRequest request = null;
        HttpResponse response = null;
        try {
            request = new HttpRequest(message.getBodyAsStream());
            if (super.isDebugMode()) logDebug("Received request: " + request + ".");
            try {
                response = this.handleRequest(request);
            } catch (Throwable t) {
                logError("Error occurred while handling request " + request + ".", t);
                response = generateInternalServerErrorResponse("Internal error - " + t);
            }
            InputStream responseInputStream;
            final byte[] responseMessageData = response.getResponseMessage().getBytes(HttpConstants.DEFAULT_CHARACTER_ENCODING);
            final ByteArrayInputStream responseMessageDataInputStream = new ByteArrayInputStream(responseMessageData);
            if (super.isDebugMode()) logDebug("Returning response (" + response + ") for request: " + request + ".");
            if (response.length() > 0) {
                InputStream bodyInputStream = response.getBodyInputStream();
                if (bodyInputStream == null) {
                    bodyInputStream = new ByteArrayInputStream(response.getBody());
                }
                responseInputStream = new SequenceInputStream(responseMessageDataInputStream, bodyInputStream);
            } else {
                responseInputStream = responseMessageDataInputStream;
            }
            super.dispatchMessageAsync(message.getHeader(), responseInputStream, (responseMessageData.length + response.length()));
        } catch (MessageDispatchFailedException mdfe) {
            logWarning("Unable to send response (" + response + ") to request (" + request + ").", mdfe);
        } catch (Exception e) {
            logError("Error occurred while sending response (" + response + ") to request (" + request + ").", e);
        }
    }

    /**
	 * Method for handling incomming HTTP requests received from the proxy.
	 * 
	 * @param request a HTTP request received from a client.
	 * 
	 * @return the HTTP response that should be returned to the client (though the proxy) that sent the request.
	 */
    public HttpResponse handleRequest(final HttpRequest request) {
        return generateInternalServerErrorResponse("Bad configuration, no request handler found - unable to server request " + request.getPath() + ".");
    }

    /**
    * Generates an error response.
    */
    private HttpResponse generateInternalServerErrorResponse(final String responseMsg) {
        HttpResponse rsp;
        String msg;
        msg = "<html><body><h1>500 Internal Server Error</h1>";
        msg += responseMsg;
        msg += "</body></html>";
        rsp = new HttpResponse(500, "Internal server error", msg);
        rsp.setContentType("text/html");
        return rsp;
    }
}
