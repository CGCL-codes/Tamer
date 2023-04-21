package org.dbunit.operation;

import java.util.Arrays;
import org.dbunit.dataset.Column;

/**
 * @author Manuel Laflamme
 * @version $Revision: 770 $
 * @since Mar 16, 2002
 */
public class OperationData {

    private final String _sql;

    private final Column[] _columns;

    /**
     * @param sql
     * @param columns
     */
    public OperationData(String sql, Column[] columns) {
        _sql = sql;
        _columns = columns;
    }

    public String getSql() {
        return _sql;
    }

    public Column[] getColumns() {
        return _columns;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(getClass().getName()).append("[");
        sb.append("_sql=").append(_sql);
        sb.append(", _columns=").append(_columns == null ? "null" : Arrays.asList(_columns).toString());
        sb.append("]");
        return sb.toString();
    }
}
