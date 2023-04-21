package org.speakmon.coffeehouse.protocol;

import org.speakmon.coffeehouse.CoffeehousePrefs;
import java.util.regex.*;

/**
 * This class models a message from the user to send to the IRC server.
 *
 * Created: Mon Nov 11 00:48:06 2002
 *
 * @author <a href="mailto:ben@speakmon.org">Ben Speakmon</a>
 * @version
 */
class ClientMessage extends IRCMessage {

    /**
     * Buffer for the parts of the IRC message being constructed here.
     */
    StringBuffer messageBuf;

    private final CoffeehousePrefs prefs = CoffeehousePrefs.getInstance();

    protected ClientMessage() {
    }

    /**
     * Creates an IRCMessage ready to send to the IRC server.
     *
     * @param userInput the user input from the front end
     * @param channelName the name of the channel associated with this command
     */
    protected ClientMessage(String userInput, String channelName) {
        serverLine = userInput;
        messageBuf = new StringBuffer(64);
        if (channelName == "") {
            messageBuf.append(userInput.substring(1));
            return;
        }
        if (userInput.startsWith("/")) {
            if (userInput.startsWith("/me")) {
                int actionStart = userInput.indexOf(" ") + 1;
                IRCMessage message = new CtcpClientMessage(channelName, "ACTION", userInput.substring(actionStart));
                messageBuf.append(message.toString());
            } else if (userInput.startsWith("/msg")) {
                Pattern msgPattern = Pattern.compile("/msg (.+?) (.+)");
                Matcher matcher = msgPattern.matcher(userInput);
                if (matcher.matches()) {
                    String targetNick = matcher.group(1);
                    String message = matcher.group(2);
                    messageBuf.append("PRIVMSG ").append(targetNick).append(" :");
                    messageBuf.append(message);
                } else {
                }
            } else if (userInput.startsWith("/part")) {
                Pattern partPattern = Pattern.compile("/part ?(.+)?");
                Matcher partMatcher = partPattern.matcher(userInput);
                if (partMatcher.matches()) {
                    messageBuf.append("PART ");
                    messageBuf.append(channelName).append(" :");
                    if (partMatcher.group(1) == null) {
                        messageBuf.append(prefs.defaultPartMsg());
                    } else {
                        messageBuf.append(partMatcher.group(1));
                    }
                } else {
                }
            } else if (userInput.startsWith("/quit")) {
                Pattern quitPattern = Pattern.compile("/quit ?(.+)?");
                Matcher quitMatcher = quitPattern.matcher(userInput);
                if (quitMatcher.matches()) {
                    messageBuf.append("QUIT :");
                    if (quitMatcher.group(1) == null) {
                        messageBuf.append(prefs.defaultQuitMsg());
                    } else {
                        messageBuf.append(quitMatcher.group(1));
                    }
                } else {
                }
            } else if (userInput.startsWith("/ctcp")) {
                Pattern ctcpPattern = Pattern.compile("^/ctcp (\\S+?) (\\S+?)(?:(?: )(\\S+?))?");
                Matcher ctcpMatcher = ctcpPattern.matcher(userInput);
                if (ctcpMatcher.matches()) {
                    IRCMessage message = new CtcpClientMessage(ctcpMatcher.group(1), ctcpMatcher.group(2), ctcpMatcher.group(3));
                    messageBuf.append(message.toString());
                }
            } else {
                messageBuf.append(userInput.substring(1));
            }
        } else {
            messageBuf.append("PRIVMSG ").append(channelName);
            messageBuf.append(" :").append(userInput);
        }
    }

    /**
     * Returns a string for this message that's ready to send to the
     * IRC server.
     *
     * @return a <code>String</code> value
     */
    public String toString() {
        return messageBuf.toString();
    }

    /**
     * Returns true, since this message contains a command.
     *
     * @return a <code>boolean</code> value
     */
    public boolean isNamed() {
        return true;
    }
}
