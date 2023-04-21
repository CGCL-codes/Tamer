package org.crappydbms.dbfiles;

import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga
 * Dec 24, 2008
 * 
 */
public interface FilePageIterator<T> {

    boolean hasNext() throws TransactionAbortedException;

    T next() throws TransactionAbortedException;
}
