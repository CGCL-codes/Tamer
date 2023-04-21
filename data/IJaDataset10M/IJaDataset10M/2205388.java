package edu.indiana.extreme.xbaya.component.dynamic;

import edu.indiana.extreme.xbaya.graph.Graph;
import edu.indiana.extreme.xbaya.graph.Node;
import edu.indiana.extreme.xbaya.graph.dynamic.CepNode;
import edu.indiana.extreme.xbaya.graph.dynamic.CombineMultipleStreamNode;

/**
 * @author Chathura Herath
 */
public class CombineMultipleStreamComponent extends CepComponent {

    public static final String NAME = "Combine_Stream";

    public CombineMultipleStreamComponent() {
        super(NAME);
    }

    public CombineMultipleStreamNode createNode(Graph graph) {
        CombineMultipleStreamNode node = new CombineMultipleStreamNode(graph);
        node.setName(getName());
        node.setComponent(new CepComponent());
        node.createID();
        createPorts(node);
        return node;
    }
}
