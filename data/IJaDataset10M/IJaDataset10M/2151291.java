package org.asteriskjava.manager;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.asteriskjava.manager.internal.ManagerConnectionImpl;
import com.safi.server.SafiManagerConnectionImpl;
import com.safi.server.SafiServerListener;

public class SafiManagerConnection extends DefaultManagerConnection {

    private Set<SafiServerListener> listeners = new HashSet<SafiServerListener>();

    public boolean addListener(SafiServerListener listener) {
        return listeners.add(listener);
    }

    public boolean removeListener(SafiServerListener listener) {
        return listeners.remove(listener);
    }

    public SafiManagerConnection() {
        super();
    }

    public SafiManagerConnection(String hostname, String username, String password) {
        super(hostname, username, password);
    }

    public SafiManagerConnection(String hostname, int port, String username, String password) {
        super(hostname, port, username, password);
    }

    @Override
    public void login() throws IllegalStateException, IOException, AuthenticationFailedException, TimeoutException {
        super.login();
        notifyStarted();
    }

    private void notifyStarted() {
        for (SafiServerListener l : listeners) {
            l.serverStarted();
        }
    }

    @Override
    protected ManagerConnectionImpl getManagerConnectionImpl() {
        return new SafiManagerConnectionImpl();
    }

    public boolean isConnected() {
        return impl == null ? false : ((SafiManagerConnectionImpl) impl).isConnected();
    }
}
