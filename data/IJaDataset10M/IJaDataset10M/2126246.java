package octlight.scene.visitor;

import octlight.scene.GeometryLeaf;
import octlight.scene.Node;

/**
 * @author $Author: creator $
 * @version $Revision: 1.1.1.1 $
 */
public class NodeVisitorAdapter implements NodeVisitor {

    public void visit(GeometryLeaf geometryLeaf) {
        visit((Node) geometryLeaf);
    }

    public void visit(Node node) {
        node.visitChildren(this);
    }
}
