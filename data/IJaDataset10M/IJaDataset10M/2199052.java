package org.gerhardb.lib.util.startup;

import java.util.HashMap;
import javax.swing.Action;
import javax.swing.Icon;

/**
 * These are the actions before any overrides are applied from user changes to the menu structure.
 */
public class FactoryDefaultActions {

    protected HashMap<String, Action> myStarterActions = new HashMap<String, Action>();

    public FactoryDefaultActions() {
    }

    protected void addStartupAction(String application, String menu, String item, Action action, Icon icon) {
        String key = ActiveActions.makeKey(application, menu, item);
        Action loadedAction = ActiveActions.loadAction(key, action, icon);
        this.myStarterActions.put(key, loadedAction);
    }

    protected HashMap<String, Action> getStartupActions() {
        return this.myStarterActions;
    }
}
