package org.dbe.studio.core.connmgr.proxy;

import org.dbe.toolkit.proxyframework.Workspace;

public class ProxyObject {

    Workspace proxy;

    long timestamp;

    public ProxyObject(Workspace ws, long timestamp) {
        this.proxy = ws;
        this.timestamp = timestamp;
    }

    public Workspace getProxy() {
        return proxy;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
