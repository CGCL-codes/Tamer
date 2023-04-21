package com.googlecode.lawu.net.irc.event;

import com.googlecode.lawu.event.AbstractEvent;
import com.googlecode.lawu.net.irc.Entity;
import com.googlecode.lawu.net.irc.IrcClient;
import com.googlecode.lawu.net.irc.cmd.IncomingIrcCommand;

/**
 * A skeleton IRC event implementation.
 * 
 * @author Miorel-Lucian Palii
 * @param <C>
 *            the type of command held
 */
public abstract class AbstractIrcEvent<C extends IncomingIrcCommand> extends AbstractEvent<IrcEventListener> implements IrcEvent<C> {

    private final IrcClient client;

    private final Entity origin;

    private final C command;

    /**
	 * Builds an event that associates the given client and origin entity with
	 * the specified command.
	 * 
	 * @param client
	 *            the client that received the command
	 * @param origin
	 *            the origin of the command
	 * @param command
	 *            the command to wrap
	 */
    public AbstractIrcEvent(IrcClient client, Entity origin, C command) {
        if (client == null) throw new NullPointerException("The client may not be null.");
        if (command == null) throw new NullPointerException("The command may not be null.");
        this.client = client;
        this.origin = origin;
        this.command = command;
    }

    @Override
    public IrcClient getClient() {
        return this.client;
    }

    @Override
    public Entity getOrigin() {
        return this.origin;
    }

    @Override
    public C getCommand() {
        return this.command;
    }
}
