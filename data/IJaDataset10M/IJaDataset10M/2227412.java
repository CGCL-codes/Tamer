package workday.server.core.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.SynchronousQueue;
import workday.server.core.iCore;
import workday.behavior.i_behavior_ConsoleProgram;
import workday.common.i_common_Localization;
import workday.common.factory.product.i_common_Logger;
import workday.common.managers.i_common_CommandManager;
import workday.module.i_module_Console;
import workday.pattern.command.CommandConsole;

/**
 *
 */
public final class CommandManager extends Thread implements iCore, i_common_CommandManager, i_behavior_ConsoleProgram {

    private String DISPLAY_NAME = "Manager - Command manager";

    private double VERSION = 0.1;

    private String INVITATION_MESSAGE = "Workday console welcomes you!\nFor help, type 'help' command.";

    private String INVITATION = "CommandManager";

    private static CommandManager self;

    private i_common_Logger logger = loggerFactory.createLogger(CommandManager.class);

    private i_common_Localization localization;

    private HashMap<String, i_behavior_ConsoleProgram> programHandlers = new HashMap<String, i_behavior_ConsoleProgram>();

    private CopyOnWriteArrayList<CommandConsole> commandQueue = new CopyOnWriteArrayList<CommandConsole>();

    private CopyOnWriteArrayList<i_module_Console> consoleList = new CopyOnWriteArrayList<i_module_Console>();

    private String[] self_commands = { "help", "exit" };

    private ArrayList<String> selfCommands = new ArrayList<String>(Arrays.asList(self_commands));

    private final Object execute_synchro = new Object();

    private boolean interrupted = false;

    private SynchronousQueue<CommandConsole> queueToExecute = new SynchronousQueue<CommandConsole>(true);

    private CommandManager() {
        this.setName("CommandManager");
        localization = registry.getInstancePrimary(new i_common_Localization.Empty());
        if (localization != null) {
            INVITATION_MESSAGE = localization.getLocalizedString(null, "CommandManager.defaultInvitationMessage");
        }
        self = this;
    }

    public void findRecipients() {
        ArrayList<Object> consoles;
        consoles = registry.getInstanceList(new i_module_Console.Empty());
        if (consoles != null && !consoles.isEmpty()) {
            for (Object object : consoles.toArray()) {
                i_module_Console console = (i_module_Console) object;
                registerConsole(console);
                console.setCommandManager(this);
            }
        }
        ArrayList<Object> programs;
        programs = registry.getInstanceList(new i_behavior_ConsoleProgram.Empty());
        if (programs != null && !programs.isEmpty()) {
            for (Object object : programs.toArray()) {
                i_behavior_ConsoleProgram program = (i_behavior_ConsoleProgram) object;
                addConsoleProgram(program);
            }
        }
        return;
    }

    public static synchronized CommandManager getInstance() {
        return (self == null) ? new CommandManager() : self;
    }

    @Override
    public void run() {
        logger.info("CommandManager starting...");
        while (!interrupted) {
            executeCommand();
        }
        logger.info("CommandManager was interrupted.");
    }

    @Override
    public void interrupt() {
        interrupted = true;
    }

    public void registerConsole(i_module_Console console) {
        if (console == null || consoleList.contains(console)) {
            logger.warn("registerConsole(): console reference is null or such console already registered.");
            return;
        }
        console.setInvitaion(INVITATION);
        console.setInvitationMessage(INVITATION_MESSAGE);
        consoleList.add(console);
    }

    public void unregisterConsole(i_module_Console console) {
        if (console != null && consoleList.contains(console)) {
            consoleList.remove(console);
        }
        logger.warn("registerConsole(): console reference is null or such console already unregistered.");
    }

    public void addCommandToQueue(CommandConsole _command) {
        synchronized (execute_synchro) {
            if (_command == null) {
                logger.warn("addCommandToExecute(): command reference is null.");
                return;
            }
            commandQueue.add(_command);
            try {
                queueToExecute.put(_command);
            } catch (InterruptedException ex) {
                logger.warn(ex.getLocalizedMessage(), ex);
            }
        }
    }

    private void removeCommandFromQueue(CommandConsole _command) {
        if (_command == null) {
            logger.warn("removeCommandFromQueue(): command reference is null.");
            return;
        }
        commandQueue.remove(_command);
    }

    private void executeCommand() {
        final CommandConsole command;
        final i_behavior_ConsoleProgram program;
        try {
            logger.info("Waitind for command...");
            command = queueToExecute.take();
            logger.info("executing program - '" + command.getCommand() + "'");
            program = programHandlers.get(command.getCommand());
            if (program != null) {
                Executing executing = new Executing(program, command);
                executing.setName("executing - " + command.getCommand());
                executing.start();
            } else {
                logger.info("handler for command - '" + command.getCommand() + "' not found.");
                command.getConsole().appendAnswer("handler not found...", command);
                command.getConsole().finishAnswer(command);
            }
        } catch (InterruptedException exception) {
            interrupted = true;
            logger.warn("CommandManager was interrupted.", exception);
        }
    }

    public double getVersion() {
        return VERSION;
    }

    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    public void execute(CommandConsole command) {
        if (command.getCommand().compareTo("help") == 0) {
            command.getConsole().appendAnswer("Registered programs:", command);
            for (String program : programHandlers.keySet()) {
                command.getConsole().appendAnswer(" " + program, command);
            }
        } else if (command.getCommand().compareTo("exit") == 0) {
            command.getConsole().closeView(command);
        }
    }

    public void addConsoleProgram(i_behavior_ConsoleProgram newProgram) {
        if (newProgram == null) {
            logger.warn("addConsoleProgram(): program referense is null.");
            return;
        }
        ArrayList<String> commands = null;
        try {
            commands = newProgram.getCommandNames();
        } catch (Exception ex) {
            logger.warn("Invalid command-line program:  " + newProgram.getDisplayName());
        }
        if (commands == null) {
            return;
        }
        for (int i = 0; i < commands.size(); i++) {
            if (programHandlers.containsKey(commands.get(i))) {
                logger.warn("Handler to program '" + commands.get(i) + "' as already registered.");
                continue;
            }
            programHandlers.put(commands.get(i), newProgram);
            logger.info("Add handler to program '" + commands.get(i) + "'");
        }
        return;
    }

    public ArrayList<String> getCommandNames() {
        return selfCommands;
    }
}

class Executing extends Thread {

    private CommandConsole Command;

    private i_behavior_ConsoleProgram Program;

    public Executing(i_behavior_ConsoleProgram Program, CommandConsole Command) {
        this.Command = Command;
        this.Program = Program;
    }

    @Override
    public void run() {
        Program.execute(Command);
        Command.getConsole().finishAnswer(Command);
    }
}
