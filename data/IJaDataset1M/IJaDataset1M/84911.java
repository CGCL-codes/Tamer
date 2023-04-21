package com.netprogress.rcp.ui.framework.genericTableElements;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

/**
 * Label provider for the TableViewerExample
 * 
 * @see org.eclipse.jface.viewers.LabelProvider
 */
public class TGenericTableLabelProvider extends LabelProvider implements ITableLabelProvider, ITableColorProvider {

    public static final String CHECKED_IMAGE = "checked";

    public static final String UNCHECKED_IMAGE = "unchecked";

    public TGenericTableViewer genericTableViewer;

    public TGenericTableLabelProvider(TGenericTableViewer genericTableViewer) {
        this.genericTableViewer = genericTableViewer;
    }

    private static ImageRegistry imageRegistry = new ImageRegistry();

    static {
        String iconPath = "";
        imageRegistry.put(CHECKED_IMAGE, ImageDescriptor.createFromFile(TGenericTableViewer.class, iconPath + CHECKED_IMAGE + ".gif"));
        imageRegistry.put(UNCHECKED_IMAGE, ImageDescriptor.createFromFile(TGenericTableViewer.class, iconPath + UNCHECKED_IMAGE + ".gif"));
    }

    private Image getImage(String isSelected) {
        String key;
        if (isSelected.equals("true")) key = CHECKED_IMAGE; else key = UNCHECKED_IMAGE;
        return imageRegistry.get(key);
    }

    public String getColumnText(Object element, int columnIndex) {
        String result = "";
        boolean command = false;
        TGenericOperator task = (TGenericOperator) element;
        int content = genericTableViewer.columntypes[columnIndex];
        if (!(content == TGenericTableViewer._CHECKBOX_COLUMN)) result = task.getter(columnIndex, 0).toString();
        return result;
    }

    public Image getColumnImage(Object element, int columnIndex) {
        boolean command = false;
        int content = genericTableViewer.columntypes[columnIndex];
        if (content == TGenericTableViewer._CHECKBOX_COLUMN) {
            String s = ((TGenericOperator) element).getter(columnIndex, 0).toString();
            return this.getImage(s);
        } else return null;
    }

    public Color getForeground(Object element, int columnIndex) {
        return null;
    }

    public Color getBackground(Object element, int columnIndex) {
        TGenericOperator task = (TGenericOperator) element;
        if (task.getColor() != null) {
            return task.getColor();
        } else {
            if (task.getEditStatus() == TGenericOperator._NEWLY_ADDED_STATUS) {
                if (genericTableViewer.addedRows.indexOf(task) == -1) {
                    genericTableViewer.addedRows.add(task);
                }
                return new Color(null, 225, 236, 255);
            }
            if (task.getEditStatus() == TGenericOperator._UPDATED_STATUS) {
                if (genericTableViewer.updatedRows.indexOf(task) == -1) {
                    genericTableViewer.updatedRows.add(task);
                }
                return new Color(null, 254, 255, 225);
            } else {
                return new Color(null, 255, 255, 255);
            }
        }
    }
}
