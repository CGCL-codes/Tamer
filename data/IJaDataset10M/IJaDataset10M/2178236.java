package fr.itris.glips.rtdaeditor.dbeditor;

import org.w3c.dom.*;
import javax.swing.tree.*;
import javax.swing.*;

/**
 * the model for the widget data base 
 * 
 * @author ITRIS, Jordi SUC
 */
public class InsertedWidgetDataBaseModel extends DefaultTreeModel implements TreeModel {

    /**
	 * the tree corresponding to the model
	 */
    private JTree tree = null;

    /**
     * the root node of the JTree
     */
    private InsertedWidgetDataBaseEditorTreeNode rootNode = null;

    /**
     * the constructor of the class
     * @param dbEditor the database editor
     * @param tree the tree corresponding to the model
     * @param rootModel the root element of the subtree of the widget data base
     */
    public InsertedWidgetDataBaseModel(DataBaseEditorModule dbEditor, JTree tree, Element rootModel) {
        super(null);
        this.tree = tree;
        if (rootModel != null) {
            rootNode = new InsertedWidgetDataBaseEditorTreeNode(dbEditor, this, rootModel);
            setRoot(rootNode);
        }
    }

    /**
     * disposes the model
     */
    public void dispose() {
        rootNode.dispose();
    }

    /**
     * @return Returns the tree.
     */
    public JTree getTree() {
        return tree;
    }
}
