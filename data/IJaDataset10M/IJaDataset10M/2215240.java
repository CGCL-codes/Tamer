package pspdash.data;

import java.util.Vector;

class NSDelayedNotifier extends Thread {

    Vector itemsToNotify = new Vector();

    public void run() {
        while (true) {
            try {
                synchronized (this) {
                    if (itemsToNotify.size() == 0) wait();
                }
                sleep(100);
            } catch (InterruptedException ie) {
            }
            while (itemsToNotify.size() > 0) {
                NSField f = (NSField) itemsToNotify.elementAt(0);
                itemsToNotify.removeElementAt(0);
                f.userEvent();
            }
        }
    }

    public synchronized void addField(NSField f) {
        if (f != null) {
            itemsToNotify.addElement(f);
            notify();
        }
    }
}
