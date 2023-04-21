package net.sf.myfacessandbox.backend.database.springhibernate;

import java.util.Iterator;
import javax.faces.model.DataModel;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * 
 * @author Werner Punz werpu@gmx.at
 * 
 * this is a place holder class for a generic hibernate data model (todo the
 * session has to be set from the outside maybe over the servlet listener, we
 * have to check that one out)
 * 
 */
public class SpringHibernateDataModel extends DataModel {

    Query _query;

    Query _sizeQuery;

    Session _session;

    int _rowCount = -1;

    int _rowIndex = -1;

    int _lastRow = -1;

    Iterator _rowIterator = null;

    /**
	 * 
	 * @param query
	 *            the db fetch query
	 * @param sizeQuery
	 *            the row count query, we have to work with two different
	 *            queries thanks to hibernate limitations
	 * @param session
	 */
    public SpringHibernateDataModel(Query query, Query sizeQuery, Session session) {
        super();
        this._session = session;
        this._query = query;
        this._sizeQuery = sizeQuery;
        _rowIterator = query.iterate();
    }

    /**
	 * fetches the total number of rows of this query
	 */
    public int getRowCount() {
        if (_rowCount == -1) {
            _rowCount = ((Integer) _sizeQuery.iterate().next()).intValue();
        }
        return _rowCount;
    }

    /**
	 * 
	 */
    public Object getRowData() {
        Object retVal = null;
        if (_rowIndex == -1) return null;
        if ((_rowIndex - _lastRow) != 1) {
            _query.setFirstResult(_rowIndex);
            _rowIterator = _query.iterate();
        }
        if (_rowIterator.hasNext()) retVal = _rowIterator.next();
        _lastRow = _rowCount;
        return retVal;
    }

    public int getRowIndex() {
        return _rowIndex;
    }

    public Object getWrappedData() {
        return null;
    }

    public boolean isRowAvailable() {
        return _rowIndex < _rowCount;
    }

    public void setRowIndex(int arg0) {
        _rowIndex = arg0;
    }

    public void setWrappedData(Object arg0) {
    }

    public Query getQuery() {
        return _query;
    }

    public void setQuery(Query query) {
        this._query = query;
    }
}
