package org.xaware.ide.xadev.table.contentprovider;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.xaware.ide.xadev.table.editor.StringEditor;
import org.xaware.ide.xadev.table.editor.TableCellEditor;
import org.xaware.ide.xadev.tools.gui.packagetool.PackageTableData;
import org.xaware.ide.xadev.tools.gui.packagetool.PubFileListTableModel;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * ContenProvider for package contents table. Also creates tabel cell editors
 * 
 * @author Saritha
 * @version 1.0
 */
public class PackageTableContentProvider extends TableContentProvider {

    /** Class level Logger */
    private final XAwareLogger logger = XAwareLogger.getXAwareLogger(PackageTableContentProvider.class.getName());

    /**
     * Creates a new PackageTableContentProvider object.
     * 
     * @param table
     *            Table
     * @param attributes
     *            list of attributes to be inserted in table
     * @param inColumnNames
     *            Column names
     */
    public PackageTableContentProvider(final Composite parent, final int style, final boolean makeEqualWidth, final int widthCorrectionFactor, final List attributes, final List inColumnNames, final int[] columnWidths) {
        super(parent, style, makeEqualWidth, widthCorrectionFactor, inColumnNames, columnWidths);
        if (attributes != null) {
            addRows(attributes);
        }
    }

    /**
     * Specifies which cells are editable
     * 
     * @param rowIndex
     *            index of the row
     * @param columnIndex
     *            index of the column
     * 
     * @return true if editable, false otherwise
     */
    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        if (columnIndex == org.xaware.ide.xadev.tools.gui.packagetool.PubFileListTableModel.DESC) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the value at the specified location of the table
     * 
     * @param rowIndex
     *            Index of the row
     * @param columnIndex
     *            Index of the column
     * 
     * @return value at the specified row and column
     */
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        final TableItem item = table.getItem(rowIndex);
        final PackageTableData rowData = (PackageTableData) item.getData();
        String result = "";
        switch(columnIndex) {
            case 0:
                result = rowData.getFileName();
                break;
            case 1:
                result = rowData.getRelativePath();
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
            default:
                result = rowData.getFullPath();
                break;
        }
        return result;
    }

    /**
     * Sets the value at the specified location of the table
     * 
     * @param aValue
     *            Value to be set
     * @param rowIndex
     *            Index of the row
     * @param columnIndex
     *            Index of the column
     */
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
        try {
            final TableItem item = table.getItem(rowIndex);
            final PackageTableData rowData = (PackageTableData) item.getData();
            switch(columnIndex) {
                case 0:
                    rowData.setFileName((String) aValue);
                    break;
                case 1:
                    rowData.setRelativePath((String) aValue);
                    break;
                case 2:
                    rowData.setFileDescription((String) aValue);
                    break;
                case 3:
                    rowData.setDocType((String) aValue);
                    break;
                case 4:
                    rowData.setRoles((String) aValue);
                    updateModel(rowIndex, columnIndex, (String) aValue);
                    break;
                case 5:
                    rowData.setFullPath((String) aValue);
                    break;
                default:
                    break;
            }
        } catch (final ClassCastException cce) {
            logger.fine("ClassCastException setting value for PublishTableDataJTableModel : " + cce);
        }
    }

    /**
     * Checks if the passed Attribute is present in the table
     * 
     * @param inObj
     *            Attribute object to be checked
     * 
     * @return true if object is found, else false
     */
    @Override
    public boolean contains(final Object inObj) {
        final PackageTableData inAtt = (PackageTableData) inObj;
        final List attributeList = getRows();
        final Iterator itr = attributeList.iterator();
        while (itr.hasNext()) {
            final PackageTableData cur = (PackageTableData) itr.next();
            if (cur.getRelativePath().equals(inAtt.getRelativePath())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Returns the row number based on the file path given
     * 
     * @param fullName
     *            String
     * 
     * @return int
     */
    public int getRowByName(final String fullName) {
        int row = -1;
        final int count = getRowCount();
        for (int i = 0; i < count; i++) {
            final PackageTableData rowData = (PackageTableData) getRow(i);
            final String name = rowData.getFullPath();
            if (name.equals(fullName)) {
                row = i;
                break;
            }
        }
        return row;
    }

    /**
     * overrides org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
     * 
     * @param element
     *            the object representing the entire row, or null indicating that no input object is set in the viewer
     * @param columnIndex
     *            the zero-based index of the column in which the label appears
     * 
     * @return Returns the label text for the given column of the given element.
     */
    @Override
    public String getColumnText(final Object element, final int columnIndex) {
        String result = "";
        final PackageTableData rowData = (PackageTableData) element;
        switch(columnIndex) {
            case 0:
                result = rowData.getFileName();
                break;
            case 1:
                result = rowData.getRelativePath();
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
            default:
                break;
        }
        return result;
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.xaware.ide.xadev.table.contentprovider.TableContentProvider#updateModel(int, int, java.lang.String)
     */
    @Override
    public void updateModel(final int row, final int columnIndex, final String newText) {
        final PackageTableData rowData = (PackageTableData) getRow(row);
        try {
            switch(columnIndex) {
                case 0:
                    rowData.setFileName(newText);
                    break;
                case 1:
                    rowData.setRelativePath(newText);
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
                default:
                    break;
            }
        } catch (final ClassCastException cce) {
            logger.fine("ClassCastException setting value for PublishTableDataJTableModel : " + cce);
        }
        updateRow(row, rowData, false);
    }

    /**
     * Default column editor class.
     * 
     * @return Class instance.
     */
    public Vector getDataVector() {
        final Iterator itr = getRows().iterator();
        final Vector tableData = new Vector();
        while (itr.hasNext()) {
            final Object obj = itr.next();
            if (obj instanceof PackageTableData) {
                final Vector rowData = new Vector();
                rowData.add(((PackageTableData) obj).getFileName());
                rowData.add(((PackageTableData) obj).getRelativePath());
                rowData.add(((PackageTableData) obj).getFileDescription());
                rowData.add(((PackageTableData) obj).getDocType());
                rowData.add(((PackageTableData) obj).getRoles());
                tableData.add(rowData);
            }
        }
        return tableData;
    }

    /**
     * Sets roles available on host server to Combobox model
     * 
     * @param roles
     *            Vector
     */
    public void setAvialableRoles(final Vector roles) {
        setEditorData(PubFileListTableModel.ROLE, roles.toArray());
    }

    /**
     * Default column render class.
     * 
     * @param parent
     *            parent instance.
     * @param column
     *            column index.
     * 
     * @return Class instance.
     */
    @Override
    protected TableCellEditor getColumnRender(final Composite parent, final int column) {
        if (column == org.xaware.ide.xadev.tools.gui.packagetool.PubFileListTableModel.DESC) {
            return new StringEditor(parent);
        } else {
            return null;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.xaware.ide.xadev.table.contentprovider.TableContentProvider#findRow(java.lang.Object)
     */
    @Override
    public int findRow(final Object rowObject) {
        return -1;
    }
}
