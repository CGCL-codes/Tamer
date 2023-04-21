package org.mxeclipse.business.table.person;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.mxeclipse.MxEclipsePlugin;
import org.mxeclipse.model.MxTreeBusiness;
import org.mxeclipse.model.MxTreePerson;
import org.mxeclipse.model.MxTreeState;

/**
 * Label provider for the TableViewerExample
 *
 * @see org.eclipse.jface.viewers.LabelProvider
 */
public class MxPersonLabelProvider extends LabelProvider implements ITableLabelProvider {

    public static final String PERSON_IMAGE = "person";

    public static final String LINK_TO_OBJECT_IMAGE = "link";

    public static final String CHECKED_IMAGE = "checked";

    public static final String UNCHECKED_IMAGE = "unchecked";

    private static ImageRegistry imageRegistry = new ImageRegistry();

    /**
	 * Note: An image registry owns all of the image objects registered with it,
	 * and automatically disposes of them the SWT Display is disposed.
	 */
    static {
        imageRegistry.put(PERSON_IMAGE, MxEclipsePlugin.getImageDescriptor("person.gif"));
        imageRegistry.put(LINK_TO_OBJECT_IMAGE, MxEclipsePlugin.getImageDescriptor("link_to_object.gif"));
        imageRegistry.put(CHECKED_IMAGE, MxEclipsePlugin.getImageDescriptor("checked.gif"));
        imageRegistry.put(UNCHECKED_IMAGE, MxEclipsePlugin.getImageDescriptor("unchecked.gif"));
    }

    private Image getTypeImage(MxTreeBusiness obj) {
        return imageRegistry.get(PERSON_IMAGE);
    }

    private Image getLinkImage() {
        return imageRegistry.get(LINK_TO_OBJECT_IMAGE);
    }

    /**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
    public String getColumnText(Object element, int columnIndex) {
        String result = "";
        MxTreePerson state = (MxTreePerson) element;
        String columnName = MxPersonComposite.columnNames[columnIndex];
        if (columnName.equals(MxPersonComposite.COLUMN_NAME)) {
            result = state.getName();
        }
        return result;
    }

    /**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
    public Image getColumnImage(Object element, int columnIndex) {
        Image result = null;
        MxTreePerson state = (MxTreePerson) element;
        String columnName = MxPersonComposite.columnNames[columnIndex];
        if (columnName.equals(MxPersonComposite.COLUMN_NAME)) {
            result = getTypeImage(state);
        } else if (columnName.equals(MxPersonComposite.COLUMN_LINK_TO_OBJECT)) {
            result = getLinkImage();
        }
        return result;
    }
}
