package org.jmesa.view.html.toolbar;

import org.jmesa.view.ExportTypesSupport;
import org.jmesa.view.component.Table;
import org.jmesa.view.component.TableSupport;

/**
 * @since 2.0
 * @author Jeff Johnston
 */
public abstract class AbstractToolbar extends Toolbar implements TableSupport, ExportTypesSupport, MaxRowsIncrementsSupport {

    private Table table;

    private String[] exportTypes;

    private int[] maxRowsIncrements;

    protected boolean enableSeparators = true;

    protected boolean enablePageNumbers = false;

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String[] getExportTypes() {
        return exportTypes;
    }

    public void setExportTypes(String... exportTypes) {
        this.exportTypes = exportTypes;
    }

    public int[] getMaxRowsIncrements() {
        return maxRowsIncrements;
    }

    public void setMaxRowsIncrements(int[] maxRowsIncrements) {
        this.maxRowsIncrements = maxRowsIncrements;
    }

    public void enableSeparators(boolean isEnabled) {
        this.enableSeparators = isEnabled;
    }

    public void enablePageNumbers(boolean isEnabled) {
        this.enablePageNumbers = isEnabled;
    }
}
