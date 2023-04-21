package org.sourceforge.vlibrary.user.dao;

import org.sourceforge.vlibrary.exceptions.LibraryException;
import org.sourceforge.vlibrary.user.valuebeans.LibraryTransaction;
import org.sourceforge.vlibrary.user.exceptions.ReaderNotFoundException;
import org.sourceforge.vlibrary.user.exceptions.DuplicateRequestException;
import java.util.ArrayList;
import java.util.List;

/**
 * @version $Revision$ $Date$
 */
public interface LibraryTransactionDAO {

    /** Retrieves data for *this using this.id.
     *  Fills in reader, book data by joining with
     *  reader, book tables
     */
    public LibraryTransaction retrieve(long id) throws LibraryException;

    public void processCheckin(long reader, long book) throws LibraryException;

    public void processCheckout(long reader, long book) throws LibraryException;

    public List getTransactions(long book) throws LibraryException;

    public void cancelRequest(long reader, long book) throws LibraryException;

    public boolean requestPending(long reader, long book) throws LibraryException;

    public List getRequestors(long book) throws LibraryException;

    public void processRequest(long reader, long book) throws LibraryException, ReaderNotFoundException, DuplicateRequestException;

    public long getPossessor(long book) throws LibraryException;
}
