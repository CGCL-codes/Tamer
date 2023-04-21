package org.scohen.juploadr.util;

import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * An observer that looks at a list for null elements and removes them
 * @author steve
 *
 */
public class NullElementeObserver implements Observer {

    public void update(Observable o, Object arg) {
        List l = (List) o;
        Iterator iter = l.iterator();
        while (iter.hasNext()) {
            Object next = iter.next();
            if (next == null) {
                iter.remove();
            }
        }
    }
}
