package com.desktopdeveloper.pendulum.components.tree;

import com.desktopdeveloper.pendulum.core.factory.ObjectFactory;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EventObject;

/**
 * Created by IntelliJ IDEA.
 * User: kent_p
 * Date: 19-Oct-2004
 * Time: 15:19:43
 * To change this template use File | Settings | File Templates.
 */
public class PCheckTreeCellEditor extends DefaultTreeCellEditor {

    private ObjectFactory factory;

    private DefaultMutableTreeNode node;

    private PCheckTreeItem itemP;

    private JCheckBox checkBox;

    public PCheckTreeCellEditor() {
        super(null, null);
    }

    public PCheckTreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer) {
        super(tree, renderer);
    }

    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
        node = ((DefaultMutableTreeNode) value);
        itemP = (PCheckTreeItem) node.getUserObject();
        String description = itemP.getDescription();
        Object actualValue = itemP.getValue();
        Boolean b = (Boolean) actualValue;
        boolean booleanValue = b.booleanValue();
        checkBox = (JCheckBox) factory.control(JCheckBox.class).setProperty("selected", booleanValue).setProperty("text", description).setProperty("opaque", false).create();
        checkBox.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                itemP.setValue(new Boolean(checkBox.isSelected()));
            }
        });
        return checkBox;
    }

    public Object getCellEditorValue() {
        return node;
    }

    public void setFactory(ObjectFactory objectFactory) {
        this.factory = objectFactory;
    }

    public boolean isCellEditable(EventObject event) {
        return true;
    }

    public void cancelCellEditing() {
        super.cancelCellEditing();
    }
}
