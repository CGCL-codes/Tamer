package com.peterhi.client.nio.handlers;

import java.net.DatagramSocket;
import java.util.Iterator;
import com.peterhi.beans.PeerBean;
import com.peterhi.client.Application;
import com.peterhi.client.impl.managers.StoreManager;
import com.peterhi.client.nio.DatagramHandler;
import com.peterhi.client.nio.DatagramSession;
import com.peterhi.client.nio.NetworkManager;
import com.peterhi.client.nio.events.PeerEvent;
import com.peterhi.client.nio.events.PeerListener;
import com.peterhi.net.msg.ISesMsg;
import com.peterhi.net.msg.QPeerRsp;

public class QueryPeersFeedbackHandler implements DatagramHandler {

    public void handle(NetworkManager man, DatagramSession session, byte[] data) {
        try {
            QPeerRsp f = new QPeerRsp();
            f.deserialize(data, 22);
            Iterator<PeerListener> itor = session.peerListenerIterator();
            while (itor.hasNext()) {
                PeerListener l = itor.next();
                l.onFound(new PeerEvent(this, f.getBeans()));
            }
            PeerBean[] beans = f.getBeans();
            if (beans == null) return;
            for (PeerBean bean : beans) {
                int targetClientHashCode = bean.getHashCode();
                DatagramSession ses = new DatagramSession(man);
                ses.setSocket(new DatagramSocket());
                ses.setRemoteSocketAddress(session.getRemoteSocketAddress());
                ses.start();
                man.putDatagram(targetClientHashCode, ses);
                ISesMsg m = new ISesMsg();
                m.setClientHashCode(getStore().getID());
                m.setTargetClientHashCode(targetClientHashCode);
                man.udpPost(targetClientHashCode, m);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private StoreManager getStore() {
        return Application.getApplication().getManager(StoreManager.class);
    }
}
