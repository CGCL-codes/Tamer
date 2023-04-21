package net.obsearch.storage;

import java.util.Iterator;
import java.util.NoSuchElementException;
import net.obsearch.exception.OBException;

/**
 * Iterator that must be closed after usage (remove locks, etc)
 * The {@link #remove()} method will throw an error 
 * if the ranges of the iterator are not equal. 
 * That is, iff {@link OBStore.processRange(key,key)}
 * (key = key).
 *
 */
public interface CloseIterator<O> extends Iterator<O> {

    void closeCursor() throws OBException;
}
