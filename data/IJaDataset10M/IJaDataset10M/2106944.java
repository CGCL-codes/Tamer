package desmoj.extensions.db.dao;

import desmoj.extensions.db.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Savepoint;
import java.sql.SQLException;

public abstract class DAO<T> {

    protected DBConnection connection;

    protected IDDAO parentDAO;

    private PreparedStatement saveStatement = null;

    private PreparedStatement loadStatement = null;

    private PreparedStatement testStatement = null;

    private ResultSet loadResultSet = null;

    private ResultSet testResultSet = null;

    protected boolean testResultsLeft = false;

    protected boolean loadResultsLeft = false;

    public DAO(DBConnection connection, IDDAO parentDAO) {
        this.connection = connection;
        this.parentDAO = parentDAO;
    }

    public DAO(DBConnection connection) {
        this(connection, null);
    }

    public boolean isSavable() {
        return (this instanceof SavableDAO);
    }

    public boolean isLoadable() {
        return (this instanceof LoadableDAO);
    }

    public boolean isIdenticalTestable() {
        return (this instanceof IdenticalTestableDAO);
    }

    public boolean hasParentDAO() {
        return (parentDAO != null);
    }

    public void save(T object) {
        if (isSavable()) {
            try {
                PreparedStatement stmnt = ((SavableDAO<T>) this).getSaveStatement(object);
                stmnt.executeUpdate();
                stmnt.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        } else {
        }
    }

    public T load() {
        T result = null;
        if (isLoadable()) {
            try {
                if (loadResultSet == null) {
                    loadStatement = ((LoadableDAO<T>) this).getLoadStatement();
                    loadResultSet = loadStatement.executeQuery();
                }
                if (loadResultSet.next()) {
                    result = ((LoadableDAO<T>) this).toResult(loadResultSet);
                    loadResultsLeft = resultsLeft(loadStatement, loadResultSet);
                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        } else {
        }
        return result;
    }

    public boolean identicalToDatabase(T object) {
        boolean result = false;
        if (isIdenticalTestable()) {
            try {
                if (testResultSet == null) {
                    testStatement = ((IdenticalTestableDAO) this).getIdenticalStatement(object);
                    testResultSet = testStatement.executeQuery();
                }
                if (testResultSet.next()) {
                    result = ((IdenticalTestableDAO) this).getTestResult(testResultSet, object);
                    testResultsLeft = resultsLeft(testStatement, testResultSet);
                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        } else {
        }
        return result;
    }

    private boolean resultsLeft(PreparedStatement statement, ResultSet resultSet) throws SQLException {
        boolean result = true;
        if (!resultSet.next()) {
            result = false;
            resultSet.close();
            resultSet = null;
            statement.close();
            statement = null;
        } else {
            resultSet.previous();
        }
        return result;
    }

    protected void finalize() {
        try {
            if (saveStatement != null) {
                saveStatement.close();
            }
            if (loadStatement != null) {
                loadStatement.close();
            }
            if (testStatement != null) {
                testStatement.close();
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            saveStatement = null;
            loadStatement = null;
            testStatement = null;
        }
    }
}
