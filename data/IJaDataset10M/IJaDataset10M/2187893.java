package com.iver.cit.gvsig;

import java.sql.Types;
import java.util.BitSet;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.layers.FBitSet;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.project.documents.table.gui.Table;

/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class TableRowsOperations extends Extension {

    /**
     * DOCUMENT ME!
     */
    public void initialize() {
        registerIcons();
    }

    private void registerIcons() {
        PluginServices.getIconTheme().registerDefault("table-selection-up", this.getClass().getClassLoader().getResource("images/selectionUp.png"));
        PluginServices.getIconTheme().registerDefault("table-invert", this.getClass().getClassLoader().getResource("images/invertSelection.png"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param actionCommand DOCUMENT ME!
     */
    public void execute(String actionCommand) {
        IWindow v = PluginServices.getMDIManager().getActiveWindow();
        if (v != null) {
            if (v.getClass() == Table.class) {
                Table table = (Table) v;
                if (actionCommand.compareTo("SELECTIONUP") == 0) showsSelectedRows(table);
                if (actionCommand.compareTo("INVERTSELECTION") == 0) invertSelection(table);
                table.getModel().setModified(true);
            }
        }
    }

    /**
     * Flip the selection (inverts selection)
     * @param table
     */
    private void invertSelection(Table table) {
        try {
            SelectableDataSource sds = table.getModel().getModelo().getRecordset();
            FBitSet selectedRows = sds.getSelection();
            selectedRows.flip(0, (int) sds.getRowCount());
            sds.setSelection(selectedRows);
        } catch (ReadDriverException e) {
            e.printStackTrace();
            NotificationManager.addError(e);
        }
    }

    private void showsSelectedRows(Table table) {
        long[] mapping = null;
        try {
            mapping = new long[table.getModel().getModelo().getRowCount()];
            FBitSet selectedRows = table.getModel().getModelo().getSelection();
            int m = 0;
            for (int i = selectedRows.nextSetBit(0); i >= 0; i = selectedRows.nextSetBit(i + 1)) {
                mapping[m] = i;
                m++;
            }
            for (int i = 0; i < mapping.length; i++) {
                if (!selectedRows.get(i)) {
                    mapping[m] = i;
                    m++;
                }
            }
            table.setOrder(mapping);
        } catch (ReadDriverException e) {
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEnabled() {
        IWindow v = PluginServices.getMDIManager().getActiveWindow();
        if (v == null) {
            return false;
        }
        if (v.getClass() == Table.class) {
            Table table = (Table) v;
            try {
                return table.getModel().getModelo().getSelection().cardinality() > 0;
            } catch (ReadDriverException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    protected boolean doIsEnabled(Table table) {
        try {
            BitSet indices = table.getSelectedFieldIndices();
            System.out.println("TableNumericFieldOperations.isEnabled: Tabla: " + table.getModel().getModelo().getRecordset().getName());
            if (indices.cardinality() == 1) {
                int type = table.getModel().getModelo().getRecordset().getFieldType(indices.nextSetBit(0));
                if ((type == Types.BIGINT) || (type == Types.DECIMAL) || (type == Types.DOUBLE) || (type == Types.FLOAT) || (type == Types.INTEGER) || (type == Types.SMALLINT) || (type == Types.TINYINT) || (type == Types.REAL) || (type == Types.NUMERIC)) {
                    return true;
                }
            }
        } catch (ReadDriverException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public boolean isVisible() {
        IWindow v = PluginServices.getMDIManager().getActiveWindow();
        if (v == null) {
            return false;
        }
        if (v instanceof Table) {
            return true;
        }
        return false;
    }
}
