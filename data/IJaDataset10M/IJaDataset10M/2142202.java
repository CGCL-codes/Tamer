package com.oz.lanslim.gui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimLogger;
import com.oz.lanslim.model.SlimContactList;
import com.oz.lanslim.model.SlimModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
*/
public class NewCategoryFrame extends JDialog implements ActionListener {

    private JLabel nameLabel;

    private JTextField nameField;

    private JPanel buttonPanel;

    private JButton cancelButton;

    private JButton okButton;

    private SlimModel model;

    private String categoryName;

    public NewCategoryFrame(Frame pParent, SlimModel pModel, String pCategoryName) {
        super(pParent, true);
        model = pModel;
        categoryName = pCategoryName;
        initGUI();
    }

    private void initGUI() {
        try {
            if (categoryName == null) {
                setTitle(Externalizer.getString("LANSLIM.67"));
            } else {
                setTitle(Externalizer.getString("LANSLIM.68"));
            }
            FormLayout thisLayout = new FormLayout("max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)", "max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)");
            getContentPane().setLayout(thisLayout);
            setSize(250, 200);
            setResizable(false);
            {
                nameLabel = new JLabel();
                getContentPane().add(nameLabel, new CellConstraints("2, 2, 1, 1, default, default"));
                nameLabel.setText(Externalizer.getString("LANSLIM.7"));
                nameLabel.setPreferredSize(new java.awt.Dimension(30, 14));
            }
            {
                nameField = new JTextField();
                getContentPane().add(nameField, new CellConstraints("4, 2, 1, 1, default, default"));
                nameField.setPreferredSize(new java.awt.Dimension(150, 20));
                if (categoryName != null) {
                    nameField.setText(categoryName);
                }
            }
            {
                buttonPanel = new JPanel();
                okButton = new JButton();
                okButton.setText(Externalizer.getString("LANSLIM.15"));
                okButton.setActionCommand(NewCategoryActionCommand.OK);
                okButton.addActionListener(this);
                cancelButton = new JButton();
                cancelButton.setText(Externalizer.getString("LANSLIM.16"));
                cancelButton.setActionCommand(NewCategoryActionCommand.CANCEL);
                cancelButton.addActionListener(this);
                buttonPanel.add(okButton);
                buttonPanel.add(cancelButton);
                getContentPane().add(buttonPanel, new CellConstraints("4, 4, 1, 1, default, default"));
            }
        } catch (Exception e) {
            SlimLogger.logException("NewGroupFrame.initGUI", e);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(NewCategoryActionCommand.OK)) {
            String lNewName = nameField.getText();
            if (lNewName == null || lNewName.length() == 0) {
                JOptionPane.showMessageDialog(getRootPane().getParent(), Externalizer.getString("LANSLIM.70"), Externalizer.getString("LANSLIM.18"), JOptionPane.WARNING_MESSAGE);
            } else {
                if (lNewName.equals(SlimContactList.CATEGORY_GROUP) || lNewName.equals(SlimContactList.CATEGORY_UNDEFINED)) {
                    JOptionPane.showMessageDialog(getRootPane().getParent(), Externalizer.getString("LANSLIM.71", SlimContactList.CATEGORY_GROUP, SlimContactList.CATEGORY_UNDEFINED), Externalizer.getString("LANSLIM.18"), JOptionPane.WARNING_MESSAGE);
                } else {
                    if (model.getContacts().getAllCategories().contains(lNewName)) {
                        JOptionPane.showMessageDialog(getRootPane().getParent(), Externalizer.getString("LANSLIM.72"), Externalizer.getString("LANSLIM.18"), JOptionPane.WARNING_MESSAGE);
                    } else {
                        if (categoryName == null) {
                            model.getContacts().addCategory(lNewName, new Boolean(true));
                        } else {
                            if (categoryName.equals(SlimContactList.CATEGORY_GROUP) || categoryName.equals(SlimContactList.CATEGORY_UNDEFINED)) {
                                JOptionPane.showMessageDialog(getRootPane().getParent(), Externalizer.getString("LANSLIM.185", SlimContactList.CATEGORY_GROUP, SlimContactList.CATEGORY_UNDEFINED), Externalizer.getString("LANSLIM.18"), JOptionPane.WARNING_MESSAGE);
                            } else {
                                model.getContacts().renameCategory(categoryName, lNewName);
                            }
                        }
                        setVisible(false);
                    }
                }
            }
        } else if (e.getActionCommand().equals(NewCategoryActionCommand.CANCEL)) {
            setVisible(false);
        }
    }

    private class NewCategoryActionCommand {

        private static final String OK = "OK";

        private static final String CANCEL = "CANCEL";
    }
}
