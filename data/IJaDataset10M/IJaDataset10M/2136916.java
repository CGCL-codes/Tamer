package com.coderdream.chapter17.observer.sample;

import java.util.Iterator;
import java.util.Vector;

public abstract class NumberGenerator {

    private Vector observers = new Vector();

    /**
	 * 添加Observer
	 * 
	 * @param observer
	 */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
	 * 刪除Observer
	 * 
	 * @param observer
	 */
    public void deleteObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
	 * 通知Observer
	 */
    public void notifyObservers() {
        Iterator it = observers.iterator();
        while (it.hasNext()) {
            Observer o = (Observer) it.next();
            o.update(this);
        }
    }

    public abstract int getNumber();

    public abstract void execute();
}
