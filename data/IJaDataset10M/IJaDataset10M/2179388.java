package phex.gui.chat;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import phex.chat.ChatEngine;
import phex.common.log.NLogger;
import phex.gui.common.*;
import phex.utils.*;

public class ChatFrame extends JFrame {

    private static final int MAX_CHAT_LENGTH = 50 * 1024;

    private ChatEngine chatEngine;

    private JTextArea chatTextArea;

    private JTextField sendTextField;

    public ChatFrame(ChatEngine aChatEngine) {
        super();
        chatEngine = aChatEngine;
        Object[] args = new Object[] { chatEngine.getHostAddress().getFullHostName() };
        setTitle(Localizer.getFormatedString("ChattingWith", args));
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                chatEngine.stopChat();
                closeChatFrame();
            }
        });
        Icon frameIcon = GUIRegistry.getInstance().getSystemIconPack().getIcon("Frame.IconImage");
        if (frameIcon != null) {
            setIconImage(((ImageIcon) frameIcon).getImage());
        }
        MainFrame mainFrame = GUIRegistry.getInstance().getMainFrame();
        GUIUtils.setWindowLocationRelativeTo(this, mainFrame);
        prepareComponent();
    }

    private void prepareComponent() {
        GridBagConstraints constraints;
        getContentPane().setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel(new GridBagLayout());
        chatTextArea = new JTextArea(10, 40);
        chatTextArea.setEditable(false);
        chatTextArea.setLineWrap(true);
        chatTextArea.setWrapStyleWord(true);
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(3, 2, 3, 2);
        mainPanel.add(new JScrollPane(chatTextArea), constraints);
        sendTextField = new JTextField(40);
        sendTextField.registerKeyboardAction(new SendTextActionHandler(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0, 6, 3, 6);
        mainPanel.add(sendTextField, constraints);
        getContentPane().add(BorderLayout.CENTER, mainPanel);
    }

    public void addChatMessage(String message) {
        message = '<' + chatEngine.getChatNick() + "> " + message + '\n';
        try {
            Document doc = chatTextArea.getDocument();
            doc.insertString(doc.getLength(), message, null);
            if (doc.getLength() > MAX_CHAT_LENGTH) {
                doc.remove(0, doc.getLength() - MAX_CHAT_LENGTH);
            }
            chatTextArea.scrollRectToVisible(chatTextArea.modelToView(doc.getLength()));
        } catch (BadLocationException exp) {
            NLogger.warn(ChatFrame.class, exp, exp);
        }
    }

    public void addInfoMessage(String message) {
        try {
            Document doc = chatTextArea.getDocument();
            doc.insertString(doc.getLength(), message, null);
            if (doc.getLength() > MAX_CHAT_LENGTH) {
                doc.remove(0, doc.getLength() - MAX_CHAT_LENGTH);
            }
            chatTextArea.scrollRectToVisible(chatTextArea.modelToView(doc.getLength()));
        } catch (BadLocationException exp) {
            NLogger.warn(ChatFrame.class, exp, exp);
        }
    }

    private void closeChatFrame() {
        setVisible(false);
        dispose();
    }

    private class SendTextActionHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String text = sendTextField.getText();
            if (text.length() == 0) {
                return;
            }
            if (!chatEngine.isConnected()) {
                addInfoMessage(Localizer.getString("NotConnected"));
                return;
            }
            Document doc = sendTextField.getDocument();
            try {
                doc.remove(0, doc.getLength());
            } catch (BadLocationException exp) {
                NLogger.warn(ChatFrame.class, exp, exp);
            }
            String message = '<' + Localizer.getString("You") + "> " + text + '\n';
            try {
                doc = chatTextArea.getDocument();
                doc.insertString(doc.getLength(), message, null);
                chatTextArea.scrollRectToVisible(chatTextArea.modelToView(doc.getLength()));
            } catch (BadLocationException exp) {
                NLogger.warn(ChatFrame.class, exp, exp);
            }
            chatEngine.sendChatMessage(text);
        }
    }
}
