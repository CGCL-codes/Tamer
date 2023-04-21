package org.state4j.publishing;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class PublisherImpl implements Publisher {

    private List<Observer> observers = new Vector<Observer>();

    public PublisherImpl() {
    }

    public void attach(Observer arg0) {
        this.observers.add(arg0);
        arg0.addPublisher(this);
    }

    public void attach(List<Observer> arg0) {
        Iterator<Observer> iter = arg0.iterator();
        while (iter.hasNext()) {
            Observer arg1 = iter.next();
            this.observers.add(arg1);
            arg1.addPublisher(this);
        }
    }

    public void detach(Observer arg0) {
        this.observers.remove(arg0);
        arg0.removePublisher(this);
    }

    public void publish(Publishable arg0) {
        Iterator<Observer> iter = this.observers.iterator();
        while (iter.hasNext()) {
            Observer observer = iter.next();
            try {
                observer.notify(arg0);
            } catch (InterruptedException exc) {
            }
        }
    }
}
