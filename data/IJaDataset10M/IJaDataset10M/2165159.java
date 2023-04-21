package net.sourceforge.nattable.extension.glazedlists.test.integration;

import static org.junit.Assert.assertEquals;
import java.io.Serializable;
import net.sourceforge.nattable.NatTable;
import net.sourceforge.nattable.config.ConfigRegistry;
import net.sourceforge.nattable.config.IConfigRegistry;
import net.sourceforge.nattable.data.IRowIdAccessor;
import net.sourceforge.nattable.data.ListDataProvider;
import net.sourceforge.nattable.selection.RowSelectionModel;
import net.sourceforge.nattable.selection.RowSelectionProvider;
import net.sourceforge.nattable.selection.SelectionLayer;
import net.sourceforge.nattable.selection.command.SelectRowsCommand;
import net.sourceforge.nattable.sort.command.SortColumnCommand;
import net.sourceforge.nattable.sort.config.DefaultSortConfiguration;
import net.sourceforge.nattable.test.fixture.NatTableFixture;
import net.sourceforge.nattable.test.fixture.data.RowDataFixture;
import net.sourceforge.nattable.test.fixture.data.RowDataListFixture;
import org.eclipse.jface.viewers.StructuredSelection;
import org.junit.Before;
import org.junit.Test;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;

public class RowSelectionIntegrationTest {

    private NatTable nattable;

    private EventList<RowDataFixture> eventListFixture;

    private ListDataProvider<RowDataFixture> bodyDataProvider;

    private SelectionLayer selectionLayer;

    private RowSelectionProvider<RowDataFixture> selectionProvider;

    @Before
    public void setup() {
        IConfigRegistry configRegistry = new ConfigRegistry();
        eventListFixture = GlazedLists.eventList(RowDataListFixture.getList(10));
        GlazedListsGridLayer<RowDataFixture> gridLayer = new GlazedListsGridLayer<RowDataFixture>(eventListFixture, RowDataListFixture.getPropertyNames(), RowDataListFixture.getPropertyToLabelMap(), configRegistry);
        nattable = new NatTableFixture(gridLayer, false);
        nattable.setConfigRegistry(configRegistry);
        selectionLayer = gridLayer.getBodyLayerStack().getSelectionLayer();
        bodyDataProvider = gridLayer.getBodyDataProvider();
        selectionProvider = new RowSelectionProvider<RowDataFixture>(selectionLayer, bodyDataProvider);
        nattable.addConfiguration(new DefaultSortConfiguration());
        selectionLayer.setSelectionModel(new RowSelectionModel<RowDataFixture>(selectionLayer, bodyDataProvider, new IRowIdAccessor<RowDataFixture>() {

            public Serializable getRowId(RowDataFixture rowObject) {
                return rowObject.getSecurity_id();
            }
        }));
        gridLayer.getGlazedListsEventLayer().setTestMode(true);
        nattable.configure();
    }

    @Test
    public void shouldPreserveRowSelectionOnDataUpdates() throws Exception {
        assertEquals(0, selectionLayer.getFullySelectedRowPositions().length);
        nattable.doCommand(new SelectRowsCommand(nattable, 1, 1, false, false));
        assertEquals(1, selectionLayer.getFullySelectedRowPositions().length);
        assertEquals("B Ford Motor", nattable.getDataValueByPosition(2, 1).toString());
        assertEquals("B Ford Motor", getSelected().getSecurity_description());
        eventListFixture.add(0, RowDataFixture.getInstance("Tata motors", "A"));
        Thread.sleep(100);
        assertEquals("Tata motors", nattable.getDataValueByPosition(2, 1).toString());
        assertEquals("B Ford Motor", getSelected().getSecurity_description());
    }

    @Test
    public void shouldPreserveRowSelectionOnSort() throws Exception {
        assertEquals(0, selectionLayer.getFullySelectedRowPositions().length);
        assertEquals("B Ford Motor", nattable.getDataValueByPosition(2, 1).toString());
        assertEquals("A Alphabet Co.", nattable.getDataValueByPosition(2, 2).toString());
        assertEquals("C General Electric Co", nattable.getDataValueByPosition(2, 3).toString());
        nattable.doCommand(new SelectRowsCommand(nattable, 1, 1, false, false));
        assertEquals("B Ford Motor", getSelected().getSecurity_description());
        nattable.doCommand(new SortColumnCommand(nattable, 2, false));
        assertEquals("A Alphabet Co.", nattable.getDataValueByPosition(2, 1).toString());
        assertEquals("B Ford Motor", nattable.getDataValueByPosition(2, 2).toString());
        assertEquals("C General Electric Co", nattable.getDataValueByPosition(2, 3).toString());
        assertEquals("B Ford Motor", getSelected().getSecurity_description());
    }

    private RowDataFixture getSelected() {
        return (RowDataFixture) ((StructuredSelection) selectionProvider.getSelection()).iterator().next();
    }
}
