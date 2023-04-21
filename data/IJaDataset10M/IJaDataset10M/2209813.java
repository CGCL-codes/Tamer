package com.googlecode.jazure.sdk.endpoint.mina;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.googlecode.jazure.sdk.core.Console;
import com.googlecode.jazure.sdk.endpoint.QueueStorageEndpoint;
import com.googlecode.jazure.sdk.endpoint.mina.codec.TaskInvocationCodecFactory;
import com.googlecode.jazure.sdk.event.EventListener;
import com.googlecode.jazure.sdk.lifecycle.LifeCycleWrapper;
import com.googlecode.jazure.sdk.lifecycle.LifeCycles;
import com.googlecode.jazure.sdk.task.TaskInvocation;

public class MinaQueueStorageEndpoint implements QueueStorageEndpoint {

    private static Logger logger = LoggerFactory.getLogger(MinaQueueStorageEndpoint.class);

    public static final long DEFAULT_RECOVERY_INTERVAL = 3000L;

    private Console console;

    private final InetSocketAddress address;

    private IoConnector connector = new NioSocketConnector();

    private IoSession session;

    private boolean connected = false;

    private long recoveryInterval = DEFAULT_RECOVERY_INTERVAL;

    private MinaResultReceiver resultReceiver = new MinaResultReceiver(this);

    private LifeCycleWrapper lifecycleWrapper = LifeCycles.wrapped();

    public MinaQueueStorageEndpoint(InetSocketAddress address) {
        this.address = address;
    }

    @Override
    public TaskInvocation receive(String resultQueue) {
        return resultReceiver.receive(resultQueue);
    }

    @Override
    public void send(TaskInvocation task) {
        connect();
        WriteFuture future = session.write(task);
        future.awaitUninterruptibly();
    }

    @Override
    public boolean isRunning() {
        return lifecycleWrapper.isRunning();
    }

    @Override
    public void start() {
        lifecycleWrapper.start(new Runnable() {

            @Override
            public void run() {
                configConnector().connect();
            }
        });
    }

    private MinaQueueStorageEndpoint configConnector() {
        connector.setHandler(resultReceiver);
        connector.getFilterChain().addLast("Logging", new LoggingFilter());
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TaskInvocationCodecFactory(true)));
        return this;
    }

    private MinaQueueStorageEndpoint connect() {
        while (isRunning() && !connected) {
            try {
                ConnectFuture future = connector.connect(address);
                future.awaitUninterruptibly();
                session = future.getSession();
                connected = true;
                logger.info("Successfully connect to " + address);
            } catch (Throwable t) {
                connected = false;
                logger.error("Couldn't connect to " + address + " : " + t.getMessage() + ", retrying in " + recoveryInterval + " ms", t);
            }
            sleepInbetweenRecoveryAttempts();
        }
        return this;
    }

    /**
	 * Sleep according to the specified recovery interval.
	 * Called in between recovery attempts.
	 */
    protected void sleepInbetweenRecoveryAttempts() {
        if (this.recoveryInterval > 0) {
            try {
                Thread.sleep(this.recoveryInterval);
            } catch (InterruptedException interEx) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void stop() {
        lifecycleWrapper.stop(new Runnable() {

            @Override
            public void run() {
                session.close(false).awaitUninterruptibly();
                connector.dispose();
                resultReceiver.clear();
            }
        });
    }

    private MinaQueueStorageEndpoint disconnected() {
        connected = false;
        return this;
    }

    public MinaQueueStorageEndpoint reconnect() {
        return this.disconnected().connect();
    }

    @Override
    public Collection<? extends EventListener> listeners() {
        return new ArrayList<EventListener>();
    }

    @Override
    public void setConsole(Console console) {
        this.console = console;
    }
}
