package org.freebxml.omar.client.ui.web.client.browser;

import java.util.Iterator;
import java.util.Map;
import org.freebxml.omar.client.ui.web.client.AbstractAsyncCallback;
import org.freebxml.omar.client.ui.web.client.Application;
import org.freebxml.omar.client.ui.web.client.RegistryObjectTableData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.UpdateManager;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.Grid;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.ContentPanel;
import com.gwtext.client.widgets.layout.ContentPanelConfig;
import com.gwtext.client.widgets.layout.LayoutRegionConfig;
import com.gwtext.client.widgets.layout.NestedLayoutPanel;
import com.gwtext.client.widgets.layout.LayoutRegionConfig.LayoutRegionConstant;
import com.gwtext.client.widgets.layout.event.ContentPanelListenerAdapter;

public class Browser {

    private final ContentPanel contentPanel;

    private final BorderLayout borderLayout;

    private final ContentPanel listPanel;

    public Browser() {
        borderLayout = new BorderLayout(null, null, null, new LayoutRegionConfig() {

            {
                setSplit(true);
                setInitialSize(200);
                setMinSize(100);
                setMaxSize(400);
                setAutoScroll(false);
                setCollapsible(true);
                setTitlebar(true);
            }
        }, new LayoutRegionConfig() {

            {
                setSplit(true);
                setInitialSize(300);
                setMinSize(175);
                setMaxSize(400);
                setTitlebar(true);
                setCollapsible(true);
                setAutoScroll(true);
                setTitle("Browser");
            }
        }, null, new LayoutRegionConfig() {

            {
                setSplit(true);
                setInitialSize(300);
                setMinSize(175);
                setMaxSize(400);
                setTitlebar(true);
                setAutoScroll(true);
                setTitle("Browser");
            }
        });
        contentPanel = new NestedLayoutPanel(borderLayout, "Browser");
        borderLayout.beginUpdate();
        Application.service.getTreeViews(new AbstractAsyncCallback() {

            public void onSuccess(Object object) {
                for (Iterator it = ((Map) object).entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry entry = (Map.Entry) it.next();
                    String key = (String) entry.getKey();
                    ContentPanel cp = new ContentPanel(key + "-cp", (String) entry.getValue());
                    cp.add(new TreeViewPanel(Browser.this, key));
                    Browser.this.borderLayout.add(LayoutRegionConfig.WEST, cp);
                }
            }
        });
        ContentPanelConfig listPanelConfig = new ContentPanelConfig();
        listPanelConfig.setTitle("Registry object list");
        listPanelConfig.setFitToFrame(true);
        listPanelConfig.setFitToContainer(true);
        listPanel = new ContentPanel("list-panel", listPanelConfig);
        borderLayout.add(LayoutRegionConfig.SOUTH, listPanel);
        borderLayout.endUpdate();
    }

    public ContentPanel getContentPanel() {
        return contentPanel;
    }

    public void addPanel(LayoutRegionConstant direction, ContentPanel contentPanel) {
        borderLayout.add(direction, contentPanel);
    }

    public void nodeSelected(String treeViewId, Map properties) {
        Application.service.selectNode(treeViewId, properties, new AbstractAsyncCallback() {

            public void onSuccess(Object object) {
                RegistryObjectTableData data = (RegistryObjectTableData) object;
                listPanel.clear();
                if (data != null) {
                    String[] labels = data.getLabels();
                    int columns = labels.length;
                    MemoryProxy proxy = new MemoryProxy(data.getValues());
                    FieldDef[] fieldDefs = new FieldDef[columns + 1];
                    fieldDefs[0] = new StringFieldDef("id");
                    ColumnConfig[] columnConfigs = new ColumnConfig[columns];
                    for (int i = 0; i < columns; i++) {
                        String dataIndex = "column" + i;
                        fieldDefs[i + 1] = new StringFieldDef(dataIndex);
                        ColumnConfig columnConfig = new ColumnConfig();
                        columnConfig.setHeader(labels[i]);
                        columnConfig.setWidth(160);
                        columnConfig.setSortable(true);
                        columnConfig.setLocked(false);
                        columnConfig.setDataIndex(dataIndex);
                        columnConfigs[i] = columnConfig;
                    }
                    RecordDef recordDef = new RecordDef(fieldDefs);
                    ArrayReader reader = new ArrayReader(0, recordDef);
                    Store store = new Store(proxy, reader);
                    store.load();
                    ColumnModel columnModel = new ColumnModel(columnConfigs);
                    final Grid grid = new Grid("registry-object-grid", "100%", "100%", store, columnModel);
                    grid.addGridRowListener(new GridRowListenerAdapter() {

                        public void onRowClick(Grid grid, int rowIndex, EventObject event) {
                            String id = grid.getStore().getAt(rowIndex).getAsString("id");
                        }
                    });
                    grid.render();
                    listPanel.add(grid);
                    listPanel.addContentPanelListener(new ContentPanelListenerAdapter() {

                        public void onResize(ContentPanel cp, int width, int height) {
                            grid.render();
                        }
                    });
                }
            }
        });
    }
}
