package oneiro.server.command.commands;

import oneiro.server.*;
import oneiro.server.entity.*;
import oneiro.server.command.*;
import oneiro.server.command.parser.*;
import oneiro.server.ai.*;
import oneiro.common.*;
import java.util.*;

/**
 * The "approach" command moves the user towards an object, whereas 
 * "enter" is used to step through doors or other openings.
 * 
 * @author Markus Mårtensson
 */
public class Move extends Command {

    public static final int APPROACH = 1;

    public static final int ENTER = 2;

    public static final int LEAVE = 3;

    /**
     * Constructs a <code>Move</code>.
     */
    public Move() {
    }

    public void registerNames() {
        addAlias("approach", APPROACH);
        addAlias("go", APPROACH);
        addAlias("enter", ENTER);
    }

    /**
     * Throws a message describing the syntax of the command.
     * 
     * @throws          FeedbackException
     *                  containing command syntax.
     */
    public void syntax() throws FeedbackException {
        throw new CommandFeedback(Message.createError().add("Move [target]"));
    }

    /**
     * Invoked by the command job if the parser found an entity as
     * parameter. 
     *
     * @param target    the entity on which to send information.
     */
    public void perform(NounPhrase target) throws FeedbackException {
        try {
            perform(target.resolveEntity(getCaller()));
        } catch (EntityNotFoundException e) {
            throw new CommandFeedback(Message.createError().add("Could not find '" + e.getMessage() + "'."));
        }
    }

    /**
     * Performs move action towards the provided entity.
     *
     * @param target    the entity on which to send information.
     */
    public void perform() throws FeedbackException {
        Entity e = getCaller();
        switch(getSubCmd()) {
            case APPROACH:
                if (e.getPosition() == Position.NEAR) {
                    approach(e.getParent());
                    return;
                }
                break;
            case ENTER:
                enter(e.getParent());
                return;
        }
        syntax();
    }

    /**
     * Performs move action towards the provided entity.
     *
     * @param target    the entity on which to send information.
     */
    protected void perform(Entity target) throws FeedbackException {
        switch(getSubCmd()) {
            case APPROACH:
                approach(target);
                break;
            case ENTER:
                enter(target);
                break;
        }
    }

    protected void approach(Entity target) throws FeedbackException {
        Entity e = getCaller();
        if (e.getParent() != target) {
            target.addChild(e, Position.NEAR);
        } else {
            target.addChild(e, Position.CLOSE);
        }
        throw new CommandFeedback(Message.createDescription().add(Message.createSentence().addPhrase("you move towards").add(target.toMessage())));
    }

    protected void enter(Entity target) throws FeedbackException {
        try {
            getCaller().moveToContainer(target);
            throw new CommandFeedback(Message.createDescription().add(Message.createSentence().addPhrase("you have entered").add(getCaller().getContainer().toMessage())));
        } catch (IllegalEntityOperationException e) {
            Log.debug(getCaller() + " failed to enter " + target + ".");
        }
    }
}
