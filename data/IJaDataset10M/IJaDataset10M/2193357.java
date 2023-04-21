package org.twdata.kokua.signal;

import org.werx.framework.bus.signals.BusSignal;
import java.awt.event.ActionEvent;

public class TerminalSignal extends BusSignal {

    private ActionEvent event;

    public TerminalSignal(ActionEvent e) {
        event = e;
    }

    public ActionEvent getEvent() {
        return event;
    }

    public String getCommand() {
        return event.getActionCommand();
    }
}
