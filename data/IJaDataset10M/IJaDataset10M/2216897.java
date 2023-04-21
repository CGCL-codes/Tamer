package com.google.code.jahath.common.http.server;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.code.jahath.Connection;
import com.google.code.jahath.ConnectionClosedLocallyException;
import com.google.code.jahath.common.connection.Service;
import com.google.code.jahath.common.container.ExecutionEnvironment;
import com.google.code.jahath.common.http.HttpConstants;
import com.google.code.jahath.common.http.HttpException;
import com.google.code.jahath.common.http.HttpHeadersProvider;
import com.google.code.jahath.common.http.HttpMessage;
import com.google.code.jahath.common.http.HttpOutMessage;
import com.google.code.jahath.common.http.HttpOutputStream;
import com.google.code.jahath.common.io.ErrorListenerInputStream;
import com.google.code.jahath.common.io.ErrorListenerOutputStream;

public class HttpService implements Service, HttpHeadersProvider {

    private static final Logger log = Logger.getLogger(HttpService.class.getName());

    private final HttpRequestHandler requestHandler;

    public HttpService(HttpRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public void writeHeaders(HttpOutMessage message) throws HttpException {
        message.addHeader(HttpConstants.Headers.CONNECTION, "keep-alive");
    }

    public void handle(ExecutionEnvironment env, Connection connection) {
        try {
            ErrorListenerInputStream in = new ErrorListenerInputStream(connection.getInputStream());
            ErrorListenerOutputStream out = new ErrorListenerOutputStream(connection.getOutputStream());
            log.fine("New connection");
            while (true) {
                HttpRequest httpRequest = new HttpRequest(in, connection.isSecure());
                try {
                    if (!httpRequest.await()) {
                        break;
                    }
                } catch (ConnectionClosedLocallyException ex) {
                    break;
                }
                log.fine("Start processing new request");
                HttpOutputStream response = new HttpOutputStream(out);
                HttpResponse httpResponse = new HttpResponse(response, this);
                requestHandler.handle(env, httpRequest, httpResponse);
                log.fine("Finished processing request");
                if (in.isError() || out.isError()) {
                    log.fine("Closing connection because of previous error");
                    break;
                }
                if (httpRequest.getStatus() != HttpMessage.Status.COMPLETE || httpResponse.getStatus() != HttpMessage.Status.COMPLETE) {
                    log.warning("Closing connection because the handler didn't completely process the request (request status: " + httpRequest.getStatus() + ", response status: " + httpResponse.getStatus() + ")");
                    break;
                }
                response.flush();
            }
        } catch (Exception ex) {
            log.log(Level.SEVERE, "", ex);
        }
    }
}
