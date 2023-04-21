package org.dbreplicator.repconsole;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import org.dbreplicator.replication.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class Push extends JDialog implements FocusListener, KeyListener {

    JPanel panel1 = new JPanel();

    JLabel jLabel1 = new JLabel();

    JTextField jTextSubName = new JTextField();

    JButton jButtonPush = new JButton();

    JButton jButtonCancle = new JButton();

    _ReplicationServer repServer;

    JLabel jLabel2 = new JLabel();

    JLabel jLabel4 = new JLabel();

    JTextField jTextRemoteServerName = new JTextField();

    JTextField jTextRemoteRepPortNo = new JTextField();

    JLabel jLabel3 = new JLabel();

    JLabel jLabel6 = new JLabel();

    JEditorPane help = new JEditorPane();

    String subName;

    boolean able = true;

    _Subscription sub = null;

    Border border1;

    public Push(Frame frame, String title, boolean modal, boolean able0) {
        super(frame, title, modal);
        able = able0;
        try {
            jbInit();
            pack();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex, "Error Message", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    public Push(_ReplicationServer repServer0, boolean able0) {
        this(StartRepServer.getMainFrame(), "Push", true, able0);
        repServer = repServer0;
        able = able0;
    }

    public Push(_ReplicationServer repServer0, String subName0, boolean able0) {
        this(null, "Push", true, able0);
        repServer = repServer0;
        subName = subName0;
        able = able0;
        init();
    }

    private void init() {
        try {
            sub = repServer.getSubscription(subName);
            jTextSubName.setText(subName);
            jTextSubName.setEnabled(able);
            jTextRemoteRepPortNo.setText("" + sub.getRemoteServerPortNo());
            jTextRemoteServerName.setText(sub.getRemoteServerUrl());
            if (!jTextRemoteRepPortNo.getText().trim().equalsIgnoreCase("") && !jTextRemoteServerName.getText().trim().equalsIgnoreCase("")) {
                jButtonPush.setEnabled(true);
            }
        } catch (RepException ex) {
            JOptionPane.showMessageDialog(this, ex, "Error Message", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    private void jbInit() throws Exception {
        border1 = new EtchedBorder(EtchedBorder.RAISED, Color.white, new Color(148, 145, 140));
        panel1.setLayout(null);
        jLabel1.setFont(new java.awt.Font("Dialog", 1, 13));
        jLabel1.setToolTipText("");
        jLabel1.setHorizontalAlignment(SwingConstants.LEFT);
        jLabel1.setText("Subscription Name");
        jLabel1.setBounds(new Rectangle(20, 65, 141, 23));
        panel1.setAlignmentY((float) 0.5);
        panel1.setDebugGraphicsOptions(0);
        jTextSubName.setFont(new java.awt.Font("Dialog", 0, 12));
        jTextSubName.setText("");
        jTextSubName.setBounds(new Rectangle(186, 63, 147, 23));
        jButtonPush.setBounds(new Rectangle(111, 211, 116, 25));
        jButtonPush.setEnabled(false);
        jButtonPush.setFont(new java.awt.Font("Dialog", 1, 12));
        jButtonPush.setText("Push");
        jButtonPush.addActionListener(new Push_jButtonPush_actionAdapter(this));
        jButtonCancle.setBounds(new Rectangle(236, 211, 116, 25));
        jButtonCancle.setFont(new java.awt.Font("Dialog", 1, 12));
        jButtonCancle.setText("Cancel");
        jButtonCancle.addActionListener(new Push_jButtonCancle_actionAdapter(this));
        jLabel2.setFont(new java.awt.Font("Serif", 3, 25));
        jLabel2.setForeground(SystemColor.infoText);
        jLabel2.setVerifyInputWhenFocusTarget(true);
        jLabel2.setText("Push");
        jLabel2.setBounds(new Rectangle(155, 12, 167, 27));
        jLabel4.setFont(new java.awt.Font("Dialog", 1, 13));
        jLabel4.setRequestFocusEnabled(true);
        jLabel4.setHorizontalAlignment(SwingConstants.LEFT);
        jLabel4.setText("Publication Server Name");
        jLabel4.setBounds(new Rectangle(20, 91, 165, 23));
        jTextRemoteServerName.setFont(new java.awt.Font("Dialog", 0, 12));
        jTextRemoteServerName.setText("");
        jTextRemoteServerName.setBounds(new Rectangle(186, 90, 147, 23));
        jTextRemoteRepPortNo.setFont(new java.awt.Font("Dialog", 0, 12));
        jTextRemoteRepPortNo.setText("");
        jTextRemoteRepPortNo.setBounds(new Rectangle(186, 117, 147, 23));
        jLabel3.setFont(new java.awt.Font("Dialog", 1, 13));
        jLabel3.setRequestFocusEnabled(true);
        jLabel3.setHorizontalAlignment(SwingConstants.LEFT);
        jLabel3.setText("Publication Port Number");
        jLabel3.setBounds(new Rectangle(20, 119, 161, 23));
        jLabel6.setBorder(border1);
        jLabel6.setPreferredSize(new Dimension(2, 2));
        jLabel6.setToolTipText("");
        jLabel6.setText("");
        jLabel6.setVerticalAlignment(SwingConstants.CENTER);
        jLabel6.setBounds(new Rectangle(12, 51, 340, 104));
        jTextRemoteRepPortNo.setDocument(new NumericDocument());
        jTextRemoteRepPortNo.addKeyListener(this);
        jTextRemoteServerName.addKeyListener(this);
        jTextSubName.addKeyListener(this);
        jTextRemoteRepPortNo.addFocusListener(this);
        jTextRemoteServerName.addFocusListener(this);
        jTextSubName.addFocusListener(this);
        jTextSubName.grabFocus();
        help.setBackground(UIManager.getColor("Button.background"));
        help.setEnabled(false);
        help.setFont(new java.awt.Font("Dialog", 2, 12));
        help.setAlignmentY((float) 0.5);
        help.setMinimumSize(new Dimension(352, 21));
        help.setDisabledTextColor(Color.black);
        help.setText(" The changes made to the data by Publisher" + "will be uploaded to Subscriber");
        help.setBounds(new Rectangle(12, 159, 339, 35));
        panel1.add(help, null);
        panel1.add(jTextRemoteServerName, null);
        panel1.add(jTextRemoteRepPortNo, null);
        panel1.add(jTextSubName, null);
        panel1.add(jLabel4, null);
        panel1.add(jLabel1, null);
        panel1.add(jLabel3, null);
        panel1.add(jLabel6, null);
        panel1.add(jButtonCancle, null);
        panel1.add(jButtonPush, null);
        panel1.add(jLabel2, null);
        this.getContentPane().add(panel1, BorderLayout.CENTER);
    }

    void jButtonCancle_actionPerformed(ActionEvent e) {
        hide();
    }

    void jButtonPush_actionPerformed(ActionEvent e) {
        try {
            String subName = jTextSubName.getText().trim();
            if (subName.equalsIgnoreCase("")) {
                throw new RepException("REP093", new Object[] { null });
            }
            String port = jTextRemoteRepPortNo.getText().trim();
            if (port.equalsIgnoreCase("")) {
                throw new RepException("REP094", new Object[] { null });
            }
            int remoteRepPortNo = Integer.valueOf(port).intValue();
            if (remoteRepPortNo <= 0) {
                throw new NumberFormatException();
            }
            String remoteServerName = jTextRemoteServerName.getText().trim();
            if (remoteServerName.equalsIgnoreCase("")) {
                throw new RepException("REP095", new Object[] { null });
            }
            sub = repServer.getSubscription(subName);
            if (sub == null) {
                throw new RepException("REP0107", new Object[] { subName });
            }
            sub.setRemoteServerPortNo(remoteRepPortNo);
            sub.setRemoteServerUrl(remoteServerName);
            sub.push();
            JOptionPane.showMessageDialog(this, "Pushed Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            hide();
        } catch (RepException ex) {
            JOptionPane.showMessageDialog(this, ex, "Error Message", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (NumberFormatException ex2) {
            JOptionPane.showMessageDialog(this, "Enter only Positive Integer in Server Port No", "Error Message", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception ex1) {
            JOptionPane.showMessageDialog(this, ex1, "Error Message", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    public void focusGained(FocusEvent fe) {
        if (((JTextField) fe.getSource()).equals(jTextRemoteRepPortNo)) {
            help.setText("Enter Port number in this box");
        } else if (((JTextField) fe.getSource()).equals(jTextSubName)) {
            help.setText("Enter Subscription Name in this box");
        } else if (((JTextField) fe.getSource()).equals(jTextRemoteServerName)) {
            help.setText("Enter Server Name in this box");
        }
        if (!(jTextRemoteRepPortNo.getText().equals("") || jTextRemoteServerName.getText().equals("") || jTextSubName.getText().equals(""))) {
            jButtonPush.setEnabled(true);
        }
    }

    public void focusLost(FocusEvent fe) {
        jButtonPush.setEnabled(true);
        if (jTextRemoteRepPortNo.getText().equals("") || jTextRemoteServerName.getText().equals("") || jTextSubName.getText().equals("")) {
            jButtonPush.setEnabled(false);
        }
    }

    public void keyTyped(KeyEvent keyEvent) {
    }

    public void keyPressed(KeyEvent keyEvent) {
    }

    public void keyReleased(KeyEvent keyEvent) {
        jButtonPush.setEnabled(true);
        if (jTextRemoteRepPortNo.getText().equals("") || jTextRemoteServerName.getText().equals("") || jTextSubName.getText().equals("")) {
            jButtonPush.setEnabled(false);
        } else {
            if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                jButtonPush_actionPerformed(null);
            }
        }
    }
}

class Push_jButtonCancle_actionAdapter implements java.awt.event.ActionListener {

    Push adaptee;

    Push_jButtonCancle_actionAdapter(Push adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jButtonCancle_actionPerformed(e);
    }
}

class Push_jButtonPush_actionAdapter implements java.awt.event.ActionListener {

    Push adaptee;

    Push_jButtonPush_actionAdapter(Push adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jButtonPush_actionPerformed(e);
    }
}
