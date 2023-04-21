package jsesh.mdc.model;

import java.util.ListIterator;

/**
 * This file is free Software (c) Serge Rosmorduc
 * 
 * @author rosmord
 *  
 */
public class HorizontalListElementIterator {

    private ListIterator rep;

    HorizontalListElementIterator(ListIterator rep) {
        this.rep = rep;
    }

    public void add(InnerGroup o) {
        rep.add(o);
    }

    public boolean hasNext() {
        return rep.hasNext();
    }

    public boolean hasPrevious() {
        return rep.hasPrevious();
    }

    public InnerGroup next() {
        return (InnerGroup) rep.next();
    }

    public int nextIndex() {
        return rep.nextIndex();
    }

    public InnerGroup previous() {
        return (InnerGroup) rep.previous();
    }

    public int previousIndex() {
        return rep.previousIndex();
    }

    public void remove() {
        rep.remove();
    }

    public void set(InnerGroup o) {
        rep.set(o);
    }
}
