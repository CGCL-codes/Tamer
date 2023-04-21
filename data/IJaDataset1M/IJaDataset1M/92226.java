package org.grapheditor.editor.menus;

import java.awt.Frame;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JCheckBoxMenuItem;
import org.grapheditor.editor.GraphEditorPane;
import org.grapheditor.editor.SplitSelectedEdgesDialog;
import org.grapheditor.editor.listeners.InsertModeChangeListener;

/**
 * This menu is displayed when only edges is selected. It has menu item to
 * paste, delete and split edges etc.
 * 
 * @author kjellw
 * 
 */
public class OnlyEdgesSelectedMenu extends JPopupMenu {

    private JMenuItem deleteItem = null;

    private JSeparator jSeparator = null;

    private JCheckBoxMenuItem insertModeItem = null;

    private GraphEditorPane graphPane;

    private JSeparator jSeparator1 = null;

    private JMenuItem pasteMenuItem = null;

    private JSeparator jSeparator2 = null;

    private JMenuItem jMenuItem = null;

    /**
	 * This method initializes
	 * 
	 */
    public OnlyEdgesSelectedMenu(GraphEditorPane graphPane) {
        super();
        this.graphPane = graphPane;
        initialize();
        graphPane.addInsertModeChangeListener(new InsertModeChangeListener() {

            public void newInsertModeEvent(boolean insertMode) {
                insertModeItem.setSelected(insertMode);
            }
        });
    }

    /**
	 * This method initializes this
	 * 
	 */
    private void initialize() {
        this.add(getPasteMenuItem());
        this.add(getJSeparator1());
        this.add(getDeleteItem());
        this.add(getJSeparator2());
        this.add(getJMenuItem());
        this.add(getJSeparator());
        this.add(getInsertModeItem());
    }

    /**
	 * This method initializes deleteItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getDeleteItem() {
        if (deleteItem == null) {
            deleteItem = new JMenuItem();
            deleteItem.setText("Delete");
            deleteItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    graphPane.removeSelected();
                }
            });
        }
        return deleteItem;
    }

    /**
	 * This method initializes jSeparator
	 * 
	 * @return javax.swing.JSeparator
	 */
    private JSeparator getJSeparator() {
        if (jSeparator == null) {
            jSeparator = new JSeparator();
        }
        return jSeparator;
    }

    /**
	 * This method initializes insertModeItem
	 * 
	 * @return javax.swing.JCheckBoxMenuItem
	 */
    private JCheckBoxMenuItem getInsertModeItem() {
        if (insertModeItem == null) {
            insertModeItem = new JCheckBoxMenuItem();
            insertModeItem.setText("Insert mode");
            insertModeItem.addItemListener(new java.awt.event.ItemListener() {

                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    graphPane.setInsertMode(((JCheckBoxMenuItem) e.getSource()).isSelected());
                }
            });
            insertModeItem.setSelected(true);
        }
        return insertModeItem;
    }

    /**
	 * This method initializes jSeparator1
	 * 
	 * @return javax.swing.JSeparator
	 */
    private JSeparator getJSeparator1() {
        if (jSeparator1 == null) {
            jSeparator1 = new JSeparator();
        }
        return jSeparator1;
    }

    /**
	 * This method initializes pasteMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getPasteMenuItem() {
        if (pasteMenuItem == null) {
            pasteMenuItem = new JMenuItem();
            pasteMenuItem.setText("Paste");
        }
        return pasteMenuItem;
    }

    /**
	 * This method initializes jSeparator2
	 * 
	 * @return javax.swing.JSeparator
	 */
    private JSeparator getJSeparator2() {
        if (jSeparator2 == null) {
            jSeparator2 = new JSeparator();
        }
        return jSeparator2;
    }

    /**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getJMenuItem() {
        if (jMenuItem == null) {
            jMenuItem = new JMenuItem();
            jMenuItem.setText("Subdivide edge(s)");
            jMenuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    SplitSelectedEdgesDialog.show(graphPane);
                }
            });
        }
        return jMenuItem;
    }
}
