package org.xaware.ide.xadev.bizview.publish;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.xaware.ide.xadev.XA_Designer_Plugin;
import org.xaware.ide.xadev.datamodel.DefaultTableContentProvider;
import org.xaware.ide.xadev.datamodel.SpecializedTableContentProvider;
import org.xaware.ide.xadev.datamodel.TableDataList;
import org.xaware.ide.xadev.datamodel.editor.BooleanEditor;
import org.xaware.ide.xadev.datamodel.editor.ComboEditor;
import org.xaware.ide.xadev.datamodel.editor.StringEditor;
import org.xaware.ide.xadev.datamodel.editor.TableCellEditor;
import org.xaware.ide.xadev.gui.ChangeEvent;
import org.xaware.ide.xadev.gui.ChangeListener;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * Table model for Publish File table
 *
 * @author Saritha
 * @version 1.0
 */
public class PublishTableContentProvider extends DefaultTableContentProvider implements SpecializedTableContentProvider, ChangeListener {

    /** Class level Logger */
    private final XAwareLogger logger = XAwareLogger.getXAwareLogger(PublishTableContentProvider.class.getName());

    /** table to which the Table model has to be applied */
    private Table table;

    /** Vector of column names for the table */
    private Vector columnNames;

    /** array of table columns */
    private TableColumn[] tableColumn;

    /** Number of columns */
    private final int numColumns;

    /**
     * Creates a new AttributeJTableModel object.
     *
     * @param table Table
     * @param attributes list of attributes to be inserted in table
     * @param inColumnNames Column names
     */
    public PublishTableContentProvider(final Table table, final List attributes, final Vector inColumnNames) {
        super(table);
        this.table = table;
        setColumnNames(inColumnNames);
        numColumns = inColumnNames.size();
        createTableViewer();
        tableViewer.setContentProvider(this);
        tableViewer.setLabelProvider(this);
        tableDataList = new TableDataList();
        tableViewer.setInput(tableDataList);
        createColoums();
        if (attributes != null) {
            Display.getCurrent().asyncExec(new Runnable() {

                public void run() {
                    try {
                        XA_Designer_Plugin.makeBusy(XA_Designer_Plugin.getShell(), "Publish");
                        for (int i = 0; i < attributes.size(); i++) {
                            addSpecializedObject(attributes.get(i));
                        }
                    } finally {
                        XA_Designer_Plugin.makeUnBusy();
                    }
                }
            });
        }
        this.addChangeListener(this);
    }

    /**
     * Creates a new PublishTableContentProvider object.
     *
     * @param table Table
     * @param attributes list of attributes to be inserted in table
     * @param inColumnNames Column names
     * @param publishShell shell instance.
     */
    public PublishTableContentProvider(final Table table, final List attributes, final Vector inColumnNames, final Shell publishShell) {
        super(table);
        this.table = table;
        setColumnNames(inColumnNames);
        numColumns = inColumnNames.size();
        createTableViewer();
        tableViewer.setContentProvider(this);
        tableViewer.setLabelProvider(this);
        tableDataList = new TableDataList();
        tableViewer.setInput(tableDataList);
        createColoums();
        if (attributes != null) {
            Display.getCurrent().asyncExec(new Runnable() {

                public void run() {
                    try {
                        XA_Designer_Plugin.makeBusy(publishShell, "Publish");
                        for (int i = 0; i < attributes.size(); i++) {
                            addSpecializedObject(attributes.get(i));
                        }
                    } finally {
                        XA_Designer_Plugin.makeUnBusy();
                    }
                }
            });
        }
        this.addChangeListener(this);
    }

    /**
     * TableViewer creation
     */
    private void createTableViewer() {
        tableViewer = new TableViewer(table);
        tableViewer.setUseHashlookup(false);
        if (columnNames != null) {
            final String[] colNames = new String[columnNames.size()];
            for (int i = 0; i < columnNames.size(); i++) {
                colNames[i] = (String) columnNames.get(i);
            }
            tableViewer.setColumnProperties(colNames);
        }
    }

    /**
     * Creates a TableEditor for the Table.
     */
    private void createColoums() {
        tableColumn = new TableColumn[numColumns];
        for (int i = 0; i < numColumns; i++) {
            tableColumn[i] = new TableColumn(table, SWT.NONE);
            if (i == 0) {
                tableColumn[i].setData("208");
            } else {
                tableColumn[i].setData("75");
            }
            tableColumn[i].setText((String) columnNames.get(i));
            tableColumn[i].setMoveable(true);
        }
    }

    /**
     * returns the no. of columns
     *
     * @return no. of columns
     */
    public int getColumnCount() {
        return numColumns;
    }

    /**
     * returns the column name at the specific index
     *
     * @param columnIndex index
     *
     * @return column name
     */
    public String getColumnName(final int columnIndex) {
        return (String) columnNames.get(columnIndex);
    }

    /**
     * returns the no. of rows
     *
     * @return no. of rows
     */
    public int getRowCount() {
        return table.getItemCount();
    }

    /**
     * Specifies which cells are editable
     *
     * @param rowIndex index of the row
     * @param columnIndex index of the column
     *
     * @return true if editable, false otherwise
     */
    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        if (columnIndex == 6) {
            return true;
        }
        final boolean flag = new Boolean((String) getValueAt(rowIndex, 6)).booleanValue();
        if (flag) {
            return (columnIndex != 0) && (columnIndex != 3);
        } else {
            return false;
        }
    }

    /**
     * Gets the value at the specified location of the table
     *
     * @param rowIndex Index of the row
     * @param columnIndex Index of the column
     *
     * @return value at the specified row and column
     */
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        final TableItem item = table.getItem(rowIndex);
        final PublishTableData rowData = (PublishTableData) item.getData();
        String result = "";
        switch(columnIndex) {
            case 0:
                result = rowData.getFileAbsoluteName();
                break;
            case 1:
                result = rowData.getFileName();
                break;
            case 2:
                result = rowData.getFileDescription();
                break;
            case 3:
                result = rowData.getDocType();
                break;
            case 4:
                result = rowData.getRoles();
                break;
            case 5:
                result = rowData.isOverWrite().toString();
                break;
            case 6:
                result = rowData.isPublish().toString();
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * Sets the value at the specified location of the table
     *
     * @param aValue Value to be set
     * @param rowIndex Index of the row
     * @param columnIndex Index of the column
     */
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
        try {
            final TableItem item = table.getItem(rowIndex);
            final PublishTableData rowData = (PublishTableData) item.getData();
            switch(columnIndex) {
                case 0:
                    rowData.setFileAbsoluteName((String) aValue);
                    break;
                case 1:
                    rowData.setFileName((String) aValue);
                    break;
                case 2:
                    rowData.setFileDescription((String) aValue);
                    break;
                case 3:
                    rowData.setDocType((String) aValue);
                    break;
                case 4:
                    rowData.setRoles((String) aValue);
                    break;
                case 5:
                    rowData.setOverWrite(new Boolean((String) aValue));
                    break;
                case 6:
                    rowData.setPublish(new Boolean((String) aValue));
                    break;
                default:
                    break;
            }
        } catch (final ClassCastException cce) {
            logger.fine("ClassCastException setting value for PublishTableDataJTableModel : " + cce);
        }
    }

    /**
     * Update the view to reflect the fact that a Attribute was added  to the
     * task list
     *
     * @param inAtt Attribute to be added
     */
    public void addSpecializedObject(final Object inAtt) {
        tableDataList.addTask(inAtt);
    }

    /**
     * Update the view to reflect the fact that one of the tasks was modified
     *
     * @param inObj object to be replaced
     */
    public void replaceSpecializedObject(final Object inObj) {
        final PublishTableData task = (PublishTableData) ((IStructuredSelection) tableViewer.getSelection()).getFirstElement();
        if (inObj != null) {
            tableDataList.updateTask(task, inObj, true);
        }
        tableViewer.update(task, null);
    }

    /**
     * Gets the attribute at the specified row
     *
     * @param row index of the table row
     *
     * @return Attribute object
     */
    public Object getSpecializedObject(final int row) {
        final PublishTableData att = (PublishTableData) tableDataList.getTask(row);
        return att;
    }

    /**
     * removes the specified row
     *
     * @param row index of the row to be removed
     */
    public void removeSpecializedObject(final int row) {
        final PublishTableData attribute = (PublishTableData) tableDataList.getTask(row);
        if (attribute != null) {
            tableDataList.removeTask(row, attribute);
        }
    }

    /**
     * removes the selected row from the table
     */
    public void removeSpecializedObject() {
        final PublishTableData task = (PublishTableData) ((IStructuredSelection) tableViewer.getSelection()).getFirstElement();
        final int index = tableDataList.getIndex(task);
        removeSpecializedObject(index);
    }

    /**
     * returns the list of all Attribute objects in table
     *
     * @return Vector of Attributes
     */
    public Vector getSpecializedObjects() {
        final Vector attributeList = tableDataList.getTasks();
        return attributeList;
    }

    /**
     * Checks if the passed Attribute is present in the table
     *
     * @param inObj Attribute object to be checked
     *
     * @return true if object is found, else false
     */
    public boolean contains(final Object inObj) {
        final PublishTableData inAtt = (PublishTableData) inObj;
        final Vector attributeList = tableDataList.getTasks();
        final Iterator itr = attributeList.iterator();
        while (itr.hasNext()) {
            final PublishTableData cur = (PublishTableData) itr.next();
            if (cur.getFileAbsoluteName().equals(inAtt.getFileAbsoluteName())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Sets the names of the columns from the vector with the specified width
     * for all columns
     *
     * @param inColNames holds the column names of the table
     */
    public void setColumnNames(final Vector inColNames) {
        this.columnNames = inColNames;
    }

    /**
     * returns the list of column names as a list
     *
     * @return column names as List
     */
    public java.util.List getColumnNames() {
        final String[] colNames = new String[columnNames.size()];
        for (int i = 0; i < columnNames.size(); i++) {
            colNames[i] = (String) columnNames.get(i);
        }
        return Arrays.asList(colNames);
    }

    /**
     * returns the selection
     *
     * @return selection on the table
     */
    public ISelection getSelection() {
        return tableViewer.getSelection();
    }

    /**
     * Return the TableDataLIst
     *
     * @return the TableDataList
     */
    public TableDataList getTableDataList() {
        return tableDataList;
    }

    /**
     * Return the Attributes as an array of Objects Overrides
     * getElements(Object) method of IStructuredContentProvider
     *
     * @param inputElement Attribute
     *
     * @return list of all Attributes
     */
    public Object[] getElements(final Object inputElement) {
        return tableDataList.getTasks().toArray();
    }

    /**
     * Disposes the table Overrides dispose() method of
     * IStructuredContentProvider
     */
    @Override
    public void dispose() {
        tableDataList.removeChangeListener(this);
        super.dispose();
    }

    /**
     * Handles the change of values in the table Overrides
     * inputChanged(Viewer,Object,Object) method of IStructuredContentProvider
     *
     * @param viewer tableviewer
     * @param oldInput the old value in the table cell
     * @param newInput the new value which will be entered in the cell
     */
    public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
        if (newInput != null) {
            ((TableDataList) newInput).addChangeListener(this);
        }
        if (oldInput != null) {
            ((TableDataList) oldInput).removeChangeListener(this);
        }
    }

    /**
     * overrides
     * org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object,
     * int)
     *
     * @param element the object representing the entire row, or null
     *        indicating that no input object is set in the viewer
     * @param columnIndex the zero-based index of the column in which the label
     *        appears
     *
     * @return Returns the label image for the given column of the given
     *         element.
     */
    public Image getColumnImage(final Object element, final int columnIndex) {
        return null;
    }

    /**
     * overrides
     * org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object,
     * int)
     *
     * @param element the object representing the entire row, or null
     *        indicating that no input object is set in the viewer
     * @param columnIndex the zero-based index of the column in which the label
     *        appears
     *
     * @return Returns the label text for the given column of the given
     *         element.
     */
    public String getColumnText(final Object element, final int columnIndex) {
        String result = "";
        final PublishTableData rowData = (PublishTableData) element;
        switch(columnIndex) {
            case 0:
                result = rowData.getFileAbsoluteName();
                break;
            case 1:
                result = rowData.getFileName();
                break;
            case 2:
                result = rowData.getFileDescription();
                break;
            case 3:
                result = rowData.getDocType();
                break;
            case 4:
                result = rowData.getRoles();
                break;
            case 5:
                result = rowData.isOverWrite().toString();
                break;
            case 6:
                result = rowData.isPublish().toString();
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * Disposes all the table components
     */
    public void disposeModel() {
        for (int i = 0; i < numColumns; i++) {
            tableColumn[i].dispose();
        }
        dispose();
    }

    /**
     * (non-Javadoc)
     *
     * @see org.xaware.ide.xadev.datamodel.DefaultTableContentProvider#updateModel(int,
     *      int, java.lang.String)
     */
    @Override
    public void updateModel(final int row, final int columnIndex, final String newText) {
        final PublishTableData rowData = (PublishTableData) tableDataList.getTask(row);
        try {
            switch(columnIndex) {
                case 0:
                    rowData.setFileAbsoluteName(newText);
                    break;
                case 1:
                    rowData.setFileName(newText);
                    break;
                case 2:
                    rowData.setFileDescription(newText);
                    break;
                case 3:
                    rowData.setDocType(newText);
                    break;
                case 4:
                    rowData.setRoles(newText);
                    break;
                case 5:
                    rowData.setOverWrite(new Boolean(newText));
                    break;
                case 6:
                    rowData.setPublish(new Boolean(newText));
                    break;
                default:
                    break;
            }
        } catch (final ClassCastException cce) {
            logger.fine("ClassCastException setting value for PublishTableDataJTableModel : " + cce);
        }
        tableDataList.updateTask(rowData, false);
    }

    /**
     * Default column editor class.
     *
     * @return Class instance.
     */
    protected Vector getDataVector() {
        final Iterator itr = tableDataList.getTasks().iterator();
        final Vector tableData = new Vector();
        while (itr.hasNext()) {
            final Object obj = itr.next();
            if (obj instanceof PublishTableData) {
                final Vector rowData = new Vector();
                rowData.add(((PublishTableData) obj).getFileAbsoluteName());
                rowData.add(((PublishTableData) obj).getFileName());
                rowData.add(((PublishTableData) obj).getFileDescription());
                rowData.add(((PublishTableData) obj).getDocType());
                rowData.add(((PublishTableData) obj).getRoles());
                rowData.add(((PublishTableData) obj).isOverWrite());
                rowData.add(((PublishTableData) obj).isPublish());
                tableData.add(rowData);
            }
        }
        return tableData;
    }

    /**
     * Sets roles avilable on host server to Combobox model
     *
     * @param roles Vector
     */
    public void setAvialableRoles(final ArrayList roles) {
        setEditorData(PublishFileListModel.ROLE, roles.toArray());
    }

    /**
     * Default column render class.
     *
     * @param parent parent instance.
     * @param column column index.
     *
     * @return Class instance.
     */
    @Override
    protected TableCellEditor getColumnRender(final Composite parent, final int column) {
        switch(column) {
            case 0:
            case 3:
            default:
                return null;
            case 1:
            case 2:
                return new StringEditor(parent);
            case 4:
                return new ComboEditor(parent, null, true);
            case 5:
            case 6:
                return new BooleanEditor(parent);
        }
    }

    /**
     * Checks whether Columns can be editable or not
     *
     * @param e ChangeEvent
     */
    public void stateChanged(final ChangeEvent e) {
        try {
            isPublishSelected((PublishTableContentProvider) e.getSource());
        } catch (final Exception exp) {
            logger.info("Exception occured while updating tabledata");
            logger.printStackTrace(exp);
        }
    }

    /**
     * Cehcks whether Publish column is selected
     *
     * @param provider PublishTableContentProvider
     *
     * @return boolean
     */
    private boolean isPublishSelected(final PublishTableContentProvider provider) {
        final Vector dataVector = provider.getTableDataList().getTasks();
        for (int row = 0; row < dataVector.size(); row++) {
            final PublishTableData data = (PublishTableData) dataVector.get(row);
            boolean enable = false;
            if (data.isPublish().booleanValue()) {
                enable = true;
            }
            for (int col = 0; col < (numColumns - 1); col++) {
                final TableCellEditor editor = getCellRenderControl(row, col);
                if (editor == null) {
                    continue;
                }
                if (enable) {
                    if (!editor.isControlEnabled()) {
                        editor.setControlEnabled(true);
                    }
                } else {
                    if (editor.isControlEnabled()) {
                        editor.setControlEnabled(false);
                    }
                }
            }
        }
        return false;
    }

    /**
     * removes Object from table model
     *
     * @param inObj Object
     */
    public void removeSpecializedObject(final Object inObj) {
    }
}
