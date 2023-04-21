package astcentric.structure.query;

import java.util.HashSet;
import java.util.Set;
import astcentric.structure.basic.MonitoredNodeHandler;
import astcentric.structure.basic.Node;
import astcentric.structure.basic.NodeHandler;

/**
 * The union of node collections.
 *
 */
public class UnionOfNodeCollections extends CollectionOfNodeCollections {

    @Override
    boolean isUnion() {
        return true;
    }

    @Override
    CollectionOfNodeCollections createCollectionOfCollections() {
        return new IntersectionOfNodeCollections();
    }

    @Override
    protected void plainTraverseNodes(final NodeHandler handler) {
        MonitoredNodeHandler monitoredHandler = new MonitoredNodeHandler(new NodeHandler() {

            Set<Node> visitedNodes = new HashSet<Node>();

            public boolean handle(Node node) {
                if (visitedNodes.contains(node)) {
                    return false;
                }
                visitedNodes.add(node);
                return handler.handle(node);
            }
        });
        Iterable<ExtendedNodeCollection> collections = getCollectionOfCollections();
        for (ExtendedNodeCollection collection : collections) {
            collection.traverseNodes(monitoredHandler);
            if (monitoredHandler.isFinished()) {
                break;
            }
        }
    }
}
