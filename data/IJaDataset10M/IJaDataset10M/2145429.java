package j_waste.gui;

import java.awt.*;
import javax.swing.*;

public class ChatTab extends JPanel {

    private JTextArea chatArea;

    private JTextField inputField;

    private JList userList;

    private JPanel chatPanel;

    public ChatTab(boolean multiUserChat) {
        super(new BorderLayout());
        inputField = new JTextField();
        chatArea = new JTextArea();
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setEditable(false);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatPanel = new JPanel(new BorderLayout(5, 5));
        chatPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 5, 5));
        chatPanel.add(chatScrollPane, BorderLayout.CENTER);
        chatPanel.add(inputField, BorderLayout.SOUTH);
        if (multiUserChat) buildMultiChat(); else buildIMChat();
    }

    private void buildMultiChat() {
        userList = new JList();
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, (chatPanel), new JScrollPane(userList));
        splitPane.setDividerLocation(600);
        add(splitPane);
    }

    private void buildIMChat() {
        add(chatPanel);
    }
}
