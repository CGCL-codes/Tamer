package dr.app.gui.table;

import dr.evolution.util.*;
import jam.table.*;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.Iterator;
import java.util.TreeSet;

public class TaxonListTable extends JTable implements MutableTaxonListListener {

    /**
	 *
	 */
    private static final long serialVersionUID = -5706027520036473157L;

    static final String TAXON_NAME = "Taxon";

    TaxaTableModel taxaTableModel = null;

    TaxonList taxonList = null;

    public TaxonListTable(TaxonList taxonList) {
        this.taxonList = taxonList;
        if (taxonList instanceof MutableTaxonList) {
            ((MutableTaxonList) taxonList).addMutableTaxonListListener(this);
        }
        String[] names = getColumnNames();
        taxaTableModel = new TaxaTableModel(names, taxonList);
        TableSorter sorter = new TableSorter(taxaTableModel);
        setModel(sorter);
        sorter.addTableModelListener(this);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setColumnSelectionAllowed(true);
        getColumn(TAXON_NAME).setPreferredWidth(120);
        for (int i = 0; i < getColumnCount(); i++) {
            getColumnModel().getColumn(i).setCellRenderer(new TableRenderer(true, SwingConstants.LEFT, new Insets(0, 4, 0, 0)));
        }
        setDefaultEditor(dr.evolution.util.Date.class, new DateCellEditor());
    }

    public void updateColumns() {
        taxaTableModel.setColumnNames(getColumnNames());
    }

    private String[] getColumnNames() {
        TreeSet attributes = new TreeSet();
        for (int i = 0; i < taxonList.getTaxonCount(); i++) {
            Taxon taxon = taxonList.getTaxon(i);
            Iterator iter = taxon.getAttributeNames();
            while (iter.hasNext()) {
                Object attribute = iter.next();
                attributes.add(attribute);
            }
        }
        String[] names = new String[attributes.size() + 1];
        names[0] = "Taxon";
        Iterator iter = attributes.iterator();
        int i = 1;
        while (iter.hasNext()) {
            names[i] = iter.next().toString();
            i++;
        }
        return names;
    }

    public void taxonAdded(TaxonList taxonList, Taxon taxon) {
        taxaTableModel.fireTableDataChanged();
    }

    public void taxonRemoved(TaxonList taxonList, Taxon taxon) {
        taxaTableModel.fireTableDataChanged();
    }

    public void taxaChanged(TaxonList taxonList) {
        updateColumns();
    }

    class TaxaTableModel extends AbstractTableModel {

        /**
		 *
		 */
        private static final long serialVersionUID = 367162013042095775L;

        String[] names = null;

        TaxonList taxonList;

        public TaxaTableModel(String[] names, TaxonList taxonList) {
            this.names = names;
            this.taxonList = taxonList;
        }

        public void setColumnNames(String[] names) {
            this.names = names;
            fireTableStructureChanged();
        }

        public int getColumnCount() {
            return names.length;
        }

        public int getRowCount() {
            return taxonList.getTaxonCount();
        }

        public Object getValueAt(int row, int column) {
            if (column == 0) {
                return taxonList.getTaxon(row).getId();
            } else {
                return taxonList.getTaxonAttribute(row, names[column]);
            }
        }

        public String getColumnName(int column) {
            return names[column];
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int column) {
            if (taxonList instanceof MutableTaxonList) {
                if (column == 0) return true;
                Object item = taxonList.getTaxonAttribute(row, names[column]);
                if (item instanceof String) return true;
                if (item instanceof dr.evolution.util.Date) return true;
            }
            return false;
        }

        public void setValueAt(Object aValue, int row, int column) {
            if (taxonList instanceof MutableTaxonList) {
                if (column == 0) {
                    ((MutableTaxonList) taxonList).setTaxonId(row, (String) aValue);
                } else {
                    ((MutableTaxonList) taxonList).setTaxonAttribute(row, names[column], aValue);
                }
            }
        }
    }
}
