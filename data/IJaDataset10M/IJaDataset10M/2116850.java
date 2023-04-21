package net.kano.partypad.objects.converters;

import net.kano.partypad.TestingTools;
import net.kano.partypad.pipeline.AbstractPipelineObject;
import net.kano.partypad.pipeline.PipelineObjectInfo;
import net.kano.partypad.pipeline.ports.OutputPort;
import net.kano.partypad.pipeline.ports.SimpleOutputHolder;
import net.kano.partypad.pipeline.ports.SimpleInputHolder;

public class DummyPipelineObject extends AbstractPipelineObject {

    {
        setObjectInfo(TestingTools.createDummyObjectInfo());
    }

    public SimpleOutputHolder getOutputHolder() {
        return super.getOutputHolder();
    }

    public SimpleInputHolder getInputHolder() {
        return super.getInputHolder();
    }

    public void setObjectInfo(PipelineObjectInfo objectInfo) throws IllegalStateException {
        super.setObjectInfo(objectInfo);
    }

    public void setPort(OutputPort<?> port) {
        getOutputHolder().add(port);
    }
}
