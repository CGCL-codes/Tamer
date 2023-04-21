package net.brokenroad.gamma.gui;

import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import net.brokenroad.gamma.net.*;
import net.brokenroad.gamma.server.*;

public class SessionStartFrame implements ActionListener {

    ArrayList<SessionListener> listeners;

    Session session;

    JFrame frame;

    JTextField port;

    JButton accept, cancel;

    public void startSession(SessionListener sl) {
        frame = new JFrame("Create New Session");
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
        port = new JTextField("1125");
        accept = new JButton("Accept");
        accept.addActionListener(this);
        cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        frame.add(new JLabel("Session Title:"));
        frame.add(port);
        frame.add(accept);
        frame.setSize(300, 400);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        new ServerLauncher().startServer(Integer.parseInt(port.getText()));
        frame.setVisible(false);
        notifyListeners();
    }

    private void notifyListeners() {
        for (int i = 0; i < listeners.size(); i++) listeners.get(i).sessionChange(session, "add");
    }

    public SessionStartFrame() {
        listeners = new ArrayList<SessionListener>(2);
        frame = null;
    }
}
