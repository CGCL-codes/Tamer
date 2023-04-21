package org.apache.hadoop.ipc.metrics;

/**
 * 
 * This is the JMX management interface for the RPC layer.
 * Many of the statistics are sampled and averaged on an interval 
 * which can be specified in the metrics config file.
 * <p>
 * For the statistics that are sampled and averaged, one must specify 
 * a metrics context that does periodic update calls. Most do.
 * The default Null metrics context however does NOT. So if you aren't
 * using any other metrics context then you can turn on the viewing and averaging
 * of sampled metrics by  specifying the following two lines
 *  in the hadoop-meterics.properties file:
 *  <pre>
 *        rpc.class=org.apache.hadoop.metrics.spi.NullContextWithUpdateThread
 *        rpc.period=10
 *  </pre>
 *<p>
 * Note that the metrics are collected regardless of the context used.
 * The context with the update thread is used to average the data periodically
 *
 */
public interface RpcMgtMBean {

    /**
   * Number of RPC Operations in the last interval
   * @return number of operations
   */
    int getRpcOpsNumber();

    /**
   * Average time for RPC Operations in last interval
   * @return time in msec
   */
    long getRpcOpsAvgProcessingTime();

    /**
   * The Minimum RPC Operation Processing Time since reset was called
   * @return time in msec
   */
    long getRpcOpsAvgProcessingTimeMin();

    /**
   * The Maximum RPC Operation Processing Time since reset was called
   * @return time in msec
   */
    long getRpcOpsAvgProcessingTimeMax();

    /**
   * The Average RPC Operation Queued Time in the last interval
   * @return time in msec
   */
    long getRpcOpsAvgQueueTime();

    /**
   * The Minimum RPC Operation Queued Time since reset was called
   * @return time in msec
   */
    long getRpcOpsAvgQueueTimeMin();

    /**
   * The Maximum RPC Operation Queued Time since reset was called
   * @return time in msec
   */
    long getRpcOpsAvgQueueTimeMax();

    /**
   * Reset all min max times
   */
    void resetAllMinMax();

    /**
   * The number of open RPC conections
   * @return the number of open rpc connections
   */
    public int getNumOpenConnections();

    /**
   * The number of rpc calls in the queue.
   * @return The number of rpc calls in the queue.
   */
    public int getCallQueueLen();
}
