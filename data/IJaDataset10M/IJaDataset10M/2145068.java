package de.searchworkorange.searchserver.searcher.twinfilefinding;

import de.searchworkorange.lib.configuration.ConfigFileParameterException;
import de.searchworkorange.lib.database.DBUnknownDBDriverException;
import de.searchworkorange.lib.database.DBUnknownHowToDoException;
import de.searchworkorange.lib.database.SQLHandlerDriverDisposer;
import de.searchworkorange.lib.database.DatabaseServerNotReachableException;
import de.searchworkorange.lib.database.MyDatabaseSQLHandler;
import de.searchworkorange.lib.database.NoSQLDriverDisposerIsSetException;
import de.searchworkorange.lib.logger.LoggerCollection;
import de.searchworkorange.searchserver.server.configuration.ConfigurationCollection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Sascha Kriegesmann kriegesmann at vaxnet.de
 */
public class SQLHandler extends MyDatabaseSQLHandler {

    private static final boolean CLASSDEBUG = true;

    private int counter = 0;

    /**
     * 
     * @param loggerCol
     * @param configCol
     * @throws ConfigFileParameterException
     * @throws DatabaseServerNotReachableException
     * @throws DBUknownHowToDoException
     * @throws DBUknownDBDriverException
     */
    public SQLHandler(LoggerCollection loggerCol, ConfigurationCollection configCol) throws ConfigFileParameterException, DatabaseServerNotReachableException, DBUnknownHowToDoException, DBUnknownDBDriverException {
        super(loggerCol, configCol.getDatabaseConfig());
        super.setDriverDisposerAndInit(new SQLHandlerDriverDisposer(loggerCol, configCol.getDatabaseConfig(), this));
    }

    /**
     *
     * @return Statement
     * @throws NoSQLDriverDisposerIsSetException
     * @throws SQLException 
     */
    @Override
    public Statement openConnectionAndGetStatement() throws NoSQLDriverDisposerIsSetException, SQLException {
        return super.openConnectionAndGetStatement();
    }

    @Override
    public void closeConnection() {
        super.closeConnection();
    }
}
