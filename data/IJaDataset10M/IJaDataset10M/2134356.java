package de.peathal.util;

import de.peathal.resource.L;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class implements a SoftRefernce Stack - Last In Last Out.
 * The elements are not strongly referenced!
 * If memory is low the garbage collection could reclaim the objects!!
 * So it could be that you will get a null instead of your expected object!
 * To have more control over enqueuing you should specify a ReferenceQueue!
 * Useful for Cache implementations.
 *
 * @author Peter Karich
 */
public class SoftStack extends CleanVector {

    private static final long serialVersionUID = 2561894539L;

    /** Creates a new instance of SoftStack with 100 as size. */
    public SoftStack() {
        this(new ReferenceQueue(), 100);
    }

    /** Creates a new instance of SoftStack with 100 as size. */
    public SoftStack(ReferenceQueue refQueue) {
        this(refQueue, 100);
    }

    /**
     * Creates a new instance of SoftStack
     * @param rq specifies the ReferenceQueue to which all new SoftReference's
     * will be push to, if garbage collector detects that there are unused.
     */
    public SoftStack(ReferenceQueue rq, int size) {
        super(rq, size);
    }

    /**
     * Pushs a new element to this stack.
     */
    public synchronized void push(Object o) {
        getVector().add(new SoftReference(o, getQueue()));
    }

    /**
     * Removes the last element from this stack and returns it.
     * @throws NoSuchElementException if stack is empty
     */
    public synchronized Object pop() {
        if (size() != 0) return ((SoftReference) getVector().remove(size() - 1)).get(); else throw new NoSuchElementException(L.tr("You_can't_pop:_Stack_is_empty!"));
    }

    public void clear() {
        getVector().clear();
    }

    /**
     * Returns the last element of this stack without removing it.
     * @throws NoSuchElementException if stack is empty
     */
    public synchronized Object peek() {
        List l = getVector();
        return ((SoftReference) l.get(l.size() - 1)).get();
    }

    /**
     * Returns the current size. This does not mean that all element are valid.
     * Because it could be that garbage collector enqueued some references and
     * reclaim the referent object!
     */
    public synchronized int size() {
        return getVector().size();
    }

    /**
     * This methods returns the distance of the specified object from the top.
     * @return -1 if not found
     *
     public int search(Object o)
     {
     int i = v.lastIndexOf(o);
     if(i >= 0)
     return size() - i;
     else return -1;
     }*/
    protected void cleanUp(Reference soft) {
        soft.clear();
        getVector().remove(soft);
    }
}
