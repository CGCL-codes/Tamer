package net.sourceforge.nattable.filterrow;

import net.sourceforge.nattable.command.ILayerCommand;
import net.sourceforge.nattable.config.IConfigRegistry;
import net.sourceforge.nattable.data.IDataProvider;
import net.sourceforge.nattable.filterrow.command.ToggleFilterRowCommand;
import net.sourceforge.nattable.grid.GridRegion;
import net.sourceforge.nattable.grid.layer.ColumnHeaderLayer;
import net.sourceforge.nattable.grid.layer.DimensionallyDependentLayer;
import net.sourceforge.nattable.layer.CompositeLayer;
import net.sourceforge.nattable.layer.DataLayer;
import net.sourceforge.nattable.layer.ILayer;
import net.sourceforge.nattable.layer.event.RowStructuralRefreshEvent;

/**
 * 1 column x 2 rows Composite layer<br/>
 * - First row is the {@link ColumnHeaderLayer}<br/>
 * - Second row is the composite is the filter row layer. The filter row layer is a {@link DimensionallyDependentLayer}
 * 		dependent on the {@link ColumnHeaderLayer}
 *
 * @see FilterRowDataLayer
 */
public class FilterRowHeaderComposite<T> extends CompositeLayer {

    private final DataLayer filterRowDataLayer;

    private boolean filterRowVisible = true;

    public FilterRowHeaderComposite(IFilterStrategy<T> filterStrategy, ILayer columnHeaderLayer, IDataProvider columnHeaderDataProvider, IConfigRegistry configRegistry) {
        super(1, 2);
        setChildLayer("columnHeader", columnHeaderLayer, 0, 0);
        filterRowDataLayer = new FilterRowDataLayer<T>(filterStrategy, columnHeaderLayer, columnHeaderDataProvider, configRegistry);
        DimensionallyDependentLayer filterRowLayer = new DimensionallyDependentLayer(filterRowDataLayer, columnHeaderLayer, filterRowDataLayer);
        setChildLayer(GridRegion.FILTER_ROW, filterRowLayer, 0, 1);
    }

    @Override
    public int getHeight() {
        if (filterRowVisible) {
            return super.getHeight();
        } else {
            ChildLayerInfo lastChildLayerInfo = getChildLayerInfoByLayout(0, 1);
            return lastChildLayerInfo.getHeightOffset();
        }
    }

    @Override
    public int getRowCount() {
        if (filterRowVisible) {
            return super.getRowCount();
        } else {
            return super.getRowCount() - 1;
        }
    }

    public boolean isFilterRowVisible() {
        return filterRowVisible;
    }

    public void setFilterRowVisible(boolean filterRowVisible) {
        this.filterRowVisible = filterRowVisible;
        fireLayerEvent(new RowStructuralRefreshEvent(filterRowDataLayer));
    }

    @Override
    public boolean doCommand(ILayerCommand command) {
        if (command instanceof ToggleFilterRowCommand) {
            setFilterRowVisible(!filterRowVisible);
            return true;
        }
        return super.doCommand(command);
    }
}
