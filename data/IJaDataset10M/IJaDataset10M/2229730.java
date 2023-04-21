package net.jankenpoi.sudokuki.ui.swing;

import java.util.HashMap;
import javax.swing.Action;

public class ActionsRepository {

    private final HashMap<String, Action> map = new HashMap<String, Action>();

    final Action get(String name) {
        Action action = map.get(name);
        return action;
    }

    final boolean put(String name, Action action) {
        if (name == null || name.isEmpty() || action == null) {
            return false;
        }
        Action oldAction = map.put(name, action);
        if (oldAction == null) {
            return true;
        } else {
            return false;
        }
    }
}
