package org.impalaframework.interactive.command;

import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.TextParsingCommand;
import org.impalaframework.facade.Impala;
import org.impalaframework.module.RootModuleDefinition;
import org.springframework.util.StopWatch;

public class RepairCommand implements TextParsingCommand {

    public boolean execute(CommandState commandState) {
        final RootModuleDefinition rootModuleDefinition = Impala.getRootModuleDefinition();
        if (rootModuleDefinition == null) {
            System.out.println("Cannot reload, as no module definition has been loaded.");
            return false;
        }
        return reload(commandState);
    }

    private boolean reload(CommandState commandState) {
        StopWatch watch = new StopWatch();
        watch.start();
        Impala.repairModules();
        watch.stop();
        InteractiveCommandUtils.printExecutionInfo(watch);
        final RootModuleDefinition rootModuleDefinition = Impala.getRootModuleDefinition();
        System.out.println("Current module state:");
        System.out.println(rootModuleDefinition.toString());
        return true;
    }

    public CommandDefinition getCommandDefinition() {
        return new CommandDefinition("Attempts to load modules which previously failed to load");
    }

    public void extractText(String[] text, CommandState commandState) {
    }
}
