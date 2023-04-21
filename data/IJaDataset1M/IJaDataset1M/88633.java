package org.openremote.android.controller.component.control;

import java.util.ArrayList;
import java.util.List;
import org.openremote.android.controller.command.ExecutableCommand;
import org.openremote.android.controller.component.Component;

/**
 * The Class Control.
 * Android port by Marc Fleury
 * 
 * @author Handy.Wang 2009-10-15
 * @author marcf@openremote.org
 */
public abstract class Control extends Component {

    public static String CURRENT_STATUS = "OFF";

    /** The Constant DELAY_ELEMENT_NAME. */
    public static final String DELAY_ELEMENT_NAME = "delay";

    /** All commands a certain operation contains. */
    private List<ExecutableCommand> executableCommands;

    /**
     * Instantiates a new control.
     */
    public Control() {
        super();
        executableCommands = new ArrayList<ExecutableCommand>();
    }

    /**
     * Gets the executable commands.
     * 
     * @return the executable commands
     */
    public List<ExecutableCommand> getExecutableCommands() {
        return executableCommands;
    }

    /**
     * add executable command into executable command list.
     */
    public void addExecutableCommand(ExecutableCommand executablecommand) {
        executableCommands.add(executablecommand);
    }
}
