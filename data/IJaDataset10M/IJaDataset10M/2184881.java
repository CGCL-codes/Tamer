package org.itsnat.impl.core.js.dom.node;

import org.itsnat.impl.core.js.JSRenderImpl;
import org.itsnat.impl.core.clientdoc.ClientAJAXDocumentImpl;
import org.itsnat.impl.core.path.NodeLocationNotParent;
import org.itsnat.impl.core.path.NodeLocationWithParent;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class JSNotAttrOrViewNodeRenderImpl extends JSNodeRenderImpl {

    /** Creates a new instance of JSNoAttributeRender */
    public JSNotAttrOrViewNodeRenderImpl() {
    }

    protected abstract String createNodeCode(Node node, ClientAJAXDocumentImpl clientDoc);

    protected abstract String getAppendNewNodeCode(Node newNode, Node parent, String parentVarName, ClientAJAXDocumentImpl clientDoc);

    protected String addCompleteChildNode(Node parent, Node newNode, String parentVarName, ClientAJAXDocumentImpl clientDoc) {
        String newNodeCode = createNodeCode(newNode, clientDoc);
        return "itsNatDoc.appendChild(" + parentVarName + "," + newNodeCode + ");\n";
    }

    protected String getInsertCompleteNodeCode(Node newNode, ClientAJAXDocumentImpl clientDoc) {
        String newNodeCode = createNodeCode(newNode, clientDoc);
        return getInsertCompleteNodeCode(newNode, newNodeCode, clientDoc);
    }

    public abstract String getInsertNewNodeCode(Node newNode, ClientAJAXDocumentImpl clientDoc);

    public String getRemoveNodeCode(Node removedNode, ClientAJAXDocumentImpl clientDoc) {
        String id = clientDoc.getCachedNodeId(removedNode);
        if (id != null) {
            return "itsNatDoc.removeChild2(\"" + id + "\");\n";
        } else {
            Node parent = removedNode.getParentNode();
            NodeLocationWithParent parentLoc = clientDoc.getNodeLocation(parent);
            String childRelPath = clientDoc.getRelativeStringPathFromNodeParent(removedNode);
            childRelPath = JSRenderImpl.toLiteralStringJS(childRelPath);
            return "itsNatDoc.removeChild3(" + parentLoc.toJSArray() + "," + childRelPath + ");\n";
        }
    }

    protected String getInsertCompleteNodeCode(Node newNode, String newNodeCode, ClientAJAXDocumentImpl clientDoc) {
        Node parent = newNode.getParentNode();
        Node nextSibling = newNode.getNextSibling();
        if (nextSibling != null) {
            NodeLocationNotParent refNodeLoc = clientDoc.getRefNodeLocationInsertBefore(newNode, nextSibling);
            NodeLocationWithParent parentLoc = clientDoc.getNodeLocation(parent);
            return "itsNatDoc.insertBefore2(" + parentLoc.toJSArray() + "," + newNodeCode + "," + refNodeLoc.toJSArray() + ");\n";
        } else {
            NodeLocationWithParent parentLoc = clientDoc.getNodeLocation(parent);
            return "itsNatDoc.appendChild2(" + parentLoc.toJSArray() + "," + newNodeCode + ");\n";
        }
    }
}
