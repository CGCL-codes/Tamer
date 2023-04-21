package org.openconcerto.erp.core.common.ui;

import org.openconcerto.erp.config.ComptaPropsConfiguration;
import org.openconcerto.erp.model.PrixHT;
import org.openconcerto.erp.model.PrixTTC;
import org.openconcerto.sql.Configuration;
import org.openconcerto.sql.model.SQLBackgroundTableCache;
import org.openconcerto.sql.model.SQLRow;
import org.openconcerto.sql.sqlobject.ElementComboBox;
import org.openconcerto.utils.GestionDevise;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/***************************************************************************************************
 * SELECTION D'UN MONTANT TTC OU HT + VALEUR TVA
 * 
 * Pour initialiser le ComboSelection --> addRequiredSQLObject(montant.getChoixTaxe(), "ID_TAXE");
 * --> setChoixTaxe(String value);
 **************************************************************************************************/
public class MontantPanel extends JPanel {

    private JRadioButton checkHT;

    private JRadioButton checkTTC;

    private DeviseField textHT;

    private DeviseField textTTC;

    private DeviseField textTaxe;

    private ElementComboBox comboTaxe;

    private boolean ue = false;

    private boolean enabled = true;

    private JLabel labelUE = new JLabel("Calcul d'une TVA intracommunautaire");

    private final DocumentListener listenerTextHT = new DocumentListener() {

        public void changedUpdate(DocumentEvent e) {
            calculMontant();
        }

        public void removeUpdate(DocumentEvent e) {
            calculMontant();
        }

        public void insertUpdate(DocumentEvent e) {
            calculMontant();
        }
    };

    private final DocumentListener listenerTextTTC = new DocumentListener() {

        public void changedUpdate(DocumentEvent e) {
            calculMontant();
        }

        public void removeUpdate(DocumentEvent e) {
            calculMontant();
        }

        public void insertUpdate(DocumentEvent e) {
            calculMontant();
        }
    };

    public MontantPanel() {
        uiInit();
    }

    private void uiInit() {
        this.setOpaque(false);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 2, 1, 2);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        this.labelUE.setVisible(this.ue);
        this.checkHT = new JRadioButton("HT");
        this.checkHT.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setHT(true);
            }
        });
        c.weightx = 0;
        this.add(this.checkHT, c);
        this.textHT = new DeviseField();
        c.gridx++;
        c.weightx = 1;
        this.add(this.textHT, c);
        c.gridx++;
        c.weightx = 0;
        this.add(new JLabel("TVA"), c);
        c.insets = new Insets(2, 10, 1, 2);
        this.comboTaxe = new ElementComboBox(false, 8);
        this.comboTaxe.setButtonsVisible(false);
        c.gridx++;
        this.add(this.comboTaxe, c);
        this.comboTaxe.addValueListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                calculMontant();
            }
        });
        c.insets = new Insets(2, 2, 1, 2);
        this.textTaxe = new DeviseField();
        this.textTaxe.setEditable(false);
        this.textTaxe.setEnabled(false);
        c.gridx++;
        c.weightx = 0;
        this.add(this.textTaxe, c);
        this.checkTTC = new JRadioButton("TTC");
        this.checkTTC.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setHT(false);
            }
        });
        c.gridy++;
        c.gridx = 0;
        c.weightx = 0;
        this.add(this.checkTTC, c);
        this.textTTC = new DeviseField();
        c.gridx++;
        c.weightx = 1;
        this.add(this.textTTC, c);
        c.gridx++;
        c.gridwidth = GridBagConstraints.REMAINDER;
        this.labelUE.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(this.labelUE, c);
        ButtonGroup grp1 = new ButtonGroup();
        grp1.add(this.checkTTC);
        grp1.add(this.checkHT);
        this.checkHT.setSelected(true);
        setHT(true);
        this.textTTC.getDocument().addDocumentListener(this.listenerTextTTC);
        this.textHT.getDocument().addDocumentListener(this.listenerTextHT);
    }

    private void setHT(boolean b) {
        if (b) {
            this.textHT.setEditable(true);
            this.textHT.setEnabled(true);
            this.textTTC.setEditable(false);
            this.textTTC.setEnabled(false);
        } else {
            this.textHT.setEditable(false);
            this.textHT.setEnabled(false);
            this.textTTC.setEditable(true);
            this.textTTC.setEnabled(true);
        }
    }

    public void calculMontant() {
        float taux;
        PrixHT pHT;
        PrixTTC pTTC;
        System.out.println("Recalcul montant");
        if (this.enabled) {
            int idTaxe = this.comboTaxe.getSelectedId();
            System.out.println("ID_TAXE =  " + idTaxe);
            if (idTaxe > 1) {
                SQLRow ligneTaxe = SQLBackgroundTableCache.getInstance().getCacheForTable(((ComptaPropsConfiguration) Configuration.getInstance()).getSQLBaseSociete().getTable("TAXE")).getRowFromId(idTaxe);
                taux = (ligneTaxe.getFloat("TAUX")) / 100.0F;
                if (this.checkHT.isSelected()) {
                    if (this.textHT.getText().trim().length() > 0) {
                        pHT = new PrixHT(0);
                        if (!this.textHT.getText().trim().equals("-")) {
                            pHT = new PrixHT(GestionDevise.parseLongCurrency(this.textHT.getText()));
                        }
                        String tva = GestionDevise.currencyToString(pHT.calculLongTVA(taux));
                        String ttc;
                        if (this.ue) {
                            ttc = this.textHT.getText();
                        } else {
                            ttc = GestionDevise.currencyToString(pHT.calculLongTTC(taux));
                        }
                        updateText(tva, ttc, pHT.toString(), false);
                    } else updateText("", "", "", false);
                } else {
                    if (this.textTTC.getText().trim().length() > 0) {
                        pTTC = new PrixTTC(0);
                        if (!this.textTTC.getText().trim().equals("-")) {
                            pTTC = new PrixTTC(GestionDevise.parseLongCurrency(this.textTTC.getText()));
                        }
                        String tva;
                        if (this.ue) {
                            PrixHT prixHT = new PrixHT(pTTC.getLongValue());
                            tva = GestionDevise.currencyToString(prixHT.calculLongTVA(taux));
                        } else {
                            tva = GestionDevise.currencyToString(pTTC.calculLongTVA(taux));
                        }
                        String ht;
                        if (this.ue) {
                            ht = this.textTTC.getText();
                        } else {
                            ht = GestionDevise.currencyToString(pTTC.calculLongHT(taux));
                        }
                        updateText(tva, pTTC.toString(), ht, true);
                    } else updateText("", "", "", true);
                }
            }
        }
    }

    private void updateText(final String prixTVA, final String prixTTC, final String prixHT, final boolean HT) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                MontantPanel.this.textHT.getDocument().removeDocumentListener(MontantPanel.this.listenerTextHT);
                MontantPanel.this.textTTC.getDocument().removeDocumentListener(MontantPanel.this.listenerTextTTC);
                if (HT) {
                    MontantPanel.this.textHT.setText(prixHT);
                } else {
                    MontantPanel.this.textTTC.setText(prixTTC);
                }
                MontantPanel.this.textTaxe.setText(prixTVA);
                MontantPanel.this.textHT.getDocument().addDocumentListener(MontantPanel.this.listenerTextHT);
                MontantPanel.this.textTTC.getDocument().addDocumentListener(MontantPanel.this.listenerTextTTC);
            }
        });
    }

    public void setEnabled(boolean b) {
        this.enabled = b;
        this.checkHT.setEnabled(b);
        this.checkTTC.setEnabled(b);
        this.textHT.setEnabled(b);
        this.textTTC.setEnabled(b);
        this.textTaxe.setEnabled(b);
        this.comboTaxe.setEnabled(b);
    }

    public DeviseField getMontantTTC() {
        return this.textTTC;
    }

    public DeviseField getMontantHT() {
        return this.textHT;
    }

    public DeviseField getMontantTVA() {
        return this.textTaxe;
    }

    public ElementComboBox getChoixTaxe() {
        return this.comboTaxe;
    }

    public void setChoixTaxe(int value) {
        this.comboTaxe.setValue(value);
    }

    public void setUE(boolean b) {
        this.ue = b;
        this.labelUE.setVisible(b);
        calculMontant();
    }
}
