package org.ufp.freecard.runtime;

import java.awt.event.*;
import java.util.Iterator;
import javax.swing.*;
import org.ufp.freecard.runtime.io.storage.DataStore;

/** Maps a FreeCard menu item to a Java menu item.
 * @author Adrian Sutton
 * @version $Revision: 1.4 $
 */
public class MenuItem extends Part implements ActionListener {

    /** Creates a new MenuItem.
     * @param id the id of this menu item.
     * @param name the name of the MenuItem to create.
     * @param menu the menu this item will be in.
     * @param runtime the runtime this item exists in.
     * @param dataStore the {@link DataStore} to load from.
     * @param dataStoreData the extra data to provide to the data store.
     * @throws FCException if the menu item could not be created.
     */
    public MenuItem(FCValue id, FCValue name, FCRuntime runtime, Menu menu, DataStore dataStore, Object dataStoreData) throws FCException {
        super(id, runtime, menu, dataStore, dataStoreData);
        setProperty(NAME, name);
    }

    /** Sets the value of the specified property of this item.  This
     * method updates the display when the NAME property is changed.
     * @param name the name of the property to change.
     * @param value the new value to set.
     * 
     * @throws FCException if the property could not be set.
     */
    public void setProperty(FCValue name, FCValue value) throws FCException {
        if (name.equals(NAME)) {
            Iterator displays = getDisplays();
            while (displays.hasNext()) {
                ((JMenuItem) displays.next()).setText(value.getAsString());
            }
        }
        super.setProperty(name, value);
    }

    /** Calss doMenu in the current stack when this menu is selected.
     * @param e the ActionEvent that occurred.
     */
    public void actionPerformed(ActionEvent e) {
        try {
            FCValue[] args = { getProperty(NAME), getParent().getProperty(NAME) };
            getRuntime().getFrontStack().callHandler("doMenu", args);
        } catch (FCException ex) {
            getRuntime().reportInternalError(ex);
        }
    }

    /** Creates a new display for this item.
     * 
     * @return a new display for this component.
     * @throws FCException if the display could not be created.
     */
    public JComponent createNewDisplay() throws FCException {
        JMenuItem item = new JMenuItem(getProperty(NAME).getAsString());
        item.addActionListener(this);
        return item;
    }

    /** Updates the position of this menu item.  Throws an
     * UnsupportedOperationException as menu items cannot be moved.
     * @exception FCException as menu items cannot be
     * moved.
     */
    public void updatePosition() throws FCException {
        throw new FCException("Menu items can not be moved.");
    }
}
