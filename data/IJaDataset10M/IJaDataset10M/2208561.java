package org.josef.dmc;

import static org.josef.annotations.Status.Stage.PRODUCTION;
import static org.josef.annotations.Status.UnitTests.COMPLETE;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.josef.annotations.Review;
import org.josef.annotations.Reviews;
import org.josef.annotations.Status;
import org.josef.util.GlobalSettings;

/**
 * DMC (Data Management Component) Utility class.
 * @author Kees Schotanus
 * @version 1.1 $Revision: 3091 $
 */
@Status(stage = PRODUCTION, unitTests = COMPLETE)
@Reviews({ @Review(by = "Kees Schotanus", at = "2009-04-11") })
public final class DmcUtil {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = Logger.getLogger(DmcUtil.class.getName());

    /**
     * Private constructor prevents creation of an instance outside this class.
     */
    private DmcUtil() {
    }

    /**
     * Gets a database connection from the database that is configured in
     * {@link GlobalSettings}.
     * @return A database connection from the database that is configured in
     *  {@link GlobalSettings}.
     * @throws SQLException When no connection could be obtained.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(GlobalSettings.getDatabaseUrl());
    }

    /**
     * Closes the supplied resultSet, statement and connection.
     * <br>A resource that could not be closed is logged as a warning.
     * @param resultSet The resultSet to close or null.
     * @param statement The statement to close or null.
     * @param connection The connection to close or null.
     */
    public static void close(final ResultSet resultSet, final Statement statement, final Connection connection) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (final SQLException exception) {
                LOGGER.log(Level.WARNING, "Could not close result set", exception);
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (final SQLException exception) {
                LOGGER.log(Level.WARNING, "Could not close statement", exception);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (final SQLException exception) {
                LOGGER.log(Level.WARNING, "Could not close connection", exception);
            }
        }
    }
}
