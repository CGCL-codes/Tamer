package org.docflower.model.actionhandlers;

import java.text.MessageFormat;
import java.util.Map;
import org.docflower.consts.DocFlowerConsts;
import org.docflower.model.*;
import org.docflower.ui.*;
import org.docflower.util.LowLevelException;
import org.docflower.util.Messages;
import org.docflower.xml.*;
import org.w3c.dom.*;

/**
 * Removes selected node or some subnode defined by xpath.
 * <p>
 * <b>xpathType</b> explains how to find node to delete. May be set to:
 * <p>
 * <b>
 * <li>xpathTypeBySelection</b> (default) means to use as base node provided by
 * selectionProviderId parameter.</li>
 * <p>
 * <b>
 * <li>xpathTypeRelative</b> means to use specified base node.</li>
 * <p>
 * <b>xpath</b> xpath used to calculate node to delete.
 * <p>
 * <b>removeParentIfEmpty</b> used to determine whether to delete parent node if
 * there was deleted its last child.
 */
public class RemoveSubTreeActionHandler extends SingleNodeActionHandler {

    private boolean removeParentIfEmpty;

    private Node removedSubTree;

    private int positionInParent;

    private String xpath;

    private String xpathType;

    @Override
    public void init(Action parent, Element actionItemElement, Map<String, Object> params, Node baseNode, UpdateInfo updateInfo) {
        String selectionProviderId;
        if (actionItemElement != null) {
            if (DocFlowerConsts.KEY_LOCAL_ACTION_NS.equals(actionItemElement.getAttribute(DocFlowerConsts.ACTION_TYPE_LOCAL))) {
                throw new LowLevelException(Messages.RemoveSubTreeActionHandler_IsNotSupportedWithinLocalAction);
            }
            selectionProviderId = DOMUtils.getAttrValue(actionItemElement, DocFlowerConsts.ATTR_SELECTION_PROVIDER_ID);
            removeParentIfEmpty = "true".equals(DOMUtils.getAttrValue(actionItemElement, DocFlowerConsts.ATTR_REMOVE_PARENT_IF_EMPTY));
            xpath = DOMUtils.getAttrValue(actionItemElement, DocFlowerConsts.ATTR_XPATH);
            xpathType = DOMUtils.getAttrValue(actionItemElement, DocFlowerConsts.PARAM_XPATH_TYPE);
            xpathType = xpathType.equals(Messages.RemoveSubTreeActionHandler_EMPTY) ? DocFlowerConsts.PARAM_XPATH_TYPE_BY_SELECTION : xpathType;
        } else {
            selectionProviderId = null;
        }
        if (params != null) {
            if (params.containsKey(DocFlowerConsts.ATTR_SELECTION_PROVIDER_ID)) {
                selectionProviderId = (String) params.get(DocFlowerConsts.ATTR_SELECTION_PROVIDER_ID);
            }
            if (params.containsKey(DocFlowerConsts.ATTR_REMOVE_PARENT_IF_EMPTY)) {
                Boolean removeParentIfEmptyBool = (Boolean) params.get(DocFlowerConsts.ATTR_REMOVE_PARENT_IF_EMPTY);
                removeParentIfEmpty = removeParentIfEmptyBool != null ? removeParentIfEmptyBool.booleanValue() : false;
            }
            if (params.containsKey(DocFlowerConsts.ATTR_XPATH)) {
                xpath = (String) params.get(DocFlowerConsts.ATTR_XPATH);
            }
            if (params.containsKey(DocFlowerConsts.PARAM_XPATH_TYPE)) {
                xpathType = (String) params.get(DocFlowerConsts.PARAM_XPATH_TYPE);
            } else {
                xpathType = DocFlowerConsts.PARAM_XPATH_TYPE_BY_SELECTION;
            }
        }
        if (xpathType.equals(DocFlowerConsts.PARAM_XPATH_TYPE_BY_SELECTION)) {
            if ((selectionProviderId != null) && (parent.getContext() instanceof ISelectionsManager)) {
                removedSubTree = getRemovedNode(parent.getContext(), selectionProviderId, xpath);
                super.init(parent, actionItemElement, params, getRemovedSubTree().getParentNode(), updateInfo);
                getBaseNode(parent.getContext(), updateInfo);
                setPositionInParent(DOMUtils.getNodePositionInParent(getRemovedSubTree()));
            }
        } else {
            ExpressionManager exManager = parent.getContext().getDataModel().getDescriptor().getExpressionManager();
            removedSubTree = exManager.getNodeByExpression(baseNode, xpath, parent.getContext().getDataModel().getDataNSContext(), true, false);
            super.init(parent, actionItemElement, params, getRemovedSubTree().getParentNode(), updateInfo);
            getBaseNode(parent.getContext(), updateInfo);
            setPositionInParent(DOMUtils.getNodePositionInParent(getRemovedSubTree()));
        }
    }

    @Override
    public boolean handle(Action parent, UpdateInfo updateInfo) {
        boolean result = false;
        if (getRemovedSubTree() != null) {
            getRemovedSubTree().getParentNode().setUserData(DocFlowerConsts.FLAG_CHILDREN_CHANGED, DocFlowerConsts.FLAG_CHILDREN_CHANGED, null);
            result = DOMUtils.removeNode(getRemovedSubTree());
            parent.getContext().getDataModel().setState(DataModel.DataState.DS_DIRTY);
            result = true;
        }
        return result;
    }

    @Override
    public boolean rollback(Action parent, UpdateInfo updateInfo) {
        boolean result = false;
        if (getRemovedSubTree() != null) {
            Node parentNode = getBaseNode(parent.getContext(), updateInfo);
            DOMUtils.insertNodeToPosition(parentNode, getRemovedSubTree(), getPositionInParent());
            parent.getContext().getDataModel().setState(DataModel.DataState.DS_DIRTY);
            result = true;
        }
        return result;
    }

    private Node getRemovedNode(IModelContext context, String selectionProviderId, String relativeXpath) {
        if (selectionProviderId.length() > 0) {
            Node removedNode = (Node) ((ISelectionsManager) context).getSelected(selectionProviderId);
            if (removedNode != null) {
                if ((relativeXpath != null) && (!"".equals(relativeXpath))) {
                    ExpressionManager em = context.getDataModel().getDescriptor().getExpressionManager();
                    removedNode = em.getNodeByExpression(removedNode, relativeXpath, context.getDataModel().getDescriptor().getActionNamespaceContext(), true, false);
                    if (removedNode == null) {
                        throw new LowLevelException(MessageFormat.format(Messages.RemoveSubTreeActionHandler_UnableFindNodeForRemoving, relativeXpath));
                    }
                }
                if (isRemoveParentIfEmpty()) {
                    if (removedNode.getParentNode().getChildNodes().getLength() == 1) {
                        removedNode = removedNode.getParentNode();
                    }
                }
            }
            return removedNode;
        } else {
            if ((relativeXpath != null) && (!"".equals(relativeXpath))) {
                ExpressionManager em = context.getDataModel().getDescriptor().getExpressionManager();
                Node removedNode = em.getNodeByExpression(context.getBaseNode(), relativeXpath, context.getDataModel().getDescriptor().getActionNamespaceContext(), true, false);
                if (removedNode == null) {
                    throw new LowLevelException(MessageFormat.format(Messages.RemoveSubTreeActionHandler_UnableFindNodeForRemoving, relativeXpath));
                }
                if (isRemoveParentIfEmpty()) {
                    if (removedNode.getParentNode().getChildNodes().getLength() == 1) {
                        removedNode = removedNode.getParentNode();
                    }
                }
                return removedNode;
            }
        }
        return null;
    }

    public Node getRemovedSubTree() {
        return removedSubTree;
    }

    public void setRemovedSubTree(Node removedSubTree) {
        this.removedSubTree = removedSubTree;
    }

    public int getPositionInParent() {
        return positionInParent;
    }

    public void setPositionInParent(int positionInParent) {
        this.positionInParent = positionInParent;
    }

    public boolean isRemoveParentIfEmpty() {
        return removeParentIfEmpty;
    }

    public void setRemoveParentIfEmpty(boolean removeParentIfEmpty) {
        this.removeParentIfEmpty = removeParentIfEmpty;
    }
}
