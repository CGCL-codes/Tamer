package jgp.container;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.io.Serializable;
import jgp.interfaces.Enumerable;
import jgp.interfaces.Iterator;
import jgp.interfaces.Iterateable;

public class List implements Cloneable, Serializable, Enumerable, Iterateable {

    protected ListLink head = null;

    ListLink getHead() {
        return head;
    }

    public List() {
    }

    private void add(ListLink aLink) {
        aLink.setNext(head);
        head = aLink;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void insert(Object info) {
        add(new ListLink(info, null));
    }

    public void remove(Object info) throws NoSuchElementException {
        if (head != null) {
            ListLink prevLink = head;
            if (prevLink.getInfo() == info) {
                head = prevLink.getNext();
                return;
            }
            while (prevLink.getNext() != null) {
                ListLink currLink = prevLink.getNext();
                if (currLink.getInfo() == info) {
                    prevLink.setNext(currLink.getNext());
                    return;
                }
                prevLink = currLink;
            }
        }
        throw new NoSuchElementException();
    }

    public Enumeration elements() {
        return new ListEnumeration(this);
    }

    public Object clone() {
        List cloneList = new List();
        Enumeration myElements = elements();
        if (myElements.hasMoreElements()) {
            ListLink prevLink = new ListLink(myElements.nextElement(), null);
            cloneList.add(prevLink);
            while (myElements.hasMoreElements()) {
                prevLink.setNext(new ListLink(myElements.nextElement(), null));
                prevLink = prevLink.getNext();
            }
        }
        return cloneList;
    }

    public Iterator getIterator() {
        return new ListIterator(this);
    }
}
