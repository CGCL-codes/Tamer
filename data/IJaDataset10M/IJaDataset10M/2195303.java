package org.openconcerto.erp.core.customerrelationship.customer.element;

import org.openconcerto.erp.core.common.element.ComptaSQLConfElement;
import org.openconcerto.sql.element.SQLComponent;
import org.openconcerto.sql.element.UISQLComponent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextField;

public class TypeLettreRelanceSQLElement extends ComptaSQLConfElement {

    public TypeLettreRelanceSQLElement() {
        super("TYPE_LETTRE_RELANCE", "un type de lettre de relance", "types de lettre de relance");
    }

    protected List<String> getListFields() {
        final List<String> l = new ArrayList<String>();
        l.add("NOM");
        return l;
    }

    protected List<String> getComboFields() {
        final List<String> l = new ArrayList<String>();
        l.add("NOM");
        return l;
    }

    public SQLComponent createComponent() {
        return new UISQLComponent(this) {

            public void addViews() {
                this.addRequiredSQLObject(new JTextField(), "NOM", "left");
                this.addRequiredSQLObject(new JTextField(), "MODELE", "right");
            }
        };
    }

    @Override
    protected String createCode() {
        return super.createCodeFromPackage() + ".chaseletter.type";
    }
}
