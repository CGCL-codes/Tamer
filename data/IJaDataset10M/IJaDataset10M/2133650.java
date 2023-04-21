package net.sourceforge.eclipsetrader.briter.databasecache;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.eclipse.core.runtime.CoreException;
import net.sourceforge.eclipsetrader.core.db.History;
import net.sourceforge.eclipsetrader.core.db.Security;

public interface IBriterDatabase {

    /**
	 * Create database and tables. Do nothing if the database is exist.
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
    public void initialize() throws CoreException;

    /**
	 * Load daily data from file to table
	 * 
	 * @param files
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
    public void fileToDataBaseForDaily(final List<File> files) throws CoreException;

    /**
	 * Load hourly data from file to table
	 * 
	 * @param files
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
    public void fileToDataBaseForHourly(final List<File> files) throws CoreException;

    /**
	 * Read history data for daily security
	 * 
	 * @param security
	 * @param history
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
    public void readHistoryDaily(Security security, History history) throws CoreException;

    /**
	 * Read history data for hourly security
	 * 
	 * @param security
	 * @param history
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
    public void readHistoryHourly(Security security, History history) throws CoreException;

    public void readLatestFeed(Security security) throws CoreException;

    /**
	 * Dispose and release connection if exists.
	 * 
	 * @throws SQLException
	 */
    public void close() throws CoreException;

    public Connection getConnection() throws ClassNotFoundException, SQLException;
}
