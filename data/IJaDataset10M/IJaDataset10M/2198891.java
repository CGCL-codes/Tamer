package com.nhncorp.usf.core.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * {@link ConnectorInfo} {@link Map}.
 *
 * @author Web Platform Development Team
 */
public final class ConnectorInfos {

    private String defaultConnector;

    private static ConnectorInfos instance = new ConnectorInfos();

    /**
     * Gets the single instance of ConnectorInfos.
     *
     * @return single instance of ConnectorInfos
     */
    public static ConnectorInfos getInstance() {
        return instance;
    }

    /**
     * Instantiates a new connector infos.
     */
    private ConnectorInfos() {
    }

    private Map<String, ConnectorInfo> connectorInfos = new HashMap<String, ConnectorInfo>();

    /**
     * Gets the connector info.
     *
     * @param connectorId the connector id
     * @return the connector info
     */
    public ConnectorInfo getConnectorInfo(String connectorId) {
        return connectorInfos.get(connectorId);
    }

    /**
     * Put connector info.
     *
     * @param connectorId   the connector id
     * @param connectorInfo the connector info
     */
    public void putConnectorInfo(String connectorId, ConnectorInfo connectorInfo) {
        connectorInfos.put(connectorId, connectorInfo);
    }

    /**
     * Gets the entrys.
     *
     * @return the entrys
     */
    public Set<Entry<String, ConnectorInfo>> getEntrys() {
        return connectorInfos.entrySet();
    }

    /**
     * Clear.
     */
    public void clear() {
        connectorInfos.clear();
    }

    /**
     * Gets the default connector.
     *
     * @return the default connector
     */
    public String getDefaultConnector() {
        return defaultConnector;
    }

    /**
     * Sets the default connector.
     *
     * @param defaultConnectorId the new default connector
     */
    public void setDefaultConnector(String defaultConnectorId) {
        this.defaultConnector = defaultConnectorId;
    }
}
