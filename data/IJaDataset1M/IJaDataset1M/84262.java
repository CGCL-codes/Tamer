package org.rapla.components.util;

import java.util.Vector;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
  * This list internaly uses WeakReferences that doesnt prevent the
  * list-entrys from being garbage-collected. Use this list if you
  * cannot determine when the objects in the list are no longer in use
  * and should be taken out of the list. <br>
  *
  * Parts taken from QueuedWRVectorModel.java written by Raimond
  * Reichert for JavaWorld
  * @author Christopher Kohlhaas
  */
public class WeakReferenceList {

    private Collection basis;

    private ReferenceQueue queue;

    public WeakReferenceList() {
        this.basis = new Vector();
        queue = new ReferenceQueue();
    }

    /**
     * Polls the reference queue, and removes all references on it from the list
     */
    public void cleanUp() {
        WeakReference wr = (WeakReference) queue.poll();
        while (wr != null) {
            basis.remove(wr);
            wr = (WeakReference) queue.poll();
        }
    }

    /**
     * add an object to the list. Note, that adding an object to this list,
     * will not add an additional reference to the object. So 
     * adding an Object to this list does not prevent it from beeing garbage-collected.
     * If the object is garbage-collected it will be removed from the list upon
     * the next call of cleanUp(),add(),remove() or iterator()
     */
    public void add(Object o) {
        cleanUp();
        WeakReference wr = new WeakReference(o, queue);
        basis.add(wr);
    }

    /**
     * remove an Object from list. 
     * Note: Objects will be automatically be removed if they are garbage-collected.
     * If you are able to call the remove method for every-object, you will
     * not need this Class. 
     */
    public void remove(Object o) {
        cleanUp();
        Iterator it = basis.iterator();
        while (it.hasNext()) {
            WeakReference wr = (WeakReference) it.next();
            if (o == wr.get()) {
                it.remove();
            }
        }
    }

    /** Use this Iterator to safely iterate the list. 
     All references to garbage-collected-objects will be skipped.*/
    public Iterator iterator() {
        return new WRIterator();
    }

    class WRIterator implements Iterator {

        Iterator it;

        Object nextElement;

        WRIterator() {
            it = basis.iterator();
            nextElement = nextElement();
        }

        public boolean hasNext() {
            return nextElement != null;
        }

        private Object nextElement() {
            while (it.hasNext()) {
                WeakReference wr = (WeakReference) it.next();
                Object o = wr.get();
                if (o == null) {
                    it.remove();
                } else {
                    return o;
                }
            }
            return null;
        }

        public Object next() {
            if (!hasNext()) throw new NoSuchElementException();
            Object result = nextElement;
            nextElement = nextElement();
            return result;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
