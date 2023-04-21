package org.openconcerto.erp.core.humanresources.payroll.element;

import org.openconcerto.erp.core.common.element.ComptaSQLConfElement;
import org.openconcerto.erp.generationEcritures.GenerationMvtAcompte;
import org.openconcerto.sql.element.BaseSQLComponent;
import org.openconcerto.sql.element.SQLComponent;
import org.openconcerto.sql.model.SQLRow;
import org.openconcerto.sql.model.SQLRowValues;
import org.openconcerto.sql.model.SQLTable;
import org.openconcerto.sql.sqlobject.ElementComboBox;
import org.openconcerto.ui.DefaultGridBagConstraints;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class AcompteSQLElement extends ComptaSQLConfElement {

    public AcompteSQLElement() {
        super("ACOMPTE", "un acompte", "acompte");
    }

    protected List<String> getListFields() {
        final List<String> list = new ArrayList<String>(2);
        list.add("ID_SALARIE");
        list.add("MONTANT");
        return list;
    }

    protected List<String> getComboFields() {
        final List<String> list = new ArrayList<String>(2);
        list.add("ID_SALARIE");
        list.add("MONTANT");
        return list;
    }

    public SQLComponent createComponent() {
        return new BaseSQLComponent(this) {

            ElementComboBox comboSelSal;

            JTextField textMontant;

            public void addViews() {
                this.setLayout(new GridBagLayout());
                final GridBagConstraints c = new DefaultGridBagConstraints();
                this.comboSelSal = new ElementComboBox();
                this.add(new JLabel("Salarié", SwingConstants.RIGHT), c);
                c.gridx++;
                c.weightx = 1;
                this.add(this.comboSelSal, c);
                c.weightx = 0;
                this.textMontant = new JTextField(10);
                JLabel labelMontant = new JLabel("Montant");
                c.gridy++;
                c.gridx = 0;
                c.weighty = 1;
                c.anchor = GridBagConstraints.NORTHEAST;
                this.add(labelMontant, c);
                c.gridx++;
                c.weightx = 1;
                c.anchor = GridBagConstraints.NORTHWEST;
                c.fill = GridBagConstraints.NONE;
                c.weightx = 0;
                this.add(this.textMontant, c);
                this.addRequiredSQLObject(this.comboSelSal, "ID_SALARIE");
                this.addRequiredSQLObject(this.textMontant, "MONTANT");
            }

            public int insert(SQLRow order) {
                int id = super.insert(order);
                SQLRow rowTmp = getTable().getRow(id);
                new GenerationMvtAcompte(id);
                SQLTable tableSal = getTable().getBase().getTable("SALARIE");
                SQLTable tableFichePaye = getTable().getBase().getTable("FICHE_PAYE");
                SQLRow rowSal = tableSal.getRow(rowTmp.getInt("ID_SALARIE"));
                SQLRow rowFiche = tableFichePaye.getRow(rowSal.getInt("ID_FICHE_PAYE"));
                SQLRowValues rowVals = new SQLRowValues(tableFichePaye);
                float nouveauMontantAcompte = rowFiche.getFloat("ACOMPTE") + rowTmp.getFloat("MONTANT");
                rowVals.put("ACOMPTE", new Float(nouveauMontantAcompte));
                try {
                    rowVals.update(rowFiche.getID());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return id;
            }
        };
    }

    @Override
    protected String createCode() {
        return createCodeFromPackage() + ".advance";
    }
}
