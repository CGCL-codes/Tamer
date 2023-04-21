package nm.util.alarmclock;

import java.awt.PopupMenu;
import java.awt.MenuItem;
import java.awt.event.ActionListener;
import java.io.Serializable;

public class AlarmMenu extends PopupMenu implements Serializable {

    public static final long serialVersionUID = 256L;

    public AlarmMenu(ActionListener listener) {
        optionsItem = startItem = stopItem = quitItem = null;
        createMenuItems();
        addActionListener(listener);
    }

    public void createMenuItems() {
        optionsItem = new MenuItem("Options");
        optionsItem.setActionCommand(AlarmClock.CMD_OPTIONS);
        startItem = new MenuItem("Start");
        startItem.setActionCommand(AlarmClock.CMD_START);
        stopItem = new MenuItem("Stop");
        stopItem.setActionCommand(AlarmClock.CMD_STOP);
        quitItem = new MenuItem("Quit");
        quitItem.setActionCommand(AlarmClock.CMD_QUIT);
        this.add(optionsItem);
        this.add(new MenuItem("-"));
        this.add(startItem);
        this.add(stopItem);
        this.add(new MenuItem("-"));
        this.add(quitItem);
    }

    public void addActionListener(ActionListener listener) {
        if (listener != null) {
            actionListener = listener;
            if (optionsItem != null) optionsItem.addActionListener(actionListener);
            if (startItem != null) startItem.addActionListener(actionListener);
            if (stopItem != null) stopItem.addActionListener(actionListener);
            if (quitItem != null) quitItem.addActionListener(actionListener);
        } else {
            actionListener = null;
        }
    }

    private ActionListener actionListener;

    private MenuItem optionsItem, startItem, stopItem, quitItem;
}
