package jimo.spi.im.msn;

import jimo.spi.im.api.Buddy;
import jimo.spi.im.api.Conference;
import jimo.spi.im.api.Message;
import jimo.spi.im.api.MessageComponent;
import jimo.spi.im.api.SmileyComponent;
import jimo.spi.im.api.TextComponent;
import jimo.spi.im.api.URLComponent;
import jimo.spi.im.api.exception.IllegalStateException;
import jimo.spi.im.api.net.ProxyInfo;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A switchboard server is used for instant messaging. Both one to one messaging
 * and conferences are carried out here.
 */
class SwitchboardServer extends MsnServer {

    /**
	 * Event processor for this server.
	 */
    private EventProcessor processor;

    /**
	 * MsnProtocol instance that owns this server.
	 */
    private MsnProtocol protocol;

    /**
	 * A conference object representing this switchboard session.
	 */
    private Conference conf;

    /**
	 * Start a switchboard session for a specific conference. This constructor
	 * is used when the user logged in using JIMO starts a new SB session.
	 *
	 * @param protocol the MsnProtocol instance that owns this server.
	 * @param processor the event processor for this server.
	 * @param hash the hash to be used to connect to this session.
	 * @param host the host name to connect to.
	 * @param port the TCP/IP port to connect to.
	 * @param conf the conference object that represents this SB session.
	 * @param info proxy information to be used for connections.
	 * @throws UnknownHostException if host is not known.
	 * @throws IOException if an I/O error occurs while connecting to the host.
	 * @throws IllegalStateException if info is not initialized properly.
	 */
    SwitchboardServer(MsnProtocol protocol, EventProcessor processor, String hash, String host, int port, Conference conf, ProxyInfo info) throws IOException, IllegalStateException {
        this(protocol, processor, host, port, info, conf);
        String passport = conf.getHost().getUsername();
        Command usr = new Command("USR");
        usr.addParam(passport);
        usr.addParam(hash);
        sendToServer(usr, "processUSR");
    }

    /**
	 * Constructs a switchboard server with a specified event processor and proxy
	 * information. This constuctor is used when the user logged in using JIMO joins
	 * a SB session initiated by another buddy.
	 *
	 * @param protocol the MsnProtocol instance that owns this server.
	 * @param processor the event processor for this server.
	 * @param host the host name to connect to.
	 * @param port the TCP/IP port to connect to.
	 * @param info proxy information to be used for connections.
	 * @param conf the conference object that represents this SB session.
	 * @throws UnknownHostException if host is not known.
	 * @throws IOException if an I/O error occurs while connecting to the host.
	 * @throws IllegalStateException if info is not initialized properly.
	 */
    SwitchboardServer(MsnProtocol protocol, EventProcessor processor, String host, int port, ProxyInfo info, Conference conf) throws java.io.IOException, jimo.spi.im.api.exception.IllegalStateException {
        super(host, port, info);
        this.protocol = protocol;
        this.processor = processor;
        this.conf = conf;
    }

    /**
	 * Process the reply of the USR command.
	 *
	 * @param cmd the reply from the server.
	 */
    void processUSR(AbstractCommand cmd) {
        if (cmd instanceof Command) {
            if ("USR".equals(cmd.getType()) && "OK".equals(cmd.getParam(0))) {
                Buddy[] buddies = conf.getParticipants();
                for (int i = 0; i < buddies.length; i++) {
                    String username = buddies[i].getUsername();
                    if (!username.equals(this.protocol.getUsername())) {
                        Command cal = new Command("CAL");
                        cal.addParam(username);
                        sendToServer(cal, "doNothing");
                    }
                }
            }
        }
    }

    /**
	 * This method does nothing. It used for callback where we don't have
	 * anything to do.
	 */
    void doNothing(AbstractCommand cmd) {
    }

    /**
	 * Join the switchboard session by sending a ANS command.
	 *
	 * @param authString authentication string to be used for joining.
	 * @param sessionID session id to be used for joining
	 */
    void join(String authString, String sessionID) {
        Command ans = new Command("ANS");
        ans.addParam(protocol.getUsername());
        ans.addParam(authString);
        ans.addParam(sessionID);
        sendToServer(ans, "processANS");
    }

    /**
	 * Process the reply of the ANS command.
	 *
	 * @param cmd the reply from the server.
	 */
    void processANS(AbstractCommand cmd) {
        if (cmd instanceof Command) {
            String type = cmd.getType();
            if ("IRO".equals(type)) {
                Buddy buddy = new Buddy(conf.getProtocol(), cmd.getParam(2));
                buddy.setAlias(Util.urlDecode(cmd.getParam(3)));
                conf.addParticipant(buddy);
            } else if ("ANS".equals(type) && "OK".equals(cmd.getParam(0))) {
                Buddy[] buddies = conf.getParticipants();
                for (int i = 0; i < buddies.length; i++) processor.conferenceParticipantJoined(conf, buddies[i]);
                Buddy myself = new Buddy(protocol, protocol.getUsername());
                conf.addParticipant(myself);
            }
        }
    }

    private static Pattern xMmsImFormatPattern = Pattern.compile("FN\\s*=\\s*([^;]*);\\s*EF\\s*=\\s*([^;]*);\\s*CO\\s*=\\s*([^;]*);");

    /**
	 * Processes messages received from MSN server.
	 *
	 * @param msg the message received from server.
	 */
    protected synchronized void processMessage(MsnMessage msg) {
        String type = msg.getHeaderField("Content-Type");
        if ("text/plain".equals(type) || "text/plain; charset=UTF-8".equals(type)) {
            Font font = new Font(null, Font.PLAIN, 10);
            Color color = Color.black;
            String fontAndColor = msg.getHeaderField("X-MMS-IM-Format");
            if (fontAndColor != null) {
                Matcher m = xMmsImFormatPattern.matcher(fontAndColor);
                if (m.lookingAt()) {
                    String fontName = m.group(1);
                    String effects = m.group(2);
                    String bgr = m.group(3);
                    if (fontName != null && !fontName.trim().equals("")) font = new Font(Util.urlDecode(fontName), Font.PLAIN, 10);
                    if (effects != null && !effects.trim().equals("")) {
                        int style = effects.indexOf('B') != -1 ? Font.BOLD : Font.PLAIN;
                        style |= effects.indexOf('I') != -1 ? Font.ITALIC : 0;
                        font = font.deriveFont(style);
                    }
                }
            }
            String body = msg.getBody();
            if (body != null) {
                Message apiMessage = Util.parseMSNMessage(body, font, color);
                String passport = msg.getParam(0);
                String alias = msg.getParam(1);
                Buddy buddy = new Buddy(protocol, passport);
                buddy.setAlias(alias);
                processor.conferenceMessageReceived(this.conf, buddy, apiMessage);
            }
        }
    }

    /**
	 * Processes asynchronous commands received from MSN server.
	 *
	 * @param cmd the command received from server.
	 */
    protected void processAsyncCommand(AbstractCommand cmd) {
        if (cmd instanceof Command) {
            String type = cmd.getType();
            if ("BYE".equals(type)) {
                String passport = cmd.getParam(0);
                if (passport != null) {
                    Buddy buddy = new Buddy(protocol, passport);
                    conf.removeParticipant(buddy);
                    processor.conferenceParticipantLeft(conf, buddy);
                    if (conf.getParticipants().length == 1) {
                        processor.conferenceClosed(conf);
                        try {
                            protocol.quitConference(conf);
                        } catch (IllegalStateException e) {
                        }
                    }
                }
            } else if ("JOI".equals(type)) {
                String passport = cmd.getParam(0);
                String alias = Util.urlDecode(cmd.getParam(1));
                Buddy buddy = new Buddy(this.protocol, passport);
                buddy.setAlias(alias);
                this.conf.addParticipant(buddy);
                processor.conferenceParticipantJoined(this.conf, buddy);
            }
        }
    }

    /**
	 * Send a message to this conference.
	 *
	 * @param message the message to be sent.
	 */
    void sendMessage(Message message) {
        MsnMessage mmsg = messageToMSNMessage(message);
        sendToServer(mmsg, "doNothing");
    }

    /**
	 * Converts a JIMO message to MSN Message.
	 *
	 * @param message the JIMO message to be converted
	 * @return the equivalent MSN message
	 */
    private MsnMessage messageToMSNMessage(Message message) {
        Font font = null;
        Color color = null;
        StringBuffer buff = new StringBuffer();
        Enumeration e = message.getComponents();
        while (e.hasMoreElements()) {
            MessageComponent comp = (MessageComponent) e.nextElement();
            if (comp instanceof TextComponent) {
                TextComponent text = (TextComponent) comp;
                if (font == null) font = text.getFont();
                if (color == null) color = text.getColor();
                buff.append(text.getSequence());
            } else if (comp instanceof SmileyComponent) {
                SmileyComponent smiley = (SmileyComponent) comp;
                buff.append(smiley.getText());
            } else if (comp instanceof URLComponent) {
                URLComponent url = (URLComponent) comp;
                buff.append(url.getLinkURL());
            }
        }
        return new MsnMessage(font, color, buff.toString());
    }

    /**
	 * Check whether this is a one to one IM session.
	 *
	 * @param buddy the other buddy of the requested session.
	 * @return <code>true</code>, if this SB session is with the single buddy specified.
	 */
    boolean isIMSession(Buddy buddy) {
        Buddy[] buddies = conf.getParticipants();
        if (buddies.length != 2) return false; else if (buddies[0].equals(buddy) || buddies[1].equals(buddy)) return true; else return false;
    }

    /**
	 * Invoked when the associated reader thread exits abnormally. This
	 * method shuts down the IM session.
	 */
    protected void readerExited() {
        shutdown();
        protocol.switchBoardTerminated(this);
    }

    /**
	 * Invoked when the associated writer thread exits abnormally. This
	 * method shuts down the IM session.
	 */
    protected void writerExited() {
        shutdown();
        protocol.switchBoardTerminated(this);
    }
}
