package com.ohua.eai.resources;

import java.util.Map;
import com.ohua.engine.AbstractExternalActivator.ManagerProxy;
import com.ohua.engine.flowgraph.elements.operator.OperatorID;
import com.ohua.engine.resource.management.AbstractCPResource;
import com.ohua.engine.resource.management.CheckpointResourceAccess;

public abstract class AbstractJMSResource extends AbstractCPResource {

    public AbstractJMSResource(ManagerProxy ohuaProcess, Map<String, String> attributes) {
        super(ohuaProcess, attributes);
    }

    @Override
    public CheckpointResourceAccess getCheckpointResourceAccess(OperatorID operatorID, String opChckPtArtifactID) {
        return new JMSResourceAccess(operatorID, opChckPtArtifactID);
    }

    public abstract void deleteDestination(String cpArtifactID);
}
