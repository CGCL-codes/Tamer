package org.persist.onm.jxtademo;

import java.io.IOException;
import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaMulticastSocket;

public class NotificationMessageListener implements Runnable {

    private PeerGroup peerGrp;

    private PipeAdvertisement notifyPipeAdv;

    public NotificationMessageListener(PeerGroup peerGrp, PipeAdvertisement notifyPipeAdv) {
        this.peerGrp = peerGrp;
        this.notifyPipeAdv = notifyPipeAdv;
    }

    @Override
    public void run() {
        JxtaMulticastSocket mcastSocket = null;
        try {
            mcastSocket = new JxtaMulticastSocket(peerGrp, notifyPipeAdv);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
