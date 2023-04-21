package com.anzsoft.client.XMPP.impl;

import com.anzsoft.client.XMPP.Debugger;
import com.anzsoft.client.XMPP.XmppConnection;
import com.anzsoft.client.XMPP.XmppConnectionListener;
import com.anzsoft.client.XMPP.XmppEventAdapter;
import com.anzsoft.client.XMPP.XmppEventListener;
import com.anzsoft.client.XMPP.XmppFactory;
import com.anzsoft.client.XMPP.XmppInfoQueryListener;
import com.anzsoft.client.XMPP.XmppMessageListener;
import com.anzsoft.client.XMPP.XmppPacket;
import com.anzsoft.client.XMPP.XmppPacketListener;
import com.anzsoft.client.XMPP.XmppPresenceListener;
import com.anzsoft.client.XMPP.XmppStatus;
import com.anzsoft.client.XMPP.XmppUserSettings;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;

abstract class JsJacConnection implements XmppConnection {

    protected final JavaScriptObject connection;

    private final XmppFactory factory;

    public JsJacConnection(final XmppFactory factory, final JavaScriptObject connection) {
        this.factory = factory;
        this.connection = connection;
    }

    public void addConnectionListener(final XmppConnectionListener listener) {
        this.addEventListener(new XmppEventAdapter() {

            public void onConnect() {
                listener.onConnect();
            }

            public void onDisconnect() {
                listener.onDisconnect();
            }
        });
    }

    public void connect(final XmppUserSettings user, int delayMillis) {
        if (delayMillis <= 0) delayMillis = 1;
        Debugger.log("Trying to connect in " + delayMillis + " miliseconds.");
        connect(user);
    }

    public native void addEventListener(XmppEventListener listener);

    public native void addInfoQueryListener(XmppInfoQueryListener listener);

    public native void addMessageListener(XmppMessageListener listener);

    public native void addPacketListener(XmppPacketListener listener);

    public native void addPreseceListener(XmppPresenceListener listener);

    public abstract void connect(XmppUserSettings user) throws JavaScriptException;

    public abstract void disconnect() throws JavaScriptException;

    public JavaScriptObject getDelegate() {
        return connection;
    }

    public XmppFactory getFactory() {
        return factory;
    }

    public native int getPollInterval();

    public XmppStatus getStatus() {
        return XmppStatus.getStatus(getStatusID());
    }

    public native String getStatusID();

    public native boolean isConnected();

    /**
	 * Resumes this connection from saved state (cookie)
	 */
    public native void resume();

    public native void send(XmppPacket packet) throws JavaScriptException;

    public native void send(final XmppPacket packet, final XmppPacketListener listener);

    /**
	 * Suspsends this connection (saving state for later resume)
	 * 
	 * @throws JavaScriptException
	 */
    public native void suspend() throws JavaScriptException;
}
