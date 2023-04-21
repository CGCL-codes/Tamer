package de.kout.wlFxp.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 *  an input dialog
 *
 *@author     Alexander Kout
 *@created    30. M�rz 2002
 */
class InputDialog extends JDialog implements ActionListener, KeyListener {

    JTextField input;

    MainPanel panel;

    JButton ok, cancel;

    String cmd;

    /**
	 *  Constructor for the InputDialog object
	 *
	 *@param  panel  Description of Parameter
	 *@param  cmd    Description of Parameter
	 */
    public InputDialog(MainPanel panel, String cmd, String text) {
        super(panel.frame, cmd, false);
        this.panel = panel;
        this.cmd = cmd;
        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(2, 2));
        pane.add(new JLabel("input: "));
        input = new JTextField(text);
        if (cmd.equals("socks4") && panel.frame.socks4Server != null) {
            input.setText(panel.frame.socks4Server.toString());
        }
        input.addKeyListener(this);
        pane.add(input);
        ok = new JButton(cmd);
        ok.addActionListener(this);
        cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        pane.add(ok);
        pane.add(cancel);
        getContentPane().add(pane);
        setLocationRelativeTo(panel.frame);
        pack();
        Point p = getLocation();
        p.setLocation(p.getX() - getWidth() / 2, p.getY() - getHeight() / 2);
        setLocation(p);
        setVisible(true);
    }

    /**
	 *  Description of the Method
	 */
    public void exit() {
        setVisible(false);
        dispose();
        panel.frame.repaint();
    }

    /**
	 *  Description of the Method
	 *
	 *@param  e  Description of Parameter
	 */
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Make")) {
            panel.makeDir(input.getText());
            exit();
        } else if (cmd.equals("Cancel")) {
            exit();
        } else if (cmd.equals("Command")) {
            if (panel.ftpSession.connected()) {
                try {
                    panel.ftpSession.ftp.doCmd(input.getText());
                } catch (IOException ex) {
                }
            }
            exit();
        } else if (cmd.equals("socks4")) {
            panel.frame.setSocks4Server(input.getText());
            exit();
        } else if (cmd.equals("Rename")) {
            panel.rename(input.getText());
            exit();
        }
    }

    /**
	 *  Description of the Method
	 *
	 *@param  e  Description of Parameter
	 */
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (cmd.equals("Make")) {
                panel.makeDir(input.getText());
                exit();
            } else if (cmd.equals("Command")) {
                if (panel.ftpSession.connected()) {
                    try {
                        panel.ftpSession.ftp.doCmd(input.getText());
                    } catch (IOException ex) {
                    }
                }
                exit();
            } else if (cmd.equals("socks4")) {
                panel.frame.setSocks4Server(input.getText());
                exit();
            } else if (cmd.equals("Rename")) {
                panel.rename(input.getText());
                exit();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            exit();
        }
    }

    /**
	 *  Description of the Method
	 *
	 *@param  event  Description of Parameter
	 */
    public void keyReleased(KeyEvent event) {
    }

    /**
	 *  Description of the Method
	 *
	 *@param  event  Description of Parameter
	 */
    public void keyTyped(KeyEvent event) {
    }
}
