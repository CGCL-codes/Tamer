package muvis.view.table.filters;

import java.util.ArrayList;
import java.util.List;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import utils.Observable;
import utils.Observer;
import muvis.view.table.TracksTableModel;

/**
 *
 * @author Ricardo
 */
public class TableFilterManager implements Observer {

    private ArrayList<TableFilter> filters;

    private TableRowSorter<TracksTableModel> sorter;

    public TableFilterManager(TableRowSorter<TracksTableModel> sorter) {
        this.filters = new ArrayList<TableFilter>();
        this.sorter = sorter;
    }

    public void addTableFilter(TableFilter filter) {
        filters.add(filter);
    }

    public void resetFilters() {
        for (TableFilter filter : filters) {
            filter.reset();
        }
    }

    public void removeTableFilter(TableFilter filter) {
        filters.remove(filter);
    }

    public void filter() {
        List<RowFilter<TracksTableModel, Object>> listFilters = new ArrayList<RowFilter<TracksTableModel, Object>>();
        for (TableFilter filter : filters) {
            listFilters.add(filter.filter());
        }
        RowFilter composedFilter = RowFilter.andFilter(listFilters);
        sorter.setRowFilter(composedFilter);
    }

    @Override
    public void update(Observable obs, Object arg) {
        for (Observer observer : filters) {
            observer.update(obs, arg);
        }
        filter();
    }
}
