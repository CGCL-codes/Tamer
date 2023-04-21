package net.community.chest.ui.components.tree.document;

import org.w3c.dom.Node;
import net.community.chest.dom.NodeTypeEnum;
import net.community.chest.ui.helpers.tree.TypedTreeNode;

/**
 * <P>Copyright 2008 as per GPLv2</P>
 *
 * @param <N> The type of {@link Node} contained
 * @author Lyor G.
 * @since Jan 5, 2009 2:42:15 PM
 */
public class DOMNode<N extends Node> extends TypedTreeNode<N> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3362436807268832853L;

    public DOMNode(Class<N> nodeClass, N nodeObject, String nodeText, boolean withChildren) {
        super(nodeClass, nodeObject, nodeText, withChildren);
    }

    public DOMNode(Class<N> nodeClass, N nodeObject, boolean withChildren) {
        this(nodeClass, nodeObject, null, withChildren);
    }

    public DOMNode(Class<N> nodeClass, N nodeObject, String nodeText) {
        this(nodeClass, nodeObject, nodeText, true);
    }

    public DOMNode(Class<N> nodeClass, N nodeObject) {
        this(nodeClass, nodeObject, null);
    }

    public DOMNode(Class<N> nodeClass) {
        this(nodeClass, null);
    }

    @Override
    public String getNodeText(N nodeObject) {
        if (nodeObject != null) return nodeObject.getNodeName();
        return super.getNodeText(nodeObject);
    }

    public NodeTypeEnum getNodeType() {
        return NodeTypeEnum.fromNode(getAssignedValue());
    }
}
