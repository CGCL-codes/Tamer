package com.objectdraw.client;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Box;
import java.io.*;
import java.net.UnknownHostException;
import java.awt.Dimension;

/**
 * Initally attempted to provide connection GUI for user.
 * Unused.
 * 
 * @author Abdel
 *
 */
public class GuiServerConnect extends JPanel {

    private static final long serialVersionUID = 2371706708929800516L;

    private GridBagConstraints constraints;

    private GridBagLayout layout = new GridBagLayout();

    private JTextField txtUser;

    private JTextField txtServer;

    public clientcomm com;

    public void close() {
        com.close();
    }

    public GuiServerConnect() {
        setLayout(layout);
        constraints = new GridBagConstraints();
        setVisible(true);
        JLabel label1 = new JLabel("Server IP Address:   ");
        JLabel label2 = new JLabel("User Name :    ");
        JLabel label3 = new JLabel("         ");
        txtUser = new JTextField(20);
        txtServer = new JTextField(20);
        JButton connectButton = new JButton("Connect");
        JButton cancelButton = new JButton("Cancel");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.NONE;
        addComponent(label1, 5, 0, 1, 1);
        constraints.fill = GridBagConstraints.NONE;
        addComponent(txtUser, 7, 2, 1, 1);
        constraints.fill = GridBagConstraints.NONE;
        addComponent(label2, 7, 0, 1, 1);
        constraints.fill = GridBagConstraints.NONE;
        addComponent(txtServer, 5, 2, 1, 1);
        constraints.fill = GridBagConstraints.NONE;
        addComponent(label3, 64, 0, 1, 1);
        Box.createRigidArea(new Dimension(12, 8));
        constraints.anchor = GridBagConstraints.SOUTH;
        constraints.fill = GridBagConstraints.NONE;
        addComponent(connectButton, 65, 0, 1, 1);
        connectButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                try {
                    com = new clientcomm(txtServer.getText());
                    com.sendMessage("connect " + txtUser.getText());
                    System.out.println(com.readMessage());
                    com.sendMessage("Control request " + txtUser.getText());
                    System.out.println(com.readMessage());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        constraints.fill = GridBagConstraints.NONE;
        addComponent(cancelButton, 65, 2, 2, 1);
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
    }

    private void addComponent(Component component, int row, int column, int width, int height) {
        constraints.gridx = column;
        constraints.gridy = row;
        constraints.gridwidth = width;
        constraints.gridheight = height;
        layout.setConstraints(component, constraints);
        add(component);
    }
}
