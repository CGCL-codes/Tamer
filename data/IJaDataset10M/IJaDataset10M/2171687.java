package ndsromrenamer.file.tasks.inputblocker;

import ndsromrenamer.file.tasks.*;
import org.jdesktop.application.Task;
import org.jdesktop.application.Task.InputBlocker;

/**
 *
 * @author wusel
 */
public class BusyInputBlocker extends InputBlocker {

    BusyIndicator busyIndicator;

    public BusyInputBlocker(Task task, BusyIndicator busy) {
        super(task, Task.BlockingScope.WINDOW, busy);
        this.busyIndicator = busy;
    }

    @Override
    protected void block() {
        this.busyIndicator.start();
    }

    @Override
    protected void unblock() {
        this.busyIndicator.stop();
    }
}
