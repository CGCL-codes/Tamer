package org.eclipse.rwt.widgets;

/**
 * A dialog field that presents data in a list.
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the RWT
 * implementation.
 * </p>
 */
public class List extends StructuredDialogField {

    /**
	 * Creates a new instance.
	 * 
	 * @param id
	 *            the widget id
	 * @param parent
	 *            the widget parent
	 * @param style
	 *            the widget style
	 */
    public List(String id, Container parent, int style) {
        super(id, parent, style);
    }
}
