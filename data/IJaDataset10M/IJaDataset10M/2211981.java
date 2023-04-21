package org.red5.server.webapp.oflaDemo;

import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.stream.IServerStream;
import org.red5.server.api.stream.IStreamCapableConnection;
import org.red5.server.api.stream.support.SimpleBandwidthConfigure;

public class Application extends ApplicationAdapter {

    private IScope appScope;

    private IServerStream serverStream;

    @Override
    public boolean appStart(IScope app) {
        appScope = app;
        return true;
    }

    @Override
    public boolean appConnect(IConnection conn, Object[] params) {
        measureBandwidth(conn);
        if (conn instanceof IStreamCapableConnection) {
            IStreamCapableConnection streamConn = (IStreamCapableConnection) conn;
            SimpleBandwidthConfigure sbc = new SimpleBandwidthConfigure();
            sbc.setMaxBurst(8 * 1024 * 1024);
            sbc.setBurst(8 * 1024 * 1024);
            sbc.setOverallBandwidth(2 * 1024 * 1024);
            streamConn.setBandwidthConfigure(sbc);
        }
        return super.appConnect(conn, params);
    }

    @Override
    public void appDisconnect(IConnection conn) {
        if (appScope == conn.getScope() && serverStream != null) {
            serverStream.close();
        }
        super.appDisconnect(conn);
    }
}
