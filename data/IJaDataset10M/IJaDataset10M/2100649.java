package org.enerj.sco;

import java.util.*;
import org.enerj.core.*;

/**
 * Second Class Object subclass for java.util.Vector.
 *
 * @version $Id: JavaUtilVectorSCO.java,v 1.3 2005/08/12 02:56:45 dsyrstad Exp $
 * @author <a href="mailto:dsyrstad@ener-j.org">Dan Syrstad</a>
 */
public class JavaUtilVectorSCO extends java.util.Vector implements SCOTracker {

    private Persistable mOwnerFCO;

    /**
     * Construct an empty collection using the specified initial capacity and 
     * owner FCO.
     *
     * @param anInitialCapacity an initial capacity.
     * @param anOwnerFCO the owning First Class Object.
     */
    public JavaUtilVectorSCO(int anInitialCapacity, Persistable anOwnerFCO) {
        super(anInitialCapacity);
        mOwnerFCO = anOwnerFCO;
    }

    public Persistable getOwnerFCO() {
        return mOwnerFCO;
    }

    public void setOwnerFCO(Persistable anOwner) {
        mOwnerFCO = anOwner;
    }

    public void setOwnerModified() {
        if (mOwnerFCO != null) {
            PersistableHelper.addModified(mOwnerFCO);
        }
    }

    /**
     * Returns a clone without the owner set.
     *
     * @return an un-owned clone.
     */
    public Object clone() {
        SCOTracker clone = (SCOTracker) super.clone();
        clone.setOwnerFCO(null);
        return clone;
    }

    public boolean add(Object o) {
        boolean b = super.add(o);
        setOwnerModified();
        return b;
    }

    public boolean addAll(Collection c) {
        boolean b = super.addAll(c);
        setOwnerModified();
        return b;
    }

    public void clear() {
        super.clear();
        setOwnerModified();
    }

    public boolean remove(Object o) {
        boolean b = super.remove(o);
        setOwnerModified();
        return b;
    }

    public boolean removeAll(Collection c) {
        boolean b = super.removeAll(c);
        setOwnerModified();
        return b;
    }

    public boolean retainAll(Collection c) {
        boolean b = super.retainAll(c);
        setOwnerModified();
        return b;
    }

    public Object set(int index, Object element) {
        Object o = super.set(index, element);
        setOwnerModified();
        return o;
    }

    public Object remove(int index) {
        Object o = super.remove(index);
        setOwnerModified();
        return o;
    }

    public boolean addAll(int index, Collection c) {
        boolean b = super.addAll(index, c);
        setOwnerModified();
        return b;
    }

    public void add(int index, Object element) {
        super.add(index, element);
        setOwnerModified();
    }

    protected void removeRange(int fromIndex, int toIndex) {
        super.removeRange(fromIndex, toIndex);
        setOwnerModified();
    }

    public void addElement(Object obj) {
        super.addElement(obj);
        setOwnerModified();
    }

    public void insertElementAt(Object obj, int index) {
        super.insertElementAt(obj, index);
        setOwnerModified();
    }

    public void setElementAt(Object obj, int index) {
        super.setElementAt(obj, index);
        setOwnerModified();
    }

    public boolean removeElement(Object obj) {
        boolean b = super.removeElement(obj);
        setOwnerModified();
        return b;
    }

    public void removeElementAt(int index) {
        super.removeElementAt(index);
        setOwnerModified();
    }

    public void removeAllElements() {
        super.removeAllElements();
        setOwnerModified();
    }

    public void setSize(int newSize) {
        super.setSize(newSize);
        setOwnerModified();
    }
}
