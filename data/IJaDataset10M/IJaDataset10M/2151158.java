package gov.sns.apps.jeri.apps.ppssignalfunctions;

import gov.sns.apps.jeri.*;
import gov.sns.apps.jeri.application.JeriDataModule;
import gov.sns.apps.jeri.data.*;
import gov.sns.apps.jeri.apps.signaltablebrowser.SignalBrowserPVTableModel;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;

/**
 * Provides the functionality for the <CODE>PPSsignalFunctionsFrame</CODE> class.
 * 
 * @author Chris Fowlkes
 */
public class PPSsignalFunctions extends JeriDataModule {

    /**
   * Creates a new instance of <CODE>PPSsignalFunctions</CODE>.
   */
    public PPSsignalFunctions() {
    }

    /**
   * Commits any pending changes to the database.
   * 
   * @throws java.sql.SQLException Thrown on sql error.
   */
    public void commit() throws java.sql.SQLException {
        getConnection().commit();
    }

    /**
   * Commits any pending changes to the database.
   * 
   * @throws java.sql.SQLException Thrown on sql error.
   */
    public void rollback() throws java.sql.SQLException {
        getConnection().rollback();
    }

    /**
   * Runs a sql statement that returns a single <CODE>int</CODE> value, such as
   * a count query.
   * 
   * @param sql The sql atatement to run.
   * @return The <CODE>int</CODE> returned by the statement.
   * @throws java.sql.SQLException Thrown on sql error.
   */
    private int findRecordCount(String sql) throws java.sql.SQLException {
        int count;
        Statement query = getConnection().createStatement();
        try {
            ResultSet result = query.executeQuery(sql);
            try {
                result.next();
                count = result.getInt(1);
            } finally {
                result.close();
            }
        } finally {
            query.close();
        }
        return count;
    }

    /**
   * Loads the data for the archive panel.
   * 
   * @return The instances of <CODE>IOC</CODE> loaded.
   * @throws java.sql.SQLException Thrown on sql error.
   */
    public IOC[] loadDBTreeData() throws java.sql.SQLException {
        try {
            setMessage("Loading IOC Data...");
            setProgressIndeterminate(true);
            ArrayList iocs;
            Statement query = getConnection().createStatement();
            try {
                StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM ");
                StringBuffer whereClause = new StringBuffer(Main.SCHEMA);
                whereClause.append(".IOC_DVC, ");
                whereClause.append(Main.SCHEMA);
                whereClause.append(".IOC_DB_FILE_ASGN WHERE IOC_DVC.DVC_ID = IOC_DB_FILE_ASGN.DVC_ID (+)");
                sql.append(whereClause);
                int recordCount = findRecordCount(sql.toString());
                sql = new StringBuffer("SELECT IOC_DVC.DVC_ID, IOC_DB_FILE_ASGN.EXT_SRC_FILE_NM, IOC_DB_FILE_ASGN.EXT_SRC_DIR_NM FROM ");
                sql.append(whereClause);
                sql.append(" ORDER BY DVC_ID, EXT_SRC_FILE_NM");
                ResultSet result = query.executeQuery(sql.toString());
                try {
                    setProgressMaximum(recordCount);
                    iocs = new ArrayList(recordCount);
                    IOC currentIOC = null;
                    String lastID = null;
                    setProgressValue(0);
                    int progress = 0;
                    setProgressIndeterminate(false);
                    while (result.next()) {
                        String newID = result.getString("DVC_ID");
                        if (lastID == null || !lastID.equals(newID)) {
                            currentIOC = new IOC(newID);
                            lastID = newID;
                            if (currentIOC.toString().equals("PPS:IOC_Phase1")) iocs.add(currentIOC);
                        }
                        String newFileName = result.getString("EXT_SRC_FILE_NM");
                        if (newFileName != null) {
                            String newDirectoryName = result.getString("EXT_SRC_DIR_NM");
                            DBFile newDBFile = new DBFile(newFileName, newDirectoryName);
                            currentIOC.addDBFile(newDBFile);
                        }
                        setProgressValue(++progress);
                    }
                } finally {
                    result.close();
                }
            } finally {
                query.close();
            }
            return (IOC[]) iocs.toArray(new IOC[iocs.size()]);
        } finally {
            clearProgress();
        }
    }

    /**
   * Loads the signal for the DB table.
   * 
   * @param dbFileName The name of the DB file for which to return the instances of <CODE>Signal</CODE>
   * @param model The model used to store the signal data loaded from the RDB.
   * @param fieldIDs The field IDs to load into the instances of <CODE>Signal</CODE>.
   * @throws java.sql.SQLException Thrown on SQL error.
   */
    public void loadDBSignals(String dbFileName, SignalBrowserPVTableModel model, String fieldIDs[]) throws java.sql.SQLException {
        try {
            setProgressIndeterminate(true);
            setMessage("Loading Signal Data...");
            Connection oracleConnection = getConnection();
            StringBuffer sql = new StringBuffer("SELECT COUNT(*)");
            StringBuffer whereClause = new StringBuffer(" FROM ");
            whereClause.append(Main.SCHEMA);
            whereClause.append(".sgnl_rec, ");
            whereClause.append(Main.SCHEMA);
            whereClause.append(".IOC_DB_FILE_ASGN_SGNL");
            if (fieldIDs.length > 0) {
                whereClause.append(", ");
                whereClause.append(Main.SCHEMA);
                whereClause.append(".sgnl_fld");
            }
            whereClause.append(" WHERE IOC_DB_FILE_ASGN_SGNL.EXT_SRC_FILE_NM = ? AND SGNL_REC.SGNL_ID = IOC_DB_FILE_ASGN_SGNL.SGNL_ID");
            if (fieldIDs.length > 0) {
                whereClause.append(" AND sgnl_rec.sgnl_id = sgnl_fld.sgnl_id (+) AND sgnl_rec.rec_type_id = sgnl_fld.rec_type_id (+)");
                for (int i = 0; i < fieldIDs.length; i++) {
                    if (i > 0) whereClause.append(" OR "); else whereClause.append(" AND (");
                    whereClause.append("sgnl_fld.fld_id = ?");
                }
                whereClause.append(" OR sgnl_fld.fld_id IS NULL)");
            }
            sql.append(whereClause);
            int recordCount;
            PreparedStatement countQuery = oracleConnection.prepareStatement(sql.toString());
            try {
                countQuery.setString(1, dbFileName);
                for (int i = 0; i < fieldIDs.length; i++) countQuery.setString(i + 2, fieldIDs[i]);
                ResultSet countResult = countQuery.executeQuery();
                try {
                    countResult.next();
                    recordCount = countResult.getInt(1);
                } finally {
                    countResult.close();
                }
            } finally {
                countQuery.close();
            }
            sql = new StringBuffer("SELECT sgnl_rec.sgnl_id, sgnl_rec.rec_type_id");
            if (fieldIDs.length > 0) sql.append(", sgnl_fld.fld_id, sgnl_fld.fld_val");
            sql.append(whereClause);
            sql.append(" ORDER BY sgnl_id");
            PreparedStatement signalQuery = oracleConnection.prepareStatement(sql.toString());
            try {
                signalQuery.setString(1, dbFileName);
                for (int i = 0; i < fieldIDs.length; i++) signalQuery.setString(i + 2, fieldIDs[i]);
                if (recordCount > 0) setProgressMaximum(recordCount); else setProgressMaximum(0);
                ResultSet signalRecords = signalQuery.executeQuery();
                try {
                    String oldSignalID = null;
                    Signal signal = null;
                    int progress = 0;
                    setProgressValue(0);
                    setProgressIndeterminate(false);
                    String recordTypeID = null;
                    while (signalRecords.next()) {
                        String signalID = signalRecords.getString("sgnl_id");
                        if (oldSignalID == null || !oldSignalID.equals(signalID)) {
                            signal = new Signal(signalID);
                            recordTypeID = signalRecords.getString("rec_type_id");
                            SignalType newSignalType = new SignalType();
                            newSignalType.setRecordType(new EpicsRecordType(recordTypeID));
                            signal.setType(newSignalType);
                            model.addSignal(signal);
                            oldSignalID = signalID;
                        }
                        if (fieldIDs.length > 0) {
                            String fieldID = signalRecords.getString("FLD_ID");
                            if (fieldID != null) {
                                String value = signalRecords.getString("FLD_VAL");
                                SignalField field = new SignalField(value);
                                field.setType(model.findFieldType(fieldID, recordTypeID));
                                signal.addField(field);
                            }
                        }
                        setProgressValue(++progress);
                    }
                } finally {
                    signalRecords.close();
                }
            } finally {
                signalQuery.close();
            }
        } finally {
            clearProgress();
        }
    }

    /**
   * Finds the <CODE>Signal</CODE> in the array with the given ID.
   * 
   * @param signals The instances of <CODE>Signal</CODE> to search.
   * @param signalID The ID of the <CODE>Signal</CODE> to return.
   * @return The <CODE>Signal</CODE> with the given ID, or <CODE>null</CODE> if no match is found.
   */
    private Signal findSignal(Signal[] signals, String signalID) {
        for (int i = 0; i < signals.length; i++) if (signals[i].getID().equals(signalID)) return signals[i];
        return null;
    }

    /**
   * Deletes the given <CODE>IOC</CODE> from the database.
   * 
   * @param iocToDelete The <CODE>IOC</CODE> to delete from the database.
   * @throws java.sql.SQLException Thrown on SQL error.
   */
    public void deleteIOC(IOC iocToDelete) throws java.sql.SQLException {
        String iocID = iocToDelete.getID();
        Statement query = getConnection().createStatement();
        try {
            StringBuffer sql = new StringBuffer("DELETE FROM ");
            sql.append(Main.SCHEMA);
            sql.append(".IOC_DB_FILE_ASGN WHERE DVC_ID = '");
            sql.append(iocID);
            sql.append("'");
            query.execute(sql.toString());
            sql = new StringBuffer("DELETE FROM ");
            sql.append(Main.SCHEMA);
            sql.append(".IOC_DVC WHERE DVC_ID = '");
            sql.append(iocID);
            sql.append("'");
            query.execute(sql.toString());
        } finally {
            query.close();
        }
    }

    /**
   * Adds the given DB file to the given <CODE>IOC</CODE> in the database.
   * 
   * @param dbFile The DB file to add to the <CODE>IOC</CODE>.
   * @param selectedIOC The <CODE>IOC</CODE> to which to add the DB file.
   * @throws java.sql.SQLException Thrown on sql error.
   */
    public void addDBFileToIOC(File dbFile, IOC selectedIOC) throws java.sql.SQLException {
        String iocID = selectedIOC.getID();
        Statement query = getConnection().createStatement();
        try {
            StringBuffer sql = new StringBuffer("INSERT INTO ");
            sql.append(Main.SCHEMA);
            sql.append(".IOC_DB_FILE_ASGN (DVC_ID, EXT_SRC_FILE_NM, EXT_SRC_DIR_NM) VALUES ('");
            sql.append(iocID);
            sql.append("', '");
            sql.append(dbFile.getName());
            sql.append("', '");
            sql.append(dbFile.getParent());
            sql.append("')");
            query.execute(sql.toString());
        } finally {
            query.close();
        }
    }

    /**
   * Deletes the IOC to DB file association in the database. This method will 
   * not remove the signal record to DB file associations.
   * 
   * @param dbFileName The name of the DB file to delete.
   * @param iocToDelete The <CODE>IOC</CODE> from which to delete the DB file.
   * @throws java.sql.SQLException Thrown on sql error.
   */
    public void deleteDBFile(String dbFileName, IOC iocToDelete) throws java.sql.SQLException {
        String iocID = iocToDelete.getID();
        Statement query = getConnection().createStatement();
        try {
            StringBuffer sql = new StringBuffer("DELETE FROM ");
            sql.append(Main.SCHEMA);
            sql.append(".IOC_DB_FILE_ASGN WHERE DVC_ID = '");
            sql.append(iocID);
            sql.append("' AND EXT_SRC_FILE_NM = '");
            sql.append(dbFileName);
            sql.append("'");
            query.execute(sql.toString());
        } finally {
            query.close();
        }
    }

    /**
   * Associates the given instances of <CODE>Signal</CODE> to the given DB file 
   * in the database.
   * 
   * @param signals The instances of <CODE>Signal</CODE> to associate with the DB file.
   * @param dbFileName The name of the DB file with which to associate the instances of <CODE>Signal</CODE>.
   * @param iocID The ID of the <CODE>IOC</CODE> to which the DB file belongs.
   * @throws java.sql.SQLException Thrown on sql error.
   */
    public void addSignalsToDBFile(Signal[] signals, String dbFileName, String iocID) throws java.sql.SQLException {
        try {
            setMessage("Adding signals to DB file.");
            setProgressIndeterminate(true);
            StringBuffer sql = new StringBuffer("INSERT INTO ");
            sql.append(Main.SCHEMA);
            sql.append(".IOC_DB_FILE_ASGN_SGNL (EXT_SRC_FILE_NM, DVC_ID, SGNL_ID) VALUES(?, ?, ?)");
            PreparedStatement insertQuery = getConnection().prepareStatement(sql.toString());
            try {
                insertQuery.setString(1, dbFileName);
                insertQuery.setString(2, iocID);
                setProgressValue(0);
                setProgressMaximum(signals.length);
                setProgressIndeterminate(false);
                for (int i = 0; i < signals.length; i++) {
                    insertQuery.setString(3, signals[i].getID());
                    insertQuery.execute();
                    setProgressValue(i + 1);
                }
            } finally {
                insertQuery.close();
            }
        } finally {
            clearProgress();
        }
    }

    /**
   * Exposing the super classes connection property.
   * 
   * @param oracleConnection The <CODE>Connection</CODE> to the database.
   * @throws java.sql.SQLException Thrown on SQL error.
   */
    @Override
    public void setConnection(Connection oracleConnection) throws SQLException {
        super.setConnection(oracleConnection);
    }

    /**
   * Closes the database connection.
   * 
   * @throws java.sql.SQLException Thrown on SQL error.
   */
    public void closeConnection() throws java.sql.SQLException {
        getConnection().close();
    }

    /**
   * Loads the given fields for the DB table. This method does not load the
   * signal data from the database, it only loads the fields given. This is used 
   * when a field is added to the table.
   * 
   * @param dbFileName The name of the DB file for which to return the instances of <CODE>Signal</CODE>
   * @param fieldIDs The field IDs to load into the instances of <CODE>Signal</CODE>.
   * @param model The model used to store the signal data loaded from the RDB.
   * @throws java.sql.SQLException Thrown on SQL error.
   */
    public void loadDBFields(String dbFileName, String fieldIDs[], final SignalBrowserPVTableModel model) throws java.sql.SQLException {
        setMessage("Loading Field Data...");
        setProgressIndeterminate(true);
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM ");
        StringBuffer whereClause = new StringBuffer(Main.SCHEMA);
        whereClause.append(".ioc_db_file_asgn_sgnl, ");
        whereClause.append(Main.SCHEMA);
        whereClause.append(".sgnl_fld, ");
        whereClause.append(Main.SCHEMA);
        whereClause.append(".sgnl_rec WHERE ioc_db_File_asgn_sgnl.ext_src_file_nm = ? AND ioc_db_file_asgn_sgnl.sgnl_id = sgnl_rec.sgnl_id AND sgnl_rec.sgnl_id = sgnl_fld.sgnl_id AND sgnl_rec.rec_type_id = sgnl_fld.rec_type_id AND sgnl_fld.fld_id IN (");
        for (int i = 0; i < fieldIDs.length; i++) {
            if (i > 0) whereClause.append(", ");
            whereClause.append("?");
        }
        whereClause.append(")");
        sql.append(whereClause);
        Connection oracleConnection = getConnection();
        PreparedStatement countStatement = oracleConnection.prepareStatement(sql.toString());
        try {
            countStatement.setString(1, dbFileName);
            for (int i = 0; i < fieldIDs.length; i++) countStatement.setString(i + 2, fieldIDs[i]);
            ResultSet countResult = countStatement.executeQuery();
            try {
                countResult.next();
                setProgressMaximum(countResult.getInt(1));
            } finally {
                countResult.close();
            }
        } finally {
            countStatement.close();
        }
        sql = new StringBuffer("SELECT sgnl_fld.fld_val, sgnl_fld.sgnl_id, sgnl_fld.rec_type_id, sgnl_fld.fld_id FROM ");
        sql.append(whereClause);
        sql.append(" ORDER BY sgnl_id, fld_id");
        PreparedStatement signalFieldStatement = oracleConnection.prepareStatement(sql.toString());
        try {
            signalFieldStatement.setString(1, dbFileName);
            for (int i = 0; i < fieldIDs.length; i++) signalFieldStatement.setString(i + 2, fieldIDs[i]);
            ResultSet fieldResults = signalFieldStatement.executeQuery();
            try {
                int progress = 0;
                setProgressValue(0);
                setProgressIndeterminate(false);
                while (fieldResults.next()) {
                    final SignalField field = new SignalField(fieldResults.getString("fld_val"));
                    field.setInDatabase(true);
                    String fieldID = fieldResults.getString("fld_ID");
                    String recordTypeID = fieldResults.getString("rec_type_id");
                    SignalFieldType fieldType = model.findFieldType(fieldID, recordTypeID);
                    field.setType(fieldType);
                    final String signalID = fieldResults.getString("sgnl_id");
                    model.getSignal(signalID).addField(field);
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            int column = model.findColumn(field.getType().getID());
                            if (column >= 0) {
                                int row = model.findSignalRow(signalID);
                                model.fireTableCellUpdated(row, column);
                            }
                        }
                    });
                    setProgressValue(++progress);
                }
                assignDefaultTypesToFields(fieldIDs, model);
            } finally {
                clearProgress();
                fieldResults.close();
            }
        } finally {
            signalFieldStatement.close();
        }
    }

    private void assignDefaultTypesToFields(String[] fieldIDs, SignalBrowserPVTableModel model) {
        setProgressIndeterminate(true);
        setMessage("Assigning Default Menus to Signals...");
        int signalCount = model.getRowCount();
        if (signalCount > 0) setProgressMaximum(signalCount - 1); else setProgressMaximum(0);
        int progress = 0;
        setProgressValue(0);
        setProgressIndeterminate(false);
        for (int i = 0; i < signalCount; i++) {
            Signal currentSignal = model.getSignalAt(i);
            for (int j = 0; j < fieldIDs.length; j++) {
                SignalField field = currentSignal.getField(fieldIDs[j]);
                if (field == null) {
                    field = new SignalField();
                    String currentRecordType = currentSignal.getType().getRecordType().getID();
                    SignalFieldType currentFieldType = model.findFieldType(fieldIDs[j], currentRecordType);
                    if (currentFieldType != null) {
                        field.setType(currentFieldType);
                        currentSignal.addField(field);
                    }
                }
            }
            setProgressValue(++progress);
        }
        setProgressIndeterminate(true);
        setMessage(" ");
    }
}
