package org.aoplib4j.gof.observer.client;

import org.aoplib4j.gof.observer.NotifyInformation;
import org.aoplib4j.gof.observer.ObserverCallback;

/**
 * @author Adrian Citu
 *
 */
public class ClientObserverCallBack extends ObserverCallback {

    public void notifyObserver(NotifyInformation si) {
        if (si.getMethod().getName().equals("sell")) {
            ((Client) si.getObserver()).buy();
        }
    }
}
