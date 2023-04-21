package net.sourceforge.antme.tasks;

import net.sourceforge.antme.AntMeException;
import net.sourceforge.antme.Language;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * This class serves as the base for all of our tasks.  Its primary
 * function is simply to hold a collection of utility routines.
 * 
 * @author khunter
 *
 */
public class BaseTask extends Task {

    /**
	 * Constructor.  This class is not intended to be instantiated on its own.
	 *
	 */
    protected BaseTask() {
    }

    /**
	 * Generate a warning message.
	 * 
	 * @param key	Language key for the string to be generated.
	 */
    public void warn(String key) {
        log(Language.getString(key), Project.MSG_WARN);
    }

    /**
	 * Generate a warning message.
	 * 
	 * @param key		Language key for the string to be generated.
	 * @param insert	Item to be inserted into the string.
	 */
    public void warn(String key, Object insert) {
        log(getLocation().toString() + Language.getString(key, insert), Project.MSG_WARN);
    }

    /**
	 * Generate a warning message.
	 * 
	 * @param key		Language key for the string to be generated.
	 * @param inserts	Array of items to be inserted into the string.
	 */
    public void warn(String key, Object[] inserts) {
        log(getLocation().toString() + Language.getString(key, inserts), Project.MSG_WARN);
    }

    /**
	 * Generate a informational message.
	 * 
	 * @param key	Language key for the string to be generated.
	 */
    public void info(String key) {
        log(getLocation().toString() + Language.getString(key), Project.MSG_INFO);
    }

    /**
	 * Generate a informational message.
	 * 
	 * @param key		Language key for the string to be generated.
	 * @param insert	Item to be inserted into the string.
	 */
    public void info(String key, Object insert) {
        log(getLocation().toString() + Language.getString(key, insert), Project.MSG_INFO);
    }

    /**
	 * Generate a informational message.
	 * 
	 * @param key		Language key for the string to be generated.
	 * @param inserts	Array of items to be inserted into the string.
	 */
    public void info(String key, Object[] inserts) {
        log(getLocation().toString() + Language.getString(key, inserts), Project.MSG_INFO);
    }

    /**
	 * Generate a verbose message.
	 * 
	 * @param key	Language key for the string to be generated.
	 */
    public void verbose(String key) {
        log(getLocation().toString() + Language.getString(key), Project.MSG_VERBOSE);
    }

    /**
	 * Generate a verbose message.
	 * 
	 * @param key		Language key for the string to be generated.
	 * @param insert	Item to be inserted into the string.
	 */
    public void verbose(String key, Object insert) {
        log(getLocation().toString() + Language.getString(key, insert), Project.MSG_VERBOSE);
    }

    /**
	 * Generate a verbose message.
	 * 
	 * @param key		Language key for the string to be generated.
	 * @param inserts	Array of items to be inserted into the string.
	 */
    public void verbose(String key, Object[] inserts) {
        log(getLocation().toString() + Language.getString(key, inserts), Project.MSG_VERBOSE);
    }

    /**
	 * Throw a <code>BuildException</code> indicating an error to Ant.
	 * @param messageKey		<code>String</code> keying the message from the resource bundle
	 * @throws BuildException	This will throw.  That's its function.
	 */
    public void throwBuildException(String messageKey) throws BuildException {
        throw new BuildException(Language.getString(messageKey), getLocation());
    }

    /**
	 * Throw a <code>BuildException</code> indicating an error to Ant.
	 * @param messageKey		<code>String</code> keying the message from the resource bundle
	 * @param substitute		Value to substitute into string.
	 * @throws BuildException	This will throw.  That's its function.
	 */
    public void throwBuildException(String messageKey, Object substitute) throws BuildException {
        throw new BuildException(Language.getString(messageKey, substitute), getLocation());
    }

    /**
	 * Throw a <code>BuildException</code> indicating an error to Ant.
	 * @param messageKey		<code>String</code> keying the message from the resource bundle
	 * @param substitute1		Value to substitute into string.
	 * @param substitute2		Value to substitute into string.
	 * @throws BuildException	This will throw.  That's its function.
	 */
    public void throwBuildException(String messageKey, Object substitute1, Object substitute2) throws BuildException {
        throw new BuildException(Language.getString(messageKey, new Object[] { substitute1, substitute2 }), getLocation());
    }

    /**
	 * Throw a <code>BuildException</code> indicating an error to Ant.
	 * @param messageKey		<code>String</code> keying the message from the resource bundle
	 * @param substitutes		Array of values to substitute into string.
	 * @throws BuildException	This will throw.  That's its function.
	 */
    public void throwBuildException(String messageKey, Object[] substitutes) throws BuildException {
        throw new BuildException(Language.getString(messageKey, substitutes), getLocation());
    }

    /**
	 * Throw a <code>BuildException</code> indicating an error to Ant.
	 * @param e					<code>AntMeException</code> that is the root cause of the BuildException
	 * @throws BuildException	This will throw.  That's its function.
	 */
    public void throwBuildException(AntMeException e) {
        String message = Language.getString(e.getLanguageKey());
        throw new BuildException(message, e, getLocation());
    }

    /**
	 * This routine wraps a throwable inside a <code>BuildException</code>.
	 * Part of the responsibility of this routine is to make sure that
	 * the language information is properly resolved into the message that
	 * the BuildException wants.
	 * 
	 * @param t					Throwable to be wrapped.
	 * @throws BuildException	A BuildException that wraps the throwable passed in.
	 */
    public void throwBuildException(Throwable t) throws BuildException {
        if (t instanceof BuildException) {
            throw (BuildException) t;
        }
        if (t instanceof AntMeException) {
            AntMeException ex = (AntMeException) t;
            throw new BuildException(Language.getString(ex.getLanguageKey()), t, getLocation());
        }
        throw new BuildException(t, getLocation());
    }
}
