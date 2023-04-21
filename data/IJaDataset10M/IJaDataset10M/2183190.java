package org.plazmaforge.studio.core.model.nodes;

public class NodeUtils {

    private NodeUtils() {
    }

    public static INode findNodeByData(INode node, Object data) {
        if (node == null || data == null) {
            return null;
        }
        if (data == node.getData()) {
            return node;
        }
        if (!node.hasChildren()) {
            return null;
        }
        INode[] children = node.getChildNodes();
        INode resultNode = null;
        for (INode c : children) {
            resultNode = findNodeByData(c, data);
            if (resultNode != null) {
                return resultNode;
            }
        }
        return null;
    }
}
