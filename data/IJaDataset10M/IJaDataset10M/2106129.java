package org.openconcerto.erp.core.finance.accounting.ui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;

public class PlanComptableCellRenderer extends CompteCellRenderer {

    private final transient int colNumeroCompte;

    public PlanComptableCellRenderer(final int colNumeroCompte) {
        super();
        this.colNumeroCompte = colNumeroCompte;
    }

    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (!isSelected) {
            final String numeroCompte = table.getValueAt(row, this.colNumeroCompte).toString().trim();
            final int compteLength = numeroCompte.length();
            if (compteLength == 1) {
                this.setBackground(couleurCompteClasse);
            } else {
                if (row < table.getRowCount() - 1) {
                    final String numeroCompteSuiv = table.getValueAt(row + 1, this.colNumeroCompte).toString().trim();
                    if ((compteLength < numeroCompteSuiv.length()) && (numeroCompte.equalsIgnoreCase(numeroCompteSuiv.substring(0, compteLength)))) {
                        if (compteLength == 2) {
                            this.setBackground(couleurCompte2);
                        } else if (compteLength == 3) {
                            this.setBackground(couleurCompte3);
                        } else {
                            this.setBackground(couleurCompteRacine);
                        }
                    }
                } else {
                    this.setBackground(Color.WHITE);
                }
            }
            this.setForeground(Color.BLACK);
        }
        return this;
    }
}
