package medisnap.gui;

import java.awt.*;
import java.awt.event.*;
import medisnap.MediSnap;
import medisnap.MediSnapOptions;
import java.util.*;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Timer;
import javax.swing.event.*;
import javax.imageio.*;
import java.io.*;
import javax.swing.*;
import medisnap.dblayer.*;
import java.util.logging.*;

/**
 *
 * @author  Elle
 */
public class Startscreen extends panelBaseClass implements medisnap.gui.isvisible {

    public static final long serialVersionUID = 1;

    public MediSnap m;

    private int delay;

    private int counter;

    public void isNowVisible() {
        textMediSnapPassword.requestFocusInWindow();
        m.ked = new MyDispatcher();
        m.manager.addKeyEventDispatcher(m.ked);
    }

    public void freeMemory() {
        m = null;
    }

    /**
     * Methode die die Textfelder je nach Fehler einstellt
     *
     */
    private void setPropFields(boolean properror) {
        String server = MediSnapOptions.getDatabaseURL() != null ? MediSnapOptions.getDatabaseURL() : new String();
        String user = MediSnapOptions.getDatabaseUser() != null ? MediSnapOptions.getDatabaseUser() : new String();
        if ((server.equals(new String())) || (user.equals(new String())) || (properror == true)) {
            jLabelErrorMessage.setText("Die DatenbankUrl oder der Nutzer wurden fehlerhaft oder gar nicht konfiguriert!\n Bitte prüfen sie die Entsprechenden Felder!");
        }
        if ((server.equals(new String())) || (properror == true)) {
            jLabelDBURL.setForeground(new Color(255, 0, 0));
            jLabelDBURL.setText("ServerURL (IP): ");
            jTextFieldDBURL.setEditable(true);
            jTextFieldDBURL.setText(server);
        } else {
            jTextFieldDBURL.setVisible(false);
            jLabelDBURL.setVisible(false);
            jTextFieldDBURL.setText(server);
        }
        if ((user.equals(new String())) || (properror == true)) {
            jLabelUser.setForeground(new Color(255, 0, 0));
            jLabelUser.setText("Benutzer: ");
            jTextFieldUser.setEditable(true);
            jTextFieldUser.setText(user);
        } else {
            jTextFieldUser.setVisible(false);
            jLabelUser.setVisible(false);
            jTextFieldUser.setText(user);
        }
    }

    /** Creates new form startscreen 
     *  properror gibt an ob der Startscreen wegen
     *  eines Fehlers in den Einstellungen aufgerufen wurde
     */
    public Startscreen(MediSnap _m, boolean properror) {
        delay = 1000;
        counter = 0;
        m = _m;
        m.clearPatient();
        initComponents();
        setPropFields(properror);
        doSimpleTimeFormat();
        jlVersion.setText(MediSnapOptions.getVersionString());
        javax.swing.Timer timer = new javax.swing.Timer(delay, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                doSimpleTimeFormat();
            }
        });
        m.setVisible(true);
        m.toFront();
        timer.start();
    }

    private void doSimpleTimeFormat() {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat timeformatter = new SimpleDateFormat("HH:mm:ss");
        lableUhrzeit.setForeground(new Color(18, 52, 102));
        lableUhrzeit.setText(timeformatter.format(now.getTime()));
        SimpleDateFormat dateformatter = new SimpleDateFormat("E. dd.MM.yyyy");
        lableDatum.setForeground(new Color(18, 52, 102));
        lableDatum.setText(dateformatter.format(now.getTime()));
    }

    private class MyDispatcher implements KeyEventDispatcher {

        MyDispatcher() {
        }

        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.isControlDown() && e.getID() == KeyEvent.KEY_PRESSED) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_B:
                        if (buttonBeenden.isEnabled()) {
                            buttonBeendenActionPerformed(null);
                            return true;
                        }
                        break;
                }
            }
            return false;
        }
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jlVersion = new javax.swing.JLabel();
        buttonBeenden = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        lableUhrzeit = new javax.swing.JLabel();
        lableDatum = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        LoginMain = new javax.swing.JPanel();
        labelPassword = new javax.swing.JLabel();
        textMediSnapPassword = new javax.swing.JPasswordField();
        buttonMediSnap = new javax.swing.JButton();
        jLabelUser = new javax.swing.JLabel();
        jLabelDBURL = new javax.swing.JLabel();
        jTextFieldUser = new javax.swing.JTextField();
        jTextFieldDBURL = new javax.swing.JTextField();
        jLabelErrorMessage = new javax.swing.JLabel();
        setLayout(new java.awt.BorderLayout());
        setOpaque(false);
        jPanel2.setLayout(new java.awt.BorderLayout());
        jPanel2.setOpaque(false);
        jPanel3.setLayout(new java.awt.BorderLayout());
        jPanel3.setOpaque(false);
        jPanel7.setLayout(new java.awt.GridBagLayout());
        jPanel7.setOpaque(false);
        jlVersion.setMaximumSize(new java.awt.Dimension(2147483647, 100));
        jlVersion.setMinimumSize(new java.awt.Dimension(304, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 20, 20);
        jPanel7.add(jlVersion, gridBagConstraints);
        buttonBeenden.setText("<html><nobr><u>B</u>eenden</nobr></html>");
        buttonBeenden.setMaximumSize(new java.awt.Dimension(2147483647, 100));
        buttonBeenden.setMinimumSize(new java.awt.Dimension(139, 30));
        buttonBeenden.setRequestFocusEnabled(false);
        buttonBeenden.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonBeendenActionPerformed(evt);
            }
        });
        buttonBeenden.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyTyped(java.awt.event.KeyEvent evt) {
                buttonBeendenKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 25, 40, 20);
        jPanel7.add(buttonBeenden, gridBagConstraints);
        jPanel3.add(jPanel7, java.awt.BorderLayout.SOUTH);
        jPanel2.add(jPanel3, java.awt.BorderLayout.EAST);
        add(jPanel2, java.awt.BorderLayout.SOUTH);
        jPanel5.setLayout(new java.awt.BorderLayout());
        jPanel5.setOpaque(false);
        jPanel6.setLayout(new java.awt.GridBagLayout());
        jPanel6.setOpaque(false);
        lableUhrzeit.setFont(new java.awt.Font("Verdana", 1, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 5, 30);
        jPanel6.add(lableUhrzeit, gridBagConstraints);
        lableDatum.setFont(new java.awt.Font("Verdana", 1, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 30);
        jPanel6.add(lableDatum, gridBagConstraints);
        jPanel5.add(jPanel6, java.awt.BorderLayout.EAST);
        add(jPanel5, java.awt.BorderLayout.NORTH);
        jPanel1.setLayout(new java.awt.GridLayout());
        jPanel1.setOpaque(false);
        jPanel4.setOpaque(false);
        LoginMain.setLayout(new java.awt.GridBagLayout());
        LoginMain.setBorder(new javax.swing.border.TitledBorder("Login:"));
        LoginMain.setOpaque(false);
        labelPassword.setLabelFor(textMediSnapPassword);
        labelPassword.setText("Passwort: ");
        labelPassword.setFocusable(false);
        labelPassword.setVerifyInputWhenFocusTarget(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        LoginMain.add(labelPassword, gridBagConstraints);
        textMediSnapPassword.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textMediSnapPasswordActionPerformed(evt);
            }
        });
        textMediSnapPassword.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyTyped(java.awt.event.KeyEvent evt) {
                textMediSnapPasswordKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        LoginMain.add(textMediSnapPassword, gridBagConstraints);
        buttonMediSnap.setFont(new java.awt.Font("Dialog", 0, 24));
        buttonMediSnap.setText("MediSnap starten");
        buttonMediSnap.setToolTipText("Dieser Knopf startet MediSnap.");
        buttonMediSnap.setEnabled(false);
        buttonMediSnap.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonMediSnapActionPerformed(evt);
            }
        });
        buttonMediSnap.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyTyped(java.awt.event.KeyEvent evt) {
                buttonMediSnapKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
        LoginMain.add(buttonMediSnap, gridBagConstraints);
        jLabelUser.setText("Benutzer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        LoginMain.add(jLabelUser, gridBagConstraints);
        jLabelUser.getAccessibleContext().setAccessibleName("Benutzer :");
        jLabelDBURL.setText("ServerURL (IP)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        LoginMain.add(jLabelDBURL, gridBagConstraints);
        jLabelDBURL.getAccessibleContext().setAccessibleName("ServerURL (IP): ");
        jTextFieldUser.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldUserKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        LoginMain.add(jTextFieldUser, gridBagConstraints);
        jTextFieldDBURL.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldDBURLKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        LoginMain.add(jTextFieldDBURL, gridBagConstraints);
        jLabelErrorMessage.setForeground(new java.awt.Color(255, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        LoginMain.add(jLabelErrorMessage, gridBagConstraints);
        jPanel4.add(LoginMain);
        jPanel1.add(jPanel4);
        add(jPanel1, java.awt.BorderLayout.CENTER);
    }

    private void jTextFieldDBURLKeyTyped(java.awt.event.KeyEvent evt) {
        if ((jTextFieldUser.getText().length() > 0) && (textMediSnapPassword.getPassword().length > 0)) buttonMediSnap.setEnabled(true);
    }

    private void jTextFieldUserKeyTyped(java.awt.event.KeyEvent evt) {
        if ((jTextFieldDBURL.getText().length() > 0) && (textMediSnapPassword.getPassword().length > 0)) buttonMediSnap.setEnabled(true);
    }

    private void buttonMediSnapKeyTyped(java.awt.event.KeyEvent evt) {
        buttonMediSnapActionPerformed(null);
    }

    private void buttonBeendenKeyTyped(java.awt.event.KeyEvent evt) {
        buttonBeendenActionPerformed(null);
    }

    private void textMediSnapPasswordKeyTyped(java.awt.event.KeyEvent evt) {
        if (((jTextFieldDBURL.getText()).length() > 0) && ((jTextFieldUser.getText()).length() > 0)) buttonMediSnap.setEnabled(true);
    }

    private void textMediSnapPasswordActionPerformed(java.awt.event.ActionEvent evt) {
        buttonMediSnapActionPerformed(null);
    }

    private void buttonBeendenActionPerformed(java.awt.event.ActionEvent evt) {
        m.exitForm(null);
    }

    /**
     *Sicherheit damit nicht so leicht Password probiert werden kann
     */
    public void loginError() {
        counter++;
        if (counter > 3) {
            Cursor cur = this.getCursor();
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            try {
                java.lang.Thread.currentThread().sleep(counter * 2000);
            } catch (java.lang.InterruptedException e) {
            } finally {
                this.setCursor(cur);
            }
        }
    }

    private void buttonMediSnapActionPerformed(java.awt.event.ActionEvent evt) {
        if (textMediSnapPassword.getPassword().length == 0) {
            Object[] options = { "Ok" };
            int answer = JOptionPane.showOptionDialog(m, "Sie haben kein Passwort angegeben.", "Passwort fehler", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            return;
        }
        String server = jTextFieldDBURL.getText();
        String user = jTextFieldUser.getText();
        String database = MediSnapOptions.getDatabaseName();
        m.dbConnected = false;
        int i = (m.establishDBConnection(user, new String(textMediSnapPassword.getPassword()), server, database));
        if (i == 0) {
            MediSnapOptions.setDatabaseURL(server);
            MediSnapOptions.setDatabaseUser(user);
            MediSnapOptions.storePermanent();
            m.isLoggedIn = true;
            m.isWellKonfiguratet = true;
            m.showForm(MediSnapFormular.PatientSuchen);
        } else if (i == -1) loginError(); else {
            m.isWellKonfiguratet = false;
            m.showForm(MediSnapFormular.KonfigFehler);
        }
    }

    private javax.swing.JPanel LoginMain;

    private javax.swing.JButton buttonBeenden;

    private javax.swing.JButton buttonMediSnap;

    private javax.swing.JLabel jLabelDBURL;

    private javax.swing.JLabel jLabelErrorMessage;

    private javax.swing.JLabel jLabelUser;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JPanel jPanel6;

    private javax.swing.JPanel jPanel7;

    private javax.swing.JTextField jTextFieldDBURL;

    private javax.swing.JTextField jTextFieldUser;

    private javax.swing.JLabel jlVersion;

    private javax.swing.JLabel labelPassword;

    private javax.swing.JLabel lableDatum;

    private javax.swing.JLabel lableUhrzeit;

    private javax.swing.JPasswordField textMediSnapPassword;
}
