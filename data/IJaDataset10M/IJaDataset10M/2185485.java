package org.ceno.tracker.srv.handler.internal;

import org.ceno.communication.srv.ICommunicatorServiceListener;
import org.ceno.communication.srv.IDatabaseConnectionProvider;
import org.ceno.communication.srv.NoDatabaseConnectionAvailableException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.eclipse.core.runtime.Status;

/**
 * @author Andre Albert &lt;andre.albert82@googlemail.com&gt
 * @created 01.01.2010
 * @since 0.0.1
 */
public class DatabasePersistenceInitializer implements IPersistenceInitializer, ICommunicatorServiceListener {

    private final IDatabaseConnectionProvider dbConnectionProvider;

    private Connection conn;

    public DatabasePersistenceInitializer(final IDatabaseConnectionProvider dbConnectionProvider) {
        this.dbConnectionProvider = dbConnectionProvider;
    }

    /**
	 * {@inheritDoc}
	 **/
    public void initTables() throws PersistenceException {
        Activator.log(Status.OK, "Initializing Tables");
        Statement statement = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = dbConnectionProvider.getConnection();
            statement = conn.createStatement();
            try {
                statement.execute(findQuery("create.metadata"));
            } catch (final SQLException e) {
                Activator.log(Status.OK, "Could not create metadata table. Maybe already exists");
            }
            try {
                final ResultSet resultSet = statement.executeQuery(findQuery("select.metaversion"));
                if (resultSet.next()) {
                    final String dbVersion = resultSet.getString(1);
                    if (dbVersion.equals(Activator.PLUGIN_VERSION)) {
                        Activator.log(Status.INFO, "Database schema up to date");
                        return;
                    }
                }
                preparedStatement = conn.prepareStatement(findQuery("update.metaversion"));
                preparedStatement.setString(1, Activator.PLUGIN_VERSION);
                preparedStatement.executeUpdate();
            } catch (final SQLException e) {
                Activator.log(Status.ERROR, "Could not save version information");
            }
            try {
                statement.execute(findQuery("drop.developerresourcelog"));
            } catch (final SQLException e) {
                Activator.log(Status.OK, "Ignoring table to drop.");
            }
            try {
                statement.execute(findQuery("drop.developerresource"));
            } catch (final SQLException e) {
                Activator.log(Status.OK, "Ignoring table to drop.");
            }
            try {
                statement.execute(findQuery("drop.message"));
            } catch (final SQLException e) {
                Activator.log(Status.OK, "Ignoring table to drop.");
            }
            try {
                statement.execute(findQuery("drop.resource"));
            } catch (final SQLException e) {
                Activator.log(Status.OK, "Ignoring table to drop.");
            }
            try {
                statement.execute(findQuery("drop.developer"));
            } catch (final SQLException e) {
                Activator.log(Status.OK, "Error dropping table. Save to ignore this message");
            }
            try {
                statement.execute(findQuery("create.developer"));
                statement.execute(findQuery("index.developername"));
                Activator.log(Status.OK, "created Developer table");
            } catch (final SQLException e) {
                Activator.log(Status.OK, "Table developer already exist. Save to ignore this message");
            }
            try {
                statement.execute(findQuery("create.resource"));
                statement.execute(findQuery("index.resourcefqn"));
                Activator.log(Status.OK, "created Resource table");
            } catch (final SQLException e) {
                Activator.log(Status.OK, "Table resource already exist. Save to ignore this message");
            }
            try {
                statement.execute(findQuery("create.developerresource"));
                Activator.log(Status.OK, "created DeveloperResource table");
            } catch (final SQLException e) {
                Activator.log(Status.OK, "Table developerresource already exist. Save to ignore this message");
            }
            try {
                statement.execute(findQuery("create.developerresourcelog"));
            } catch (final SQLException e) {
                Activator.log(Status.OK, "Table developerresourcelog already exist. Save to ignore this message");
            }
            try {
                statement.execute(findQuery("create.message"));
                Activator.log(Status.OK, "created Message table");
            } catch (final SQLException e) {
                Activator.log(Status.OK, "Table message already exist. Save to ignore this message");
            }
        } catch (final SQLException e) {
            throw new PersistenceException("Could not create statement", e);
        } catch (NoDatabaseConnectionAvailableException e) {
            throw new PersistenceException("Could not obtain a Database Connection", e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (final SQLException e) {
                Activator.log(Status.ERROR, "Could no close JDBC Connection", e);
            }
        }
    }

    /**
	 * {@inheritDoc}
	 **/
    public void serverStarted(final String identifier) {
        try {
            initTables();
            Activator.getCenoDao().clearDeveloperResourceStates();
        } catch (final PersistenceException e) {
            Activator.log(Status.ERROR, "Could not initialize tables", e);
        }
    }

    /**
	 * {@inheritDoc}
	 **/
    public void serverStopped(final String identifier) {
    }

    private String findQuery(final String queryName) throws PersistenceException {
        final String query = Activator.getSqlQueryProvider().findQuery(queryName);
        if (query == null || query.isEmpty()) {
            throw new PersistenceException("Query " + queryName + " not found in QueryProvider");
        }
        return query;
    }
}
