package medisnap.gui;

import medisnap.MediSnap;
import medisnap.dblayer.*;
import java.util.*;
import javax.swing.event.*;
import java.awt.event.*;
import javax.imageio.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.util.logging.*;

/**
 *
 * @author  Elle
 */
public class LokalisationNeuZuordnen extends panelBaseClass implements medisnap.gui.isvisible {

    public static final long serialVersionUID = 1;

    public MediSnap m;

    private Vector<valuePatient> data;

    public void freeMemory() {
        if (data != null) {
            data.removeAllElements();
            data = null;
        }
    }

    public void isNowVisible() {
        m.ked = new MyDispatcher();
        m.manager.addKeyEventDispatcher(m.ked);
        textSucheVeraendert();
    }

    /** Creates new form LokalisationNeuZuordnen */
    public LokalisationNeuZuordnen(MediSnap _m) {
        m = _m;
        initComponents();
        listPatienten.setCellRenderer(new ListePatientenCellRenderer());
        this.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                textSuche.requestFocusInWindow();
            }

            public void focusLost(FocusEvent e) {
            }
        });
    }

    private class MyDispatcher implements KeyEventDispatcher {

        MyDispatcher() {
        }

        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.isControlDown() && e.getID() == KeyEvent.KEY_PRESSED) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_A:
                        jbAbbrechenActionPerformed(null);
                        return true;
                }
            }
            return false;
        }
    }

    private void initComponents() {
        panelMain = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        textSuche = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        listPatienten = new javax.swing.JList();
        jPanel4 = new javax.swing.JPanel();
        panelButtons = new javax.swing.JPanel();
        buttonsRechts = new javax.swing.JPanel();
        jbLokalisationZuordnen = new javax.swing.JButton();
        jbAbbrechen = new javax.swing.JButton();
        setLayout(new java.awt.BorderLayout());
        setOpaque(false);
        panelMain.setLayout(new java.awt.BorderLayout());
        panelMain.setOpaque(false);
        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanel1.setOpaque(false);
        jPanel2.setLayout(new java.awt.GridLayout(1, 0, 0, 50));
        jPanel2.setOpaque(false);
        jPanel3.setOpaque(false);
        jPanel2.add(jPanel3);
        jPanel5.setLayout(new java.awt.BorderLayout(0, 5));
        jPanel5.setOpaque(false);
        jPanel6.setLayout(new java.awt.BorderLayout());
        jPanel6.setOpaque(false);
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 14));
        jLabel1.setLabelFor(textSuche);
        jLabel1.setText("Name: ");
        jPanel6.add(jLabel1, java.awt.BorderLayout.WEST);
        textSuche.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                textSucheVeraendert();
            }

            public void removeUpdate(DocumentEvent e) {
                textSucheVeraendert();
            }

            public void insertUpdate(DocumentEvent e) {
                textSucheVeraendert();
            }
        });
        textSuche.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textSucheActionPerformed(evt);
            }
        });
        textSuche.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                textSucheKeyPressed(evt);
            }
        });
        jPanel6.add(textSuche, java.awt.BorderLayout.CENTER);
        jPanel5.add(jPanel6, java.awt.BorderLayout.NORTH);
        jScrollPane1.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3));
        listPatienten.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listPatienten.setToolTipText("Wie werden alle Patienten aufgelistet, die den bisherigen Suchkriterien entsprechen.");
        listPatienten.setValueIsAdjusting(true);
        listPatienten.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyReleased(java.awt.event.KeyEvent evt) {
                listPatientenKeyReleased(evt);
            }
        });
        listPatienten.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listPatientenMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(listPatienten);
        jPanel5.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        jPanel2.add(jPanel5);
        jPanel4.setOpaque(false);
        jPanel2.add(jPanel4);
        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);
        panelMain.add(jPanel1, java.awt.BorderLayout.CENTER);
        add(panelMain, java.awt.BorderLayout.CENTER);
        panelButtons.setLayout(new java.awt.BorderLayout());
        panelButtons.setOpaque(false);
        buttonsRechts.setOpaque(false);
        jbLokalisationZuordnen.setText("Lokalisation dem markierten Patienten zuordnen");
        jbLokalisationZuordnen.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbLokalisationZuordnenActionPerformed(evt);
            }
        });
        buttonsRechts.add(jbLokalisationZuordnen);
        jbAbbrechen.setText("<html><u>A</u>bbrechen</html>");
        jbAbbrechen.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAbbrechenActionPerformed(evt);
            }
        });
        buttonsRechts.add(jbAbbrechen);
        panelButtons.add(buttonsRechts, java.awt.BorderLayout.EAST);
        add(panelButtons, java.awt.BorderLayout.SOUTH);
    }

    private void jbLokalisationZuordnenActionPerformed(java.awt.event.ActionEvent evt) {
        int selection = listPatienten.getSelectedIndex();
        if (selection != -1) {
            valuePatient vp = data.get(selection);
            if (vp.getId() != m.activeValuePatient.getId()) {
                Object[] options = { "Ja", "Nein" };
                int answer = JOptionPane.showOptionDialog(this, "Soll die Lokalisation '" + m.activeValueLokalisation.getBezeichnung() + "' dem Patienten " + vp.getName() + ", " + vp.getVorname() + " zugeordnet werden?", "Lokalisation anderem Patienten zuordnen", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (answer == JOptionPane.YES_OPTION) {
                    try {
                        DBLayer.beginTransaction();
                        movePictures(m.activeValueLokalisation, m.activeValuePatient, vp);
                        DBLayer.editLokalisationChangePatient(m.activeValueLokalisation, vp);
                        DBLayer.commit();
                    } catch (java.sql.SQLException e) {
                        DBLayer.rollback(e);
                        MediSnap.log.severe("Verschieben von Bildern fehlgeschlagen! SQLEx:" + e);
                        javax.swing.JOptionPane.showMessageDialog(null, "Das Bild/Die Bilder konnten nicht verschoben werden!\n" + "Zugriff auf die Medisnap DatenBank gescheitert\n", "Datenbankzugriff-Fehler", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                    m.createTreeNodes(true);
                    m.showForm(MediSnapFormular.Bereichszuordnung);
                }
            }
        }
    }

    private void movePictures(valueLokalisation vl, valuePatient from, valuePatient to) {
        Vector<valueBild> vec_vb = DBLayer.getBilderZuLokalisation(vl.getId());
        ListIterator<valueBild> vb_it = vec_vb.listIterator();
        valueBild vb;
        while (vb_it.hasNext()) {
            vb = vb_it.next();
            File org_path = new File(to.getCompletePicturePath(vb));
            org_path.mkdirs();
            File org_to = new File(to.getPicturePath(vb));
            File thumb_to = new File(to.getThumbnailPath(vb));
            File list_to = new File(to.getListPath(vb));
            new File(from.getPicturePath(vb)).renameTo(org_to);
            new File(from.getThumbnailPath(vb)).renameTo(thumb_to);
            new File(from.getListPath(vb)).renameTo(list_to);
        }
        Vector<valueLokalisation> vec_sub_vl = DBLayer.searchSubLokalisationOf(vl.getId());
        ListIterator<valueLokalisation> sub_vl_it = vec_sub_vl.listIterator();
        valueLokalisation sub_vl;
        while (sub_vl_it.hasNext()) {
            sub_vl = sub_vl_it.next();
            movePictures(sub_vl, from, to);
        }
    }

    private void textSucheActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void textSucheKeyPressed(java.awt.event.KeyEvent evt) {
        int selected = listPatienten.getSelectedIndex();
        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            if (data.size() > selected) {
                listPatienten.setSelectedIndex(selected + 1);
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            if (selected > 0) {
                listPatienten.setSelectedIndex(selected - 1);
            }
        }
    }

    private void listPatientenKeyReleased(java.awt.event.KeyEvent evt) {
    }

    private void listPatientenMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() >= 2) {
        }
    }

    private void jbAbbrechenActionPerformed(java.awt.event.ActionEvent evt) {
        m.showForm(MediSnapFormular.LokalisationBearbeiten);
    }

    private void textSucheVeraendert() {
        if (textSuche.getText().length() > 0) {
            data = DBLayer.searchPatientByName(textSuche.getText());
        } else {
            data = DBLayer.getFirstXXPatients(50);
        }
        listPatienten.setListData(data);
        if (data.size() > 0) {
            listPatienten.setSelectedIndex(0);
        }
    }

    private javax.swing.JPanel buttonsRechts;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JPanel jPanel6;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JButton jbAbbrechen;

    private javax.swing.JButton jbLokalisationZuordnen;

    private javax.swing.JList listPatienten;

    private javax.swing.JPanel panelButtons;

    private javax.swing.JPanel panelMain;

    private javax.swing.JTextField textSuche;
}
