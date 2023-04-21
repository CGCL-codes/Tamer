package net.perham.jnap.gui;

import java.util.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import net.perham.jnap.*;
import net.perham.jnap.net.*;
import net.perham.util.*;

public class ConsolePanel extends JPanel implements IPanel, INetworkEventListener {

    JTextArea text;

    public ConsolePanel(JFrame f) {
        super();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        text = new JTextArea("");
        JScrollPane sp = new JScrollPane(text);
        add(sp, BorderLayout.CENTER);
        INetwork i = (INetwork) Main.getConfig().getObjectProperty("net");
        i.addNetworkEventListener(this);
    }

    public void networkEvent(int code, String arg) {
        switch(code) {
            case INetwork.SYSTEM_MESSAGE:
                text.append(arg + "\n");
                break;
            case INetwork.OFF_LINE:
            case INetwork.LOGGED_IN:
                text.setText("");
                break;
        }
    }

    public Icon getIcon() {
        return null;
    }

    public Component getPanel() {
        return this;
    }

    public String getName() {
        return "Console";
    }

    public void shutdown() {
    }
}
