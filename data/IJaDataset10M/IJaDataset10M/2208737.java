package net.lagerwey.gash.command;

import net.lagerwey.gash.CurrentWorkingLocation;
import org.openspaces.admin.Admin;

/**
 * Changes directory by /<spacename>/<partitionId>/<objectType>.
 */
public class ChangeDirectoryCommand implements Command {

    private CurrentWorkingLocation currentWorkingLocation;

    public ChangeDirectoryCommand(CurrentWorkingLocation currentWorkingLocation) {
        this.currentWorkingLocation = currentWorkingLocation;
    }

    @Override
    public void perform(Admin admin, String command, String arguments) {
        currentWorkingLocation.changeLocation(admin, arguments);
    }

    @Override
    public String description() {
        return "Changes current working location.";
    }

    @Override
    public boolean connectionRequired() {
        return true;
    }
}
