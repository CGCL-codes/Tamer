package org.deft.repository.ast.transform;

import java.util.List;
import org.deft.repository.ast.TreeNode;
import org.deft.repository.ast.TreeWalker;

public class TreeConverter {

    private List<INodeTemplate> templates;

    private INodeGrouper grouper;

    private INodeFactory factory;

    private boolean firstCall;

    private boolean dummyCreated;

    public TreeConverter() {
    }

    /**
	 * Converts a xml node into a structure of TreeNodes. 
	 * @param node - the xml node that should be processed
	 * @param factory - describes how to create a TreeNode (or a list of TreeNodes) from a xml node. This
	 * argument is not allowed to be null.
	 * @param filter - describes which xml nodes should be processed. If this is null, all nodes will be converted
	 * @param grouper - describes if a special node should be grouped under TreeNode with the given name. This
	 * argument can be null.
	 * @param templates - a list of templates that are capable of adding information to each TreeNode. This 
	 * argument is allowed to be null
	 * @return a NodeList<TreeNode>, which is a list of TreeNodes. This must be a list because you can create more than
	 * one TreeNode out of one xml node. The creation process is driven by the given INodeFactory.
	 */
    public NodeList<TreeNode> convert(TreeNode node, INodeFactory factory, ITreeNodeFilter filter, INodeGrouper grouper, List<INodeTemplate> templates) {
        if (factory == null) {
            throw new IllegalArgumentException("INodeFactory must not be null");
        }
        if (node == null) {
            throw new IllegalArgumentException("Node must not be null");
        }
        this.templates = templates;
        this.grouper = grouper;
        this.factory = factory;
        this.firstCall = true;
        this.dummyCreated = false;
        TreeWalker treeWalker = new TreeWalker(node, filter);
        NodeList<TreeNode> result = processNode(treeWalker);
        if (dummyCreated) {
            TreeNode root = result.getParent();
            result = new NodeList<TreeNode>();
            for (TreeNode tn : root.getChildren()) {
                result.add(tn);
            }
        }
        firstCall = true;
        return result;
    }

    private NodeList<TreeNode> processNode(TreeWalker tw) {
        TreeNode n = tw.getCurrentNode();
        NodeList<TreeNode> nodeList = getNodeList(n);
        NodeList<TreeNode> result = nodeList;
        for (TreeNode child = tw.firstChild(); child != null; child = tw.nextSibling()) {
            NodeList<TreeNode> childNodeList = processNode(tw);
            if (nodeList == null) {
                if (result == null) result = new NodeList<TreeNode>();
                result.addAll(childNodeList);
            } else {
                if (childNodeList != null) {
                    for (TreeNode childNode : childNodeList.getElements()) {
                        TreeNode parent = nodeList.getParent();
                        TreeNode grouper = getGrouperNode(parent, child);
                        if (grouper != null) parent = grouper;
                        parent.addChild(childNode);
                    }
                }
            }
        }
        tw.setCurrentNode(n);
        return result;
    }

    private NodeList<TreeNode> getNodeList(TreeNode node) {
        NodeList<TreeNode> result = factory.createNode(node);
        if (firstCall) {
            firstCall = false;
            if (result == null) {
                result = new NodeList<TreeNode>(new TreeNode("_dummy"));
                dummyCreated = true;
            }
        }
        return result;
    }

    private TreeNode getGrouperNode(TreeNode parent, TreeNode node) {
        if (grouper == null) return null;
        String groupName = grouper.getGroupName(node);
        if (groupName == null || groupName.length() == 0) return null;
        for (TreeNode child : parent.getChildren()) {
            if (child.getName().equals(groupName)) {
                return child;
            }
        }
        TreeNode grouper = factory.createGroupingNode(groupName, node);
        parent.addChild(grouper);
        return grouper;
    }
}
