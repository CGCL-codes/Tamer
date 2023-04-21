package org.dbunit.operation;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import java.sql.SQLException;

/**
 * Defines the interface contract for operations performed on the database.
 *
 * @author Manuel Laflamme
 * @version $Revision: 810 $
 * @since Feb 18, 2002
 */
public abstract class DatabaseOperation {

    public static final DatabaseOperation NONE = new DummyOperation();

    public static final DatabaseOperation UPDATE = new UpdateOperation();

    public static final DatabaseOperation INSERT = new InsertOperation();

    public static final DatabaseOperation REFRESH = new RefreshOperation();

    public static final DatabaseOperation DELETE = new DeleteOperation();

    public static final DatabaseOperation DELETE_ALL = new DeleteAllOperation();

    public static final DatabaseOperation TRUNCATE_TABLE = new TruncateTableOperation();

    public static final DatabaseOperation CLEAN_INSERT = new CompositeOperation(DELETE_ALL, INSERT);

    public static final DatabaseOperation TRANSACTION(DatabaseOperation operation) {
        return new TransactionOperation(operation);
    }

    public static final DatabaseOperation CLOSE_CONNECTION(DatabaseOperation operation) {
        return new CloseConnectionOperation(operation);
    }

    /**
     * Executes this operation on the specified database using the specified
     * dataset contents.
     *
     * @param connection the database connection.
     * @param dataSet the dataset to be used by this operation.
     */
    public abstract void execute(IDatabaseConnection connection, IDataSet dataSet) throws DatabaseUnitException, SQLException;

    private static class DummyOperation extends DatabaseOperation {

        public void execute(IDatabaseConnection connection, IDataSet dataSet) {
        }
    }
}
