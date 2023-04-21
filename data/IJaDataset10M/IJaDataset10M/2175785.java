package org.rakiura.util;

/**
 * 
 * @author pmallet Created: 19-Jul-05 5:11:19 PM TODO
 */
public interface Set {

    boolean contains(Object o);

    boolean add(Object o);

    boolean remove(Object o);

    public int size();

    public void clear();

    public Object[] toArray(Object[] a);
}
