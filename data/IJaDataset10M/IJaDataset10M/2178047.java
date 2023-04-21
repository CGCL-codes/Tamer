package org.mooym.cmd;

import java.util.List;

/**
 * This class is the main class for the cmd-interface of Mooym.
 * 
 * @author roesslerj
 * @since 0.2
 */
public class MooymCmd {

    /**
   * The main method, to interact with the cmd-interface.
   * 
   * @param args The arguments.
   */
    public static void main(String[] args) {
    }

    /**
   * Converts the given Strings into a command to be executed.
   * 
   * @param args The arguments to convert.
   * @return The resulting command object.
   */
    private Command createCmd(List<String> args) {
        String commandString = args.get(0);
        Command command = null;
        if (commandString.equalsIgnoreCase("addRootFolder")) {
            AddRootFolderCmd addRootFolderCmd = new AddRootFolderCmd();
            String rootFolder = args.get(1);
            addRootFolderCmd.setRootFolder(rootFolder);
            command = addRootFolderCmd;
        }
        return command;
    }
}
