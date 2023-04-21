package org.enjavers.jethro.dspi.adapters;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import org.enjavers.jethro.api.adapters.QLAdapter;

/**
 * @author Alessandro Lombardi
 */
public class PostgreQLAdapter extends QLAdapterSPI {

    private static PostgreQLAdapter _SINGLETON_ADAPTER = null;

    private PostgreQLAdapter() {
    }

    public static QLAdapter getAdapter() {
        if (_SINGLETON_ADAPTER == null) _SINGLETON_ADAPTER = new PostgreQLAdapter();
        return _SINGLETON_ADAPTER;
    }

    /**
	 * This method provides an override of org.enjavers.jethro.dspi.adapters.QLAdapter#getObject(java.sql.ResultSet, java.lang.String, java.lang.Class)
	 * It simply use java.sql.ResultSet#getObject(Object).
	 * @see org.enjavers.jethro.dspi.adapters.QLAdapter#getObject(java.sql.ResultSet, java.lang.String, java.lang.Class)
	 */
    public Object getObject(ResultSet i_rs, String i_descriptor, Class i_class_type) throws SQLException {
        return i_rs.getObject(i_descriptor);
    }

    /**
	 * Format a Date Object for PostgreSQL adapting.
	 * @param i_date The date object.
	 * @return String The formatted String.
	 */
    protected String formatDate(java.util.Date i_date) {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        return "'" + sdFormat.format(i_date) + "'";
    }
}
