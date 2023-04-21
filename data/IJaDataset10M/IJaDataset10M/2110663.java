package org.nakedobjects.viewer.cli.object;

import org.nakedobjects.viewer.cli.Command;
import org.nakedobjects.viewer.cli.Context;
import org.nakedobjects.viewer.cli.Dispatcher;
import org.nakedobjects.viewer.cli.View;
import org.nakedobjects.viewer.cli.action.ActionAgent;
import org.nakedobjects.viewer.cli.field.AbstractFieldAgent;

public class NextHistoryEntryDispatcher implements Dispatcher {

    public void execute(final Command command, final Context context, final View view) {
        context.getHistory().next();
    }

    public String getHelp() {
        return "Open next entry in history list";
    }

    public String getNames() {
        return "forward for f";
    }

    public boolean isAvailable(final Context context) {
        return context.getHistory().hasNext() && !(context.currentAgentIs(ActionAgent.class) || context.currentAgentIs(AbstractFieldAgent.class));
    }
}
