package com.incesoft.botplatform.sdk.support;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.ByteBufferAllocator;
import org.apache.mina.common.DefaultIoFilterChainBuilder;
import org.apache.mina.common.IoConnector;
import org.apache.mina.common.IoServiceConfig;
import org.apache.mina.common.PooledByteBufferAllocator;
import org.apache.mina.common.ThreadModel;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.SocketConnector;
import org.apache.mina.transport.socket.nio.SocketSessionConfig;
import com.incesoft.botplatform.sdk.RobotServer;
import com.incesoft.botplatform.sdk.RobotServerFactory;
import com.incesoft.botplatform.sdk.protocol.FLAPCodecFactory;

/**
 * @author LiBo
 */
public class DefaultRobotServerFactory extends RobotServerFactory {

    private String defaultHost = null;

    private int defaultPort;

    private ThreadPoolExecutor threadPoolExecutor = null;

    private ScheduledThreadPoolExecutor scheduledExecutor = null;

    private IoConnector connector = null;

    private int getInt(String key, int defaultValue) {
        int value = defaultValue;
        try {
            value = Integer.parseInt(System.getProperty(key));
        } catch (Exception e) {
        }
        return value;
    }

    public DefaultRobotServerFactory() {
        defaultHost = System.getProperty("robotserver.host", "");
        defaultPort = getInt("robotserver.port", 6602);
        int mainpoolsize = getInt("robotserver.threadpoolfilter.poolsize", 1);
        int scheduledpoolsize = getInt("robotserver.scheduledexecutor.poolsize", 2);
        int processorCount = getInt("robotserver.ioprocessor.count", 1);
        int sendbufsize = getInt("robotserver.socket.sendbufsize", -1);
        int recvbufsize = getInt("robotserver.socket.recvbufsize", -1);
        boolean usedirectbuffers = "true".equals(System.getProperty("robotserver.bytebuffer.usedirectbuffers"));
        int timeout = getInt("robotserver.bytebuffer.timeout", 60);
        ByteBuffer.setUseDirectBuffers(usedirectbuffers);
        ByteBufferAllocator bufferAllocator = ByteBuffer.getAllocator();
        if (bufferAllocator instanceof PooledByteBufferAllocator) {
            ((PooledByteBufferAllocator) bufferAllocator).setTimeout(timeout);
        }
        connector = new SocketConnector(processorCount, Executors.newCachedThreadPool());
        IoServiceConfig connectorConfig = connector.getDefaultConfig();
        connectorConfig.setThreadModel(ThreadModel.MANUAL);
        SocketSessionConfig sessionConfig = (SocketSessionConfig) connectorConfig.getSessionConfig();
        if (sendbufsize > 0) sessionConfig.setSendBufferSize(sendbufsize);
        if (recvbufsize > 0) sessionConfig.setReceiveBufferSize(recvbufsize);
        DefaultIoFilterChainBuilder filterChainBuilder = connector.getDefaultConfig().getFilterChain();
        filterChainBuilder.addLast("protocol-filter", new ProtocolCodecFilter(new FLAPCodecFactory()));
        this.threadPoolExecutor = new ThreadPoolExecutor(mainpoolsize, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        this.scheduledExecutor = new ScheduledThreadPoolExecutor(scheduledpoolsize);
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                threadPoolExecutor.shutdown();
                scheduledExecutor.shutdown();
            }
        });
    }

    public RobotServer createRobotServer() {
        return createRobotServer(defaultHost, defaultPort);
    }

    public RobotServer createRobotServer(String host, int port) {
        DefaultRobotServer server = new DefaultRobotServer();
        server.setConnector(connector);
        server.setExecutor(threadPoolExecutor);
        server.setScheduledExecutor(scheduledExecutor);
        server.setHost(host);
        server.setPort(port);
        return server;
    }
}
