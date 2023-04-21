package org.zxframework.misc;

import java.util.Iterator;
import org.zxframework.ZXBO;
import org.zxframework.ZXException;
import org.zxframework.ZXObject;
import org.zxframework.zXType;
import org.zxframework.datasources.DSHRdbms;
import org.zxframework.datasources.DSHandler;
import org.zxframework.datasources.DSRS;
import org.zxframework.datasources.DSWhereClause;
import org.zxframework.property.LongProperty;
import org.zxframework.property.StringProperty;
import org.zxframework.logging.Log;
import org.zxframework.logging.LogFactory;

/**
 * Support routines to deal with recusrive tree.
 * 
 * <pre>
 * 
 * Change    : BD5APR05 - V1.5:1
 * Why       : Added support for data sources
 * </pre>
 * 
 * @author Michael Brewer
 * @author Bertus Dispa
 * @author David Swann
 * 
 * @version 0.0.1
 */
public class RecTree extends ZXObject {

    private static Log log = LogFactory.getLog(RecTree.class);

    /** 
     * Default constructor.
     **/
    public RecTree() {
        super();
    }

    /**
     * Create a new tree from a node of an existing tree.
     * 
     * @param pobjTree The Node to create the new one from.
     * @param pintNode The primary key of the node.
     * @return Returns a detached tree node from a specified one.
     * @throws ZXException Thrown if detachTree fails.
     */
    public RecTreeNode detachTree(RecTreeNode pobjTree, int pintNode) throws ZXException {
        if (getZx().trace.isFrameworkTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pobjTree", pobjTree);
            getZx().trace.traceParam("pintNode", pintNode);
        }
        RecTreeNode detachTree = null;
        try {
            detachTree = pobjTree.findNode(pintNode);
            if (detachTree == null) {
                throw new Exception("Unable to retrieve node to detach");
            }
            if (recalc(detachTree).pos != zXType.rc.rcOK.pos) {
                throw new Exception("Unable to recalc node.");
            }
            return detachTree;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Create a new tree from a node of an existing tree.", e);
            if (log.isErrorEnabled()) {
                log.error("Parameter : pobjTree = " + pobjTree);
                log.error("Parameter : pintNode = " + pintNode);
            }
            if (getZx().throwException) throw new ZXException(e);
            return detachTree;
        } finally {
            if (getZx().trace.isFrameworkTraceEnabled()) {
                getZx().trace.returnValue(detachTree);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Initialise a BO that implements a recursive tree node for being the root
     * of a tree.
     * 
     * <pre>
     * 
     *  Assumes   :
     *   PK of BO has been set
     * </pre>
     * 
     * @param pobjBO The business object for the Rec Tree.
     * @return Returns the return code of the method.
     * @throws ZXException Thrown if initRootNode fails.
     */
    public zXType.rc initRootNode(ZXBO pobjBO) throws ZXException {
        if (getZx().trace.isFrameworkTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pobjBO", pobjBO);
        }
        zXType.rc initRootNode = zXType.rc.rcOK;
        try {
            pobjBO.setValue("lvl", new LongProperty(1, false));
            pobjBO.setValue("root", pobjBO.getPKValue());
            pobjBO.setValue("hrrchy", new StringProperty("/" + pobjBO.getPKValue().getStringValue() + "/"));
            pobjBO.setValue("prnt", new LongProperty(0, true));
            return initRootNode;
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                getZx().trace.addError("Failed to : Initialise a BO that implements a recursive tree node for being the root of a tree.", e);
                log.error("Parameter : pobjBO = " + pobjBO);
            }
            if (getZx().throwException) throw new ZXException(e);
            initRootNode = zXType.rc.rcError;
            return initRootNode;
        } finally {
            if (getZx().trace.isFrameworkTraceEnabled()) {
                getZx().trace.returnValue(initRootNode);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Initialise a recursive tree node as a sub-node of the given parent.
     * 
     * <pre>
     * 
     * Assumes   :
     * 	Will set level / hierarchy / root and parent
     * </pre>
     * 
     * @param pobjNode The Business object for the Rec Tree Node.
     * @param pintParentID The parentID, primary key for the parent node.
     * @return Returns the return code of the method.
     * @throws ZXException Thrown if initSubNode fails.
     */
    public zXType.rc initSubNode(ZXBO pobjNode, int pintParentID) throws ZXException {
        if (getZx().trace.isFrameworkTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pobjNode", pobjNode);
            getZx().trace.traceParam("pintParentID", pintParentID);
        }
        zXType.rc initSubNode = zXType.rc.rcOK;
        try {
            ZXBO objParent = pobjNode.quickFKLoad(String.valueOf(pintParentID));
            if (objParent == null) {
                getZx().trace.addError("Unable to retrieve parent node for node subnode, ", pobjNode.getDescriptor().getName() + " - " + pintParentID);
                initSubNode = zXType.rc.rcError;
                return initSubNode;
            }
            pobjNode.setValue("root", objParent.getValue("root"));
            pobjNode.setValue("lvl", objParent.getValue("lvl").longValue() + 1 + "");
            pobjNode.setValue("prnt", objParent.getPKValue());
            pobjNode.setValue("hrrchy", objParent.getValue("hrrchy").getStringValue() + pobjNode.getPKValue().getStringValue() + "/");
            return initSubNode;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Initialise a recursive tree node as a sub-node of the given parent.", e);
            if (log.isErrorEnabled()) {
                log.error("Parameter : pobjNode = " + pobjNode);
                log.error("Parameter : pintParentID = " + pintParentID);
            }
            if (getZx().throwException) throw new ZXException(e);
            initSubNode = zXType.rc.rcError;
            return initSubNode;
        } finally {
            if (getZx().trace.isFrameworkTraceEnabled()) {
                getZx().trace.returnValue(initSubNode);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Check that the BO contains all the relevant attributes required to be a
     * recursive tree BO.
     * 
     * @param pobjBO The business object to use for checking.
     * @return Returns true if it is a valid rec tree.
     * @throws ZXException Thrown if isValidTreeBO fails.
     */
    public boolean isValidTreeBO(ZXBO pobjBO) throws ZXException {
        if (getZx().trace.isFrameworkTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pobjBO", pobjBO);
        }
        boolean isValidTreeBO = false;
        try {
            if (pobjBO.getDescriptor().getAttribute("root") == null) return isValidTreeBO;
            if (pobjBO.getDescriptor().getAttribute("lvl") == null) return isValidTreeBO;
            if (pobjBO.getDescriptor().getAttribute("hrrchy") == null) return isValidTreeBO;
            if (pobjBO.getDescriptor().getAttribute("prnt") == null) return isValidTreeBO;
            isValidTreeBO = true;
            return isValidTreeBO;
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                getZx().trace.addError("Failed to : Check that the BO contains all the relevant attributes", e);
                log.error("Parameter : pobjBO = " + pobjBO);
            }
            if (getZx().throwException) throw new ZXException(e);
            return isValidTreeBO;
        } finally {
            if (getZx().trace.isFrameworkTraceEnabled()) {
                getZx().trace.returnValue(isValidTreeBO);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Move one node in the tree to a new parent.
     *	
     * <pre>
     * 
     * Assumes   :
     * 	Node to be moved is NOT the root
     * </pre>
     *	
     * @param pobjTree The tree node to move. 
     * @param pintNode The primary key of the node to select. 
     * @param pintNewParent The new id to use. 
     * @return Returns the return code of the method.
     * @throws ZXException Thrown if moveNode fails. 
     */
    public zXType.rc moveNode(RecTreeNode pobjTree, int pintNode, int pintNewParent) throws ZXException {
        if (getZx().trace.isFrameworkTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pobjTree", pobjTree);
            getZx().trace.traceParam("pintNode", pintNode);
            getZx().trace.traceParam("pintNewParent", pintNewParent);
        }
        zXType.rc moveNode = zXType.rc.rcOK;
        try {
            RecTreeNode objNode = pobjTree.findNode(pintNode);
            if (objNode == null) {
                throw new Exception("Unable to find node to move");
            }
            RecTreeNode objNewParent = pobjTree.findNode(pintNewParent);
            if (objNewParent == null) {
                throw new Exception("Unable to find new parent node to move to");
            }
            RecTreeNode objOldParent = objNode.getParent();
            RecTreeNode objNodeWhat;
            Iterator iter = objOldParent.getChildNodes().iterator();
            while (iter.hasNext()) {
                objNodeWhat = (RecTreeNode) iter.next();
                if (objNode.getBo().getPKValue().compareTo(objNodeWhat.getBo().getPKValue()) == 0) {
                    iter.remove();
                    objNewParent.getChildNodes().add(objNodeWhat);
                    this.recalc(pobjTree);
                    break;
                }
            }
            return moveNode;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Move one node in the tree to a new parent.", e);
            if (log.isErrorEnabled()) {
                log.error("Parameter : pobjTree = " + pobjTree);
                log.error("Parameter : pintNode = " + pintNode);
                log.error("Parameter : pintNewParent = " + pintNewParent);
            }
            if (getZx().throwException) throw new ZXException(e);
            moveNode = zXType.rc.rcError;
            return moveNode;
        } finally {
            if (getZx().trace.isFrameworkTraceEnabled()) {
                getZx().trace.returnValue(moveNode);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
      * Move a node from one tree to another.
      *	
      * @param pobjTree The tree to copy the node from. 
      * @param pintNode The pimary key of the node to move. 
      * @param pobjNewTree The new Rec tree to move it to. 
      * @param pintNewParent The new primary key to use. 
      * @return Returns the return code of the method.
      * @throws ZXException Thrown if moveNodesAccrossTrees fails. 
      */
    public zXType.rc moveNodesAccrossTrees(RecTreeNode pobjTree, int pintNode, RecTreeNode pobjNewTree, int pintNewParent) throws ZXException {
        if (getZx().trace.isFrameworkTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pobjTree", pobjTree);
            getZx().trace.traceParam("pintNode", pintNode);
            getZx().trace.traceParam("pobjNewTree", pobjNewTree);
            getZx().trace.traceParam("pintNewParent", pintNewParent);
        }
        zXType.rc moveNodesAccrossTrees = zXType.rc.rcOK;
        try {
            if (pintNode == pobjTree.getBo().getPKValue().longValue()) {
                pobjNewTree.getChildNodes().add(pobjTree);
                if (recalc(pobjNewTree).pos != zXType.rc.rcOK.pos) {
                    moveNodesAccrossTrees = zXType.rc.rcError;
                    return moveNodesAccrossTrees;
                }
            } else {
                RecTreeNode objNode = pobjTree.findNode(pintNode);
                if (objNode == null) {
                    throw new Exception("Unable to find node to move");
                }
                RecTreeNode objNodeWhat;
                Iterator iter = objNode.getParent().getChildNodes().iterator();
                while (iter.hasNext()) {
                    objNodeWhat = (RecTreeNode) iter.next();
                    if (objNode.getBo().getPKValue().compareTo(objNodeWhat.getBo().getPKValue()) == 0) {
                        iter.remove();
                        RecTreeNode objNewParent = pobjNewTree.findNode(pintNewParent);
                        if (objNewParent == null) {
                            throw new Exception("Unable to find new parent to move node to");
                        }
                        objNewParent.getChildNodes().add(objNode);
                        if (recalc(pobjNewTree).pos != zXType.rc.rcOK.pos) {
                            moveNodesAccrossTrees = zXType.rc.rcError;
                            return moveNodesAccrossTrees;
                        }
                        if (recalc(pobjTree).pos != zXType.rc.rcOK.pos) {
                            moveNodesAccrossTrees = zXType.rc.rcError;
                            return moveNodesAccrossTrees;
                        }
                        break;
                    }
                }
            }
            return moveNodesAccrossTrees;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Move a node from one tree to another.", e);
            if (log.isErrorEnabled()) {
                log.error("Parameter : pobjTree = " + pobjTree);
                log.error("Parameter : pintNode = " + pintNode);
                log.error("Parameter : pobjNewTree = " + pobjNewTree);
                log.error("Parameter : pintNewParent = " + pintNewParent);
            }
            if (getZx().throwException) throw new ZXException(e);
            moveNodesAccrossTrees = zXType.rc.rcError;
            return moveNodesAccrossTrees;
        } finally {
            if (getZx().trace.isFrameworkTraceEnabled()) {
                getZx().trace.returnValue(moveNodesAccrossTrees);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Recalculate a tree.
     * 
     * @param pobjTree The tree node to recalc.
     * @return Returns the return code of the method.
     * @throws ZXException Thrown if recalc fails.
     */
    public zXType.rc recalc(RecTreeNode pobjTree) throws ZXException {
        if (getZx().trace.isFrameworkTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pobjTree", pobjTree);
        }
        zXType.rc recalc = zXType.rc.rcOK;
        try {
            initRootNode(pobjTree.getBo());
            RecTreeNode objNode;
            Iterator iter = pobjTree.getChildNodes().iterator();
            while (iter.hasNext()) {
                objNode = (RecTreeNode) iter.next();
                objNode.recalc(pobjTree);
            }
            return recalc;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Recalculate a tree.", e);
            if (log.isErrorEnabled()) {
                log.error("Parameter : pobjTree = " + pobjTree);
            }
            if (getZx().throwException) throw new ZXException(e);
            recalc = zXType.rc.rcError;
            return recalc;
        } finally {
            if (getZx().trace.isFrameworkTraceEnabled()) {
                getZx().trace.returnValue(recalc);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Return /<pk>.
     *
     * @param pobjBO The business object get the heirarchy as a string. 
     * @return Returns /<pk>
     * @throws ZXException Thrown if nodeHierarchyString fails. 
     */
    public String nodeHierarchyString(ZXBO pobjBO) throws ZXException {
        String nodeHierarchyString;
        nodeHierarchyString = '/' + pobjBO.getPKValue().getStringValue();
        return nodeHierarchyString;
    }

    /**
     * Resolve a recursive tree.
     * 
     * @param pobjRoot The business object for the root of the rec tree 
     * @return Returns the root rec tree.
     * @throws ZXException Thrown if resolve fails
     */
    public RecTreeNode resolve(ZXBO pobjRoot) throws ZXException {
        return resolve(pobjRoot, "*");
    }

    /**
     * Resolve a recursive tree.
     *
     *<pre>
     *
     *Assumes   :
     *	PK of root BO has been set
     *	Returns node for root or nothing in case
     *	of error
     *
     * Reviewed for V1.5:1
     *</pre>
     *
     * @param pobjRoot The business object for the root of the rec tree 
     * @param pstrGroup Group to load for each node. Optional, default should be "*"
     * @return Returns a recursive tree.
     * @throws ZXException  Thrown if resolve fails. 
     */
    public RecTreeNode resolve(ZXBO pobjRoot, String pstrGroup) throws ZXException {
        if (getZx().trace.isFrameworkTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pobjRoot", pobjRoot);
            getZx().trace.traceParam("pstrGroup", pstrGroup);
        }
        RecTreeNode resolve = null;
        DSHandler objDSHandler = null;
        DSRS objRS = null;
        if (pstrGroup == null) pstrGroup = "*";
        try {
            if (!isValidTreeBO(pobjRoot)) {
                return resolve;
            }
            objDSHandler = pobjRoot.getDS();
            if (pobjRoot.loadBO(pstrGroup).pos != zXType.rc.rcOK.pos) {
                return resolve;
            }
            if (objDSHandler.getDsType().pos == zXType.dsType.dstChannel.pos) {
                DSWhereClause objDSWhereClause = new DSWhereClause();
                objDSWhereClause.singleWhereCondition(pobjRoot, pobjRoot.getDescriptor().getAttributes().get("hrrchy"), zXType.compareOperand.coCNT, new StringProperty(nodeHierarchyString(pobjRoot) + "/"), zXType.dsWhereConditionOperator.dswcoNone);
                objDSWhereClause.singleWhereCondition(pobjRoot, pobjRoot.getDescriptor().getAttributes().get("root"), zXType.compareOperand.coEQ, pobjRoot.getPKValue(), zXType.dsWhereConditionOperator.dswcoAnd);
                if (treeHasSequence(pobjRoot)) {
                    if (objDSHandler.getOrderSupport().pos != zXType.dsOrderSupport.dsosFull.pos) {
                        throw new ZXException("Hierarchy sorting for recusrive trees requires full orderBy support of datasource");
                    }
                    objRS = objDSHandler.boRS(pobjRoot, pstrGroup, ':' + objDSWhereClause.getAsWhereClause(), false, "hrrchy,hrrchySrt");
                } else {
                    objRS = objDSHandler.boRS(pobjRoot, pstrGroup, ':' + objDSWhereClause.getAsWhereClause(), false, "hrrchy");
                }
            } else {
                String strQry = getZx().getSql().loadQuery(pobjRoot, pstrGroup);
                strQry = strQry + " AND " + getZx().getSql().singleWhereCondition(pobjRoot, pobjRoot.getDescriptor().getAttribute("hrrchy"), zXType.compareOperand.coCNT, new StringProperty(this.nodeHierarchyString(pobjRoot) + "/"));
                strQry = strQry + " AND " + getZx().getSql().whereCondition(pobjRoot, "root");
                if (treeHasSequence(pobjRoot)) {
                    strQry = strQry + getZx().getSql().orderByClause(pobjRoot, "hrrchySrt", false);
                } else {
                    strQry = strQry + getZx().getSql().orderByClause(pobjRoot, "lvl,nme", false);
                }
                objRS = ((DSHRdbms) objDSHandler).sqlRS(strQry);
                if (objRS == null) {
                    throw new Exception("Failed to execute query :" + strQry);
                }
            }
            if (objRS.eof()) {
                resolve = new RecTreeNode();
                resolve.setBo(pobjRoot);
                return resolve;
            }
            objRS.rs2obj(pobjRoot, pstrGroup);
            RecTreeNode objRoot = new RecTreeNode();
            objRoot.setBo(pobjRoot);
            objRS.moveNext();
            ZXBO objBO;
            RecTreeNode objNode;
            RecTreeNode objParent;
            while (!objRS.eof()) {
                objBO = pobjRoot.cloneBO();
                if (objBO == null) {
                    return resolve;
                }
                objRS.rs2obj(objBO, pstrGroup);
                objNode = new RecTreeNode();
                objParent = objRoot.findParentNode(objBO);
                if (objParent == null) {
                    throw new Exception("Unable to find parent node : " + objBO.formattedString("label"));
                }
                objNode.setBo(objBO);
                objNode.setParent(objParent);
                objNode.setRoot(objRoot);
                objParent.getChildNodes().put(objNode.getBo().getPKValue().getStringValue(), objNode);
                objRS.moveNext();
            }
            resolve = objRoot;
            return resolve;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Resolve a recursive tree.", e);
            if (log.isErrorEnabled()) {
                log.error("Parameter : pobjRoot = " + pobjRoot);
                log.error("Parameter : pstrGroup = " + pstrGroup);
            }
            if (getZx().throwException) throw new ZXException(e);
            return resolve;
        } finally {
            if (objRS != null) objRS.RSClose();
            if (getZx().trace.isFrameworkTraceEnabled()) {
                getZx().trace.returnValue(resolve);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Check that BO support special attribute for sequence. (i.e. sequence of siblibngs is significant)
     *
     * @param pobjBO The business object to check. 
     * @return Returns true of the rec tree has a sequence.
     * @throws ZXException Thrown if treeHasSequence fails. 
     */
    public boolean treeHasSequence(ZXBO pobjBO) throws ZXException {
        if (getZx().trace.isFrameworkTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pobjBO", pobjBO);
        }
        boolean treeHasSequence = false;
        try {
            if (pobjBO.getDescriptor().getAttribute("hrrchySrt") == null) {
                treeHasSequence = false;
            } else {
                treeHasSequence = true;
            }
            return treeHasSequence;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Check that BO support special attribute for sequence (i.e. sequence of siblibngs is significant)", e);
            if (log.isErrorEnabled()) {
                log.error("Parameter : pobjBO = " + pobjBO);
            }
            if (getZx().throwException) throw new ZXException(e);
            return treeHasSequence;
        } finally {
            if (getZx().trace.isFrameworkTraceEnabled()) {
                getZx().trace.returnValue(treeHasSequence);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
	 * Persist a tree.
	 *
	 * @param pobjTree The tree node to persist  
	 * @param pstrGroup Optional, default is "*" 
	 * @return Returns the return code of the method. 
	 * @throws ZXException Thrown if persist fails. 
	 */
    public zXType.rc persist(RecTreeNode pobjTree, String pstrGroup) throws ZXException {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pobjTree", pobjTree);
            getZx().trace.traceParam("pstrGroup", pstrGroup);
        }
        zXType.rc persist = zXType.rc.rcOK;
        if (pstrGroup == null) pstrGroup = "*";
        try {
            if (pobjTree.persist(pstrGroup).pos != zXType.rc.rcOK.pos) {
                return persist;
            }
            return persist;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Persist a tree.", e);
            if (log.isErrorEnabled()) {
                log.error("Parameter : pobjTree = " + pobjTree);
                log.error("Parameter : pstrGroup = " + pstrGroup);
            }
            if (getZx().throwException) throw new ZXException(e);
            persist = zXType.rc.rcError;
            return persist;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(persist);
                getZx().trace.exitMethod();
            }
        }
    }
}
