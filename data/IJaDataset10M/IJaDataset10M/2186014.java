package edu.indiana.extreme.xbaya.graph.system;

import edu.indiana.extreme.xbaya.XBayaRuntimeException;
import edu.indiana.extreme.xbaya.component.ComponentDataPort;
import edu.indiana.extreme.xbaya.component.system.ForEachComponent;
import edu.indiana.extreme.xbaya.graph.*;
import edu.indiana.extreme.xbaya.graph.Port.Kind;
import edu.indiana.extreme.xbaya.graph.gui.NodeGUI;
import edu.indiana.extreme.xbaya.graph.system.gui.ForEachNodeGUI;
import edu.indiana.extreme.xbaya.util.WSConstants;
import org.xmlpull.infoset.XmlElement;
import javax.xml.namespace.QName;
import java.util.List;

/**
 * @author Satoshi Shirasuna
 */
public class ForEachNode extends SystemNode {

    private ForEachNodeGUI gui;

    /**
     * Creates a InputNode.
     * 
     * @param graph
     */
    public ForEachNode(Graph graph) {
        super(graph);
    }

    /**
     * Constructs a InputNode.
     * 
     * @param nodeElement
     * @throws GraphException
     */
    public ForEachNode(XmlElement nodeElement) throws GraphException {
        super(nodeElement);
    }

    /**
     * @see edu.indiana.extreme.xbaya.graph.Node#getGUI()
     */
    public NodeGUI getGUI() {
        if (this.gui == null) {
            this.gui = new ForEachNodeGUI(this);
        }
        return this.gui;
    }

    /**
     * @see edu.indiana.extreme.xbaya.graph.impl.NodeImpl#getComponent()
     */
    @Override
    public ForEachComponent getComponent() {
        ForEachComponent component = (ForEachComponent) super.getComponent();
        if (component == null) {
            component = new ForEachComponent();
            setComponent(component);
        }
        return component;
    }

    /**
     * Adds additional input port.
     */
    public void addInputPort() {
        ForEachComponent component = getComponent();
        ComponentDataPort input = component.getInputPort();
        DataPort port = input.createPort();
        addInputPort(port);
    }

    /**
     * Removes the last input port.
     * 
     * @throws GraphException
     */
    public void removeInputPort() throws GraphException {
        List<DataPort> inputPorts = getInputPorts();
        DataPort inputPort = inputPorts.get(inputPorts.size() - 1);
        removeInputPort(inputPort);
    }

    /**
     * Adds additional output port.
     */
    public void addOutputPort() {
        ForEachComponent component = getComponent();
        ComponentDataPort outputPort = component.getOutputPort();
        DataPort port = outputPort.createPort();
        addOutputPort(port);
    }

    /**
     * Removes the last output port.
     * 
     * @throws GraphException
     */
    public void removeOutputPort() throws GraphException {
        List<DataPort> outputPorts = getOutputPorts();
        DataPort outputPort = outputPorts.get(outputPorts.size() - 1);
        removeOutputPort(outputPort);
    }

    /**
     * @throws GraphException
     * @see edu.indiana.extreme.xbaya.graph.impl.NodeImpl#edgeWasAdded(edu.indiana.extreme.xbaya.graph.Edge)
     */
    @Override
    protected void edgeWasAdded(Edge edge) throws GraphException {
        Port fromPort = edge.getFromPort();
        Port toPort = edge.getToPort();
        if (edge instanceof DataEdge) {
            if (fromPort instanceof EPRPort) {
                return;
            }
            DataPort fromDataPort = (DataPort) fromPort;
            DataPort toDataPort = (DataPort) toPort;
            QName fromType = fromDataPort.getType();
            QName toType = toDataPort.getType();
            if (fromDataPort.getNode() == this) {
                if (!(toType == null || toType.equals(WSConstants.XSD_ANY_TYPE))) {
                    fromDataPort.copyType(toDataPort);
                }
            } else if (toDataPort.getNode() == this) {
                if (!(fromType == null || fromType.equals(WSConstants.XSD_ANY_TYPE))) {
                    toDataPort.copyType(fromDataPort);
                }
            } else {
                throw new XBayaRuntimeException();
            }
        }
    }

    /**
     * @see edu.indiana.extreme.xbaya.graph.system.SystemNode#portTypeChanged(edu.indiana.extreme.xbaya.graph.system.SystemDataPort)
     */
    @Override
    protected void portTypeChanged(SystemDataPort port) throws GraphException {
        super.portTypeChanged(port);
        List<DataPort> inputPorts = getInputPorts();
        List<DataPort> outputPorts = getOutputPorts();
        Kind kind = port.getKind();
        int index;
        if (kind == Kind.DATA_IN) {
            index = inputPorts.indexOf(port);
        } else if (kind == Kind.DATA_OUT) {
            index = outputPorts.indexOf(port);
        } else {
            throw new XBayaRuntimeException();
        }
        SystemDataPort inputPort = (SystemDataPort) inputPorts.get(index);
        SystemDataPort outputPort = (SystemDataPort) outputPorts.get(index);
        QName inputType = inputPort.getType();
        QName outputType = outputPort.getType();
        QName portType = port.getType();
        if (portType == null || portType.equals(WSConstants.XSD_ANY_TYPE)) {
            return;
        }
        if (port == inputPort) {
            if (outputType.equals(WSConstants.XSD_ANY_TYPE)) {
                outputPort.copyType(port, -1);
            } else if (outputType.equals(portType)) {
            } else {
            }
        } else if (port == outputPort) {
            if (inputType.equals(WSConstants.XSD_ANY_TYPE)) {
                inputPort.copyType(port, 1);
            } else if (inputType.equals(portType)) {
            } else {
            }
        } else {
            throw new XBayaRuntimeException();
        }
    }

    @Override
    protected void parseConfiguration(XmlElement configElement) {
        super.parseConfiguration(configElement);
    }

    @Override
    protected XmlElement toXML() {
        XmlElement nodeElement = super.toXML();
        nodeElement.setAttributeValue(GraphSchema.NS, GraphSchema.NODE_TYPE_ATTRIBUTE, GraphSchema.NODE_TYPE_SPLIT);
        return nodeElement;
    }

    @Override
    protected XmlElement addConfigurationElement(XmlElement nodeElement) {
        XmlElement configElement = nodeElement.addElement(GraphSchema.NS, GraphSchema.NODE_CONFIG_TAG);
        return configElement;
    }
}
