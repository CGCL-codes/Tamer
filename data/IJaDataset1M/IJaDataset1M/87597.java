package org.nightlabs.jfire.base.ui.jdo.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.jdo.JDOHelper;
import org.apache.log4j.Logger;

/**
 * 
 * @author Marco Schulze
 */
public class JDOObjectTreeNode<JDOObjectID, JDOObject, Controller extends ActiveJDOObjectTreeController> {

    private static final Logger logger = Logger.getLogger(JDOObjectTreeNode.class);

    private Controller activeJDOObjectTreeController;

    private JDOObjectTreeNode parent;

    private JDOObject jdoObject;

    public void setActiveJDOObjectTreeController(Controller activeJDOObjectTreeController) {
        this.activeJDOObjectTreeController = activeJDOObjectTreeController;
    }

    public void setParent(JDOObjectTreeNode parent) {
        this.parent = parent;
    }

    public void setJdoObject(JDOObject jdoObject) {
        this.jdoObject = jdoObject;
    }

    public Controller getActiveJDOObjectTreeController() {
        return activeJDOObjectTreeController;
    }

    public JDOObjectTreeNode getParent() {
        return parent;
    }

    public JDOObject getJdoObject() {
        return jdoObject;
    }

    private List<JDOObjectTreeNode> childNodes = null;

    private List<JDOObjectTreeNode> childNodes_ro = null;

    /**
	 * @return a read-only copy of the child-nodes of this node or <code>null</code>, if there are no child-nodes assigned (i.e. {@link #setChildNodes(List)} has never been called).
	 */
    public synchronized List<JDOObjectTreeNode> getChildNodes() {
        if (childNodes == null) return null;
        if (childNodes_ro == null) childNodes_ro = Collections.unmodifiableList(new ArrayList<JDOObjectTreeNode>(childNodes));
        return childNodes_ro;
    }

    public synchronized void setChildNodes(List<JDOObjectTreeNode> childNodes) {
        this.childNodes = childNodes;
        this.childNodes_ro = null;
        if (logger.isDebugEnabled()) logger.debug("setChildNodes: childNodes.size()=\"" + (childNodes == null ? null : childNodes.size()) + "\" this.jdoObjectID=\"" + JDOHelper.getObjectId(this.jdoObject) + "\"");
    }

    /**
	 * @param childNode The child-node to be added.
	 * @return <code>true</code>, if the child-node has been added - i.e. {@link #setChildNodes(List)} has already been
	 *		called and a List of child-nodes assigned. <code>false</code>, if the <code>List</code> of child-nodes does not
	 *		yet exist (is <code>null</code>) and the child-node could therefore not be added.
	 */
    public synchronized boolean addChildNode(JDOObjectTreeNode childNode) {
        if (childNodes == null) {
            if (logger.isDebugEnabled()) logger.debug("addChildNode: this.childNodes=null => do nothing + return false! childNode.jdoObjectID=\"" + JDOHelper.getObjectId(childNode.getJdoObject()) + "\" this.jdoObjectID=\"" + JDOHelper.getObjectId(this.jdoObject) + "\"");
            return false;
        }
        childNodes.add(childNode);
        this.childNodes_ro = null;
        if (logger.isDebugEnabled()) logger.debug("addChildNode: added childNode! childNode.jdoObjectID=\"" + JDOHelper.getObjectId(childNode.getJdoObject()) + "\" this.jdoObjectID=\"" + JDOHelper.getObjectId(this.jdoObject) + "\"");
        return true;
    }

    /**
	 * @param childNode The child-node to be removed.
	 * @return <code>true</code>, if the child-node has been removed - i.e. {@link #setChildNodes(List)} has already been
	 *		called and a List of child-nodes assigned <b>and</b> the given <code>childNode</code> did exist in this
	 *		<code>List</code>. <code>false</code>, if the <code>List</code> of child-nodes does not
	 *		yet exist (is <code>null</code>) or the child-node was not contained in it.
	 */
    public synchronized boolean removeChildNode(JDOObjectTreeNode childNode) {
        if (childNodes == null) {
            if (logger.isDebugEnabled()) logger.debug("removeChildNode: this.childNodes=null => do nothing + return false! childNode.jdoObjectID=\"" + JDOHelper.getObjectId(childNode.getJdoObject()) + "\" this.jdoObjectID=\"" + JDOHelper.getObjectId(this.jdoObject) + "\"");
            return false;
        }
        boolean res = childNodes.remove(childNode);
        this.childNodes_ro = null;
        if (logger.isDebugEnabled()) logger.debug("removeChildNode: removed childNode (return " + res + ")! childNode.jdoObjectID=\"" + JDOHelper.getObjectId(childNode.getJdoObject()) + "\" this.jdoObjectID=\"" + JDOHelper.getObjectId(this.jdoObject) + "\"");
        return res;
    }
}
