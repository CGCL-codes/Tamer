package net.tinyos.sf.old;

import java.net.*;
import java.io.*;
import java.util.*;

public class ClientServicer extends Thread {

    private static final boolean DEBUG = true;

    private Socket m_socket = null;

    private int m_nTimeout = 5000;

    private InputStream input = null;

    public OutputStream output = null;

    private ListenServer lstnSrvr = null;

    private boolean bShutdown = false;

    private boolean bFirstTime = true;

    private String hostname, ipaddr;

    private SerialForward sf;

    private ListenServer listenServer;

    public ClientServicer(Socket socket, SerialForward serialForward, ListenServer listenSvr) {
        sf = serialForward;
        listenServer = listenSvr;
        m_socket = socket;
        InetAddress addr = m_socket.getInetAddress();
        hostname = addr.getHostName();
        ipaddr = addr.getHostAddress();
        sf.DEBUG("ServerReceivingThread created to service host " + hostname);
    }

    public String toString() {
        return "Client " + hostname + " (" + ipaddr + ")";
    }

    private void InitConnection() {
        try {
            output = m_socket.getOutputStream();
            input = m_socket.getInputStream();
            if (sf.serialPortIO != null) {
                sf.serialPortIO.RegisterPacketForwarder(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            bShutdown = true;
            return;
        }
        return;
    }

    public void run() {
        sf.VERBOSE("client connected from " + hostname + " (" + ipaddr + ")");
        InitConnection();
        ReadPackets();
        Shutdown();
        sf.VERBOSE("client disconnected from " + hostname + " (" + ipaddr + ")");
        sf.DEBUG("ClientServicer: terminating host = " + hostname);
    }

    private synchronized void ReadPackets() {
        int nBytesRead = 0;
        int nBytesReturned = 0;
        byte[] currentPacket = new byte[sf.PACKET_SIZE];
        try {
            nBytesReturned = input.read(currentPacket, nBytesRead, sf.PACKET_SIZE - nBytesRead);
            while (nBytesReturned != -1 && (!bShutdown || bFirstTime)) {
                bFirstTime = false;
                nBytesRead += nBytesReturned;
                if (nBytesRead == sf.PACKET_SIZE) {
                    nBytesRead = 0;
                    HandlePacket(currentPacket);
                }
                nBytesReturned = input.read(currentPacket, nBytesRead, sf.PACKET_SIZE - nBytesRead);
            }
        } catch (IOException e) {
            sf.DEBUG("ClientServicer: connection was closed to host " + hostname);
        }
    }

    private void HandlePacket(byte[] currentPckt) {
        sf.DEBUG("Packet received from " + hostname);
        sf.IncrementPacketsWritten();
        sf.serialPortIO.WriteBytes(currentPckt);
    }

    public void Shutdown() {
        if (!bShutdown) {
            bShutdown = true;
            if (sf.serialPortIO != null) sf.serialPortIO.UnregisterPacketForwarder(this);
            listenServer.RemoveClientServicer(this);
            sf.DecrementClients();
            try {
                m_socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.interrupt();
        }
    }
}
