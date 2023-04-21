package fido.response;

import java.util.*;

/**
 * Collects information during processing to report back to the user.
 * Methods of this class are called by the Processing module to set
 * the status of executed commands, or report an error.<P>
 * Todo:
 * <UL>
 * <LI>Store an action that was performed. Ex. <I>Throw the ball.</i>
 *     The response object would be notified that the ball was picked up first.
 * <LI>Store a number. If the user asks a How many type question, the
 *     system would store the result with this method. Ex. <I>How many
 *     dogs does John have?</I>
 * <LI>Set error. If an error occurred, such as NoMeaningsMatchException,
 *     this method could be called with a code for each error. Each would
 *     have to be specified in advance. This could use canned responses, mapped
 *     to words in the dictionary for the given language. For example, if
 *     no valid meanings were found, the canned response in English is <I>I don't
 *     understand</I>. This idea would some how be stored and the words from
 *     the dictionary would be used to tell the user, in whatever language.<br>
 * <LI>The class needs to read some kind of config info from the database telling
 *     the generateResponse() method how to formulate responses. <br>
 *     <br>
 *     For example: for something action that happens correctly, a rule in
 *     the table would be to print out simply: OK. Another rule could take
 *     the input of the sentence and reword it as to be more descriptive.<P>
 *     Ex1:<P>
 *     Fido&gt; Throw the ball to John.<br>
 *     OK<P>
 *     Ex2:<P>
 *     Fido&gt; Throw the ball to John.<br>
 *     Thrown.<P>
 *     Ex3:<P>
 *     Fido&gt; Throw the ball to John.<br>
 *     The ball was thrown to John<br>
 * </UL>
 */
public class Response {

    private static final int STATUS_NOT_SET = 0;

    private static final int STATUS_YES = 1;

    private static final int STATUS_NO = 2;

    private boolean processingStatus;

    private int yesNoStatus;

    private Vector returnObjects;

    private Vector whichObjects;

    private int missingInfo;

    private Exception exception;

    /**
	 * Creates a new Response instance.
	 */
    public Response() {
        whichObjects = new Vector();
        returnObjects = new Vector();
        reset();
    }

    /**
	 * Returns the class to its default state.  This should be
	 * called before processing each command.
	 */
    public void reset() {
        processingStatus = true;
        yesNoStatus = STATUS_NOT_SET;
        returnObjects.clear();
        whichObjects.clear();
        missingInfo = 0;
        exception = null;
    }

    /**
	 * If the user queries the system, the system will call this
	 * method, from an internal instruction, to set the yes / no
	 * response.
	 *
	 * @param status True if the query was true.  False
	 *        if it failed.
	 */
    public void setYesNoResponse(boolean response) {
        if (response == true) yesNoStatus = STATUS_YES; else yesNoStatus = STATUS_NO;
    }

    /**
	 * If the user requests the system perform an action, the
	 * system will report if the action was successful or not.
	 * The <I>response</I> parameter is set by the processing
	 * module if the simple action was successful.<P>
	 *
	 * Ex. <I>Throw the ball.</I> If the user does not have a ball, and there 
	 * is no ball around, this method would be called with false, indicating 
	 * the action (throw) could not be performed. If the user asks the system 
	 * to perform a command, and the command is successful, the Response class 
	 * will look at this value and print 'OK' if the action was successful. 
	 * If unsuccessful, use setMissingInformation() to store what requirements 
	 * were not set.
	 *
	 * @param status True if the action was sucessful.  False
	 *        if it failed.
	 */
    public void setProcessingStatus(boolean status) {
        processingStatus = status;
    }

    /**
	 * Returns the status of the last command run by the system.
	 * This is used internally by instructions to test 
	 * instructions.
	 * 
	 * @return True if the last command was successful.  False
	 *         otherwise.
	 */
    public boolean getProcessingStatus() {
        return processingStatus;
    }

    /**
	 * Adds an object to the list of possible object the user
	 * ment.
	 * 
	 * @param id Object id to add.
	 */
    public void addWhichObject(int id) {
        whichObjects.add(new Integer(id));
    }

    /**
	 * Adds the object the user requested.
	 * 
	 * @param id Object id to set.
	 */
    public void addReturnObject(int id) {
        returnObjects.add(new Integer(id));
    }

    /**
	 * 
	 * 
	 * 
	 * @param id Object id
	 */
    public void setMissingInformation(int id) {
        missingInfo = id;
    }

    /**
	 * Called when an unrecoverable error occured within
	 * the server that caused processing to end before
	 * a response could be genereated.
	 * 
	 * @param e Exception thrown by the server
	 */
    public void setException(Exception e) {
        exception = e;
    }

    /**
	 * After all processing is complete, this method generates
	 * a natural language response.
	 *
	 * @return String containing the natural language response
	 *         to the user input.
	 */
    public String generateResponse() {
        if (exception != null) {
            return "Internal error: " + exception.getClass().getName() + ": " + exception.getMessage();
        } else if (whichObjects.isEmpty() == false) {
            return "Which not supported.";
        } else if (returnObjects.isEmpty() == false) {
            return "Return objects not supported.";
        } else if (missingInfo != 0) {
            return "Missing info not supported.";
        } else if (yesNoStatus != STATUS_NOT_SET) {
            if (yesNoStatus == STATUS_YES) return "Yes"; else return "No";
        } else {
            if (processingStatus == true) return "OK"; else return "Failed";
        }
    }
}
