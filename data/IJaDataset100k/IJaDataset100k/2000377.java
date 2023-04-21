package org.smapcore.smap.transport;

import org.smapcore.smap.core.SMAPException;

public interface SMAPTransportManager {

    public void init() throws SMAPException;

    public SMAPSession getSession(String server, int port, boolean privacy) throws SMAPException;

    public SMAPListener getListener(String server, int port) throws SMAPException;
}
