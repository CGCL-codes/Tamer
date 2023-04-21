package org.xulbooster.eclipse.ui.utils.form.controls.tableViewer;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * Label provider for the TableViewerExample
 * 
 * @see org.eclipse.jface.viewers.LabelProvider 
 */
public class ColumnLabelProvider extends LabelProvider implements ITableLabelProvider {

    /**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
    public String getColumnText(Object element, int columnIndex) {
        Line line = (Line) element;
        return line.getCellValue(columnIndex);
    }

    /**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
    public Image getColumnImage(Object element, int columnIndex) {
        return null;
    }
}
