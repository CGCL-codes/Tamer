package com.arsenal.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.File;
import com.arsenal.log.Log;
import com.arsenal.message.IMessage;
import com.arsenal.rtcomm.server.*;
import com.arsenal.rtcomm.server.http.*;
import com.arsenal.util.ArsenalProperties;
import com.arsenal.util.SplashWindow;
import com.arsenal.observer.IObserver;
import com.arsenal.observer.Observer;
import com.arsenal.plugin.PlugInLoader;
import com.arsenal.session.SessionManager;
import com.arsenal.group.GroupManager;
import com.arsenal.user.UserManager;

public class Server implements IServer {

    private ConnectionManager connectionManager = null;

    private IObserver observer = Observer.getInstance();

    private String tmpDir = null;

    private String confDir = null;

    private MessageValidator messageValidator = new MessageValidator();

    private ArsenalProperties ap = ArsenalProperties.getInstance();

    private boolean validateMessages = true;

    private boolean showSplash = false;

    private static Server instance = null;

    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    public Server() {
    }

    public void start() {
        if (ap.getProperty("showSplash") == null) {
            ap.setProperty("showSplash", "false");
            ap.saveProperties();
            this.showSplash = false;
        }
        if (ap.getProperty("showSplash") == "true") this.showSplash = true;
        System.out.println("Welcome To Arsenal v.1.4");
        if (showSplash) {
            SplashWindow.getInstance().show();
            SplashWindow.getInstance().append("Welcome To Arsenal v.1.4");
            SplashWindow.getInstance().append("Configuration directory: " + System.getProperty("user.dir") + File.separator + "conf");
        }
        assignMessageValidation();
        if (showSplash) {
            if (validateMessages) SplashWindow.getInstance().append("Message Validation turned ON"); else SplashWindow.getInstance().append("Message Validation turned OFF");
        }
        Log.setConfigFilePath(System.getProperty("user.dir") + File.separator + "conf");
        if (showSplash) SplashWindow.getInstance().append("tmp directory: " + System.getProperty("user.dir") + File.separator + "tmp");
        tmpDir = System.getProperty("user.dir") + File.separator + "tmp";
        if (showSplash) SplashWindow.getInstance().append("Configuration directory: " + System.getProperty("user.dir") + File.separator + "conf");
        confDir = System.getProperty("user.dir") + File.separator + "conf";
        Log.info(this, "Register Core Components");
        if (showSplash) SplashWindow.getInstance().append("Register Core Components");
        registerCoreComponents();
        Log.info(this, "Start Loading Plugins");
        if (showSplash) SplashWindow.getInstance().append("Start Loading Plugins");
        PlugInLoader.getInstance().loadAllPlugIns(PlugInLoader.SERVER);
        debug("Finished Loading Plugins");
        if (showSplash) SplashWindow.getInstance().append("Finished Loading Plugins");
        Log.info(this, "Server finished loading plugins");
        Log.info(this, "Server - Attempting to start connection manager");
        if (showSplash) SplashWindow.getInstance().append("Attempting to start connection manager");
        Log.debug(this, System.getProperty("user.dir") + File.separator + "conf");
        connectionManager = new ConnectionManager((IServer) this, System.getProperty("user.dir") + File.separator + "conf");
        connectionManager.start();
        Log.info(this, "Server - Successfully started connection manager");
        Log.info(this, "Server initialization completed");
        if (showSplash) {
            SplashWindow.getInstance().append("Successfully started connection manager");
            SplashWindow.getInstance().append("Server initialization completed");
            SplashWindow.getInstance().hide();
        } else {
            Log.info(this, "Arsenal Server v1.4 has successfully started.");
            System.out.println("Arsenal Server v1.4 has successfully started.");
        }
        System.gc();
    }

    public void receive(ObjectInputStream in, ObjectOutputStream out) throws IOException {
        debug("receive: " + in + " " + out);
        try {
            while (true) {
                Log.debug(this, "receive(): attempt to read message");
                processMessage((IMessage) in.readObject());
                Log.debug(this, "receive(): message successfully read");
            }
        } catch (Exception e) {
            Log.debug(this, "receive(): " + e.getMessage(), e);
        }
    }

    public void receive(ObjectInputStream in) throws IOException {
        try {
            debug("recieve(): handle message");
            processMessage((IMessage) in.readObject());
            debug("receive(): done handling message");
        } catch (Exception e) {
            Log.debug(this, "receive(): " + e.getMessage(), e);
        }
    }

    public ConnectionManager getConnectionManager() {
        return this.connectionManager;
    }

    public void processMessage(IMessage message) {
        if (validateMessages) {
            if (!messageValidator.validateMessage(message)) return;
        }
        observer.sendNotify(message);
    }

    public static void main(String[] args) {
        Server s = Server.getInstance();
        s.start();
    }

    public void debug(String str) {
        Log.debug(this, str);
    }

    public void disconnectClientConnection(String key) {
        debug("disconnect client at: " + key);
        this.connectionManager.disconnectClient(key);
    }

    public void sendDisconnectMessageToUnauthenticatedClient(IMessage message, String key) {
        this.connectionManager.sendMessageToServerConnection(message, key);
        disconnectClientConnection(key);
    }

    public String getConfDir() {
        return this.confDir;
    }

    public String getTmpDir() {
        return this.tmpDir;
    }

    private void registerCoreComponents() {
        GroupManager.getInstance();
        GroupManager.getInstance().init();
        Observer.getInstance().registerListener("group", GroupManager.getInstance());
        UserManager.getInstance();
        UserManager.getInstance().init();
        Observer.getInstance().registerListener("user", UserManager.getInstance());
        SessionManager.getInstance();
        SessionManager.getInstance().init();
        Observer.getInstance().registerListener("session", SessionManager.getInstance());
    }

    private void assignMessageValidation() {
        if (ap.getProperty("messagevalidation") == null) {
            Log.info(this, "Messages to the server will not be validated");
            return;
        }
        boolean val = (new Boolean(ap.getProperty("messagevalidation"))).booleanValue();
        Log.info(this, "Messages to the server will be validated");
        this.validateMessages = val;
    }
}
