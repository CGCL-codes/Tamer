package ca.llsutherland.squash.persistence;

import ca.llsutherland.squash.exceptions.ValidationException;

public class ReadOnlyTransactionHandler {

    protected static final ReadOnlyTransactionHandler instance = new ReadOnlyTransactionHandler();

    public static ReadOnlyTransactionHandler getInstance() {
        return instance;
    }

    public Object runTransaction(Transaction t) throws ValidationException {
        Object result = null;
        AbstractDatabaseConnection db = DatabaseConnectionManager.getInstance().getDatabaseConnection();
        try {
            db.initializeConnection();
            db.isCurrentConnectionReadOnly(true);
            result = executeTransaction(t);
        } catch (ValidationException vex) {
            throw vex;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.returnCurrentConnection();
            }
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
