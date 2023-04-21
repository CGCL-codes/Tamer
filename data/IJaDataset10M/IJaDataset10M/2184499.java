package com.kni.util.net.smtp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.Vector;
import com.kni.util.net.MalformedServerReplyException;
import com.kni.util.net.ProtocolCommandListener;
import com.kni.util.net.ProtocolCommandSupport;
import com.kni.util.net.SocketClient;

/***
 * SMTP provides the basic the functionality necessary to implement your
 * own SMTP client.  To derive the full benefits of the SMTP class requires
 * some knowledge of the FTP protocol defined in RFC 821.  However, there
 * is no reason why you should have to use the SMTP class.  The
 * <a href="org.apache.commons.net.smtp.SMTPClient.html"> SMTPClient </a> class,
 * derived from SMTP,
 * implements all the functionality required of an SMTP client.  The
 * SMTP class is made public to provide access to various SMTP constants
 * and to make it easier for adventurous programmers (or those with
 * special needs) to interact with the SMTP protocol and implement their
 * own clients.  A set of methods with names corresponding to the SMTP
 * command names are provided to facilitate this interaction.
 * <p>
 * You should keep in mind that the SMTP server may choose to prematurely
 * close a connection for various reasons.  The SMTP class will detect a
 * premature SMTP server connection closing when it receives a
 * <a href="org.apache.commons.net.smtp.SMTPReply.html#SERVICE_NOT_AVAILABLE">
 * SMTPReply.SERVICE_NOT_AVAILABLE </a> response to a command.
 * When that occurs, the SMTP class method encountering that reply will throw
 * an <a href="org.apache.commons.net.smtp.SMTPConnectionClosedException.html">
 * SMTPConnectionClosedException </a>.
 * <code>SMTPConectionClosedException</code>
 * is a subclass of <code> IOException </code> and therefore need not be
 * caught separately, but if you are going to catch it separately, its
 * catch block must appear before the more general <code> IOException </code>
 * catch block.  When you encounter an
 * <a href="org.apache.commons.net.smtp.SMTPConnectionClosedException.html">
 * SMTPConnectionClosedException </a>, you must disconnect the connection with
 * <a href="org.apache.commons.net.SocketClient.html#disconnect"> disconnect() </a>
 * to properly clean up the system resources used by SMTP.  Before
 * disconnecting, you may check the
 * last reply code and text with
 * <a href="#getReplyCode"> getReplyCode </a>,
 * <a href="#getReplyString"> getReplyString </a>,
 * and <a href="#getReplyStrings"> getReplyStrings</a>.
 * <p>
 * Rather than list it separately for each method, we mention here that
 * every method communicating with the server and throwing an IOException
 * can also throw a 
 * <a href="org.apache.commons.net.MalformedServerReplyException.html">
 * MalformedServerReplyException </a>, which is a subclass
 * of IOException.  A MalformedServerReplyException will be thrown when
 * the reply received from the server deviates enough from the protocol 
 * specification that it cannot be interpreted in a useful manner despite
 * attempts to be as lenient as possible.
 * <p>
 * <p>
 * @author Daniel F. Savarese
 * @see SMTPClient
 * @see SMTPConnectionClosedException
 * @see org.apache.commons.net.MalformedServerReplyException
 ***/
public class SMTP extends SocketClient {

    /*** The default SMTP port (25). ***/
    public static final int DEFAULT_PORT = 25;

    private StringBuffer __commandBuffer;

    BufferedReader _reader;

    BufferedWriter _writer;

    int _replyCode;

    Vector _replyLines;

    boolean _newReplyString;

    String _replyString;

    /***
     * A ProtocolCommandSupport object used to manage the registering of
     * ProtocolCommandListeners and te firing of ProtocolCommandEvents.
     ***/
    protected ProtocolCommandSupport _commandSupport_;

    /***
     * The default SMTP constructor.  Sets the default port to
     * <code>DEFAULT_PORT</code> and initializes internal data structures
     * for saving SMTP reply information.
     ***/
    public SMTP() {
        this.setDefaultPort(SMTP.DEFAULT_PORT);
        this.__commandBuffer = new StringBuffer();
        this._replyLines = new Vector();
        this._newReplyString = false;
        this._replyString = null;
        this._commandSupport_ = new ProtocolCommandSupport(this);
    }

    private int __sendCommand(String command, String args, boolean includeSpace) throws IOException {
        String message;
        this.__commandBuffer.setLength(0);
        this.__commandBuffer.append(command);
        if (args != null) {
            if (includeSpace) this.__commandBuffer.append(' ');
            this.__commandBuffer.append(args);
        }
        this.__commandBuffer.append(SocketClient.NETASCII_EOL);
        this._writer.write(message = this.__commandBuffer.toString());
        this._writer.flush();
        if (this._commandSupport_.getListenerCount() > 0) this._commandSupport_.fireCommandSent(command, message);
        this.__getReply();
        return this._replyCode;
    }

    private int __sendCommand(int command, String args, boolean includeSpace) throws IOException {
        return this.__sendCommand(SMTPCommand._commands[command], args, includeSpace);
    }

    private void __getReply() throws IOException {
        int length;
        String line, code;
        this._newReplyString = true;
        this._replyLines.setSize(0);
        line = this._reader.readLine();
        if (line == null) throw new SMTPConnectionClosedException("Connection closed without indication.");
        length = line.length();
        if (length < 3) throw new MalformedServerReplyException("Truncated server reply: " + line);
        try {
            code = line.substring(0, 3);
            this._replyCode = Integer.parseInt(code);
        } catch (NumberFormatException e) {
            throw new MalformedServerReplyException("Could not parse response code.\nServer Reply: " + line);
        }
        this._replyLines.addElement(line);
        if (length > 3 && line.charAt(3) == '-') {
            do {
                line = this._reader.readLine();
                if (line == null) throw new SMTPConnectionClosedException("Connection closed without indication.");
                this._replyLines.addElement(line);
            } while (!(line.length() >= 4 && line.charAt(3) != '-' && Character.isDigit(line.charAt(0))));
        }
        if (this._commandSupport_.getListenerCount() > 0) this._commandSupport_.fireReplyReceived(this._replyCode, this.getReplyString());
        if (this._replyCode == SMTPReply.SERVICE_NOT_AVAILABLE) throw new SMTPConnectionClosedException("SMTP response 421 received.  Server closed connection.");
    }

    /*** Initiates control connections and gets initial reply. ***/
    @Override
    protected void _connectAction_() throws IOException {
        super._connectAction_();
        this._reader = new BufferedReader(new InputStreamReader(this._input_));
        this._writer = new BufferedWriter(new OutputStreamWriter(this._output_));
        this.__getReply();
    }

    /***
     * Adds a ProtocolCommandListener.  Delegates this task to
     * <a href="#_commandSupport_"> _commandSupport_ </a>.
     * <p>
     * @param listener  The ProtocolCommandListener to add.
     ***/
    public void addProtocolCommandListener(ProtocolCommandListener listener) {
        this._commandSupport_.addProtocolCommandListener(listener);
    }

    /***
     * Removes a ProtocolCommandListener.  Delegates this task to
     * <a href="#_commandSupport_"> _commandSupport_ </a>.
     * <p>
     * @param listener  The ProtocolCommandListener to remove.
     ***/
    public void removeProtocolCommandistener(ProtocolCommandListener listener) {
        this._commandSupport_.removeProtocolCommandListener(listener);
    }

    /***
     * Closes the connection to the SMTP server and sets to null
     * some internal data so that the memory may be reclaimed by the
     * garbage collector.  The reply text and code information from the
     * last command is voided so that the memory it used may be reclaimed.
     * <p>
     * @exception IOException If an error occurs while disconnecting.
     ***/
    @Override
    public void disconnect() throws IOException {
        super.disconnect();
        this._reader = null;
        this._writer = null;
        this._replyString = null;
        this._replyLines.setSize(0);
        this._newReplyString = false;
    }

    /***
     * Sends an SMTP command to the server, waits for a reply and returns the
     * numerical response code.  After invocation, for more detailed
     * information, the actual reply text can be accessed by calling
     * <a href="#getReplyString"> getReplyString </a> or 
     * <a href="#getReplyStrings"> getReplyStrings </a>.
     * <p>
     * @param command  The text representation of the  SMTP command to send.
     * @param args The arguments to the SMTP command.  If this parameter is
     *             set to null, then the command is sent with no argument.
     * @return The integer value of the SMTP reply code returned by the server
     *         in response to the command.
     * @exception SMTPConnectionClosedException
     *      If the SMTP server prematurely closes the connection as a result
     *      of the client being idle or some other reason causing the server
     *      to send SMTP reply code 421.  This exception may be caught either
     *      as an IOException or independently as itself.
     * @exception IOException  If an I/O error occurs while either sending the
     *      command or receiving the server reply.
     ***/
    public int sendCommand(String command, String args) throws IOException {
        return this.__sendCommand(command, args, true);
    }

    /***
     * Sends an SMTP command to the server, waits for a reply and returns the
     * numerical response code.  After invocation, for more detailed
     * information, the actual reply text can be accessed by calling
     * <a href="#getReplyString"> getReplyString </a> or 
     * <a href="#getReplyStrings"> getReplyStrings </a>.
     * <p>
     * @param command  The SMTPCommand constant corresponding to the SMTP command
     *                 to send.
     * @param args The arguments to the SMTP command.  If this parameter is
     *             set to null, then the command is sent with no argument.
     * @return The integer value of the SMTP reply code returned by the server
     *         in response to the command.
     * @exception SMTPConnectionClosedException
     *      If the SMTP server prematurely closes the connection as a result
     *      of the client being idle or some other reason causing the server
     *      to send SMTP reply code 421.  This exception may be caught either
     *      as an IOException or independently as itself.
     * @exception IOException  If an I/O error occurs while either sending the
     *      command or receiving the server reply.
     ***/
    public int sendCommand(int command, String args) throws IOException {
        return this.sendCommand(SMTPCommand._commands[command], args);
    }

    /***
     * Sends an SMTP command with no arguments to the server, waits for a
     * reply and returns the numerical response code.  After invocation, for
     * more detailed information, the actual reply text can be accessed by
     * calling <a href="#getReplyString"> getReplyString </a> or 
     * <a href="#getReplyStrings"> getReplyStrings </a>.
     * <p>
     * @param command  The text representation of the  SMTP command to send.
     * @return The integer value of the SMTP reply code returned by the server
     *         in response to the command.
     * @exception SMTPConnectionClosedException
     *      If the SMTP server prematurely closes the connection as a result
     *      of the client being idle or some other reason causing the server
     *      to send SMTP reply code 421.  This exception may be caught either
     *      as an IOException or independently as itself.
     * @exception IOException  If an I/O error occurs while either sending the
     *      command or receiving the server reply.
     ***/
    public int sendCommand(String command) throws IOException {
        return this.sendCommand(command, null);
    }

    /***
     * Sends an SMTP command with no arguments to the server, waits for a
     * reply and returns the numerical response code.  After invocation, for
     * more detailed information, the actual reply text can be accessed by
     * calling <a href="#getReplyString"> getReplyString </a> or 
     * <a href="#getReplyStrings"> getReplyStrings </a>.
     * <p>
     * @param command  The SMTPCommand constant corresponding to the SMTP command
     *                 to send.
     * @return The integer value of the SMTP reply code returned by the server
     *         in response to the command.
     * @exception SMTPConnectionClosedException
     *      If the SMTP server prematurely closes the connection as a result
     *      of the client being idle or some other reason causing the server
     *      to send SMTP reply code 421.  This exception may be caught either
     *      as an IOException or independently as itself.
     * @exception IOException  If an I/O error occurs while either sending the
     *      command or receiving the server reply.
     ***/
    public int sendCommand(int command) throws IOException {
        return this.sendCommand(command, null);
    }

    /***
     * Returns the integer value of the reply code of the last SMTP reply.
     * You will usually only use this method after you connect to the
     * SMTP server to check that the connection was successful since
     * <code> connect </code> is of type void.
     * <p>
     * @return The integer value of the reply code of the last SMTP reply.
     ***/
    public int getReplyCode() {
        return this._replyCode;
    }

    /***
     * Fetches a reply from the SMTP server and returns the integer reply
     * code.  After calling this method, the actual reply text can be accessed
     * from either  calling <a href="#getReplyString"> getReplyString </a> or 
     * <a href="#getReplyStrings"> getReplyStrings </a>.  Only use this
     * method if you are implementing your own SMTP client or if you need to
     * fetch a secondary response from the SMTP server.
     * <p>
     * @return The integer value of the reply code of the fetched SMTP reply.
     * @exception SMTPConnectionClosedException
     *      If the SMTP server prematurely closes the connection as a result
     *      of the client being idle or some other reason causing the server
     *      to send SMTP reply code 421.  This exception may be caught either
     *      as an IOException or independently as itself.
     * @exception IOException  If an I/O error occurs while receiving the
     *                         server reply.
     ***/
    public int getReply() throws IOException {
        this.__getReply();
        return this._replyCode;
    }

    /***
     * Returns the lines of text from the last SMTP server response as an array
     * of strings, one entry per line.  The end of line markers of each are
     * stripped from each line.
     * <p>
     * @return The lines of text from the last SMTP response as an array.
     ***/
    public String[] getReplyStrings() {
        String[] lines;
        lines = new String[this._replyLines.size()];
        this._replyLines.copyInto(lines);
        return lines;
    }

    /***
     * Returns the entire text of the last SMTP server response exactly
     * as it was received, including all end of line markers in NETASCII
     * format.
     * <p>
     * @return The entire text from the last SMTP response as a String.
     ***/
    public String getReplyString() {
        Enumeration enumReply;
        StringBuffer buffer;
        if (!this._newReplyString) return this._replyString;
        buffer = new StringBuffer(256);
        enumReply = this._replyLines.elements();
        while (enumReply.hasMoreElements()) {
            buffer.append((String) enumReply.nextElement());
            buffer.append(SocketClient.NETASCII_EOL);
        }
        this._newReplyString = false;
        return (this._replyString = buffer.toString());
    }

    /***
     * A convenience method to send the SMTP HELO command to the server,
     * receive the reply, and return the reply code.
     * <p>
     * @param hostname The hostname of the sender.
     * @return The reply code received from the server.
     * @exception SMTPConnectionClosedException
     *      If the SMTP server prematurely closes the connection as a result
     *      of the client being idle or some other reason causing the server
     *      to send SMTP reply code 421.  This exception may be caught either
     *      as an IOException or independently as itself.
     * @exception IOException  If an I/O error occurs while either sending the
     *      command or receiving the server reply.
     ***/
    public int helo(String hostname) throws IOException {
        return this.sendCommand(SMTPCommand.HELO, hostname);
    }

    /***
     * A convenience method to send the SMTP MAIL command to the server,
     * receive the reply, and return the reply code.
     * <p>
     * @param reversePath The reverese path.
     * @return The reply code received from the server.
     * @exception SMTPConnectionClosedException
     *      If the SMTP server prematurely closes the connection as a result
     *      of the client being idle or some other reason causing the server
     *      to send SMTP reply code 421.  This exception may be caught either
     *      as an IOException or independently as itself.
     * @exception IOException  If an I/O error occurs while either sending the
     *      command or receiving the server reply.
     ***/
    public int mail(String reversePath) throws IOException {
        return this.__sendCommand(SMTPCommand.MAIL, reversePath, false);
    }

    /***
     * A convenience method to send the SMTP RCPT command to the server,
     * receive the reply, and return the reply code.
     * <p>
     * @param forwardPath The forward path.
     * @return The reply code received from the server.
     * @exception SMTPConnectionClosedException
     *      If the SMTP server prematurely closes the connection as a result
     *      of the client being idle or some other reason causing the server
     *      to send SMTP reply code 421.  This exception may be caught either
     *      as an IOException or independently as itself.
     * @exception IOException  If an I/O error occurs while either sending the
     *      command or receiving the server reply.
     ***/
    public int rcpt(String forwardPath) throws IOException {
        return this.__sendCommand(SMTPCommand.RCPT, forwardPath, false);
    }

    /***
     * A convenience method to send the SMTP DATA command to the server,
     * receive the reply, and return the reply code.
     * <p>
     * @return The reply code received from the server.
     * @exception SMTPConnectionClosedException
     *      If the SMTP server prematurely closes the connection as a result
     *      of the client being idle or some other reason causing the server
     *      to send SMTP reply code 421.  This exception may be caught either
     *      as an IOException or independently as itself.
     * @exception IOException  If an I/O error occurs while either sending the
     *      command or receiving the server reply.
     ***/
    public int data() throws IOException {
        return this.sendCommand(SMTPCommand.DATA);
    }

    /***
     * A convenience method to send the SMTP SEND command to the server,
     * receive the reply, and return the reply code.
     * <p>
     * @param reversePath The reverese path.
     * @return The reply code received from the server.
     * @exception SMTPConnectionClosedException
     *      If the SMTP server prematurely closes the connection as a result
     *      of the client being idle or some other reason causing the server
     *      to send SMTP reply code 421.  This exception may be caught either
     *      as an IOException or independently as itself.
     * @exception IOException  If an I/O error occurs while either sending the
     *      command or receiving the server reply.
     ***/
    public int send(String reversePath) throws IOException {
        return this.sendCommand(SMTPCommand.SEND, reversePath);
    }

    /***
     * A convenience method to send the SMTP SOML command to the server,
     * receive the reply, and return the reply code.
     * <p>
     * @param reversePath The reverese path.
     * @return The reply code received from the server.
     * @exception SMTPConnectionClosedException
     *      If the SMTP server prematurely closes the connection as a result
     *      of the client being idle or some other reason causing the server
     *      to send SMTP reply code 421.  This exception may be caught either
     *      as an IOException or independently as itself.
     * @exception IOException  If an I/O error occurs while either sending the
     *      command or receiving the server reply.
     ***/
    public int soml(String reversePath) throws IOException {
        return this.sendCommand(SMTPCommand.SOML, reversePath);
    }

    /***
     * A convenience method to send the SMTP SAML command to the server,
     * receive the reply, and return the reply code.
     * <p>
     * @param reversePath The reverese path.
     * @return The reply code received from the server.
     * @exception SMTPConnectionClosedException
     *      If the SMTP server prematurely closes the connection as a result
     *      of the client being idle or some other reason causing the server
     *      to send SMTP reply code 421.  This exception may be caught either
     *      as an IOException or independently as itself.
     * @exception IOException  If an I/O error occurs while either sending the
     *      command or receiving the server reply.
     ***/
    public int saml(String reversePath) throws IOException {
        return this.sendCommand(SMTPCommand.SAML, reversePath);
    }

    /***
     * A convenience method to send the SMTP RSET command to the server,
     * receive the reply, and return the reply code.
     * <p>
     * @return The reply code received from the server.
     * @exception SMTPConnectionClosedException
     *      If the SMTP server prematurely closes the connection as a result
     *      of the client being idle or some other reason causing the server
     *      to send SMTP reply code 421.  This exception may be caught either
     *      as an IOException or independently as itself.
     * @exception IOException  If an I/O error occurs while either sending the
     *      command or receiving the server reply.
     ***/
    public int rset() throws IOException {
        return this.sendCommand(SMTPCommand.RSET);
    }

    /***
     * A convenience method to send the SMTP VRFY command to the server,
     * receive the reply, and return the reply code.
     * <p>
     * @param user The user address to verify.
     * @return The reply code received from the server.
     * @exception SMTPConnectionClosedException
     *      If the SMTP server prematurely closes the connection as a result
     *      of the client being idle or some other reason causing the server
     *      to send SMTP reply code 421.  This exception may be caught either
     *      as an IOException or independently as itself.
     * @exception IOException  If an I/O error occurs while either sending the
     *      command or receiving the server reply.
     ***/
    public int vrfy(String user) throws IOException {
        return this.sendCommand(SMTPCommand.VRFY, user);
    }

    /***
     * A convenience method to send the SMTP VRFY command to the server,
     * receive the reply, and return the reply code.
     * <p>
     * @param name The name to expand.
     * @return The reply code received from the server.
     * @exception SMTPConnectionClosedException
     *      If the SMTP server prematurely closes the connection as a result
     *      of the client being idle or some other reason causing the server
     *      to send SMTP reply code 421.  This exception may be caught either
     *      as an IOException or independently as itself.
     * @exception IOException  If an I/O error occurs while either sending the
     *      command or receiving the server reply.
     ***/
    public int expn(String name) throws IOException {
        return this.sendCommand(SMTPCommand.EXPN, name);
    }

    /***
     * A convenience method to send the SMTP HELP command to the server,
     * receive the reply, and return the reply code.
     * <p>
     * @return The reply code received from the server.
     * @exception SMTPConnectionClosedException
     *      If the SMTP server prematurely closes the connection as a result
     *      of the client being idle or some other reason causing the server
     *      to send SMTP reply code 421.  This exception may be caught either
     *      as an IOException or independently as itself.
     * @exception IOException  If an I/O error occurs while either sending the
     *      command or receiving the server reply.
     ***/
    public int help() throws IOException {
        return this.sendCommand(SMTPCommand.HELP);
    }

    /***
     * A convenience method to send the SMTP HELP command to the server,
     * receive the reply, and return the reply code.
     * <p>
     * @param command  The command name on which to request help.
     * @return The reply code received from the server.
     * @exception SMTPConnectionClosedException
     *      If the SMTP server prematurely closes the connection as a result
     *      of the client being idle or some other reason causing the server
     *      to send SMTP reply code 421.  This exception may be caught either
     *      as an IOException or independently as itself.
     * @exception IOException  If an I/O error occurs while either sending the
     *      command or receiving the server reply.
     ***/
    public int help(String command) throws IOException {
        return this.sendCommand(SMTPCommand.HELP, command);
    }

    /***
     * A convenience method to send the SMTP NOOP command to the server,
     * receive the reply, and return the reply code.
     * <p>
     * @return The reply code received from the server.
     * @exception SMTPConnectionClosedException
     *      If the SMTP server prematurely closes the connection as a result
     *      of the client being idle or some other reason causing the server
     *      to send SMTP reply code 421.  This exception may be caught either
     *      as an IOException or independently as itself.
     * @exception IOException  If an I/O error occurs while either sending the
     *      command or receiving the server reply.
     ***/
    public int noop() throws IOException {
        return this.sendCommand(SMTPCommand.NOOP);
    }

    /***
     * A convenience method to send the SMTP TURN command to the server,
     * receive the reply, and return the reply code.
     * <p>
     * @return The reply code received from the server.
     * @exception SMTPConnectionClosedException
     *      If the SMTP server prematurely closes the connection as a result
     *      of the client being idle or some other reason causing the server
     *      to send SMTP reply code 421.  This exception may be caught either
     *      as an IOException or independently as itself.
     * @exception IOException  If an I/O error occurs while either sending the
     *      command or receiving the server reply.
     ***/
    public int turn() throws IOException {
        return this.sendCommand(SMTPCommand.TURN);
    }

    /***
     * A convenience method to send the SMTP QUIT command to the server,
     * receive the reply, and return the reply code.
     * <p>
     * @return The reply code received from the server.
     * @exception SMTPConnectionClosedException
     *      If the SMTP server prematurely closes the connection as a result
     *      of the client being idle or some other reason causing the server
     *      to send SMTP reply code 421.  This exception may be caught either
     *      as an IOException or independently as itself.
     * @exception IOException  If an I/O error occurs while either sending the
     *      command or receiving the server reply.
     ***/
    public int quit() throws IOException {
        return this.sendCommand(SMTPCommand.QUIT);
    }
}
