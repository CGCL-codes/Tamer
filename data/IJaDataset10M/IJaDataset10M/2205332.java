package com.hisham.util.collections;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * <p>Title: DynamicList</p>
 * <p>Description: Helper class to ease of readability to create a vector list
 * of different java class types
 * </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p> </p>
 *
 * @author Ali Hisham Malik
 * @version 2.0
 */
public abstract class DynamicList<E> extends ArrayList<E> implements IDynamicList {

    /**
   *
   */
    public DynamicList() {
        super();
    }

    /**
   *
   * @param p0 int
   */
    public DynamicList(int p0) {
        super(p0);
    }

    /**
   *
   * @param p0 Collection
   */
    public DynamicList(Collection<E> p0) {
        super(p0);
    }

    /**
   * adds all the objects in the array
   * @param objects Object[]
   */
    public DynamicList(E[] objects) {
        super(objects.length);
        for (int i = 0; i < objects.length; i++) {
            super.add(i, objects[i]);
        }
    }

    /**
   * adds all the objects in the array
   * @param objects Object[]
   * @return boolean
   */
    public boolean addAll(E[] objects) {
        boolean allOK = true;
        int lastIndex = 0;
        for (int i = 0; i < objects.length && allOK; i++) {
            lastIndex = i;
            allOK = super.add(objects[i]);
        }
        if (!allOK) {
            for (int i = 0; i < lastIndex; i++) {
                super.remove(objects[i]);
            }
        }
        return allOK;
    }

    public String[] getStringArray() {
        String[] typeVector = new String[this.size()];
        for (int i = 0; i < this.size(); i++) {
            typeVector[i] = this.get(i).toString();
        }
        return typeVector;
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        if (this.size() > 0) {
            result.append(this.get(0).toString());
            for (int i = 1; i < this.size(); i++) {
                result.append(",");
                result.append(this.get(i).toString());
            }
        }
        return result.toString();
    }

    /**
   * Overrides the get function to return a new Object
   * instead of throwing an exception (Useful for struts)
   * @param index int
   * @return Object
   */
    public E get(int index) {
        if (this.size() <= index) {
            for (int i = this.size(); i <= index; i++) {
                this.add(getNewElement());
            }
        }
        return super.get(index);
    }

    public abstract E getNewElement();
}
