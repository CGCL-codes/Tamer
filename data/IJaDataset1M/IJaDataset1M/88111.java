package com.realtime.crossfire.jxclient.gui.commands;

import java.util.ArrayList;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

/**
 * A list of {@link GUICommand} instances.
 * @author Andreas Kirschbaum
 */
public class CommandList {

    /**
     * The command list type.
     */
    @NotNull
    private final CommandListType commandListType;

    /**
     * The list of {@link GUICommand}s in execution order.
     */
    @NotNull
    private final Collection<GUICommand> commandList = new ArrayList<GUICommand>();

    /**
     * Create a new instance as an empty command list.
     * @param commandListType The command list type.
     */
    public CommandList(@NotNull final CommandListType commandListType) {
        this.commandListType = commandListType;
    }

    /**
     * Add a command to the end of this command list.
     * @param guiCommand The command to add.
     */
    public void add(@NotNull final GUICommand guiCommand) {
        commandList.add(guiCommand);
    }

    /**
     * Returns whether execution is possible.
     * @return whether execution is possible
     */
    public boolean canExecute() {
        switch(commandListType) {
            case AND:
                for (final GUICommand command : commandList) {
                    if (!command.canExecute()) {
                        return false;
                    }
                }
                break;
            case OR:
                boolean ok = false;
                for (final GUICommand command : commandList) {
                    if (command.canExecute()) {
                        ok = true;
                        break;
                    }
                }
                if (!ok) {
                    return false;
                }
                break;
        }
        return true;
    }

    /**
     * Execute the command list by calling {@link GUICommand#execute()} for each
     * command in order.
     */
    public void execute() {
        if (!canExecute()) {
            return;
        }
        for (final GUICommand command : commandList) {
            command.execute();
        }
    }

    /**
     * Return the commands as a string.
     * @return The commands as a string.
     */
    @NotNull
    public String getCommandString() {
        final StringBuilder sb = new StringBuilder();
        boolean firstCommand = true;
        for (final Object guiCommand : commandList) {
            final String commandString;
            if (guiCommand instanceof ExecuteCommandCommand) {
                commandString = ((ExecuteCommandCommand) guiCommand).getCommand();
            } else if (guiCommand instanceof ActivateCommandInputCommand) {
                final String commandText = ((ActivateCommandInputCommand) guiCommand).getCommandText();
                commandString = commandText.length() > 0 ? "-e " + commandText : "-e";
            } else {
                throw new AssertionError("Cannot encode command of type " + guiCommand.getClass().getName());
            }
            if (firstCommand) {
                firstCommand = false;
            } else {
                sb.append(';');
            }
            sb.append(commandString);
        }
        return sb.toString();
    }
}
