package org.springframework.binding.form.swing;

import javax.swing.Icon;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.FieldFace;
import org.springframework.binding.form.MessageSourceFieldFaceSource;
import org.springframework.richclient.core.Application;
import org.springframework.richclient.swing.image.IconSource;

public class MessageSourceSwingFieldFaceSource extends MessageSourceFieldFaceSource {

    /**
     * Name for the FieldFace's <code>icon</code> property.
     */
    private static final String ICON_PROPERTY = "icon";

    private IconSource iconSource;

    protected FieldFace createFieldFace(FormModel formModel, String field, String displayName, String caption, String description, String encodedLabel) {
        Icon icon = getIconSource().getIcon(getMessage(formModel, field, ICON_PROPERTY));
        return new SwingFieldFace(displayName, caption, description, encodedLabel, icon);
    }

    /**
     * Set the icon source that will be used to resolve the 
     * FieldFace's icon property.
     */
    public void setIconSource(IconSource iconSource) {
        this.iconSource = iconSource;
    }

    protected IconSource getIconSource() {
        if (iconSource == null) {
            iconSource = (IconSource) Application.services().getService(IconSource.class);
        }
        return iconSource;
    }
}
