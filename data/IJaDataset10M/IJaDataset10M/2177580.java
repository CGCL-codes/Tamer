package edu.indiana.extreme.xbaya.component;

import java.util.List;
import edu.indiana.extreme.xbaya.component.system.SystemComponent;
import edu.indiana.extreme.xbaya.component.system.SystemComponentDataPort;
import edu.indiana.extreme.xbaya.graph.Graph;
import edu.indiana.extreme.xbaya.graph.Node;
import edu.indiana.extreme.xbaya.graph.system.InputNode;
import edu.indiana.extreme.xbaya.graph.system.gui.StreamSourceNode;

/**
 * @author Chathura Herath
 * 
 * */
public class StreamSourceComponent extends SystemComponent {

    /**
	 * The name of the input component
	 */
    public static final String NAME = "Stream Source";

    private static final String DESCRIPTION = "A system component that represents an stream input parameter of a workflow.";

    private static final String PORT_NAME = "Parameter";

    /**
	 * The description.
	 */
    private static final String PORT_DESCRIPTION = "This port can be connected to any type.";

    private String rate;

    private String streamName;

    /**
	 * Creates an InputComponent.
	 */
    public StreamSourceComponent() {
        super();
        setName(NAME);
        setDescription(DESCRIPTION);
    }

    /**
	 * Constructs a StreamSourceComponent.
	 * 
	 * @param stream
	 * @param rate
	 */
    public StreamSourceComponent(String stream, String rate) {
        setName(NAME + ":" + stream);
        this.streamName = stream;
        this.rate = rate;
        SystemComponentDataPort port = new SystemComponentDataPort(stream);
        port.setDescription(PORT_DESCRIPTION);
        this.outputs.add(port);
    }

    /**
	 * @see edu.indiana.extreme.xbaya.component.Component#createNode(edu.indiana.extreme.xbaya.graph.Graph)
	 */
    @Override
    public StreamSourceNode createNode(Graph graph) {
        StreamSourceNode node = new StreamSourceNode(graph);
        if (this.streamName != null) {
            node.setName(NAME + "_" + this.streamName);
        } else {
            node.setName(NAME);
        }
        node.setComponent(this);
        node.createID();
        createPorts(node);
        return node;
    }
}
