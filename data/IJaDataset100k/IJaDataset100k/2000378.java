package ca.llsutherland.squash.persistence;

import java.sql.SQLException;
import ca.llsutherland.squash.exceptions.ValidationException;

public class TransactionHandler {

    protected static final TransactionHandler instance = new TransactionHandler();

    public static TransactionHandler getInstance() {
        return instance;
    }

    public Object runTransaction(Transaction t) throws ValidationException {
        Object result = null;
        AbstractDatabaseConnection db = DatabaseConnectionManager.getInstance().getDatabaseConnection();
        try {
            db.initializeConnection();
            db.isCurrentConnectionAutoCommit(false);
            db.isCurrentConnectionReadOnly(false);
            result = executeTransaction(t);
            db.commitTransaction();
        } catch (Exception ex) {
            try {
                db.rollback();
            } catch (SQLException e) {
                throw new ValidationException(e.getMessage());
            }
            throw new ValidationException(ex.getMessage());
        } finally {
            db.returnCurrentConnection();
        }
        return result;
    }

    public Object executeTransaction(Transaction t) throws ValidationException {
        Object result = null;
        try {
            result = t.executeTransaction();
        } finally {
        }
        return result;
    }
}
