package com.bluemarsh.jswat.core.session;

import com.bluemarsh.jswat.core.connect.JvmConnection;

/**
 * A Session implementation that is only good for testing.
 *
 * @author  Nathan Fiedler
 */
public class DummySession extends AbstractSession {

    /**
     * Creates a new instance of DummySession.
     */
    public DummySession() {
        super();
    }

    public void connect(JvmConnection connection) {
    }

    public void close() {
    }

    public void disconnect(boolean forceExit) {
    }

    public JvmConnection getConnection() {
        return null;
    }

    public boolean isConnected() {
        return false;
    }

    public boolean isSuspended() {
        return false;
    }

    public void resumeVM() {
    }

    public void suspendVM() {
    }
}
