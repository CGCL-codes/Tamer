package org.dbunit.dataset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Manuel Laflamme
 * @since Apr 9, 2003
 * @version $Revision: 679 $
 */
public class ForwardOnlyTable implements ITable {

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(ForwardOnlyTable.class);

    private final ITable _table;

    private int _lastRow = -1;

    public ForwardOnlyTable(ITable table) {
        _table = table;
    }

    public ITableMetaData getTableMetaData() {
        return _table.getTableMetaData();
    }

    public int getRowCount() {
        throw new UnsupportedOperationException();
    }

    public Object getValue(int row, String column) throws DataSetException {
        if (logger.isDebugEnabled()) logger.debug("getValue(row={}, columnName={}) - start", Integer.toString(row), column);
        if (row < _lastRow) {
            throw new UnsupportedOperationException("Cannot go backward!");
        }
        _lastRow = row;
        return _table.getValue(row, column);
    }
}
