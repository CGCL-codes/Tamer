package ca.llsutherland.squash.dao;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import ca.llsutherland.squash.exceptions.ValidationException;
import ca.llsutherland.squash.persistence.DatabaseConnectionManager;
import ca.llsutherland.squash.utils.StringUtils;

public abstract class BaseDao {

    protected Long create(String sql) {
        Statement statement = null;
        try {
            statement = DatabaseConnectionManager.getInstance().getDatabaseConnection().getCurrentConnection().createStatement();
            int rowsAffected = statement.executeUpdate(sql);
            if (rowsAffected < 1) {
                throw new ValidationException("Database Create failed: " + sql);
            }
            Long lastInsertedId = findLastInsertedId();
            DatabaseConnectionManager.getInstance().registerNewDomainObject(Long.valueOf(lastInsertedId), getUnderlyingTableName());
            return lastInsertedId;
        } catch (SQLException e) {
            throw new ValidationException(e.getMessage() + " : " + sql);
        } finally {
            statement = null;
        }
    }

    protected void delete(String sql, Long idToRemove) throws ValidationException {
        Statement statement = null;
        try {
            statement = DatabaseConnectionManager.getInstance().getDatabaseConnection().getCurrentConnection().createStatement();
            int rowsAffected = statement.executeUpdate(sql);
            if (rowsAffected < 1) {
                DatabaseConnectionManager.getInstance().removeDomainObject(idToRemove);
                throw new ValidationException("Database Delete failed: " + sql);
            }
        } catch (SQLException e) {
            throw new ValidationException(e.getMessage() + " : " + sql);
        } finally {
            statement = null;
        }
    }

    protected ResultSet executeStoredProc(String sql) {
        CallableStatement statement = null;
        try {
            statement = DatabaseConnectionManager.getInstance().getDatabaseConnection().getCurrentConnection().prepareCall(sql);
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            throw new ValidationException(e.getMessage() + " : " + sql);
        } finally {
            statement = null;
        }
    }

    protected ResultSet findByQuery(String sql) {
        Statement statement = null;
        try {
            statement = DatabaseConnectionManager.getInstance().getDatabaseConnection().getCurrentConnection().createStatement();
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            throw new ValidationException(e.getMessage() + " : " + sql);
        } finally {
            statement = null;
        }
    }

    protected Long findLastInsertedId() {
        int id = -1;
        String sql = "SELECT LAST_INSERT_ID()";
        ResultSet rs = findByQuery(sql);
        try {
            if (rs.next()) {
                id = rs.getInt(1);
            }
            rs.close();
            return Long.valueOf(id);
        } catch (SQLException e) {
            throw new ValidationException(e.getMessage() + " : " + sql);
        } finally {
            rs = null;
        }
    }

    public Long update(String sql) {
        Statement statement = null;
        try {
            statement = DatabaseConnectionManager.getInstance().getDatabaseConnection().getCurrentConnection().createStatement();
            int rowsAffected = statement.executeUpdate(sql);
            if (rowsAffected < 1) {
                throw new ValidationException("Database Updated failed: " + sql);
            }
            return findLastInsertedId();
        } catch (SQLException e) {
            throw new ValidationException(e.getMessage() + " : " + sql);
        } finally {
            statement = null;
        }
    }

    public abstract String getUnderlyingTableName();

    protected String getFindByIdQuery(Long id) {
        return "SELECT * FROM " + getUnderlyingTableName() + " WHERE ID = " + id + ";";
    }

    protected String getFindByNameQuery(String name) {
        return "SELECT * FROM " + getUnderlyingTableName() + " WHERE NAME = " + StringUtils.addSingleQuotes(name) + ";";
    }
}
