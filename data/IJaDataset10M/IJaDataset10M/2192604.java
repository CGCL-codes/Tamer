package macaw.persistenceLayer.production;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import macaw.MacawMessages;
import macaw.system.MacawErrorType;
import macaw.system.MacawException;

public class SQLUtilities {

    public static void closeStatementsWithCatch(Statement statement, ResultSet resultSet) throws MacawException {
        try {
            SQLUtilities.closeStatementsWithoutCatch(statement, resultSet);
        } catch (SQLException exception) {
            String errorMessage = MacawMessages.getMessage("sql.error.unableToCloseConnection");
            MacawException macawException = new MacawException(MacawErrorType.UNABLE_TO_CLOSE_CONNECTION, errorMessage);
            throw macawException;
        }
    }

    public static void closeStatementsWithoutCatch(Statement statement, ResultSet resultSet) throws SQLException {
        if (statement != null) {
            statement.close();
        }
        if (resultSet != null) {
            resultSet.close();
        }
    }
}
