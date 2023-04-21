package org.pprun.common.monitor;

import com.mchange.v2.c3p0.PooledDataSource;
import java.sql.SQLException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;

public final class C3P0PooledDataSourceMonitorMXBean implements PoolDataSourceMonitorMXBean, Statistic {

    private static final Logger log = LoggerFactory.getLogger(C3P0PooledDataSourceMonitorMXBean.class);

    private final PooledDataSource pooledDataSource;

    public C3P0PooledDataSourceMonitorMXBean(PooledDataSource pooledDataSource) {
        this.pooledDataSource = pooledDataSource;
    }

    @Override
    @ManagedAttribute(description = "the number of Connections in the pool that are active (or said checked out)")
    public int getActiveConnections() {
        int activeConnections = Integer.MIN_VALUE;
        try {
            activeConnections = pooledDataSource.getNumBusyConnectionsDefaultUser();
        } catch (SQLException ex) {
            log.error("Failure to get ActiveConnections from C3P0", ex);
        }
        return activeConnections;
    }

    @Override
    @ManagedAttribute(description = "the number of Connections in the pool that are currently available for checkout")
    public int getIdleConnections() {
        int idleConnections = Integer.MIN_VALUE;
        try {
            idleConnections = pooledDataSource.getNumIdleConnectionsDefaultUser();
        } catch (SQLException ex) {
            log.error("Failure to get idleConnections from C3P0", ex);
        }
        return idleConnections;
    }

    @Override
    @ManagedAttribute(description = "the total number of Connections in the pool: The invariant idleConnections + activeConnections == totalConnections should always hold.")
    public int getTotalConnections() {
        int totalConnections = Integer.MIN_VALUE;
        try {
            totalConnections = pooledDataSource.getNumConnectionsDefaultUser();
        } catch (SQLException ex) {
            log.error("Failure to get totalConnections from C3P0", ex);
        }
        return totalConnections;
    }

    /**
     * In fact, this method should be {@literal synchronized}, but it is immutable.
     * 
     * @return
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("C3P0PooledDataSource=");
        s.append("{");
        s.append("activeConnections=").append(getActiveConnections()).append(", ");
        s.append("idleConnections=").append(getIdleConnections()).append(", ");
        s.append("totalConnections=").append(getTotalConnections());
        s.append("}");
        return s.toString();
    }

    /**
     * Marshall as JSON.
     * 
     * @param writer 
     */
    @Override
    public String asJson() throws Exception {
        ObjectMapper om = new ObjectMapper();
        return new StringBuilder("c3p0PoolMonitor=").append(om.writeValueAsString(this)).toString();
    }
}
