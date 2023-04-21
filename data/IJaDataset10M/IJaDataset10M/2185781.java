package com.bluemarsh.jswat.command;

import com.bluemarsh.jswat.Log;
import com.bluemarsh.jswat.Session;

/**
 * Defines the class that handles the 'history' command.
 *
 * @author  Nathan Fiedler
 */
public class historyCommand extends JSwatCommand {

    /**
     * Perform the 'history' command.
     *
     * @param  session  JSwat session on which to operate.
     * @param  args     Tokenized string of command arguments.
     * @param  out      Output to write messages to.
     */
    public void perform(Session session, CommandArguments args, Log out) {
        CommandManager cmdman = (CommandManager) session.getManager(CommandManager.class);
        if (args.hasMoreTokens()) {
            String arg = args.nextToken();
            try {
                int size = Integer.parseInt(arg);
                cmdman.setHistorySize(size);
                out.writeln(Bundle.getString("history.sizeSet"));
            } catch (NumberFormatException nfe) {
                throw new CommandException(Bundle.getString("history.invalidSize"));
            } catch (IllegalArgumentException iae) {
                throw new CommandException(Bundle.getString("history.invalidRange"));
            }
        } else {
            cmdman.displayHistory();
        }
    }
}
