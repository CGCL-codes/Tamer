package de.kout.wlFxp.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import de.kout.wlFxp.ftp.FtpFile;
import de.kout.wlFxp.ftp.Transfer;

/**
 *  the resume dialog class
 *
 *@author     Alexander Kout
 *@created    17. Sep 2002
 */
public class ResumeDialog extends JDialog implements ActionListener {

    MainPanel panel;

    long rest;

    /**
	 *  Constructor for the ResumeDialog object
	 *
	 *@param  panel  Description of the Parameter
	 *@param  t      Description of the Parameter
	 */
    public ResumeDialog(MainPanel panel, Transfer t) {
        super(panel.frame, "Resume/Overwrite", true);
        this.panel = panel;
        JLabel label;
        JButton resume;
        JButton overwrite;
        JButton cancel;
        JPanel p = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc;
        p.setLayout(gbl);
        FtpFile from = t.getSource();
        FtpFile to = t.getDest();
        String text = "overwrite \"" + to.getName() + "\" size: " + to.getSize() + " with: \"" + from.getName() + "\" size: " + from.getSize() + " ?";
        rest = to.getSize();
        label = new JLabel(text);
        gbc = makegbc(0, 0, 3, 1);
        gbl.setConstraints(label, gbc);
        p.add(label);
        resume = new JButton("Resume");
        resume.addActionListener(this);
        gbc = makegbc(0, 1, 1, 1);
        gbl.setConstraints(resume, gbc);
        p.add(resume);
        overwrite = new JButton("Overwrite");
        overwrite.addActionListener(this);
        gbc = makegbc(1, 1, 1, 1);
        gbl.setConstraints(overwrite, gbc);
        p.add(overwrite);
        cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        gbc = makegbc(2, 1, 1, 1);
        gbl.setConstraints(cancel, gbc);
        p.add(cancel);
        getContentPane().add(p);
        setLocationRelativeTo(panel.frame);
        pack();
        Point point = getLocation();
        point.setLocation(point.getX() - getWidth() / 2, point.getY() - getHeight() / 2);
        setLocation(point);
        setVisible(true);
    }

    /**
	 *  disposes the Dialog
	 */
    public void exit() {
        setVisible(false);
        dispose();
        panel.frame.repaint();
    }

    /**
	 *  the method that is called by the ActionListener
	 *
	 *@param  e  the ActionEvent
	 */
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (panel.mode == panel.FTP) {
            if (cmd.equals("Resume")) {
                panel.ftpSession.ftp.rest = rest;
                exit();
            } else if (cmd.equals("Overwrite")) {
                panel.ftpSession.ftp.rest = 0;
                exit();
            } else if (cmd.equals("Cancel")) {
                panel.ftpSession.ftp.rest = -1;
                exit();
            }
        } else {
            if (cmd.equals("Resume")) {
                panel.frame.copyFile.rest = rest;
                exit();
            } else if (cmd.equals("Overwrite")) {
                panel.frame.copyFile.rest = 0;
                exit();
            } else if (cmd.equals("Cancel")) {
                panel.frame.copyFile.rest = -1;
                exit();
            }
        }
    }

    /**
	 *  Description of the Method
	 *
	 *@param  x       Description of the Parameter
	 *@param  y       Description of the Parameter
	 *@param  width   Description of the Parameter
	 *@param  height  Description of the Parameter
	 *@return         Description of the Return Value
	 */
    private GridBagConstraints makegbc(int x, int y, int width, int height) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        gbc.weightx = 100;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(1, 1, 1, 1);
        return gbc;
    }
}
