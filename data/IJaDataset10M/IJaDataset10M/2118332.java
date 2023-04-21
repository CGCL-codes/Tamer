package com.coldcore.coloradoftp.connection.impl;

import com.coldcore.coloradoftp.connection.Connection;
import com.coldcore.coloradoftp.connection.ConnectionPool;
import com.coldcore.coloradoftp.connection.TerminatedException;
import com.coldcore.coloradoftp.core.Core;
import com.coldcore.coloradoftp.core.CoreStatus;
import com.coldcore.coloradoftp.factory.ObjectFactory;
import com.coldcore.coloradoftp.factory.ObjectName;
import org.apache.log4j.Logger;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @see com.coldcore.coloradoftp.connection.ConnectionPool
 *
 * This class is thread safe as it takes care of all synchronizations.
 */
public class GenericConnectionPool implements ConnectionPool, Runnable {

    private static Logger log = Logger.getLogger(GenericConnectionPool.class);

    protected Set<Connection> connections;

    protected Core core;

    protected Thread thr;

    protected long sleep;

    protected boolean running;

    public GenericConnectionPool() {
        sleep = 1000L;
    }

    /** Get thread sleep time
   * @return Time in mills
   */
    public long getSleep() {
        return sleep;
    }

    /** Set thread sleep time
   * @param sleep Time in mills
   */
    public void setSleep(long sleep) {
        this.sleep = sleep;
    }

    public void initialize() throws Exception {
        if (connections == null) connections = new HashSet<Connection>();
        core = (Core) ObjectFactory.getObject(ObjectName.CORE);
        if (!running) {
            running = true;
            thr = new Thread(this);
            thr.start();
        }
    }

    public void add(Connection connection) {
        synchronized (connections) {
            connections.add(connection);
        }
    }

    public void remove(Connection connection) {
        synchronized (connections) {
            connections.remove(connection);
        }
    }

    public int size() {
        synchronized (connections) {
            return connections.size();
        }
    }

    public void run() {
        while (running) {
            synchronized (connections) {
                Connection connection = null;
                Iterator<Connection> it = connections.iterator();
                while (it.hasNext()) try {
                    connection = it.next();
                    if (connection.isDestroyed()) {
                        it.remove();
                        continue;
                    }
                    if (core.getStatus() == CoreStatus.POISONED && !connection.isPoisoned()) {
                        connection.poison();
                    }
                    connection.service();
                } catch (TerminatedException e) {
                    log.debug(e);
                    try {
                        connection.destroy();
                    } catch (Throwable ex) {
                    }
                } catch (Throwable e) {
                    log.error("Connection failed", e);
                    try {
                        connection.destroy();
                    } catch (Throwable ex) {
                    }
                }
            }
            try {
                Thread.sleep(sleep);
            } catch (Throwable e) {
            }
        }
        log.debug("Connection pool thread finished");
    }

    public void destroy() {
        running = false;
        synchronized (connections) {
            for (Connection connection : connections) try {
                connection.destroy();
            } catch (Throwable e) {
            }
            connections.clear();
        }
        try {
            thr.join(30000);
        } catch (Throwable e) {
        }
    }

    public Set<Connection> list() {
        synchronized (connections) {
            return new HashSet<Connection>(connections);
        }
    }
}
