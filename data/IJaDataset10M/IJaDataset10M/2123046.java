package org.jcvi.vics.web.gwt.common.client.ui.list;

import com.google.gwt.user.client.ui.*;
import org.jcvi.vics.web.gwt.common.client.service.log.Logger;
import org.jcvi.vics.web.gwt.common.client.ui.LoadingLabel;
import org.jcvi.vics.web.gwt.common.client.ui.SelectionListener;
import org.jcvi.vics.web.gwt.common.client.ui.imagebundles.ImageBundleFactory;
import org.jcvi.vics.web.gwt.common.client.ui.table.*;
import org.jcvi.vics.web.gwt.common.client.ui.table.columns.ImageColumn;
import org.jcvi.vics.web.gwt.common.client.ui.table.paging.LocalPaginator;
import org.jcvi.vics.web.gwt.common.client.ui.table.paging.PagingPanel;
import org.jcvi.vics.web.gwt.common.client.util.HtmlUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Press
 */
public class MultiSelectList extends Composite {

    private static Logger _logger = Logger.getLogger("org.jcvi.vics.web.gwt.common.client.ui.list.MultiSelectList");

    private List<String> _items;

    private List<String> _selectedItems;

    private SelectionListener _listener;

    private LocalPaginator _paginator;

    private PagingPanel _pagingPanel;

    private static final String TABLE_SIZE_PREFERENCE_KEY = "MultiSelectListTable";

    private AbstractImagePrototype SELECTED_IMAGE = ImageBundleFactory.getControlImageBundle().getCheckImage();

    private AbstractImagePrototype UNSELECTED_IMAGE = ImageBundleFactory.getControlImageBundle().getBlankImage();

    public MultiSelectList(String headerText) {
        this(headerText, null);
    }

    public MultiSelectList(String headerText, SelectionListener selectionListener) {
        addSelectionListener(selectionListener);
        init(headerText);
    }

    private void init(String header) {
        SortableTable table = new SortableTable();
        table.addColumn(new ImageColumn(header, true, true));
        table.addStyleName("MultiSelectList");
        table.setDefaultSortColumns(new SortableColumn[] { new SortableColumn(0, header, SortableColumn.SORT_ASC) });
        _paginator = new LocalPaginator(table, new LoadingLabel("Loading...", true), TABLE_SIZE_PREFERENCE_KEY);
        _pagingPanel = new MultiSelectListPagingPanel(table, TABLE_SIZE_PREFERENCE_KEY, new SelectAllClickListener(), new SelectNoneClickListener());
        _pagingPanel.setNoDataMessage("No matching data.");
        _pagingPanel.setSortColumns(new SortableColumn[] { new SortableColumn(0, header, SortableColumn.SORT_ASC) });
        table.setTableController(new MultiSelectSortableTableController(table));
        table.addSelectionListener(new ListItemClickedListener(this));
        Panel mainPanel = new HorizontalPanel();
        mainPanel.add(_pagingPanel);
        initWidget(mainPanel);
    }

    private class SelectAllClickListener implements ClickListener {

        public void onClick(Widget sender) {
            selectUnselectAll(true, getAvailableItems());
        }
    }

    private class SelectNoneClickListener implements ClickListener {

        public void onClick(Widget sender) {
            selectUnselectAll(false, null);
        }
    }

    private void selectUnselectAll(boolean selected, List<String> selectedList) {
        for (String item : _items) {
            if (selected && !isItemSelected(item)) notifyListeners(selected, item); else if (!selected && isItemSelected(item)) notifyListeners(selected, item);
        }
        setSelectedList(selectedList, false);
        SortableTable table = _pagingPanel.getSortableTable();
        updateAllRows();
        for (int i = 1; i < table.getRowCount(); i++) updateRow(i, table.getValue(i, 0).getValue().toString(), true);
    }

    /**
     * Sets list of available items but does NOT refresh table
     */
    public void setAvailableList(List<String> items) {
        setAvailableList(items, false);
    }

    /**
     * Sets list of available items and refreshes table if refresh==true
     */
    public void setAvailableList(List<String> items, boolean refresh) {
        _items = items;
        if (refresh) refresh();
    }

    public void refresh() {
        _pagingPanel.clear();
        updateAllRows();
        _pagingPanel.first();
        _pagingPanel.getSortableTable().sort();
    }

    /**
     * Recalculates the widget in every row (even non-visible pages) and updates the paginator but does NOT refresh the table
     */
    private void updateAllRows() {
        ArrayList<TableRow> rows = new ArrayList();
        for (String item : _items) {
            TableRow row = new TableRow();
            row.setValue(0, new TableCell(item, getListItemWidget(item)));
            rows.add(row);
        }
        _pagingPanel.getPaginator().setData(rows);
    }

    /**
     * Sets given items as selected but does NOT refresh table
     */
    public void setSelectedList(List<String> items) {
        _selectedItems = items;
    }

    /**
     * Sets given items as selected; refreshes table if refresh==true
     */
    public void setSelectedList(List<String> items, boolean refresh) {
        _selectedItems = items;
        if (refresh) refresh();
    }

    /**
     * 1-based rows
     */
    private Widget getListItemWidget(String item) {
        HorizontalPanel panel = new HorizontalPanel();
        if (_selectedItems != null && _selectedItems.contains(item)) panel.add(createImage(SELECTED_IMAGE)); else panel.add(createImage(UNSELECTED_IMAGE));
        panel.add(HtmlUtils.getHtml(item, "text"));
        return panel;
    }

    private Widget createImage(AbstractImagePrototype prototype) {
        Image image = prototype.createImage();
        image.setStyleName("MultiSelectImage");
        return image;
    }

    public void addSelectionListener(SelectionListener listener) {
        _listener = listener;
    }

    protected void notifyListeners(boolean onSelect, String value) {
        if (_listener != null) {
            if (onSelect) _listener.onSelect(value); else _listener.onUnSelect(value);
        }
    }

    public List<String> getSelectedItems() {
        return _selectedItems;
    }

    public List<String> getAvailableItems() {
        return _items;
    }

    private class ListItemClickedListener implements SelectionListener {

        private MultiSelectList _parent;

        private ListItemClickedListener(MultiSelectList parent) {
            _parent = parent;
        }

        public void onSelect(String row) {
            onClick(row);
        }

        public void onUnSelect(String row) {
        }

        private void onClick(String rowVal) {
            int row = Integer.valueOf(rowVal);
            String value = _pagingPanel.getSortableTable().getText(row, 0);
            if (isItemSelected(value)) {
                unselectItem(row, value);
                _parent.notifyListeners(false, value);
            } else {
                selectItem(row, value);
                _parent.notifyListeners(true, value);
            }
        }
    }

    public void selectItem(Integer row, String value) {
        if (_selectedItems == null) _selectedItems = new ArrayList();
        if (!_selectedItems.contains(value)) _selectedItems.add(value);
        updateRow(row, value, true);
    }

    public void unselectItem(String value) {
        int row = findItemInTable(value);
        if (row > 0) unselectItem(row, value);
    }

    private int findItemInTable(String value) {
        int row = -1;
        for (int i = 1; i < _pagingPanel.getSortableTable().getRowCount(); i++) {
            if (_pagingPanel.getSortableTable().getValue(i, 0).getValue().toString().equals(value)) {
                _logger.debug("found item in table row  " + i);
                row = i;
            }
        }
        return row;
    }

    public void unselectItem(int row, String value) {
        _selectedItems.remove(value);
        _logger.debug("unselectItem: now " + _selectedItems.size() + " items selected");
        updateRow(row, value, true);
    }

    public boolean isItemSelected(String value) {
        return _selectedItems != null && value != null && _selectedItems.contains(value);
    }

    public int getNumSelected() {
        return (_selectedItems == null) ? 0 : _selectedItems.size();
    }

    /**
     * 1-based rows
     */
    private void updateRow(int row, String value, boolean refreshCell) {
        HorizontalPanel panel = new HorizontalPanel();
        if (SELECTED_IMAGE != null && _selectedItems != null && _selectedItems.contains(value)) panel.add(createImage(SELECTED_IMAGE)); else panel.add(createImage(UNSELECTED_IMAGE));
        panel.add(HtmlUtils.getHtml(value, "text"));
        _paginator.getSortableTable().setValue(row, 0, value, panel, refreshCell);
    }
}
