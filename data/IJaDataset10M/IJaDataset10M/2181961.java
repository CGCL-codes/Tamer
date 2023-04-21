package astcentric.structure.vl.basic;

import java.util.List;
import astcentric.structure.basic.Node;
import astcentric.structure.bl.AbstractCalculator;
import astcentric.structure.bl.CalculationContext;
import astcentric.structure.bl.Data;
import astcentric.structure.bl.DataFactory;
import astcentric.structure.bl.Function;
import astcentric.structure.query.ExtendedNodeCollection;

final class NodeMatching extends AbstractCalculator {

    private final DataFactory<Object> _dataFactory;

    NodeMatching(DataFactory<Object> dataFactory) {
        _dataFactory = dataFactory;
    }

    protected Data doCalculate(CalculationContext calculationContext, List<Function> arguments) {
        if (arguments == null || arguments.size() != 2) {
            throw new IllegalArgumentException("Two arguments expected.");
        }
        Function firstArgument = arguments.get(0);
        if (firstArgument instanceof Data == false) {
            throw new IllegalArgumentException("Data instead of function '" + firstArgument.getFunctionDeclaration().getName() + "' expected.");
        }
        Data matcher = (Data) firstArgument;
        List<Data> children = matcher.getChildren();
        if (children.size() != 1) {
            throw new IllegalArgumentException("Exactly one child expected instead of " + children.size());
        }
        Data data = children.get(0);
        if (data instanceof NodeCollectionProvider == false) {
            throw new IllegalArgumentException("First argument isn't a Node or a Node Collection.");
        }
        ExtendedNodeCollection nodeCollection = ((NodeCollectionProvider) data).getNodeCollection();
        Function secondArgument = arguments.get(1);
        if (secondArgument instanceof NodeData == false) {
            throw new IllegalArgumentException("Second argument isn't a node data: " + secondArgument);
        }
        NodeData nodeData = (NodeData) secondArgument;
        Node node = nodeData.getNode();
        return _dataFactory.create(node != null && nodeCollection.contains(node));
    }
}
