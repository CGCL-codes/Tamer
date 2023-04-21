package com.arsenal.group.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import com.arsenal.group.message.CreateNewGroupMessage;
import com.arsenal.group.*;
import com.arsenal.client.observer.*;
import com.arsenal.util.JTextFieldMaxLength;

public class GroupCreateWindow extends JFrame implements LogoutObserver, LoginObserver {

    private JLabel textLabel = new JLabel("New Group Name: ");

    private JTextField text = new JTextFieldMaxLength(20);

    private JButton button = new JButton("Enter");

    private JPanel topPanel = new JPanel();

    private JPanel bottomPanel = new JPanel();

    private static GroupCreateWindow instance = new GroupCreateWindow();

    public static GroupCreateWindow getInstance() {
        if (instance == null) {
            instance = new GroupCreateWindow();
        }
        return instance;
    }

    public GroupCreateWindow() {
        super("Arsenal Group Create Window");
        setSize(new Dimension(500, 125));
        topPanel.setBackground(Color.white);
        bottomPanel.setBackground(Color.white);
        textLabel.setLabelFor(text);
        topPanel.add(textLabel);
        topPanel.add(text);
        bottomPanel.add(button);
        JSplitPane jsplitpane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
        jsplitpane.setDividerSize(0);
        jsplitpane.setDividerLocation(60);
        jsplitpane.setBackground(Color.white);
        getContentPane().add(jsplitpane);
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                GroupClientHandler.getInstance().groupChosenToCreate(text.getText().trim());
                text.setText("");
                hide();
            }
        });
        registerLogoutListener(this);
        registerLoginListener(this);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = getSize();
        screenSize.height = screenSize.height / 2;
        screenSize.width = screenSize.width / 2;
        size.height = size.height / 2;
        size.width = size.width / 2;
        int y = screenSize.height - size.height;
        int x = screenSize.width - size.width;
        setLocation(x, y);
    }

    public void doLoginAction() {
        text.setEnabled(true);
        button.setEnabled(true);
    }

    public void registerLoginListener(LoginObserver loginObserver) {
        GroupClientHandler.getInstance().registerLoginListener(loginObserver);
    }

    public void doLogoutAction() {
        text.setEnabled(false);
        button.setEnabled(false);
    }

    public void registerLogoutListener(LogoutObserver logoutObserver) {
        GroupClientHandler.getInstance().registerLogoutListener(logoutObserver);
    }
}
