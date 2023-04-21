package com.arsenal.repository.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import com.arsenal.repository.message.CreateNewRepositoryMessage;
import com.arsenal.repository.*;
import com.arsenal.user.client.UserClientHandler;
import com.arsenal.client.*;
import com.arsenal.log.Log;

public class AddNewRepositoryWindow extends JFrame {

    String privateButtonText = "Private";

    String groupButtonText = "Group";

    String publicButtonText = "Public";

    String adminButtonText = "Administrator";

    private ButtonGroup group = new ButtonGroup();

    public JRadioButton privateButton = new JRadioButton(privateButtonText);

    public JRadioButton publicGroupButton = new JRadioButton(groupButtonText);

    public JRadioButton publicAllButton = new JRadioButton(publicButtonText);

    public JRadioButton adminButton = new JRadioButton(adminButtonText);

    private JTextField text = new JTextField(25);

    private JButton button = new JButton("Create New Repository");

    private JPanel topPanel = new JPanel();

    private JPanel bottomPanel = new JPanel();

    private static AddNewRepositoryWindow instance = new AddNewRepositoryWindow();

    public static AddNewRepositoryWindow getInstance() {
        if (instance == null) {
            instance = new AddNewRepositoryWindow();
        }
        return instance;
    }

    public AddNewRepositoryWindow() {
        super("Create a New Repository on the server");
        setSize(new Dimension(500, 215));
        topPanel.setBackground(Color.white);
        bottomPanel.setBackground(Color.white);
        JLabel label = new JLabel("Repository Name: ");
        label.setLabelFor(text);
        topPanel.add(label);
        topPanel.add(text);
        JPanel radioPanel = new JPanel();
        radioPanel.setBackground(Color.white);
        radioPanel.setLayout(new GridLayout(0, 1));
        group.add(privateButton);
        group.add(publicGroupButton);
        group.add(publicAllButton);
        group.add(adminButton);
        radioPanel.add(privateButton);
        radioPanel.add(publicGroupButton);
        radioPanel.add(publicAllButton);
        radioPanel.add(adminButton);
        privateButton.setBackground(Color.white);
        publicGroupButton.setBackground(Color.white);
        publicAllButton.setBackground(Color.white);
        adminButton.setBackground(Color.white);
        radioPanel.setBackground(Color.white);
        bottomPanel.add(button);
        JSplitPane fsplitpane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, radioPanel);
        fsplitpane.setDividerLocation(30);
        fsplitpane.setDividerSize(0);
        JSplitPane jsplitpane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, fsplitpane, bottomPanel);
        getContentPane().add(jsplitpane);
        jsplitpane.setDividerLocation(150);
        jsplitpane.setDividerSize(0);
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!text.getText().trim().equals("")) {
                    RepositoryDataBean rdb = new RepositoryDataBean();
                    rdb.setName(text.getText().trim());
                    if (privateButton.isSelected()) rdb.setPermission(Permissions.PRIVATE); else if (publicGroupButton.isSelected()) rdb.setPermission(Permissions.GROUP); else if (publicAllButton.isSelected()) rdb.setPermission(Permissions.PUBLIC); else {
                        rdb.setPermission(Permissions.ADMIN);
                    }
                    rdb.setUsername(ConnectionWindow.getInstance().getUsername());
                    rdb.setGroup(UserClientHandler.getInstance().getGroupForMyUser());
                    boolean sendMessage = true;
                    String mess = "";
                    if ((rdb.getPermission() == Permissions.ADMIN) && !UserClientHandler.getInstance().isAdmin()) {
                        sendMessage = false;
                        mess = "Your username does not have admin privledges to add repositories or files, please contact your Arsenal Administrator\n\n";
                    }
                    if (UserClientHandler.getInstance().getGroupForMyUser().equals("none") && (rdb.getPermission() == Permissions.ADMIN)) {
                        sendMessage = false;
                        mess = "Your username is not associated with a group so you cannot add a repository to any group, please contact your Arsenal Administrator\n\n";
                    }
                    Log.debug(this, mess);
                    if (sendMessage) {
                        CreateNewRepositoryMessage message = new CreateNewRepositoryMessage();
                        message.setHandlerName("repository");
                        message.setPayload(rdb);
                        if (Client.getInstance().isConnectedToServer()) Client.getInstance().sendMessage(message);
                    }
                }
                text.setText("");
                hide();
            }
        });
    }
}
