package net.effigent.jdownman.queue.store.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import net.effigent.jdownman.Download;
import net.effigent.jdownman.DownloadException;
import net.effigent.jdownman.Download.ChunkDownload;
import net.effigent.jdownman.Download.STATUS;

/**
 * @author vipul
 *
 */
public interface JDBCAdapter {

    /**
	 * 
	 * @param connection 
	 * @param download
	 * @throws SQLException
	 */
    public void persistDownload(Connection connection, Download download) throws SQLException;

    /**
	 * 
	 * @param chunk
	 * @throws SQLException
	 */
    public void persistChunk(Connection connection, ChunkDownload chunk) throws SQLException;

    /**
	 * 
	 * @param connection
	 * @throws SQLException
	 */
    public void doCreateTables(Connection connection) throws SQLException;

    /**
	 * 
	 * @param connection 
	 * @param id
	 * @param id2
	 * @param complete
	 * @throws SQLException
	 */
    public void updateChunkStatus(Connection connection, String id, int id2, STATUS complete) throws SQLException;

    /**
	 * 
	 * @param connection 
	 * @param id
	 * @param cancelled
	 * @throws SQLException
	 */
    public void updateDownloadStatus(Connection connection, String id, STATUS cancelled) throws SQLException;

    /**
	 * 
	 * @param connection 
	 * @param id
	 * @param e
	 * @throws SQLException
	 */
    public void registerException(Connection connection, String id, DownloadException e) throws SQLException;

    /**
	 * 
	 * @param connection 
	 * @param id
	 * @param totalFileLength
	 * @throws SQLException
	 */
    public void setDownloadSize(Connection connection, String id, long totalFileLength) throws SQLException;

    /**
	 * 
	 * @param statementProvider
	 */
    public void setStatements(Statements statements);

    /**
     * 
     * @param connection
     * @return
     */
    public List<Download> getPendingDownloads(Connection connection) throws SQLException;

    /**
	 * 
	 * @param connection
	 * @param id
	 */
    public void cleanupCompletedDownload(Connection connection, String id) throws SQLException;
}
