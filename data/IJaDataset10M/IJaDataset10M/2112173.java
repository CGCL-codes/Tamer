package net.sf.babble.plugins.ctcp;

import java.util.regex.*;
import net.sf.babble.Connection;
import net.sf.babble.Rfc2812Util;
import net.sf.babble.UserInfo;
import net.sf.babble.plugins.ctcp.events.CtcpPingReplyEvent;
import net.sf.babble.plugins.ctcp.events.CtcpPingReplyEventHandler;
import net.sf.babble.plugins.ctcp.events.CtcpPingRequestEvent;
import net.sf.babble.plugins.ctcp.events.CtcpPingRequestEventHandler;
import net.sf.babble.plugins.ctcp.events.CtcpReplyEvent;
import net.sf.babble.plugins.ctcp.events.CtcpReplyEventHandler;
import net.sf.babble.plugins.ctcp.events.CtcpRequestEvent;
import net.sf.babble.plugins.ctcp.events.CtcpRequestEventHandler;

/**
 * @version $Id: CtcpListener.java 233 2004-07-11 05:30:27Z speakmon $
 * @author Ben Speakmon
 */
public class CtcpListener {

    private Connection connection;

    private static final CtcpEventManager eventManager = CtcpEventManager.getInstance();

    private static final Pattern ctcpPattern;

    private static final String ctcpTypes;

    private final int NAME = 0;

    private final int COMMAND = 1;

    private final int TEXT = 2;

    static {
        ctcpTypes = "(FINGER|USERINFO|VERSION|SOURCE|CLIENTINFO|ERRMSG|PING|TIME)";
        ctcpPattern = Pattern.compile(":([^ ]+) [A-Z]+ [^:]+:" + ctcpTypes + "([^]*)");
    }

    /** Creates a new instance of CtcpListener */
    protected CtcpListener(Connection connection) {
        this.connection = connection;
    }

    public void parse(String line) {
        String[] ctcpTokens = tokenizeMessage(line);
        if (ctcpTokens != null) {
            if (ctcpTokens[COMMAND].toUpperCase() == CtcpCommand.PING.toString()) {
                if (connection.getCtcpSender().isMyRequest(ctcpTokens[TEXT])) {
                    connection.getCtcpSender().replyReceived(ctcpTokens[TEXT]);
                    CtcpPingReplyEvent event = new CtcpPingReplyEvent(Rfc2812Util.userInfoFromString(ctcpTokens[NAME]));
                    event.setTimestamp(ctcpTokens[TEXT]);
                    eventManager.fireCtcpPingReplyEventHandlers(event);
                } else {
                    if (ctcpTokens[TEXT] != null && ctcpTokens[TEXT].length() < 1) {
                        CtcpPingRequestEvent event = new CtcpPingRequestEvent(Rfc2812Util.userInfoFromString(ctcpTokens[NAME]));
                        event.setTimestamp(ctcpTokens[TEXT]);
                        eventManager.fireCtcpPingRequestEventHandlers(event);
                    }
                }
            } else {
                if (isReply(ctcpTokens)) {
                    CtcpReplyEvent event = new CtcpReplyEvent(ctcpTokens[COMMAND].toUpperCase());
                    event.setWho(Rfc2812Util.userInfoFromString(ctcpTokens[NAME]));
                    event.setReply(ctcpTokens[TEXT]);
                    eventManager.fireCtcpReplyEventHandlers(event);
                } else {
                    CtcpRequestEvent event = new CtcpRequestEvent(ctcpTokens[COMMAND]);
                    event.setWho(Rfc2812Util.userInfoFromString(ctcpTokens[NAME]));
                    eventManager.fireCtcpRequestEventHandlers(event);
                }
            }
        }
    }

    private static String[] tokenizeMessage(String message) {
        Matcher matcher = ctcpPattern.matcher(message);
        return new String[] { matcher.group(1), matcher.group(2), matcher.group(3).trim() };
    }

    private boolean isReply(String[] tokens) {
        if (tokens[TEXT] != null) {
            return false;
        }
        return true;
    }

    public static boolean isCtcpMessage(String message) {
        Matcher matcher = ctcpPattern.matcher(message);
        return matcher.matches();
    }
}
