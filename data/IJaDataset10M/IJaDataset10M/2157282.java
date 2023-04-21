package edu.indiana.extreme.xbaya.component.system;

import edu.indiana.extreme.xbaya.graph.Graph;
import edu.indiana.extreme.xbaya.graph.Node;
import edu.indiana.extreme.xbaya.graph.system.OutputNode;

/**
 * @author Satoshi Shirasuna
 */
public class OutputComponent extends SystemComponent {

    /**
     * The name of the parameter component
     */
    public static final String NAME = "Output";

    private static final String DESCRIPTION = "A system component that represents an output parameter of a workflow.";

    private static final String PORT_NAME = "Parameter";

    private static final String PORT_DESCRIPTION = "This port can be connected to any type.";

    /**
     * Creates an OutputComponent.
     */
    public OutputComponent() {
        setName(NAME);
        setDescription(DESCRIPTION);
        SystemComponentDataPort port = new SystemComponentDataPort(PORT_NAME);
        port.setDescription(PORT_DESCRIPTION);
        this.inputs.add(port);
    }

    /**
     * @see edu.indiana.extreme.xbaya.component.Component#createNode(edu.indiana.extreme.xbaya.graph.Graph)
     */
    @Override
    public Node createNode(Graph graph) {
        OutputNode node = new OutputNode(graph);
        node.setName(NAME);
        node.setComponent(this);
        node.createID();
        createPorts(node);
        return node;
    }
}
