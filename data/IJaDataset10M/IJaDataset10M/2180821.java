package org.lastfm.observ;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Observable<T extends ObserveObject> implements ObserverCollection<T> {

    private static final Log log = LogFactory.getLog(Observable.class);

    List<Observer<T>> observers = new ArrayList<Observer<T>>();

    public boolean fire(T param) {
        if (param == null) {
            return true;
        }
        boolean ok = true;
        List<Observer<T>> observers = new ArrayList<Observer<T>>(this.observers);
        for (Observer<T> listener : observers) {
            try {
                listener.observe(param);
            } catch (Exception e) {
                ok = false;
                log.error(e, e);
            }
        }
        return ok;
    }

    public void add(Observer<T> l) {
        if (l == null) {
            throw new NullPointerException("Can't add a null listener.");
        }
        observers.add(l);
    }

    public void remove(Observer<T> listener) {
        observers.remove(listener);
    }

    public void clean() {
        observers.clear();
    }
}
