package gui.tabs;

import framework.IATMClient;
import framework.IChatTab;
import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.*;

/**
 *
 * @author  m0ng85
 */
public class ChatTab extends javax.swing.JPanel implements IChatTab {

    private IATMClient atmClient;

    private LinkedList<String> nicknames = new LinkedList<String>();

    private StyleContext sc;

    private DefaultStyledDocument doc;

    private Style[] myStyles;

    /** Creates new form ChatTab */
    public ChatTab(IATMClient atmc) {
        this.atmClient = atmc;
        sc = new StyleContext();
        doc = new DefaultStyledDocument(sc);
        initStyles();
        initComponents();
    }

    private void initStyles() {
        myStyles = new Style[5];
        myStyles[0] = sc.addStyle(null, null);
        StyleConstants.setForeground(myStyles[0], Color.BLACK);
        myStyles[1] = sc.addStyle(null, null);
        StyleConstants.setBold(myStyles[1], true);
        StyleConstants.setForeground(myStyles[1], Color.BLUE);
        myStyles[2] = sc.addStyle(null, null);
        StyleConstants.setBold(myStyles[2], true);
        StyleConstants.setForeground(myStyles[2], Color.RED);
        myStyles[3] = sc.addStyle(null, null);
        StyleConstants.setItalic(myStyles[3], true);
        StyleConstants.setForeground(myStyles[3], Color.GRAY);
        myStyles[4] = sc.addStyle(null, null);
        StyleConstants.setBold(myStyles[4], true);
        StyleConstants.setForeground(myStyles[4], new Color(51, 0, 153));
    }

    private void initComponents() {
        messageInput = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        nicknameOutput = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        messageOutput = new javax.swing.JTextPane();
        addComponentListener(new java.awt.event.ComponentAdapter() {

            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        messageInput.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                messageInputKeyPressed(evt);
            }
        });
        nicknameOutput.setEnabled(false);
        jScrollPane1.setViewportView(nicknameOutput);
        messageOutput.setEditable(false);
        jScrollPane2.setViewportView(messageOutput);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(messageInput, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(messageInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
    }

    private void messageInputKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == 10) {
            if (!messageInput.getText().equals("")) {
                this.addText(atmClient.getModel().getNickname(), messageInput.getText());
                atmClient.getModel().sendChatMessage(messageInput.getText());
                messageInput.setText("");
            }
        }
    }

    private void formKeyPressed(java.awt.event.KeyEvent evt) {
    }

    private void formComponentShown(java.awt.event.ComponentEvent evt) {
        messageOutput.requestFocus();
    }

    private void formFocusGained(java.awt.event.FocusEvent evt) {
        messageOutput.requestFocus();
    }

    public synchronized void addNick(String nick) {
        if (!nicknames.contains(nick)) {
            nicknames.add(nick);
        }
        refreshUserList();
    }

    public synchronized void removeNick(String nick) {
        nicknames.remove(nick);
        refreshUserList();
    }

    private synchronized void refreshUserList() {
        nicknameOutput.removeAll();
        nicknameOutput.setListData(nicknames.toArray());
        nicknameOutput.repaint();
    }

    public void addText(String nick, String text) {
        try {
            DateFormat tmp = new SimpleDateFormat();
            String test = tmp.format(new Date());
            messageOutput.setDocument(doc);
            doc.insertString(doc.getLength(), "(" + test + ")  ", myStyles[1]);
            doc.insertString(doc.getLength(), nick + ": ", myStyles[2]);
            doc.insertString(doc.getLength(), text + "\n", myStyles[0]);
            messageOutput.setSelectionEnd(messageOutput.getText().length());
        } catch (BadLocationException ex) {
            Logger.getLogger(ChatTab.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JTextField messageInput;

    private javax.swing.JTextPane messageOutput;

    private javax.swing.JList nicknameOutput;

    public synchronized void addNicks(String[] nicks) {
        LinkedList<String> tmp = new LinkedList<String>();
        for (String str : nicks) {
            tmp.add(str);
        }
        nicknames.clear();
        nicknames.addAll(tmp);
        refreshUserList();
    }

    public synchronized void removeNicks(String[] nicks) {
        LinkedList<String> tmp = new LinkedList<String>();
        for (String str : nicks) {
            tmp.add(str);
        }
        nicknames.removeAll(tmp);
        refreshUserList();
    }
}
