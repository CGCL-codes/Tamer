package com.umc.gui.scanner.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import com.umc.gui.scanner.MainFrame;
import com.umc.helper.UMCLanguage;

public class VdrDialog extends JDialog {

    private JLabel lblName;

    private JLabel lblURL;

    private JTextField txtName;

    private JTextField txtURL;

    private JButton btnAdd;

    private static final int MAX_RETRY = 3;

    public VdrDialog(final JFrame owner) {
        super(owner, UMCLanguage.getText("gui.vdr.add.title", MainFrame.selectedLanguage), true);
        lblName = new JLabel(UMCLanguage.getText("gui.vdr.add.step.1", MainFrame.selectedLanguage) + ": ");
        txtName = new JTextField(20);
        lblURL = new JLabel(UMCLanguage.getText("gui.vdr.add.step.2", MainFrame.selectedLanguage) + ": ");
        txtURL = new JTextField(20);
        btnAdd = new JButton("ok");
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 4, 0, 4);
        add(lblName, c);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 4, 0, 4);
        add(txtName, c);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 4, 0, 4);
        add(lblURL, c);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 4, 0, 4);
        add(txtURL, c);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.weightx = 2;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 4, 0, 4);
        add(btnAdd, c);
        btnAdd.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String vdrName = txtName.getText().trim();
                String vdrURL = txtURL.getText();
                if (vdrName.equals("") || vdrURL.equals("")) {
                    return;
                }
                setVisible(false);
                dispose();
            }
        });
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(owner);
        pack();
        setVisible(true);
    }

    private void login(String username, String password) throws SecurityException {
        if (!(username.equals("root") && password.equals("admin"))) {
            throw new SecurityException("Illegal Login!");
        }
    }

    public String getVdrName() {
        return txtName.getText();
    }

    public String getVdrUrl() {
        return txtURL.getText();
    }
}
