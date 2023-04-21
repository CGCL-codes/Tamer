package net.kano.joustsim.oscar.oscar.service.icbm;

import net.kano.joustsim.oscar.oscar.service.icbm.ft.controllers.PassiveConnector;
import net.kano.joustsim.oscar.oscar.service.icbm.ft.state.SocketStreamInfo;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

class HangConnector implements PassiveConnector, MockConnector {

    private final Object lock = new Object();

    private boolean done = false;

    public SocketStreamInfo createStream() throws IOException {
        try {
            synchronized (lock) {
                done = true;
                lock.notifyAll();
            }
            waitForever();
        } catch (InterruptedException e) {
        }
        throw new IllegalStateException();
    }

    private void waitForever() throws InterruptedException {
        Object o = new Object();
        synchronized (o) {
            o.wait(0);
        }
    }

    public void waitForConnectionAttempt() {
        synchronized (lock) {
            while (!done) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public boolean hasAttemptedConnection() {
        synchronized (lock) {
            return done;
        }
    }

    public void checkConnectionInfo() throws Exception {
    }

    public void prepareStream() throws IOException {
    }

    public int getLocalPort() {
        return 501;
    }

    public InetAddress getLocalHost() {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
