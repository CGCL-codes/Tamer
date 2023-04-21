package com.aelitis.azureus.core.speedmanager;

import java.net.InetSocketAddress;

public interface SpeedManagerPingSource {

    public InetSocketAddress getAddress();

    public int getPingTime();

    public void destroy();
}
