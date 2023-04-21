package de.grogra.pf.ui.swing;

import javax.swing.*;
import de.grogra.pf.ui.ComponentWrapper;
import de.grogra.pf.ui.tree.*;
import de.grogra.pf.ui.awt.*;

abstract class MenuModelBase extends MappedComponentModel {

    static final String WRAPPER = "de.grogra.pf.ui.swing.WRAPPER";

    MenuModelBase(UITree sourceTree) {
        super(sourceTree);
    }

    public void disposeNode(Object targetNode) {
        ButtonSupport b = ButtonSupport.get(targetNode);
        if (b != null) {
            b.dispose();
        }
        ComponentWrapper w = (ComponentWrapper) ((JComponent) targetNode).getClientProperty(WRAPPER);
        if (w != null) {
            w.dispose();
        }
    }

    public void valueForPathChanged(javax.swing.tree.TreePath path, Object newValue) {
        JComponent c = (JComponent) path.getLastPathComponent();
        c.putClientProperty(SwingToolkit.SOURCE, newValue);
        ButtonSupport b = ButtonSupport.get(c);
        if (b != null) {
            b.updateState(newValue);
        }
    }

    public boolean isImage(Object sourceNode, Object targetNode) {
        return sourceTree.nodesEqual(sourceNode, SwingToolkit.getSource(targetNode));
    }
}
