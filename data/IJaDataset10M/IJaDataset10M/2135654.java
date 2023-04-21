package org.bff.slimserver;

import java.util.ArrayList;
import java.util.List;

/**
 * SlimCommand represents a command along with optional command paramaters to be
 * sent to a {@link SlimServer} server.
 * 
 * @author Bill Findeisen
 * @version 1.0
 */
public class SlimCommand {

    private String command;

    private List<String> params;

    /**
     * Constructor for SlimServer command for a command requiring no parameters.
     * @param command the parameterless command to send.
     */
    public SlimCommand(String command) {
        this(command, new String[] { null });
    }

    /**
     * Constructor for SlimServer command for a command requiring a single 
     * parameter.
     * @param command the command to send
     * @param param the parameter for the command
     */
    public SlimCommand(String command, String param) {
        this(command, new String[] { param });
    }

    /**
     * Constructor for SlimServer command for a command requiring more 
     * than 1 parameter.
     * @param command the command to send
     * @param params the parameters to send
     */
    public SlimCommand(String command, String[] params) {
        this.command = command;
        this.params = new ArrayList<String>();
        for (int i = 0; i < params.length; i++) {
            this.params.add(params[i]);
        }
    }

    /**
     * Returns the command od this object.
     * @return the command
     */
    public String getCommand() {
        return command;
    }

    /**
     * Returns the parameter(s) of this command as a {@link List} of {@link String}s.
     * Returns null of there is no parameter for the command.
     * @return the parameters for the command
     */
    public List<String> getParams() {
        return params;
    }
}
