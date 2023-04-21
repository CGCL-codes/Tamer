package com.triplea.rolap.plugins.acl;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;

/**
 * @author kononyhin
 *
 */
public class ObjectsTreeNodeAction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    private Logger _logger = Logger.getLogger(ObjectsTreeNodeAction.class.getName());

    private JPluginACLDefaultController _controller = null;

    private int _actionType;

    public static final int RULE_NEW = 1;

    public static final int RULE_DELETE = 2;

    public ObjectsTreeNodeAction(JPluginACLDefaultController controller, int actionType) {
        super();
        this._controller = controller;
        this._actionType = actionType;
        String text = "";
        switch(actionType) {
            case ObjectsTreeNodeAction.RULE_NEW:
                text = "New";
                break;
            case ObjectsTreeNodeAction.RULE_DELETE:
                text = "Delete";
                break;
        }
        this.putValue(Action.NAME, text);
    }

    public void actionPerformed(ActionEvent e) {
        DefaultMutableTreeNode dbNode = null;
        DefaultMutableTreeNode cubeNode = null;
        DefaultMutableTreeNode rulesNode = null;
        switch(this._actionType) {
            case ObjectsTreeNodeAction.RULE_NEW:
                dbNode = this._controller.getAncestorDatabaseNode();
                if (null == dbNode) {
                    _logger.warn("No ancestor database node");
                    return;
                }
                cubeNode = this._controller.getAncestorCubeNode();
                if (null == cubeNode) {
                    _logger.warn("No ancestor cube node");
                    return;
                }
                rulesNode = this._controller.getAncestorReadRulesNode();
                if (null == rulesNode) {
                    rulesNode = this._controller.getAncestorWriteRulesNode();
                }
                if (null == rulesNode) {
                    _logger.warn("No ancestor rules node (neither write no read)");
                    return;
                }
                Rule newRule = Rule.createAllowRule("unnamed");
                if (ObjectTreeNodeInfo.NODE_TYPE_READRULES == ((ObjectTreeNodeInfo) rulesNode.getUserObject()).getType()) {
                    ((Default) this._controller.getView().getPlugin()).addReadRule(((ObjectTreeNodeInfo) dbNode.getUserObject()).getLabel(), ((ObjectTreeNodeInfo) cubeNode.getUserObject()).getLabel(), newRule);
                } else {
                    ((Default) this._controller.getView().getPlugin()).addWriteRule(((ObjectTreeNodeInfo) dbNode.getUserObject()).getLabel(), ((ObjectTreeNodeInfo) cubeNode.getUserObject()).getLabel(), newRule);
                }
                ObjectTreeNodeInfo treeNodeInfo = new ObjectTreeNodeInfo(ObjectTreeNodeInfo.NODE_TYPE_RULE, newRule.getName());
                treeNodeInfo.setRule(newRule);
                DefaultMutableTreeNode ruleNode = new DefaultMutableTreeNode(treeNodeInfo);
                this._controller.getView().addTreeNode(rulesNode, ruleNode);
                this._controller.getView().getJObjectsTree().scrollPathToVisible(new TreePath(ruleNode.getPath()));
                this._controller.getView().getJObjectsTree().setSelectionPath(new TreePath(ruleNode.getPath()));
                break;
            case ObjectsTreeNodeAction.RULE_DELETE:
                DefaultMutableTreeNode nodeToRemove = (DefaultMutableTreeNode) this._controller.getView().getJObjectsTree().getLastSelectedPathComponent();
                if (null == nodeToRemove) {
                    _logger.warn("Node hasn't been selected");
                    return;
                }
                Rule rule = ((ObjectTreeNodeInfo) nodeToRemove.getUserObject()).getRule();
                if (null == rule) {
                    _logger.warn("Rule hasn't specified for selected node : " + (new TreePath((nodeToRemove.getPath()))).toString());
                    return;
                }
                dbNode = this._controller.getAncestorDatabaseNode();
                if (null == dbNode) {
                    _logger.warn("No ancestor database node");
                    return;
                }
                cubeNode = this._controller.getAncestorCubeNode();
                if (null == cubeNode) {
                    _logger.warn("No ancestor cube node");
                    return;
                }
                rulesNode = this._controller.getAncestorReadRulesNode();
                if (null == rulesNode) {
                    rulesNode = this._controller.getAncestorWriteRulesNode();
                }
                if (null == rulesNode) {
                    _logger.warn("No ancestor rules node (neither write no read)");
                    return;
                }
                if (ObjectTreeNodeInfo.NODE_TYPE_READRULES == ((ObjectTreeNodeInfo) rulesNode.getUserObject()).getType()) {
                    ((Default) this._controller.getView().getPlugin()).deleteReadRule(((ObjectTreeNodeInfo) dbNode.getUserObject()).getLabel(), ((ObjectTreeNodeInfo) cubeNode.getUserObject()).getLabel(), rule);
                } else {
                    ((Default) this._controller.getView().getPlugin()).deleteWriteRule(((ObjectTreeNodeInfo) dbNode.getUserObject()).getLabel(), ((ObjectTreeNodeInfo) cubeNode.getUserObject()).getLabel(), rule);
                }
                ((DefaultTreeModel) this._controller.getView().getJObjectsTree().getModel()).removeNodeFromParent(nodeToRemove);
                break;
        }
    }
}
