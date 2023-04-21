package com.apelon.beans.apelapp;

import java.util.*;
import com.apelon.apelonserver.client.*;
import com.apelon.apelonserver.server.*;
import com.apelon.beans.apelconfig.*;
import com.apelon.beans.apelconnection.ApelConnectionItem;
import com.apelon.beans.apelconnection.ApelConnectionMgr;
import com.apelon.common.xml.XML;
import com.apelon.common.log4j.Categories;
import org.apache.log4j.Category;

/**
 * The default connection manager when connecting to a server over a socket.
 *
 * Creation date: (1/29/00 12:47:15 PM)
 * @author: Chris Hopkins
 */
public class ApelSocketConnMgr extends ServerConnMgr {

    private String fHost;

    private int fPort;

    private boolean authentication_required = false;

    private static final String USERNAME = "Username";

    private static final String PASSWORD = "Password";

    private static final String PORT = "Port";

    private static final String HOST = "Host";

    private static Category cat = Categories.ui();

    public ApelSocketConnMgr(ApelConfig ac) {
        super(ac);
        cat.debug("ApelSocketConnMgr.Constructor");
    }

    public void performConnection(Map map, boolean autoConnect, boolean useAsDefaults) throws Exception {
        cat.debug("ApelSocketConnMgr.performConnection()");
        if (map == null) {
            return;
        }
        fHost = (String) map.get(HOST);
        if (fHost == null || fHost.trim().equals("")) {
            throw new Exception("No host name or IP provided. Please provide a valid host.");
        }
        String portStr = (String) map.get(PORT);
        if (portStr == null || portStr.trim().equals("")) {
            throw new Exception("No port number provided. Please provide a valid port number.");
        }
        try {
            fPort = Integer.valueOf(portStr).intValue();
        } catch (NumberFormatException e) {
            throw new Exception("Please provide a valid port number.");
        }
        try {
            if (!authentication_required) {
                cat.debug("ApelSocketConnMgr.performConnection(): SOCKET MODE");
                fServerConnection = new ServerConnectionSocket(fHost, fPort);
            } else {
                cat.debug("ApelSocketConnMgr.performConnection(): SECURE SOCKET MODE");
                String username = (String) map.get(USERNAME);
                String password = (String) map.get(PASSWORD);
                cat.debug("ApelSocketConnMgr.performConnection(): fHost = " + fHost);
                cat.debug("ApelSocketConnMgr.performConnection(): portStr = " + portStr);
                cat.debug("ApelSocketConnMgr.performConnection(): username = " + username);
                cat.debug("ApelSocketConnMgr.performConnection(): password = " + password);
                if (username == null || username.trim().equals("")) {
                    throw new Exception("No username provided. Please provide a valid username.");
                }
                if (username != null) {
                    if ((username.indexOf('<') >= 0) || (username.indexOf('>') >= 0) || (username.indexOf('\\') >= 0) || (username.indexOf('/') >= 0)) {
                        throw new Exception("Cannot use characters <, >, / or \\ in username");
                    }
                }
                if (password == null || password.trim().equals("")) {
                    throw new Exception("No password provided. Please provide a valid password.");
                }
                fServerConnection = new ServerConnectionSecureSocket(fHost, fPort, username, password);
                if (useAsDefaults || autoConnect) {
                    fApelConfig.storeProperty("ssusername", username);
                }
                fApelConfig.storeProperty(ApelConfig.CONNECTED_USER, username);
                fApelConfig.storeProperty(ApelConfig.CONNECTED_PASSWORD, password);
            }
        } catch (ApelonException ex) {
            fServerConnection = null;
            StringBuffer connFailureMsg = new StringBuffer(128);
            connFailureMsg.append("<html>");
            connFailureMsg.append("A socket server connection could not be made.  ");
            connFailureMsg.append("<UL>");
            connFailureMsg.append("<li>The host may be invalid.");
            connFailureMsg.append("<li>The port may be invalid.");
            connFailureMsg.append("<li>The server may not be running.");
            connFailureMsg.append("</UL>");
            connFailureMsg.append("</html>");
            cat.error("Socket connection failed.", ex);
            throw new Exception(connFailureMsg.toString());
        }
        storeConnInfo(useAsDefaults, autoConnect, fHost, portStr);
    }

    /**
   * creates a connection and stores it for later use.
   *
   * !!!m this method should throw its exceptions, rather than attempt to show a dialog message
   * that may or may not be appropriate.
   *
   * @author: Matt Munz
   * @deprecated: use performConnection(Map map, boolean autoConnect, boolean useAsDefaults)
   */
    public void performConnection(String[] values, boolean autoConnect, boolean useAsDefaults) throws Exception {
        if (values == null || values.length < 3) return;
        String portStr = "";
        Map connectionPropertyMap = new HashMap();
        String keys[] = { HOST, PORT, USERNAME, PASSWORD };
        for (int i = 0; i < values.length; i++) {
            connectionPropertyMap.put(keys[i], values[i]);
        }
        performConnection(connectionPropertyMap, autoConnect, useAsDefaults);
    }

    public void performAutoConnection() throws Exception {
        String socketHost = fApelConfig.getProperty("socketHost");
        if (socketHost == null || socketHost.length() == 0) {
            String message = "Connection failed! Need socketHost in properties ";
            message += "file to autoConnect!";
            ApelApp.showErrorMsg(message, ApelApp.fAppJFrame);
            return;
        }
        String socketPort = fApelConfig.getProperty("socketPort");
        if (socketPort == null || socketPort.length() == 0) {
            String message2 = "Connection failed! Need socketPort in properties ";
            message2 += "file to autoConnect!";
            ApelApp.showErrorMsg(message2, ApelApp.fAppJFrame);
            return;
        }
        String auth = fApelConfig.getAuthentication();
        if (auth != null) authentication_required = new Boolean(auth).booleanValue();
        performConnection(new String[] { socketHost, socketPort, "" }, true, false);
    }

    public String getHost() {
        return fHost;
    }

    public int getPort() {
        return fPort;
    }

    public ApelConnectionItem[] getConnectionItems() {
        initDefaults();
        String auth = fApelConfig.getAuthentication();
        if (auth != null) authentication_required = new Boolean(auth).booleanValue();
        if (authentication_required) {
            return new ApelConnectionItem[] { new ApelConnectionItem(HOST, fApelConfig.getHost(), false), new ApelConnectionItem(PORT, Integer.toString(fApelConfig.getPort()), false), new ApelConnectionItem(USERNAME, fApelConfig.getSecureSocketUserName(), false), new ApelConnectionItem(PASSWORD, "", true) };
        } else {
            return new ApelConnectionItem[] { new ApelConnectionItem(HOST, fDefaults[0], false), new ApelConnectionItem(PORT, fDefaults[1], false) };
        }
    }

    /**
   * Called to initialize the defaults when we construct the manager using
   * the ApelConfig object
   *
   * Creation date: (5/18/2000 10:26:21 AM)
   * @author Chris Hopkins
   */
    protected void initDefaults() {
        fDefaults = new String[] { fApelConfig.getHost(), String.valueOf(fApelConfig.getPort()) };
    }
}
