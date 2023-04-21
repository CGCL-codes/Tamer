package com.sshtools.shift;

import javax.swing.Action;
import com.sshtools.common.ui.ResourceIcon;
import com.sshtools.common.ui.StandardAction;

public class FavoriteAction extends StandardAction {

    protected String directory;

    public FavoriteAction(String name, int weight, String directory) {
        this.directory = directory;
        putValue(Action.NAME, name);
        putValue(Action.SMALL_ICON, new ResourceIcon(FavoriteAction.class, "favourite.png"));
        putValue(Action.SHORT_DESCRIPTION, directory);
        putValue(Action.LONG_DESCRIPTION, "Favorite");
        putValue(Action.ACTION_COMMAND_KEY, "favorite-command");
        putValue(StandardAction.MENU_NAME, "Favorites");
        putValue(StandardAction.ON_MENUBAR, new Boolean(true));
        putValue(StandardAction.MENU_ITEM_GROUP, new Integer(80));
        putValue(StandardAction.MENU_ITEM_WEIGHT, new Integer(weight));
        putValue(StandardAction.ON_TOOLBAR, new Boolean(false));
    }
}
