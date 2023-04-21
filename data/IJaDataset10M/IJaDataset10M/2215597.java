package org.makagiga.commons.beans;

import java.awt.Component;
import org.makagiga.commons.MIcon;
import org.makagiga.commons.UI;
import org.makagiga.commons.sb.SecureOpen;
import org.makagiga.commons.swing.MText;

/**
 * @since 4.0
 */
public abstract class ComponentBeanInfo extends AbstractBeanInfo {

    protected ComponentBeanInfo(final Class<? extends Component> beanClass, final String displayName, final String iconName) {
        super(beanClass, displayName, iconName);
        if (MIcon.Name.class.isAssignableFrom(beanClass)) addPropertyDescriptor("iconName", PREFERRED, "The icon name (example: ui/ok)");
        if (MText.TextFieldExtensions.class.isAssignableFrom(beanClass)) {
            addPropertyDescriptor("autoCompletion", PREFERRED, "The text Auto Completion ID");
            addPropertyDescriptor("text", PREFERRED, "The text");
        }
        if (SecureOpen.class.isAssignableFrom(beanClass)) addPropertyDescriptor("secureOpen", PREFERRED, "Whether or not secure link open function is enabled");
        if (UI.EventsControl.class.isAssignableFrom(beanClass)) addPropertyDescriptor("eventsEnabled", HIDDEN, "Whether or not \"onChange\" event handler method is enabled");
        if (UI.MouseWheelEventsControl.class.isAssignableFrom(beanClass)) addPropertyDescriptor("mouseWheelEventsEnabled", PREFERRED, "Whether or not additional mouse wheel support is enabled");
    }
}
