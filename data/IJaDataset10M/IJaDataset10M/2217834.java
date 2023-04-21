package com.rapidminer.operator.ports;

import java.util.List;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.ResultObject;
import com.rapidminer.operator.ports.metadata.MDTransformationRule;
import com.rapidminer.operator.ports.metadata.MetaData;
import com.rapidminer.operator.ports.metadata.OneToManyPassThroughRule;

/** Does the same as its superclass but provides also a method to generate
 *  a meta data transformation rule that copies from an input to all
 *  generated output ports.
 * 
 *  @author Simon Fischer 
 * 
 */
public class OutputPortExtender extends SinglePortExtender<OutputPort> {

    public OutputPortExtender(String name, Ports<OutputPort> ports) {
        super(name, ports);
    }

    /** The generated rule copies all meta data from the input port to all generated output ports. */
    public MDTransformationRule makePassThroughRule(InputPort inputPort) {
        return new OneToManyPassThroughRule(inputPort, getManagedPorts());
    }

    public void deliverToAll(IOObject data, boolean clone) {
        for (OutputPort port : getManagedPorts()) {
            if (clone) {
                port.deliver(data.copy());
            } else {
                port.deliver(data);
            }
        }
    }

    public void deliver(List<? extends IOObject> inputs) {
        int i = 0;
        for (OutputPort port : getManagedPorts()) {
            if (port.isConnected()) {
                if (i >= inputs.size()) {
                    getPorts().getOwner().getOperator().getLogger().fine("Insufficient input for " + port.getSpec());
                } else {
                    IOObject input = inputs.get(i);
                    port.deliver(input);
                    String name;
                    if (input instanceof ResultObject) {
                        name = ((ResultObject) input).getName();
                    } else {
                        name = input.getClass().getName();
                    }
                    name += " (" + input.getSource() + ")";
                    getPorts().getOwner().getOperator().getLogger().fine("Delivering " + name + " to " + port.getSpec());
                }
                i++;
            }
        }
    }

    public void deliverMetaData(List<MetaData> inputMD) {
        int i = 0;
        for (OutputPort port : getManagedPorts()) {
            if (port.isConnected()) {
                if (i < inputMD.size()) {
                    port.deliverMD(inputMD.get(i));
                }
                i++;
            }
        }
    }
}
