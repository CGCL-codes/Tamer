package com.acv.common.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @deprecated
 *
 */
public class ConnectorSessionContext implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, ConnectorSession> connectorSessions = new HashMap<String, ConnectorSession>();

    public ConnectorSessionContext() {
    }

    public ConnectorSession find(Class clazz) {
        return connectorSessions.get(clazz.getCanonicalName());
    }

    public void set(ConnectorSession session) {
        if (session == null) {
            return;
        }
        connectorSessions.put(session.getClass().getCanonicalName(), session);
    }
}
