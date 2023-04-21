package uk.gov.dti.og.fox.dbinterface;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import oracle.jdbc.OraclePreparedStatement;
import oracle.sql.*;
import uk.gov.dti.og.fox.dbinterface.CursorParameter;
import uk.gov.dti.og.fox.dbinterface.RawCursor;
import uk.gov.dti.og.fox.dom.DOM;
import uk.gov.dti.og.fox.ex.*;
import uk.gov.dti.og.fox.ex.ExInternal;
import uk.gov.dti.og.fox.xhtml.NodeInfo;

/**
 *  Specialised fox cursors dealing in one table only
 *
 *@author     Clayton
 *@created    February 9, 2003
 */
public class TableCursor extends RawCursor {

    private ArrayList mTableColumns;

    private ArrayList mColumnDatatypes;

    /**
    *  Prepare and Execute (using params) the statement
    *
    *@param  pDBCon          db connection
    *@param  pTableName      database table name
    *@param  pInputParams    bind variables
    *@exception  ExInternal  Description of the Exception
    *@throws  ExCursorRoot   Any underlying sql errors will appear as embedded
    *      exceptions
    */
    public void execute(Connection pDBCon, String pTableName, CursorParameter[] pInputParams) throws ExCursorRoot, ExInternal {
        if (pInputParams == null) {
            checkMetadata(pDBCon, pTableName);
        } else {
            executeDml(pDBCon, pTableName, pInputParams);
        }
    }

    /**
    *  Checks the table definition exists on the database and loads up all the
    *  table column sfor later query
    *
    *@param  pDBCon         db connection
    *@param  pTableName     database table name
    *@throws  ExCursorRoot  Any underlying sql errors will appear as embedded
    *      exceptions
    */
    void checkMetadata(Connection pDBCon, String pTableName) throws ExCursorRoot {
        DatabaseMetaData mDbMeta = null;
        try {
            mDbMeta = pDBCon.getMetaData();
            mTableColumns = new ArrayList();
            mColumnDatatypes = new ArrayList();
            ResultSet rset = mDbMeta.getColumns("", null, pTableName, null);
            while (rset.next()) {
                mTableColumns.add(rset.getString(4));
                mColumnDatatypes.add(rset.getString(6));
            }
            if (mTableColumns.size() == 0) {
                throw new ExCursorRoot("Table not found " + pTableName);
            }
        } catch (SQLException e) {
            throw new ExCursorRoot("Error parsing table " + pTableName, e);
        }
    }

    /**
    *  Prepare and Execute (using params) the insert/update/delete statement
    *
    *@param  pDBCon          db connection
    *@param  pInputParams    bind variables
    *@param  pDMLStatement   Description of the Parameter
    *@exception  ExInternal  Description of the Exception
    *@throws  ExCursorRoot   Any underlying sql errors will appear as embedded
    *      exceptions
    */
    void executeDml(Connection pDBCon, String pDMLStatement, CursorParameter[] pInputParams) throws ExCursorRoot, ExInternal {
        try {
            mPreparedStatement = pDBCon.prepareStatement(pDMLStatement);
            mCursorParameters = pInputParams;
            for (int i = 0; pInputParams != null && i < pInputParams.length; i++) {
                this.bindInputParameter(pInputParams[i], i, mPreparedStatement);
            }
            ((OraclePreparedStatement) mPreparedStatement).setExecuteBatch(1);
            mRowCount += mPreparedStatement.executeUpdate();
            mRowCount += ((OraclePreparedStatement) mPreparedStatement).sendBatch();
            mPreparedStatement.close();
        } catch (SQLException ex) {
            throw new ExCursorRoot("Error creating cursor for:\n" + pDMLStatement + "\n\n" + CursorParameter.arrayToText(pInputParams), ex);
        }
    }

    /**
    *  Returns the column names as an ArrayList
    *
    *@return                an array for column names for the cursor
    *@throws  ExCursorRoot  Any underlying sql errors will appear as embedded
    *      exceptions
    */
    public ArrayList getColumnNames() throws ExCursorRoot {
        if (mTableColumns == null || mTableColumns.size() == 0) {
            throw new ExCursorRoot("No columns names available for table ");
        }
        return mTableColumns;
    }

    /**
    *  Returns the column datatypes as an ArrayList
    *
    *@return                an array for column datatypes for the cursor
    *@throws  ExCursorRoot  Any underlying sql errors will appear as embedded
    *      exceptions
    */
    public ArrayList getColumnDatatypes() throws ExCursorRoot {
        if (mTableColumns == null || mTableColumns.size() == 0) {
            throw new ExCursorRoot("No columns names available for table ");
        }
        return mColumnDatatypes;
    }

    /**
    *  Closes the fox cursor Note as this is just metadata there is no physical
    *  cursor to close so just clean up data structures
    */
    public void close() {
        mTableColumns = null;
    }
}
