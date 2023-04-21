package org.openconcerto.erp.core.finance.accounting.model;

import org.openconcerto.erp.config.ComptaPropsConfiguration;
import org.openconcerto.sql.Configuration;
import org.openconcerto.sql.model.SQLBase;
import org.openconcerto.sql.model.SQLRow;
import org.openconcerto.sql.model.SQLSelect;
import org.openconcerto.sql.model.SQLTable;
import org.openconcerto.sql.model.Where;
import java.util.List;
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;
import org.apache.commons.dbutils.handlers.ArrayListHandler;

public class LettrageModel extends AbstractTableModel {

    private String[] titresCol;

    private String[] titresRow;

    private long debitLettre, creditLettre, debitNonLettre, creditNonLettre, creditSelection, debitSelection;

    private static final SQLBase base = ((ComptaPropsConfiguration) Configuration.getInstance()).getSQLBaseSociete();

    private static final SQLTable tableEcr = base.getTable("ECRITURE");

    int idCpt;

    public LettrageModel(int idCpt) {
        this.creditNonLettre = 0;
        this.creditLettre = 0;
        this.creditSelection = 0;
        this.debitNonLettre = 0;
        this.debitLettre = 0;
        this.debitSelection = 0;
        this.idCpt = idCpt;
        this.titresCol = new String[6];
        this.titresCol[0] = "Totaux";
        this.titresCol[1] = "Lettrée";
        this.titresCol[2] = "Non Lettrée";
        this.titresCol[3] = "Total";
        this.titresCol[4] = "Sélection";
        this.titresCol[5] = "Lettrée + sélection";
        this.titresRow = new String[3];
        this.titresRow[0] = "Débit";
        this.titresRow[1] = "Crédit";
        this.titresRow[2] = "Solde";
        updateTotauxCompte();
    }

    public void setIdCompte(int id) {
        this.idCpt = id;
        updateTotauxCompte();
        updateSelection(null);
    }

    public void updateSelection(int[] rowIndex) {
        System.err.println("Update Selection");
        this.creditSelection = 0;
        this.debitSelection = 0;
        if (rowIndex != null) {
            for (int i = 0; i < rowIndex.length; i++) {
                SQLRow row = tableEcr.getRow(rowIndex[i]);
                if (row != null) {
                    this.debitSelection += ((Long) row.getObject("DEBIT")).longValue();
                    this.creditSelection += ((Long) row.getObject("CREDIT")).longValue();
                }
            }
        }
        this.fireTableDataChanged();
    }

    public void updateTotauxCompte() {
        new SwingWorker<String, Object>() {

            @Override
            protected String doInBackground() throws Exception {
                SQLSelect sel = new SQLSelect(base);
                sel.addSelect(tableEcr.getField("CREDIT"), "SUM");
                sel.addSelect(tableEcr.getField("DEBIT"), "SUM");
                Where w = new Where(tableEcr.getField("ID_COMPTE_PCE"), "=", LettrageModel.this.idCpt);
                sel.setWhere(w.and(new Where(tableEcr.getField("LETTRAGE"), "!=", "")));
                String reqLettree = sel.toString();
                Object obLettree = base.getDataSource().execute(reqLettree, new ArrayListHandler());
                List myListLettree = (List) obLettree;
                LettrageModel.this.creditLettre = 0;
                LettrageModel.this.debitLettre = 0;
                if (myListLettree.size() != 0) {
                    for (int i = 0; i < myListLettree.size(); i++) {
                        Object[] objTmp = (Object[]) myListLettree.get(i);
                        if (objTmp[0] != null) {
                            LettrageModel.this.creditLettre += ((Number) objTmp[0]).longValue();
                        }
                        if (objTmp[1] != null) {
                            LettrageModel.this.debitLettre += ((Number) objTmp[1]).longValue();
                        }
                    }
                }
                sel.setWhere(w.and(new Where(tableEcr.getField("LETTRAGE"), "=", "")));
                String reqNotLettree = sel.toString();
                Object obNotLettree = base.getDataSource().execute(reqNotLettree, new ArrayListHandler());
                List myListNotLettree = (List) obNotLettree;
                LettrageModel.this.creditNonLettre = 0;
                LettrageModel.this.debitNonLettre = 0;
                if (myListNotLettree.size() != 0) {
                    for (int i = 0; i < myListNotLettree.size(); i++) {
                        Object[] objTmp = (Object[]) myListNotLettree.get(i);
                        if (objTmp[0] != null) {
                            LettrageModel.this.creditNonLettre += ((Number) objTmp[0]).longValue();
                        }
                        if (objTmp[1] != null) {
                            LettrageModel.this.debitNonLettre += ((Number) objTmp[1]).longValue();
                        }
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                LettrageModel.this.fireTableDataChanged();
            }
        }.execute();
    }

    public String getColumnName(int column) {
        return this.titresCol[column];
    }

    public int getColumnCount() {
        return this.titresCol.length;
    }

    public int getRowCount() {
        return this.titresRow.length;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return String.class;
        } else {
            return Long.class;
        }
    }

    public long getSoldeSelection() {
        return this.creditSelection - this.debitSelection;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return this.titresRow[rowIndex];
        }
        if (columnIndex == 1) {
            if (rowIndex == 0) {
                return new Long(this.debitLettre);
            }
            if (rowIndex == 1) {
                return new Long(this.creditLettre);
            }
            if (rowIndex == 2) {
                return new Long(this.debitLettre - this.creditLettre);
            }
        }
        if (columnIndex == 2) {
            if (rowIndex == 0) {
                return new Long(this.debitNonLettre);
            }
            if (rowIndex == 1) {
                return new Long(this.creditNonLettre);
            }
            if (rowIndex == 2) {
                return new Long(this.debitNonLettre - this.creditNonLettre);
            }
        }
        if (columnIndex == 3) {
            if (rowIndex == 0) {
                return new Long(this.debitNonLettre + this.debitLettre);
            }
            if (rowIndex == 1) {
                return new Long(this.creditNonLettre + this.creditLettre);
            }
            if (rowIndex == 2) {
                return new Long((this.debitNonLettre - this.creditNonLettre) + (this.debitLettre - this.creditLettre));
            }
        }
        if (columnIndex == 4) {
            if (rowIndex == 0) {
                return new Long(this.debitSelection);
            }
            if (rowIndex == 1) {
                return new Long(this.creditSelection);
            }
            if (rowIndex == 2) {
                return new Long(this.debitSelection - this.creditSelection);
            }
        }
        if (columnIndex == 5) {
            if (rowIndex == 0) {
                return new Long(this.debitSelection + this.debitLettre);
            }
            if (rowIndex == 1) {
                return new Long(this.creditSelection + this.creditLettre);
            }
            if (rowIndex == 2) {
                return new Long(this.debitSelection - this.creditSelection + this.debitLettre - this.creditLettre);
            }
        }
        return null;
    }
}
