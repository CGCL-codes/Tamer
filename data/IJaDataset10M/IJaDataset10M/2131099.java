package org.sulweb.infumon.common;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class DeleteDlg extends JDialog {

    private JTextArea message;

    private JTextField confirmCode;

    private JLabel labelCC;

    private String code;

    private JButton ok, cancel;

    private boolean confirmed;

    public DeleteDlg(Frame owner, String strmessage) {
        super(owner, true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        message = new JTextArea(5, 50);
        message.setLineWrap(true);
        message.setText(strmessage);
        message.setEditable(false);
        message.setBackground(getBackground());
        int icode = ((int) (Math.random() * (Integer.MAX_VALUE >> 1))) + ((Integer.MAX_VALUE >> 1) - 1);
        code = Integer.toHexString(icode);
        labelCC = new JLabel("Codice di conferma: " + code);
        confirmCode = new JTextField(code.length());
        ok = new JButton("S�");
        cancel = new JButton("No");
        Container cpane = getContentPane();
        GridBagLayout gbl = new GridBagLayout();
        cpane.setLayout(gbl);
        GridBagConstraints gbc = new GridBagConstraints(0, 0, 3, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0);
        cpane.add(message, gbc);
        gbc.gridy++;
        gbc.gridwidth = 2;
        cpane.add(labelCC, gbc);
        gbc.gridx += 2;
        gbc.gridwidth = 1;
        cpane.add(confirmCode, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        cpane.add(new JSeparator(JSeparator.HORIZONTAL), gbc);
        gbc.gridx++;
        cpane.add(new JSeparator(JSeparator.HORIZONTAL), gbc);
        gbc.gridx++;
        cpane.add(new JSeparator(JSeparator.HORIZONTAL), gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        cpane.add(ok, gbc);
        gbc.gridx++;
        gbc.gridwidth = 2;
        cpane.add(cancel, gbc);
        cancel.setFocusable(true);
        ok.setFocusable(false);
        cancel.setSelected(true);
        ok.setSelected(false);
        cancel.setDefaultCapable(true);
        ok.setDefaultCapable(false);
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                checkCode();
            }
        });
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                dispose();
            }
        });
        pack();
    }

    private void checkCode() {
        if (code.equals(confirmCode.getText())) confirmed = true; else JOptionPane.showMessageDialog(this, "Il codice di conferma non corrisponde", "Configuratore", JOptionPane.OK_OPTION);
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
