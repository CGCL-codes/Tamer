package de.shandschuh.jaolt.gui.maintabbedpane.auctionlist;

import javax.swing.JToolTip;
import de.shandschuh.jaolt.core.Tagable;
import de.shandschuh.jaolt.core.auction.Picture;
import de.shandschuh.jaolt.gui.core.RowHighlightedJTable;
import de.shandschuh.jaolt.gui.maintabbedpane.ListJPanel;
import de.shandschuh.jaolt.gui.maintabbedpane.PointableTable;

public class ListJTable<T extends Tagable> extends RowHighlightedJTable implements PointableTable {

    /** Default serial version uid */
    private static final long serialVersionUID = 1L;

    private int currentRow = -1;

    private int currentColumn = -1;

    public ListJTable(ListJPanel<T> listJPanel) {
        super(new ListJTableModel(listJPanel));
    }

    public JToolTip createToolTip() {
        try {
            return new ImageIconJToolTip((Picture[]) getValueAt(currentRow, currentColumn), 150);
        } catch (Exception e) {
            return super.createToolTip();
        }
    }

    public void setCurrentColumn(int currentColumn) {
        this.currentColumn = currentColumn;
    }

    public void setCurrentRow(int currentRow) {
        if (currentColumn >= 0 && currentRow >= 0) {
            try {
                setToolTipText(((Picture[]) getValueAt(currentRow, currentColumn)).length > 0 ? "N" + currentRow : null);
            } catch (Exception exception) {
                setToolTipText(null);
            }
        }
        this.currentRow = currentRow;
    }

    public synchronized void setValueAt(Object value, int row, int column) {
        super.setValueAt(value, row, column);
        setToolTipText(getToolTipText());
    }

    @SuppressWarnings("unchecked")
    public T getValueAt(int row) {
        return (T) getValueAt(row, -1);
    }

    public ListJTableModel getModel() {
        return (ListJTableModel) super.getModel();
    }
}
