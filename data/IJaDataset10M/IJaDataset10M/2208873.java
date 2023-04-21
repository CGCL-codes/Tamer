package org.synthful.jdbc;

import java.sql.*;
import java.util.Date;
import java.util.Vector;
import org.synthful.jdbc.JDbLogin;
import org.synthful.jdbc.JRowBufferVector;
import org.synthful.jdbc.JDataBufferVector;
import org.synthful.jdbc.JDataArrayVector;
import org.synthful.jdbc.JDataColumns;
import org.synthful.jdbc.JDataColumn;

/**
 * JUtil Class.
 */
public class JUtil {

    /**
	 * Exec SQL.
	 * 
	 * @param jconn
	 *            the jconn as Connection
	 * @param sql
	 *            to be executed.
	 * 
	 * @return 1 if has results, 0 if has no results, -1 if failure.
	 */
    public static int SqlExec(Connection jconn, String sql) {
        try {
            boolean hasresults = jconn.createStatement().execute(sql);
            if (hasresults) return 1;
            return 0;
        } catch (SQLException ex) {
            System.out.println("JUtil.SqlExec:" + ex);
        } catch (NullPointerException ex) {
            System.out.println("JUtil.SqlExec:" + ex);
        }
        return -1;
    }

    /**
	 * Exec SQL.
	 * 
	 * @param jlogin
	 * @param sql
	 *            to be executed.
	 * 
	 * @return 1 if has results, 0 if has no results, -1 if failure.
	 */
    public static int SqlExec(JDbLogin jlogin, String sql) {
        try {
            boolean hasresults = jlogin.getStatement().execute(sql);
            if (hasresults) return 1;
            return 0;
        } catch (SQLException ex) {
            System.out.println("JUtil.SqlExec:" + ex);
        } catch (NullPointerException ex) {
            System.out.println("JUtil.SqlExec:" + ex);
        }
        return -1;
    }

    /**
		 * Exec SQL.
		 * 
		 * @param jconn
		 * @param sql
		 * 
		 * @return Object of 1st row 1st column
		 */
    public static Object SqlFetchSingleField(Connection jconn, String sql) {
        Object[][] oo = SqlFetch(jconn, sql);
        if (oo != null && oo.length > 0) if (oo[0] != null && oo[0].length > 0) return oo[0][0];
        return null;
    }

    /**
		 * Exec SQL.
		 * 
		 * @param sql
		 * @param jlogin
		 * 
		 * @return Object of 1st row 1st column
		 */
    public static Object SqlFetchSingleField(JDbLogin jlogin, String sql) {
        Object[][] oo = SqlFetch(jlogin, sql);
        if (oo != null && oo.length > 0) if (oo[0] != null && oo[0].length > 0) return oo[0][0];
        return null;
    }

    /**
	 * Exec SQL.
	 * 
	 * @param jconn
	 * @param sql
	 * 
	 * @return 2D Object array
	 */
    public static Object[][] SqlFetch(Connection jconn, String sql) {
        JDataBufferVector sqldata = new JDataBufferVector(jconn);
        return SqlFetch(sqldata, sql);
    }

    /**
	 * Exec SQL and fetch results into a 2D array.
	 * 
	 * @param jconn
	 * @param sql
	 * 
	 * @return 2D Object array
	 */
    public static Object[][] SqlFetch(JDbLogin jlogin, String sql) {
        JDataArrayVector sqldata = new JDataArrayVector(jlogin);
        return SqlFetch(sqldata, sql);
    }

    /**
	 * Sql fetch.
	 * 
	 * @param sqldata
	 * @param sql
	 * 
	 * @return Sql fetch} as Object[][]
	 */
    public static Object[][] SqlFetch(JDataArrayVector sqldata, String sql) {
        sqldata.fetch(sql);
        Object[][] results = new Object[sqldata.size()][];
        for (int i = 0; i < sqldata.size(); i++) {
            Object[] row = (Object[]) sqldata.get(i);
            if (row.length < 1) continue;
            results[i] = row;
            if (Debug) System.out.println(results[i].length + ":" + results[i][0]);
        }
        if (Debug) {
            Vector jstat = sqldata.getResultStatus();
            System.out.println("Num Recs:" + sqldata.size());
            System.out.println("ResultsStatus:");
            System.out.println(jstat);
        }
        return results;
    }

    /**
	 * Sql fetch.
	 * 
	 * @param sqldata
	 * @param sql
	 * 
	 * @return Sql fetch} as Object[][]
	 */
    public static Object[][] SqlFetch(JDataBufferVector sqldata, String sql) {
        sqldata.fetch(sql);
        Object[][] results = new Object[sqldata.size()][];
        for (int i = 0; i < sqldata.size(); i++) {
            JRowBufferVector rowvec = (JRowBufferVector) sqldata.get(i);
            if (rowvec.size() < 1) continue;
            results[i] = rowvec.toArray();
            if (Debug) System.out.println(results[i].length + ":" + results[i][0]);
        }
        if (Debug) {
            Vector jstat = sqldata.getResultStatus();
            System.out.println("Num Recs:" + sqldata.size());
            System.out.println("ResultsStatus:");
            System.out.println(jstat);
        }
        return results;
    }

    /**
	 * Gets the Columns.
	 * 
	 * @param jlogin
	 *            the jlogin as JDbLogin
	 * 
	 * @return the Columns as JDataColumns
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 */
    public static JDataColumns getColumns(JDbLogin jlogin) throws SQLException {
        return getColumns(jlogin.getStatement());
    }

    /**
	 * Gets the Columns.
	 * 
	 * @param s
	 * 
	 * @return the Columns as JDataColumns
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 */
    public static JDataColumns getColumns(Statement s) throws SQLException {
        return getColumns(getResultSetMetaData(s));
    }

    /**
	 * Gets the Columns.
	 * 
	 * @param rmd
	 * 
	 * @return the Columns as JDataColumns
	 * 
	 * @throws SQLException
	 */
    public static JDataColumns getColumns(ResultSetMetaData rmd) throws SQLException {
        return new JDataColumns(rmd);
    }

    /**
	 * Gets the ResultSetMetaData.
	 * 
	 * @param s
	 * 
	 * @return the ResultSetMetaData as ResultSetMetaData
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 */
    public static ResultSetMetaData getResultSetMetaData(Statement s) throws SQLException {
        return s.getResultSet().getMetaData();
    }

    public static boolean Debug = false;
}
