package net.sf.fikin.pojostate.util;

import java.util.Enumeration;

/**
 * Enumerate over an array of objects
 * 
 * created on Apr 20, 2005
 * @author fiykov
 * @version $Revision: 1.1 $
 * @since
 */
public class ArrayEnumeration implements Enumeration {

    protected Object[] array;

    protected int index = 0;

    public ArrayEnumeration(Object[] arr) {
        this.array = arr;
    }

    public boolean hasMoreElements() {
        return index < array.length;
    }

    public Object nextElement() {
        return array[index++];
    }

    public int getSize() {
        return array.length;
    }

    public Object[] getArray() {
        return array;
    }
}
