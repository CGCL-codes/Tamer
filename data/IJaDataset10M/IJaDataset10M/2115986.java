package net.sf.isnake.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import net.sf.isnake.core.GameController;
import net.sf.isnake.core.iSnakeConfiguration;
import net.sf.isnake.ui.widget.ChatVScrollBarUI;

/**
 * Chat panel - that allows players to chat
 *
 * @author  Abhishek Dutta (adutta.np@gmail.com)
 * @version $Id: ChatPanel.java 146 2008-05-07 02:39:19Z thelinuxmaniac $
 */
public class ChatPanel extends javax.swing.JPanel {

    public static String PANEL_NAME = "ChatPanel";

    public static final Integer TAB_SIZE = 5;

    public static final Integer ROWS = 5;

    public static final Integer COLUMNS = 20;

    public static final Integer LINE_MAX_CHARS = 40;

    public static final Integer ATTR_SENDER_NAME = 0;

    public static final Integer ATTR_CHAT_MSG = 1;

    public static final Integer ATTR_SYS_INFO = 2;

    public static final Integer ATTR_SYS_LOGO = 3;

    private UIResources uiResources;

    private iSnakeConfiguration conf;

    private GameController gameController;

    private javax.swing.JTextPane chatMessagePane;

    private javax.swing.JScrollPane chatScrollPane;

    private javax.swing.JTextField localPlayerMessage;

    private SimpleAttributeSet[] chatAreaAttributes;

    private StyledDocument chatMessageDoc;

    /** Creates new form ChatPanel */
    public ChatPanel(GameController gc, iSnakeConfiguration isc, UIResources uir) {
        setGameController(gc);
        setConf(isc);
        setUiResources(uir);
        setPreferredSize(getConf().getChatFieldPanelDim());
        setMinimumSize(getConf().getChatFieldPanelDim());
        setOpaque(false);
        initChatAreaAttributes();
        localPlayerMessage = new javax.swing.JTextField();
        chatScrollPane = new javax.swing.JScrollPane(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        chatScrollPane.setWheelScrollingEnabled(true);
        ChatVScrollBarUI chatVScrollBar = new ChatVScrollBarUI();
        chatMessagePane = new JTextPane();
        chatScrollPane.getVerticalScrollBar().setUI(chatVScrollBar);
        chatScrollPane.setViewportView(chatMessagePane);
        chatScrollPane.setBorder(null);
        chatMessagePane.setOpaque(false);
        chatScrollPane.setOpaque(false);
        chatScrollPane.getViewport().setOpaque(false);
        setMinimumSize(new java.awt.Dimension(380, 380));
        localPlayerMessage.setForeground(new java.awt.Color(255, 255, 255));
        localPlayerMessage.setText("type your message here (press ENTER key to send)");
        localPlayerMessage.setToolTipText("Enter Chat message and press ENTER key to send");
        localPlayerMessage.setBorder(null);
        localPlayerMessage.setCaretColor(new java.awt.Color(255, 255, 0));
        localPlayerMessage.setOpaque(false);
        localPlayerMessage.setPreferredSize(new java.awt.Dimension(220, 18));
        localPlayerMessage.setSelectionColor(new java.awt.Color(255, 255, 0));
        localPlayerMessage.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendChatActionPerformed(evt);
            }
        });
        localPlayerMessage.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                textFieldFocusLost(evt);
            }

            public void focusGained(java.awt.event.FocusEvent evt) {
                textFieldFocusGained(evt);
            }
        });
        chatMessagePane.setToolTipText("Chat Messages Sent/Received");
        chatMessagePane.setBorder(null);
        chatMessagePane.setEditable(false);
        localPlayerMessage.setSelectionColor(new java.awt.Color(255, 255, 0));
        chatMessageDoc = chatMessagePane.getStyledDocument();
        String senderName[] = { "me", "bishu", "chabbi", "mukesh", "me", "prakash" };
        String chatMessages[] = { "hi guys! ready for the iSnake adventure?", "chabbi my internet connection is slow. I am quitiing this game.", ":( common bishu, we don't need fast internet connection for this game.", "oye ! yo game kasari khelne?", "guys, start the game. click on the 'red flag' button to start the game.", "the game looks cool. I am ready for the adventure.the game looks cool. I am ready for the adventure.the game looks cool. I am ready for the adventure." };
        showChatWelcomeMsg();
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(129, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(8, 8, 8).addComponent(localPlayerMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(chatScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(34, 34, 34)))));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(78, 78, 78).addComponent(chatScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(15, 15, 15).addComponent(localPlayerMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(90, Short.MAX_VALUE)));
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(getUiResources().getChatPanel(), 0, 0, null);
    }

    /**
     * Formats the message string into the form (styled) that can be displayed
     * in the chat area
     * @TODO apply the word wrap style of breaking message string
     */
    public void formatMsgAndShow(String sender, String message) {
        try {
            chatMessageDoc.insertString(chatMessageDoc.getLength(), sender + "\t", chatAreaAttributes[ATTR_SENDER_NAME]);
            if (message.length() > LINE_MAX_CHARS) {
                int begin = 0;
                int end = 0;
                chatMessageDoc.insertString(chatMessageDoc.getLength(), message.substring(begin, begin + LINE_MAX_CHARS).trim() + "\n", chatAreaAttributes[ATTR_CHAT_MSG]);
                begin += LINE_MAX_CHARS;
                while (begin < message.length()) {
                    if ((begin + LINE_MAX_CHARS) < message.length()) chatMessageDoc.insertString(chatMessageDoc.getLength(), "\t" + message.substring(begin, begin + LINE_MAX_CHARS).trim() + "\n", chatAreaAttributes[ATTR_CHAT_MSG]); else chatMessageDoc.insertString(chatMessageDoc.getLength(), "\t" + message.substring(begin).trim() + "\n", chatAreaAttributes[ATTR_CHAT_MSG]);
                    begin += LINE_MAX_CHARS;
                }
            } else {
                chatMessageDoc.insertString(chatMessageDoc.getLength(), message + "\n", chatAreaAttributes[ATTR_CHAT_MSG]);
            }
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
        moveScrollPane();
    }

    public synchronized void moveScrollPane() {
        chatScrollPane.getVerticalScrollBar().setValue(chatScrollPane.getVerticalScrollBar().getMaximum());
        repaint();
    }

    /**
     * Invoked when user presses ENTER key after typing the chat message
     */
    private void sendChatActionPerformed(java.awt.event.ActionEvent evt) {
        String message = ((JTextField) evt.getSource()).getText();
        if (message.length() == 0) return;
        String sender = "me";
        try {
            getGameController().stage2_SendChatMessage(message);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
        ((JTextField) evt.getSource()).setText("");
    }

    /**
     * Display the chat messages arriving from remote players
     */
    private void showNewChatMessage(String sender, String message) {
        formatMsgAndShow(sender, message);
    }

    /**
     * Removes all messages from the chat text area.
     * performs cleanup action
     */
    private void clearChatArea() {
        try {
            chatMessageDoc.remove(0, chatMessageDoc.getLength());
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    public void textFieldFocusLost(java.awt.event.FocusEvent evt) {
    }

    public void textFieldFocusGained(java.awt.event.FocusEvent evt) {
        ((JTextField) evt.getSource()).setText("");
    }

    private void showChatWelcomeMsg() {
        String welcomeMsg = "iSnake Chat\n\n";
        String instr = "Press Start button when you are ready to play\n";
        try {
            chatMessageDoc.insertString(chatMessageDoc.getLength(), welcomeMsg, chatAreaAttributes[ATTR_SYS_LOGO]);
            chatMessageDoc.insertString(chatMessageDoc.getLength(), instr, chatAreaAttributes[ATTR_SYS_INFO]);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Initialize the styled text attributes for the Chat Message area
     */
    protected void initChatAreaAttributes() {
        chatAreaAttributes = new SimpleAttributeSet[4];
        chatAreaAttributes[ATTR_SENDER_NAME] = new SimpleAttributeSet();
        StyleConstants.setFontSize(chatAreaAttributes[ATTR_SENDER_NAME], 12);
        StyleConstants.setForeground(chatAreaAttributes[ATTR_SENDER_NAME], new Color(255, 255, 0));
        chatAreaAttributes[ATTR_CHAT_MSG] = new SimpleAttributeSet();
        StyleConstants.setFontSize(chatAreaAttributes[ATTR_CHAT_MSG], 12);
        StyleConstants.setForeground(chatAreaAttributes[ATTR_CHAT_MSG], new Color(255, 255, 255));
        chatAreaAttributes[ATTR_SYS_LOGO] = new SimpleAttributeSet();
        StyleConstants.setFontSize(chatAreaAttributes[ATTR_SYS_LOGO], 16);
        StyleConstants.setForeground(chatAreaAttributes[ATTR_SYS_LOGO], new Color(255, 255, 255));
        chatAreaAttributes[ATTR_SYS_INFO] = new SimpleAttributeSet();
        StyleConstants.setFontSize(chatAreaAttributes[ATTR_SYS_INFO], 12);
        StyleConstants.setForeground(chatAreaAttributes[ATTR_SYS_INFO], new Color(255, 255, 255));
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public iSnakeConfiguration getConf() {
        return conf;
    }

    public void setConf(iSnakeConfiguration conf) {
        this.conf = conf;
    }

    public UIResources getUiResources() {
        return uiResources;
    }

    public void setUiResources(UIResources uiResources) {
        this.uiResources = uiResources;
    }

    private void initComponents() {
        textField1 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        setMinimumSize(new java.awt.Dimension(380, 380));
        textField1.setForeground(new java.awt.Color(255, 255, 255));
        textField1.setText("hey, why aren't you starting the game?");
        textField1.setToolTipText("Enter Chat message and press ENTER key to send");
        textField1.setBorder(null);
        textField1.setCaretColor(new java.awt.Color(255, 255, 0));
        textField1.setOpaque(false);
        textField1.setPreferredSize(new java.awt.Dimension(220, 18));
        textField1.setSelectionColor(new java.awt.Color(255, 255, 0));
        jTextPane1.setBorder(null);
        jTextPane1.setEditable(false);
        jTextPane1.setText("hello\\nHi");
        jTextPane1.setToolTipText("Chat Messages Sent/Received");
        jTextPane1.setAlignmentX(0.0F);
        jTextPane1.setAlignmentY(0.0F);
        jScrollPane2.setViewportView(jTextPane1);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(textField1, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(392, 392, 392)).addGroup(layout.createSequentialGroup().addGap(178, 178, 178).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(296, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(115, 115, 115).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(47, 47, 47).addComponent(textField1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(90, Short.MAX_VALUE)));
    }

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JTextPane jTextPane1;

    private javax.swing.JTextField textField1;
}
