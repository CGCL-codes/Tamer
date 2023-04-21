package org.xaware.ide.xadev.table.contentprovider;

import java.util.List;
import org.eclipse.swt.widgets.Composite;
import org.xaware.ide.xadev.datamodel.InputParameterData;
import org.xaware.ide.xadev.table.editor.BooleanEditor;
import org.xaware.ide.xadev.table.editor.TableCellEditor;

/**
 * Table model for Input Parameter Panel table
 * 
 * @author BlueAlly
 * @version 1.0
 */
public class InputParameterTableContentProvider extends TableContentProvider {

    /**
	 * editValueOnly holds true or false. If true then disables Name, Type,
	 * Default, Description controls and enables only Value and Path controls.
	 */
    private boolean editValueOnly;

    /**
	 * Creates a new InputParameterContentProviderTable object.
	 * 
	 * @param parent
	 *            Parent composite.
	 * @param style
	 *            Style bit for table.
	 * @param makeEqualWidth
	 *            Equal width for columns if value is true. If false, drags the
	 *            last column to fit the table
	 * @param widthCorrectionFactor
	 *            Table width correction factor, by default 4.
	 * @param inputParams
	 *            Input parameters list
	 * @param inEditValueOnly
	 *            holds true or false. If true then disables Name,Type, Default,
	 *            Description controls and enables only Value and Path controls.
	 * @param columnNames
	 *            Column names list.
	 * @param columnWidths
	 *            Array of Column widths.
	 */
    public InputParameterTableContentProvider(Composite parent, int style, boolean makeEqualWidth, int widthCorrectionFactor, List inputParams, boolean editValueOnly, List columnNames, int[] columnWidths) {
        super(parent, style, makeEqualWidth, widthCorrectionFactor, columnNames, columnWidths);
        this.editValueOnly = editValueOnly;
        if (inputParams != null) {
            addRows(inputParams);
        }
    }

    /**
	 * (non-Javadoc)
	 * 
	 * @see org.xaware.ide.xadev.table.contentprovider.TableContentProvider#isCellEditable(int,
	 *      int)
	 */
    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        boolean retVal = false;
        if (editValueOnly) {
            if (columnIndex == 5) {
                retVal = true;
            }
        }
        return retVal;
    }

    /**
	 * (non-Javadoc)
	 * 
	 * @see org.xaware.ide.xadev.table.contentprovider.TableContentProvider#getColumnText(java.lang.Object,
	 *      int)
	 */
    @Override
    public String getColumnText(final Object element, final int columnIndex) {
        String columnText = "";
        final InputParameterData data = ((InputParameterData) element);
        if (columnIndex == 0) {
            columnText = data.getName();
        } else if (columnIndex == 1) {
            columnText = data.getType();
        } else if (columnIndex == 2) {
            columnText = data.getDefault();
        } else if (columnIndex == 3) {
            columnText = data.getDescription();
        } else if (columnIndex == 4) {
            columnText = data.getRequired();
        } else if (columnIndex == 5) {
            columnText = data.getWorkingValue();
        }
        return columnText;
    }

    /**
	 * (non-Javadoc)
	 * 
	 * @see org.xaware.ide.xadev.table.contentprovider.TableContentProvider#contains(java.lang.Object)
	 */
    @Override
    public boolean contains(final Object object) {
        boolean retVal = false;
        final int result = getRows().indexOf(object);
        if (result >= 0) {
            retVal = true;
        }
        return retVal;
    }

    /**
	 * (non-Javadoc)
	 * 
	 * @see org.xaware.ide.xadev.table.contentprovider.TableContentProvider#updateModel(int,
	 *      int, java.lang.String)
	 */
    @Override
    public void updateModel(final int rowIndex, final int columnIndex, final String newValue) {
        final InputParameterData data = (InputParameterData) getRow(rowIndex);
        if (columnIndex == 0) {
            data.setName(newValue);
        } else if (columnIndex == 1) {
            data.setType(newValue);
        } else if (columnIndex == 2) {
            data.setDefault(newValue);
        } else if (columnIndex == 3) {
            data.setDescription(newValue);
        } else if (columnIndex == 4) {
            data.setRequired(newValue);
        } else if (columnIndex == 5) {
            data.setWorkingValue(newValue);
        }
        updateRow(rowIndex, data, false);
    }

    /**
	 * (non-Javadoc)
	 * 
	 * @see org.xaware.ide.xadev.table.contentprovider.TableContentProvider#getColumnRender(org.eclipse.swt.widgets.Composite,
	 *      int)
	 */
    @Override
    protected TableCellEditor getColumnRender(Composite parent, int column) {
        if (column == 4) {
            return new BooleanEditor(parent);
        }
        return null;
    }

    /**
	 * (non-Javadoc)
	 * 
	 * @see org.xaware.ide.xadev.table.contentprovider.TableContentProvider#findRow(java.lang.Object)
	 */
    @Override
    public int findRow(Object rowObject) {
        return getRows().indexOf(rowObject);
    }
}
