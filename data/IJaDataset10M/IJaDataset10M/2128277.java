package newsclient.nntp;

import java.util.*;

/**
 * NNTP connection class.
 * <p>
 * This class extends the {@link ClientConnection} class, and is used
 * for communication using the NNTP protocol. It handles starting and
 * finishing a session on an NNTP server, and also sending commands
 * and receiving responses.
 * <p>
 * In this package the {@code NNTPConnection} is intended to be subclassed
 * by the {@code NNTPClient} class.
 *
 * @see <a href="http://www.ietf.org/rfc/rfc977.txt">RFC 977</a>
 * @see <a href="http://www.ietf.org/rfc/rfc2980.txt">RFC 2980</a>
 *
 * @see ClientConnection
 * @see NNTPClient
 *
 */
public class NNTPConnection extends ClientConnection {

    /** The TCP port assigned for the NNTP service according to RFC 977. */
    public static final int DEFAULT_NNTP_PORT = 119;

    /** String representing the empty string. */
    private static final String EMPTY_STRING = "";

    /** String representing a period followed by a CR LF pair. */
    private static final String DOT = ".";

    /** String representing a doubled period. */
    private static final String DOT_DOT = "..";

    /** Initial capacity for a buffer for received lines of a text response. */
    private static final int INITIAL_LINE_BUFFER_SIZE = 8;

    private LineInputReader lir = null;

    private LineOutputWriter low = null;

    /**
     * Constuctor for an NNTP connection.
     *
     * @see ClientConnection#ClientConnection()
     */
    protected NNTPConnection() {
    }

    /**
     * Starts a session on the given NNTP server.
     * Opens a connection to the server and receives the initial response.
     *
     * @param hostname Hostname of the NNTP server.
     *  Should not be {@code null} or empty.
     * @param port Port number used by the NNTP server.
     *  Should not be {@literal <0} or {@literal >65535}.
     * @throws NNTPException If the NNTP session cannot be started, i.e.
     *  if either of the following is true:
     *  <ul><li>The connection cannot be opened,
     *  <li>the server does not give a recognized response. </ul>
     *
     * @see ClientConnection#connect(String, int)
     */
    public void startSession(String hostname, int port) throws NNTPException {
        try {
            connect(hostname, port);
            lir = getLineInputReader();
            low = getLineOutputWriter();
            StatusResponse response = getStatusResponse();
            if (!response.codeIs(NNTPResponses.CODE_200) && !response.codeIs(NNTPResponses.CODE_201)) {
                throw new NNTPException(response, NNTPException.BAD_RESPONSE);
            }
        } catch (ConnectionException ce) {
            throw new NNTPException(ce, NNTPException.CONNECT_FAILED);
        } catch (NNTPException nntpe) {
            nntpe.printTypeAndMessage();
            nntpe.printStackTrace();
            throw new NNTPException(nntpe, NNTPException.CONNECT_FAILED);
        }
    }

    /**
     * Finishes the session on the NNTP server.
     * The server should close the connection at request, otherwise
     * the connection is closed by force, and consequently
     * the connection is no longer open after this method has been called.
     *
     * @see ClientConnection#disconnect()
     */
    public void finishSession() {
        if (isConnected()) {
            try {
                sendQuitCommand();
            } catch (NNTPException nntpe) {
                nntpe.printTypeAndMessage();
                nntpe.printStackTrace();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
            }
        }
        disconnect();
    }

    /**
     * Sends an {@code AUTHINFO USER} command with arguments to the server.
     *
     * @param username Username.
     * @throws NNTPException If send fails for some reason.
     */
    public void sendAuthinfoUserCommand(String username) throws NNTPException {
        sendCommand(NNTPCommands.AUTHINFO_USER, username);
    }

    /**
     * Sends an {@code AUTHINFO PASS} command with arguments to the server.
     *
     * @param password Password.
     * @throws NNTPException If send fails for some reason.
     */
    public void sendAuthinfoPassCommand(String password) throws NNTPException {
        sendCommand(NNTPCommands.AUTHINFO_PASS, password);
    }

    /**
     * Sends a {@code BODY} command with arguments to the server.
     *
     * @param messageID Article number or message-ID of the article to
     *  receive the body of.
     * @throws NNTPException If send fails for some reason.
     */
    public void sendBodyCommand(String messageID) throws NNTPException {
        sendCommand(NNTPCommands.BODY, messageID);
    }

    /**
     * Sends a {@code GROUP} command with arguments to the server.
     *
     * @param name Name of the newsgroup to select.
     * @throws NNTPException If send fails for some reason.
     */
    public void sendGroupCommand(String name) throws NNTPException {
        sendCommand(NNTPCommands.GROUP, name);
    }

    /**
     * Sends a {@code QUIT} command to the server.
     *
     * @throws NNTPException If send fails for some reason.
     */
    public void sendQuitCommand() throws NNTPException {
        sendCommand(NNTPCommands.QUIT);
    }

    /**
     * Sends an {@code XOVER} command with arguments to the server.
     *
     * @param range Article number or range to get overview of.
     * @throws NNTPException If send fails for some reason.
     */
    public void sendXoverCommand(String range) throws NNTPException {
        sendCommand(NNTPCommands.XOVER, range);
    }

    /**
     * Returns a status response from the server.
     *
     * @return A status response.
     * @throws NNTPException If receive fails for some reason.
     *
     * @see StatusResponse#StatusResponse(String)
     */
    public StatusResponse getStatusResponse() throws NNTPException {
        String response = getResponse();
        return new StatusResponse(response);
    }

    /**
     * Returns lines of a text response in an array of strings.
     * This method can be used to retrive an article body as an array.
     *
     * @return An array of strings, forming a text response.
     * @throws NNTPException If receive fails for some reason.
     */
    public String[] getTextResponseAsArray() throws NNTPException {
        Vector v = getTextResponseVector();
        String[] sa = new String[v.size()];
        v.copyInto(sa);
        return sa;
    }

    /**
     * Returns lines of a text response in an enumeration.
     * This method can be used to retrive an article overview as an
     * enumeration.
     *
     * @return An enumeration, forming a text response.
     * @throws NNTPException If receive fails for some reason.
     */
    public Enumeration getTextResponseAsEnumeration() throws NNTPException {
        Vector v = getTextResponseVector();
        return v.elements();
    }

    /**
     * Sends an NNTP command with no arguments to the server.
     * Usage: {@code sendCommand(NNTPCommands.COMMAND_NAME)}.
     *
     * @param command Command to send.
     * @throws IllegalArgumentException If {@code command} is not
     *  a valid command value.
     * @throws NNTPException If send fails for some reason.
     *
     * @see #sendCommand(String)
     */
    protected void sendCommand(int command) throws NNTPException {
        sendCommand(NNTPCommands.commands(command));
    }

    /**
     * Sends an NNTP command with arguments to the server.
     * Usage: {@code sendCommand(NNTPCommands.COMMAND_NAME, arguments)}.
     *
     * @param command Command to send.
     * @param arguments Arguments to command. May be {@code null},
     *   if no arguments are to be sent.
     * @throws IllegalArgumentException If either of the following is true:
     *  <ul><li>{@code command} is not a valid command value,
     *  <li>the whole command string, including space and arguments,
     *  has more than 510 characters. </ul>
     * @throws NNTPException If send fails for some reason.
     *
     * @see #sendCommand(String)
     */
    protected void sendCommand(int command, String arguments) throws NNTPException {
        String s = NNTPCommands.commands(command);
        StringBuffer sb = new StringBuffer(s.length() + 1 + arguments.length());
        if (arguments != null) {
            sb.append(s);
            sb.append(' ');
            sb.append(arguments);
        }
        sendCommand(sb.toString());
    }

    /**
     * Sends an NNTP command string to the server.
     *
     * @param command Command string to send.
     *  Should not be terminated with {@code CRLF},
     *  must be less than 510 characters in length.
     * @throws IllegalArgumentException If either of the following is true:
     *  <ul><li>{@code command} is {@code null},
     *  <li>{@code command} has more than 510 characters. </ul>
     * @throws NNTPException If send fails for some reason.
     *
     * @see #send(String)
     */
    protected void sendCommand(String command) throws NNTPException {
        if (command == null) {
            throw new IllegalArgumentException("No command given");
        }
        if (command.length() > 510) {
            throw new IllegalArgumentException("Command too long");
        }
        send(command);
    }

    /**
     * Send a string to the server.
     * 
     * @param line Line to send.
     * @throws NNTPException If send fails for some reason.
     *
     * @see ClientConnection.LineOutputWriter#writeLine(String)
     */
    private void send(String line) throws NNTPException {
        try {
            low.writeLine(line);
        } catch (ConnectionException ce) {
            disconnect();
            throw new NNTPException(ce, NNTPException.SEND_FAILED);
        } catch (RuntimeException re) {
            throw new NNTPException(re, NNTPException.SEND_FAILED);
        }
    }

    /**
     * Returns lines of a text response in a vector.
     *
     * @return A vector, containing a text response.
     * @throws NNTPException If receive fails for some reason.
     */
    private Vector getTextResponseVector() throws NNTPException {
        String line = null;
        Vector v = new Vector(INITIAL_LINE_BUFFER_SIZE);
        try {
            while (true) {
                line = getResponse();
                if (line == null) {
                    throw new NNTPException(NNTPException.RECEIVE_FAILED);
                } else if (line.equals(DOT)) {
                    break;
                } else if (line.startsWith(DOT_DOT)) {
                    v.addElement(line.substring(1));
                } else {
                    v.addElement(line);
                }
            }
        } catch (NNTPException nntpe) {
            if (v.isEmpty()) {
                throw nntpe;
            }
        }
        return v;
    }

    /**
     * Gets a response string from the line input stream.
     *
     * @return Received response line.
     *
     * @see ClientConnection.LineInputReader#readLine()
     */
    private String getResponse() throws NNTPException {
        String response = null;
        try {
            response = lir.readLine();
        } catch (ConnectionException ce) {
            disconnect();
            throw new NNTPException(ce, NNTPException.RECEIVE_FAILED);
        } catch (RuntimeException re) {
            throw new NNTPException(re, NNTPException.RECEIVE_FAILED);
        }
        return response;
    }
}
