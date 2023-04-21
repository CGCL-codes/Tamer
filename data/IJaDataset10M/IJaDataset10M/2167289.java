package com.aelitis.net.upnp.impl;

import java.net.*;

public interface SSDPIGDListener {

    public void rootDiscovered(NetworkInterface network_interface, InetAddress local_address, String usn, URL location);

    public void rootAlive(String usn, URL location);

    public void rootLost(InetAddress local_address, String usn);

    public void interfaceChanged(NetworkInterface network_interface);
}
