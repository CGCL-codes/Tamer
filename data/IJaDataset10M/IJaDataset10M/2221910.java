package astcentric.structure.validation.syntax;

import astcentric.structure.basic.Node;
import astcentric.structure.basic.NodeHandler;
import astcentric.structure.query.UnionOfNodeCollections;
import astcentric.structure.vl.basic.NodeMatcher;

public class UnionOfMatcherFactory extends AbstractNodeMatcherFactory {

    public UnionOfMatcherFactory(NodeMatcherFactory matcherFactory) {
        super(matcherFactory);
    }

    public NodeMatcher create(Node specification) {
        final UnionOfNodeCollections union = new UnionOfNodeCollections();
        specification.traverseNodes(new NodeHandler() {

            public boolean handle(Node child) {
                union.addCollection(_matcherFactory.create(child));
                return false;
            }
        });
        return new NodeReferenceMatcher(union);
    }
}
