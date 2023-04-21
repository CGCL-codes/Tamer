package com.aelitis.net.natpmp;

import java.net.InetAddress;
import java.net.NetworkInterface;

public interface NatPMPDevice {

    public boolean connect() throws Exception;

    public int addPortMapping(boolean tcp, int internal_port, int external_port) throws Exception;

    public void deletePortMapping(boolean tcp, int internal_port, int external_port) throws Exception;

    public InetAddress getLocalAddress();

    public NetworkInterface getNetworkInterface();

    public int getEpoch();

    public String getExternalIPAddress();
}
