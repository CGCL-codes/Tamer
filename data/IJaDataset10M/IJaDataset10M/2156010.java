package org.mxeclipse.configure.table;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.mxeclipse.MxEclipsePlugin;
import org.mxeclipse.model.MxTableColumn;

/**
 * Label provider for the TableViewerExample
 *
 * @see org.eclipse.jface.viewers.LabelProvider
 */
public class MxTableColumnLabelProvider extends LabelProvider implements ITableLabelProvider {

    public static final String ATTRIBUTE_IMAGE = "attribute";

    public static final String BASIC_IMAGE = "basic";

    public static final String CHECKED_IMAGE = "checked";

    public static final String UNCHECKED_IMAGE = "unchecked";

    private static ImageRegistry imageRegistry = new ImageRegistry();

    /**
	 * Note: An image registry owns all of the image objects registered with it,
	 * and automatically disposes of them the SWT Display is disposed.
	 */
    static {
        imageRegistry.put(ATTRIBUTE_IMAGE, MxEclipsePlugin.getImageDescriptor("iconSmallAttribute.gif"));
        imageRegistry.put(BASIC_IMAGE, MxEclipsePlugin.getImageDescriptor("history.gif"));
        imageRegistry.put(CHECKED_IMAGE, MxEclipsePlugin.getImageDescriptor("checked.gif"));
        imageRegistry.put(UNCHECKED_IMAGE, MxEclipsePlugin.getImageDescriptor("unchecked.gif"));
    }

    /**
	 * Returns the image with the given key, or <code>null</code> if not found.
	 */
    private Image getBooleanImage(boolean isSelected) {
        String key = isSelected ? CHECKED_IMAGE : UNCHECKED_IMAGE;
        return imageRegistry.get(key);
    }

    private Image getTypeImage(String type) {
        if (type.equals(MxTableColumn.TYPE_ATTRIBUTE)) {
            return imageRegistry.get(ATTRIBUTE_IMAGE);
        } else if (type.equals(MxTableColumn.TYPE_ATTRIBUTE)) {
            return imageRegistry.get(BASIC_IMAGE);
        } else {
            return null;
        }
    }

    /**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
    public String getColumnText(Object element, int columnIndex) {
        String result = "";
        MxTableColumn task = (MxTableColumn) element;
        switch(columnIndex) {
            case 0:
                result = task.getName();
                break;
            case 1:
                result = task.getType();
                break;
            case 2:
                break;
            case 4:
                result = "" + task.getWidth();
                break;
            default:
                break;
        }
        return result;
    }

    /**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
    public Image getColumnImage(Object element, int columnIndex) {
        Image result = null;
        MxTableColumn task = (MxTableColumn) element;
        switch(columnIndex) {
            case 0:
                result = getTypeImage(task.getType());
                break;
            case 1:
                break;
            case 2:
                result = getBooleanImage(((MxTableColumn) element).isVisible());
                break;
            case 3:
                result = getBooleanImage(((MxTableColumn) element).isOnRelationship());
                break;
            case 4:
                break;
            default:
                break;
        }
        return result;
    }
}
