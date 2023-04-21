package vsha.gui;

import vsha.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;

/**Diese Klasse bildet den Dialog zur Abfrage des Passwortes.*/
public class PasswortChangeDialog extends JDialog {

    private String m_sNewPassword = null;

    private String m_sConfirmPassword = null;

    private transient JOptionPane m_optionPane;

    private transient JPasswordField m_newPwdField = new JPasswordField("");

    private transient JPasswordField m_confirmPwdField = new JPasswordField("");

    private boolean pwdConfirmed = false;

    public PasswortChangeDialog(JFrame frame) {
        super(frame);
        setTitle("Passwort eingeben");
        setModal(true);
        m_sNewPassword = "";
        m_sConfirmPassword = "";
        final String Message1 = "Bitte neues Passwort eingeben !";
        final String Message2 = "Bitte Passwort best�tigen !";
        Object[] inhalt = { Message1, m_newPwdField, Message2, m_confirmPwdField };
        final String OK_Button = "OK";
        final String Abbruch_Button = "Abbruch";
        Object[] options = { OK_Button, Abbruch_Button };
        m_optionPane = new JOptionPane(inhalt, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, null, options, options[0]);
        this.setContentPane(m_optionPane);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent we) {
                m_optionPane.nextFocus();
            }
        });
        m_confirmPwdField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                m_optionPane.setValue(OK_Button);
            }
        });
        m_optionPane.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent e) {
                String prop = e.getPropertyName();
                if (isVisible() && (e.getSource() == m_optionPane) && (prop.equals(JOptionPane.VALUE_PROPERTY) || prop.equals(JOptionPane.INPUT_VALUE_PROPERTY))) {
                    Object value = m_optionPane.getValue();
                    if (value == JOptionPane.UNINITIALIZED_VALUE) {
                        return;
                    }
                    m_optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
                    if (value.equals(OK_Button)) {
                        m_sNewPassword = m_newPwdField.getText().toUpperCase();
                        m_sConfirmPassword = m_confirmPwdField.getText().toUpperCase();
                        if (m_sNewPassword.equals(m_sConfirmPassword)) {
                            pwdConfirmed = true;
                            setVisible(false);
                        } else {
                            m_newPwdField.setText("");
                            m_confirmPwdField.setText("");
                            JOptionPane.showMessageDialog(PasswortChangeDialog.this, "Die Passworte waren verschieden\nBitte erneut eingeben!", "Keine �bereinstimmung", JOptionPane.ERROR_MESSAGE);
                            m_sNewPassword = null;
                            m_sConfirmPassword = null;
                        }
                    } else {
                        m_sNewPassword = null;
                        m_sConfirmPassword = null;
                        setVisible(false);
                    }
                }
            }
        });
        pack();
        this.setLocation(300, 300);
        setLocationRelativeTo(frame);
    }

    /**Gibt true zur�ck, wenn das neue Passwort zweimal identisch eingegeben
     wurde.*/
    public boolean isConfirmed() {
        boolean confirmed = this.pwdConfirmed;
        this.pwdConfirmed = false;
        return confirmed;
    }

    /**Gibt das neue Passwort zur�ck.*/
    public String getNewPasswort() {
        String thePasswort = m_sNewPassword;
        m_sNewPassword = null;
        m_sConfirmPassword = null;
        return thePasswort;
    }
}
