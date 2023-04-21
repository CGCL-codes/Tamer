package astcentric.structure.query;

import java.util.Arrays;
import org.jmock.Mock;
import astcentric.structure.basic.AST;
import astcentric.structure.basic.Node;
import astcentric.structure.basic.NodeTool;
import astcentric.structure.basic.NodeValidator;

public class DifferenceCollectorTest extends NodeCollectorCollectionTestCase {

    private AST _ast;

    private Node _root;

    private Node _child;

    @Override
    protected void setUp() throws Exception {
        _ast = createAST();
        _root = _ast.createChildNode(null, 0);
        _child = _ast.createChildNode(_root, 0);
    }

    public void testEmptyCollector() {
        DifferenceCollector collector = new DifferenceCollector();
        assertEquals(Arrays.asList(_child), NodeTool.getNodesOf(collector.collect(_child, null)));
        assertEquals(Arrays.asList(_root), NodeTool.getNodesOf(collector.collect(_root, null)));
    }

    public void testFilterButNoCollector() {
        DifferenceCollector collector = new DifferenceCollector();
        Mock filter = mock(NodeValidator.class);
        prepareFilter(filter, _root, true);
        prepareFilter(filter, _child, false);
        collector.setFilter((NodeValidator) filter.proxy());
        assertEquals(Arrays.asList(), NodeTool.getNodesOf(collector.collect(_child, null)));
        assertEquals(Arrays.asList(_root), NodeTool.getNodesOf(collector.collect(_root, null)));
    }

    public void testWithNoFilterAndOneCollector() {
        DifferenceCollector collector = new DifferenceCollector();
        addCollection(collector, _root);
        assertEquals(Arrays.asList(_root), NodeTool.getNodesOf(collector.collect(_child, null)));
    }

    public void testWithNoFilterAndTwoCollectors() {
        DifferenceCollector collector = new DifferenceCollector();
        addCollection(collector, _root, _child);
        addCollection(collector, _root);
        assertEquals(Arrays.asList(_child), NodeTool.getNodesOf(collector.collect(_root, null)));
        assertEquals(Arrays.asList(_child), NodeTool.getNodesOf(collector.collect(_child, null)));
    }
}
