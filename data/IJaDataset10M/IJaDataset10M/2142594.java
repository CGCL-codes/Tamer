package com.javampire.util.dao;

import java.io.Closeable;
import java.io.IOException;

/**
 * TODO: document this.
 *
 * @author <a href="mailto:cnagy@ecircle.de">Csaba Nagy</a>
 * @version $Revision: 1.1.1.1 $ $Date: 2007/04/10 10:21:59 $
 */
public interface ReadableDataNode<T> extends Closeable, RecordFactory<T> {

    boolean seek(int recordId) throws IOException, UnsupportedOperationException;

    boolean hasNext() throws IOException;

    /**
     * Fills the provided record, or returns a new one.
     * If T is null: a new record is returned, which is guarrantied to not
     * be modified further.
     * If T is not null: either a new record is returned, or the same record
     * is filled and returned. In any case, the returned record is guarrantied
     * not to be further modified by the reader, but instead the given in record
     * might be retained and used for the next record to be returned.
     * @param record
     * @return
     * @throws IOException
     */
    T next(T record) throws IOException;

    int getRecordCount() throws IOException, UnsupportedOperationException;

    int getRecordId() throws IOException;
}
