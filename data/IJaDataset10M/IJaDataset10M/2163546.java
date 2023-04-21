package com.ezware.oxbow.swingbits.table.filter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import com.ezware.oxbow.swingbits.util.IObjectToStringTranslator;

public final class TableRowFilterSupport {

    private boolean searchable = false;

    private IObjectToStringTranslator translator;

    private final ITableFilter<?> filter;

    private boolean actionsVisible = true;

    private boolean useTableRenderers = false;

    private TableRowFilterSupport(ITableFilter<?> filter) {
        if (filter == null) throw new NullPointerException();
        this.filter = filter;
    }

    public static TableRowFilterSupport forTable(JTable table) {
        return new TableRowFilterSupport(new JTableFilter(table));
    }

    public static TableRowFilterSupport forFilter(ITableFilter<?> filter) {
        return new TableRowFilterSupport(filter);
    }

    /**
	 * Additional actions visible in column filter list
	 * 
	 * @param visible
	 * @return
	 */
    public TableRowFilterSupport actions(boolean visible) {
        this.actionsVisible = visible;
        return this;
    }

    /**
	 * Comlumn filter list is searchable
	 * 
	 * @param serachable
	 * @return
	 */
    public TableRowFilterSupport searchable(boolean serachable) {
        this.searchable = serachable;
        return this;
    }

    public TableRowFilterSupport searchTransalator(IObjectToStringTranslator translator) {
        this.translator = translator;
        return this;
    }

    public TableRowFilterSupport useTableRenderers(boolean value) {
        this.useTableRenderers = value;
        return this;
    }

    public JTable apply() {
        final TableFilterColumnPopup filterPopup = new TableFilterColumnPopup(filter);
        filterPopup.setEnabled(true);
        filterPopup.setActionsVisible(actionsVisible);
        filterPopup.setSearchable(searchable);
        filterPopup.setSearchTranslator(translator);
        filterPopup.setUseTableRenderers(useTableRenderers);
        setupTableHeader();
        return filter.getTable();
    }

    private void setupTableHeader() {
        final JTable table = filter.getTable();
        filter.addChangeListener(new IFilterChangeListener() {

            public void filterChanged(ITableFilter<?> filter) {
                table.getTableHeader().repaint();
            }
        });
        setupHeaderRenderers(table.getModel(), true);
    }

    private void setupHeaderRenderers(TableModel newModel, boolean fullSetup) {
        JTable table = filter.getTable();
        FilterTableHeaderRenderer headerRenderer = new FilterTableHeaderRenderer(filter);
        filter.modelChanged(newModel);
        for (TableColumn c : Collections.list(table.getColumnModel().getColumns())) {
            c.setHeaderRenderer(headerRenderer);
        }
        if (!fullSetup) return;
        table.addPropertyChangeListener("model", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent e) {
                setupHeaderRenderers((TableModel) e.getNewValue(), false);
            }
        });
    }
}
