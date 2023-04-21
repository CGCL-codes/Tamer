package com.dbxml.db.common.fulltext;

import com.dbxml.db.common.query.ResultSetBase;
import com.dbxml.db.core.Collection;
import com.dbxml.db.core.DBException;
import com.dbxml.db.core.data.Key;
import com.dbxml.db.core.query.ProcessingException;
import com.dbxml.db.core.query.Query;
import com.dbxml.db.core.query.QueryException;
import com.dbxml.db.core.transaction.Transaction;
import com.dbxml.xml.dtsm.Constants;
import com.dbxml.xml.dtsm.DocumentTable;

/**
 * FullTextResultSet
 */
public final class FullTextResultSet extends ResultSetBase {

    public FullTextResultSet(Transaction tx, Collection context, Query query, Key[] keySet) {
        super(tx, context, query, keySet);
    }

    public boolean next() throws QueryException {
        checkOpened();
        return nextKey() != null;
    }

    public int getResultType() throws QueryException {
        checkOpened();
        return Constants.OBJ_BEGIN_DOCUMENT;
    }

    public DocumentTable getResult() throws QueryException {
        checkOpened();
        try {
            return context.getDocument(tx, currentKey);
        } catch (DBException e) {
            throw new ProcessingException("Error retrieving Document '" + currentKey.toString() + "'", e);
        }
    }
}
