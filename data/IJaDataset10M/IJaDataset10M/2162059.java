package org.mxeclipse.business.table.trigger;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.mxeclipse.MxEclipsePlugin;
import org.mxeclipse.model.MxTableColumn;
import org.mxeclipse.model.MxTreeBusiness;
import org.mxeclipse.model.MxTreePolicy;
import org.mxeclipse.model.MxTreeRange;
import org.mxeclipse.model.MxTreeTrigger;
import org.mxeclipse.object.tree.MxTreeContentProvider;

/**
 * Label provider for the TableViewerExample
 *
 * @see org.eclipse.jface.viewers.LabelProvider
 */
public class MxTriggerLabelProvider extends LabelProvider implements ITableLabelProvider {

    public static final String TRIGGER_IMAGE = "trigger";

    public static final String TRIGGER_IMAGE_GRAY = "triggergray";

    public static final String LINK_TO_OBJECT_IMAGE = "link";

    public static final String CHECKED_IMAGE = "checked";

    public static final String UNCHECKED_IMAGE = "unchecked";

    private static ImageRegistry imageRegistry = new ImageRegistry();

    /**
	 * Note: An image registry owns all of the image objects registered with it,
	 * and automatically disposes of them the SWT Display is disposed.
	 */
    static {
        imageRegistry.put(TRIGGER_IMAGE, MxEclipsePlugin.getImageDescriptor("trigger.gif"));
        imageRegistry.put(TRIGGER_IMAGE_GRAY, MxEclipsePlugin.getImageDescriptor("trigger_gray.gif"));
        imageRegistry.put(LINK_TO_OBJECT_IMAGE, MxEclipsePlugin.getImageDescriptor("link_to_object.gif"));
        imageRegistry.put(CHECKED_IMAGE, MxEclipsePlugin.getImageDescriptor("checked.gif"));
        imageRegistry.put(UNCHECKED_IMAGE, MxEclipsePlugin.getImageDescriptor("unchecked.gif"));
    }

    private Image getTypeImage(MxTreeBusiness obj) {
        if (obj.isInherited()) {
            return imageRegistry.get(TRIGGER_IMAGE_GRAY);
        } else {
            return imageRegistry.get(TRIGGER_IMAGE);
        }
    }

    private Image getLinkImage() {
        return imageRegistry.get(LINK_TO_OBJECT_IMAGE);
    }

    /**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
    public String getColumnText(Object element, int columnIndex) {
        String result = "";
        MxTreeTrigger trigger = (MxTreeTrigger) element;
        String columnName = MxTriggerComposite.columnNames[columnIndex];
        if (columnName.equals(MxTriggerComposite.COLUMN_EVENT_TYPE)) {
            result = trigger.getEventType();
        } else if (columnName.equals(MxTriggerComposite.COLUMN_TRIGGER_TYPE)) {
            result = trigger.getTriggerType();
        } else if (columnName.equals(MxTriggerComposite.COLUMN_PROGRAM_NAME)) {
            result = trigger.getMainProgramName();
        } else if (columnName.equals(MxTriggerComposite.COLUMN_ARGS)) {
            result = trigger.getArgs();
        }
        return result;
    }

    /**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
    public Image getColumnImage(Object element, int columnIndex) {
        Image result = null;
        MxTreeTrigger trigger = (MxTreeTrigger) element;
        String columnName = MxTriggerComposite.columnNames[columnIndex];
        if (columnName.equals(MxTriggerComposite.COLUMN_EVENT_TYPE)) {
            result = getTypeImage(trigger);
        } else if (columnName.equals(MxTriggerComposite.COLUMN_LINK_TO_OBJECT)) {
            result = getLinkImage();
        }
        return result;
    }
}
