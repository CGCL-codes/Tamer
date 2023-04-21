package org.digitall.apps.personalfiles.interfaces;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import javax.swing.JTree;
import org.digitall.lib.components.grid.GridPanel;

public class DropTargetListenerResourcesTree implements DropTargetListener {

    private JTree tree;

    private GridPanel grilla;

    public DropTargetListenerResourcesTree(JTree _tree, GridPanel _grilla) {
        grilla = _grilla;
        tree = _tree;
    }

    public void dragEnter(DropTargetDragEvent dtde) {
    }

    public void dragOver(DropTargetDragEvent dtde) {
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    public void dragExit(DropTargetEvent dte) {
    }

    public void drop(DropTargetDropEvent dtde) {
        System.out.println("solto al arbol...");
        System.out.println("seleccionado del arbol: " + tree.getPathForRow(tree.getLeadSelectionRow()));
    }
}
