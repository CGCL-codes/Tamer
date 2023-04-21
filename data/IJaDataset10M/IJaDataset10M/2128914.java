package org.tide.gui.tree;

import org.tidelaget.core.*;
import org.tidelaget.gui.*;
import org.tidelaget.gui.tree.*;
import org.tide.gui.panel.*;
import org.tide.gui.*;
import org.tidelaget.gui.panel.*;
import org.tide.tomcataccess.*;
import javax.swing.*;
import java.io.*;

public class ContextNode extends TIDENode implements IfcDirectoryNode, IfcTomcatNode {

    public ContextNode(Object obj) {
        super(obj);
        addDroppable("org.tidelaget.gui.tree.DirNode");
        addDroppable("org.tidelaget.gui.tree.FileNode");
        addDroppable("org.tide.gui.tree.TIDERoleNode");
    }

    public String toString() {
        return ((TomcatContext) getUserObject()).getName();
    }

    public TIDEPanel getPanel() {
        return new ContextPanel(toString(), getUserObject(), (TomcatContext) getUserObject());
    }

    public boolean delete(boolean ask) {
        if (!super.delete(ask)) return false;
        TIDEPanel p = TIDEController.get().getPanel(getUserObject());
        if (p != null) {
            p.setAltered(false);
            TIDEController.get().closePanel(p);
        }
        return true;
    }

    public ImageIcon getIcon(boolean expanded) {
        return m_contextIcon;
    }

    public File getDirectory() {
        return new File(((TomcatContext) getUserObject()).getPath());
    }

    public TomcatContext getContext() {
        return (TomcatContext) getUserObject();
    }

    public boolean isSecurable() {
        return false;
    }
}
