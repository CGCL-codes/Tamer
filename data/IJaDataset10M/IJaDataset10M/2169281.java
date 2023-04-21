package net.sf.rcer.conn.connections;

import net.sf.rcer.conn.locales.Locale;

/**
 * A wrapper for the connection data provided by a {@link IConnectionProvider} to ensure that
 * the provider ID is supplied with the connection ID.
 * @author vwegert
 *
 */
public class ProvidedConnectionData implements IConnectionData {

    /**
	 * The ID of the connection data provider.
	 */
    private String providerID;

    /**
	 * The wrapped connection. 
	 */
    private IConnectionData connectionData;

    /**
	 * @param providerID
	 * @param connection
	 */
    public ProvidedConnectionData(String providerID, IConnectionData connection) {
        super();
        this.providerID = providerID;
        this.connectionData = connection;
    }

    public String getApplicationServer() {
        return connectionData.getApplicationServer();
    }

    public String getConnectionDataID() {
        return providerID + "#" + connectionData.getConnectionDataID();
    }

    public String getDefaultClient() {
        return connectionData.getDefaultClient();
    }

    public Locale getDefaultLocale() {
        return connectionData.getDefaultLocale();
    }

    public String getDefaultUser() {
        return connectionData.getDefaultUser();
    }

    public String getDescription() {
        return connectionData.getDescription();
    }

    public String getLoadBalancingGroup() {
        return connectionData.getLoadBalancingGroup();
    }

    public String getMessageServer() {
        return connectionData.getMessageServer();
    }

    public int getMessageServerPort() {
        return connectionData.getMessageServerPort();
    }

    public String getRouter() {
        return connectionData.getRouter();
    }

    public String getSystemID() {
        return connectionData.getSystemID();
    }

    public int getSystemNumber() {
        return connectionData.getSystemNumber();
    }

    public boolean isDefaultClientEditable() {
        return connectionData.isDefaultClientEditable();
    }

    public boolean isDefaultLocaleEditable() {
        return connectionData.isDefaultLocaleEditable();
    }

    public boolean isDefaultUserEditable() {
        return connectionData.isDefaultUserEditable();
    }

    public boolean isDirectConnection() {
        return connectionData.isDirectConnection();
    }

    public boolean isLoadBalancedConnection() {
        return connectionData.isLoadBalancedConnection();
    }

    public ConnectionType getConnectionType() {
        return connectionData.getConnectionType();
    }

    @Override
    public String toString() {
        return connectionData.toString();
    }
}
