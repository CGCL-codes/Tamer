package net.sourceforge.nattable.test.integration;

import java.util.Map;
import net.sourceforge.nattable.config.IConfigRegistry;
import net.sourceforge.nattable.data.IColumnPropertyAccessor;
import net.sourceforge.nattable.data.IDataProvider;
import net.sourceforge.nattable.data.ListDataProvider;
import net.sourceforge.nattable.data.ReflectiveColumnPropertyAccessor;
import net.sourceforge.nattable.extension.glazedlists.GlazedListsEventLayer;
import net.sourceforge.nattable.extension.glazedlists.GlazedListsSortModel;
import net.sourceforge.nattable.grid.data.DefaultColumnHeaderDataProvider;
import net.sourceforge.nattable.grid.data.DefaultCornerDataProvider;
import net.sourceforge.nattable.grid.data.DefaultRowHeaderDataProvider;
import net.sourceforge.nattable.grid.layer.ColumnHeaderLayer;
import net.sourceforge.nattable.grid.layer.CornerLayer;
import net.sourceforge.nattable.grid.layer.DefaultColumnHeaderDataLayer;
import net.sourceforge.nattable.grid.layer.DefaultRowHeaderDataLayer;
import net.sourceforge.nattable.grid.layer.GridLayer;
import net.sourceforge.nattable.grid.layer.RowHeaderLayer;
import net.sourceforge.nattable.layer.DataLayer;
import net.sourceforge.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import net.sourceforge.nattable.layer.stack.DefaultBodyLayerStack;
import net.sourceforge.nattable.sort.SortHeaderLayer;
import net.sourceforge.nattable.sort.command.SortColumnCommand;
import net.sourceforge.nattable.util.IClientAreaProvider;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;

/**
 * This is a copy of the net.sourceforge.nattable.examples.fixtures.GlazedListsGridLayer
 * It has been copied here since glazed list tests bundle can't depend on the examples bundle
 */
public class GlazedListsGridLayer<T> extends GridLayer {

    private ColumnOverrideLabelAccumulator columnLabelAccumulator;

    private DataLayer bodyDataLayer;

    private DataLayer columnHeaderDataLayer;

    private DefaultBodyLayerStack bodyLayerStack;

    private ListDataProvider<T> bodyDataProvider;

    public GlazedListsGridLayer(EventList<T> eventList, String[] propertyNames, Map<String, String> propertyToLabelMap, IConfigRegistry configRegistry) {
        this(eventList, propertyNames, propertyToLabelMap, configRegistry, true);
    }

    /**
	 * The underlying {@link DataLayer} created is able to handle Events raised by GlazedLists
	 * and fire corresponding NatTable events.
	 *
	 * The {@link SortHeaderLayer} triggers sorting on the the underlying SortedList when
	 * a {@link SortColumnCommand} is received.
	 */
    public GlazedListsGridLayer(EventList<T> eventList, String[] propertyNames, Map<String, String> propertyToLabelMap, IConfigRegistry configRegistry, boolean useDefaultConfiguration) {
        super(useDefaultConfiguration);
        SortedList<T> sortedList = new SortedList<T>(eventList, null);
        IColumnPropertyAccessor<T> columnPropertyAccessor = new ReflectiveColumnPropertyAccessor<T>(propertyNames);
        bodyDataProvider = new ListDataProvider<T>(sortedList, columnPropertyAccessor);
        bodyDataLayer = new DataLayer(bodyDataProvider);
        GlazedListsEventLayer<T> glazedListsEventLayer = new GlazedListsEventLayer<T>(bodyDataLayer, eventList);
        bodyLayerStack = new DefaultBodyLayerStack(glazedListsEventLayer);
        IDataProvider columnHeaderDataProvider = new DefaultColumnHeaderDataProvider(propertyNames, propertyToLabelMap);
        columnHeaderDataLayer = new DefaultColumnHeaderDataLayer(columnHeaderDataProvider);
        ColumnHeaderLayer columnHeaderLayer = new ColumnHeaderLayer(columnHeaderDataLayer, bodyLayerStack, bodyLayerStack.getSelectionLayer());
        SortHeaderLayer<T> columnHeaderSortableLayer = new SortHeaderLayer<T>(columnHeaderLayer, new GlazedListsSortModel<T>(sortedList, columnPropertyAccessor, configRegistry, columnHeaderDataLayer), false);
        DefaultRowHeaderDataProvider rowHeaderDataProvider = new DefaultRowHeaderDataProvider(bodyDataProvider);
        DefaultRowHeaderDataLayer rowHeaderDataLayer = new DefaultRowHeaderDataLayer(rowHeaderDataProvider);
        RowHeaderLayer rowHeaderLayer = new RowHeaderLayer(rowHeaderDataLayer, bodyLayerStack, bodyLayerStack.getSelectionLayer());
        DefaultCornerDataProvider cornerDataProvider = new DefaultCornerDataProvider(columnHeaderDataProvider, rowHeaderDataProvider);
        DataLayer cornerDataLayer = new DataLayer(cornerDataProvider);
        CornerLayer cornerLayer = new CornerLayer(cornerDataLayer, rowHeaderLayer, columnHeaderLayer);
        setBodyLayer(bodyLayerStack);
        setColumnHeaderLayer(columnHeaderSortableLayer);
        setRowHeaderLayer(rowHeaderLayer);
        setCornerLayer(cornerLayer);
    }

    public ColumnOverrideLabelAccumulator getColumnLabelAccumulator() {
        return columnLabelAccumulator;
    }

    @Override
    public void setClientAreaProvider(IClientAreaProvider clientAreaProvider) {
        super.setClientAreaProvider(clientAreaProvider);
    }

    public DataLayer getBodyDataLayer() {
        return bodyDataLayer;
    }

    public ListDataProvider<T> getBodyDataProvider() {
        return bodyDataProvider;
    }

    public DataLayer getColumnHeaderDataLayer() {
        return columnHeaderDataLayer;
    }

    public DefaultBodyLayerStack getBodyLayerStack() {
        return bodyLayerStack;
    }
}
