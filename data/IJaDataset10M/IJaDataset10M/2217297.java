package org.groovymud.engine.event.observer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.groovymud.engine.event.IScopedEvent;

public abstract class Observable implements IObservable {

    private transient Set<Observer> observers;

    private transient boolean changed;

    public Set<Observer> getObservers() {
        if (observers == null) {
            observers = createObservers();
        }
        return new HashSet(observers);
    }

    public void setObservers(HashSet<Observer> observers) {
        this.observers = createObservers();
        this.observers.addAll(observers);
    }

    private Set<Observer> createObservers() {
        return Collections.synchronizedSet(new HashSet<Observer>());
    }

    public void addObserver(Observer obj) {
        if (observers == null) {
            observers = createObservers();
        }
        observers.add(obj);
    }

    public void deleteObserver(Observer o) {
        observers.remove(o);
    }

    public synchronized void notifyObservers(IScopedEvent arg) {
        Set<Observer> observers = getObservers();
        Iterator<Observer> i = observers.iterator();
        if (hasChanged()) {
            changed = false;
            while (i.hasNext()) {
                Observer ob = i.next();
                ob.update((IObservable) this, arg);
            }
        }
    }

    public void fireEvent(IScopedEvent event) {
        setChanged();
        if (event.getSource() == null) {
            event.setSource(this);
        }
        notifyObservers(event);
    }

    public abstract void doEvent(IScopedEvent event);

    public boolean hasChanged() {
        return changed;
    }

    public void setChanged() {
        this.changed = true;
    }

    public int size() {
        return getObservers().size();
    }
}
