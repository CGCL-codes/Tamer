package org.dbunit.dataset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Specialized IDataSet decorator that convert the table name and
 * column names to lower case. Used in DbUnit own test suite to verify that
 * operations are case insensitive.
 *
 * @author Manuel Laflamme
 * @version $Revision: 677 $
 * @since Feb 14, 2003
 */
public class LowerCaseDataSet extends AbstractDataSet {

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(LowerCaseDataSet.class);

    private final IDataSet _dataSet;

    public LowerCaseDataSet(ITable table) throws DataSetException {
        this(new DefaultDataSet(table));
    }

    public LowerCaseDataSet(ITable[] tables) throws DataSetException {
        this(new DefaultDataSet(tables));
    }

    public LowerCaseDataSet(IDataSet dataSet) throws DataSetException {
        _dataSet = dataSet;
    }

    private ITable createLowerTable(ITable table) throws DataSetException {
        logger.debug("createLowerTable(table={}) - start", table);
        return new CompositeTable(new LowerCaseTableMetaData(table.getTableMetaData()), table);
    }

    protected ITableIterator createIterator(boolean reversed) throws DataSetException {
        logger.debug("createIterator(reversed={}) - start", String.valueOf(reversed));
        return new LowerCaseIterator(reversed ? _dataSet.reverseIterator() : _dataSet.iterator());
    }

    public String[] getTableNames() throws DataSetException {
        logger.debug("getTableNames() - start");
        String[] tableNames = super.getTableNames();
        for (int i = 0; i < tableNames.length; i++) {
            tableNames[i] = tableNames[i].toLowerCase();
        }
        return tableNames;
    }

    public ITableMetaData getTableMetaData(String tableName) throws DataSetException {
        logger.debug("getTableMetaData(tableName={}) - start", tableName);
        return new LowerCaseTableMetaData(super.getTableMetaData(tableName));
    }

    public ITable getTable(String tableName) throws DataSetException {
        logger.debug("getTable(tableName={}) - start", tableName);
        return createLowerTable(super.getTable(tableName));
    }

    private class LowerCaseIterator implements ITableIterator {

        private final ITableIterator _iterator;

        public LowerCaseIterator(ITableIterator iterator) {
            _iterator = iterator;
        }

        public boolean next() throws DataSetException {
            return _iterator.next();
        }

        public ITableMetaData getTableMetaData() throws DataSetException {
            return new LowerCaseTableMetaData(_iterator.getTableMetaData());
        }

        public ITable getTable() throws DataSetException {
            return createLowerTable(_iterator.getTable());
        }
    }
}
