package org.akrogen.tkui.ui.swt.databinding.viewers;

import org.akrogen.tkui.core.ui.viewers.IUITableLabelProvider;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.widgets.Item;

public class SWTElementCellModifier implements ICellModifier {

    private StructuredViewer viewer;

    private IUITableLabelProvider elementTableLabelProvider;

    public SWTElementCellModifier(StructuredViewer viewer, IUITableLabelProvider elementTableLabelProvider) {
        this.viewer = viewer;
        this.elementTableLabelProvider = elementTableLabelProvider;
    }

    public boolean canModify(Object element, String property) {
        Object elt = getElement(element);
        int columnIndex = getColumnIndex(element, property);
        return elementTableLabelProvider.canModify(elt, columnIndex);
    }

    public Object getValue(Object element, String property) {
        return element;
    }

    public void modify(Object element, String property, Object value) {
        Object elt = getElement(element);
        int columnIndex = getColumnIndex(element, property);
        elementTableLabelProvider.setCellText(elt, columnIndex, value);
        viewer.refresh(elt);
    }

    protected Object getElement(Object element) {
        if (element instanceof Item) {
            Item tableItem = ((Item) element);
            return tableItem.getData();
        }
        return element;
    }

    protected int getColumnIndex(Object element, String property) {
        return Integer.parseInt(property);
    }
}
