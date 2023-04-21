package com.ibm.realtime.flexotask.cloning;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A cloner to use with Collection subclasses.  This assumes a <T>(Collection) constructor, but does
 *   not assume it does a deep enough clone (the members are cloned explicitly).
 * The collection type must be mutable (do not use with immutable collections).
 */
public class CollectionCloner implements DeepCloner {

    public Object deepClone(Object toClone, boolean notIfPinned) throws CloneNotSupportedException {
        try {
            Class clazz = toClone.getClass();
            Constructor cons = clazz.getConstructor(new Class[] { Collection.class });
            Collection coll = (Collection) toClone;
            List members = new ArrayList(coll.size());
            for (Iterator iter = coll.iterator(); iter.hasNext(); ) {
                members.add(CloningSupport.simulatedDeepClone(iter.next(), notIfPinned));
            }
            return cons.newInstance(new Object[] { members });
        } catch (Exception e) {
            CloneNotSupportedException cnse = new CloneNotSupportedException();
            cnse.initCause(e);
            throw cnse;
        }
    }
}
