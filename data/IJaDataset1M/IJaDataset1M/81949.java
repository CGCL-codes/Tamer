package com.mockturtlesolutions.snifflib.flatfiletools.workbench;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.Box;
import java.awt.GridLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Container;
import java.awt.event.*;
import java.awt.Dimension;
import java.util.Vector;

public class FlatFileToolsHelpFrame extends JFrame {

    private String name;

    private JTextField nameText;

    private JLabel nameLabel;

    private Container contentPane;

    private Vector okListeners;

    private Vector cancelListeners;

    private JButton okButton;

    private JButton cancelButton;

    public FlatFileToolsHelpFrame() {
        super("FlatFileCenter Help");
        this.setSize(500, 300);
        this.contentPane = this.getContentPane();
        this.contentPane.setLayout(new BoxLayout(this.contentPane, BoxLayout.Y_AXIS));
        this.okListeners = new Vector();
        this.cancelListeners = new Vector();
        JPanel aboutPanel = new JPanel(new GridLayout(1, 1));
        JTextArea ta = new JTextArea(15, 72);
        JScrollPane jsp = new JScrollPane(ta);
        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setCaretPosition(0);
        String msg = "This is FlatFileCenter version 0.1\n\nCopyright (C) 2009  Daniel P. Dougherty\n\n.";
        ta.setText(msg);
        aboutPanel.add(jsp);
        JPanel licensePanel = new JPanel(new GridLayout(1, 1));
        ta = new JTextArea(15, 72);
        jsp = new JScrollPane(ta);
        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        msg = " Copyright (C) 2009  Daniel P. Dougherty\n\nThis program is free  software; you can redistribute it and/or modify it under the terms of  the GNU General Public License as published by the Free Software  Foundation; either version 2 of the License, or (at your option) any  later version.\n\nThis program is distributed in the hope that it will be useful, but  WITHOUT ANY WARRANTY; without even the implied warranty of  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU  General Public License for more details.\n\nYou should have received a copy of the GNU General Public License  along with this program; if not, write to the Free Software  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307   USA.\n\n ";
        ta.setText(msg);
        ta.setCaretPosition(0);
        licensePanel.add(jsp);
        JPanel docPanel = new JPanel(new GridLayout(1, 1));
        ta = new JTextArea(15, 72);
        jsp = new JScrollPane(ta);
        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        msg = "---------------         FlatFileCenter        --------------\n\n";
        msg = msg + "FlatFileCenter is a repository based framework for\n";
        msg = msg + "creating and sharing distributed text file data sets.  An\n";
        msg = msg + "ASCII or UTF text file is often called a 'flat file' if it is\n";
        msg = msg + "human readable and represents data in a tabular way.\n\n";
        msg = msg + "A wide variety of files including tab-delimited and \n";
        msg = msg + "comma-delimited (CSV) files can be parsed into data.\n";
        msg = msg + "FlatFileCenter allows flat files to be identified either\n";
        msg = msg + "by the path to a local file or over the internet via a\n";
        msg = msg + "URL.\n\n";
        msg = msg + "Multiple flat file specifications sharing a similar formats\n";
        msg = msg + "formats can be associated together within a flat file set.\n";
        msg = msg + "A flat file set can be accessed transparently by an end-\n";
        msg = msg + "user as if it were a single flat file.\n\n";
        msg = msg + "Flat file specifications and flat file sets may be stored\n";
        msg = msg + "within repositories and shared among multiple remote\n";
        msg = msg + "users.\n";
        ta.setText(msg);
        ta.setCaretPosition(0);
        docPanel.add(jsp);
        JTabbedPane jtb = new JTabbedPane();
        jtb.addTab("Documentation", docPanel);
        jtb.addTab("About", aboutPanel);
        jtb.addTab("License", licensePanel);
        this.okButton = new JButton("Ok");
        this.cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < cancelListeners.size(); i++) {
                    ((ActionListener) cancelListeners.get(i)).actionPerformed(e);
                }
            }
        });
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < okListeners.size(); i++) {
                    ((ActionListener) okListeners.get(i)).actionPerformed(e);
                }
            }
        });
        Box mainBox = Box.createHorizontalBox();
        mainBox.add(jtb);
        Box controlBox = Box.createHorizontalBox();
        controlBox.add(Box.createHorizontalGlue());
        controlBox.add(this.okButton);
        controlBox.add(this.cancelButton);
        controlBox.add(Box.createHorizontalGlue());
        this.add(mainBox);
        this.add(controlBox);
        this.setVisible(false);
    }

    public void addOkListener(ActionListener listen) {
        this.okListeners.add(listen);
    }

    public void addCancelListener(ActionListener listen) {
        this.cancelListeners.add(listen);
    }

    public void removeOkListener(ActionListener listen) {
        this.okListeners.remove(listen);
    }

    public void removeCancelListener(ActionListener listen) {
        this.cancelListeners.remove(listen);
    }

    public void closeAndSave() {
        this.setVisible(false);
    }

    public void closeAndCancel() {
        this.setVisible(false);
    }

    public void showError(String msg) {
        JOptionPane dialog = new JOptionPane();
        JTextArea area = new JTextArea(msg);
        JScrollPane jsp = new JScrollPane(area);
        jsp.setPreferredSize(new Dimension(500, 200));
        dialog.showMessageDialog(this, jsp, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
