package org.activebpel.rt.bpel.def;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Base class for def objects that have a collection of other objects which need
 * to be visited as a collection and/or individually.  
 */
public abstract class AeBaseContainer extends AeBaseDef {

    /** HashMap used for associating names to objects */
    private Map mMap;

    /** List maintains the order of the objects added to this collection */
    private List mList = new ArrayList();

    /**
    * Private getter forces subclasses to use the collection mutator methods below.
    */
    private Map getMap() {
        if (mMap == null) {
            mMap = new HashMap();
        }
        return mMap;
    }

    /**
    * Private getter forces subclasses to use the collection mutator methods below.
    */
    private List getList() {
        return mList;
    }

    /**
    * Gets the named object
    */
    protected Object get(String aKey) {
        return getMap().get(aKey);
    }

    /**
    * Adds a named object to this collection.
    */
    protected void add(String aKey, Object aValue) {
        getMap().put(aKey, aValue);
        getList().add(aValue);
    }

    /**
    * Adds an object to the collection. This is here for subclasses that don't
    * need to have names associated with the objects.
    */
    protected void add(Object aValue) {
        getList().add(aValue);
    }

    /**
    * Removes a named object from the collection.
    * 
    * @param aKey
    * @param aValue
    */
    protected void remove(String aKey, Object aValue) {
        getMap().remove(aKey);
        getList().remove(aValue);
    }

    /**
    * Removes an object from the collection.
    * 
    * @param aValue
    */
    protected void remove(Object aValue) {
        getList().remove(aValue);
    }

    /**
    * Gets the size of the collection
    */
    public int getSize() {
        return getList().size();
    }

    /**
    * Gets an iterator over the values in the collection.
    */
    public Iterator getValues() {
        return getList().iterator();
    }

    /**
    * Returns true if the container is empty.
    */
    public boolean isEmpty() {
        return getSize() == 0;
    }
}
