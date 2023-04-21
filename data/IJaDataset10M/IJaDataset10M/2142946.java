package de.teamwork.jaicwain.gui.swing;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
import de.teamwork.ctcp.*;
import de.teamwork.ctcp.msgutils.*;
import de.teamwork.irc.*;
import de.teamwork.irc.msgutils.*;
import de.teamwork.jaicwain.App;
import de.teamwork.jaicwain.gui.*;
import de.teamwork.jaicwain.options.*;
import de.teamwork.jaicwain.session.*;
import de.teamwork.jaicwain.session.irc.*;
import de.teamwork.util.swing.JHistoryTextField;

/**
 * Provides a nice panel with the typical server interface. Can be used with
 * <code>DefaultIRCSession</code>s and derived classes only. Provides a text
 * area and an input field.
 * 
 * @author Christoph Daniel Schulze
 * @version $Id: IRCSessionPanel.java 3 2003-01-07 14:16:38Z captainnuss $
 */
public class IRCSessionPanel extends ExtendedPanel implements ActionListener, IRCSessionListener, OptionsChangedListener, SessionStatusEventListener {

    /**
     * <code>DefaultIRCSession</code> this panel visualizes.
     */
    private DefaultIRCSession session = null;

    /**
     * Used to set the text styles.
     */
    private MutableAttributeSet currStyle = new SimpleAttributeSet();

    /**
     * Font style bold.
     */
    private boolean bold = false;

    /**
     * Font style italic.
     */
    private boolean italic = false;

    /**
     * Font style underlined.
     */
    private boolean underline = false;

    /**
     * IRC indexed colors.
     */
    public static final Color[] IRC_TEXT_COLORS = { Color.white, Color.black, new Color(0x00007f), new Color(0x009300), Color.red, new Color(0x7f0000), new Color(0x9c009c), new Color(0xfc7f00), Color.yellow, Color.green, new Color(0x009393), Color.cyan, Color.blue, Color.magenta, Color.darkGray, Color.lightGray };

    private JScrollPane logScrollPane;

    private JTextPane logTextPane;

    private JHistoryTextField commandField;

    /**
     * Creates a new <code>IRCSessionPanel</code> object and initializes it.
     * 
     * @param session <code>DefaultIRCSession</code> this panel shall display.
     */
    public IRCSessionPanel(DefaultIRCSession session) {
        super();
        if (session == null) throw new NullPointerException("session can't be null");
        this.session = session;
        session.addIRCSessionListener(this);
        session.addSessionStatusEventListener(this);
        createUI();
        adaptUI();
        setPanelTitle(session.getConnection().getRemoteAddress().toString());
        setPanelHeading(session.getConnection().getRemoteAddress().toString());
        setPanelDescription("");
        App.options.addOptionsChangedListener(this);
    }

    /**
     * Sets up the user interface.
     */
    private void createUI() {
        logTextPane = new JTextPane();
        logTextPane.setEditable(false);
        logScrollPane = new JScrollPane(logTextPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        commandField = new JHistoryTextField(App.options.getIntOption("gui", "historytextfield.size", 25));
        commandField.setEditable(true);
        commandField.setName("field");
        commandField.setVisible(true);
        commandField.addActionListener(this);
        TreeSet keys = new TreeSet();
        keys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.CTRL_MASK));
        commandField.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, keys);
        GridBagLayout g = new GridBagLayout();
        GridBagConstraints c = null;
        setLayout(g);
        c = new GridBagConstraints();
        c.weightx = 1.0f;
        c.weighty = 1.0f;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.BOTH;
        g.setConstraints(logScrollPane, c);
        add(logScrollPane);
        c = new GridBagConstraints();
        c.weightx = 1.0f;
        c.weighty = 0.0f;
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.SOUTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        g.setConstraints(commandField, c);
        add(commandField);
    }

    /**
     * As soon as the "gui" options package is changed, we'll have to update our
     * stuff.
     */
    private void adaptUI() {
        Font font = new Font(App.options.getStringOption("gui", "chattextarea.font.name", "Monospaced"), App.options.getIntOption("gui", "chattextarea.font.style", 0), App.options.getIntOption("gui", "chattextarea.font.size", 12));
        logTextPane.setFont(font);
        font = new Font(App.options.getStringOption("gui", "chattextfield.font.name", "Monospaced"), App.options.getIntOption("gui", "chattextfield.font.style", 0), App.options.getIntOption("gui", "chattextfield.font.size", 12));
        commandField.setFont(font);
        commandField.setHistorySize(App.options.getIntOption("gui", "historytextfield.size", 25));
    }

    /**
     * Returns this panel's session.
     * 
     * @return <code>DefaultIRCSession</code> this panel visualizes.
     */
    public DefaultIRCSession getSession() {
        return session;
    }

    /**
     * Returns an array of custom menu stuff related to this panel.
     * 
     * @return <code>JMenuItem</code> array or <code>null</code> if it doesn't
     *         have any.
     */
    public JMenuItem[] getPanelMenuItems() {
        return null;
    }

    /**
     * Appends the given message to the text area.
     *
     * @param message String object containing the message that is to be
     *                appended. It does not contain any line seperators at its
     *                end.
     * @param type The message type.
     * @param flag <code>boolean</code> indicating whether the user's nick is
     *             in this message (<code>true</code>) or not. If it is, the
     *             message will have a certain style.
     */
    public synchronized void append(String message, String type, boolean flag) {
        if (!message.startsWith("*")) {
            if (type.equals("ACTION")) {
                message = "* " + message;
            } else if (type.equals("NOTICE") || type.equals("NOTIFY")) {
                message = "*** " + message;
            }
        }
        if (flag) {
            setStyleBold(App.options.getBooleanOption("irc", "styles.FLAGGED.bold", false));
            setStyleItalic(App.options.getBooleanOption("irc", "styles.FLAGGED.italic", false));
            setStyleUnderline(App.options.getBooleanOption("irc", "styles.FLAGGED.underline", false));
            setStyleForeground(App.options.getColorOption("irc", "styles.FLAGGED.foreground", Color.BLACK));
            setStyleBackground(App.options.getColorOption("irc", "styles.FLAGGED.background", new Color(-921360)));
            decode(message, type);
            panelContainer.flagContentChange(this, true);
            return;
        }
        setStyleBold(App.options.getBooleanOption("irc", "styles." + type + ".bold", false));
        setStyleItalic(App.options.getBooleanOption("irc", "styles." + type + ".italic", false));
        setStyleUnderline(App.options.getBooleanOption("irc", "styles." + type + ".underline", false));
        setStyleForeground(App.options.getColorOption("irc", "styles." + type + ".foreground", Color.BLACK));
        setStyleBackground(App.options.getColorOption("irc", "styles." + type + ".background", Color.WHITE));
        decode(message, type);
        panelContainer.flagContentChange(this, false);
    }

    /**
     * Writes the message to the text area.
     *
     * @param message String containing the message.
     */
    private void write(String message) {
        int position = logScrollPane.getVerticalScrollBar().getValue();
        try {
            logTextPane.getDocument().insertString(logTextPane.getDocument().getLength(), message, currStyle);
        } catch (BadLocationException e) {
        }
        logTextPane.setCaretPosition(logTextPane.getDocument().getLength());
    }

    /**
     * Appends the current buffer's contents to the output area while decoding
     * a message. The StringBuffer will be setLength(0) afterwards.
     */
    protected void writeDecodedMessage(StringBuffer buf) {
        write(buf.toString());
        buf.setLength(0);
    }

    /**
     * Decodes the given message.
     *
     * @param message String containing the message.
     * @param type The message type.
     */
    private void decode(String message, String type) {
        StringBuffer buf = new StringBuffer();
        int len = message.length();
        int i;
        char c;
        for (i = 0; i < len; i++) {
            c = message.charAt(i);
            switch(c) {
                case '\002':
                    writeDecodedMessage(buf);
                    toggleStyleBold();
                    break;
                case '\003':
                    boolean comma = false;
                    char tmp;
                    int j = 1;
                    StringBuffer fgCol = new StringBuffer("");
                    StringBuffer bgCol = new StringBuffer("");
                    for (j = 1; j <= 5; j++) {
                        if (i + j < len) {
                            tmp = message.charAt(i + j);
                            if ((tmp >= '0') && (tmp <= '9')) {
                                if (comma) {
                                    if (bgCol.length() == 2) {
                                        break;
                                    } else {
                                        bgCol.append(tmp);
                                    }
                                } else {
                                    if (fgCol.length() == 2) {
                                        break;
                                    } else {
                                        fgCol.append(tmp);
                                    }
                                }
                            } else if (tmp == ',') {
                                comma = true;
                            } else {
                                break;
                            }
                        }
                    }
                    i += j - 1;
                    writeDecodedMessage(buf);
                    setStyleForeground(App.options.getColorOption("irc", "styles." + type + ".foreground", Color.BLACK));
                    setStyleBackground(App.options.getColorOption("irc", "styles." + type + ".background", Color.WHITE));
                    break;
                case '\026':
                    writeDecodedMessage(buf);
                    setStyleForeground(App.options.getColorOption("irc", "styles.PRIVMSG.background", Color.WHITE));
                    setStyleBackground(App.options.getColorOption("irc", "styles.PRIVMSG.foreground", Color.BLACK));
                    break;
                case '\037':
                    writeDecodedMessage(buf);
                    toggleStyleUnderline();
                    break;
                default:
                    buf.append(c);
                    break;
            }
        }
        writeDecodedMessage(buf);
        writeDecodedMessage(new StringBuffer("\n"));
    }

    /**
     * Enable or disable boldface mode for subsequent messages.
     */
    protected synchronized void setStyleBold(boolean bold) {
        currStyle.removeAttribute(StyleConstants.Bold);
        if (bold) currStyle.addAttribute(StyleConstants.Bold, Boolean.TRUE);
        this.bold = bold;
    }

    /**
     * Toggle boldface mode for subsequent messages.
     */
    protected void toggleStyleBold() {
        setStyleBold(!bold);
    }

    /**
     * Enable or disable italic mode for subsequent messages.
     */
    protected synchronized void setStyleItalic(boolean italic) {
        currStyle.removeAttribute(StyleConstants.Italic);
        if (italic) currStyle.addAttribute(StyleConstants.Italic, Boolean.TRUE);
        this.italic = italic;
    }

    /**
     * Toggle italic mode for subsequent messages.
     */
    protected void toggleStyleItalic() {
        setStyleItalic(!italic);
    }

    /**
     * Enable or disable underline mode for subsequent messages.
     */
    protected synchronized void setStyleUnderline(boolean underline) {
        currStyle.removeAttribute(StyleConstants.Underline);
        if (underline) currStyle.addAttribute(StyleConstants.Underline, Boolean.TRUE);
        this.underline = underline;
    }

    /**
     * Toggle underline mode for subsequent messages.
     */
    protected void toggleStyleUnderline() {
        setStyleUnderline(!underline);
    }

    /**
     * Set foreground for subsequent messages (IRC standard indexed color).
     */
    void setStyleForeground(int index) {
        setStyleForeground(IRC_TEXT_COLORS[index]);
    }

    /**
     * Set foreground for subsequent messages.
     */
    protected void setStyleForeground(Color col) {
        currStyle.removeAttribute(StyleConstants.Foreground);
        currStyle.addAttribute(StyleConstants.Foreground, col);
    }

    /**
     * Set background for subsequent messages (IRC standard indexed color).
     */
    protected void setStyleBackground(int index) {
        setStyleBackground(IRC_TEXT_COLORS[index]);
    }

    /**
     * Set background for subsequent messages.
     */
    protected void setStyleBackground(Color col) {
        currStyle.removeAttribute(StyleConstants.Background);
        currStyle.addAttribute(StyleConstants.Background, col);
    }

    /**
     * Parses the given colour String part.
     *
     * @param col String containing the colour value.
     * @param defaultColor <code>Color</code> to return if something goes wrong
     *                     with the parsing.
     * @return <code>Color</code> object for the String.
     */
    private Color parseColorString(String col, Color defaultColor) {
        try {
            int color = Integer.parseInt(col);
            while (color > 16) color -= 16;
            return IRC_TEXT_COLORS[color];
        } catch (NumberFormatException e) {
            return defaultColor;
        }
    }

    /**
     * Invoked when the user hits enter in the command field.
     */
    public void actionPerformed(ActionEvent e) {
        session.processUserCommand(commandField.getText());
        commandField.setText("");
    }

    public void messageProcessed(AbstractIRCSession session, IRCMessage msg) {
        if (msg.getType().equals(IRCMessageTypes.MSG_ADMIN)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_AWAY)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_CONNECT)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_DIE)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_ERROR)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_INFO)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_INVITE)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_ISON)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_JOIN)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_KICK)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_KILL)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_LINKS)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_LIST)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_LUSERS)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_MODE)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_MOTD)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_NAMES)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_NICK)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_NOTICE)) {
            MessageFormat f = new MessageFormat("[{0}] {1}");
            try {
                Object[] o = f.parse(NoticeMessage.getText(msg));
                if (IRCUtils.isChannel(o[0].toString())) return;
            } catch (ParseException exc) {
            }
            String origin = msg.getNick();
            String txt;
            if (origin.equals("")) {
                String args[] = new String[] { NoticeMessage.getText(msg) };
                txt = App.localization.localize("app", "irc.msg_notice.noorigin", "{0}", args);
            } else {
                String args[] = new String[] { origin, NoticeMessage.getText(msg) };
                txt = App.localization.localize("app", "irc.msg_notice", "{0}: {1}", args);
            }
            append(txt, "NOTICE", false);
        } else if (msg.getType().equals(IRCMessageTypes.MSG_OPER)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_PART)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_PASS)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_PING)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_PONG)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_PRIVMSG)) {
            if (!CTCPMessage.isCTCPMessage(PrivateMessage.getText(msg))) return;
            CTCPMessage ctcp = CTCPMessage.parseMessageString(PrivateMessage.getText(msg));
            if (ctcp.getType().equals(CTCPMessageTypes.CTCP_CLIENTINFO)) {
                String args[] = new String[] { msg.getNick() };
                String txt = App.localization.localize("app", "irc.msg_privmsg.ctcp.clientinfo", "{0} just requested client information (CTCP Clientinfo)", args);
                append(txt, "CTCP", false);
            } else if (ctcp.getType().equals(CTCPMessageTypes.CTCP_PING)) {
                String args[] = new String[] { msg.getNick() };
                String txt = App.localization.localize("app", "irc.msg_privmsg.ctcp.ping", "{0} just pinged you (CTCP Ping)", args);
                append(txt, "CTCP", false);
            } else if (ctcp.getType().equals(CTCPMessageTypes.CTCP_TIME)) {
                String args[] = new String[] { msg.getNick() };
                String txt = App.localization.localize("app", "irc.msg_privmsg.ctcp.time", "{0} just timed you (CTCP Time)", args);
                append(txt, "CTCP", false);
            } else if (ctcp.getType().equals(CTCPMessageTypes.CTCP_VERSION)) {
                String args[] = new String[] { msg.getNick() };
                String txt = App.localization.localize("app", "irc.msg_privmsg.ctcp.version", "{0} just versioned you (CTCP Version)", args);
                append(txt, "CTCP", false);
            }
        } else if (msg.getType().equals(IRCMessageTypes.MSG_QUIT)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_REHASH)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_RESTART)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_SERVICE)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_SERVLIST)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_SQUERY)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_SQUIT)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_STATS)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_SUMMON)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_TIME)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_TOPIC)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_TRACE)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_USER)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_USERHOST)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_USERS)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_VERSION)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_WALLOPS)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_WHO)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_WHOIS)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_WHOWAS)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOSUCHNICK)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOSUCHSERVER)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOSUCHCHANNEL)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_CANNOTSENDTOCHAN)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_TOOMANYCHANNELS)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_WASNOSUCHNICK)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_TOOMANYTARGETS)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOSUCHSERVICE)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOORIGIN)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NORECIPIENT)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOTEXTTOSEND)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOTOPLEVEL)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_WILDTOPLEVEL)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_BADMASK)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_UNKNOWNCOMMAND)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOMOTD)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOADMININFO)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_FILEERROR)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NONICKNAMEGIVEN)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_ERRONEUSNICKNAME)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NICKNAMEINUSE)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NICKCOLLISION)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_UNAVAILRESOURCE)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_USERNOTINCHANNEL)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOTONCHANNEL)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_USERONCHANNEL)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOLOGIN)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_SUMMONDISABLED)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_USERSDISABLED)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOTREGISTERED)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NEEDMOREPARAMS)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_ALREADYREGISTERED)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOPERMFORHOST)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_PASSWDMISMATCH)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_YOUREBANNEDCREEP)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_YOUWILLBEBANNED)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_KEYSET)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_CHANNELISFULL)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_UNKNOWNMODE)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_INVITEONLYCHAN)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_BANNEDFROMCHAN)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_BADCHANNELKEY)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_BADCHANMASK)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOCHANMODES)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_BANLISTFULL)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOPRIVILEGES)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_CHANOPRIVSNEEDED)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_CANTKILLSERVER)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_RESTRICTED)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_UNIQOPPRIVSNEEDED)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOOPERHOST)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_UMODEUNKNOWNFLAG)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_USERSDONTMATCH)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_WELCOME)) {
            String txt = msg.getArgs().get(msg.getArgs().size() - 1).toString();
            append(txt, "REPLY", false);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_YOURHOST)) {
            String txt = msg.getArgs().get(msg.getArgs().size() - 1).toString();
            append(txt, "REPLY", false);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_CREATED)) {
            String txt = msg.getArgs().get(msg.getArgs().size() - 1).toString();
            append(txt, "REPLY", false);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_MYINFO)) {
            String txt = msg.getArgs().get(msg.getArgs().size() - 1).toString();
            append(txt, "REPLY", false);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_BOUNCE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACELINK)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACECONNECTING)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACEHANDSHAKE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACEUNKNOWN)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACEOPERATOR)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACEUSER)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACESERVER)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACESERVICE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACENEWTYPE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_STATSLINKINFO)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_STATSCOMMANDS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_STATSCLINE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_STATSNLINE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_STATSILINE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_STATSKLINE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_STATSYLINE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFSTATS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_UMODEIS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_SERVLIST)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_STATSLLINE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_STATSUPTIME)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_STATSOLINE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_STATSHLINE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_LUSERCLIENT)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_LUSEROP)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_LUSERUNKNOWN)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_LUSERCHANNELS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_LUSERME)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ADMINME)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ADMINLOC1)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ADMINLOC2)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ADMINEMAIL)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACELOG)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACEEND)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRYAGAIN)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_NONE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_AWAY)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_USERHOST)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ISON)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_UNAWAY)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_NOWAWAY)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_WHOISUSER)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_WHOISSERVER)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_WHOISOPERATOR)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_WHOWASUSER)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFWHO)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_WHOISIDLE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFWHOIS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_WHOISCHANNELS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_LISTSTART)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_LIST)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_LISTEND)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_CHANNELMODEIS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_UNIQOPIS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_NOTOPIC)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TOPIC)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_INVITING)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_SUMMONING)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_INVITELIST)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFINVITELIST)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFEXCEPTLIST)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_VERSION)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_WHOREPLY)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_NAMREPLY)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_LINKS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFLINKS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFNAMES)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_BANLIST)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFBANLIST)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFWHOWAS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_INFO)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_MOTD)) {
            String txt = msg.getArgs().get(msg.getArgs().size() - 1).toString();
            append(txt, "REPLY", false);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFINFO)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_MOTDSTART)) {
            String txt = msg.getArgs().get(msg.getArgs().size() - 1).toString();
            append(txt, "REPLY", false);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFMOTD)) {
            String txt = msg.getArgs().get(msg.getArgs().size() - 1).toString();
            append(txt, "REPLY", false);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_YOUREOPER)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_REHASHING)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_YOURESERVICE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TIME)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_USERSSTART)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_USERS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFUSERS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_NOUSERS)) {
        }
    }

    public void channelCreated(AbstractIRCSession session, AbstractIRCChannel channel) {
        panelContainer.addExtendedPanel(new IRCChatPanel((DefaultIRCChannel) channel));
    }

    public void channelRemoved(AbstractIRCSession session, AbstractIRCChannel channel) {
    }

    public void optionsChanged(String set) {
        if (set.equals("gui")) adaptUI();
    }

    public void sessionStatusChanged(Session session, int oldStatus) {
        if (session.getStatus() == Session.CONNECTING) {
            String txt = App.localization.localize("app", "irc.connecting", "Connecting...");
            append(txt, "NOTIFY", false);
        } else if (session.getStatus() == Session.UNCONNECTED) {
            String txt = App.localization.localize("app", "irc.unconnected", "Connection closed");
            append(txt, "NOTIFY", false);
        } else if (session.getStatus() == Session.DISCONNECTED) {
            String txt = App.localization.localize("app", "irc.disconnected", "Connection terminated");
            append(txt, "NOTIFY", false);
        }
    }
}
