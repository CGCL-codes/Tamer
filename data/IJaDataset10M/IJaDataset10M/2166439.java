package com.marchingcube.datatable;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.UIManager.*;

public class DTPreferencesDialog extends JDialog implements ActionListener {

    private DataTableWindow window;

    private DTPreferences preferences;

    private JPanel uiPanel;

    private JComboBox uiControl;

    private JPanel tablePanel;

    private JTextField rowHeightField;

    private JTextField horPadField;

    private JTextField verPadField;

    private UIManager.LookAndFeelInfo[] lafs;

    private JButton okButton;

    private JButton cancelButton;

    static String UI_TAB = "Interface";

    static String TABLE_TAB = "Table";

    static String OK = "OK";

    static String CANCEL = "Cancel";

    static final int WINDOW_WIDTH = 346;

    static final int WINDOW_HEIGHT = 186;

    public DTPreferencesDialog(DataTableWindow owner, String title) {
        super(owner, title);
        window = owner;
        preferences = window.getDocument().getApp().getPreferences();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(getTabbedPanel(), BorderLayout.NORTH);
        getContentPane().add(getButtonsPanel(), BorderLayout.SOUTH);
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        pack();
        setBounds(40, 40, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals(OK)) {
            doPreferenceAction();
            hide();
        } else if (command.equals(CANCEL)) {
            hide();
        }
    }

    public void doPreferenceAction() {
        int i = uiControl.getSelectedIndex();
        if (i > -1) updateAppUi(i);
        updateTable();
        preferences.savePreferences();
    }

    public void updateAppUi(int index) {
        try {
            UIManager.setLookAndFeel(lafs[index].getClassName());
            window.getDocument().getApp().updateUI();
            preferences.setLookAndFeel(lafs[index].getClassName());
        } catch (Exception e) {
            System.out.println("DTPreferencesDialog.updateAppUi...");
            e.printStackTrace();
        }
    }

    public void updateUi() {
        SwingUtilities.updateComponentTreeUI(this);
        pack();
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public void updateTable() {
        try {
            int rowHeight = Integer.parseInt(getRowHeightField().getText());
            int horPad = Integer.parseInt(getHorPadField().getText());
            int verPad = Integer.parseInt(getVerPadField().getText());
            window.getDataTable().setIntercellSpacing(new Dimension(horPad, verPad));
            window.getDataTable().setRowHeight(rowHeight);
            preferences.setDefaultTableRowHeight(rowHeight);
            preferences.setDefaultHorizontalPadding(horPad);
            preferences.setDefaultVerticalPadding(verPad);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private JTabbedPane getTabbedPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add(UI_TAB, getUIPrefTab());
        tabbedPane.add(TABLE_TAB, getTablePrefTab());
        return tabbedPane;
    }

    private JPanel getUIPrefTab() {
        if (uiPanel == null) {
            uiPanel = new JPanel();
            uiPanel.add(getUiControl());
        }
        return uiPanel;
    }

    private JComboBox getUiControl() {
        if (uiControl == null) {
            uiControl = new JComboBox();
            String currentLaf = UIManager.getLookAndFeel().getClass().getName();
            lafs = UIManager.getInstalledLookAndFeels();
            for (int i = 0; i < lafs.length; i++) {
                uiControl.addItem(lafs[i].getName());
                if (currentLaf.equals(lafs[i].getClassName())) uiControl.setSelectedIndex(i);
            }
        }
        return uiControl;
    }

    private JPanel getTablePrefTab() {
        int flowHgap = 4;
        int flowVgap = 6;
        int borderHgap = 4;
        int borderVgap = 4;
        int gridRows = 3;
        int gridCols = 1;
        int gridHgap = 2;
        int gridVgap = 4;
        JPanel tablePanel = new JPanel(new BorderLayout(borderHgap, borderVgap));
        JPanel container = new JPanel(new BorderLayout(borderHgap, borderVgap));
        JPanel labels = new JPanel(new FlowLayout(FlowLayout.LEFT, flowHgap, flowVgap));
        JPanel lc = new JPanel(new GridLayout(gridRows, gridCols, gridHgap, gridVgap + 2));
        lc.add(new JLabel("Row Height:", JLabel.RIGHT));
        lc.add(new JLabel("H. Padding:", JLabel.RIGHT));
        lc.add(new JLabel("V. Padding:", JLabel.RIGHT));
        labels.add(lc);
        JPanel textfields = new JPanel(new FlowLayout(FlowLayout.LEFT, flowHgap, flowVgap));
        JPanel tc = new JPanel(new GridLayout(gridRows, gridCols, gridHgap, gridVgap));
        tc.add(getRowHeightField());
        tc.add(getHorPadField());
        tc.add(getVerPadField());
        textfields.add(tc);
        container.add(labels, BorderLayout.WEST);
        container.add(textfields, BorderLayout.CENTER);
        tablePanel.add(container, BorderLayout.NORTH);
        return tablePanel;
    }

    private JTextField getRowHeightField() {
        if (rowHeightField == null) {
            rowHeightField = new JTextField(String.valueOf(preferences.getDefaultTableRowHeight()), 5);
        }
        return rowHeightField;
    }

    private JTextField getHorPadField() {
        if (horPadField == null) {
            horPadField = new JTextField(String.valueOf(preferences.getDefaultHorizontalPadding()), 5);
        }
        return horPadField;
    }

    private JTextField getVerPadField() {
        if (verPadField == null) {
            verPadField = new JTextField(String.valueOf(preferences.getDefaultVerticalPadding()), 5);
        }
        return verPadField;
    }

    private JPanel getButtonsPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JPanel p2 = new JPanel();
        p2.setLayout(new GridLayout(1, 0, 6, 6));
        p2.add(getCancelButton());
        p2.add(getOkButton());
        p.add(p2, BorderLayout.EAST);
        return p;
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
}
