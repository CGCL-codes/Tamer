package com.test;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import com.google.code.actionscriptnativebridge.ActionScriptBridge;
import com.google.code.actionscriptnativebridge.annotation.ActionScriptService;

@SuppressWarnings("serial")
@ActionScriptService
public class ChatWindow extends JFrame {

    private JTextArea textArea;

    private JTextField textField;

    private JButton button;

    private static final ChatWindow INSTANCE = new ChatWindow();

    public static ChatWindow getInstance() {
        return INSTANCE;
    }

    private ChatWindow() {
        super("Chat Window");
        setLayout(new FlowLayout());
        Container c = getContentPane();
        textArea = new JTextArea(10, 50);
        c.add(new JScrollPane(textArea));
        textField = new JTextField(50);
        c.add(textField);
        button = new JButton("Send");
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                ActionScriptBridge.getInstance().callActionscriptMethod(null, "newMessage", textField.getText());
                textArea.setText("<<< " + textField.getText() + "\n" + textArea.getText());
                textField.setText("");
            }
        });
        c.add(button);
        setSize(600, 300);
    }

    public void showMessage(String message) {
        textArea.setText(">>> " + message + "\n" + textArea.getText());
    }
}
