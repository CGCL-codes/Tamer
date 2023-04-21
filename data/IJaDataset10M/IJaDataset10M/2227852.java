package eroc;

import java.util.Observable;

class Watcher extends Observable {

    static final double serialVersionUID = 1;

    public Watcher() {
        oVal = 50;
    }

    public synchronized int getValue() {
        return oVal;
    }

    public synchronized void setValue(int newValu) {
        if (newValu != oVal) {
            oVal = newValu;
            setChanged();
            notifyObservers();
        }
    }

    int oVal;
}
