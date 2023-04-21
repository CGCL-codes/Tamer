package gov.lanl.PidsCorrelate;

import java.awt.*;
import javax.swing.*;

/**
 *		A basic implementation of the JDialog class.
 * @author $Author: dwforslund $
 * @date $Date: 2000-08-12 00:37:42 -0400 (Sat, 12 Aug 2000) $
 * @version $Revision: 4 $
 * @log $Log$
 * @log Revision 1.1  2000/08/12 04:38:28  dwforslund
 * @log Initial revision
 * @log
 * @log Revision 1.1.1.1  2000/05/30 03:51:58  dwf
 * @log initial import of OpenEMed
 * @log
 * @log Revision 1.1.1.1  2000/03/14 16:29:51  dwf
 * @log TM2000 Initial Import
 * @log
 * @log Revision 1.1  1999/08/29 04:22:56  dwf
 * @log Added PidsCorrelate client
 * @log
 * @log Revision 1.1  1999/08/27 20:18:23  tew
 * @log test client for CorrelationMgr interface
 * @log
 * @log Revision 1.1  1999/08/26 22:02:57  tew
 * @log part of the initial archiving of a "rough" GUI client to show
 * @log correlated external ids from one or more correlationMgr
 * @log Pids
 * @log
 */
public class AboutDialog extends javax.swing.JDialog {

    public AboutDialog(Frame parentFrame) {
        super(parentFrame);
        setTitle("JFC Application - About");
        setModal(true);
        getContentPane().setLayout(new GridBagLayout());
        setSize(248, 94);
        setVisible(false);
        okButton.setText("OK");
        okButton.setActionCommand("OK");
        okButton.setOpaque(false);
        okButton.setMnemonic((int) 'O');
        getContentPane().add(okButton, new GridBagConstraints());
        okButton.setBounds(98, 59, 51, 25);
        aboutLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        aboutLabel.setText("What\'s it all about...");
        getContentPane().add(aboutLabel, new GridBagConstraints());
        aboutLabel.setBounds(0, 0, 248, 59);
        SymWindow aSymWindow = new SymWindow();
        this.addWindowListener(aSymWindow);
        SymAction lSymAction = new SymAction();
        okButton.addActionListener(lSymAction);
    }

    public void setVisible(boolean b) {
        if (b) {
            Rectangle bounds = (getParent()).getBounds();
            Dimension size = getSize();
            setLocation(bounds.x + (bounds.width - size.width) / 2, bounds.y + (bounds.height - size.height) / 2);
        }
        super.setVisible(b);
    }

    public void addNotify() {
        Dimension d = getSize();
        super.addNotify();
        if (fComponentsAdjusted) return;
        Insets insets = getInsets();
        setSize(insets.left + insets.right + d.width, insets.top + insets.bottom + d.height);
        Component components[] = getContentPane().getComponents();
        for (int i = 0; i < components.length; i++) {
            Point p = components[i].getLocation();
            p.translate(insets.left, insets.top);
            components[i].setLocation(p);
        }
        fComponentsAdjusted = true;
    }

    boolean fComponentsAdjusted = false;

    javax.swing.JButton okButton = new javax.swing.JButton();

    javax.swing.JLabel aboutLabel = new javax.swing.JLabel();

    class SymWindow extends java.awt.event.WindowAdapter {

        public void windowClosing(java.awt.event.WindowEvent event) {
            Object object = event.getSource();
            if (object == AboutDialog.this) jAboutDialog_windowClosing(event);
        }
    }

    void jAboutDialog_windowClosing(java.awt.event.WindowEvent event) {
        jAboutDialog_windowClosing_Interaction1(event);
    }

    void jAboutDialog_windowClosing_Interaction1(java.awt.event.WindowEvent event) {
        try {
            this.setVisible(false);
        } catch (Exception e) {
        }
    }

    class SymAction implements java.awt.event.ActionListener {

        public void actionPerformed(java.awt.event.ActionEvent event) {
            Object object = event.getSource();
            if (object == okButton) okButton_actionPerformed(event);
        }
    }

    void okButton_actionPerformed(java.awt.event.ActionEvent event) {
        okButton_actionPerformed_Interaction1(event);
    }

    void okButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event) {
        try {
            this.setVisible(false);
        } catch (Exception e) {
        }
    }
}
