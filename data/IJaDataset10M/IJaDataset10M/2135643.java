package filterthroughcommand;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.dialogs.IDialogSettings;

/***
 * Manages the history of the filter through command dialog.
 * 
 * @author wduff2
 */
public class FilterThroughCommandHistoryManager {

    private static FilterThroughCommandHistoryManager manager;

    /**
	 * Uses the singleton design pattern so that there is only one history
	 * manager at a time.
	 * 
	 * @return A static FilterThroughCommandHistoryManager
	 */
    public static FilterThroughCommandHistoryManager getManager() {
        if (manager == null) {
            manager = new FilterThroughCommandHistoryManager();
        }
        return manager;
    }

    private List<String> commands;

    private static final int MAX_OLD_COMMANDS = 8;

    private static final String COMMAND_KEY = "Command";

    /**
	 * Instantiates a new filter through command history manager.
	 */
    private FilterThroughCommandHistoryManager() {
        openHistory();
    }

    /**
	 * Adds a command that was run into the history.
	 * 
	 * @param oldCommand
	 */
    public void addCommand(String oldCommand) {
        this.commands.remove(oldCommand);
        this.commands.add(0, oldCommand);
        if (this.commands.size() > MAX_OLD_COMMANDS) {
            this.commands.remove(this.commands.size() - 1);
        }
        saveHistory();
    }

    /**
	 * Gets the history of commands.
	 * 
	 * @return the history of commands
	 */
    public String[] getCommands() {
        return this.commands.toArray(new String[this.commands.size()]);
    }

    /**
	 * Opens the history of commands from the settings file.
	 */
    private void openHistory() {
        IDialogSettings settings = Activator.getDefault().getDialogSettings();
        setCommands(settings.getArray(COMMAND_KEY));
    }

    /**
	 * Saves the history of commands to the settings file.
	 */
    private void saveHistory() {
        IDialogSettings settings = Activator.getDefault().getDialogSettings();
        settings.put(COMMAND_KEY, getCommands());
    }

    /***
	 * Sets the history of commands.
	 * 
	 * @param commands
	 *            the history of commands
	 */
    public void setCommands(String[] commands) {
        this.commands = new ArrayList<String>();
        if (commands != null) {
            for (String command : commands) {
                this.commands.add(command);
            }
        }
    }
}
