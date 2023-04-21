package org.nakedobjects.nos.client.cli.awt;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.nakedobjects.nof.core.conf.Configuration;
import org.nakedobjects.nof.core.context.NakedObjectsContext;
import org.nakedobjects.nos.client.cli.Input;
import org.nakedobjects.nos.client.cli.View;
import org.nakedobjects.nos.client.cli.controller.AbstractConsole;
import org.nakedobjects.nos.client.cli.controller.CommandLineController;
import org.nakedobjects.nos.client.cli.debug.DebugInput;
import org.nakedobjects.nos.client.cli.debug.DebugView;

class AwtConsoleCliClient extends AbstractConsole {

    protected void init(final CommandLineController controller) {
        ConsoleWindow console = new ConsoleWindow();
        console.addWindowListener(new WindowAdapter() {

            public void windowClosing(final WindowEvent e) {
                shutdown();
            }
        });
        try {
            Input input = console.getInput();
            View view = console.getView();
            view = chainView(view);
            input = chainInput(input);
            boolean logInteractions = NakedObjectsContext.getConfiguration().getBoolean(Configuration.ROOT + "viewer.cli-awt.log");
            if (logInteractions) {
                PrintStream printer;
                printer = new PrintStream(new FileOutputStream("commandline.log"));
                input = new DebugInput(printer, input);
                view = new DebugView(printer, view);
            }
            controller.setInput(input);
            controller.setView(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
        console.pack();
        console.show();
        console.toFront();
    }

    protected Input chainInput(Input input) {
        return input;
    }

    protected View chainView(View view) {
        return view;
    }
}
