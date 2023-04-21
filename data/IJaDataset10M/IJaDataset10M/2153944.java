package ow.tool.emulator.commands;

import java.io.PrintStream;
import java.util.List;
import ow.tool.emulator.EmulatorContext;
import ow.tool.util.shellframework.Command;
import ow.tool.util.shellframework.ShellContext;

public final class HelpCommand implements Command<EmulatorContext> {

    private static final String[] NAMES = { "help", "?" };

    public String[] getNames() {
        return NAMES;
    }

    public String getHelp() {
        return "help|?";
    }

    public boolean execute(ShellContext<EmulatorContext> context) {
        PrintStream out = context.getOutputStream();
        List<Command<EmulatorContext>> commandList = context.getCommandList();
        for (Command<EmulatorContext> command : commandList) {
            out.println(command.getHelp());
        }
        out.flush();
        return false;
    }
}
