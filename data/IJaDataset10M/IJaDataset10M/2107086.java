package org.speakmon.babble.ctcp;

import java.util.ArrayList;
import java.util.List;
import org.speakmon.babble.CommandBuilder;
import org.speakmon.babble.Connection;
import org.speakmon.babble.Rfc2812Util;

/**
 *
 * @author  speakmon
 */
public class CtcpSender extends CommandBuilder {

    private List pingList;

    /** Creates a new instance of CtcpSender */
    protected CtcpSender(Connection connection) {
        super(connection);
        pingList = new ArrayList();
    }

    protected boolean isMyRequest(String timestamp) {
        return pingList.contains(timestamp);
    }

    protected void replyReceived(String timestamp) {
        pingList.remove(timestamp);
    }

    public void ctcpReply(String command, String nick, String reply) {
        synchronized (this) {
            if (!Rfc2812Util.isValidNick(connection, nick)) {
                clearBuffer();
                throw new IllegalArgumentException(nick + " is not a valid nick");
            }
            if (reply == null || reply.length() < 1) {
                clearBuffer();
                throw new IllegalArgumentException("reply cannot be null or empty");
            }
            if (command == null || command.length() < 1) {
                clearBuffer();
                throw new IllegalArgumentException("the CTCP command cannot be null or empty");
            }
            int max = MAX_COMMAND_SIZE - 14 - nick.length() - command.length();
            if (reply.length() > max) {
                reply = reply.substring(0, max);
            }
            StringBuffer commandBuffer = new StringBuffer(MAX_COMMAND_SIZE);
            commandBuffer.append(CTCP_QUOTE);
            commandBuffer.append(command.toUpperCase());
            commandBuffer.append(SPACE);
            commandBuffer.append(CTCP_QUOTE);
            sendMessage("NOTICE", nick, commandBuffer.toString());
        }
    }

    public void ctcpRequest(String command, String nick) {
        synchronized (this) {
            if (!Rfc2812Util.isValidNick(connection, nick)) {
                clearBuffer();
                throw new IllegalArgumentException(nick + " is not a valid nick");
            }
            if (command == null || command.length() < 1) {
                clearBuffer();
                throw new IllegalArgumentException("the CTCP command cannot be null or empty");
            }
            StringBuffer request = new StringBuffer(16);
            request.append(CTCP_QUOTE);
            request.append(command.toUpperCase());
            request.append(CTCP_QUOTE);
            sendMessage("PRIVMSG", nick, request.toString());
        }
    }

    public void ctcpPingReply(String nick, String timestamp) {
        synchronized (this) {
            if (!Rfc2812Util.isValidNick(connection, nick)) {
                clearBuffer();
                throw new IllegalArgumentException(nick + " is not a valid nick");
            }
            if (timestamp == null || timestamp.length() < 1) {
                clearBuffer();
                throw new IllegalArgumentException("timestamp cannot be null or empty");
            }
            StringBuffer pingResponse = new StringBuffer(64);
            pingResponse.append(CTCP_QUOTE);
            pingResponse.append(CtcpUtil.PING);
            pingResponse.append(SPACE);
            pingResponse.append(timestamp);
            pingResponse.append(CTCP_QUOTE);
            sendMessage("NOTICE", nick, pingResponse.toString());
        }
    }

    public void ctcpPingRequest(String nick, String timestamp) {
        synchronized (this) {
            if (!Rfc2812Util.isValidNick(connection, nick)) {
                clearBuffer();
                throw new IllegalArgumentException(nick + " is not a valid nick");
            }
            pingList.add(timestamp);
            StringBuffer pingRequest = new StringBuffer(16);
            pingRequest.append(CTCP_QUOTE);
            pingRequest.append(CtcpUtil.PING);
            pingRequest.append(SPACE);
            pingRequest.append(timestamp);
            pingRequest.append(CTCP_QUOTE);
            sendMessage("PRIVMSG", nick, pingRequest.toString());
        }
    }
}
