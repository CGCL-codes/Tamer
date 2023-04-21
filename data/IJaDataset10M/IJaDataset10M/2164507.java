package org.dbunit.dataset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dbunit.dataset.filter.IColumnFilter;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Manuel Laflamme
 * @version $Revision: 677 $
 * @since May 11, 2004
 */
public class FilteredTableMetaData extends AbstractTableMetaData {

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(FilteredTableMetaData.class);

    private final String _tableName;

    private final Column[] _columns;

    private final Column[] _primaryKeys;

    public FilteredTableMetaData(ITableMetaData metaData, IColumnFilter columnFilter) throws DataSetException {
        _tableName = metaData.getTableName();
        _columns = getFilteredColumns(_tableName, metaData.getColumns(), columnFilter);
        _primaryKeys = getFilteredColumns(_tableName, metaData.getPrimaryKeys(), columnFilter);
    }

    public static Column[] getFilteredColumns(String tableName, Column[] columns, IColumnFilter columnFilter) {
        if (logger.isDebugEnabled()) {
            logger.debug("getFilteredColumns(tableName={}, columns={}, columnFilter={}) - start", new Object[] { tableName, columns, columnFilter });
        }
        if (columns == null) {
            return new Column[0];
        }
        List columnList = new ArrayList();
        for (int i = 0; i < columns.length; i++) {
            Column column = columns[i];
            if (columnFilter.accept(tableName, column)) {
                columnList.add(column);
            }
        }
        return (Column[]) columnList.toArray(new Column[0]);
    }

    public String getTableName() {
        return _tableName;
    }

    public Column[] getColumns() throws DataSetException {
        return _columns;
    }

    public Column[] getPrimaryKeys() throws DataSetException {
        return _primaryKeys;
    }
}
