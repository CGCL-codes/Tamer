package org.nakedobjects.nos.client.cli.controller;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.nakedobjects.nof.core.util.DebugString;
import org.nakedobjects.nos.client.cli.Command;
import org.nakedobjects.nos.client.cli.CommandHelpDispatcher;
import org.nakedobjects.nos.client.cli.Context;
import org.nakedobjects.nos.client.cli.Dispatcher;
import org.nakedobjects.nos.client.cli.IllegalDispatchException;
import org.nakedobjects.nos.client.cli.View;

class DispatchManager {

    private static final Logger LOG = Logger.getLogger(DispatchManager.class);

    private Hashtable dispatchers = new Hashtable();

    void addDispatcher(Dispatcher dispatcher) {
        StringTokenizer names = new StringTokenizer(dispatcher.getNames(), " ");
        while (names.hasMoreTokens()) {
            Object name = names.nextElement();
            Vector list = (Vector) dispatchers.get(name);
            if (list == null) {
                list = new Vector();
                dispatchers.put(name, list);
            }
            list.addElement(dispatcher);
        }
    }

    Dispatcher createDispatchHelper() {
        return new CommandHelpDispatcher(dispatchers);
    }

    void dispatch(Command command, Context context, Vector dispatchers, View view) {
        try {
            for (Enumeration e = dispatchers.elements(); e.hasMoreElements(); ) {
                Dispatcher dispatcher = (Dispatcher) e.nextElement();
                if (dispatcher.isAvailable(context)) {
                    LOG.debug("request handled by " + dispatcher);
                    dispatcher.execute(command, context, view);
                    return;
                }
            }
            view.error("can't invoke this command at this time");
        } catch (IllegalDispatchException e) {
            view.error(e.getMessage());
        }
    }

    void dispatch(final Command command, Context context, View view) {
        String name = command.getName().toLowerCase();
        Vector list = (Vector) dispatchers.get(name);
        if (list != null) {
            dispatch(command, context, list, view);
        } else {
            view.error("unknown command " + command.getName());
        }
    }

    void dispatchers(Context context, View view) {
        DebugString debug = new DebugString();
        Enumeration f = dispatchers.elements();
        while (f.hasMoreElements()) {
            Vector list = (Vector) f.nextElement();
            for (Enumeration e = list.elements(); e.hasMoreElements(); ) {
                Dispatcher dispatcher = (Dispatcher) e.nextElement();
                String name = dispatcher.getClass().getName();
                name = name.substring(name.lastIndexOf('.') + 1);
                debug.append(dispatcher.isAvailable(context) ? "*" : " ");
                debug.append(name);
                debug.append(" (");
                debug.append(dispatcher.getNames());
                debug.append(") - ");
                debug.appendln(dispatcher.getHelp());
            }
        }
        view.display(debug.toString());
    }
}
