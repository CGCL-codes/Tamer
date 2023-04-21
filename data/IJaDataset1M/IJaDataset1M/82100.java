package org.scrinch.gui;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.scrinch.model.Item;
import org.scrinch.model.ScrinchEnvToolkit;
import org.scrinch.model.Sprint;

public class ItemCellEditorAndRenderer implements TableCellEditor, TableCellRenderer {

    private static final Color BGCOLOR_1 = new Color(250, 250, 250);

    private static final Color BGCOLOR_2 = new Color(231, 240, 251);

    private List<ItemPanel> itemPanelList = new ArrayList<ItemPanel>();

    private Map<Item, ItemPanel> itemPanelMap = new HashMap<Item, ItemPanel>();

    private Map<Integer, ItemPanel> itemPanelIndexedMap = new HashMap<Integer, ItemPanel>();

    private List<CellEditorListener> listeners = new ArrayList<CellEditorListener>();

    private ItemTablePanel itemTablePanel;

    private boolean projectNameVisible;

    private final ScrinchEnvToolkit toolkit;

    public ItemCellEditorAndRenderer(ItemTablePanel itemTablePanel, boolean projectNameVisible, ScrinchEnvToolkit toolkit) {
        this.itemTablePanel = itemTablePanel;
        this.projectNameVisible = projectNameVisible;
        this.toolkit = toolkit;
    }

    protected void releaseAllResources() {
        for (ItemPanel panel : itemPanelList) {
            panel.releaseAllResources();
        }
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return getTableCellComponent(value, row, column);
    }

    public ItemPanel getItemPanel(Item item) {
        return itemPanelMap.get(item);
    }

    public ItemPanel getItemPanel(int row) {
        return itemPanelIndexedMap.get(new Integer(row));
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return getTableCellComponent(value, row, column);
    }

    private Component getTableCellComponent(Object value, int row, int column) {
        Component tableCellComponent = null;
        if (column == 1) {
            if (value == null) {
                tableCellComponent = getItemPanel(row);
            } else {
                tableCellComponent = itemPanelMap.get((Item) value);
                if (tableCellComponent == null) {
                    if (itemTablePanel.getItemSet() instanceof Sprint) {
                        tableCellComponent = new ItemPanel((Sprint) itemTablePanel.getItemSet(), toolkit);
                    } else {
                        tableCellComponent = new ItemPanel(projectNameVisible, toolkit);
                    }
                    ((ItemPanel) tableCellComponent).setItem((Item) value);
                    itemPanelList.add((ItemPanel) tableCellComponent);
                    itemPanelMap.put((Item) value, (ItemPanel) tableCellComponent);
                    itemPanelIndexedMap.put(new Integer(row), (ItemPanel) tableCellComponent);
                }
            }
            if (tableCellComponent != null) {
                tableCellComponent.setBackground(row % 2 == 0 ? BGCOLOR_1 : BGCOLOR_2);
                ((ItemPanel) tableCellComponent).setTitleVisible(row == 0);
            }
        }
        return tableCellComponent;
    }

    public void setTargetVisible(boolean targetVisible) {
        for (ItemPanel itemPanel : itemPanelMap.values()) {
            itemPanel.setTargetVisible(targetVisible);
        }
        this.itemTablePanel.refreshTable();
    }

    public void addCellEditorListener(CellEditorListener l) {
        listeners.add(l);
    }

    public void cancelCellEditing() {
    }

    public Object getCellEditorValue() {
        return null;
    }

    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    public void removeCellEditorListener(CellEditorListener l) {
        listeners.remove(l);
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    public boolean stopCellEditing() {
        return true;
    }
}
