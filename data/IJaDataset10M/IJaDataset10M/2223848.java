package protoj.build;

import java.util.ArrayList;
import java.util.List;
import protoj.build.StartVmCommand.ClasspathConfig;

public class VirtualMachineSequence {

    private ArrayList<StartVmCommand> commands = new ArrayList<StartVmCommand>();

    private final String currentMainClass;

    private final List<String> args;

    private final List<String> jvmArgs;

    private final ClasspathConfig config;

    public VirtualMachineSequence(String currentMainClass, List<String> args, List<String> jvmArgs, StartVmCommand.ClasspathConfig config) {
        this.currentMainClass = currentMainClass;
        this.args = args;
        this.jvmArgs = jvmArgs;
        this.config = config;
    }

    public StartVmCommand addMachine(String mainClass, String memory) {
        StartVmCommand command = new StartVmCommand(mainClass);
        command.initFork(memory, false);
        command.initArgs(args);
        command.initJvmargs(jvmArgs);
        command.initClasspath(config);
        commands.add(command);
        return command;
    }

    public List<StartVmCommand> getCommands() {
        return commands;
    }

    public boolean inLastMachine() {
        StartVmCommand topCommand = commands.get(commands.size() - 1);
        return inMachine(topCommand);
    }

    public StartVmCommand bootstrap() {
        StartVmCommand destCommand = null;
        if (!inLastMachine()) {
            for (int i = 0; i < commands.size() - 1; i++) {
                StartVmCommand currentCommand = commands.get(i);
                StartVmCommand nextCommand = commands.get(i + 1);
                if (inMachine(currentCommand)) {
                    boolean machineExists = startMachine(nextCommand);
                    destCommand = machineExists ? nextCommand : null;
                    break;
                }
            }
        }
        return destCommand;
    }

    private boolean inMachine(StartVmCommand command) {
        return command.getMainClass().equals(currentMainClass);
    }

    private boolean startMachine(StartVmCommand nextCommand) {
        boolean machineExists = true;
        nextCommand.execute();
        if (!nextCommand.isSuccess()) {
            String target = nextCommand.getMainClass().replace(".", "/");
            String message = "java.lang.NoClassDefFoundError: " + target;
            boolean missingClass = nextCommand.getSystemErr().contains(message);
            if (missingClass) {
                machineExists = false;
            }
        }
        return machineExists;
    }
}
