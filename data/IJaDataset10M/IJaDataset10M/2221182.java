package net.sourceforge.island.ui.swing;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import net.sourceforge.island.command.CommandDispatcher;
import net.sourceforge.island.command.impl.SendMessage;
import net.sourceforge.island.contact.Contact;
import net.sourceforge.island.log.MessageLog;
import net.sourceforge.island.messaging.Message;
import net.sourceforge.island.resources.Configuration;
import net.sourceforge.island.ui.MessageWindow;

/**
 * @author mliberato
 */
public class MessageWindowImpl extends JFrame implements MessageWindow {

    private final String _ip;

    private final Contact _localContact;

    private final Contact _remoteContact;

    private final JTextPane _history = new JTextPane();

    private final JTextPane _textTyping = new JTextPane();

    private final JButton _sendMessageButton = new JButton(Configuration.getString("messagewindow.sendbutton.text", "send"));

    /**
	 * 
	 */
    public MessageWindowImpl(final String ip, final Contact localContact, final Contact remoteContact) {
        super(remoteContact.toString());
        _ip = ip;
        _remoteContact = remoteContact;
        _localContact = localContact;
        setupComponent();
    }

    private void setupComponent() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(Configuration.getInt("messagewindow.width", 450), Configuration.getInt("messagewindow.height", 450));
        setLocation(Configuration.getInt("messagewindow.x", 50), Configuration.getInt("messagewindow.y", 50));
        _history.setEditable(false);
        _history.setFont(new Font("Tahoma", Font.BOLD, 12));
        _textTyping.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK), "sendMessageKey");
        _textTyping.getActionMap().put("sendMessageKey", SEND_MESSAGE_ACTION);
        _textTyping.setFont(new Font("Tahoma", Font.BOLD, 12));
        _sendMessageButton.setMnemonic(Configuration.getChar("messagewindow.sendbutton.mnemomic", 's'));
        _sendMessageButton.addActionListener(SEND_MESSAGE_ACTION);
        final SpringLayout springLayout = new SpringLayout();
        getContentPane().setLayout(springLayout);
        final JScrollPane historyScroll = new JScrollPane(_history);
        getContentPane().add(historyScroll);
        _history.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                scrollToBottom(e);
            }

            public void insertUpdate(DocumentEvent e) {
                scrollToBottom(e);
            }

            public void removeUpdate(DocumentEvent e) {
                scrollToBottom(e);
            }

            private void scrollToBottom(DocumentEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        _history.scrollRectToVisible(new Rectangle(0, _history.getHeight() - 2, 1, 1));
                    }
                });
            }
        });
        springLayout.putConstraint(SpringLayout.SOUTH, historyScroll, -100, SpringLayout.SOUTH, getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, historyScroll, -100, SpringLayout.EAST, getContentPane());
        springLayout.putConstraint(SpringLayout.NORTH, historyScroll, 5, SpringLayout.NORTH, getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, historyScroll, 5, SpringLayout.WEST, getContentPane());
        final JScrollPane textTypingScroll = new JScrollPane(_textTyping);
        getContentPane().add(textTypingScroll);
        springLayout.putConstraint(SpringLayout.SOUTH, textTypingScroll, 95, SpringLayout.SOUTH, historyScroll);
        springLayout.putConstraint(SpringLayout.EAST, textTypingScroll, 0, SpringLayout.EAST, historyScroll);
        springLayout.putConstraint(SpringLayout.NORTH, textTypingScroll, 5, SpringLayout.SOUTH, historyScroll);
        springLayout.putConstraint(SpringLayout.WEST, textTypingScroll, 0, SpringLayout.WEST, historyScroll);
        getContentPane().add(_sendMessageButton);
        springLayout.putConstraint(SpringLayout.SOUTH, _sendMessageButton, 0, SpringLayout.SOUTH, textTypingScroll);
        springLayout.putConstraint(SpringLayout.EAST, _sendMessageButton, -5, SpringLayout.EAST, getContentPane());
        springLayout.putConstraint(SpringLayout.NORTH, _sendMessageButton, 0, SpringLayout.NORTH, textTypingScroll);
        springLayout.putConstraint(SpringLayout.WEST, _sendMessageButton, 5, SpringLayout.EAST, textTypingScroll);
        setVisible(true);
        addGlobalKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent e) {
                if (KeyEvent.VK_ESCAPE == e.getKeyChar()) {
                    dispatchEvent(new WindowEvent(MessageWindowImpl.this, WindowEvent.WINDOW_CLOSING));
                }
            }
        });
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                _textTyping.requestFocus();
            }
        });
    }

    /**
	 * @param adapter
	 */
    private void addGlobalKeyListener(KeyAdapter adapter) {
        addKeyListener(adapter);
        _history.addKeyListener(adapter);
        _textTyping.addKeyListener(adapter);
    }

    /**
	 * @see island.messaging.IncomingMessageListener#incomingMessage(island.messaging.Message)
	 */
    public void incomingMessage(Message message) {
        insertConversationEntry(message, _remoteContact);
    }

    /**
	 * @param string
	 * @param _contact
	 * @return
	 */
    private synchronized void insertConversationEntry(Message message, Contact contact) {
        final DateFormat formatter = DateFormat.getTimeInstance();
        StringBuffer buffer = new StringBuffer();
        buffer.append('(').append(formatter.format(Calendar.getInstance().getTime())).append(')').append(' ').append(contact.getNickName()).append(' ').append(Configuration.getString("messagewindow.saysmessage", "says")).append(':').append(Configuration.LINE_SEPARATOR).append(message.getBody()).append(Configuration.LINE_SEPARATOR).append(Configuration.LINE_SEPARATOR);
        final String builtMessage = buffer.toString();
        MessageLog.log(_remoteContact, builtMessage);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    _history.getDocument().insertString(_history.getDocument().getLength(), builtMessage, null);
                } catch (BadLocationException e) {
                }
            }
        });
    }

    private final AbstractAction SEND_MESSAGE_ACTION = new AbstractAction() {

        public void actionPerformed(ActionEvent event) {
            String body = _textTyping.getText();
            if (body != null && body.trim().length() > 0) {
                try {
                    Message message = new Message(body);
                    _textTyping.setText(null);
                    insertConversationEntry(message, _localContact);
                    CommandDispatcher.dispatchCommand(_ip, new SendMessage(message));
                    _textTyping.requestFocus();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    };
}
