package roast;

/**
* The interface for log messages.
*
* @see LogFile
*/
public interface LogMessage extends java.io.Serializable {

    /**
* Returns a string representation of the log message.  The string
* should include the log message text, message level, and any other
* log message information formatted in a single line.
* <tt>LogFile</tt> uses this method to print the log message for
* the user.
* 
* @return
*	a string representation of the log message object
*/
    public abstract String toString();

    /**
* Returns the log message text as a String.
*
* @return
*	the log message text
*/
    public abstract String getMessage();

    /**
* Returns the log message level.
*
* @return
*	the log message level
*/
    public abstract int getLevel();
}
