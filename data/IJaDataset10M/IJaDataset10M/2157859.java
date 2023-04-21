package gui;

import java.awt.event.ActionEvent;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.*;
import ogv.OGV;
import util.SwingUtils;

public class ActionFabric {

    private final Map<CommandsLocal, Action> actions = new EnumMap<CommandsLocal, Action>(CommandsLocal.class);

    private final CommandsLocalListener listener;

    public ActionFabric(CommandsLocalListener listener) {
        this.listener = listener;
    }

    public final void initActions(CommandsLocal[] commands) {
        for (CommandsLocal c : commands) getAction(c);
    }

    public final Action getAction(final CommandsLocal c) {
        Action action = actions.get(c);
        if (action == null) {
            action = new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    listener.actionPerformed(e, c);
                }
            };
            SwingUtils.loadAction(action, OGV.getConfig().subnode("LocalActions").subnode(c.name()));
            actions.put(c, action);
        }
        return action;
    }
}
