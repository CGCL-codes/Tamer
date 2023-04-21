package oneiro.server.command;

import oneiro.server.ai.*;
import oneiro.server.entity.*;
import oneiro.server.command.parser.*;
import oneiro.server.*;
import oneiro.common.*;
import java.util.*;

/**
 * A CommandJob is a special kind of Job, which allows user commands to
 * be carried out. This abstract class is missing a few parts which are
 * added by the <code>ParserCommandJob</code>.
 *
 * @author  Markus M�rtensson
 */
public abstract class CommandJob extends Job {

    protected Command cmd;

    protected NounPhrase firstNP = null;

    protected NounPhrase secondNP = null;

    protected String firstStr = null;

    protected String secondStr = null;

    protected CommandJob(String name) throws CommandNotFoundException {
        CommandInfo info = CommandLoader.getInfo(name);
        try {
            cmd = (Command) info.getCmd().newInstance();
            cmd.setSubCmd(info.getSubCmd());
            return;
        } catch (IllegalAccessException e) {
            Log.debug("Wrong constructor permissions.");
        } catch (InstantiationException e) {
            Log.debug("Command goes BANG!");
        } catch (NullPointerException e) {
            Log.debug("Command '" + name + "' not found.");
        }
        throw new CommandNotFoundException();
    }

    public abstract void perform() throws FeedbackException;

    public void run() {
        try {
            perform();
        } catch (FeedbackException e) {
            try {
                for (Iterator it = cmd.getCaller().getListeners(MessageChannelListener.class); it.hasNext(); ) {
                    ((MessageChannelListener) it.next()).messageReceived(e.getFeedback());
                }
            } catch (EnumerationFailedException ef) {
            }
        }
    }

    public void enqueue() {
        Server.addJob(this);
    }
}
