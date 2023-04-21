package medi.db.deleter;

import javatools.db.DbException;
import medi.db.AbstractProvider;

/**
 *
 * @author  Antonio Petrelli
 */
public class AuthorDeleter extends medi.db.deleter.AbstractDeleter {

    /** Creates a new instance of DataDeleter */
    public AuthorDeleter(AbstractProvider pPrv) {
        super(pPrv);
    }

    public void deleteAll(Object[] ID) throws DbException {
        prv.removeAuthorClean((Integer) ID[0]);
    }

    public void deletePreserving(Object[] ID) throws DbException {
        prv.removeAuthor((Integer) ID[0]);
    }
}
