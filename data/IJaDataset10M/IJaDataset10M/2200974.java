package org.gjt.universe.gui.tree;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

public class TVPopup extends JPopupMenu {

    TreeViewer treeViewer = null;

    JTree tree = null;

    TVData data = null;

    MouseEvent e = null;

    TreePath path = null;

    JMenuItem[] defaultItems = null;

    Hashtable extensions = null;

    /********************************************************************
	*
	*	Creates the extendable popup menu for TreeViewer.
	*
	*********************************************************************/
    public TVPopup(TreeViewer treeViewer) {
        super();
        this.treeViewer = treeViewer;
        this.tree = treeViewer.getJTree();
        setInvoker(tree);
        defaultItems = new JMenuItem[4];
        defaultItems[0] = new JMenuItem("Info", KeyEvent.VK_I);
        defaultItems[0].addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onInfo();
            }
        });
        defaultItems[1] = new JMenuItem("Annotate", KeyEvent.VK_A);
        defaultItems[1].addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onAnnotate();
            }
        });
        defaultItems[2] = new JMenuItem("Expand all", KeyEvent.VK_E);
        defaultItems[2].addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onExpand();
            }
        });
        defaultItems[3] = new JMenuItem("Shrink all", KeyEvent.VK_S);
        defaultItems[3].addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onCompress();
            }
        });
    }

    /********************************************************************
	*
	*	Add a menu for a particular type of data. If an extension 
	*	already exists for the type, it will be replaced.
	*
	*
	*********************************************************************/
    public void addExtensibleMenu(String type, TVMenuExtension ext) {
        if (extensions == null) {
            extensions = new Hashtable(5);
        }
        extensions.put(type, ext);
    }

    /********************************************************************
	*
	*	Remove a menu extension for a particular type of data.
	*
	*********************************************************************/
    public void removeExtensibleMenu(String type) {
        extensions.remove(type);
    }

    /********************************************************************
	*
	*	Dipslay the popup menu. Disables/enables commands as required.
	*	Inserts any menu extensions if the TVData is of the proper type.
	*
	*********************************************************************/
    public void show(MouseEvent e, TreePath path, TVData data) {
        this.e = e;
        this.data = data;
        this.path = path;
        boolean toDisplay = true;
        if (data != null) {
            if (data.isLabel()) {
                toDisplay = false;
            }
        } else {
            toDisplay = false;
        }
        removeAll();
        add(defaultItems[0]);
        add(defaultItems[1]);
        defaultItems[0].setEnabled(toDisplay);
        defaultItems[1].setEnabled(toDisplay);
        addSeparator();
        if (toDisplay && (extensions != null)) {
            TVMenuExtension tvme = (TVMenuExtension) extensions.get(data.getType());
            if (tvme != null) {
                JMenuItem extItems[] = tvme.getMenuItems(data);
                for (int i = 0; i < extItems.length; i++) {
                    add(extItems[i]);
                }
                addSeparator();
            }
        }
        add(defaultItems[2]);
        add(defaultItems[3]);
        pack();
        show(tree, e.getX(), e.getY());
    }

    /********************************************************************
	*
	*	Show Information about the object selected.
	*	
	*	<B>Not yet implemented!</B>
	*
	*********************************************************************/
    public void onInfo() {
        JOptionPane.showMessageDialog(null, "Not Implemented", "Error!", JOptionPane.ERROR_MESSAGE);
    }

    /********************************************************************
	*
	*	Completely expand the tree.	
	*
	*********************************************************************/
    public void onExpand() {
        int row = 0;
        while (row < tree.getRowCount()) {
            tree.expandRow(row);
            row++;
        }
    }

    /********************************************************************
	*
	*	Completely compress the tree.
	*
	*********************************************************************/
    public void onCompress() {
        int row = tree.getRowCount() - 1;
        while (row >= 0) {
            tree.collapseRow(row);
            row--;
        }
    }

    /********************************************************************
	*
	*	Allow user to view, edit, or delete a note by bringing up
	*	the note-editing dialog.
	*
	*********************************************************************/
    public void onAnnotate() {
        String note = TVNoteGUI.displayDialog(null, data.getNote());
        data.setNote(note);
        tree.getModel().valueForPathChanged(path, data);
    }
}
