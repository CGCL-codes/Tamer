package org.ms150hams.trackem.network;

public interface TCPIPClientListener {

    public void tcpipClientConnected(TCPIPClient client);

    public void tcpipClientDisconnected(TCPIPClient client);
}
