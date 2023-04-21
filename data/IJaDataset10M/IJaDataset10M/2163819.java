package gov.sns.apps.jeri.apps.signaldetails;

import javax.swing.table.AbstractTableModel;
import javax.sql.*;
import java.sql.*;
import gov.sns.apps.jeri.tools.database.swing.DatabaseTableModel;
import gov.sns.apps.jeri.data.EpicsGroup;
import gov.sns.apps.jeri.data.Signal;
import gov.sns.apps.jeri.data.SignalField;
import gov.sns.apps.jeri.data.EpicsRecordType;
import gov.sns.apps.jeri.data.SignalFieldType;
import gov.sns.apps.jeri.Main;

/**
 * Provides a model for the signal detail table. This table is the old record
 * browser and displays the information in sgnl_rec and sgnl_fld for a 
 * particular <CODE>Signal</CODE>.
 * 
 * @author Chris Fowlkes
 */
public class SignalDetailTableModel extends AbstractTableModel implements DatabaseTableModel {

    /**
   * Holds the <CODE>Signal</CODE> being displayed in the table.
   */
    private Signal signal;

    /**
   * Holds the original <CODE>Signal</CODE> data passed in to the interface.
   * This is used to determine if the data has changed.
   */
    private Signal originalSignalData;

    /**
   * Holds the <CODE>Connection</CODE> for the window. This is initialized when 
   * the data source property is set.
   */
    private Connection oracleConnection;

    /**
   * Holds the query used to retrieve the data for the <CODE>Signal</CODE>.
   */
    private PreparedStatement signalQuery;

    /**
   * Holds the query used to retrieve the data for the fields associated with 
   * the <CODE>Signal</CODE>.
   */
    private PreparedStatement fieldDataQuery;

    /**
   * Holds the query used to retrieve all available fields for the signal.
   */
    private PreparedStatement allFieldsQuery;

    /**
   * Holds the query used to update the value for fields already in the 
   * database.
   */
    private PreparedStatement updateFieldQuery;

    /**
   * Holds the query used to insert new fields into the database.
   */
    private PreparedStatement insertFieldQuery;

    /**
   * Flag that determines if the commit and rollback buttons should be enabled.
   */
    private boolean commitNeeded;

    /**
   * Creates a new <CODE>SignalDetailTableModel</CODE>.
   */
    public SignalDetailTableModel() {
    }

    /**
   * Determines if the given cell is editable. The only editable cell is the 
   * interior value cell.
   * 
   * @param rowIndex The row index of the cell to check.
   * @param columnIndex The column index of the cell to check.
   * @return <CODE>true</CODE> if the cell is editable, <CODE>false</CODE> otherwise.
   */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1;
    }

    /**
   * Sets the value at the given cell.
   * 
   * @param aValue The value for the given cell.
   * @param rowIndex The row index of cell to set the value for.
   * @param columnIndex The column index of the cell to set the value for.
   */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex != 1) throw new IllegalArgumentException(columnIndex + " is not a valid column number.");
        if (aValue == null) getSignal().getFieldAt(rowIndex).setValue(""); else getSignal().getFieldAt(rowIndex).setValue(aValue.toString().trim());
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    /**
   * Gets the value that appears in the header above the given column.
   * 
   * @param column The index of the column to return the name of.
   * @return The name of the column to appear in the table header.
   */
    @Override
    public String getColumnName(int column) {
        String name;
        switch(column) {
            case 0:
                name = "Field";
                break;
            case 1:
                name = "Value";
                break;
            case 2:
                name = "Description";
                break;
            case 3:
                name = "Prompt Group";
                break;
            default:
                throw new IllegalArgumentException(column + " is not a valid column number.");
        }
        return name;
    }

    /**
   * Gets the number of columns in the table. This method always returns 5.
   * 
   * @return The number of columns in the table.
   */
    public int getColumnCount() {
        return 4;
    }

    /**
   * Returns the number of rows in the table. The number of rows is equal to the 
   * number of fields associated with the <CODE>Signal</CODE>.
   * 
   * @return The number of fields in the <CODE>Signal</CODE>.
   */
    public int getRowCount() {
        Signal signal = getSignal();
        if (signal == null) return 0; else return signal.getFieldCount();
    }

    /**
   * Returns the value of the given cell.
   * 
   * @param row The row index of the cell to return the value of.
   * @param column The column index of the cell to return the value of.
   * @return The value of the given cell.
   */
    public Object getValueAt(int row, int column) {
        SignalField field = getSignal().getFieldAt(row);
        Object value;
        switch(column) {
            case 0:
                value = field.getType().getID();
                break;
            case 1:
                value = field.getValue();
                break;
            case 2:
                value = field.getType().getDescription();
                break;
            case 3:
                value = field.getType().getPromptGroup();
                break;
            default:
                throw new IllegalArgumentException(column + " is not a valid column number.");
        }
        return value;
    }

    /**
   * Sets the <CODE>Signal</CODE> for the model. The <CODE>Signal</CODE> needs 
   * to have the ID and EPICS record type values set. All other fields displayed 
   * will be loaded from the database.
   * 
   * @param signal The <CODE>Signal</CODE> to display in the table.
   */
    public void setSignal(Signal signal) {
        this.signal = signal;
    }

    /**
   * Gets the <CODE>Signal</CODE> shown in the table.
   * 
   * @return The <CODE>Signal</CODE> displayed in the table.
   */
    public Signal getSignal() {
        return signal;
    }

    /**
   * Sets the <CODE>DataSource</CODE> used by the window to connect to the 
   * database. This method sets up all of the prepared statements used by the 
   * model.
   *
   * @param connectionPool The <CODE>DataSource</CODE> to use to connect to the database.
   * @throws java.sql.SQLException Not thrown in this method, but can be thrown in subclasses.
   */
    public void setDataSource(DataSource connectionPool) throws java.sql.SQLException {
        oracleConnection = connectionPool.getConnection();
        oracleConnection.setAutoCommit(false);
    }

    /**
   * Reloads the data in the model using the <CODE>DataSource</CODE> and 
   * <CODE>Signal</CODE> in the model. This should only be called after 
   * <CODE>setDataSource</CODE> and <CODE>setSignal</CODE> have been set.
   * 
   * @throws java.sql.SQLException Not thrown in this method, but can be thrown in subclasses.
   */
    public void refresh() throws java.sql.SQLException {
        Signal currentSignal = getSignal();
        currentSignal.removeAllFields();
        String currentSignalID = currentSignal.getID();
        EpicsRecordType currentRecordType = currentSignal.getType().getRecordType();
        if (signalQuery == null) {
            StringBuffer query = new StringBuffer("SELECT epics_grp_id, ext_src FROM ");
            query.append(Main.SCHEMA);
            query.append(".sgnl_rec WHERE sgnl_id = ? AND rec_type_id = ?");
            signalQuery = oracleConnection.prepareStatement(query.toString());
        }
        signalQuery.setString(1, currentSignalID);
        signalQuery.setString(2, currentRecordType.getID());
        ResultSet signalData = signalQuery.executeQuery();
        try {
            if (signalData.next()) {
                EpicsGroup currentGroup = new EpicsGroup();
                currentGroup.setID(signalData.getString("epics_grp_id"));
                currentSignal.setGroup(currentGroup);
                currentSignal.setExternalSource(signalData.getString("ext_src"));
            } else {
                currentSignal.setGroup(new EpicsGroup());
                currentSignal.setExternalSource(null);
            }
        } finally {
            signalData.close();
        }
        if (allFieldsQuery == null) {
            StringBuffer query = new StringBuffer("SELECT fld_id, fld_desc, fld_prmt_grp FROM ");
            query.append(Main.SCHEMA);
            query.append(".sgnl_fld_def WHERE rec_type_id = ? ORDER BY fld_id");
            allFieldsQuery = oracleConnection.prepareStatement(query.toString());
        }
        allFieldsQuery.setString(1, currentRecordType.getID());
        ResultSet allFieldData = allFieldsQuery.executeQuery();
        try {
            while (allFieldData.next()) {
                SignalFieldType currentFieldType = new SignalFieldType();
                currentFieldType.setID(allFieldData.getString("fld_id"));
                currentFieldType.setRecordType(currentRecordType);
                currentFieldType.setDescription(allFieldData.getString("fld_desc"));
                currentFieldType.setPromptGroup(allFieldData.getString("fld_prmt_grp"));
                SignalField currentField = new SignalField();
                currentField.setType(currentFieldType);
                currentSignal.addField(currentField);
            }
        } finally {
            allFieldData.close();
        }
        if (fieldDataQuery == null) {
            StringBuffer query = new StringBuffer("SELECT fld_id, fld_val FROM ");
            query.append(Main.SCHEMA);
            query.append(".sgnl_fld WHERE sgnl_id = ? AND rec_type_id = ? ORDER BY fld_id");
            fieldDataQuery = oracleConnection.prepareStatement(query.toString());
        }
        fieldDataQuery.setString(1, currentSignalID);
        fieldDataQuery.setString(2, currentRecordType.getID());
        ResultSet fieldData = fieldDataQuery.executeQuery();
        try {
            while (fieldData.next()) {
                String currentFieldID = fieldData.getString("fld_id");
                SignalField currentField = currentSignal.getField(currentFieldID);
                currentField.setValue(fieldData.getString("fld_val"));
                currentField.setInDatabase(true);
            }
        } finally {
            fieldData.close();
        }
        originalSignalData = (Signal) currentSignal.clone();
        fireTableDataChanged();
    }

    /**
   * Cancels any changes to the data by reverting to the original data.
   */
    public void cancel() {
        setSignal((Signal) originalSignalData.clone());
        fireTableDataChanged();
    }

    /**
   * Determines if the data has changed.
   * 
   * @return <CODE>true</CODE> if the data in the model has chaned, <CODE>false</CODE> otherwise.
   */
    public boolean isChanged() {
        return !getSignal().equals(originalSignalData);
    }

    /**
   * Determines if a commit is needed. A commit is needed if data has been 
   * changed locally or if changes have been posted but not commited or rolled 
   * back.
   * 
   * @return <CODE>true</CODE> if changes are pending, <CODE>false</CODE> otherwise.
   */
    public boolean isCommitNeeded() {
        return commitNeeded || isChanged();
    }

    /**
   * Commits any pending changes to the database.
   * 
   * @throws java.sql.SQLException Thrown on sql error.
   */
    public void commit() throws java.sql.SQLException {
        oracleConnection.commit();
        commitNeeded = false;
    }

    /**
   * Posts the changes made to the data to the database.
   * 
   * @throws java.sql.SQLException Thrown on sql error.
   */
    public void post() throws java.sql.SQLException {
        int fieldCount = getRowCount();
        Signal signal = getSignal();
        String currentSignalID = signal.getID();
        String currentRecordTypeID = signal.getType().getRecordType().getID();
        for (int i = 0; i < fieldCount; i++) {
            SignalField currentField = signal.getFieldAt(i);
            String currentFieldID = currentField.getType().getID();
            SignalField originalField = originalSignalData.getField(currentFieldID);
            if (!currentField.equals(originalField)) {
                if (currentField.isInDatabase()) {
                    if (updateFieldQuery == null) {
                        StringBuffer query = new StringBuffer("UPDATE ");
                        query.append(Main.SCHEMA);
                        query.append(".sgnl_fld SET fld_val = ? WHERE sgnl_id = ? AND rec_type_id = ? AND fld_id = ?");
                        updateFieldQuery = oracleConnection.prepareStatement(query.toString());
                    }
                    String currentValue = currentField.getValue();
                    updateFieldQuery.setString(1, currentValue);
                    updateFieldQuery.setString(2, currentSignalID);
                    updateFieldQuery.setString(3, currentRecordTypeID);
                    updateFieldQuery.setString(4, currentFieldID);
                    updateFieldQuery.execute();
                    commitNeeded = true;
                    originalField.setValue(currentValue);
                } else {
                    if (insertFieldQuery == null) {
                        StringBuffer query = new StringBuffer("INSERT INTO ");
                        query.append(Main.SCHEMA);
                        query.append(".sgnl_fld (sgnl_id, fld_id, rec_type_id, fld_val) VALUES (?, ?, ?, ?)");
                        insertFieldQuery = oracleConnection.prepareStatement(query.toString());
                    }
                    String currentValue = currentField.getValue();
                    insertFieldQuery.setString(1, currentSignalID);
                    insertFieldQuery.setString(2, currentFieldID);
                    insertFieldQuery.setString(3, currentRecordTypeID);
                    insertFieldQuery.setString(4, currentValue);
                    insertFieldQuery.execute();
                    commitNeeded = true;
                    originalField.setValue(currentValue);
                    currentField.setInDatabase(true);
                    originalField.setInDatabase(true);
                }
            }
        }
        fireTableDataChanged();
    }

    /**
   * Performs a rollback on the database connection.
   * 
   * @throws java.sql.SQLException Thrown on sql error.
   */
    public void rollback() throws java.sql.SQLException {
        oracleConnection.rollback();
        commitNeeded = false;
    }

    /**
   * Called when the insert tool bar button is clicked. This method is provided 
   * as part of the <CODE>DatabaseTableModel</CODE> interface, but is not 
   * implemented.
   */
    public void insert() {
    }

    /**
   * Called when the delete tool bar button is clicked. This method is provided 
   * as part of the <CODE>DatabaseTableModel</CODE> interface, but is not 
   * implemented.
   * 
   * @param rows The rows to delete
   */
    public void delete(int[] rows) {
    }

    /**
   * This method is provided for the abstract method contained in the 
   * <CODE>AbstractTableInterface</CODE> class, but always returns 
   * <CODE>false</CODE> as filters are not supported.
   */
    public boolean isFiltered() {
        return false;
    }
}
