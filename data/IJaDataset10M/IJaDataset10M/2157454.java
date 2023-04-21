package gov.sns.apps.jeri.apps.signaltablebrowser;

import gov.sns.apps.jeri.tools.swing.AbstractSignalTableModel;
import java.awt.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.StructDescriptor;
import oracle.sql.STRUCT;
import gov.sns.apps.jeri.data.EpicsGroup;
import gov.sns.apps.jeri.data.SignalField;
import gov.sns.apps.jeri.data.SignalFieldType;
import gov.sns.apps.jeri.data.SignalFieldMenu;
import gov.sns.apps.jeri.data.Device;
import gov.sns.apps.jeri.data.Signal;
import gov.sns.apps.jeri.data.EpicsRecordType;
import gov.sns.apps.jeri.data.SignalType;
import gov.sns.apps.jeri.Main;

/**
 * This class provides a model for the table in the signal table browser 
 * interface. It displays the data from the SGNL_REC and SGNL_FLD tables.
 * 
 * @author Chris Fowlkes
 */
public class SignalBrowserPVTableModel extends AbstractSignalTableModel {

    /**
   * Holds the <CODE>Connection</CODE> for the window. This is initialized when 
   * the data source property is set.
   */
    private Connection oracleConnection;

    /**
   * Holds the column index for the name column.
   */
    public static final int NAME_COLUMN = 0;

    /**
   * Holds the column index for the type column.
   */
    public static final int TYPE_COLUMN = 1;

    /**
   * Holds the IDs of the signal fields that have been added to the model.
   */
    private ArrayList signalFieldIDs = new ArrayList();

    /**
   * Holds the query used to insert fields into the database.
   */
    private PreparedStatement insertFieldStatement;

    /**
   * Holds the query used to update fields in the database.
   */
    private PreparedStatement updateFieldStatement;

    /**
   * Holds the instances of <CODE>Field</CODE> that have been changed.
   */
    private ArrayList fieldsChanged = new ArrayList();

    /**
   * Holds the instances of <CODE>Signal</CODE> created by the 
   * <CODE>Duplicate</CODE> method that are ready to be inserted.
   */
    private ArrayList signalsToInsert = new ArrayList();

    private ArrayList originalInsertIDs = new ArrayList();

    /**
   * Holds the field types being used
   */
    private HashMap fieldTypes = new HashMap();

    private JProgressBar progressBar;

    private JLabel progressLabel;

    /**
   * Holds the fetch size used by the queries.
   */
    private int fetchSize = 750;

    /**
   * Creates a new <CODE>SignalTableBrowserModel</CODE>.
   */
    public SignalBrowserPVTableModel() {
    }

    /**
   * Returns the number of columns in the model. The name and type columns are 
   * always present, so the number of columns is the number of epics fields that 
   * have been added to the display plus 2.
   * 
   * @return The number of columns in the table.
   */
    public int getColumnCount() {
        return signalFieldIDs.size() + 2;
    }

    /**
   * Returns the value for a given cell in the table.
   * 
   * @param row The row of the cell to return the value for.
   * @param column The column of the cell to return the value for.
   * @return The value for the given table cell.
   */
    public Object getValueAt(int row, int column) {
        Signal currentSignal = getSignalAt(row);
        Object value;
        switch(column) {
            case NAME_COLUMN:
                value = currentSignal;
                break;
            case TYPE_COLUMN:
                value = currentSignal.getType().getRecordType();
                break;
            default:
                String fieldID = getFieldID(column);
                value = currentSignal.getField(fieldID);
        }
        return value;
    }

    /**
   * Returns the name of the column, which appears in the header above the 
   * column.
   * 
   * @param column The number of the column to return the name of.
   * @return The name of the given column.
   */
    @Override
    public String getColumnName(int column) {
        if (column == NAME_COLUMN) return "SGNL_ID"; else if (column == TYPE_COLUMN) return "TYPE"; else return getFieldID(column);
    }

    /**
   * Adds the given signal field to the table.
   * 
   * @param fieldID The value for the fld_id field in the sgnl_fld table of the record to add.
   * @throws java.sql.SQLException Thrown on sql error.
   */
    public void addField(String fieldID) throws SQLException {
        signalFieldIDs.add(fieldID);
        fireTableStructureChanged();
    }

    /**
   * Drops the field from the table model.
   * 
   * @param fieldID The ID of the field to drop.
   */
    public void dropField(String fieldID) {
        signalFieldIDs.remove(fieldID);
        fireTableStructureChanged();
    }

    /**
   * Determines if the given cell is editable or not. The name column is always 
   * editable, the type column is only editable for new rows, and the signal 
   * fields are editable if there is an entry in the sgnl_fld_def for the 
   * rec_type_id and sgnl_id.
   * 
   * @param row The row of the cell to check.
   * @param column The column of the cell to check.
   * @return <CODE>true</CODE> if the cell is editable, <CODE>false</CODE> otherwise.
   */
    @Override
    public boolean isCellEditable(int row, int column) {
        boolean editable;
        switch(column) {
            case NAME_COLUMN:
                editable = !getSignalAt(row).isInDatabase();
                break;
            case TYPE_COLUMN:
                editable = false;
                break;
            default:
                editable = findFieldType(row, column) != null;
        }
        return editable;
    }

    /**
   * Gets the type for the field. This method is called by the cell editor to 
   * populate the combo box, and by the renderer to create the tool tip for 
   * EPICS signal fields.
   * 
   * @param row The row of the cell to return the field type for.
   * @param column The column of the cell to return the field type for.
   * @return The type of the field in the given cell.
   */
    public SignalFieldType findFieldType(int row, int column) {
        Signal currentSignal = getSignalAt(row);
        String currentFieldID = getFieldID(column);
        SignalField currentSignalField = currentSignal.getField(currentFieldID);
        SignalFieldType currentFieldType;
        if (currentSignalField == null) {
            String currentRecordType = currentSignal.getType().getRecordType().getID();
            currentFieldType = findFieldType(currentFieldID, currentRecordType);
        } else currentFieldType = currentSignalField.getType();
        return currentFieldType;
    }

    /**
   * Finds a reference to the <CODE>SignalFieldType</CODE> that corresponds to
   * the given field and record type IDs.
   * 
   * @param fieldID The ID of the <CODE>SignalFieldType</CODE> to return.
   * @param recordType The reecord type ID of the <CODE>SignalFieldType</CODE> to return.
   * @return The <CODE>SignalFieldType</CODE> with the given ID and record type, or <CODE>null</CODE> if no matching <CODE>SignalFieldType</CODE> is found.
   */
    public SignalFieldType findFieldType(String fieldID, String recordType) {
        ArrayList types = (ArrayList) fieldTypes.get(fieldID);
        if (types == null) return null; else {
            int typeCount = types.size();
            for (int i = 0; i < typeCount; i++) {
                SignalFieldType currentType = (SignalFieldType) types.get(i);
                if (currentType.getRecordType().getID().equals(recordType)) return currentType;
            }
        }
        return null;
    }

    /**
   * Gets the EPICS field ID for the given column.
   * 
   * @param column The column to return the EPICS field ID for.
   * @return The EPICS field ID for the given column.
   */
    private String getFieldID(int column) {
        return signalFieldIDs.get(column - 2).toString();
    }

    /**
   * Gets the class represented by the column at the given index.
   * 
   * @param columnIndex The index of the column to get the class of.
   * @return The class represented by the column at the given index.
   */
    @Override
    public Class getColumnClass(int columnIndex) {
        if (columnIndex == 0) return gov.sns.apps.jeri.data.Signal.class; else if (columnIndex >= 2) return gov.sns.apps.jeri.data.SignalField.class; else return super.getColumnClass(columnIndex);
    }

    /**
   * Returns the field IDs of the fields being displayed in the table.
   * 
   * @return Instances of <CODE>String</CODE> representing the field IDs that have been added to the model with the <CODE>addFIeld</CODE> method.
   */
    public String[] getFieldIDs() {
        return (String[]) signalFieldIDs.toArray(new String[signalFieldIDs.size()]);
    }

    /**
   * Returns a <CODE>ArrayList</CODE> containing the distinct record types in 
   * the model.
   * 
   * @return A <CODE>ArrayList</CODE> containing the distinct EPICS record types displayed in the table.
   */
    public ArrayList getRecordTypes() {
        int rowCount = getRowCount();
        ArrayList types = new ArrayList();
        for (int i = 0; i < rowCount; i++) {
            String currentType = getSignalAt(i).getType().getRecordType().getID();
            if (!types.contains(currentType)) types.add(currentType);
        }
        return types;
    }

    /**
   * Sets the value at the given cell. This method changes the data for the 
   * signal at the given row, and the field at the given column.
   * 
   * @param aValue The new value for the cell given.
   * @param rowIndex The index of the signal to change.
   * @param columnIndex The column changed.
   */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        String newStringValue, oldStringValue;
        if (aValue == null) newStringValue = ""; else newStringValue = aValue.toString();
        Object oldValue = getValueAt(rowIndex, columnIndex);
        if (oldValue == null) oldStringValue = ""; else oldStringValue = oldValue.toString();
        if (!Main.compare(newStringValue, oldStringValue)) try {
            Signal currentSignal = getSignalAt(rowIndex);
            switch(columnIndex) {
                case NAME_COLUMN:
                    String newID = aValue.toString();
                    String originalID = currentSignal.getID();
                    String[] signalIDs = new String[] { originalID, newID };
                    String[][] signalIDArray = new String[][] { signalIDs };
                    String messageText = duplicateDatabaseSignal(signalIDArray, false);
                    JScrollPane message = new JScrollPane();
                    JTextPane textComponent = new JTextPane();
                    textComponent.setEditable(false);
                    textComponent.setText(messageText);
                    message.getViewport().add(textComponent, null);
                    Dimension currentSize = message.getPreferredSize();
                    int width = Math.min(400, currentSize.width);
                    int height = Math.min(400, currentSize.height);
                    message.setPreferredSize(new Dimension(width, height));
                    int confirm = JOptionPane.showConfirmDialog(getProgressBar(), message, "Confirm ID Change", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if (confirm == JOptionPane.OK_OPTION) {
                        signalsToInsert.add(currentSignal);
                        originalInsertIDs.add(currentSignal.getID());
                        currentSignal.setID(newID);
                    }
                    break;
                default:
                    SignalField field = currentSignal.getField(getFieldID(columnIndex));
                    if (aValue == null) field.setValue(null); else field.setValue(aValue.toString());
                    getFieldsChanged().add(field);
            }
            fireTableCellUpdated(rowIndex, columnIndex);
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(getProgressBar(), ex.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
   * Takes a field ID and returns the index of the column in the model that 
   * represents that field.
   * 
   * @param fieldID The ID of the field to return the model's column number for.
   * @return The column index of the given field ID.
   */
    public int convertFieldToColumn(String fieldID) {
        int fieldIndex = signalFieldIDs.indexOf(fieldID);
        if (fieldIndex >= 0) fieldIndex = fieldIndex + 2;
        return fieldIndex;
    }

    /**
   * Returns <CODE>true</CODE> if the data has been changed, but not saved to 
   * the database.
   * 
   * @return <CODE>true</CODE> if the data has changed, <CODE>false</CODE> if not.
   */
    public boolean isChanged() {
        return fieldsChanged.size() > 0 || signalsToInsert.size() > 0;
    }

    /**
   * Sets the field types for the model.
   * 
   * @param fieldTypes The instances of <CODE>SignalFieldType</CODE> for the model.
   */
    public void setFieldTypes(HashMap fieldTypes) {
        this.fieldTypes = fieldTypes;
    }

    /**
   * Gets the field types for the model.
   * 
   * @return The instances of <CODE>SignalFieldType</CODE> for the model.
   */
    public HashMap getFieldTypes() {
        return fieldTypes;
    }

    /**
   * Gets the IDs of the fields in the model.
   * 
   * @return The field IDs in the model.
   */
    public final ArrayList getSignalFieldIDs() {
        return signalFieldIDs;
    }

    /**
   * Loads the column data for the column representing the given field IDs. The
   * second parameter should be something like 
   * <CODE>"WHERE SGNL_ID LIKE '%MBT%'"</CODE>. It should only deal with fields 
   * in the SGNL_REC table.
   * 
   * @param fieldIDs The field IDs of the columns to load.
   * @param signalFilter The filter used to load the instances of <CODE>Signal</CODE>.
   * @throws java.sql.SQLException Thrown on sql error.
   */
    public void loadFields(String[] fieldIDs, String signalFilter) throws SQLException {
        getFieldsChanged().clear();
        loadFieldTypes(fieldIDs);
        setMessage("Loading Field Data...");
        setProgressIndeterminate(true);
        Statement signalFieldStatement = getConnection().createStatement();
        try {
            Signal currentSignal = null;
            String currentSignalID = "", currentRecordTypeID = null;
            StringBuffer sql = new StringBuffer("SELECT sgnl_fld.fld_val, sgnl_fld.sgnl_id, sgnl_fld.rec_type_id, sgnl_fld.fld_id FROM ");
            sql.append(Main.SCHEMA);
            sql.append(".sgnl_fld, ");
            sql.append(Main.SCHEMA);
            sql.append(".sgnl_rec ");
            StringBuffer whereClause = new StringBuffer("WHERE sgnl_rec.sgnl_id = sgnl_fld.sgnl_id AND sgnl_rec.rec_type_id = sgnl_fld.rec_type_id AND sgnl_fld.fld_id IN (");
            for (int i = 0; i < fieldIDs.length; i++) {
                if (i > 0) whereClause.append(", ");
                whereClause.append("'");
                whereClause.append(fieldIDs[i]);
                whereClause.append("'");
            }
            whereClause.append(") AND sgnl_fld.sgnl_id IN (SELECT sgnl_id FROM ");
            whereClause.append(Main.SCHEMA);
            whereClause.append(".sgnl_rec ");
            whereClause.append(signalFilter);
            whereClause.append(")");
            sql.append(whereClause);
            sql.append(" ORDER BY sgnl_id, fld_id");
            StringBuffer countQuery = new StringBuffer("SELECT COUNT(*) FROM ");
            countQuery.append(Main.SCHEMA);
            countQuery.append(".sgnl_fld, ");
            countQuery.append(Main.SCHEMA);
            countQuery.append(".sgnl_rec ");
            countQuery.append(whereClause);
            int recordCount = findRecordCount(countQuery.toString());
            if (recordCount > 0) setProgressMaximum(recordCount - 1); else setProgressMaximum(0);
            ResultSet fieldResults = signalFieldStatement.executeQuery(sql.toString());
            try {
                int signalCount = getRowCount();
                ArrayList signalRecords = new ArrayList(signalCount);
                for (int i = 0; i < signalCount; i++) signalRecords.add(getSignalAt(i));
                int progress = 0;
                setProgressValue(0);
                setProgressIndeterminate(false);
                while (fieldResults.next()) {
                    String newSignalID = fieldResults.getString("sgnl_id");
                    if (!currentSignalID.equals(newSignalID)) {
                        currentSignalID = newSignalID;
                        currentSignal = null;
                        signalCount = signalRecords.size();
                        for (int i = 0; i < signalCount; i++) {
                            Signal newSignal = (Signal) signalRecords.get(i);
                            if (newSignal.getID().equals(newSignalID)) {
                                currentSignal = newSignal;
                                currentRecordTypeID = currentSignal.getType().getRecordType().getID();
                                signalRecords.remove(i);
                                break;
                            }
                        }
                    }
                    if (currentSignal != null) {
                        final SignalField field = new SignalField(fieldResults.getString("fld_val"));
                        field.setInDatabase(true);
                        String currentFieldID = fieldResults.getString("fld_ID");
                        SignalFieldType currentFieldType = findFieldType(currentFieldID, currentRecordTypeID);
                        field.setType(currentFieldType);
                        final Signal finalSignal = currentSignal;
                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                                finalSignal.addField(field);
                                int column = findColumn(field.getType().getID());
                                if (column >= 0) {
                                    int row = findSignalRow(finalSignal.getID());
                                    fireTableCellUpdated(row, column);
                                }
                            }
                        });
                    }
                    setProgressValue(++progress);
                }
                setProgressIndeterminate(true);
                setMessage("Assigning Default Menus to Signals...");
                signalCount = getRowCount();
                if (signalCount > 0) setProgressMaximum(signalCount - 1); else setProgressMaximum(0);
                progress = 0;
                setProgressValue(0);
                setProgressIndeterminate(false);
                for (int i = 0; i < signalCount; i++) {
                    currentSignal = getSignalAt(i);
                    for (int j = 0; j < fieldIDs.length; j++) {
                        SignalField field = currentSignal.getField(fieldIDs[j]);
                        if (field == null) {
                            field = new SignalField();
                            String currentRecordType = currentSignal.getType().getRecordType().getID();
                            SignalFieldType currentFieldType = findFieldType(fieldIDs[j], currentRecordType);
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
            } finally {
                setProgressValue(0);
                setProgressIndeterminate(false);
                fieldResults.close();
            }
        } finally {
            signalFieldStatement.close();
        }
    }

    /**
   * Loads all of the data from the SGNL_FLD_DEF and SGNL_FLD_MENU tables for
   * the given field IDs. The instances of <CODE>SignalFieldType</CODE> are 
   * stored in the <CODE>fieldTypes HashMap</CODE> with the field IDs as keys
   * and instances of <CODE>ArrayList</CODE> wich hold the instances of 
   * <CODE>SignalFieldType</CODE> for each field ID as values. If the values for
   * a field ID are already loaded, they are not refreshed. Instead the field 
   * ID is skipped.
   * 
   * @param fieldIDs The fieldIDs The field IDs to load the field types for.
   * @throws java.sql.SQLException Thrown on sql error.
   */
    public void loadFieldTypes(String[] fieldIDs) throws java.sql.SQLException {
        try {
            setMessage("Loading Field Types...");
            setProgressIndeterminate(true);
            HashMap fieldTypes = getFieldTypes();
            fieldTypes.clear();
            ArrayList fieldsToLoad = new ArrayList();
            for (int i = 0; i < fieldIDs.length; i++) {
                if (fieldTypes.get(fieldIDs[i]) == null) fieldsToLoad.add(fieldIDs[i]);
            }
            if (fieldsToLoad.size() <= 0) return;
            Statement fieldTypeStatement = getConnection().createStatement();
            try {
                fieldTypeStatement.setFetchSize(fetchSize);
                StringBuffer query = new StringBuffer("SELECT sgnl_fld_def.fld_id, sgnl_fld_def.rec_type_id, sgnl_fld_def.fld_desc, sgnl_fld_menu.sgnl_fld_menu_id, sgnl_fld_menu.fld_menu_val FROM ");
                query.append(Main.SCHEMA);
                query.append(".sgnl_fld_def, ");
                query.append(Main.SCHEMA);
                query.append(".sgnl_fld_menu ");
                StringBuffer whereClause = new StringBuffer("where sgnl_fld_def.fld_id in (");
                int fieldCount = fieldsToLoad.size();
                for (int i = 0; i < fieldCount; i++) {
                    if (i > 0) whereClause.append(", ");
                    whereClause.append("'");
                    whereClause.append(fieldsToLoad.get(i));
                    whereClause.append("'");
                }
                whereClause.append(") and sgnl_fld_def.sgnl_fld_menu_id = sgnl_fld_menu.sgnl_fld_menu_id (+)");
                query.append(whereClause);
                query.append(" order by sgnl_fld_def.fld_id, sgnl_fld_def.rec_type_id");
                StringBuffer countQuery = new StringBuffer("SELECT COUNT(*) FROM ");
                countQuery.append(Main.SCHEMA);
                countQuery.append(".sgnl_fld_def, ");
                countQuery.append(Main.SCHEMA);
                countQuery.append(".sgnl_fld_menu ");
                countQuery.append(whereClause);
                int recordCount = findRecordCount(countQuery.toString());
                if (recordCount <= 0) return;
                setProgressMaximum(recordCount);
                ResultSet fieldTypeResults = fieldTypeStatement.executeQuery(query.toString());
                try {
                    String oldFieldID = "", oldRecordType = "";
                    ArrayList currentFieldIDTypes = null;
                    SignalFieldType currentType = null;
                    SignalFieldMenu currentMenu = null;
                    int progress = 0;
                    setProgressValue(0);
                    setProgressIndeterminate(false);
                    while (fieldTypeResults.next()) {
                        String newFieldID = fieldTypeResults.getString("fld_id");
                        String newRecordType = fieldTypeResults.getString("rec_type_id");
                        if (!newFieldID.equals(oldFieldID)) {
                            currentFieldIDTypes = new ArrayList();
                            fieldTypes.put(newFieldID, currentFieldIDTypes);
                            currentType = null;
                        }
                        if (currentType == null || !newRecordType.equals(oldRecordType)) {
                            EpicsRecordType currentRecordType = new EpicsRecordType(newRecordType);
                            String description = fieldTypeResults.getString("fld_desc");
                            currentType = new SignalFieldType(newFieldID, currentRecordType, description);
                            String currentMenuID = fieldTypeResults.getString("sgnl_fld_menu_id");
                            if (currentMenuID != null) {
                                currentMenu = new SignalFieldMenu(currentMenuID);
                                currentType.setMenu(currentMenu);
                            } else currentMenu = null;
                            currentFieldIDTypes.add(currentType);
                        }
                        if (currentMenu != null) {
                            String currentMenuItem = fieldTypeResults.getString("fld_menu_val");
                            currentMenu.addMenuItem(currentMenuItem);
                        }
                        oldFieldID = newFieldID;
                        oldRecordType = newRecordType;
                        setProgressValue(++progress);
                    }
                } finally {
                    fieldTypeResults.close();
                }
            } finally {
                fieldTypeStatement.close();
            }
        } finally {
            setProgressValue(0);
            setProgressIndeterminate(false);
            setMessage(" ");
        }
    }

    /**
   * Uses <CODE>SwingUtilities.invokeLater</CODE> to set the indeterminate 
   * property of the progress bar.
   * 
   * @param indeterminate The new value of the indeterminate property of the progress bar.
   */
    private void setProgressIndeterminate(final boolean indeterminate) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JProgressBar progressBar = getProgressBar();
                if (progressBar != null) progressBar.setIndeterminate(indeterminate);
            }
        });
    }

    /**
   * Uses <CODE>SwingUtilities.invokeLater</CODE> to safely set the value of the 
   * progress bar from a <CODE>Thread</CODE>.
   * 
   * @param progressValue The value to pass to the <CODE>setValue</CODE> method of the progress bar.
   */
    private void setProgressValue(final int progressValue) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JProgressBar progressBar = getProgressBar();
                if (progressBar != null) progressBar.setValue(progressValue);
            }
        });
    }

    /**
   * Uses <CODE>SwingUtilities.invokeLater</CODE> to safely set the maximum 
   * value of the progress bar from a <CODE>Thread</CODE>.
   * 
   * @param progressMaximum The value to pass to the <CODE>setMaximum</CODE> method of the progress bar.
   */
    private void setProgressMaximum(final int progressMaximum) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JProgressBar progressBar = getProgressBar();
                if (progressBar != null) progressBar.setMaximum(progressMaximum);
            }
        });
    }

    /**
   * Uses <CODE>SwingUtilities.invokeLater</CODE> to safely set the text of the 
   * label in the status bar from a <CODE>Thread</CODE>.
   * 
   * @param message The value to pass to the <CODE>setText</CODE> method of the label.
   */
    private void setMessage(final String message) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JLabel progressLabel = getProgressLabel();
                if (progressLabel != null) progressLabel.setText(message);
            }
        });
    }

    /**
   * Gets the <CODE>JProgressBar</CODE> used by the model.
   * 
   * @return The <CODE>JProgressBar</CODE> used by the model.
   */
    public JProgressBar getProgressBar() {
        return progressBar;
    }

    /**
   * Sets the <CODE>JProgressBar</CODE> for the model.
   * 
   * @param newProgressBar The <CODE>JProgressBar</CODE> for the model.
   */
    public void setProgressBar(JProgressBar newProgressBar) {
        progressBar = newProgressBar;
    }

    /**
   * Gets the <CODE>JLabel</CODE> used by the model to convey status.
   * 
   * @return The <CODE>JLabel</CODE> used by the model.
   */
    public JLabel getProgressLabel() {
        return progressLabel;
    }

    /**
   * Sets the <CODE>JLabel</CODE> for the model to use to convey status.
   * 
   * @param newProgressLabel The <CODE>JLabel</CODE> for the model.
   */
    public void setProgressLabel(JLabel newProgressLabel) {
        progressLabel = newProgressLabel;
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
   * Gets the <CODE>Connection</CODE> used by the model to load the field data.
   * 
   * @return The <CODE>Connection</CODE> used by the model.
   */
    public Connection getConnection() {
        return oracleConnection;
    }

    /**
   * Sets the <CODE>Connection</CODE> used by the model to load the field data.
   * 
   * @param newConnection The <CODE>Connection</CODE> used by the model.
   */
    public void setConnection(Connection newConnection) {
        oracleConnection = newConnection;
    }

    /**
   * Commits any duplicated signals.
   * 
   * @return A description of the change and it's repercussions.
   * @throws java.sql.SQLException Thrown on sql error.
   */
    public String commitDuplicates() throws java.sql.SQLException {
        String message;
        int duplicateCount = signalsToInsert.size();
        if (duplicateCount > 0) {
            String[][] signalIDs = new String[duplicateCount][2];
            for (int i = 0; i < duplicateCount; i++) {
                signalIDs[i][0] = originalInsertIDs.get(i).toString();
                signalIDs[i][1] = ((Signal) signalsToInsert.get(i)).getID();
            }
            message = duplicateDatabaseSignal(signalIDs, true);
            for (int i = 0; i < duplicateCount; i++) ((Signal) signalsToInsert.get(i)).setInDatabase(true);
        } else message = null;
        return message;
    }

    /**
   * Posts the fields changed to the database.
   * 
   * @return <CODE>true</CODE> if one or more fields are posted.
   * @throws java.sql.SQLException Thrown on sql error.
   */
    public boolean post() throws java.sql.SQLException {
        boolean commitNeeded = false;
        ArrayList fieldsChanged = getFieldsChanged();
        for (int i = fieldsChanged.size() - 1; i >= 0; i--) {
            SignalField currentField = (SignalField) fieldsChanged.get(i);
            saveField(currentField, currentField.getValue());
            commitNeeded = true;
            fieldsChanged.remove(i);
        }
        return commitNeeded;
    }

    /**
   * Copies the database record matching the given old ID to one with the new 
   * ID. The signal IDs are passed in as a double dimension array such that 
   * <CODE>signalIDs[0]</CODE> is an array defined as 
   * <CODE>{oldSignalID, newSignalID}</CODE>.
   * 
   * @param signalIDs Double dimension array containing old and new signal IDs.
   * @param commit Pass as <CODE>true</CODE> to commit the change, <CODE>false</CODE> to not commit the change.
   * @return A description of the change and it's repercussions.
   * @throws java.sql.SQLException Thrown on sql error.
   */
    private String duplicateDatabaseSignal(String[][] signalIDs, boolean commit) throws java.sql.SQLException {
        StringBuffer sql = new StringBuffer("{? = call ");
        sql.append(Main.SCHEMA);
        sql.append(".EPICS_PKG.rename_sgnl(?, 'N', ?)}");
        Connection oracleConnection = getConnection();
        CallableStatement rdbFunction = oracleConnection.prepareCall(sql.toString());
        try {
            rdbFunction.registerOutParameter(1, Types.VARCHAR);
            Object[] p1arrobj = new Object[signalIDs.length];
            for (int i = 0; i < signalIDs.length; i++) {
                Object[] p1recobj = signalIDs[i];
                StructDescriptor desc1 = StructDescriptor.createDescriptor(Main.SCHEMA + ".RENAMED_SGNL_TYPE", oracleConnection);
                STRUCT p1struct = new STRUCT(desc1, oracleConnection, p1recobj);
                p1arrobj[i] = p1struct;
            }
            ArrayDescriptor desc2 = ArrayDescriptor.createDescriptor(Main.SCHEMA + ".RENAMED_SGNL_TAB", oracleConnection);
            ARRAY p1arr = new ARRAY(desc2, oracleConnection, p1arrobj);
            ((oracle.jdbc.driver.OracleCallableStatement) rdbFunction).setARRAY(2, p1arr);
            if (commit) rdbFunction.setString(3, "Y"); else rdbFunction.setString(3, "N");
            rdbFunction.execute();
            return rdbFunction.getString(1);
        } finally {
            rdbFunction.close();
        }
    }

    /**
   * Saves a <CODE>SignalField</CODE> to the database.
   * 
   * @param fieldToSave The <CODE>SignalField</CODE> to save.
   * @param newValue The new value for the fld_val field.
   * @throws java.sql.SQLException Thrown on sql error.
   */
    private void saveField(SignalField fieldToSave, String newValue) throws java.sql.SQLException {
        String fieldID = fieldToSave.getType().getID();
        Signal signal = fieldToSave.getSignal();
        if (fieldToSave.isInDatabase()) {
            if (updateFieldStatement == null) {
                StringBuffer query = new StringBuffer("UPDATE ");
                query.append(Main.SCHEMA);
                query.append(".sgnl_fld SET fld_val = ? WHERE sgnl_id = ? AND fld_id = ? AND rec_type_id = ?");
                updateFieldStatement = oracleConnection.prepareStatement(query.toString());
            }
            updateFieldStatement.setString(1, newValue);
            updateFieldStatement.setString(2, signal.getID());
            updateFieldStatement.setString(3, fieldID);
            updateFieldStatement.setString(4, signal.getType().getRecordType().getID());
            updateFieldStatement.execute();
        } else {
            if (insertFieldStatement == null) {
                StringBuffer query = new StringBuffer("INSERT INTO ");
                query.append(Main.SCHEMA);
                query.append(".sgnl_fld (sgnl_id, fld_id, rec_type_id, fld_val) VALUES (?, ?, ?, ?)");
                insertFieldStatement = oracleConnection.prepareStatement(query.toString());
            }
            insertFieldStatement.setString(1, signal.getID());
            insertFieldStatement.setString(2, fieldID);
            insertFieldStatement.setString(3, signal.getType().getRecordType().getID());
            insertFieldStatement.setString(4, newValue);
            insertFieldStatement.execute();
            fieldToSave.setInDatabase(true);
        }
        fieldToSave.setValue(newValue);
    }

    /**
   * Cancels the pending changes for the model.
   * 
   * @throws java.sql.SQLException Thrown on SQL error.
   */
    public void cancelChanges() throws java.sql.SQLException {
        try {
            setProgressIndeterminate(true);
            setMessage("Canceling Pending Changes...");
            StringBuffer sql = new StringBuffer("SELECT FLD_ID, FLD_VAL FROM ");
            sql.append(Main.SCHEMA);
            sql.append(".SGNL_FLD, ");
            sql.append(Main.SCHEMA);
            sql.append(".SGNL_REC WHERE SGNL_FLD.SGNL_ID = SGNL_REC.SGNL_ID AND SGNL_FLD.REC_TYPE_ID = SGNL_REC.REC_TYPE_ID AND SGNL_FLD.SGNL_ID = ? AND FLD_ID = ?");
            PreparedStatement query = oracleConnection.prepareStatement(sql.toString());
            try {
                ArrayList fieldsChanged = getFieldsChanged();
                setProgressMaximum(fieldsChanged.size());
                int progress = 0;
                while (fieldsChanged.size() > 0) {
                    SignalField currentField = (SignalField) fieldsChanged.get(0);
                    query.setString(1, currentField.getSignal().getID());
                    query.setString(2, currentField.getType().getID());
                    ResultSet result = query.executeQuery();
                    try {
                        if (result.next()) currentField.setValue(result.getString("FLD_VAL")); else currentField.setValue(null);
                    } finally {
                        result.close();
                    }
                    fieldsChanged.remove(0);
                    setProgressValue(++progress);
                }
            } finally {
                query.close();
            }
        } finally {
            setMessage(" ");
            setProgressValue(0);
            setProgressIndeterminate(false);
        }
    }

    /**
   * Returns the <CODE>ArrayList</CODE> used to hold the chnged fields.
   * 
   * @return The fields changed.
   */
    public ArrayList getFieldsChanged() {
        return fieldsChanged;
    }

    /**
   * Duplicates the instances of <CODE>Signal</CODE> at the given row indexes.
   * 
   * @param signalsToDuplicate The instances of <CODE>Signal</CODE> to duplicate.
   * @param duplicateCount The number of duplicates of each row to create.
   * @return The indices of the rows added.
   * @throws java.sql.SQLException Thrown on sql error.
   */
    public int[] duplicate(Signal[] signalsToDuplicate, int duplicateCount) throws java.sql.SQLException {
        StringBuffer query = new StringBuffer("SELECT * FROM ");
        query.append(Main.SCHEMA);
        query.append(".sgnl_rec WHERE sgnl_id = ?");
        PreparedStatement signalDetailsStatement = oracleConnection.prepareStatement(query.toString());
        try {
            signalDetailsStatement.setFetchSize(fetchSize);
            query = new StringBuffer("SELECT * FROM ");
            query.append(Main.SCHEMA);
            query.append(".sgnl_fld WHERE sgnl_id = ? AND rec_type_id = ?");
            PreparedStatement fieldsBySignalStatement = oracleConnection.prepareStatement(query.toString());
            try {
                fieldsBySignalStatement.setFetchSize(fetchSize);
                for (int i = 0; i < signalsToDuplicate.length; i++) {
                    String currentSignalID = signalsToDuplicate[i].getID();
                    signalDetailsStatement.setString(1, currentSignalID);
                    ResultSet signalData = signalDetailsStatement.executeQuery();
                    try {
                        if (signalData.next()) {
                            String value = signalData.getString("dvc_id");
                            signalsToDuplicate[i].setDevice(new Device(value));
                            value = signalData.getString("sgnl_id_alias");
                            signalsToDuplicate[i].setIDAlias(value);
                            EpicsGroup currentGroup = new EpicsGroup();
                            currentGroup.setID(signalData.getString("epics_grp_id"));
                            signalsToDuplicate[i].setGroup(currentGroup);
                            SignalType currentType = signalsToDuplicate[i].getType();
                            value = signalData.getString("sgnl_nm");
                            currentType.setName(value);
                            value = signalData.getString("bulk_ind");
                            currentType.setBulk(value);
                            value = signalData.getString("mchn_prot_ind");
                            currentType.setMachineProtection(value);
                            value = signalData.getString("multimode_ind");
                            currentType.setMultimode(value);
                            value = signalData.getString("ext_src");
                            signalsToDuplicate[i].setExternalSource(value);
                            value = signalData.getString("invalid_id_ind");
                            signalsToDuplicate[i].setInvalidID(value);
                        }
                    } finally {
                        signalData.close();
                    }
                    fieldsBySignalStatement.setString(1, currentSignalID);
                    fieldsBySignalStatement.setString(2, signalsToDuplicate[i].getType().getRecordType().getID());
                    ResultSet signalFields = fieldsBySignalStatement.executeQuery();
                    try {
                        while (signalFields.next()) {
                            String newFieldID = signalFields.getString("fld_id");
                            SignalField currentField = signalsToDuplicate[i].getField(newFieldID);
                            if (currentField == null) {
                                String value = signalFields.getString("fld_val");
                                currentField = new SignalField(value);
                                EpicsRecordType recordType = new EpicsRecordType(signalFields.getString("rec_type_id"));
                                SignalFieldType currentType = new SignalFieldType(newFieldID, recordType);
                                currentField.setType(currentType);
                                signalsToDuplicate[i].addField(currentField);
                            }
                        }
                    } finally {
                        signalFields.close();
                    }
                }
            } finally {
                fieldsBySignalStatement.close();
            }
        } finally {
            signalDetailsStatement.close();
        }
        int[] rowsAdded = new int[duplicateCount * signalsToDuplicate.length];
        int rowsAddedIndex = 0;
        int rowCount = getRowCount();
        for (int i = 0; i < duplicateCount; i++) for (int j = 0; j < signalsToDuplicate.length; j++) {
            Signal currentSignal = (Signal) signalsToDuplicate[(j)].clone();
            currentSignal.setInDatabase(false);
            currentSignal.setFieldsInDatabase(false);
            addSignal(currentSignal);
            rowsAdded[rowsAddedIndex++] = rowCount++;
        }
        return rowsAdded;
    }

    /**
   * Finds the engineer responsible for the <CODE>Signal</CODE> at the given 
   * index. This is determined by looking up the system and subsystem associated 
   * with the <CODE>Signal</CODE> and returns the engineer assigned to that 
   * system. If the engineer can not be determined, <CODE>null</CODE> is 
   * returned.
   * 
   * @param signalIndex The index of the <CODE>Signal</CODE> of which to return the responsible engineer.
   * @return The name of the engineer responsibel for the <CODE>Signal</CODE>.
   * @throws java.sql.SQLException Thrown on sql error.
   */
    public String findResponsibleEngineer(int signalIndex) throws java.sql.SQLException {
        String engineer;
        Statement query = oracleConnection.createStatement();
        try {
            Signal selectedSignal = getSignalAt(signalIndex);
            StringBuffer sql = new StringBuffer("SELECT FIRST_NM, LAST_NAME FROM EQUIP.EMPLOYEE_V A, ");
            sql.append(Main.SCHEMA);
            sql.append(".SGNL_REC B, ");
            sql.append(Main.SCHEMA);
            sql.append(".DVC C, ");
            sql.append(Main.SCHEMA);
            sql.append(".RESP_ENGR D WHERE B.SGNL_ID = '");
            sql.append(selectedSignal.getID());
            sql.append("' AND B.DVC_ID = C.DVC_ID AND C.SUBSYS_ID = D.SUBSYS_ID AND D.BN = A.BN");
            ResultSet result = query.executeQuery(sql.toString());
            try {
                if (result.next()) engineer = result.getString("FIRST_NM") + " " + result.getString("LAST_NAME"); else engineer = null;
            } finally {
                result.close();
            }
        } finally {
            query.close();
        }
        return engineer;
    }

    /**
   * Loads the Field types from the database that apply to the data.
   * 
   * @return The names of the fileds that apply to the instances of <CODE>Signal</CODE> in the model.
   * @throws java.sql.SQLException Thrown on sql error.
   */
    public ArrayList loadApplicableColumns() throws java.sql.SQLException {
        ArrayList applicableColumns = new ArrayList();
        Statement query = oracleConnection.createStatement();
        try {
            ArrayList recordTypes = getRecordTypes();
            int recordTypeCount = recordTypes.size();
            if (recordTypeCount > 0) {
                StringBuffer sql = new StringBuffer("SELECT DISTINCT fld_id, fld_prmt_grp FROM ");
                sql.append(Main.SCHEMA);
                sql.append(".sgnl_fld_def WHERE rec_type_id IN (");
                for (int i = 0; i < recordTypeCount; i++) {
                    if (i > 0) sql.append(", ");
                    sql.append("'");
                    sql.append(recordTypes.get(i));
                    sql.append("'");
                }
                sql.append(") ORDER BY fld_prmt_grp");
                ResultSet result = query.executeQuery(sql.toString());
                try {
                    while (result.next()) {
                        SignalFieldType currentFieldType = new SignalFieldType(result.getString("fld_id"));
                        currentFieldType.setPromptGroup(result.getString("fld_prmt_grp"));
                        applicableColumns.add(currentFieldType);
                    }
                } finally {
                    result.close();
                }
            }
        } finally {
            query.close();
        }
        return applicableColumns;
    }
}
