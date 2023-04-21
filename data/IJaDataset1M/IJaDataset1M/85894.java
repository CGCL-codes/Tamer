package mudstrate.ui.listeners;

import mudstrate.engine.GlobalTicker;
import mudstrate.user.User;

public class TimeListener extends ExecutableListener {

    public TimeListener() {
        super("time", 2);
        this.setDesc("Displays various time-related data.\n");
    }

    @Override
    public boolean interpretCommand(String args) {
        if (super.interpretCommand(args)) {
            User u = this.getUser();
            u.send("Server time: " + GlobalTicker.getInstance().getDateString() + '\n' + "Online for : " + GlobalTicker.getInstance().getUptimeString() + '\n');
            return true;
        }
        return false;
    }
}
