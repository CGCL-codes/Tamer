package org.carabiner.infopanel.event;

import java.io.PrintStream;
import java.util.EventObject;

public class ConsoleEventHandler implements EventHandler {

    private PrintStream stream;

    public ConsoleEventHandler() {
        stream = System.out;
    }

    public ConsoleEventHandler(PrintStream printStream) {
        stream = printStream;
    }

    public void handleEvent(EventObject e) {
        stream.println(e.toString());
    }
}
