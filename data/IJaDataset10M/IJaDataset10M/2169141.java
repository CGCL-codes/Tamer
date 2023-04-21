package com.baycloud.synpos.ui;

import com.baycloud.synpos.xt.Synchronizer;
import com.baycloud.synpos.util.*;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.baycloud.synpos.od.Configuration;

/**
 * <p>Title: synPOS</p>
 *
 * <p>Description: synPOS is a desktop POS (Point Of Sale) client for online
 * ERP, eCommerce, and CRM systems. Released under the GNU General Public
 * License. Absolutely no warranty. Use at your own risk.</p>
 *
 * <p>Copyright: Copyright (c) 2006 synPOS.com</p>
 *
 * <p>Website: www.synpos.com</p>
 *
 * @author H.Q.
 * @version 0.9.2
 */
public class SyncDlg extends JDialog {

    private static SyncDlg dlg;

    public static SyncDlg getInstance() {
        if (dlg == null) {
            dlg = new SyncDlg();
        }
        return dlg;
    }

    public SyncDlg() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(borderLayout1);
        jPanel1.setLayout(flowLayout1);
        flowLayout1.setAlignment(FlowLayout.LEFT);
        jPanel3.setLayout(borderLayout2);
        jLabel2.setText(I18N.getLabelString("Frequency (seconds)") + ":");
        jButton1.setMnemonic('P');
        jButton1.setText(I18N.getButtonString("Stop"));
        jButton1.addActionListener(new SyncDlg_jButton1_actionAdapter(this));
        jButton2.setMnemonic('R');
        jButton2.setText(I18N.getButtonString("Start"));
        jButton2.addActionListener(new SyncDlg_jButton2_actionAdapter(this));
        jTextField1.setColumns(10);
        this.setTitle(I18N.getLabelString("Synchronizer"));
        jButton3.setMnemonic('T');
        jButton3.setText(I18N.getButtonString("Set"));
        jButton3.addActionListener(new SyncDlg_jButton3_actionAdapter(this));
        jPanel1.add(jLabel1);
        jLabel1.setText(I18N.getLabelString("Message"));
        jPanel2.add(jProgressBar1);
        this.getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);
        this.getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);
        jPanel3.add(jPanel4, java.awt.BorderLayout.WEST);
        jPanel4.add(jLabel2);
        jPanel4.add(jTextField1);
        jPanel5.add(jButton2);
        jPanel5.add(jButton1);
        jPanel3.add(jPanel5, java.awt.BorderLayout.EAST);
        this.getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);
        jPanel4.add(jButton3);
        jProgressBar1.setValue(0);
        jProgressBar1.setStringPainted(true);
        task = new Synchronizer();
        timer = new Timer(ONE_SECOND, new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                jProgressBar1.setValue(task.getCurrent());
                String s = task.getMessage();
                if (s != null) {
                    jLabel1.setText(s);
                    validate();
                    repaint();
                }
                if (task.isDone()) {
                    timer.stop();
                    jButton2.setEnabled(true);
                    jButton1.setEnabled(false);
                    jProgressBar1.setValue(jProgressBar1.getMinimum());
                }
            }
        });
        String freqStr = Configuration.get("auto.sync");
        if (freqStr != null) {
            jTextField1.setText(freqStr);
            jButton2.setEnabled(false);
            jButton1.setEnabled(true);
            task.go();
            timer.start();
        } else {
            jButton2.setEnabled(true);
            jButton1.setEnabled(false);
        }
    }

    public static final int ONE_SECOND = 1000;

    Timer timer;

    Synchronizer task;

    BorderLayout borderLayout1 = new BorderLayout();

    JPanel jPanel1 = new JPanel();

    JPanel jPanel2 = new JPanel();

    JPanel jPanel3 = new JPanel();

    JLabel jLabel1 = new JLabel();

    FlowLayout flowLayout1 = new FlowLayout();

    JProgressBar jProgressBar1 = new JProgressBar();

    BorderLayout borderLayout2 = new BorderLayout();

    JPanel jPanel4 = new JPanel();

    JPanel jPanel5 = new JPanel();

    JLabel jLabel2 = new JLabel();

    JTextField jTextField1 = new JTextField();

    JButton jButton1 = new JButton();

    JButton jButton2 = new JButton();

    JButton jButton3 = new JButton();

    public void jButton1_actionPerformed(ActionEvent e) {
        jButton2.setEnabled(true);
        jButton1.setEnabled(false);
        task.stop();
        timer.stop();
        Configuration.delete("auto.sync");
    }

    public void jButton2_actionPerformed(ActionEvent e) {
        String freqStr = Configuration.get("auto.sync");
        if (freqStr == null) {
            freqStr = jTextField1.getText().trim();
            try {
                int freq = Integer.parseInt(freqStr);
                if (freq > 0) {
                    Configuration.set("auto.sync", freqStr);
                } else {
                    JOptionPane.showMessageDialog(this, I18N.getLabelString("Frequency must be greater than zero."), I18N.getLabelString("Error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), I18N.getLabelString("Error"), JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        jButton2.setEnabled(false);
        jButton1.setEnabled(true);
        task.go();
        timer.start();
    }

    public void jButton3_actionPerformed(ActionEvent e) {
        String freqStr = jTextField1.getText().trim();
        try {
            int freq = Integer.parseInt(freqStr);
            if (freq > 0) {
                Configuration.set("auto.sync", freqStr);
                JOptionPane.showMessageDialog(this, I18N.getLabelString("Frequency has been changed."), I18N.getLabelString("Message"), JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, I18N.getMessageString("Frequency must be greater than zero."), I18N.getLabelString("Error"), JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), I18N.getLabelString("Error"), JOptionPane.ERROR_MESSAGE);
        }
    }
}

class SyncDlg_jButton3_actionAdapter implements ActionListener {

    private SyncDlg adaptee;

    SyncDlg_jButton3_actionAdapter(SyncDlg adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jButton3_actionPerformed(e);
    }
}

class SyncDlg_jButton2_actionAdapter implements ActionListener {

    private SyncDlg adaptee;

    SyncDlg_jButton2_actionAdapter(SyncDlg adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jButton2_actionPerformed(e);
    }
}

class SyncDlg_jButton1_actionAdapter implements ActionListener {

    private SyncDlg adaptee;

    SyncDlg_jButton1_actionAdapter(SyncDlg adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jButton1_actionPerformed(e);
    }
}
