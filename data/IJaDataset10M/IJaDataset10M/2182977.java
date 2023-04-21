package com.itextpdf.rups.view.itext;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import com.itextpdf.rups.model.XfaFile;
import com.itextpdf.rups.view.icons.IconTreeCellRenderer;
import com.itextpdf.rups.view.icons.IconTreeNode;
import com.itextpdf.rups.view.itext.treenodes.XdpTreeNode;

/**
 * Tree that visualizes the XFA information.
 */
public class XfaTree extends JTree {

    /**
	 * Constructs an XfaTree.
	 */
    public XfaTree() {
        super();
    }

    public void clear() {
        setCellRenderer(new IconTreeCellRenderer());
        setModel(new DefaultTreeModel(new IconTreeNode("xfa.png")));
    }

    public void load(XfaFile file) {
        setCellRenderer(new IconTreeCellRenderer());
        setModel(new DefaultTreeModel(new XdpTreeNode(file.getXfaDocument())));
    }

    /** A Serial Version UID. */
    private static final long serialVersionUID = -5072971223015095193L;
}
