package net.sf.hsdd.telnet;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import net.sf.hsdd.conf.usr.AuthInfo;
import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.io.terminal.ColorHelper;
import net.wimpi.telnetd.net.Connection;

/**
 *
 * @author Alexey.Kurgan
 */
public class HelpCommand extends AbstractTelnetCommand {

    private Map<String, TelnetCommand> commands;

    private List<TelnetCommand> sortedCommands;

    private int indent = 0;

    public HelpCommand(Map<String, TelnetCommand> commands) {
        super("help", "Print command list with description", "help\n  Print command list with description\n" + "help <command>\n  Print detail command help\n");
        this.commands = commands;
        this.sortedCommands = new LinkedList<TelnetCommand>(commands.values());
        Collections.sort(this.sortedCommands);
        for (TelnetCommand cmd : sortedCommands) {
            int cmdLength = cmd.getCommandName().length();
            if (indent < cmdLength) indent = cmdLength;
        }
        indent += 2;
    }

    public void run(String line, BasicTerminalIO tio, Connection connection, AuthInfo authInfo) throws IOException, ShutdownException, StopException {
        StringTokenizer tokens = new StringTokenizer(line);
        if (tokens.countTokens() > 1) {
            tokens.nextToken();
            String cmdName = trimAndRemoveQuotes(tokens.nextToken().toLowerCase());
            TelnetCommand cmd = commands.get(cmdName);
            if (cmd == null) {
                tio.write(MessageFormat.format("{0} {1} {2}", ColorHelper.colorizeText("Command", ColorHelper.RED), ColorHelper.colorizeText(cmdName, ColorHelper.YELLOW), ColorHelper.colorizeText("not found\n", ColorHelper.RED)));
                return;
            }
            tio.write(cmd.getLongHelp());
        } else {
            for (TelnetCommand cmd : sortedCommands) {
                String cmdName = cmd.getCommandName();
                int spaces = indent - cmdName.length();
                tio.write(cmdName);
                for (int i = 0; i < spaces; i++) tio.write(' ');
                tio.write(cmd.getCommandHelp());
                tio.write("\n");
            }
        }
        tio.flush();
    }
}
