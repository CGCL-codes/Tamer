package org.dbunit.database;

import com.mockobjects.ExpectationCounter;
import com.mockobjects.Verifiable;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultTableMetaData;
import org.dbunit.dataset.ITableMetaData;

/**
 * @author Manuel Laflamme
 * @since Apr 12, 2003
 * @version $Revision: 421 $
 */
public class MockResultSetTable implements IResultSetTable, Verifiable {

    private final ExpectationCounter _closeCalls = new ExpectationCounter("MockResultSetTable.close");

    private ITableMetaData _metaData;

    public void setupTableMetaData(String tableName) {
        _metaData = new DefaultTableMetaData(tableName, new Column[0]);
    }

    public void setExpectedCloseCalls(int callsCount) {
        _closeCalls.setExpected(callsCount);
    }

    public void verify() {
        _closeCalls.verify();
    }

    public Object getValue(int row, String column) throws DataSetException {
        return null;
    }

    public int getRowCount() {
        return 0;
    }

    public ITableMetaData getTableMetaData() {
        return _metaData;
    }

    public void close() throws DataSetException {
        _closeCalls.inc();
    }
}
