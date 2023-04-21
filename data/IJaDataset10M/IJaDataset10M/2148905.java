package com.rapidminer.operator.ports.metadata;

import java.util.List;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;

/** Passes meta data through and recursively unfolds element meta data if CollectionMetaData is found.
 * 
 * @author Simon Fischer
 *
 */
public class FlatteningPassThroughRule implements MDTransformationRule {

    private final List<InputPort> inputPorts;

    private final OutputPort outputPort;

    public FlatteningPassThroughRule(List<InputPort> inputPorts, OutputPort outputPort) {
        this.inputPorts = inputPorts;
        this.outputPort = outputPort;
    }

    @Override
    public void transformMD() {
        for (InputPort inputPort : inputPorts) {
            MetaData metaData = inputPort.getMetaData();
            if (metaData != null) {
                if (metaData instanceof CollectionMetaData) {
                    metaData = ((CollectionMetaData) metaData).getElementMetaDataRecursive();
                }
                metaData = metaData.clone();
                metaData.addToHistory(outputPort);
                outputPort.deliverMD(modifyMetaData(metaData));
                return;
            }
        }
        outputPort.deliverMD(null);
    }

    protected MetaData modifyMetaData(MetaData metaData) {
        return metaData;
    }
}
