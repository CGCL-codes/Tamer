package com.gwtext.client.widgets.tree;

import com.google.gwt.core.client.JavaScriptObject;
import com.gwtext.client.core.NameValuePair;

/** 
 * This class is used to represent the Node UI of a ColumnTree
 * widget.
 * @author mlim1972
 *
 */
public class ColumnNodeUI extends TreeNodeUI {

    public ColumnNodeUI(JavaScriptObject jsObj) {
        super(jsObj);
    }

    /**
	 * Creates an instance of ColumnNodeUI
	 * @return
	 */
    public static ColumnNodeUI instance() {
        return new ColumnNodeUI(create());
    }

    private static ColumnNodeUI instance(JavaScriptObject jsObj) {
        return new ColumnNodeUI(jsObj);
    }

    public native JavaScriptObject create(JavaScriptObject config);

    protected static native JavaScriptObject create();

    /**
     * returns the appropriate JavaScriptObject to the UI provider
     * used in a ColumnTee
     * @return
     */
    public static native JavaScriptObject getUiProvider();

    /**
     * This metod returns the TreeNode representation of a particular
     * ColumnTree Node.
     * @param values NameValuePair of the columns for the ColumnTree
     * 
     * @return the TreeNode representation based on the columns passed
     */
    public static TreeNode getNewTreeNode(NameValuePair[] values) {
        ColumnTreeNode newTreeNode = new ColumnTreeNode();
        for (int i = 0; i < values.length; i++) {
            if (values[i].getType() == NameValuePair.BOOLEAN) {
                newTreeNode.setColumnValue(values[i].getName(), values[i].getValueAsBoolean());
            } else if (values[i].getType() == NameValuePair.STRING) {
                newTreeNode.setColumnValue(values[i].getName(), values[i].getValue());
            } else if (values[i].getType() == NameValuePair.FLOAT) {
                newTreeNode.setColumnValue(values[i].getName(), values[i].getValueAsFloat());
            } else if (values[i].getType() == NameValuePair.DATE) {
                newTreeNode.setColumnValue(values[i].getName(), values[i].getValueAsDate());
            } else if (values[i].getType() == NameValuePair.INT) {
                newTreeNode.setColumnValue(values[i].getName(), values[i].getValueAsInt());
            }
        }
        newTreeNode.setUiProvider(getNewTreeUiProvider());
        return newTreeNode;
    }

    /**
     * Returns the AsyncTreeNode based on the columns that are part of the 
     * ColumnTreeNode
     * @param values The NameValuePair for the column tree 
     * @return the generated AsynTreeNode representing the columns passed
     */
    public static AsyncTreeNode getNewAsyncTreeNode(NameValuePair[] values) {
        ColumnAsyncTreeNode newTreeNode = new ColumnAsyncTreeNode();
        for (int i = 0; i < values.length; i++) {
            if (values[i].getType() == NameValuePair.BOOLEAN) {
                newTreeNode.setColumnValue(values[i].getName(), values[i].getValueAsBoolean());
            } else if (values[i].getType() == NameValuePair.STRING) {
                newTreeNode.setColumnValue(values[i].getName(), values[i].getValue());
            } else if (values[i].getType() == NameValuePair.FLOAT) {
                newTreeNode.setColumnValue(values[i].getName(), values[i].getValueAsFloat());
            } else if (values[i].getType() == NameValuePair.DATE) {
                newTreeNode.setColumnValue(values[i].getName(), values[i].getValueAsDate());
            } else if (values[i].getType() == NameValuePair.INT) {
                newTreeNode.setColumnValue(values[i].getName(), values[i].getValueAsInt());
            }
        }
        newTreeNode.setUiProvider(getNewTreeUiProvider());
        return newTreeNode;
    }

    private static native JavaScriptObject getNewTreeUiProvider();
}
