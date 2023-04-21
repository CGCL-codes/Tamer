package client;

import java.util.Observable;

public class KoAnim extends Observable implements Runnable {

    Ko k;

    int opacity = 0;

    boolean appear;

    public void run() {
        while (opacity != 0 && opacity != 100) {
            if (appear) opacity++; else opacity--;
            setChanged();
            notifyObservers(1);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public KoAnim(Ko k_, boolean appear_) {
        k = k_;
        appear = appear_;
        if (appear) opacity = 1; else opacity = 99;
    }

    public float getOp() {
        return ((float) opacity / 100);
    }
}
