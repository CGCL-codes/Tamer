package org.login.console;

import org.makagiga.console.Console;
import org.makagiga.console.ConsoleCommand;
import org.makagiga.console.ConsoleIO;
import org.makagiga.plugins.PluginInfo;

/**
 * See org.makagiga.console package (Makagiga source)
 * for more examples.
 */
public class MainConsole extends ConsoleCommand {

    MainConsole(PluginInfo info) {
        super("login", info.shortDescription.get());
    }

    public Object onCommand(Console console, String... args) {
        ConsoleIO io = console.getIO();
        try {
            if (args.length >= 1) {
                onHelp(console);
                if (args[0].equals("login")) {
                }
            } else {
                this.onHelp(console);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            io.printLine("Wrong command used ! Type 'login help' for help manual.");
        }
        return null;
    }

    @Override
    public boolean onHelp(Console console) {
        ConsoleIO io = console.getIO();
        io.printLine("\n");
        io.printLine("Login - console mod. ver. 0.1 Alpha");
        io.printLine("Usage: login [command] <options/other>");
        io.printLine("Command");
        io.printLine("=======");
        io.printLine("chpwd - change password");
        io.printLine(" - e.g. man add <man name> <man filepath>");
        io.printLine("enpwd - enable password protection");
        io.printLine(" - e.g. man remove <man name>");
        io.printLine("dispwd  - disable password protection");
        io.printLine(" - e.g. man update <man name> <new man filepath>");
        io.printLine("timer - sets timer timing");
        io.printLine("entime - enable timer lock screen");
        io.printLine("distime - disable timer lock screen");
        io.printLine("enmin - enable lock screen upon minimizing Makagiga");
        io.printLine("dismin - disable lock screen upon minimizing Makagiga");
        io.printLine("");
        io.printLine("Note: The above command names are reserved key words.");
        return true;
    }

    @Override
    public boolean onExclusiveCommand(final Console console, final String command) {
        return true;
    }
}
