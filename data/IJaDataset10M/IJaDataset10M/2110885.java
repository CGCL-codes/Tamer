package org.mxeclipse.business.table.type;

import java.util.Iterator;
import matrix.util.MatrixException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;
import org.mxeclipse.business.table.range.MxRangeComposite;
import org.mxeclipse.exception.MxEclipseException;
import org.mxeclipse.model.MxTableColumn;
import org.mxeclipse.model.MxTreeAttribute;
import org.mxeclipse.model.MxTreePolicy;
import org.mxeclipse.model.MxTreeRange;
import org.mxeclipse.model.MxTreeType;
import org.mxeclipse.utils.MxEclipseLogger;
import org.mxeclipse.utils.MxEclipseUtils;
import org.mxeclipse.views.MxEclipseObjectView;

/**
 * This class implements an ICellModifier
 * An ICellModifier is called when the user modifes a cell in the
 * tableViewer
 */
public class MxTypeCellModifier implements ICellModifier {

    MxTypeComposite composite;

    /**
    * Constructor
    * @param TableViewerExample an instance of a TableViewerExample
    */
    public MxTypeCellModifier(MxTypeComposite composite) {
        super();
        this.composite = composite;
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
        Object result = "";
        MxTreeType type = (MxTreeType) element;
        try {
            if (property.equals(MxTableColumn.FIELD_NAME)) {
                Iterator itAttribute = MxTreeType.getAllTypes(false).iterator();
                result = -1;
                int i = 0;
                while (itAttribute.hasNext()) {
                    MxTreeType a = (MxTreeType) itAttribute.next();
                    if (a.getName().equals(type.getName())) {
                        result = i;
                        break;
                    }
                    i++;
                }
            }
        } catch (Exception ex) {
            MessageDialog.openError(composite.getShell(), "Type retrieval", "Error when retrieving a list of all types in the system!");
        }
        return result;
    }

    /**
    * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
    */
    public void modify(Object element, String property, Object value) {
        TableItem item = (TableItem) element;
        MxTreeType type = (MxTreeType) item.getData();
        try {
            if (property.equals(MxTableColumn.FIELD_NAME)) {
                int nValue = ((Integer) value).intValue();
                if (nValue >= 0) {
                    type.setName(MxTreeType.getAllTypes(false).get(nValue).getName());
                }
            }
        } catch (Exception ex) {
            MessageDialog.openInformation(composite.getShell(), "Type Configuration", "Error when editing value. Please give a correct value!");
        }
        composite.getBusiness().propertyChanged(type);
    }
}
