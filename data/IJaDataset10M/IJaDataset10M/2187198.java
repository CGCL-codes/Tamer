package q_impress.pmi.lib.jmt.xml;

import org.w3c.dom.Element;

/**
 * A sink in the queue network.
 * @author Mauro Luigi Drago
 *
 */
public class Sink extends QueueElement {

    private static final String SINK_NAME = "sink";

    /** The XML element representing the sink */
    private Element sinkElement = null;

    /**
	 * Default constructor.
	 * @param network the queue network.
	 */
    public Sink(QueueNetwork network) {
        super(SINK_NAME, network);
        sinkElement = network.getXmlDoc().createElement("node");
        sinkElement.setAttribute("name", SINK_NAME);
        Element section = network.getXmlDoc().createElement("section");
        sinkElement.appendChild(section);
        section.setAttribute("className", "JobSink");
        network.getSimulationElement().appendChild(sinkElement);
    }
}
