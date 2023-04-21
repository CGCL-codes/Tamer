package org.jbudget.gui.trans.editors;

import java.util.Collections;
import java.util.List;
import org.jbudget.Core.User;
import org.jbudget.io.DataManager;

/**
 * A combo box that allows user selection.
 * 
 * @author petrov
 */
public class UserSelectorComponent extends FormattedComboBox {

    public UserSelectorComponent() {
        super();
        List<User> users = DataManager.getInstance().getAllUsers(false, false);
        Collections.sort(users);
        for (User u : users) addItem(u);
    }
}
