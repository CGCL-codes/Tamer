package org.nakedobjects.viewer.cli;

import org.nakedobjects.utility.DebugString;

public class DebugContext implements Dispatcher {

    public void execute(Command command, Context context, View view) {
        DebugString debug = new DebugString();
        context.debug(debug);
        view.display(debug.toString());
    }

    public String getHelp() {
        return "Show the context stack in debug mode";
    }

    public String getNames() {
        return "debug";
    }

    public boolean isAvailable(Context context) {
        return true;
    }
}
