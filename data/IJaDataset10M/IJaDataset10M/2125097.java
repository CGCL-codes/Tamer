package com.bluemarsh.jswat.command;

import com.bluemarsh.jswat.Log;
import com.bluemarsh.jswat.Session;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Defines the class that handles the 'apropos' command.
 *
 * @author  Nathan Fiedler
 */
public class aproposCommand extends JSwatCommand {

    /**
     * Perform the 'apropos' command.
     *
     * @param  session  JSwat session on which to operate.
     * @param  args     Tokenized string of command arguments.
     * @param  out      Output to write messages to.
     */
    public void perform(Session session, CommandArguments args, Log out) {
        if (!args.hasMoreTokens()) {
            throw new MissingArgumentsException();
        }
        CommandManager cmdman = (CommandManager) session.getManager(CommandManager.class);
        String[] commands = cmdman.getCommandNames();
        String regex = args.rest();
        regex = regex.toLowerCase();
        boolean success = false;
        Pattern patt = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        for (int ii = 0; ii < commands.length; ii++) {
            JSwatCommand command = cmdman.getCommand(commands[ii]);
            String desc = command.description();
            Matcher matcher = patt.matcher(desc);
            if (matcher.find()) {
                out.write(command.getCommandName());
                out.write(" - ");
                out.writeln(desc);
                success = true;
            }
        }
        if (!success) {
            throw new CommandException(Bundle.getString("apropos.notfound"));
        }
    }
}
