package com.nexirius.framework.swing;

import com.nexirius.framework.command.Command;
import com.nexirius.framework.command.Processor;
import com.nexirius.util.assertion.Assert;
import com.nexirius.util.resource.ClientResource;

/**
 * This class extends CFJMenuItem and invokes a Command when the menu status
 * is clicked. The enabled/disabled status of the command is also automatically
 * reflected in the menu status status.
 * <pp>
 *
 * @author MB
 *         <p/>
 *         Date        Chng.Rq.   Author    Changes/Enhancements
 *         1999.02.09             MB       Created
 */
public class CommandJMenuItem extends CFJMenuItem implements CommandClient {

    private Command command;

    private Processor processor = null;

    private CommandClient.CommandPropertyChangeHandler listener;

    /**
     * Construct a CommandJMenuItem
     *
     * @param resource to be used to obtain JMenuItem properties
     * @param command  the command to be executed when the button is clicked.
     *                 The command name will be used as a resource key.
     */
    public CommandJMenuItem(ClientResource resource, Command command) {
        this(resource, command.getCommandName(), command);
    }

    /**
     * Construct a CommandJMenuItem
     *
     * @param resource    to be used to obtain JMenuItem properties
     * @param resourceKey key to be used with clienr resource
     * @param command     the command to be executed when the button is clicked
     */
    public CommandJMenuItem(ClientResource resource, String resourceKey, Command command) {
        super(resource, resourceKey);
        Assert.pre(command != null, "Parameter command is not null");
        this.command = command;
        addActionListener(new CommandClient.ActionHandler(command));
        listener = new CommandClient.CommandPropertyChangeHandler(command, this);
        this.command.addPropertyChangeListener(listener);
        setEnabled(command.isEnabled());
    }

    /**
     * Construct a CommandJMenuItem
     *
     * @param resource  to be used to obtain JMenuItem properties
     * @param command   the command to be executed when the button is clicked
     *                  The command name will be used as a resource key.
     * @param processor the processor on which to execute the command
     */
    public CommandJMenuItem(ClientResource resource, Command command, Processor processor) {
        this(resource, command.getResourceKey(), command, processor);
    }

    /**
     * Construct a CommandJMenuItem
     *
     * @param resource    to be used to obtain JMenuItem properties
     * @param resourceKey resource key to use with client resource
     * @param command     the command to be executed when the button is clicked
     * @param processor   the processor on which to execute the command
     */
    public CommandJMenuItem(ClientResource resource, String resourceKey, Command command, Processor processor) {
        this(resource, resourceKey, command);
        Assert.pre(processor != null, "Parameter processor is not null");
        this.processor = processor;
    }

    /**
     * Return the command executed whth this button is clicked
     *
     * @return the command
     */
    public Command getCommand() {
        return this.command;
    }

    /**
     * Return the processor which executes the command when this button is
     * clicked
     *
     * @return the processor (may be null)
     */
    public Processor getProcessor() {
        return this.processor;
    }

    /**
     * Set the command executed whth this button is clicked
     */
    public void setCommand(Command command) {
        Assert.pre(command != null, "Parameter command is not null");
        this.command = command;
        Assert.post(this.command == command, "command is set");
    }

    /**
     * Set the processor which executes the command when this button is
     * clicked
     *
     * @param processor the processor (may be null)
     */
    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    public void changeResourceKey(String resourceKey) {
        setResourceKey(resourceKey);
        update((ClientResource) null);
    }
}
