package com.emental.mindraider.outline.tt;

import com.emental.mindraider.outline.NotebookOutlineDirectory;

/**
 * BookmarksModel is a TreeTableModel extending from DynamicTreeTableModel. The
 * only functionality it adds is overriding <code>isCellEditable</code> to
 * return a different value based on the type of node passed in. Specifically,
 * the root node is not editable, at all.
 * 
 * @author Scott Violet
 */
public class BookmarksModel extends DynamicTreeTableModel {

    /**
     * Names of the columns.
     */
    public static final String[] columnNames = { "Label", "Annotation", "Created" };

    /**
     * Method names used to access the data to display.
     */
    private static final String[] methodNames = { "getLabel", "getAnnotation", "getCreated" };

    /**
     * Method names used to set the data.
     */
    private static final String[] setterMethodNames = { "setLabel", "setAnnotation", "setCreated" };

    /**
     * Classes presenting the data.
     */
    private static final Class[] classes = { TreeTableModel.class, String.class, String.class };

    public BookmarksModel(NotebookOutlineDirectory root) {
        super(root, columnNames, methodNames, setterMethodNames, classes);
    }

    /**
     * <code>isCellEditable</code> is invoked by the JTreeTable to determine
     * if a particular entry can be added. This is overridden to return true for
     * the first column, assuming the node isn't the root, as well as returning
     * two for the second column if the node is a BookmarkEntry. For all other
     * columns this returns false.
     */
    public boolean isCellEditable(Object node, int column) {
        switch(column) {
            case 0:
                return (node != getRoot());
            case 1:
                return false;
            default:
                return false;
        }
    }
}
