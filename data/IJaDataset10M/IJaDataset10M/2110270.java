package com.pehrs.mailpost.wmlblog.sql;

import java.util.*;
import java.text.*;
import java.sql.*;

/**
 * This is the superclass for the value objects 
 *
 * @author <a href="mailto:matti.pehrs@home.se">Matti Pehrs</a>
 * @version $Id: JdbcValueObject.java,v 1.1.1.1 2004/10/19 22:46:12 mattipehrs Exp $
 */
public abstract class JdbcValueObject {

    public JdbcValueObject() {
    }

    protected static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public static SimpleDateFormat getDateFormat() {
        return df;
    }

    /**
     * Load values from persistent storage
     * @return true if successfull
     */
    public boolean load(Connection dbConnection) throws SQLException {
        return dbSelect(dbConnection);
    }

    /**
     * Save values to persistent storage
     */
    public void save(Connection dbConnection) throws SQLException {
        if (dbExists(dbConnection)) {
            dbUpdate(dbConnection);
        } else {
            dbInsert(dbConnection);
        }
    }

    public static boolean hasColumn(ResultSetMetaData meta, String colName) throws SQLException {
        for (int col = 1; col <= meta.getColumnCount(); col++) {
            if (meta.getColumnName(col).equals(colName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Delete Object from persistent storage
     */
    public void delete(Connection dbConnection) throws SQLException {
        dbDelete(dbConnection);
    }

    /** Do the SQL Select statment to load data from the dB 
     * @return true if successfull
     */
    protected abstract boolean dbSelect(Connection dbConnection) throws SQLException;

    /** Do the SQL Update statment to update data in the dB */
    protected abstract void dbUpdate(Connection dbConnection) throws SQLException;

    /** Do the SQL Insert statment to create data in the dB */
    protected abstract void dbInsert(Connection dbConnection) throws SQLException;

    /** Check and se if the row exists in the dB  */
    protected abstract boolean dbExists(Connection dbConnection) throws SQLException;

    /** Remvoe the row from the dB  */
    protected abstract void dbDelete(Connection dbConnection) throws SQLException;
}
