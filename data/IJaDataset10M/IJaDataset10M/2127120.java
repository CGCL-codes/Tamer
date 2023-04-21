package com.hardcode.gdbms.gui;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;

/**
 * DOCUMENT ME!
 *
 * @author Fernando Gonz�lez Cort�s
 */
public class ParseTreeFrame extends JFrame {

    private javax.swing.JPanel jContentPane = null;

    private JScrollPane jScrollPane = null;

    private JTree tree = null;

    /**
	 * This is the default constructor
	 */
    public ParseTreeFrame() {
        super();
        initialize();
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param tm DOCUMENT ME!
	 */
    public void setTreeModel(TreeModel tm) {
        getTree().setModel(tm);
    }

    /**
	 * This method initializes this
	 */
    private void initialize() {
        this.setSize(300, 200);
        this.setContentPane(getJContentPane());
        this.setTitle("JFrame");
    }

    /**
	 * This method initializes jContentPane
	 *
	 * @return javax.swing.JPanel
	 */
    private javax.swing.JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new javax.swing.JPanel();
            jContentPane.setLayout(new java.awt.BorderLayout());
            jContentPane.add(getJScrollPane(), java.awt.BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
	 * This method initializes jScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getTree());
        }
        return jScrollPane;
    }

    /**
	 * This method initializes tree
	 *
	 * @return javax.swing.JTree
	 */
    private JTree getTree() {
        if (tree == null) {
            tree = new JTree();
        }
        return tree;
    }
}
