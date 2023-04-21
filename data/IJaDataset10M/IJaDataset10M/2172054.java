package com.bluebrim.browser.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.tree.TreeCellEditor;
import com.bluebrim.browser.shared.CoCatalogElementIF;
import com.bluebrim.gui.client.CoPanelTreeCellRenderer;
import com.bluebrim.resource.shared.CoResourceLoader;

/**
 * 
 */
public abstract class CoCheckedCatalogTreeCellRenderer extends CoPanelTreeCellRenderer implements TreeCellEditor {

    protected EventListenerList listenerList = new EventListenerList();

    protected JLabel label;

    protected JCheckBox checkBox;

    protected int editedRow;

    protected Object editedObject;

    protected Color highlightBackground = new Color(255, 255, 0);

    protected Color highlightForeground = Color.black;

    /**
 */
    protected CoCheckedCatalogTreeCellRenderer() {
        super();
        buildRenderer();
    }

    public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }

    /**
 */
    private void buildRenderer() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        createWidgets();
        add(Box.createHorizontalGlue());
    }

    public void cancelCellEditing() {
        editedObject = null;
        editedRow = -1;
        fireEditingCanceled();
    }

    /**
 */
    protected abstract boolean checkRow(Object value, int row);

    /**
 */
    protected JCheckBox createCheckBox() {
        checkBox = new JCheckBox("");
        checkBox.setAlignmentY(Component.CENTER_ALIGNMENT);
        checkBox.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (editedObject != null) selectionHasChanged(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        return checkBox;
    }

    /**
 */
    protected JLabel createLabel() {
        label = new JLabel("     ");
        label.setAlignmentY(Component.CENTER_ALIGNMENT);
        return label;
    }

    /**
 */
    protected void createWidgets() {
        add(createCheckBox());
        add(createLabel());
    }

    /**
 */
    protected abstract boolean enableCheckBox(Object value, int row);

    protected void fireEditingCanceled() {
        ChangeEvent tChangeEvent = null;
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                if (tChangeEvent == null) tChangeEvent = new ChangeEvent(this);
                ((CellEditorListener) listeners[i + 1]).editingCanceled(tChangeEvent);
            }
        }
    }

    protected void fireEditingStopped() {
        ChangeEvent tChangeEvent = null;
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                if (tChangeEvent == null) tChangeEvent = new ChangeEvent(this);
                ((CellEditorListener) listeners[i + 1]).editingStopped(tChangeEvent);
            }
        }
    }

    public Object getCellEditorValue() {
        return new Boolean(checkBox.isSelected());
    }

    public int getClickCountToStart() {
        return 1;
    }

    public Component getComponent() {
        return this;
    }

    public Dimension getPreferredSize() {
        Dimension tPrefSize = super.getPreferredSize();
        return tPrefSize;
    }

    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
        Component tEditor = getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, hasFocus);
        editedObject = value;
        editedRow = row;
        selected = isSelected || (getClickCountToStart() == 1);
        return tEditor;
    }

    /**
 * Som default visas objekt som implementerar CoTreeCatalogElementIF
 * upp med sin ikon och sin identitet. Jag s�rbehandlar inte
 * element som �r expanderade eller l�v.
 */
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        editedObject = null;
        editedRow = -1;
        setValue(value, row, selected, expanded, leaf, hasFocus);
        selectAndEnableCheckBox(value, tree.isEnabled(), row);
        label.setEnabled(tree.isEnabled());
        return this;
    }

    public boolean isCellEditable(EventObject event) {
        if (event == null || ((event instanceof MouseEvent) && ((MouseEvent) event).getClickCount() >= getClickCountToStart())) return true; else return false;
    }

    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }

    /**
 */
    protected void selectAndEnableCheckBox(Object value, boolean treeIsEnabled, int row) {
        checkBox.setSelected(checkRow(value, row));
        checkBox.setEnabled(treeIsEnabled && enableCheckBox(value, row));
    }

    protected abstract void selectionHasChanged(boolean state);

    public void setIcon(Icon icon) {
        label.setIcon(icon);
    }

    public void setText(String text) {
        label.setText(text);
    }

    /**
 * Som default visas objekt som implementerar CoTreeCatalogElementIF
 * upp med sin ikon och sin identitet. Jag s�rbehandlar inte
 * element som �r expanderade eller l�v.
 */
    protected void setValue(Object value, int row, boolean isSelected, boolean expanded, boolean leaf, boolean hasFocus) {
        Color color = getBackgroundColor();
        label.setBackground(color);
        checkBox.setBackground(color);
        if (value instanceof CoCatalogElementIF) {
            CoCatalogElementIF tElement = (CoCatalogElementIF) value;
            setText(tElement.getIdentity());
            String tIconName = (tElement.getSmallIconName());
            if (tIconName != null && tIconName.length() > 0) setIcon(CoResourceLoader.loadIcon(tElement.getIconResourceAnchor(), tIconName));
            label.invalidate();
            invalidate();
            validate();
        }
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        return isCellEditable(anEvent);
    }

    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }
}
