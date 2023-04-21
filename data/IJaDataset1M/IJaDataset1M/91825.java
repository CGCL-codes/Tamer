package com.arsenal.session.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import com.arsenal.session.*;
import com.arsenal.message.*;
import com.arsenal.client.MenuCommand;

public class SessionMenu extends JMenu implements ActionListener {

    public SessionMenu() {
        super("Sessions");
        CreateSessionItem createSessionItem = new CreateSessionItem();
        createSessionItem.addActionListener(this);
        add(createSessionItem);
        DeleteSessionItem deleteSessionItem = new DeleteSessionItem();
        deleteSessionItem.addActionListener(this);
        add(deleteSessionItem);
    }

    public void actionPerformed(ActionEvent e) {
        MenuCommand cmd = (MenuCommand) e.getSource();
        cmd.execute();
    }
}
