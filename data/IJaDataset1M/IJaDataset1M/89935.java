package org.openconcerto.erp.core.finance.accounting.ui;

import org.openconcerto.erp.config.ComptaPropsConfiguration;
import org.openconcerto.erp.core.finance.accounting.element.ComptePCESQLElement;
import org.openconcerto.erp.model.ISQLCompteSelector;
import org.openconcerto.sql.Configuration;
import org.openconcerto.sql.model.SQLBase;
import org.openconcerto.sql.model.SQLRow;
import org.openconcerto.sql.model.SQLRowValues;
import org.openconcerto.sql.model.SQLTable;
import org.openconcerto.ui.DefaultGridBagConstraints;
import org.openconcerto.ui.TitledSeparator;
import org.openconcerto.ui.preferences.DefaultPreferencePanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ComptePayePreferencePanel extends DefaultPreferencePanel {

    private ISQLCompteSelector selCompteAcompte, selCompteAcompteReglement, selCompteRemunPers;

    private static final SQLBase base = ((ComptaPropsConfiguration) Configuration.getInstance()).getSQLBaseSociete();

    private static final SQLTable tablePrefCompte = base.getTable("PREFS_COMPTE");

    private SQLRowValues rowPrefCompteVals = new SQLRowValues(tablePrefCompte);

    public ComptePayePreferencePanel() {
        super();
        final SQLRow rowPrefCompte = tablePrefCompte.getRow(2);
        this.rowPrefCompteVals.loadAbsolutelyAll(rowPrefCompte);
        final Insets separatorInsets = new Insets(10, 2, 1, 2);
        this.setLayout(new GridBagLayout());
        final GridBagConstraints c = new DefaultGridBagConstraints();
        final Insets normalInsets = c.insets;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 0;
        TitledSeparator sep = new TitledSeparator("Acompte");
        c.gridwidth = GridBagConstraints.REMAINDER;
        this.add(sep, c);
        c.gridwidth = 1;
        c.gridy++;
        c.weightx = 0;
        this.add(new JLabel("Compte Acomptes"), c);
        c.weightx = 1;
        c.gridx++;
        this.selCompteAcompte = new ISQLCompteSelector();
        this.selCompteAcompte.init();
        this.add(this.selCompteAcompte, c);
        c.gridy++;
        c.weightx = 0;
        c.gridx = 0;
        this.add(new JLabel("Compte règlement acompte"), c);
        c.weightx = 1;
        c.gridx++;
        this.selCompteAcompteReglement = new ISQLCompteSelector();
        this.selCompteAcompteReglement.init();
        this.add(this.selCompteAcompteReglement, c);
        c.gridy++;
        c.gridx = 0;
        TitledSeparator sepVenteC = new TitledSeparator("Paye");
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = separatorInsets;
        this.add(sepVenteC, c);
        c.insets = normalInsets;
        c.gridwidth = 1;
        c.gridy++;
        c.weightx = 0;
        this.add(new JLabel("Compte rémunérations du personnel"), c);
        c.weightx = 1;
        c.gridx++;
        this.selCompteRemunPers = new ISQLCompteSelector();
        this.selCompteRemunPers.init();
        this.add(this.selCompteRemunPers, c);
        c.weighty = 1;
        c.gridy++;
        this.add(new JPanel(), c);
        setValues();
    }

    public void storeValues() {
        this.rowPrefCompteVals.put("ID_COMPTE_PCE_ACOMPTE", this.selCompteAcompte.getValue());
        this.rowPrefCompteVals.put("ID_COMPTE_PCE_ACOMPTE_REGL", this.selCompteAcompteReglement.getValue());
        this.rowPrefCompteVals.put("ID_COMPTE_PCE_PAYE", this.selCompteRemunPers.getValue());
        try {
            this.rowPrefCompteVals.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void restoreToDefaults() {
        try {
            String compte;
            compte = ComptePCESQLElement.getComptePceDefault("PayeAcompte");
            int value = ComptePCESQLElement.getId(compte);
            this.selCompteAcompte.setValue(value);
            compte = ComptePCESQLElement.getComptePceDefault("PayeReglementAcompte");
            value = ComptePCESQLElement.getId(compte);
            this.selCompteAcompteReglement.setValue(value);
            compte = ComptePCESQLElement.getComptePceDefault("PayeRemunerationPersonnel");
            value = ComptePCESQLElement.getId(compte);
            this.selCompteRemunPers.setValue(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTitleName() {
        return "Paye";
    }

    private void setValues() {
        try {
            int value = this.rowPrefCompteVals.getInt("ID_COMPTE_PCE_ACOMPTE");
            if (value <= 1) {
                String compte = ComptePCESQLElement.getComptePceDefault("PayeAcompte");
                value = ComptePCESQLElement.getId(compte);
            }
            this.selCompteAcompte.setValue(value);
            value = this.rowPrefCompteVals.getInt("ID_COMPTE_PCE_ACOMPTE_REGL");
            if (value <= 1) {
                String compte = ComptePCESQLElement.getComptePceDefault("PayeReglementAcompte");
                value = ComptePCESQLElement.getId(compte);
            }
            this.selCompteAcompteReglement.setValue(value);
            value = this.rowPrefCompteVals.getInt("ID_COMPTE_PCE_PAYE");
            if (value <= 1) {
                String compte = ComptePCESQLElement.getComptePceDefault("PayeRemunerationPersonnel");
                value = ComptePCESQLElement.getId(compte);
            }
            this.selCompteRemunPers.setValue(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
