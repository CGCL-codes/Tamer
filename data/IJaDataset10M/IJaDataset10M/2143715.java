package de.shandschuh.jaolt.gui.task;

import java.util.TimerTask;
import de.shandschuh.jaolt.gui.Lister;
import de.shandschuh.jaolt.tools.log.Logger;

public class SaveMemberTimerTask extends TimerTask {

    @Override
    public void run() {
        try {
            Lister.getCurrentInstance().save(true);
        } catch (Exception exception) {
            Logger.log(exception);
        }
    }
}
