package com.tensegrity.palowebviewer.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.palo.api.Connection;
import com.tensegrity.palowebviewer.modules.paloclient.client.XServer;

public class GlobalConnectionPool implements IConnectionPool {

    private final PaloConfiguration configuration;

    private Map pools = new HashMap();

    private IConnectionFactory factory;

    public GlobalConnectionPool(PaloConfiguration configurator) {
        this(configurator, new PaloConnectionFactory());
    }

    public GlobalConnectionPool(PaloConfiguration configurator, IConnectionFactory factory) {
        this.configuration = configurator;
        this.factory = factory;
        String[] servers = getServerNames();
        String[] services = getServerServices();
    }

    public String[] getServerServices() {
        final int size = configuration.getServers().size();
        String[] r = new String[size];
        for (int i = 0; i < size; i++) {
            PaloConfiguration.PaloServer server = (PaloConfiguration.PaloServer) configuration.getServers().get(i);
            r[i] = server.getService();
        }
        return r;
    }

    public String[] getServerNames() {
        final int size = configuration.getServers().size();
        String[] r = new String[size];
        for (int i = 0; i < size; i++) {
            PaloConfiguration.PaloServer server = (PaloConfiguration.PaloServer) configuration.getServers().get(i);
            r[i] = server.getHost();
        }
        return r;
    }

    public String[] getServerProviders() {
        final int size = configuration.getServers().size();
        String[] r = new String[size];
        for (int i = 0; i < size; i++) {
            PaloConfiguration.PaloServer server = (PaloConfiguration.PaloServer) configuration.getServers().get(i);
            r[i] = server.getProvider();
        }
        return r;
    }

    public Connection getConnection(String host, String service) {
        return getConnection(XServer.createId(host, service));
    }

    public Connection getConnection(String serverId) {
        PaloConfiguration.PaloServer server = configuration.getServer(serverId);
        ServerConnectionPool pool;
        synchronized (pools) {
            pool = (ServerConnectionPool) pools.get(serverId);
            if (pool == null) {
                String sProvider = server.getProvider();
                pool = new ServerConnectionPool(factory, server.getHost(), server.getService(), server.getLogin(), server.getPassword(), getMaxPoolConnections(), sProvider);
                pools.put(serverId, pool);
            }
        }
        return pool.getConnection();
    }

    protected int getMaxPoolConnections() {
        return configuration.getPoolMaxConnections();
    }

    public int getServerCount() {
        return configuration.getServers().size();
    }

    public void markForceReload() {
        for (Iterator it = pools.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            ServerConnectionPool serverPool = (ServerConnectionPool) entry.getValue();
            serverPool.markNeedReload();
        }
    }

    public IConnectionFactory getConnectionFactory() {
        return factory;
    }

    public void setConnectionFactrory(IConnectionFactory factory) {
        this.factory = factory;
    }
}
