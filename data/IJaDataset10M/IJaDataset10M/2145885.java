package databaseVersionControl.infra.connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import databaseVersionControl.domain.db.Translatable;
import databaseVersionControl.domain.dialect.Dialect;
import databaseVersionControl.domain.script.ExecutableScript;
import databaseVersionControl.infra.exception.DVCGeneralException;
import databaseVersionControl.infra.script.ScriptRunner;
import databaseVersionControl.infra.script.validation.PreInstallCondition;

public class DataBaseProxy {

    public static final String SEQUENCE_NAME = "DVC_SEQ";

    private static Logger logger = Logger.getLogger(ScriptRunner.class);

    private static Dialect currentDialect;

    public static String nextSequenceValue() {
        return currentDialect.getNextSequenceValue(SEQUENCE_NAME);
    }

    public static void executeStatement(String sql) throws SQLException {
        PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql);
        logger.info(sql);
        ps.executeUpdate();
    }

    public static ResultSet executeQuery(String sqlQuery) throws SQLException {
        PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sqlQuery);
        logger.info(sqlQuery);
        return ps.executeQuery();
    }

    public static long getNextId() throws SQLException {
        ResultSet executeQuery = executeQuery(nextSequenceValue());
        executeQuery.next();
        return executeQuery.getLong(1);
    }

    public static boolean existsTable(String name) {
        String sql = String.format("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '%s'", name);
        try {
            ResultSet executeQuery = executeQuery(sql);
            return numberOfRowReturned(executeQuery) == 1;
        } catch (SQLException e) {
            throw new DVCGeneralException("Check table exists fail.", e);
        }
    }

    private static int numberOfRowReturned(ResultSet executeQuery) {
        int size = -1;
        try {
            executeQuery.last();
            size = executeQuery.getRow();
            return size;
        } catch (SQLException e) {
            throw new DVCGeneralException("Check number of rows of table fail.", e);
        }
    }

    public static boolean checkPreInstallCondition(PreInstallCondition<?> preInstallCondition) {
        try {
            ResultSet executeQuery = executeQuery(translate(preInstallCondition));
            return numberOfRowReturned(executeQuery) == preInstallCondition.getValueCondition();
        } catch (SQLException e) {
            throw new DVCGeneralException("Check table exists fail.", e);
        }
    }

    public static void setDialect(Dialect dialect) {
        currentDialect = dialect;
    }

    public static Dialect dialect() {
        return currentDialect;
    }

    private static void execute(ExecutableScript script) throws SQLException {
        executeStatement(translate(script));
    }

    public static String translate(Translatable translatable) {
        return translatable.sqlInDialect(currentDialect);
    }

    public static void install(ExecutableScript script) throws SQLException {
        if (canExecute(script)) execute(script);
    }

    private static boolean canExecute(ExecutableScript script) {
        if (!script.hasPreInstallCondition()) return true;
        return checkPreInstallCondition(script.getPreInstallCondition());
    }

    public static void connect() {
        ConnectionFactory.getConnection();
    }

    public static void setConnectionInfo(ConnectionInfo connectionInfo) {
        ConnectionFactory.setConnectionInfo(connectionInfo);
    }

    public static void reset() {
        ConnectionFactory.reset();
    }
}
