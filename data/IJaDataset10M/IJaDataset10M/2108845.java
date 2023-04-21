package org.deri.wsml.eclipse.visualizer.graph.node.ontology.parameter;

import net.sourceforge.jpowergraph.DefaultNode;
import net.sourceforge.powerswing.localization.PBundle;
import org.omwg.ontology.Parameter;

/**
 * A node in the OI-model graph representing a concept.
 */
public class ParameterNode extends DefaultNode {

    private Parameter parameter;

    private int position;

    private PBundle messages;

    public ParameterNode(Parameter theParameter, int thePosition, PBundle theMessages) {
        super();
        this.parameter = theParameter;
        this.position = thePosition;
        this.messages = theMessages;
    }

    public String getLabel() {
        return messages.format("ParameterNode.Parameter", new Object[] { position });
    }

    public Parameter getParameter() {
        return parameter;
    }

    public Integer getPosition() {
        return position;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ParameterNode)) {
            return false;
        }
        return ((ParameterNode) obj).getParameter().equals(getParameter()) && ((ParameterNode) obj).getPosition().equals(getPosition());
    }

    public int hashCode() {
        return parameter.hashCode();
    }

    public String getNodeType() {
        return messages.getString("ParameterNode.NodeType");
    }
}
