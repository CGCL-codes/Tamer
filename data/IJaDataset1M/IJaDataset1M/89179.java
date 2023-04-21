package org.eclipse.rwt.widgets;

import org.eclipse.rwt.RWT;
import org.eclipse.rwt.layout.Layout;

/**
 * A container to group and layout dialog fields.
 * <p>
 * In typical usage, the client instantiates this class and uses it as a parent
 * for a set {@link DialogField dialog fields}. The dialog field group
 * orchestrates the presentation of its fields.
 * </p>
 * <p>
 * The standard layout is roughly as follows: it has an area at the top
 * containing both the group's title, description, and image; the dialog fields
 * appear in the middle with their label on the beginning, their main control in
 * the middle, filling the remaining space, and their optional controls at the
 * end.
 * </p>
 * <p>
 * Note, although the wizard container is a {@link Container container} it
 * doesn't make sense to add children other than
 * {@link DialogField dialog fields} to it. Also setting a layout is a no-op.
 * </p>
 */
public class DialogFieldGroup extends Container {

    private String title, description;

    /**
	 * Creates and returns a new dialog field group.
	 * 
	 * @param id
	 * @param parent
	 * @param style
	 */
    public DialogFieldGroup(String id, Container parent, int style) {
        super(id, parent, style);
    }

    @Override
    protected void checkChildWidget(Widget widget) {
        if (!DialogField.class.isAssignableFrom(widget.getClass())) RWT.error(RWT.ERROR_INVALID_ARGUMENT, "not a dialog field");
    }

    /**
	 * Returns the description.
	 * 
	 * @return the description
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * Returns the name.
	 * 
	 * @return the name
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * Sets the description.
	 * 
	 * @param description
	 *            the description to set
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setLayout(Layout layout) {
    }

    /**
	 * Sets the title.
	 * 
	 * @param title
	 *            the title to set
	 */
    public void setTitle(String title) {
        this.title = title;
    }
}
