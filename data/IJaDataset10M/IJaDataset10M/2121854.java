package com.cidero.proxy;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Logger;
import com.cidero.http.*;
import com.cidero.util.MrUtil;
import com.cidero.upnp.UPnPException;

/**
 * HTTP server proxy class. Reads a single input stream from another HTTP
 * server and writes the data to one or more requesting devices. Has some
 * logic to (attempt to) synchronize devices of the same type.
 *
 * TODO: This version is slightly modified version of one in server
 * directory - bring them back in sync when done hacking
 *
 */
public class HTTPProxyServer implements HTTPRequestListener {

    private static Logger logger = Logger.getLogger("com.cidero.proxy");

    HTTPServerList serverList = null;

    Vector sessionList = new Vector();

    int port;

    int syncWaitMillisec;

    /**
   *  Constructor for the proxy. An HTTP server is started up on
   *  the specified port, and left running for the duration of the
   *  process  
   *
   *  @param  port  Port number of the proxy server
   *
   *  @param  syncWaitMillisec  
   *          Number of milliseconds to wait for multiple devices to join 
   *          synchronized device group.  0 to disable sync
   */
    public HTTPProxyServer(int port, int syncWaitMillisec) throws IOException {
        logger.fine("HTTPProxyServer: Opening server on port: " + port + " syncWaitMillisec: " + syncWaitMillisec);
        this.port = port;
        this.syncWaitMillisec = syncWaitMillisec;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setSyncWaitMillisec(int syncWaitMillisec) {
        this.syncWaitMillisec = syncWaitMillisec;
    }

    public void start() {
        serverList = new HTTPServerList();
        if (serverList.open(port) == false) {
            logger.severe("HTTPProxyServer: Error opening server on port: " + port);
            System.exit(-1);
        }
        serverList.addRequestListener(this);
        logger.fine("HTTPProxyServer: starting server ");
        serverList.start();
    }

    public void stop() {
        logger.fine("Stopping HTTPProxyServer");
        if (serverList != null) {
            serverList.stop();
            serverList.close();
            serverList = null;
        }
    }

    /**
   * Do clean shutdown of proxy server. This was added to clean up
   * after Soundbridge 'sketch' logic (make sure sketch mode is exited
   * on all listening soundbridges when server is exited)
   */
    public void close() {
        HTTPProxySession session;
        for (int n = 0; n < sessionList.size(); n++) {
            session = (HTTPProxySession) sessionList.get(n);
            session.close();
        }
    }

    /**
   *  Process HTTP GET request from media playback device. The GET request
   *  has encoded in it the name of the 'true' http address. For example,
   *  a UPnP server making use of this proxy will translate the URL
   * 
   *    http://64.236.34.196:80/stream/2001   (a typical internet radio addr) 
   *
   *    http://<proxyIP>:<proxyPort>/64.236.34.196:80/stream/2001
   *
   *    OR
   *
   *    http://<proxyIP>:<proxyPort>/opt:bitRate=16384/64.236.34.196:80/stream/2001
   *  
   *    if it chooses to add optional info in a comma-separated list
   *    (useful for sync proxy)
   *
   *  TODO: May need to substitute different char for ':' in the original
   *  address. I have seen some devices get confused when parsing URL's 
   *  with more than one ':'
   *
   *  @return  Returns true if server should close connection on return,
   *           false otherwise (to allow socket to continue to be used by
   *           another thread)
   */
    public boolean httpRequestReceived(HTTPRequest httpReq) {
        logger.fine("HTTPServer: httpRequestReceived");
        logger.fine(httpReq.toString());
        if (!httpReq.isGetRequest()) {
            logger.warning("Unsupported request type\n" + httpReq.toString());
            httpReq.returnBadRequest();
            return true;
        }
        logger.info("ProxyServer: Get Request, URL = " + httpReq.getResource());
        if (httpReq.getResource().equals("/SyncProxyPing")) {
            try {
                HTTPResponse response = new HTTPResponse();
                response.setStatusCode(HTTPStatus.OK);
                response.setPacketContent("Proxy is alive");
                HTTPConnection connection = httpReq.getConnection();
                connection.sendResponse(response);
            } catch (IOException e) {
                logger.warning("HTTPServer: Error sending response header");
            }
            return true;
        } else if (httpReq.getResource().equals("/SyncProxyStop")) {
            try {
                stopAllSessions();
                HTTPResponse response = new HTTPResponse();
                response.setStatusCode(HTTPStatus.OK);
                response.setPacketContent("Stopping all proxy sessions");
                HTTPConnection connection = httpReq.getConnection();
                connection.sendResponse(response);
            } catch (IOException e) {
                logger.warning("HTTPServer: Error sending response header");
            }
            return true;
        }
        HTTPProxySession session = null;
        logger.fine("userAgent = " + httpReq.getHeaderValue(HTTP.USER_AGENT));
        String host = httpReq.getHeaderValue(HTTP.HOST);
        if (host != null) logger.fine("HTTPServer: host = " + host);
        try {
            if (syncSupportedForDevice(httpReq.getHeaderValue(HTTP.USER_AGENT))) {
                session = getSession(httpReq, syncWaitMillisec);
            } else {
                session = createSession(httpReq, 0);
            }
        } catch (MalformedURLException e) {
            logger.warning("Error starting proxy session");
            httpReq.returnBadRequest();
            return true;
        } catch (IOException e) {
            logger.warning("Error starting proxy session");
            httpReq.returnBadRequest();
            return true;
        }
        HTTPResponse httpResponse = session.getHTTPResponse();
        if ((httpResponse == null) || (httpResponse.getStatusCode() != HTTPStatus.OK)) {
            httpReq.returnBadRequest();
            return true;
        }
        HTTPConnection connection = httpReq.getConnection();
        logger.fine("SENDBUFSIZE = " + connection.getSendBufferSize() + "RXBUFSIZE = " + connection.getReceiveBufferSize());
        connection.setTcpNoDelay(true);
        logger.fine("HTTPServer Response Header:\n" + httpResponse.toString());
        logger.fine("HTTPServer: Posting Header");
        try {
            connection.sendResponseHeader(httpResponse);
        } catch (IOException e) {
            logger.warning("HTTPServer: Error sending response header");
            return true;
        }
        BufferedOutputStream outStream = new BufferedOutputStream(connection.getOutputStream());
        logger.fine("Joining sync group ");
        SyncGroup syncGroup = session.joinSyncGroup(outStream, connection);
        if (syncGroup == null) {
            return false;
        }
        if (session.getSyncWaitMillisec() > 0) {
            logger.info(" Waiting " + session.getSyncWaitMillisec() / 1000 + " seconds waiting for other renderers to join session");
            MrUtil.sleep(session.getSyncWaitMillisec());
        }
        syncGroup.run();
        removeSession(session);
        return true;
    }

    /**
   * Certain devices have behavior that makes it hard to synchronize them.
   * Know problem devices hardcoded here.
   * 
   * Default is to assume that sync *will* work ok.  (return true)
   *
   */
    public boolean syncSupportedForDevice(String userAgent) {
        if (userAgent != null) {
            if (userAgent.toLowerCase().indexOf("redsonic") >= 0) return false;
        }
        return true;
    }

    /**
   * Create a new proxy session
   *
   * @param  syncWaitMillisec
   *         Number of seconds to wait for multiple devices to join 
   *         synchronized device group.  0 to disable sync
   */
    public synchronized HTTPProxySession createSession(HTTPRequest httpReq, int syncWaitMillisec) throws MalformedURLException, IOException {
        HTTPProxySession session = new HTTPProxySession(httpReq, syncWaitMillisec);
        sessionList.add(session);
        return session;
    }

    /**
   * Search for recently created proxy session with matching URL and
   * HTTP userAgent header (Assume same userAgent = same device type for now).
   * Create new session if match not found.
   * 
   */
    public synchronized HTTPProxySession getSession(HTTPRequest httpReq, int syncWaitMillisec) throws MalformedURLException, IOException {
        HTTPProxySession session = null;
        if (syncWaitMillisec > 0) session = findSession(httpReq, syncWaitMillisec);
        if (session == null) {
            logger.fine("No synchronous session found - creating new one");
            session = new HTTPProxySession(httpReq, syncWaitMillisec);
            sessionList.add(session);
        } else {
            logger.fine("HTTPProxyServer: Found matching session");
        }
        return session;
    }

    /**
   *  Find a session with a given resourceURL
   *
   *  @param   resourceURL
   *           Name of the resource on the UPnP server
   *
   *  @param   userAgent
   *           UserAgent of the HTTP client. This must match in order
   *           for session sharing to work (same device type)
   *
   *  @return  session or null if no session with specified URL pair exists
   *
   *  TODO: possible race condition between joining session and session
   *        starting up without the currently joining renderer - add lock
   */
    public synchronized HTTPProxySession findSession(HTTPRequest httpReq, long syncWaitMillisec) {
        logger.fine("Searching for session with resource " + httpReq.getResource());
        HTTPProxySession session = null;
        for (int n = 0; n < sessionList.size(); n++) {
            session = (HTTPProxySession) sessionList.get(n);
            if (session.getResourceURL().equals(httpReq.getResource()) && session.getUserAgent().equals(httpReq.getHeaderValue(HTTP.USER_AGENT))) {
                long currTime = System.currentTimeMillis();
                if ((currTime - session.getCreateTimeMillis()) < syncWaitMillisec) return session;
            }
        }
        logger.fine("No matching session found");
        return null;
    }

    /**
   * Force sessions to stop (break outgoing connections). 
   */
    public synchronized void stopAllSessions() {
        HTTPProxySession session = null;
        for (int n = 0; n < sessionList.size(); n++) {
            session = (HTTPProxySession) sessionList.get(n);
            session.stopSyncGroup();
        }
    }

    public synchronized void removeSession(HTTPProxySession session) {
        sessionList.remove(session);
    }

    /**
   * Get number of active sessions. This is one of the reported fields 
   * in the Web interface
   */
    public int getNumActiveSessions() {
        return sessionList.size();
    }
}
