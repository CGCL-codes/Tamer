package org.bd.banglasms.ui.lcdui;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Gauge;
import org.bd.banglasms.control.event.Event;
import org.bd.banglasms.control.event.EventHandler;
import org.bd.banglasms.control.event.NotifierHelper;
import org.bd.banglasms.ui.BusyView;

/**
 * Implementation of {@link BusyView} using <code>Alert</code>.
 *
 */
public class BusyViewLcdui implements BusyView, LcduiView, CommandListener {

    private Alert alert;

    private Command cancel;

    private boolean isIndefinite = true;

    private static Command defaultCommand = new Command("​", Command.OK, 1);

    private Gauge gauge = new Gauge(null, false, Gauge.INDEFINITE, Gauge.CONTINUOUS_RUNNING);

    private NotifierHelper notifier = new NotifierHelper();

    public BusyViewLcdui() {
        alert = new Alert(null);
        alert.addCommand(defaultCommand);
        alert.setIndicator(gauge);
        alert.setCommandListener(this);
    }

    public void setCancel(boolean hasCancel) {
        if ((cancel != null) != hasCancel) {
            if (hasCancel) {
                cancel = new Command("Cancel", Command.CANCEL, 1);
                alert.addCommand(cancel);
            } else {
                alert.removeCommand(cancel);
                cancel = null;
            }
        }
    }

    public void setIndefinite(boolean isIndefinite) {
        this.isIndefinite = isIndefinite;
    }

    public void setProgress(int alreadyDone, int totalToDo) {
        gauge.setMaxValue(totalToDo);
        gauge.setValue(alreadyDone);
    }

    public void setText(String text) {
        alert.setString(text);
    }

    public Displayable getDisplayable() {
        return alert;
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cancel) {
            notifier.notify(new Event(BusyView.EVENT_BUSY_VIEW_CANCELLED, this));
        }
    }

    public void addEventHandler(EventHandler eventHandler) {
        notifier.add(eventHandler);
    }

    public void init() {
    }

    public void removeEventHandler(EventHandler eventHandler) {
        notifier.remove(eventHandler);
    }

    public void setTitle(String title) {
        alert.setTitle(title);
    }
}
