package org.jfonia.view.scribbles;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Rik Bauwens
 */
public class Scribbles implements Iterable<Scribble> {

    private Set<IScribblesObserver> observers = new LinkedHashSet<IScribblesObserver>();

    private List<Scribble> scribbles;

    public Scribbles() {
        scribbles = new LinkedList<Scribble>();
    }

    public void addScribble(Scribble scribble) {
        scribbles.add(scribble);
        scribbleAdded(scribble);
    }

    public void removeScribble(Scribble scribble) {
        scribbles.remove(scribble);
        scribbleRemoved(scribble);
    }

    public boolean isEmpty() {
        return scribbles.isEmpty();
    }

    public void addObserver(IScribblesObserver o) {
        if (!observers.contains(o)) observers.add(o);
    }

    public boolean containsObserver(IScribblesObserver o) {
        return observers.contains(o);
    }

    public void removeObserver(IScribblesObserver o) {
        observers.remove(o);
    }

    private void scribbleAdded(Scribble scribble) {
        for (IScribblesObserver o : observers) o.scribbleAdded(scribble);
    }

    private void scribbleRemoved(Scribble scribble) {
        for (IScribblesObserver o : observers) o.scribbleRemoved(scribble);
    }

    public Iterator<Scribble> iterator() {
        return scribbles.iterator();
    }
}
