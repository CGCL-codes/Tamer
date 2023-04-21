package org.atomictagging.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.atomictagging.core.accessors.DB;
import org.atomictagging.core.configuration.Configuration;
import org.atomictagging.core.moleculehandler.MoleculeHandlerFactory;
import org.atomictagging.moleculehandler.base.RemoteMoleculeViewer;
import org.atomictagging.moleculehandler.image.ImageMoleculeImporter;
import org.atomictagging.moleculehandler.video.IMDBMoleculeImporter;
import org.atomictagging.shell.commands.EditCommand;
import org.atomictagging.shell.commands.HelpCommand;
import org.atomictagging.shell.commands.ICommand;
import org.atomictagging.shell.commands.ImportCommand;
import org.atomictagging.shell.commands.ListCommand;
import org.atomictagging.shell.commands.NewCommand;
import org.atomictagging.shell.commands.OpenCommand;
import org.atomictagging.shell.commands.RemoveCommand;
import org.atomictagging.shell.commands.SetScopeCommand;
import org.atomictagging.shell.commands.ShowCommand;
import org.atomictagging.shell.commands.TestDataCommand;

/**
 * A CLI for Atomic Tagging
 * 
 * @author Stephan Mann
 */
public class Shell implements IShell {

    /**
	 * Main entry point if a CLI is to be executed.
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        Shell shell = new Shell();
        shell.run();
    }

    private final Map<String, ICommand> commands = new HashMap<String, ICommand>();

    private final Map<String, String> environment = new HashMap<String, String>();

    /**
	 * Default constructor
	 */
    public Shell() {
        if (!Configuration.init()) {
            System.err.println("Could not load any configuration. Please check the manual on how to configure Atomic Tagging Shell.");
            System.exit(1);
        }
        try {
            DB.init();
        } catch (Exception e) {
            System.err.println("Could not connect to database.");
            System.err.println("Cause: " + e.getMessage());
            System.exit(1);
        }
        initCommands();
        initHandlers();
    }

    /**
	 * Initialize all commands.
	 */
    private void initCommands() {
        register(new HelpCommand(this));
        register(new ListCommand(this));
        register(new TestDataCommand(this));
        register(new ShowCommand(this));
        register(new OpenCommand(this));
        register(new ImportCommand(this));
        register(new SetScopeCommand(this));
        register(new EditCommand(this));
        register(new NewCommand(this));
        register(new RemoveCommand(this));
    }

    /**
	 * Initialize additional handlers.
	 */
    private void initHandlers() {
        MoleculeHandlerFactory factory = MoleculeHandlerFactory.getInstance();
        factory.registerViewer(new RemoteMoleculeViewer());
        factory.registerImporter(new IMDBMoleculeImporter());
        factory.registerImporter(new ImageMoleculeImporter());
    }

    @Override
    public String getEnvironment(final String key) {
        return environment.get(key);
    }

    @Override
    public void setEnvironment(final String key, final String value) {
        if (key == null || value == null) {
            return;
        }
        environment.put(key, value);
    }

    @Override
    public void register(final ICommand command) {
        commands.put(command.getCommandString(), command);
    }

    @Override
    public ICommand getCommand(final String commandString) {
        if (commands.containsKey(commandString)) {
            return commands.get(commandString);
        }
        return null;
    }

    @Override
    public Collection<ICommand> getCommands() {
        return Collections.unmodifiableCollection(commands.values());
    }

    private void run() {
        printWelcomeMessage();
        boolean run = true;
        while (run) {
            printPrompt();
            String input = readInput();
            if (input == null || input.isEmpty()) {
                continue;
            } else if (input.trim().equals("quit") || input.trim().equals("exit")) {
                run = false;
            } else {
                delegate(input);
            }
        }
        printGoodByeMessage();
    }

    private void delegate(String input) {
        String[] parts = input.trim().split(" ", 2);
        String command = parts[0];
        String params = "";
        if (parts.length == 2) {
            params = parts[1];
        }
        if (command.isEmpty()) {
            return;
        }
        if (commands.containsKey(command)) {
            commands.get(command).handleInput(params, System.out);
        } else {
            System.out.println("Command \"" + command + "\" not found.");
        }
    }

    private static String readInput() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = null;
        try {
            input = br.readLine();
        } catch (IOException ioe) {
            System.err.println("IO error trying to read user input.");
            System.exit(1);
        }
        return input;
    }

    private void printPrompt() {
        String scope = getEnvironment("scope");
        scope = (scope == null) ? "" : scope;
        System.out.print(scope + "> ");
    }

    private static void printWelcomeMessage() {
        System.out.println("Welcome to Atomic Tagging Shell version 0.0.6-mrmcd. Type 'help' to get startet.\n\n" + "This program comes with ABSOLUTELY NO WARRANTY. It is free software,\n" + "and you are welcome to redistribute it under the terms of GPLv3.\n" + "For more details see COPYING or <http://www.gnu.org/licenses/>.");
    }

    private static void printGoodByeMessage() {
        System.out.println("Bye.");
    }
}
