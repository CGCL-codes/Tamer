package com.gwtext.sample.showcase2.client.tree;

import com.gwtext.client.core.Connection;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.dd.DragData;
import com.gwtext.client.dd.DragDrop;
import com.gwtext.client.dd.DragSource;
import com.gwtext.client.dd.DropTarget;
import com.gwtext.client.dd.DropTargetConfig;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Radio;
import com.gwtext.client.widgets.grid.*;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;
import com.gwtext.client.widgets.tree.*;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;
import com.gwtext.sample.showcase2.client.SampleData;
import com.gwtext.sample.showcase2.client.ShowcasePanel;
import com.gwtext.client.core.EventObject;

public class GridTreeDDSample extends ShowcasePanel {

    public String getSourceUrl() {
        return "source/tree/GridTreeDDSample.java.html";
    }

    public Panel getViewPanel() {
        if (panel == null) {
            panel = new Panel();
            FormPanel formPanel = new FormPanel();
            formPanel.setHideLabels(true);
            FieldSet fieldSet = new FieldSet("Drop style");
            fieldSet.setWidth(420);
            formPanel.add(fieldSet);
            final Radio moveRadio = new Radio();
            moveRadio.setName("dropstyle");
            moveRadio.setBoxLabel("Move");
            moveRadio.setChecked(true);
            fieldSet.add(moveRadio);
            final Radio copyRadio = new Radio();
            copyRadio.setName("dropstyle");
            copyRadio.setBoxLabel("Copy");
            fieldSet.add(copyRadio);
            final Store store = new SimpleStore(new String[] { "abbr", "country" }, SampleData.getCountries());
            store.load();
            final Store store2 = new SimpleStore(new String[] { "abbr", "country" }, new String[][] {});
            store.load();
            ColumnConfig[] columns = { new ColumnConfig("Flag", "abbr", 45, true, new Renderer() {

                public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
                    return Format.format("<img src=\"images/flags/{0}.gif\">", new String[] { record.getAsString("abbr") });
                }
            }, "abbr"), new ColumnConfig("Code", "abbr", 45), new ColumnConfig("Country", "country", 90, true, null, "country") };
            ColumnModel columnModel = new ColumnModel(columns);
            final GridPanel countriesGrid = new GridPanel();
            countriesGrid.setTitle("Countries");
            countriesGrid.setStore(store);
            countriesGrid.setColumnModel(columnModel);
            countriesGrid.setHeight(390);
            countriesGrid.setWidth(200);
            countriesGrid.setAutoExpandColumn("country");
            countriesGrid.setEnableDragDrop(true);
            countriesGrid.setDdGroup("myDDGroup");
            final TreePanel tripTreePanel = new TreePanel();
            tripTreePanel.setTitle("Trip Planner");
            tripTreePanel.setAnimate(true);
            tripTreePanel.setEnableDrop(true);
            tripTreePanel.setDdGroup("myDDGroup");
            tripTreePanel.setContainerScroll(true);
            tripTreePanel.setRootVisible(true);
            tripTreePanel.setWidth(200);
            tripTreePanel.setHeight(390);
            tripTreePanel.setEnableDD(true);
            final XMLTreeLoader tripLoader = new XMLTreeLoader();
            tripLoader.setDataUrl("data/trip.xml");
            tripLoader.setMethod(Connection.GET);
            tripLoader.setRootTag("vacations");
            tripLoader.setFolderIdMapping("@title");
            tripLoader.setFolderTag("trip");
            tripLoader.setQtipMapping("@qtip");
            tripLoader.setIconMapping("@icon");
            tripLoader.setAttributeMappings(new String[] { "@trip" });
            final AsyncTreeNode tripRoot = new AsyncTreeNode("Places to Visit", tripLoader);
            tripTreePanel.setRootNode(tripRoot);
            tripRoot.expand();
            tripTreePanel.expandAll();
            tripTreePanel.addListener(new TreePanelListenerAdapter() {

                public boolean doBeforeNodeDrop(TreePanel treePanel, TreeNode target, DragData dragData, String point, DragDrop source, TreeNode dropNode, DropNodeCallback dropDropNodeCallback) {
                    if (dragData instanceof GridDragData) {
                        GridDragData gridDragData = (GridDragData) dragData;
                        Record[] records = gridDragData.getSelections();
                        TreeNode[] copyNodes = new TreeNode[records.length];
                        for (int i = 0; i < records.length; i++) {
                            Record record = records[i];
                            String country = record.getAsString("country");
                            TreeNode copyNode = new TreeNode(country);
                            copyNode.setId(country);
                            copyNode.setIcon("images/flags/" + record.getAsString("abbr") + ".gif");
                            copyNodes[i] = copyNode;
                            target.appendChild(copyNode);
                            if (moveRadio.getValue()) {
                                GridPanel grid = gridDragData.getGrid();
                                Store store = grid.getStore();
                                store.remove(record);
                                store.commitChanges();
                                store2.add(record);
                                store2.commitChanges();
                            }
                        }
                    }
                    return true;
                }
            });
            DropTargetConfig cfg = new DropTargetConfig();
            cfg.setdDdGroup("myDDGroup");
            DropTarget tg = new DropTarget(countriesGrid, cfg) {

                public boolean notifyDrop(DragSource source, EventObject e, DragData data) {
                    if (data instanceof GridDragData) return false;
                    TreeDragData treeDragData = (TreeDragData) data;
                    TreeNode treeNode = treeDragData.getTreeNode();
                    String country = treeNode.getText();
                    GridView view = countriesGrid.getView();
                    int droppedRow = view.findRowIndex(e);
                    int index = store2.find("country", country, 0, true, true);
                    Record record = store2.getAt(index);
                    if (record != null) {
                        if (moveRadio.getValue()) {
                            if (droppedRow == -1) {
                                store.add(record);
                            } else {
                                store.insert(droppedRow, record);
                            }
                            store.commitChanges();
                        }
                        store2.remove(record);
                        store2.commitChanges();
                    }
                    TreeNode node = tripTreePanel.getNodeById(country);
                    tripRoot.removeChild(node);
                    return true;
                }

                public String notifyOver(DragSource source, EventObject e, DragData data) {
                    return "x-dd-drop-ok";
                }
            };
            Panel horizontalPanel = new Panel();
            horizontalPanel.setLayout(new HorizontalLayout(25));
            horizontalPanel.add(countriesGrid);
            horizontalPanel.add(tripTreePanel);
            Panel verticalPanel = new Panel();
            verticalPanel.setLayout(new VerticalLayout(15));
            verticalPanel.add(fieldSet);
            verticalPanel.add(horizontalPanel);
            panel.add(verticalPanel);
        }
        return panel;
    }

    public String getIntro() {
        return "This example demonstrates Drag & Drop from a Grid to a Tree and vice-versa";
    }
}
