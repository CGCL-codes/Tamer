package de.sonivis.tool.view.views;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * Defines the labeling of the tables in all views
 * 
 * @author Benedikt
 * @version $Revision: 905 $
 */
public class KeyTableLabelProvider extends LabelProvider implements ITableLabelProvider {

    public final Image getColumnImage(final Object element, final int columnIndex) {
        return null;
    }

    public final String getColumnText(final Object element, final int columnIndex) {
        if (element instanceof String[]) {
            final String[] values = (String[]) element;
            switch(columnIndex) {
                case 0:
                    return values[0];
                case 1:
                    return values[1];
                default:
                    return "unknown " + columnIndex;
            }
        }
        return null;
    }
}
