package edu.iicm.hvs.data;

import javax.swing.tree.*;
import java.util.*;
import java.util.logging.Logger;
import javax.naming.*;
import javax.naming.directory.*;

/**
 * This class represents a node in the namespace/directory.
 * It is modified from DynamicTreeNode in Swing's SampleTreeexample.
 *
 * Each HVSTreeNode has a user object that is of class HVSDirData.
 * <BR><BR>
 * This class is based on the JNDI Browser example from Sun by Rosanna Lee.
 *
 * @author Peter Scheir
 */
public class HVSTreeNode extends DefaultMutableTreeNode {

    /** The logging class. */
    private static Logger logger_ = Logger.getLogger("edu.iicm.hvs.data.HVSTreeNode");

    /** A flag that shows if this node has loaded it's children. */
    boolean has_loaded_ = false;

    /** The initial context for JNDI. */
    private Context initial_context_ = null;

    /** A flag the shows if the inital context was allready fetched. */
    private boolean initial_context_gotten_ = false;

    /**
   * Constructs a new HVSTreeNode instance with user_obj as the user
   * object.
   *
   * param user_obj The Object which should be stored in the node.

  */
    public HVSTreeNode(Object user_obj) {
        super(user_obj);
    }

    /**
   * Determines whether this node is a leaf node.
   * A node is a leaf if it is not a context.
   * Overriding this seems to affect the display.
   * When not overridden, empty contexts are displayed as "dots".
   * When overridden as follows, empty contexts are displayed as folders.
   *
   * @return TRUE if the node is a leaf node.
   */
    public boolean isLeaf() {
        return !((HVSDirData) getUserObject()).isContext();
    }

    /**
   * Determines whether this node can have children.
   * Only contexts can have children.
   *
   * @return TRUE if the node can have children.
   */
    public boolean getAllowsChildren() {
        return ((HVSDirData) getUserObject()).isContext();
    }

    /**
   * Loads the children of this node and returns the number of children.
   *
   * If the children have not been loaded yet, use loadChildren()
   * to load the children before returning the number of children.
   *
   * @return The number of children of this node.
   */
    public int getChildCount() {
        logger_.info("->getchildcount: " + this.toString());
        loadChildren(false);
        return super.getChildCount();
    }

    /**
   * Helper to get the context of the parent node.
   * If the parent is the root node the parent context is the 
   * initial context. <BR>
   * All naming operations are relative to a context.
   *
   * @param node The Node to get the parent context.
   * @return The context of the parent node.
   * 
   * @see javax.naming.Context
   */
    private Context getParentContext(HVSTreeNode node) throws NamingException {
        HVSTreeNode parent = (HVSTreeNode) node.getParent();
        Context parent_ctx = null;
        if (parent != null) {
            parent_ctx = (Context) ((HVSDirData) parent.getUserObject()).getObject();
        }
        return parent_ctx;
    }

    /**
   * Loads children from the naming/directory service by using list().
   *
   * This method is invoked the first time getChildCount() is called.
   * The list is then cached, so there might be slight inconsistencies
   * between the actual state of the namespace/directory and what is
   * shown.
   * If higher level of consistency is desired, then the list should
   * be refreshed (each time getChildCount()) is called but this
   * can be prohibitively costly.
   *
   * @param force Load children of this node even if the have
   * allready been loaded.
   * @return TRUE if the children have been loaded problemlessly.
   */
    boolean loadChildren(boolean force) {
        logger_.info("loadChildren: force= " + force + " / has_loaded_= " + has_loaded_);
        if (!force && has_loaded_) {
            return true;
        }
        HVSDirData dirData = (HVSDirData) getUserObject();
        String name = dirData.getName();
        Object obj = dirData.getObject();
        logger_.finer("->load children: " + name);
        try {
            if (obj == null) {
                try {
                    Context parent_ctx = getParentContext(HVSTreeNode.this);
                    logger_.finer("load children: " + name);
                    obj = parent_ctx.lookup(name);
                } catch (NamingException e) {
                    logger_.severe(e.getMessage());
                    return has_loaded_ = false;
                }
                dirData.setObject(obj);
                dirData.resetContextFlag();
            }
            if (!(obj instanceof Context)) {
                return has_loaded_ = false;
            }
            HVSDirData child_data;
            int counter = 0;
            if (force) {
                removeAllChildren();
            }
            Context ctx = (Context) obj;
            try {
                NamingEnumeration nl = ctx.list("");
                while (nl.hasMoreElements()) {
                    NameClassPair nc = (NameClassPair) nl.next();
                    child_data = new HVSDirData(nc.getName(), nc.getClassName());
                    HVSTreeNode childNode = new HVSTreeNode(child_data);
                    insert(childNode, counter++);
                }
                return has_loaded_ = true;
            } catch (NamingException e) {
                logger_.severe(e.getMessage());
                return has_loaded_ = false;
            }
        } finally {
        }
    }
}
