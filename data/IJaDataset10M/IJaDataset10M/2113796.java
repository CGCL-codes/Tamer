package org.rjam.net.impl.server;

import java.util.HashMap;
import java.util.Map;
import org.rjam.base.BaseThread;
import org.rjam.net.api.IConnection;
import org.rjam.net.api.server.IProcessor;
import org.rjam.net.api.server.IServer;

public abstract class AbstractProcessor extends BaseThread implements IProcessor {

    private static final long serialVersionUID = 1L;

    private IConnection connection;

    private IServer server;

    private Map<String, Object> sessionValues = new HashMap<String, Object>();

    public IServer getServer() {
        return server;
    }

    public void setServer(IServer server) {
        this.server = server;
    }

    public void setConnection(IConnection connection) {
        this.connection = connection;
    }

    public IConnection getConnection() {
        return connection;
    }

    public Map<String, Object> getSessionValues() {
        return sessionValues;
    }

    public void setSessionValues(Map<String, Object> sessionValues) {
        this.sessionValues = sessionValues;
    }

    public void setSessionValue(String name, Object value) {
        sessionValues.put(name, value);
    }

    public Object getSessionValue(String name) {
        return sessionValues.get(name);
    }

    public Object removeSessionValue(String name) {
        return sessionValues.remove(name);
    }

    public void setServerAttribute(String name, Object value) {
        getServer().setAttribute(name, value);
    }

    public Object getServerAttribute(String name) {
        return getServer().getAttribute(name);
    }

    public Object removeServerAttribute(String name) {
        return getServer().removeAttribute(name);
    }

    public Map<String, Object> getServerAttributes() {
        return getServer().getAttributes();
    }
}
