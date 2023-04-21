package org.dbunit.dataset;

/**
 * Thrown to indicate that a database column has been accessed that does not
 * exist. 
 * 
 * @author Manuel Laflamme
 * @version $Revision: 718 $
 * @since Feb 17, 2002
 */
public class NoSuchColumnException extends DataSetException {

    /**
     * @deprecated since 2.3.0. Prefer constructor taking a table/columnName as argument
     */
    public NoSuchColumnException() {
    }

    /**
     * @deprecated since 2.3.0. Prefer constructor taking a table/columnName as argument
     */
    public NoSuchColumnException(String msg) {
        super(msg);
    }

    /**
     * Creates an exception using the given table name + column name
     * @param tableName table in which the column was not found. Can be null
     * @param columnName the column that was not found
     * @since 2.3.0
     */
    public NoSuchColumnException(String tableName, String columnName) {
        this(tableName, columnName, null);
    }

    /**
     * Creates an exception using the given table name + column name
     * @param tableName table in which the column was not found. Can be null
     * @param columnName the column that was not found
     * @param msg Additional message to append to the exception text
     * @since 2.3.0
     */
    public NoSuchColumnException(String tableName, String columnName, String msg) {
        super(buildText(tableName, columnName, msg));
    }

    /**
	 * @param msg
	 * @param e
     * @deprecated since 2.3.0. Prefer constructor taking a table/columnName as argument
	 */
    public NoSuchColumnException(String msg, Throwable e) {
        super(msg, e);
    }

    /**
     * @param e
     * @deprecated since 2.3.0. Prefer constructor taking a table/columnName as argument
     */
    public NoSuchColumnException(Throwable e) {
        super(e);
    }

    private static String buildText(String tableName, String columnName, String message) {
        StringBuffer sb = new StringBuffer();
        if (tableName != null) {
            sb.append(tableName).append(".");
        }
        sb.append(columnName);
        if (message != null) {
            sb.append(" - ").append(message);
        }
        return sb.toString();
    }
}
