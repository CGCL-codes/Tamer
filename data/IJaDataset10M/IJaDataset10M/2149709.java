package net.sf.elm_ve.cn.cns;

import net.sf.elm_ve.cn.*;
import java.net.Socket;
import java.io.*;

public class CNSHub extends CNHub {

    @Override
    public String getProtocol() {
        return "CNS";
    }

    @Override
    protected CNPeerImpl createPeerImpl(CNPeer peer, Object... args) throws IOException {
        String id = peer.getID();
        String internalID = id.substring(id.indexOf(':') + 1);
        CNSPeerImpl peerImpl = new CNSPeerImpl(internalID, peer);
        return peerImpl;
    }

    @Override
    protected void sendPacket(String internalID, byte data[]) throws IOException {
        String ipAddr = internalID.substring(0, internalID.indexOf(':'));
        String portNo = internalID.substring(internalID.indexOf(':') + 1);
        int port = Integer.parseInt(portNo);
        Socket socket = new Socket(ipAddr, port);
        OutputStream os = socket.getOutputStream();
        os.write(data);
        socket.close();
    }

    @Override
    protected void close() {
        ;
    }
}
