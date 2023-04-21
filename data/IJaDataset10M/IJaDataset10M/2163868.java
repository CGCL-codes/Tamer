package net.persister.exception;

import java.sql.SQLException;

/**
 * @author Park, chanwook
 *
 */
public class TranslatedSQLException extends PersistentContextException {

    public TranslatedSQLException() {
        super("throw TranslatedSQLException");
    }

    public TranslatedSQLException(String message) {
        super(message);
    }

    public TranslatedSQLException(String message, SQLException cuase) {
        super(message, cuase);
    }

    public TranslatedSQLException(SQLException e) {
        super(e.getMessage(), e);
    }
}
