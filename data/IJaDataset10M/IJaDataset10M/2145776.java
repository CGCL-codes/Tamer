package net.sf.sstpong.event;

import java.util.Iterator;
import java.util.LinkedList;

public class HitPaddleNotifier extends Thread {

    LinkedList<SSTPongListener> listeners;

    public HitPaddleNotifier(LinkedList<SSTPongListener> newListeners) {
        this.listeners = newListeners;
    }

    public void run() {
        SSTPongEvent e = new SSTPongEvent(this);
        Iterator<SSTPongListener> itr = listeners.iterator();
        while (itr.hasNext()) {
            SSTPongListener l = itr.next();
            l.hitPaddle(e);
        }
    }
}
