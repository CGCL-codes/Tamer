package com.pallas.unicore.client.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Date;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import org.unicore.resources.USpace;
import com.pallas.unicore.client.explorer.SelectorDialog;
import com.pallas.unicore.client.remotefilechooser.RemoteFile;
import com.pallas.unicore.client.tables.AdjustTable;
import com.pallas.unicore.client.tables.ComboBoxCellEditor;
import com.pallas.unicore.client.tables.ComboBoxCellRenderer;
import com.pallas.unicore.client.tables.FilePermission;
import com.pallas.unicore.client.tables.FilePermissionCellEditor;
import com.pallas.unicore.client.tables.FilePermissionPanel;
import com.pallas.unicore.container.TaskContainer;
import com.pallas.unicore.extensions.FileOperation;
import com.pallas.unicore.extensions.FileStorage;
import com.pallas.unicore.extensions.FileSystemOperation;
import com.pallas.unicore.extensions.NamedResourceSet;
import com.pallas.unicore.resourcemanager.ResourceManager;
import com.pallas.unicore.utility.UserMessages;

/**
 * Panel to define a file operations in a certain file space (Uspace or Xspace)
 * 
 * @author Ralf Ratering
 * @author Klaus-Dieter Oertel
 * @version $Id: FileOperationPanel.java,v 1.1 2004/05/25 14:58:47 rmenday Exp $
 */
public class FileOperationPanel extends FileStoragePanel {

    static final int COL_OPERATION = 0;

    static final int COL_STORAGE = 1;

    static final int COL_SOURCE = 2;

    static final int COL_TARGET = 3;

    private JComboBox operationComboBox;

    private Vector operations;

    private FilePermissionCellEditor permissionsEditor;

    private FilePermissionPanel permissionsRenderer;

    private JComboBox storagesEditorComboBox;

    private ComboBoxCellRenderer storagesRendererComboBox;

    private DefaultCellEditor stringEditor;

    private FileOperationTableCellRenderer stringRenderer;

    /**
	 * Constructor
	 * 
	 * @param container
	 *            to be modified
	 */
    public FileOperationPanel(TaskContainer container) {
        this(container, null);
    }

    public FileOperationPanel(TaskContainer container, String initialDirectory) {
        super(container, initialDirectory);
        operations = new Vector();
        String[] ops = FileSystemOperation.getOperations();
        for (int i = 0; i < ops.length; i++) {
            operations.add(ops[i]);
        }
        ComboBoxListener comboBoxListener = new ComboBoxListener();
        operationComboBox = new JComboBox(operations);
        operationComboBox.addItemListener(comboBoxListener);
        table.getColumnModel().getColumn(COL_OPERATION).setCellEditor(new ComboBoxCellEditor(operationComboBox));
        table.getColumnModel().getColumn(COL_OPERATION).setCellRenderer(new ComboBoxCellRenderer(operations));
        stringRenderer = new FileOperationTableCellRenderer();
        stringEditor = new DefaultCellEditor(new JTextField());
        permissionsRenderer = new FilePermissionPanel();
        permissionsEditor = new FilePermissionCellEditor(new FilePermissionPanel());
        buildComponents();
    }

    /**
	 * Add an empty row at the table end
	 */
    protected void addEmptyRow() {
        Vector lastRowData = null;
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            lastRowData = (Vector) tableModel.getDataVector().elementAt(selectedRow);
        }
        Vector rowData = new Vector();
        if (lastRowData == null) {
            USpace uspace;
            if (container.getVsite() != null) {
                uspace = ResourceManager.getResourceSet(container.getVsite()).getUspace();
            } else {
                uspace = new USpace();
            }
            rowData.add(FileSystemOperation.COPY);
            rowData.add(uspace);
            rowData.add("");
            rowData.add("");
        } else {
            rowData.add(lastRowData.elementAt(COL_OPERATION));
            rowData.add(lastRowData.elementAt(COL_STORAGE));
            rowData.add("");
            if (FileSystemOperation.CHMOD.equals(rowData.elementAt(0))) {
                FilePermission permission = (FilePermission) lastRowData.elementAt(COL_TARGET);
                rowData.add(permission.clone());
            } else {
                rowData.add("");
            }
        }
        tableModel.addRow(rowData);
        selectedRow = table.getRowCount() - 1;
        table.setRowSelectionInterval(selectedRow, selectedRow);
    }

    /**
	 * Apply gui values to container
	 */
    public void applyValues() {
        if (table.getCellEditor() != null) {
            table.getCellEditor().stopCellEditing();
        }
        int nrOfRows = table.getRowCount();
        FileSystemOperation[] fileOps = new FileSystemOperation[nrOfRows];
        for (int i = 0; i < nrOfRows; i++) {
            fileOps[i] = (FileSystemOperation) getFileOperationAt(i);
        }
        updateModifiedTime(fileOps);
        container.setFileSystemOperations(fileOps);
        container.checkContents();
    }

    private void buildComponents() {
        int operationChoice = 135;
        table.getColumnModel().getColumn(COL_OPERATION).setPreferredWidth(operationChoice);
        int storageChoice = 90;
        table.getColumnModel().getColumn(COL_STORAGE).setPreferredWidth(storageChoice);
        storages = new Vector();
        storages.add(FileStorage.NSPACE_STRING);
        storagesEditorComboBox = new JComboBox(storages);
        storagesRendererComboBox = new ComboBoxCellRenderer(storages);
        table.getColumnModel().getColumn(COL_STORAGE).setCellEditor(new DefaultCellEditor(storagesEditorComboBox));
        table.getColumnModel().getColumn(COL_STORAGE).setCellRenderer(storagesRendererComboBox);
    }

    protected AdjustTable createTable() {
        return new FileOperationTable(tableModel);
    }

    protected FileTableModel createTableModel() {
        columnNames = getColumnNames();
        return new FileOperationTableModel(columnNames, 0);
    }

    /**
	 * Gets the columnNames
	 */
    protected String[] getColumnNames() {
        String[] columnNames = { "Operation", "File System", "Filename", "Second Parameter" };
        return columnNames;
    }

    /**
	 * Gets the dataFlavor
	 */
    protected DataFlavor getDataFlavor() {
        return FileSystemOperation.getFlavor();
    }

    /**
	 * Get file operation from a certain row
	 * 
	 * @param row
	 *            index of row
	 * @return file operation at selected row
	 */
    protected FileOperation getFileOperationAt(int row) {
        String operation = (String) tableModel.getValueAt(row, COL_OPERATION);
        Object storageItem = tableModel.getValueAt(row, COL_STORAGE);
        if (storageItem.equals(res.getString("NONE"))) {
            storageItem = null;
        }
        String sourceName = (String) tableModel.getValueAt(row, COL_SOURCE);
        String destinationName = tableModel.getValueAt(row, COL_TARGET).toString();
        return new FileSystemOperation(sourceName, operation, destinationName, storageItem);
    }

    /**
	 * Reset current GUI entries to last container contents.
	 */
    public void resetValues() {
        if (container.getIdentifier() == null) {
            return;
        }
        if (table.getCellEditor() != null) {
            table.getCellEditor().stopCellEditing();
        }
        clearTable();
        FileSystemOperation[] fileOps = container.getFileSystemOperations();
        for (int i = 0; i < fileOps.length; i++) {
            addFileOperation(fileOps[i]);
        }
    }

    /**
	 * Fill an existing table row with a certain FileExport object. If row index
	 * is too high add a new row and use that one.
	 * 
	 * @param index
	 *            nr of table row
	 * @param fileOp
	 *            The new fileOperation
	 */
    protected void setFileOperationAt(FileOperation fileOp, int index) {
        FileSystemOperation fileSysOp = (FileSystemOperation) fileOp;
        String operation = fileSysOp.getOperation();
        Object target = fileSysOp.getDestinationName();
        if (index < table.getRowCount()) {
            String oldSource = (String) tableModel.getValueAt(index, COL_SOURCE);
            if (!oldSource.equals("")) {
                operation = (String) tableModel.getValueAt(index, COL_OPERATION);
                target = tableModel.getValueAt(index, COL_TARGET);
            }
        }
        if (index > table.getRowCount() - 1) {
            tableModel.addRow(new Vector());
        }
        tableModel.setValueAt(operation, index, COL_OPERATION);
        tableModel.setValueAt(fileSysOp.getStorageItem(), index, COL_STORAGE);
        tableModel.setValueAt(fileSysOp.getSourceName(), index, COL_SOURCE);
        if (operation.equals(FileSystemOperation.CHMOD) && !(target instanceof FilePermission)) {
            FilePermission permission = new FilePermission();
            permission.setPermission((String) target);
            tableModel.setValueAt(permission, index, COL_TARGET);
        } else {
            tableModel.setValueAt(target, index, COL_TARGET);
        }
    }

    protected void showBrowser() {
        if (container.getVsite() == null) {
            UserMessages.info("Please select a virtual site first.");
            return;
        }
        if (!ResourceManager.isVsiteAvailable(container.getVsite())) {
            UserMessages.info("Virtual site " + container.getVsite() + " is not available.");
            return;
        }
        SelectorDialog chooser = ResourceManager.getSelectorDialog(null, container.getVsite());
        chooser.setTitle("UNICORE: Choose source for file operation");
        chooser.setLocalEnabled(false);
        int result = chooser.showDialog();
        if (result == JFileChooser.APPROVE_OPTION) {
            File[] files = { chooser.getSelectedFile() };
            for (int i = 0; i < files.length; i++) {
                FileSystemOperation fileOp = null;
                if (files[i] instanceof RemoteFile) {
                    RemoteFile file = (RemoteFile) files[i];
                    fileOp = new FileSystemOperation(file.getSubspacePath(), FileSystemOperation.COPY, file.getSubspacePath(), file.getStorage());
                }
                if (table.getSelectedRowCount() == 1 && files.length == 1) {
                    setFileOperationAt(fileOp, table.getSelectedRow());
                } else {
                    addFileOperation(fileOp);
                }
            }
        }
    }

    private void updateModifiedTime(FileSystemOperation[] newOps) {
        FileSystemOperation[] oldOps = container.getFileSystemOperations();
        if (oldOps.length != newOps.length) {
            container.setModifiedTime(new Date());
            return;
        }
        for (int i = 0; i < oldOps.length; i++) {
            boolean found = false;
            for (int j = 0; j < newOps.length; j++) {
                if (newOps[j].equals(oldOps[i])) {
                    found = true;
                }
            }
            if (!found) {
                container.setModifiedTime(new Date());
                return;
            }
        }
    }

    /**
	 * Get current storage resources from ResourceManager
	 */
    public void updateStorageResources() {
        storages = new Vector();
        NamedResourceSet resourceSet = ResourceManager.getResourceSet(container.getVsite());
        resourceSet.addStorageResources(storages);
        if (storages.size() == 0) {
            return;
        }
        Vector cloned = (Vector) storages.clone();
        for (int i = 0; i < cloned.size(); i++) {
            if (cloned.elementAt(i) instanceof org.unicore.resources.Spool || cloned.elementAt(i) instanceof org.unicore.resources.AlternativeUspace) {
                storages.remove(cloned.elementAt(i));
            }
        }
        cloned.clear();
    }

    /**
	 * Update storage resources
	 */
    public void updateValues() {
        updateStorageResources();
        replaceStorageResources(COL_STORAGE);
        storagesEditorComboBox.setModel(new DefaultComboBoxModel(storages));
        storagesRendererComboBox.setModel(new DefaultComboBoxModel(storages));
    }

    private class ComboBoxListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                return;
            }
            if (e.getSource() == operationComboBox) {
                int row = table.getSelectedRow();
                if (e.getItem().equals(FileSystemOperation.CHMOD)) {
                    tableModel.setValueAt(new FilePermission(), row, COL_TARGET);
                } else if (e.getItem().equals(FileSystemOperation.DELETE) || e.getItem().equals(FileSystemOperation.MAKEDIR)) {
                    tableModel.setValueAt(new String(""), row, COL_TARGET);
                } else {
                    Object value = tableModel.getValueAt(row, COL_OPERATION);
                    if (FileSystemOperation.CHMOD.equals(value) || FileSystemOperation.DELETE.equals(value) || FileSystemOperation.MAKEDIR.equals(value)) {
                        tableModel.setValueAt(new String(""), row, COL_TARGET);
                    }
                }
                tableModel.fireTableRowsUpdated(row, row);
            }
        }
    }

    /**
	 * Table with cell dependent renderers/editors
	 */
    class FileOperationTable extends AdjustTable {

        public FileOperationTable(FileTableModel tableModel) {
            super((TableModel) tableModel);
        }

        public TableCellEditor getCellEditor(int row, int col) {
            if (convertColumnIndexToModel(col) == COL_TARGET) {
                if (tableModel.getValueAt(row, COL_OPERATION).equals(FileSystemOperation.CHMOD)) {
                    return permissionsEditor;
                } else {
                    return stringEditor;
                }
            } else {
                return super.getCellEditor(row, col);
            }
        }

        public TableCellRenderer getCellRenderer(int row, int col) {
            if (convertColumnIndexToModel(col) == COL_TARGET) {
                if (tableModel.getValueAt(row, COL_OPERATION).equals(FileSystemOperation.CHMOD)) {
                    return permissionsRenderer;
                } else {
                    return stringRenderer;
                }
            } else {
                return super.getCellRenderer(row, col);
            }
        }
    }

    private class FileOperationTableCellRenderer extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == COL_TARGET) {
                String selectedItem = (String) tableModel.getValueAt(row, COL_OPERATION);
                if (selectedItem.equals(FileSystemOperation.DELETE) || selectedItem.equals(FileSystemOperation.MAKEDIR)) {
                    component.setBackground(Color.LIGHT_GRAY);
                } else {
                    if (isSelected) {
                        component.setBackground(table.getSelectionBackground());
                    } else {
                        component.setBackground(Color.WHITE);
                    }
                }
            }
            return component;
        }
    }

    class FileOperationTableModel extends FileTableModel {

        public FileOperationTableModel(String[] columnNames, int numRows) {
            super(columnNames, numRows);
        }

        public boolean isCellEditable(int row, int col) {
            if (col != COL_TARGET) {
                return true;
            }
            String selectedItem = (String) tableModel.getValueAt(row, COL_OPERATION);
            if (selectedItem.equals(FileSystemOperation.DELETE) || selectedItem.equals(FileSystemOperation.MAKEDIR)) {
                return false;
            } else {
                return true;
            }
        }
    }
}
