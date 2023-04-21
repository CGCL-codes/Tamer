package org.mxeclipse.object.property;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.mxeclipse.MxEclipsePlugin;

/**
 * Label provider for the TableViewerExample
 *
 * @see org.eclipse.jface.viewers.LabelProvider
 */
public class MxObjectLabelProvider extends LabelProvider implements ITableLabelProvider {

    public static final String CHECKED_IMAGE = "checked";

    public static final String UNCHECKED_IMAGE = "unchecked";

    public static final String PENDING_IMAGE = "pending";

    public static final String ATTRIBUTE_IMAGE = "attribute";

    public static final String HISTORY_IMAGE = "history";

    private static ImageRegistry imageRegistry = new ImageRegistry();

    /**
	 * Note: An image registry owns all of the image objects registered with it,
	 * and automatically disposes of them the SWT Display is disposed.
	 */
    static {
        imageRegistry.put(CHECKED_IMAGE, MxEclipsePlugin.getImageDescriptor("checked.gif"));
        imageRegistry.put(UNCHECKED_IMAGE, MxEclipsePlugin.getImageDescriptor("unchecked.gif"));
        imageRegistry.put(ATTRIBUTE_IMAGE, MxEclipsePlugin.getImageDescriptor("iconSmallAttribute.gif"));
        imageRegistry.put(PENDING_IMAGE, MxEclipsePlugin.getImageDescriptor("pending.gif"));
        imageRegistry.put(HISTORY_IMAGE, MxEclipsePlugin.getImageDescriptor("history.gif"));
    }

    /**
	 * Returns the image with the given key, or <code>null</code> if not found.
	 */
    private Image getImageChanged(boolean isChanged) {
        String key = isChanged ? PENDING_IMAGE : null;
        return imageRegistry.get(key);
    }

    private Image getTypeImage(String type) {
        if (type.equals(MxObjectProperty.TYPE_ATTRIBUTE)) {
            return imageRegistry.get(ATTRIBUTE_IMAGE);
        } else if (type.equals(MxObjectProperty.TYPE_HISTORY)) {
            return imageRegistry.get(HISTORY_IMAGE);
        } else {
            return null;
        }
    }

    /**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
    public String getColumnText(Object element, int columnIndex) {
        String result = "";
        MxObjectProperty task = (MxObjectProperty) element;
        switch(columnIndex) {
            case 0:
                result = task.getName();
                break;
            case 1:
                result = task.getValue();
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
        MxObjectProperty task = (MxObjectProperty) element;
        switch(columnIndex) {
            case 0:
                result = getTypeImage(task.getType());
                break;
            case 1:
                result = getImageChanged(((MxObjectProperty) element).isModified());
                break;
            default:
                break;
        }
        return result;
    }
}
