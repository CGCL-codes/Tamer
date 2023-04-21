package org.eralyautumn.common.model;

import java.net.InetAddress;
import java.util.List;
import me.prettyprint.cassandra.service.CassandraClient;
import me.prettyprint.cassandra.service.CassandraClientPool;
import me.prettyprint.cassandra.service.Keyspace;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.cassandra.client.RingCache;
import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.thrift.ConsistencyLevel;

public abstract class CassandraCommand<T> {

    private CassandraClientPool cassandraClientPool;

    private static RingCache ringCache;

    public CassandraCommand(CassandraClientPool cassandraClientPool) {
        this.cassandraClientPool = cassandraClientPool;
        if (ringCache == null) ringCache = new RingCache();
    }

    /**
	 * 重置ringCache 新加入节点时，调用此方法，使用ringCache保持最新的
	 */
    public static void resetRingCache() {
        ringCache = new RingCache();
    }

    /**
	 * Implement this abstract method to operate on cassandra.
	 * 
	 * the given keyspace is the keyspace referenced by
	 * {@link #execute(String, int, String)}. The method
	 * {@link #execute(String, int, String)} calls this method internally and
	 * provides it with the keyspace reference. <br> 
	 * for Test：<br> 
	 * String [] host ={"172.16.0.13:9160","172.16.0.14:9160"}; <br> 
	 * CassandraClient c = cassandraClientPool.borrowClient();<br> 
	 * 
	 * @param ks
	 * @return
	 * @throws Exception
	 */
    public abstract T execute(final Keyspace ks) throws Exception;

    protected final T execute(String keyspace, ConsistencyLevel consistency, String key) throws IllegalStateException, PoolExhaustedException, Exception {
        List<InetAddress> endPoints2 = ringCache.getEndPoint(keyspace, key);
        CassandraClient c = cassandraClientPool.borrowClient(endPoints2.get(0).getHostAddress(), DatabaseDescriptor.getThriftPort());
        Keyspace ks = c.getKeyspace(keyspace, consistency);
        try {
            return execute(ks);
        } finally {
            cassandraClientPool.releaseClient(ks.getClient());
        }
    }
}
