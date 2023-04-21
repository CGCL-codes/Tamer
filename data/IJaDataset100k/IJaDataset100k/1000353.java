package com.angel.email.application;

import com.angel.common.keyValue.KeyValueResult;
import com.angel.email.connection.EmailConnection;

/**
 * @author William
 * @since 13/August/2009
 *
 */
public class ApplicationEmailConnections {

    private static ApplicationEmailConnections INSTANCE;

    private KeyValueResult connections;

    private ApplicationEmailConnections() {
        super();
    }

    public static synchronized ApplicationEmailConnections createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ApplicationEmailConnections();
        }
        return INSTANCE;
    }

    public void addEmailConnection(EmailConnection emailConnection) {
        connections.addKeyValue(emailConnection.getName(), emailConnection);
    }

    public void removeEmailConnection(EmailConnection emailConnection) {
        connections.removeKeyValue(emailConnection.getName());
    }

    public void removeEmailConnection(String emailConnectionName) {
        connections.removeKeyValue(emailConnectionName);
    }

    public boolean containsEmailConnection(EmailConnection emailConnection) {
        return connections.containsResultFor(emailConnection.getName());
    }

    public EmailConnection findEmailConnection(String name) {
        return (EmailConnection) connections.getResultFor(name);
    }
}
