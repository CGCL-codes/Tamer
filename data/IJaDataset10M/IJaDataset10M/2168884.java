package org.dbe.composer.wfengine.bpel.server.logging;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.dbutils.ResultSetHandler;
import org.dbe.composer.wfengine.bpel.server.engine.IProcessLogger;
import org.dbe.composer.wfengine.bpel.server.engine.SdlEngineFactory;
import org.dbe.composer.wfengine.bpel.server.engine.storage.SdlStorageException;
import org.dbe.composer.wfengine.bpel.server.engine.storage.sql.SdlSQLConfig;
import org.dbe.composer.wfengine.bpel.server.engine.storage.sql.SdlSQLObject;
import org.dbe.composer.wfengine.util.SdlCloser;
import org.dbe.composer.wfengine.util.SdlUtil;

/**
 * Responsible for reading the log for a given process out of the database. The
 * log gets displayed in the admin console web page as well as the remote debugging
 * console window so we don't want to return the entire log file if it's huge.
 * Instead, we'll return a configurable head and tail portion of the log.
 */
public class SdlLogReader extends SdlSQLObject {

    private static final String SQL_GET_HEAD = "GetLogHead";

    private static final String SQL_GET_TAIL = "GetLogTail";

    private static final String SQL_GET_LOG_ENTRIES = "GetLogEntries";

    private static final String SQL_GET_SMALL_LOG = "GetSmallLog";

    /** shared instance of the resultset handler used to read small logs */
    private static final ResultSetHandler SMALL_LOG_HANDLER = new SdlSmallLogHandler();

    /** process id we're searching for */
    private Long mProcessId;

    /** contains the sql queries needed for this class */
    private SdlSQLConfig mSQLConfig;

    /** list of log entries for the process */
    private List mLogEntries;

    /** total number of lines for entire process log */
    private int mTotalLineCount;

    /** max number of lines to include in the head of the log */
    private int mHeadLimit;

    /** max number of lines to include in the tail of the log */
    private int mTailLimit;

    /**
     * Ctor
     * @param aProcessId
     */
    public SdlLogReader(long aProcessId, SdlSQLConfig aConfig) {
        mProcessId = new Long(aProcessId);
        mSQLConfig = aConfig;
        mHeadLimit = SdlEngineFactory.getSdlEngineConfig().getIntegerEntry("Logging.Head", IProcessLogger.DEFAULT_HEAD);
        mTailLimit = SdlEngineFactory.getSdlEngineConfig().getIntegerEntry("Logging.Tail", IProcessLogger.DEFAULT_TAIL);
    }

    /**
     * Reads the log for the process.
     */
    public String readLog() throws SdlStorageException, SQLException {
        mLogEntries = (List) getQueryRunner().query(getSQLStatement(SQL_GET_LOG_ENTRIES), mProcessId, new SdlLogEntryHandler());
        String result = null;
        if (getTotalLineCount() <= (mHeadLimit + mTailLimit)) {
            result = (String) getQueryRunner().query(getSQLStatement(SQL_GET_SMALL_LOG), mProcessId, SMALL_LOG_HANDLER);
        } else {
            int headCounterValue = findHeadCounterValue();
            result = getLogSegment(headCounterValue, SQL_GET_HEAD);
            result = result + IProcessLogger.SNIP + getLogSegment(findTailCounterValue(headCounterValue), SQL_GET_TAIL);
        }
        return result;
    }

    /**
     * Returns the segment of the log indicated by the counter value and the
     * sql statement.
     * @param aCounterValue value that identifies the row in the process log table to read
     * @param aSqlStatement
     * @throws SQLException
     * @throws SdlStorageException
     */
    private String getLogSegment(int aCounterValue, String aSqlStatement) throws SQLException, SdlStorageException {
        String result;
        Object[] args = { mProcessId, new Integer(aCounterValue) };
        result = (String) getQueryRunner().query(getSQLStatement(aSqlStatement), args, SMALL_LOG_HANDLER);
        return result;
    }

    /**
     * Finds the value of the counter from the sorted list that will give us the
     * number of entries we need for the head of the log.
     */
    protected int findHeadCounterValue() {
        return findCounter(mHeadLimit, -1);
    }

    /**
     * Finds the value of the counter from the sorted list that will give us the
     * number of entries we need for the tail of the log.
     */
    protected int findTailCounterValue(int aHeadCounterValue) {
        Collections.reverse(mLogEntries);
        int counter = findCounter(mHeadLimit, aHeadCounterValue);
        Collections.reverse(mLogEntries);
        return counter;
    }

    /**
     * Walks the list from the top, returning the value of the counter when it
     * gets the specified number of lines or greater.
     * @param aLimit - max number of lines we want
     * @param aStopValue - we'll stop searching when we come across the counter
     *                     with this value.
     */
    protected int findCounter(int aLimit, int aStopValue) {
        int lines = 0;
        int counter = 0;
        for (Iterator iter = mLogEntries.iterator(); iter.hasNext() && lines < aLimit; ) {
            SdlLogEntry entry = (SdlLogEntry) iter.next();
            counter = entry.getCounter();
            if (counter == aStopValue) break;
            lines += entry.getLines();
        }
        return counter;
    }

    /**
     * A ResultSetHandler that's responsible for walking the entire result set
     * and returning a String that represents the concatenation of all of the clobs.
     * As its name implies, this is designed for reading a small number of clobs since
     * it reads the entire contents of the clobs into memory.
     */
    protected static class SdlSmallLogHandler implements ResultSetHandler {

        /**
         * @see org.apache.commons.dbutils.ResultSetHandler#handle(java.sql.ResultSet)
         */
        public Object handle(ResultSet rs) throws SQLException {
            Reader reader = null;
            StringBuffer sb = new StringBuffer();
            try {
                synchronized (sb) {
                    char[] cbuf = null;
                    int read;
                    while (rs.next()) {
                        Clob clob = rs.getClob(1);
                        sb.ensureCapacity((int) (sb.length() + clob.length()));
                        cbuf = sizeArray(cbuf, (int) clob.length());
                        reader = clob.getCharacterStream();
                        while ((read = reader.read(cbuf)) != -1) {
                            sb.append(cbuf, 0, read);
                        }
                        reader.close();
                    }
                    return sb.toString();
                }
            } catch (IOException e) {
                SdlCloser.close(reader);
                throw new SQLException(e.getMessage());
            }
        }

        /**
         * Resizes the array passed in so it's at least the length of the
         * clob that we're trying to read.
         * @param aArray - can be null
         * @param aClobSize
         */
        private char[] sizeArray(char[] aArray, int aClobSize) {
            if (aArray == null || aArray.length < aClobSize) return new char[Math.max(aClobSize, 1024 * 4)];
            return aArray;
        }
    }

    /**
     * Converts the ResultSet into a <code>java.util.List</code> of AeLogEntry objects
     * sorted by their counter value. We're doing the sorting here to get around an
     * apparent performance issue with mysql where it was generating OutOfMemoryErrors
     * when trying to sort a small ResultSet.
     */
    protected class SdlLogEntryHandler implements ResultSetHandler {

        /**
         * @see org.apache.commons.dbutils.ResultSetHandler#handle(java.sql.ResultSet)
         */
        public Object handle(ResultSet rs) throws SQLException {
            List list = new ArrayList();
            while (rs.next()) {
                int counter = rs.getInt(1);
                int lines = rs.getInt(2);
                setTotalLineCount(getTotalLineCount() + lines);
                list.add(new SdlLogEntry(counter, lines));
            }
            Collections.sort(list);
            return list;
        }
    }

    /**
     * Container class for a clob row. Includes the number of lines in the clob
     * and the value of the counter field for the row.
     */
    protected static class SdlLogEntry implements Comparable {

        /** number of lines in the clob */
        private int mLines;

        /** value of the counter for the row */
        private int mCounter;

        /**
         * Ctor
         * @param aLines
         * @param aCounter
         */
        public SdlLogEntry(int aCounter, int aLines) {
            mLines = aLines;
            mCounter = aCounter;
        }

        /**
         * Getter for the counter
         */
        public int getCounter() {
            return mCounter;
        }

        /**
         * Geter for the number of lines
         */
        public int getLines() {
            return mLines;
        }

        /**
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        public int compareTo(Object o) {
            SdlLogEntry other = (SdlLogEntry) o;
            return getCounter() - other.getCounter();
        }
    }

    /**
     * Getter for the sql config
     */
    protected SdlSQLConfig getSQLConfig() {
        return mSQLConfig;
    }

    /**
     * Returns a SQL statement from the SQL configuration object. This
     * convenience method prepends the name of the statement with
     * "LogReader.".
     *
     * @param aStatementName The name of the statement, such as "InsertProcess".
     * @return The SQL statement found for that name.
     * @throws SdlStorageException If the SQL statement is not found.
     */
    protected String getSQLStatement(String aStatementName) throws SdlStorageException {
        String key = "LogReader." + aStatementName;
        String sql = getSQLConfig().getSQLStatement(key);
        if (SdlUtil.isNullOrEmpty(sql)) {
            throw new SdlStorageException("Warning: could not find SQL statement for key " + key + ".");
        }
        return sql;
    }

    /**
     * Getter for the total number of lines in process log
     */
    public int getTotalLineCount() {
        return mTotalLineCount;
    }

    /**
     * Setter for the total number of lines in process log
     * @param aI
     */
    public void setTotalLineCount(int aI) {
        mTotalLineCount = aI;
    }
}
