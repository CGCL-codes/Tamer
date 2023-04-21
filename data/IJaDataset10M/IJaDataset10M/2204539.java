package net.sourceforge.nattable.extension.glazedlists.examples;

import java.util.HashMap;
import java.util.Map;
import net.sourceforge.nattable.NatTable;
import net.sourceforge.nattable.data.ListDataProvider;
import net.sourceforge.nattable.dataset.pricing.ColumnHeaders;
import net.sourceforge.nattable.dataset.pricing.PricingDataBean;
import net.sourceforge.nattable.dataset.pricing.PricingDataBeanGenerator;
import net.sourceforge.nattable.examples.AbstractNatExample;
import net.sourceforge.nattable.examples.runner.StandaloneNatExampleRunner;
import net.sourceforge.nattable.extension.glazedlists.GlazedListsEventLayer;
import net.sourceforge.nattable.grid.GridRegion;
import net.sourceforge.nattable.grid.data.DummyColumnHeaderDataProvider;
import net.sourceforge.nattable.grid.layer.ColumnHeaderLayer;
import net.sourceforge.nattable.grid.layer.DefaultBodyDataLayer;
import net.sourceforge.nattable.layer.CompositeLayer;
import net.sourceforge.nattable.layer.DataLayer;
import net.sourceforge.nattable.layer.ILayer;
import net.sourceforge.nattable.reorder.ColumnReorderLayer;
import net.sourceforge.nattable.selection.SelectionLayer;
import net.sourceforge.nattable.util.ArrayUtil;
import net.sourceforge.nattable.viewport.ViewportLayer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;

public class GlazedListsEventLayerExample extends AbstractNatExample {

    public static void main(String[] args) {
        StandaloneNatExampleRunner.run(new GlazedListsEventLayerExample());
    }

    private EventList<PricingDataBean> rowObjectsGlazedList;

    public Control createExampleControl(Composite parent) {
        EventList<PricingDataBean> eventList = GlazedLists.eventList(PricingDataBeanGenerator.getData(10));
        rowObjectsGlazedList = GlazedLists.threadSafeList(eventList);
        Map<String, String> propertyToLabelMap = populateColHeaderPropertiesToLabelsMap();
        String[] propertyNames = propertyToLabelMap.keySet().toArray(ArrayUtil.STRING_TYPE_ARRAY);
        ListDataProvider<PricingDataBean> bodyDataProvider = new ListDataProvider<PricingDataBean>(rowObjectsGlazedList, propertyNames);
        DataLayer bodyDataLayer = new DefaultBodyDataLayer(bodyDataProvider);
        GlazedListsEventLayer<PricingDataBean> glazedListsBodyDataLayer = new GlazedListsEventLayer<PricingDataBean>(bodyDataLayer);
        eventList.addListEventListener(glazedListsBodyDataLayer);
        SelectionLayer selectionLayer = new SelectionLayer(new ColumnReorderLayer(new DataLayer(GridRegion.BODY, bodyDataProvider)));
        ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);
        ILayer columnHeaderLayer = new ColumnHeaderLayer(new DataLayer(GridRegion.COLUMN_HEADER, new DummyColumnHeaderDataProvider(bodyDataProvider)), viewportLayer, selectionLayer);
        CompositeLayer compositeLayer = new CompositeLayer(1, 2);
        compositeLayer.setChildLayer(columnHeaderLayer, 0, 0);
        compositeLayer.setChildLayer(viewportLayer, 0, 1);
        return new NatTable(parent, compositeLayer);
    }

    private Map<String, String> populateColHeaderPropertiesToLabelsMap() {
        Map<String, String> propertyToLabelMap = new HashMap<String, String>();
        ColumnHeaders[] columnHeaders = ColumnHeaders.values();
        for (int i = 0; i < columnHeaders.length; i++) {
            propertyToLabelMap.put(columnHeaders[i].getProperty(), columnHeaders[i].getLabel());
        }
        return propertyToLabelMap;
    }
}
