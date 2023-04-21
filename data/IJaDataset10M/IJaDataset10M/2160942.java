package org.mxeclipse.object.property;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;

/**
 * This class implements an ICellModifier
 * An ICellModifier is called when the user modifes a cell in the
 * tableViewer
 */
public class MxObjectPropertyCellModifier implements ICellModifier {

    private MxObjectProperyTable tableViewerExample;

    private String[] columnNames;

    /**
    * Constructor
    * @param TableViewerExample an instance of a TableViewerExample
    */
    public MxObjectPropertyCellModifier(MxObjectProperyTable tableViewerExample) {
        super();
        this.tableViewerExample = tableViewerExample;
    }

    /**
    * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
    */
    public boolean canModify(Object element, String property) {
        return true;
    }

    /**
    * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
    */
    public Object getValue(Object element, String property) {
        int columnIndex = tableViewerExample.getColumnNames().indexOf(property);
        Object result = null;
        MxObjectProperty task = (MxObjectProperty) element;
        switch(columnIndex) {
            case 0:
                result = task.getName();
                break;
            case 1:
                result = task.getValue();
                break;
            default:
                result = "";
        }
        return result;
    }

    /**
    * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
    */
    public void modify(Object element, String property, Object value) {
        int columnIndex = tableViewerExample.getColumnNames().indexOf(property);
        TableItem item = (TableItem) element;
        MxObjectProperty mxProperty = (MxObjectProperty) item.getData();
        String valueString;
        boolean bModified = false;
        switch(columnIndex) {
            case 0:
                break;
            case 1:
                valueString = ((String) value).trim();
                mxProperty.setValue(valueString);
                bModified = mxProperty.isModified();
                break;
            default:
        }
        if (bModified) {
            tableViewerExample.getProperties().propertyChanged(mxProperty);
        }
    }
}
