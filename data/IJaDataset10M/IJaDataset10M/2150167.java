package org.argkit.dung;

import java.security.InvalidParameterException;
import java.util.Iterator;
import java.util.logging.Logger;
import org.argkit.Argument;
import org.argkit.ArgumentSource;

/**
 * An abstract class that manages a Reasoner's argument source and 
 * defeat source, ready for a particular Reasoner implementation.
 * To implement a Reasoner, create a class that subclasses this one,
 * build the constructors you want and implement the calculate() method,
 * see {@link GroundedReasoner}.
 * 
 * @author Matt South
 *
 * @param <C> (Argument's) claim class
 */
public abstract class AbstractReasoner<C> implements Reasoner<C> {

    private static String MSG_MISSING_REFERENCES = "runArgumentGame() cannot be called before an ArgumentSource and a DefeatSource have been set";

    private static String MSG_INCOMPATIBLE_ARGUMENT = "The candidate argument is not available from the ArgumentSource";

    protected static Logger logger = Logger.getLogger(AbstractReasoner.class.getName());

    protected ArgumentSource<C> argumentSource;

    protected DefeatSource<C> defeatSource;

    /**
	 * Default Constructor.
	 */
    public AbstractReasoner() {
        super();
    }

    /**
	 * Typical constructor.  Often the argument source and the defeat
	 * source will be the same object, so this constructor checks to
	 * see if the provided argument source object also implements the 
	 * defeat source interface, and if it does, it sets the defeat source 
	 * as well.
	 * 
	 * @param source argument source
	 */
    public AbstractReasoner(ArgumentSource<C> source) {
        super();
        this.argumentSource = source;
        if (source instanceof DefeatSource) {
            this.defeatSource = (DefeatSource<C>) source;
        }
    }

    public ArgumentSource<C> getArgumentSource() {
        return argumentSource;
    }

    public void setArgumentSource(ArgumentSource<C> argumentSource) {
        this.argumentSource = argumentSource;
    }

    public DefeatSource<C> getDefeatSource() {
        return defeatSource;
    }

    public void setDefeatSource(DefeatSource<C> defeatSource) {
        this.defeatSource = defeatSource;
    }

    /**
	 * Throw error if there is no ArgumentSource or DefeatSource attached.
	 * Throw error if the argument presented is not available in the ArgumentSource
	 */
    public Dialogue<C> runArgumentGame(Argument<C> argument) {
        if (argumentSource == null || defeatSource == null) {
            throw new NullPointerException(MSG_MISSING_REFERENCES);
        } else {
            if (argumentSource.getAllArguments().contains(argument)) {
                return calculate(new Dialogue<C>(argument), 0);
            } else {
                throw new InvalidParameterException(MSG_INCOMPATIBLE_ARGUMENT);
            }
        }
    }

    protected abstract Dialogue<C> calculate(Dialogue<C> dialogue, int level);

    /**
	 * Write indented message to the logStream
	 * @param message The message to write
	 * @param level The level of indentation
	 */
    protected void log(String message, int level) {
        StringBuffer indentedMessage = new StringBuffer();
        for (int i = 0; i < level; i++) {
            indentedMessage.append("    ");
        }
        indentedMessage.append(message);
        logger.fine(indentedMessage.toString());
    }

    /**
	 * Draw a version of the current line of reasoning.
	 * i.e. {[a, c], [b]} = "a < b < c".
	 * 
	 * @param dialogue
	 * @return a description of the current line of reasoning.
	 */
    protected String drawBranch(Branch<C> dialogue) {
        StringBuffer buff = new StringBuffer();
        Iterator<Argument<C>> iterator = dialogue.iterator();
        while (iterator.hasNext()) {
            buff.append(iterator.next());
            if (iterator.hasNext()) buff.append(" < ");
        }
        return buff.toString();
    }
}
