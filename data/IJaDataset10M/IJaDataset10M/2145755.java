package com.db4o.internal.cs;

import com.db4o.foundation.*;
import com.db4o.internal.*;
import com.db4o.internal.cs.messages.*;

/**
 * @exclude
 */
public class ClientHeartbeat implements Runnable {

    private SimpleTimer _timer;

    private final ClientObjectContainer _container;

    public ClientHeartbeat(ClientObjectContainer container) {
        _container = container;
        _timer = new SimpleTimer(this, frequency(container.configImpl()), "db4o client heartbeat");
    }

    private int frequency(Config4Impl config) {
        return Math.min(config.timeoutClientSocket(), config.timeoutServerSocket()) / 4;
    }

    public void run() {
        _container.writeMessageToSocket(Msg.PING);
    }

    public void start() {
        _timer.start();
    }

    public void stop() {
        if (_timer == null) {
            return;
        }
        _timer.stop();
        _timer = null;
    }
}
