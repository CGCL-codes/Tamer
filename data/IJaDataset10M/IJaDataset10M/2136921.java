package org.infraxx.lang.samples.patterns.observer.java;

import org.infraxx.lang.samples.patterns.observer.Event;
import org.infraxx.lang.samples.patterns.observer.EventType;
import java.util.Observable;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: igor
 * Date: 06.11.11
 * Time: 19:23
 * To change this template use File | Settings | File Templates.
 */
public class StandardEventSource extends Observable implements Runnable {

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(new Random().nextInt(10000));
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            setChanged();
            Event e = new Event(new Random().nextInt(), EventType.DATA_RECEIVED, "Data has been received");
            notifyObservers(e);
        }
    }
}
