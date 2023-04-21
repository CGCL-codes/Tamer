package com.danikenan.p4jb.gui;

import java.awt.Component;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import gnu.regexp.RE;
import com.danikenan.p4jb.svr.BasicCmdHandler;
import com.danikenan.p4jb.svr.PerforceServer;
import com.danikenan.p4jb.svr.cmd.P4Cmd;
import com.danikenan.p4jb.svr.cmd.P4ConnExcetion;
import com.danikenan.p4jb.svr.cmd.P4GlobalParams;
import com.danikenan.p4jb.util.Util;

/**
 * Description of the Class
 *
 * @author   dani kenan
 */
public class DepotFolderTreeNode implements MutableTreeNode {

    /** Description of the Field */
    private static final RE FOLDER_REGEX = Util.createRegex("/([^/]+)$");

    /** Description of the Field */
    private Vector _children = null;

    /** Description of the Field */
    private String _name;

    /** Description of the Field */
    private Component _cursorComponent;

    /** Description of the Field */
    private P4GlobalParams _p4globalParams;

    /** Description of the Field */
    private P4Cmd _subdirsCmd = new P4Cmd("dirs", new BasicCmdHandler() {

        protected RE getRegex() {
            return FOLDER_REGEX;
        }
    }, true);

    /** Description of the Field */
    private DepotFolderTreeNode _parent;

    /** Description of the Field */
    private boolean _active;

    /**
	 * Constructor for the DepotFolderNode object
	 *
	 * @param parent  Description of Parameter
	 * @param name    Description of Parameter
	 * @param comp    Description of Parameter
	 * @param params  Description of Parameter
	 */
    public DepotFolderTreeNode(DepotFolderTreeNode parent, String name, Component comp, P4GlobalParams params) {
        _name = name;
        _parent = parent;
        _cursorComponent = comp;
        _p4globalParams = params;
    }

    /**
	 * Sets the userObject attribute of the DepotFolderNode object
	 *
	 * @param object  The new userObject value
	 */
    public void setUserObject(Object object) {
        throw new java.lang.UnsupportedOperationException("Method setUserObject() not yet implemented.");
    }

    /**
	 * Sets the parent attribute of the DepotFolderNode object
	 *
	 * @param newParent  The new parent value
	 */
    public void setParent(MutableTreeNode newParent) {
        throw new java.lang.UnsupportedOperationException("Method setParent() not yet implemented.");
    }

    /**
	 * Sets the active attribute of the DepotFolderNode object
	 *
	 * @param active  The new active value
	 */
    public void setActive(boolean active) {
        _active = active;
    }

    /**
	 * Gets the childAt attribute of the DepotFolderNode object
	 *
	 * @param childIndex  Description of Parameter
	 * @return            The childAt value
	 */
    public TreeNode getChildAt(int childIndex) {
        debug("getChildAt");
        return (TreeNode) getChildren().get(childIndex);
    }

    /**
	 * Gets the childCount attribute of the DepotFolderNode object
	 *
	 * @return   The childCount value
	 */
    public int getChildCount() {
        debug("getChildCount");
        return getChildren().size();
    }

    /**
	 * Gets the parent attribute of the DepotFolderNode object
	 *
	 * @return   The parent value
	 */
    public TreeNode getParent() {
        throw new java.lang.UnsupportedOperationException("Method getParent() not yet implemented.");
    }

    /**
	 * Gets the index attribute of the DepotFolderNode object
	 *
	 * @param node  Description of Parameter
	 * @return      The index value
	 */
    public int getIndex(TreeNode node) {
        debug("getIndex");
        return getChildren().indexOf(node);
    }

    /**
	 * Gets the allowsChildren attribute of the DepotFolderNode object
	 *
	 * @return   The allowsChildren value
	 */
    public boolean getAllowsChildren() {
        debug("getAllowsChildren");
        return true;
    }

    /**
	 * Gets the leaf attribute of the DepotFolderNode object
	 *
	 * @return   The leaf value
	 */
    public boolean isLeaf() {
        debug("isLeaf");
        return false;
    }

    /**
	 * Description of the Method
	 *
	 * @return   Description of the Returned Value
	 */
    public String toString() {
        return _name;
    }

    /**
	 * Description of the Method
	 *
	 * @param child  Description of Parameter
	 * @param index  Description of Parameter
	 */
    public void insert(MutableTreeNode child, int index) {
        throw new java.lang.UnsupportedOperationException("Method insert() not yet implemented.");
    }

    /**
	 * Description of the Method
	 *
	 * @param index  Description of Parameter
	 */
    public void remove(int index) {
        throw new java.lang.UnsupportedOperationException("Method remove() not yet implemented.");
    }

    /**
	 * Description of the Method
	 *
	 * @param node  Description of Parameter
	 */
    public void remove(MutableTreeNode node) {
        throw new java.lang.UnsupportedOperationException("Method remove() not yet implemented.");
    }

    /** Description of the Method */
    public void removeFromParent() {
        throw new java.lang.UnsupportedOperationException("Method removeFromParent() not yet implemented.");
    }

    /**
	 * Description of the Method
	 *
	 * @return   Description of the Returned Value
	 */
    public Enumeration children() {
        debug("children");
        return getChildren().elements();
    }

    /**
	 * Gets the fullDepotPath attribute of the DepotFolderNode object
	 *
	 * @return   The fullDepotPath value
	 */
    protected String getFullDepotPath() {
        return _parent.getFullDepotPath() + "/" + _name;
    }

    /**
	 * Description of the Method
	 *
	 * @return   Description of the Returned Value
	 */
    protected boolean shouldLoad() {
        return _children == null;
    }

    /**
	 * Gets the children attribute of the DepotFolderNode object
	 *
	 * @return   The children value
	 */
    private Vector getChildren() {
        debug("getChildren");
        loadChildren();
        return _children;
    }

    /** Description of the Method */
    private void loadChildren() {
        debug("loadChildren");
        if (shouldLoad()) {
            debug("loadChildren need to load");
            try {
                String[] pathSet = new String[] { getFullDepotPath() + "/*" };
                String[] dirs = (String[]) _subdirsCmd.run(_p4globalParams.getCmdGlobalPrefix(), pathSet, null, _cursorComponent);
                Util.dump(dirs);
                _children = new Vector();
                for (int i = 0; i < dirs.length; ++i) {
                    _children.add(new DepotFolderTreeNode(this, dirs[i], _cursorComponent, _p4globalParams));
                }
            } catch (P4ConnExcetion e) {
                PerforceServer.showP4ConnExcetion(e);
                _children = null;
            } catch (Exception e) {
                e.printStackTrace();
                _children = null;
            }
        }
    }

    /**
	 * Description of the Method
	 *
	 * @param s  Description of Parameter
	 */
    private void debug(String s) {
        Util.info(getFullDepotPath() + " > " + s);
    }
}
