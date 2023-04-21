package edu.upmc.opi.caBIG.common;

import java.util.NoSuchElementException;

/**
 * An interface for a heap implementation. Can either be descending or
 * ascending.
 * 
 * @see Heap
 * @author <A HREF="http://www.radwin.org/michael/">Michael J. Radwin</A>
 * @version 1.0 2/23/96
 * @author mitchellkj@upmc.edu
 * @version $Id: HeapImpl.java,v 1.1 2009/06/02 19:06:31 girish_c1980 Exp $
 * @since 1.4.2_04
 */
public interface HeapImpl {

    /**
     * Method remove.
     * 
     * @return Heapable
     * 
     * @throws NoSuchElementException the no such element exception
     */
    public Heapable remove() throws NoSuchElementException;

    /**
     * Method insert.
     * 
     * @param key Heapable
     */
    public void insert(Heapable key);

    /**
     * Method heapify.
     * 
     * @param i int
     */
    public void heapify(int i);

    /**
     * Method isEmpty.
     * 
     * @return boolean
     */
    public boolean isEmpty();

    /**
     * Method size.
     * 
     * @return int
     */
    public int size();

    /**
     * Method removeAllElements.
     */
    public void removeAllElements();
}
