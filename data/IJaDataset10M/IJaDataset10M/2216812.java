package com.zuaari.gameclient;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JTextField;

public class BaseClient extends JApplet {

    private JTextField inputField;

    Image lobbyImage;

    Image loader;

    public void init() {
        this.setLayout(new FlowLayout());
        inputField = new JTextField("Enter your input");
        loader = getImage(getDocumentBase(), "./../assets/loader.gif");
        add(inputField, this);
        lobbyImage = getImage(getDocumentBase(), "./../assets/lobby.jpg");
    }

    public void start() {
    }

    public void stop() {
    }

    public void destroy() {
    }

    public void paint(Graphics g) {
        if (inputField.getText().equalsIgnoreCase("y")) {
            inputField.setVisible(false);
            g.drawImage(lobbyImage, 0, 0, this);
        } else {
            g.drawImage(loader, 350, 250, this);
        }
    }
}
