package mipt.gui;

import javax.swing.Icon;

/**
 * Supplier of strings and icons by name
 * Is named GUI.. (not UI) not to conflict with swing.UIResource
 * @author Evdokimov
 */
public interface GUIResource {

    String getString(String key);

    Icon getIcon(String key);
}
