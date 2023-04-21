package net.sf.rmoffice.ui.models;

import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;
import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.meta.DevPack;

/**
 * 
 */
public class DevPackDialogTableModel extends AbstractTableModel {

    private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale");

    private static final int COL_NAME = 0;

    private static final int COL_TYPE = 1;

    private static final int COL_DURATION = 2;

    private static final int COL_COSTS = 3;

    private static final String[] COLUMN_NAMES = { "Ausbildungspaket", "Typ", "Dauer(Monate)", "AP-Kosten" };

    private final RMSheet sheet;

    public DevPackDialogTableModel(RMSheet sheet) {
        this.sheet = sheet;
    }

    /** {@inheritDoc} */
    @Override
    public Object getValueAt(int row, int column) {
        DevPack devPack = sheet.getAvailableDevPacks().get(row);
        switch(column) {
            case COL_NAME:
                return devPack.getName();
            case COL_TYPE:
                return RESOURCE.getString("DevPack.Type." + devPack.getType().toString());
            case COL_DURATION:
                return "" + devPack.getDurationMonth();
            case COL_COSTS:
                return "" + sheet.getDevPackCosts(devPack);
        }
        return "";
    }

    /** {@inheritDoc} */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    /** {@inheritDoc} */
    @Override
    public int getRowCount() {
        return sheet.getAvailableDevPacks().size();
    }

    /** {@inheritDoc} */
    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    /** {@inheritDoc} */
    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    /** {@inheritDoc} */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException();
    }

    /**
	 * Returns the {@link DevPack} at given index.
	 * 
	 * @param index the row index
	 * @return {@link DevPack}
	 */
    public DevPack getDevPack(int index) {
        return sheet.getAvailableDevPacks().get(index);
    }
}
