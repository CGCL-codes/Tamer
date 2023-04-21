package net.sf.syracuse.request.impl;

import net.sf.syracuse.net.NetworkRequest;
import net.sf.syracuse.request.RequestException;
import net.sf.syracuse.request.RequestGlobals;
import net.sf.syracuse.request.RequestServicer;
import org.apache.commons.logging.Log;
import java.io.IOException;

public final class RequestRunner implements Runnable {

    private RequestServicer errorServicer;

    private Log log;

    private NetworkRequest networkRequest;

    private RequestGlobals requestGlobals;

    private RequestServicer requestServicer;

    public RequestRunner(NetworkRequest networkRequest, RequestServicer requestServicer, RequestServicer errorServicer) {
        this.networkRequest = networkRequest;
        this.requestServicer = requestServicer;
        this.errorServicer = errorServicer;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public NetworkRequest getNetworkRequest() {
        return networkRequest;
    }

    public void setRequestGlobals(RequestGlobals requestGlobals) {
        this.requestGlobals = requestGlobals;
    }

    public void run() {
        requestGlobals.store(networkRequest);
        try {
            requestServicer.service();
        } catch (RequestException e) {
            log.info("Handling exception", e);
            handleException(e);
        } catch (Exception e) {
            log.error("Unhandled exception caught", e);
            handleException(e);
        }
    }

    private void handleException(Exception e) {
        requestGlobals.store(e);
        try {
            errorServicer.service();
        } catch (Exception re) {
            log.error("Error while attempting to handle exception, closing connection", re);
            try {
                networkRequest.getChannel().close();
            } catch (IOException ioe) {
                log.info("Exception while closing SocketChannel", ioe);
            }
        }
    }
}
