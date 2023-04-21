package org.snuvy;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class TableIterator {

    protected final DbTable _table;

    protected Cursor _cursor;

    protected DatabaseColumn _pk;

    DatabaseColumn _key = new DatabaseColumn();

    DatabaseColumn _value = new DatabaseColumn();

    public TableIterator(DbTable table) throws DatabaseException {
        _table = table;
        _cursor = _table.createCursor();
    }

    public DbRow next() throws DatabaseException {
        OperationStatus status = _cursor.getNext(_key.getEntry(), _value.getEntry(), LockMode.DEFAULT);
        if (status == OperationStatus.SUCCESS) {
            DbRow row = _table.getRow(_key);
            return row;
        }
        close();
        return null;
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        this.finalize();
    }

    private void close() throws DatabaseException {
        if (_cursor != null) {
            _cursor.close();
            _cursor = null;
        }
    }
}
