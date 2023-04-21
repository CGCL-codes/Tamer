package org.mitre.rt.client.ui.requiredcomponents;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.mitre.rt.client.util.GlobalUITools;

public class RequiredTableListener extends RequiredComponent implements TableModelListener {

    private Map<TableModel, JTable> mapModel = null;

    public RequiredTableListener() {
        super.mapParent = new HashMap<JComponent, JComponent>();
        super.mapBorder = new HashMap<JComponent, Border>();
        super.mapBadValue = new HashMap<JComponent, Object>();
        this.mapModel = new HashMap<TableModel, JTable>();
    }

    @Override
    public void registerComponent(JComponent jtable, JComponent container) {
        JTable table = (JTable) jtable;
        TableModel model = table.getModel();
        if (super.mapParent.containsKey(table) == false) {
            super.mapParent.put(table, container);
            super.mapBadValue.put(table, 0);
            if (container == null) super.mapBorder.put(table, table.getBorder()); else super.mapBorder.put(table, container.getBorder());
            this.determineBorder(table, container);
            this.mapModel.put(model, table);
            model.addTableModelListener(this);
        }
    }

    @Override
    public void unregisterComponent(JComponent component) {
        JTable table = (JTable) component;
        if (super.mapParent.containsKey(table)) {
            TableModel model = table.getModel();
            JComponent container = super.mapParent.get(table);
            Border defaultBorder = null;
            if (container == null) container = table;
            defaultBorder = super.mapBorder.get(table);
            if (defaultBorder != null) container.setBorder(defaultBorder);
            super.mapBorder.remove(table);
            super.mapParent.remove(table);
            this.mapModel.remove(model);
            super.mapBadValue.remove(table);
            model.removeTableModelListener(this);
        }
    }

    private void setComponentBorder(JComponent component, Border border) {
        component.setBorder(border);
    }

    private void determineBorder(JTable table, JComponent parent) {
        Border border = null;
        Integer rowCount = table.getRowCount();
        boolean badValue = rowCount.equals(super.mapBadValue.get(table));
        if (badValue == true) border = new LineBorder(GlobalUITools.REQUIRED_ITEM_COLOR_FG); else border = super.mapBorder.get(table);
        if (parent != null) this.setComponentBorder(parent, border); else this.setComponentBorder(table, border);
    }

    @Override
    public void handleChangeEvent(Object eventObject) {
        if (eventObject instanceof TableModelEvent) {
            TableModelEvent evt = (TableModelEvent) eventObject;
            TableModel model = (TableModel) evt.getSource();
            JTable table = this.mapModel.get(model);
            JComponent parent = super.mapParent.get(table);
            this.determineBorder(table, parent);
        }
    }

    @Override
    public void setBadValue(JComponent component, Object value) throws Exception {
        if (value instanceof Integer) {
            super.mapBadValue.put(component, (Integer) value);
        } else throw new Exception("@value must be an Integer");
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        this.handleChangeEvent(e);
    }
}
