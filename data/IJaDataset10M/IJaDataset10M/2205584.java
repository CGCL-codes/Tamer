package org.apptools.ui.swing.prefs;

import java.util.prefs.BackingStoreException;
import javax.swing.JComponent;

/**An interface for components that should act as editors for Preferences 
 * objects. Each editor defines a JComponent to use as editor, a title 
 * to display with the editor, and a method for saving the associated 
 * Preferences object (or whatever is being edited)
 * @author joste021
 * 
 *
 */
public interface PreferencesEditor {

    /**Return a component that allows editing of the Preferences object
   * associated with this editor.*/
    public JComponent getEditorComponent();

    /**Return a title suitable for display with the editor.*/
    public String getTitle();

    /**Save the preferences entered into the current PreferencesEditor*/
    public void savePreferences() throws BackingStoreException;
}
