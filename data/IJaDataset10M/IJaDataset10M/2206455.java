package uk.ac.ebi.intact.application.intSeq.exception;

/**
 * This class manages the SRS exceptions.
 *
 * @author shuet (shuet@ebi.ac.uk)
 * @version : $Id: IntactSrsException.java 1275 2003-04-28 12:44:51Z shuet $
 */
public class IntactSrsException extends Exception {

    private String commandMessage;

    public IntactSrsException() {
    }

    /**
     * Constructor with a message.
     * @param msg which explains the exception.
     */
    public IntactSrsException(String msg) {
        super(msg);
    }

    /**
     * Constructor with a message.
     * @param msg which explains the exception and displays this one.
     */
    public IntactSrsException(String msg, Exception e) {
        super(msg);
        e.fillInStackTrace();
        commandMessage = e.getMessage();
    }

    /**
     * Keeps inform about the srs execution.
     *
     * @return String representing the message to display.
     */
    public String getCommandMessage() {
        if (commandMessage != null) {
            return commandMessage;
        } else {
            return "The command line seems to be passed on.";
        }
    }
}
