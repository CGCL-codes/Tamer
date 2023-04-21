package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.DevVarDoubleStringArray;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.ConnectionFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtilsFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.DateUtil;

public class GenericExtractorMethods {

    private final int archType;

    public GenericExtractorMethods(final int type) {
        this.archType = type;
    }

    public void makeDataException(final int format, final String type1, final String type2) throws ArchivingException {
        String message = "", reason = "", desc = "";
        message = "Failed retrieving data ! ";
        reason = "The attribute should be " + type1;
        desc = "The attribute format is  not " + type1 + " : " + format + " (" + type2 + ") !!";
        throw new ArchivingException(message, reason, null, desc, this.getClass().getName());
    }

    public int getDataCountFromQuery(final String query, final IDBConnection dbConn) throws ArchivingException {
        int valuesCount = 0;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;
        try {
            conn = dbConn.getConnection();
            stmt = conn.createStatement();
            rset = stmt.executeQuery(query);
            while (rset.next()) {
                valuesCount = rset.getInt(1);
            }
        } catch (final SQLException e) {
            String message = "";
            if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE) || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1) {
                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
            } else {
                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;
            }
            final String reason = GlobalConst.QUERY_FAILURE;
            final String desc = "Failed while executing GenericExtractorMethods.getAttDataCount() method...";
            throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this.getClass().getName(), e);
        } finally {
            ConnectionCommands.close(rset);
            ConnectionCommands.close(stmt);
            dbConn.closeConnection(conn);
        }
        return valuesCount;
    }

    public DevVarDoubleStringArray getAttScalarDataCondition(final String attributeName, final String query, final boolean roFields, final IDBConnection dbConn) throws ArchivingException {
        DevVarDoubleStringArray dvdsa;
        final Vector<String> timeVect = new Vector<String>();
        final Vector<Double> valueRVect = new Vector<Double>();
        final Vector<Double> valueWVect = new Vector<Double>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;
        try {
            conn = dbConn.getConnection();
            stmt = conn.createStatement();
            rset = stmt.executeQuery(query);
            if (roFields) {
                while (rset.next()) {
                    timeVect.addElement(DateUtil.stringToDisplayString(rset.getString(1)));
                    final double result = rset.getDouble(2);
                    if (rset.wasNull()) {
                        valueRVect.addElement(new Double(GlobalConst.NAN_FOR_NULL));
                    } else {
                        valueRVect.addElement(new Double(result));
                    }
                }
            } else {
                while (rset.next()) {
                    timeVect.addElement(DateUtil.stringToDisplayString(rset.getString(1)));
                    final double result1 = rset.getDouble(2);
                    if (rset.wasNull()) {
                        valueRVect.addElement(new Double(GlobalConst.NAN_FOR_NULL));
                    } else {
                        valueRVect.addElement(new Double(result1));
                    }
                    final double result2 = rset.getDouble(3);
                    if (rset.wasNull()) {
                        valueWVect.addElement(new Double(GlobalConst.NAN_FOR_NULL));
                    } else {
                        valueWVect.addElement(new Double(result2));
                    }
                }
            }
        } catch (final SQLException e) {
            throw new ArchivingException(e);
        } finally {
            ConnectionCommands.close(rset);
            ConnectionCommands.close(stmt);
            dbConn.closeConnection(conn);
        }
        final String[] timeArr = DbUtils.toStringArray(timeVect);
        final double[] valueRWArr = DbUtils.toDoubleArray(valueRVect, valueWVect);
        dvdsa = new DevVarDoubleStringArray(valueRWArr, timeArr);
        return dvdsa;
    }

    /**
     * @param attributeName
     * @param writable
     * @param cmd
     * @return
     * @throws ArchivingException
     */
    public double getAttScalarDataMinMaxAvg(final String attributeName, final int writable, final String cmd) throws ArchivingException {
        final IDBConnection dbConn = ConnectionFactory.getInstance(archType);
        double min = 0;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;
        String selectField = "";
        final String tableName = dbConn.getSchema() + "." + DbUtilsFactory.getInstance(archType).getTableName(attributeName.trim());
        if (writable == AttrWriteType._READ || writable == AttrWriteType._WRITE) {
            selectField = ConfigConst.TAB_SCALAR_RO[1];
        } else {
            selectField = ConfigConst.TAB_SCALAR_RW[1];
        }
        final String getAttributeDataQuery = "SELECT " + cmd + "(" + selectField + ")" + " FROM " + tableName;
        try {
            conn = dbConn.getConnection();
            stmt = conn.createStatement();
            rset = stmt.executeQuery(getAttributeDataQuery);
            while (rset.next()) {
                min = rset.getDouble(1);
                if (rset.wasNull()) {
                    min = Double.NaN;
                }
            }
        } catch (final SQLException e) {
            String message = "";
            if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE) || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1) {
                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
            } else {
                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;
            }
            final String reason = GlobalConst.QUERY_FAILURE;
            final String desc = "Failed while executing GenericExtractorMethods.getAttDataMin() method...";
            throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this.getClass().getName(), e);
        } finally {
            ConnectionCommands.close(rset);
            ConnectionCommands.close(stmt);
            dbConn.closeConnection(conn);
        }
        return min;
    }

    public double getAttScalarDataMinMaxAvgBetweenDates(final String attributeName, final String time0, final String time1, final int writable, final String cmd) throws ArchivingException {
        final IDBConnection dbConn = ConnectionFactory.getInstance(archType);
        double max = 0;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;
        String selectField0 = "";
        String selectField1 = "";
        String dateClause = "";
        final String tableName = dbConn.getSchema() + "." + DbUtilsFactory.getInstance(archType).getTableName(attributeName);
        if (writable == AttrWriteType._READ || writable == AttrWriteType._WRITE) {
            selectField0 = ConfigConst.TAB_SCALAR_RO[0];
            selectField1 = ConfigConst.TAB_SCALAR_RO[1];
        } else {
            selectField0 = ConfigConst.TAB_SCALAR_RW[0];
            selectField1 = ConfigConst.TAB_SCALAR_RW[1];
        }
        dateClause = selectField0 + " BETWEEN " + DbUtilsFactory.getInstance(archType).toDbTimeString(time0.trim()) + " AND " + DbUtilsFactory.getInstance(archType).toDbTimeString(time1.trim());
        final String getAttributeDataQuery = "SELECT " + cmd + "(" + selectField1 + ")" + " FROM " + tableName + " WHERE " + "(" + dateClause + ")" + " ORDER BY time";
        try {
            conn = dbConn.getConnection();
            stmt = conn.createStatement();
            rset = stmt.executeQuery(getAttributeDataQuery);
            while (rset.next()) {
                max = rset.getDouble(1);
                if (rset.wasNull()) {
                    max = Double.NaN;
                }
            }
        } catch (final SQLException e) {
            String message = "";
            if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE) || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1) {
                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
            } else {
                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;
            }
            final String reason = GlobalConst.QUERY_FAILURE;
            final String desc = "Failed while executing GenericExtractorMethods.getAttDataMaxBetweenDates() method...";
            throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this.getClass().getName(), e);
        } finally {
            ConnectionCommands.close(rset);
            ConnectionCommands.close(stmt);
            dbConn.closeConnection(conn);
        }
        return max;
    }

    public void buildAttributeScalarTab(final String attributeID, final int dataType, final String[] tab) throws ArchivingException {
        final IDBConnection dbConn = ConnectionFactory.getInstance(archType);
        if (dbConn == null) {
            return;
        }
        Connection conn = null;
        Statement stmt = null;
        String type = "double";
        if (dataType == TangoConst.Tango_DEV_STRING || dataType == TangoConst.Tango_DEV_BOOLEAN) {
            type = "varchar(255)";
        }
        String query = "CREATE TABLE `" + attributeID + "` (" + "`" + tab[0] + "` " + "datetime NOT NULL default '0000-00-00 00:00:00', " + "`" + tab[1] + "` " + type + " default NULL ";
        if (tab.length == 3) {
            query += ",`" + tab[2] + "` " + type + " default NULL)";
        } else {
            query += ")";
        }
        try {
            conn = dbConn.getConnection();
            stmt = conn.createStatement();
            System.out.println("=============== REQUETE DE CREATION DE TABLE IS " + query.toString());
            stmt.executeUpdate(query.toString().trim());
        } catch (final SQLException e) {
            throw new ArchivingException(e);
        } finally {
            ConnectionCommands.close(stmt);
            dbConn.closeConnection(conn);
        }
    }

    public DevVarDoubleStringArray getAttScalarConditionBetweenDates(final String query, final boolean roFields, final IDBConnection dbConn) throws ArchivingException {
        DevVarDoubleStringArray dvdsa;
        final Vector<String> timeVect = new Vector<String>();
        final Vector<Double> valueRVect = new Vector<Double>();
        final Vector<Double> valueWVect = new Vector<Double>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;
        try {
            conn = dbConn.getConnection();
            stmt = conn.createStatement();
            rset = stmt.executeQuery(query);
            if (roFields) {
                while (rset.next()) {
                    timeVect.addElement(DateUtil.stringToDisplayString(rset.getString(1)));
                    final double result = rset.getDouble(2);
                    if (rset.wasNull()) {
                        valueRVect.addElement(new Double(GlobalConst.NAN_FOR_NULL));
                    } else {
                        valueRVect.addElement(new Double(result));
                    }
                }
            } else {
                while (rset.next()) {
                    timeVect.addElement(DateUtil.stringToDisplayString(rset.getString(1)));
                    final double result1 = rset.getDouble(2);
                    if (rset.wasNull()) {
                        valueRVect.addElement(new Double(GlobalConst.NAN_FOR_NULL));
                    } else {
                        valueRVect.addElement(new Double(result1));
                    }
                    final double result2 = rset.getDouble(3);
                    if (rset.wasNull()) {
                        valueWVect.addElement(new Double(GlobalConst.NAN_FOR_NULL));
                    } else {
                        valueWVect.addElement(new Double(result2));
                    }
                }
            }
        } catch (final SQLException e) {
            throw new ArchivingException(e);
        } finally {
            ConnectionCommands.close(rset);
            ConnectionCommands.close(stmt);
            dbConn.closeConnection(conn);
        }
        final String[] timeArr = DbUtils.toStringArray(timeVect);
        final double[] valueRWArr = DbUtils.toDoubleArray(valueRVect, valueWVect);
        dvdsa = new DevVarDoubleStringArray(valueRWArr, timeArr);
        return dvdsa;
    }

    public void buildAttributeSpectrumTab(final String attributeID, final String[] tab, final int dataType) throws ArchivingException {
        final IDBConnection dbConn = ConnectionFactory.getInstance(archType);
        if (dbConn == null) {
            return;
        }
        Connection conn = null;
        Statement stmt = null;
        String query = "CREATE TABLE `" + attributeID + "` (" + "`" + tab[0] + "` " + "DATETIME NOT NULL default '0000-00-00 00:00:00', " + "`" + tab[1] + "` " + "SMALLINT NOT NULL, " + "`" + tab[2] + "` " + "BLOB default NULL";
        if (tab.length == 4) {
            query += ",`" + tab[3] + "` " + "BLOB default NULL";
        }
        query += ")";
        try {
            conn = dbConn.getConnection();
            stmt = conn.createStatement();
            stmt.executeUpdate(query.toString().trim());
        } catch (final SQLException e) {
            throw new ArchivingException(e);
        } finally {
            ConnectionCommands.close(stmt);
            dbConn.closeConnection(conn);
        }
    }

    /**
     * <b>Description : </b> Creates the table of an image read/write type
     * attribute <I>(mySQL only)</I>.
     * 
     * @param attributeID
     *            The ID of the associated attribute
     */
    public void buildAttributeImageTab(final String attributeID, final String[] tab, final int dataType) throws ArchivingException {
        final IDBConnection dbConn = ConnectionFactory.getInstance(archType);
        if (dbConn == null) {
            return;
        }
        Connection conn = null;
        Statement stmt = null;
        String query = "CREATE TABLE `" + attributeID + "` (" + "`" + tab[0] + "` " + "DATETIME NOT NULL default '0000-00-00 00:00:00', " + "`" + tab[1] + "` " + "SMALLINT NOT NULL, " + "`" + tab[2] + "` " + "SMALLINT NOT NULL, " + "`" + tab[3] + "` " + "LONGBLOB default NULL, ";
        if (tab.length == 5) {
            query += "`" + tab[4] + "` " + "LONGBLOB default NULL)";
        }
        try {
            conn = dbConn.getConnection();
            stmt = conn.createStatement();
            stmt.executeUpdate(query.toString().trim());
        } catch (final SQLException e) {
            throw new ArchivingException(e);
        } finally {
            ConnectionCommands.close(stmt);
            dbConn.closeConnection(conn);
        }
    }
}
