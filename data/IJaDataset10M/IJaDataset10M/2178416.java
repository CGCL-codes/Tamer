package userInterfaceLaag;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.*;
import java.text.*;
import java.util.*;
import Klassendiagram.Speler;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class WizardStep2 extends ImagePanel {

    private int playerNo = 1;

    private byte maxPlayers;

    private WizardController ctl;

    private JLabel lblTitle;

    private JTextField tfName;

    private JTextField tfBirthdate;

    private JTextField tfBalance;

    private JLabel lblBirthdate;

    private JLabel lblName;

    private JLabel lblBalance;

    public WizardStep2(String file, WizardController ctl) {
        super(file);
        this.ctl = ctl;
        initGUI();
    }

    public void setMaximumPlayers(byte max) {
        maxPlayers = max;
    }

    public boolean getPrev() {
        if (playerNo == 1) {
            return true;
        }
        playerNo--;
        lblTitle.setText(String.format("Enter data player %s", playerNo));
        Speler speler = ctl.geefSpeler(playerNo - 1);
        if (speler != null) {
            tfName.setText(speler.geefNaam());
            tfBirthdate.setText(speler.geefGeboorteDatum());
            tfBalance.setText(speler.geefSaldo() + "");
        }
        return false;
    }

    public boolean getNext() {
        Speler speler = ctl.geefSpeler(playerNo);
        if (speler != null) {
            tfName.setText(speler.geefNaam());
            tfBirthdate.setText(speler.geefGeboorteDatum());
            tfBalance.setText(speler.geefSaldo() + "");
        } else {
            ctl.volgende(playerNo < maxPlayers);
        }
        if (playerNo == maxPlayers) {
            return true;
        }
        playerNo++;
        lblTitle.setText(String.format("Enter data player %s", playerNo));
        return false;
    }

    private Date convertString2Date(String ds) {
        if (!ds.equals("")) {
            try {
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                return df.parse(ds);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void initGUI() {
        try {
            setLayout(null);
            setPreferredSize(new Dimension(300, 300));
            lblTitle = new JLabel(String.format("Enter data player %s", playerNo));
            lblTitle.setFont(new java.awt.Font("Arial", 1, 24));
            lblTitle.setForeground(new Color(0, 0, 255));
            lblTitle.setBounds(30, 38, 251, 40);
            this.add(lblTitle);
            lblName = new JLabel("Name");
            lblName.setFont(new Font("Arial", 0, 20));
            lblName.setForeground(new Color(0, 0, 255));
            lblName.setBounds(26, 105, 70, 21);
            add(lblName);
            tfName = new JTextField(20);
            tfName.setFont(new java.awt.Font("Arial", 0, 20));
            tfName.setForeground(new Color(128, 128, 255));
            tfName.setBounds(122, 107, 161, 25);
            tfName.addFocusListener(new FocusAdapter() {

                public void focusLost(FocusEvent evt) {
                    tfNameFocusLost(evt);
                }
            });
            add(tfName);
            lblBirthdate = new JLabel("Birthdate");
            lblBirthdate.setFont(new Font("Arial", 0, 20));
            lblBirthdate.setForeground(new Color(0, 0, 255));
            lblBirthdate.setBounds(25, 138, 91, 24);
            add(lblBirthdate);
            tfBirthdate = new JTextField(20);
            tfBirthdate.setFont(new java.awt.Font("Arial", 0, 20));
            tfBirthdate.setForeground(new Color(128, 128, 255));
            tfBirthdate.setBounds(122, 138, 113, 25);
            tfBirthdate.addFocusListener(new FocusAdapter() {

                public void focusLost(FocusEvent evt) {
                    tfBirthdateFocusLost(evt);
                }
            });
            add(tfBirthdate);
            lblBalance = new JLabel("Balance");
            lblBalance.setFont(new Font("Arial", 0, 20));
            lblBalance.setForeground(new Color(0, 0, 255));
            lblBalance.setBounds(25, 167, 91, 28);
            add(lblBalance);
            tfBalance = new JTextField(20);
            tfBalance.setFont(new java.awt.Font("Arial", 0, 20));
            tfBalance.setForeground(new Color(128, 128, 255));
            tfBalance.setBounds(122, 170, 59, 24);
            tfBalance.addFocusListener(new FocusAdapter() {

                public void focusLost(FocusEvent evt) {
                    tfBalanceFocusLost(evt);
                }
            });
            add(tfBalance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tfNameFocusLost(FocusEvent evt) {
        System.out.println("tfName.focusLost, event=" + evt);
        String naam = tfName.getText();
        if (!naam.equals("")) {
            ctl.zetNaam(naam);
        }
    }

    private void tfBirthdateFocusLost(FocusEvent evt) {
        System.out.println("tfBirthdate.focusLost, event=" + evt);
        String gebdat = tfBirthdate.getText();
        if (!gebdat.equals("")) {
            ctl.zetGeboorteDatum(convertString2Date(gebdat));
        }
    }

    private void tfBalanceFocusLost(FocusEvent evt) {
        System.out.println("tfBalance.focusLost, event=" + evt);
        String saldo = tfBalance.getText();
        if (!saldo.equals("")) {
            ctl.zetSaldo(Double.parseDouble(saldo));
        }
    }
}
