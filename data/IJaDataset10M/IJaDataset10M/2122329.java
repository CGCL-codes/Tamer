package org.openconcerto.erp.core.finance.tax.element;

import org.openconcerto.erp.core.common.ui.DeviseField;
import org.openconcerto.sql.element.BaseSQLComponent;
import org.openconcerto.sql.element.ConfSQLElement;
import org.openconcerto.sql.element.SQLComponent;
import org.openconcerto.ui.DefaultGridBagConstraints;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class EcoTaxeSQLElement extends ConfSQLElement {

    public EcoTaxeSQLElement() {
        super("ECOTAXE", "une écotaxe", "écotaxes");
    }

    protected List<String> getListFields() {
        final List<String> l = new ArrayList<String>();
        l.add("NOM");
        l.add("MONTANT_HT");
        return l;
    }

    protected List<String> getComboFields() {
        final List<String> l = new ArrayList<String>();
        l.add("NOM");
        l.add("MONTANT_HT");
        return l;
    }

    public SQLComponent createComponent() {
        return new BaseSQLComponent(this) {

            public void addViews() {
                this.setLayout(new GridBagLayout());
                final GridBagConstraints c = new DefaultGridBagConstraints();
                c.gridx = GridBagConstraints.RELATIVE;
                this.add(new JLabel(getLabelFor("NOM")), c);
                c.gridx++;
                JTextField nom = new JTextField();
                c.weightx = 1;
                this.add(nom, c);
                c.weightx = 0;
                c.gridx++;
                DeviseField montant = new DeviseField();
                this.add(new JLabel(getLabelFor("MONTANT_HT")), c);
                c.gridx++;
                this.add(montant, c);
                this.addView(montant, "MONTANT_HT");
                this.addView(nom, "NOM");
            }
        };
    }
}
