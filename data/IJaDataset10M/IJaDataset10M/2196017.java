package com.marchingcube.datatable;

import com.marchingcube.datatable.*;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.List;
import java.util.Vector;
import java.sql.*;
import java.io.*;

public class DTPropertyWindow extends JDialog implements ActionListener {

    private DataTableWindow window;

    private JScrollPane propPane;

    private JTable propTable;

    private JButton okButton;

    private JButton cancelButton;

    private JButton saveButton;

    private List titles;

    private List data;

    static String OK = "OK";

    static String CANCEL = "Cancel";

    static String SAVE = "Save";

    static final int WINDOW_WIDTH = 420;

    static final int WINDOW_HEIGHT = 360;

    public DTPropertyWindow(DataTableWindow owner, String title, List titles, List data) {
        super(owner, title);
        window = owner;
        this.data = data;
        this.titles = titles;
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(getPropertyScrollPane(), BorderLayout.CENTER);
        getContentPane().add(getButtonsPanel(), BorderLayout.SOUTH);
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        saveButton.addActionListener(this);
        pack();
        setBounds(40, 40, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public void updateUi() {
        SwingUtilities.updateComponentTreeUI(this);
        pack();
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals(OK)) {
            dispose();
        } else if (command.equals(CANCEL)) {
            dispose();
        } else if (command.equals(SAVE)) {
            JFileChooser fileChooser = new JFileChooser();
            int returnVal = fileChooser.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                this.window.getDocument().saveTabbedTextDocument(file.getAbsolutePath(), this.data);
            }
        }
    }

    public JScrollPane getPropertyScrollPane() {
        if (propPane == null) {
            propPane = new JScrollPane(getPropertyTable(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        }
        return propPane;
    }

    public JTable getPropertyTable() {
        if (propTable == null) {
            propTable = new JTable(new DefaultTableModel(new Vector(data), new Vector(titles)));
            propTable.setShowGrid(true);
            propTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            propTable.setIntercellSpacing(new Dimension(4, 4));
        }
        return propTable;
    }

    private JPanel getButtonsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel panela = new JPanel(new FlowLayout());
        JPanel p2 = new JPanel(new GridLayout(1, 0, 6, 6));
        p2.add(getCancelButton());
        p2.add(getOkButton());
        panela.add(p2);
        JPanel panelb = new JPanel(new FlowLayout());
        JPanel p3 = new JPanel(new GridLayout(1, 0, 6, 6));
        p3.add(getSaveButton());
        panelb.add(p3);
        panel.add(panelb, BorderLayout.WEST);
        panel.add(panela, BorderLayout.EAST);
        return panel;
    }

    private JButton getOkButton() {
        if (okButton == null) {
            okButton = new JButton(OK);
            okButton.setMnemonic(KeyEvent.VK_O);
        }
        return okButton;
    }

    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton(CANCEL);
            cancelButton.setMnemonic(KeyEvent.VK_C);
        }
        return cancelButton;
    }

    private JButton getSaveButton() {
        if (saveButton == null) {
            saveButton = new JButton(SAVE);
            saveButton.setMnemonic(KeyEvent.VK_S);
        }
        return saveButton;
    }
}
