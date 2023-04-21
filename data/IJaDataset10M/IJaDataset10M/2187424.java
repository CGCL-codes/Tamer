package com.objectwave.persist.sqlConstruction;

import com.objectwave.persist.*;

/**
* A class that handles the creation of SQL Insert statements.
* For example:
* <pre>
*    SQLInsert sql = new SQLInsert();
*    sql.setTableName("TABLENAME");
* </pre>
*
* @see            com.objectwave.persist.sqlConstruction.SQLAssembler
* @see            com.objectwave.persist.broker.RDBBroker
* @version        $Id: SQLInsert.java,v 2.1 2005/02/13 19:25:34 dave_hoag Exp $
* @author         Dave Hoag
*/
public class SQLInsert extends SQLModifier {

    /**
	 * Abstract the issues with an SQL insert statement.
	 * If this constructor is used, the table name must be set with
	 * the setTableName method.
	 * @see SQLObject#setTableName(String )
	 */
    public SQLInsert() {
    }

    /**
	 * @param tableName String The table name for which this statement will insert values.
	 */
    public SQLInsert(String tableName) {
        setTableName(tableName);
    }

    /**
	 * No value add other than specifying it is an INSERT statement.
	 *
	 * @param stmt java.sql.PreparedStatement
	 */
    public void bindValues(final java.sql.PreparedStatement stmt, final Class persistenceClass, boolean verbose) throws java.sql.SQLException, QueryException {
        if (verbose) {
            BrokerFactory.println("Binding PerparedStatement\nINSERT INTO " + table);
        }
        super.bindValues(stmt, persistenceClass, verbose);
    }

    /**
	 * The column portion of an insert statement.
	 * @param buf StringBuffer being built that will contain the full sql statement.
	 * @see #getSqlStatement() The user of this method.
	 */
    protected void formatColumnList(StringBuffer buf) {
        buf.append('(');
        for (int i = 0; i < valueCount; i++) {
            buf.append(columnList[i]);
            buf.append(" ,");
        }
        buf.setCharAt(buf.length() - 1, ')');
    }

    /**
	 * Simply add "INTO tableName" to the provided StringBuffer.
	 * @param buf StringBuffer being built that will contain the full sql statement.
	 * @see #getSqlStatement() The user of this method.
	 */
    protected void formatTable(StringBuffer buf) {
        buf.append("INTO " + table);
    }

    /**
	 * The values portion of an insert statement.
	 * @param buf StringBuffer being built that will contain the full sql statement.
	 * @see #getSqlStatement() The user of this method.
	 */
    protected void formatValueList(StringBuffer buf) {
        buf.append(" VALUES (");
        for (int i = 0; i < valueCount; i++) {
            formatValue(valueList[i], buf);
            buf.append(" ,");
        }
        buf.setCharAt(buf.length() - 1, ')');
    }

    /**
	* As with the getSqlStatement() method, this method trusts that there's an ordered, 1-1
	* mapping between the columns list and the values list.
	* @return java.lang.String, a string which will be appropriate for a SQL prepared statement,
	* based on the columns and values contained by this SQLInsert instance.
	*/
    public String getPreparedString() {
        StringBuffer buf = new StringBuffer("INSERT ");
        formatTable(buf);
        formatColumnList(buf);
        buf.append(" VALUES (");
        for (int i = 0; i < valueCount; i++) buf.append("? ,");
        buf.setCharAt(buf.length() - 1, ')');
        return buf.toString();
    }

    /**
	 * Assemble all of the information that has been gathered into a valid sql statement.
	 * ex.
	 *  "insert INTO myTable (columnName1, columnName2) values (value1, 'value2')"
	 *
	 * @return StringBuffer That is the full sql statement.
	 */
    public StringBuffer getSqlStatement() {
        StringBuffer buf = new StringBuffer("INSERT ");
        formatTable(buf);
        formatColumnList(buf);
        formatValueList(buf);
        return buf;
    }
}
