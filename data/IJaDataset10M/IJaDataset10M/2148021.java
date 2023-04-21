package net;

import java.util.Iterator;
import net.datagram.RDHashSet;
import net.datagram.RDPacket;

public class ReceiveVerifier extends Thread {

    private RDHashSet set;

    private Listener listener;

    public ReceiveVerifier(Listener l) {
        set = new RDHashSet();
        listener = l;
        start();
    }

    public boolean verify(RDPacket datagram) {
        boolean result = set.add(datagram);
        if (result) {
            datagram.setStamp(System.currentTimeMillis());
        }
        return result;
    }

    public void run() {
        while (true) {
            try {
                expire();
                Thread.sleep(Protocol.RECV_EXPIRE_INTERVAL);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void expire() {
        if (set.size() <= 0) {
            return;
        }
        long now = System.currentTimeMillis();
        int numExpired = 0;
        RDPacket[] packets = set.getAll();
        for (int i = 0; i < packets.length; i++) {
            RDPacket cur = packets[i];
            if ((now - cur.getStamp()) >= Protocol.RECV_EXPIRE_TIMEOUT) {
                if (listener != null) {
                    boolean b = listener.expire(cur);
                    if (b) {
                        set.remove(cur);
                        numExpired++;
                    } else {
                        cur.setStamp(now);
                    }
                } else {
                    set.remove(cur);
                }
            }
        }
        if (listener != null) {
            listener.expired(numExpired);
        }
    }

    public static interface Listener {

        boolean expire(RDPacket packet);

        void expired(int count);
    }
}
