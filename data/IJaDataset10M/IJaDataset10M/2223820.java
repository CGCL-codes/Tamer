package org.kommando.core.controlcenter;

import javax.swing.JComponent;
import org.kommando.core.Identifiable;

/**
 * @author Peter De Bruycker
 */
public interface PreferencePage extends Identifiable {

    /**
     * Returns the component
     * 
     * @return
     */
    JComponent getComponent();

    /**
     * The parent id is used to determine to location in the {@link PreferenceDialog}.
     * <p>
     * If this method returns <code>null</code>, this {@link PreferencePage} is added as a root element, otherwise it is
     * added as a child of the {@link PreferencePage} with the returned id.
     * 
     * @return the parent id
     */
    String getParentId();

    boolean apply();

    void restoreDefaults();

    /**
     * The name is shown to the user.
     * 
     * @return the name, cannot be <code>null</code> or empty.
     */
    String getName();
}
