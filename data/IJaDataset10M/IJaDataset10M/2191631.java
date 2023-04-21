package net.ardvaark.jackbot;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * A message from the IRC server.
 * 
 * @author Brian Vargas
 * @version $Revision: 55 $ $Date: 2008-04-13 14:36:35 -0400 (Sun, 13 Apr 2008) $
 */
public final class IRCMessage {

    public static final int MSG_TYPE_IDLE = 0;

    public static final int MSG_TYPE_IRCMESSAGE = 1;

    public static final int MSG_TYPE_CONNECTION_LOST = 2;

    /**
     * Parses a string as an IRC message and returns a new IRCMessage.
     * 
     * @param msg The message to be parsed. This is usally directly from the IRC
     *        server.
     * @return A parsed IRCMessage. If <code>msg</code> is <code>null</code>,
     *         then <code>parseMessage(String)</code> will return
     *         <code>null</code>.
     */
    public static final IRCMessage parseMessage(String msg) {
        if (msg == null || msg.length() == 0) {
            throw new IllegalArgumentException("msg: " + msg);
        }
        IRCMessage newMsg = null;
        if (msg != null) {
            String prefix = null;
            String command = null;
            String params = null;
            int nSpaceIndex = 0;
            if (msg.charAt(0) == ':') {
                nSpaceIndex = msg.indexOf(' ');
                prefix = msg.substring(1, nSpaceIndex);
                msg = msg.substring(nSpaceIndex + 1);
            }
            nSpaceIndex = msg.indexOf(' ');
            command = msg.substring(0, nSpaceIndex);
            params = msg.substring(nSpaceIndex + 1);
            newMsg = new IRCMessage(prefix, command, params);
            if (command.equalsIgnoreCase(IRC.CMD_PRIVMSG) || command.equalsIgnoreCase(IRC.CMD_NOTICE)) {
                if (params.indexOf(IRC.CTCP_DELIM) >= 0) {
                    StringBuffer paramsBuffer = new StringBuffer(params);
                    newMsg.ctcpMessages = CTCPMessage.parseMessages(prefix, command, paramsBuffer);
                    newMsg.paramsString = paramsBuffer.toString();
                }
            }
        }
        return newMsg;
    }

    /**
     * Constructs a new IRCMessage.
     * 
     * @param prefix The prefix.
     * @param command The command.
     * @param params The parameters string, whitespace seperated.
     */
    protected IRCMessage(String prefix, String command, String params) {
        this.prefix = prefix;
        this.command = command.toUpperCase();
        this.paramsString = params;
        this.ctcpMessages = new ArrayList<CTCPMessage>();
        this.messageType = IRCMessage.MSG_TYPE_IRCMESSAGE;
    }

    /**
     * Constructs a new IRCMessage that has no message, but a non-default
     * message type.
     * 
     * @param messageType The type of the message.
     */
    public IRCMessage(int messageType) {
        this.prefix = "";
        this.command = "";
        this.paramsString = "";
        this.ctcpMessages = new ArrayList<CTCPMessage>();
        this.messageType = messageType;
    }

    /**
     * Gets the message type.
     * 
     * @return The type of the message.
     */
    public int getMessageType() {
        return this.messageType;
    }

    /**
     * Gets the prefix.
     * 
     * @return The prefix, or <code>null</code> if one does not exist.
     */
    public String getPrefix() {
        return this.prefix;
    }

    /**
     * Gets the command.
     * 
     * @return The command.
     */
    public String getCommand() {
        return this.command;
    }

    /**
     * Gets the unparsed parameter string.
     * 
     * @return The unparsed parameter string.
     */
    public String getParams() {
        return this.paramsString;
    }

    /**
     * Gets a specific parameter from the parameter list, zero-based index. This
     * will cause the parameters to be parsed if they have not already been.
     * 
     * @return The parameter at the given index.
     * @see #getParamCount()
     * @param index The index of the parameter to get.
     */
    public String getParam(int index) {
        if (this.params == null) {
            this.parseParams();
        }
        return this.params.get(index);
    }

    /**
     * Gets the last parameter in the parameters. The last parameter is
     * significant in an IRC message because it is the only parameter that can
     * have a space character in it. Thus, most chat comes through this
     * parameter.
     * 
     * @return The last parameter of the IRC message.
     */
    public String getLastParam() {
        if (this.params == null) {
            this.parseParams();
        }
        return this.params.get(this.params.size() - 1);
    }

    /**
     * Returns the number of parameters in the parameter list. This will cause
     * the parameters to be parsed if they have not already been.
     * 
     * @return The count of the parameters in the command.
     */
    public int getParamCount() {
        if (this.params == null) {
            this.parseParams();
        }
        return this.params.size();
    }

    /**
     * Gets the number of CTCP messages that were embedded in this IRC message.
     * 
     * @return The number of CTCP messages.
     * @see #getCTCPMessage(int)
     */
    public int getCTCPMessageCount() {
        return this.ctcpMessages == null ? 0 : this.ctcpMessages.size();
    }

    /**
     * Gets the <CODE>CTCPMessage</CODE> object embedded in this
     * <CODE>IRCMessage</CODE> at the given index. This must be greater than
     * or equal to 0 and less than the value returned by
     * {@link #getCTCPMessageCount getCTCPMessageCount()}.
     * 
     * @param item The index of the <CODE>CTCPMessage</CODE> to get.
     * @return The <CODE>CTCPMessasge</CODE> at the given index.
     * @see #getCTCPMessageCount()
     */
    public CTCPMessage getCTCPMessage(int item) {
        return this.ctcpMessages == null ? null : this.ctcpMessages.get(item);
    }

    /**
     * Gets a string representation of the message.
     * 
     * @return A string representation of the message.
     */
    @Override
    public String toString() {
        return "prefix:" + this.prefix + " command:" + this.command + " params:" + this.paramsString;
    }

    /**
     * Parses the parameter string.
     */
    private void parseParams() {
        if (this.params == null) {
            this.params = new Vector<String>();
            String lastParam = null;
            String ps = this.paramsString;
            int colonIndex = ps.indexOf(':');
            if (colonIndex >= 0) {
                lastParam = colonIndex < 0 ? null : ps.substring(colonIndex + 1);
                ps = ps.substring(0, colonIndex);
            }
            StringTokenizer tok = new StringTokenizer(ps, " ");
            while (tok.hasMoreTokens()) {
                this.params.addElement(tok.nextToken());
            }
            if (lastParam != null) {
                this.params.addElement(lastParam);
            }
        }
    }

    /**
     * The prefix.
     */
    private String prefix;

    /**
     * The command.
     */
    private String command;

    /**
     * The unparsed parameter string.
     */
    private String paramsString;

    /**
     * The collection of unparsed parameters.
     */
    private Vector<String> params = null;

    /**
     * A collection of all the CTCP messages in this message.
     */
    private ArrayList<CTCPMessage> ctcpMessages = null;

    /**
     * The type of the message.
     */
    private int messageType;
}
