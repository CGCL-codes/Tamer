package org.tide.gui.tree;

import org.tide.tomcataccess.*;
import org.tidelaget.*;
import org.tidelaget.core.*;
import org.tidelaget.gui.*;
import org.tidelaget.gui.tree.*;
import org.tidelaget.gui.panel.*;
import org.tidelaget.gui.tree.listener.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.io.*;
import java.util.*;

public class TIDESecurityConstraintsNode extends TIDENode {

    protected static ImageIcon m_rolesIcon = new ImageIcon(ClassLoader.getSystemResource("icons/node_secconstraints.gif"));

    public TIDESecurityConstraintsNode(Object obj) {
        super(obj);
        addDroppable("org.tide.gui.tree.TIDERoleNode");
        addDroppable("org.tidelaget.gui.tree.FileNode");
        addDroppable("org.tidelaget.gui.tree.DirNode");
    }

    /**
	 * Returns the expanded/collapsed icon for this node.
	 * Override this method in subclasses to invoke correct behaviour.
	 */
    public ImageIcon getIcon(boolean expanded) {
        return m_rolesIcon;
    }

    /**
	 * Deletes this node from the tree.
	 */
    public boolean delete(boolean ask) {
        return false;
    }

    /**
 	 * Copy this node to another place in tree. Returns the cloned node.
 	 */
    public TIDENode[] copy(JTree destTree, TreePath destPath) {
        return null;
    }

    /**
 	 * Move this node to another place in tree. Returns the cloned nodes.
 	 */
    public TIDENode[] move(JTree destTree, TreePath destPath) {
        return null;
    }
}
