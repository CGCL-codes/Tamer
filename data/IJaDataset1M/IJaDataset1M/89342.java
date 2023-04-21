package com.objectwave.persist.broker;

import com.objectwave.persist.QueryException;
import com.objectwave.persist.RDBPersistence;
import com.objectwave.persist.sqlConstruction.*;
import java.sql.SQLException;

/**
 *  Use a simple strategy for maintaining a primary key. This approach is a
 *  built in race condition.
 *
 * @author  dhoag
 * @version  $Id: SelectAndUpdate.java,v 2.2 2005/02/13 15:38:33 dave_hoag Exp $
 */
public class SelectAndUpdate implements PrimaryKeyStrategy {

    String tableName = "SEQUENCE";

    String columnName = "NEXTVAL";

    /**
	 *  Constructor for the SelectAndUpdate object
	 */
    public SelectAndUpdate() {
    }

    /**
	 * @param  aValue The new TableName value
	 */
    public void setTableName(String aValue) {
        tableName = aValue;
    }

    /**
	 * @param  aValue The new ColumnName value
	 */
    public void setColumnName(String aValue) {
        columnName = aValue;
    }

    /**
	 * @return  The TableName value
	 */
    public String getTableName() {
        return tableName;
    }

    /**
	 * @return  The ColumnName value
	 */
    public String getColumnName() {
        return columnName;
    }

    /**
	 *  Select the value from the DB. Increment it by 1. Update the value in the
	 *  db.
	 *
	 * @param  broker
	 * @param  pObj
	 * @return
	 * @exception  SQLException
	 * @exception  QueryException
	 */
    public synchronized Object nextPrimaryKey(final RDBBroker broker, final RDBPersistence pObj) throws SQLException, QueryException {
        SQLSelect sql = new SQLSelect(getTableName());
        sql.addColumnList(getColumnName());
        int resultA = broker.getConnection().nextPrimaryKey(sql);
        SQLUpdate sqlUpdate = new SQLUpdate(getTableName());
        sqlUpdate.addColumnValue(getColumnName(), new Integer(resultA + 1));
        broker.getConnection().updateExecSql(sqlUpdate);
        return new Integer(resultA);
    }
}
