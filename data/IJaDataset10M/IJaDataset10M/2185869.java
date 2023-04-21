package com.webloq.io.filter;

import java.io.IOException;
import java.util.Map;
import com.webloq.io.ISession;
import com.webloq.io.ProtocolException;
import com.webloq.io.lexer.LengthExceededException;
import com.webloq.io.util.BinaryString;

public class SimplexCommandFilter extends OctetStuffingFilter implements ASCIIBasedProtocol {

    /** Constant controlling encryption/decryption for commands and prefix */
    protected static final boolean COMMAND_ENCRYPT_BYPASS = false;

    protected ResultHandler handler = null;

    private Map<BinaryString, Command> commandMap;

    /**
     * Creates a new SimplexCommandFilter to process transactions between the sessions.
     * @param frontSession the front Session.
     * @param backSession the back Session.
     * @param chunkSize the size of chunks to read.
     */
    public SimplexCommandFilter(ISession frontSession, ISession backSession, int chunkSize) {
        super(frontSession, backSession, chunkSize);
    }

    /**
     * Gets the current command map.
     * @return the map of command names to Commands.
     */
    public Map<BinaryString, Command> getCommandMap() {
        return commandMap;
    }

    /**
     * Sets the map of commands and their command names.
     * @param commandMap a map of command names to Commands.
     */
    public void setCommandMap(Map<BinaryString, Command> commandMap) {
        this.commandMap = commandMap;
    }

    protected void frontPartialData(BinaryString data) throws IOException {
        LengthExceededException e = new LengthExceededException("Maximum length exceeded");
        e.setData(data);
        throw e;
    }

    protected void backPartialData(BinaryString data) throws IOException {
        LengthExceededException e = new LengthExceededException("Maximum length exceeded");
        e.setData(data);
        throw e;
    }

    /**
     * Extracts and returns the command portion (in lowercase) of the data.
     * @param data the line to get the command from.
     * @return the command key (in lowercase) from the command map.
     */
    protected BinaryString extractCommand(BinaryString data) {
        int commandEnd = data.indexOf(SPACE);
        if (commandEnd == -1) {
            commandEnd = data.indexOf(CRLF[eol]);
        }
        return data.substring(0, commandEnd).toLowerCase();
    }

    /**
     * Checks to see if the command portion of the data is in the command.
     * @param data the line to get the command from.
     * @return true if the return value of extractCommand(data) maps to something in the command map.
     */
    protected boolean hasCommand(BinaryString data) {
        BinaryString command = this.extractCommand(data);
        if (commandMap.get(command) != null) return true;
        return false;
    }

    /**
     * Passes the message to the correct command.
     * @param data the command and message.
     * @throws IOException if there is an IO error.
     */
    protected void command(BinaryString data) throws IOException {
        BinaryString command = this.extractCommand(data);
        if (commandMap.get(command) != null) {
            handler = commandMap.get(command).command(data);
            if (handler != null) {
                backExpectingMessage();
            } else {
                frontExpectingMessage();
            }
        } else {
            commandMissing(command, data);
        }
    }

    /**
     * Generates and throws a ProtocolException since the command is missing. 
     * @param command the command that isn't supported.
     * @param data the data attached to this command.
     * @throws IOException always throws an exception with the command name and data as its body.
     */
    protected void commandMissing(BinaryString command, BinaryString data) throws IOException {
        throw new ProtocolException("Command " + command.toString() + " missing");
    }

    /**
     * Handles the data and decides which side will be sending the next message.
     * @param data the data to process and send.
     * @throws IOException if there is an IO error.
     */
    protected void response(BinaryString data) throws IOException {
        if (handler == null) {
            handleGreeting(data);
            frontExpectingMessage();
        } else {
            ResultHandler newHandler = handler.handleResult(data);
            if (newHandler != null) {
                handler = newHandler;
                backExpectingMessage();
            } else {
                frontExpectingMessage();
            }
        }
    }

    protected void handleGreeting(BinaryString data) throws IOException {
    }

    protected void frontData(BinaryString data) throws IOException {
        command(data);
    }

    protected void backData(BinaryString data) throws IOException {
        response(data);
    }

    /**
	* Adds the temporary command to the command map.
	* @param tempCommand the command to support.
	* @param theCommand the command to execute when tempCommand is called.
	* @throws IOException if the command is already in the command map.
	*/
    protected void addTempCommand(BinaryString tempCommand, Command theCommand) throws IOException {
        BinaryString newCommand = this.extractCommand(tempCommand);
        if (this.hasCommand(tempCommand)) throw new IOException("SimplexCommandFilter.addAuthCommand(): Tried to overwrite an " + "existing command with the temporary command: " + newCommand);
        this.getCommandMap().put(newCommand, theCommand);
    }

    /**
	* Removes the temporary command from the command map.
	* @param tempCommand the command to remove.
	*/
    protected void removeTempCommand(BinaryString tempCommand) {
        BinaryString commandToRemove = this.extractCommand(tempCommand);
        this.getCommandMap().remove(commandToRemove);
    }

    /**
     * Commands pretty much execute on the data they are provided.
     * Sometimes they return ResultHandlers to process resultant data.
     * @author Evan Buswell
     */
    public interface Command {

        /**
		 * Handles the data and possibly returns a ReultHandler.
		 * @param data the data to process (depends on the command).
		 * @return a ResultHandler that can handle any applicable result data.
		 * @throws IOException if there is an IO error.
		 */
        public ResultHandler command(BinaryString data) throws IOException;
    }

    /**
     * A ResultHandler knows how to handle result data.
     * @author Evan Buswell
     */
    public interface ResultHandler {

        /**
    	 * Processes the result data and possibly returns another ResultHandler.
    	 * @param result the data to process.
    	 * @return a ResultHanlder that can handler any applicable result data.
    	 * @throws IOException if there is an IO error.
    	 */
        public ResultHandler handleResult(BinaryString result) throws IOException;
    }
}
