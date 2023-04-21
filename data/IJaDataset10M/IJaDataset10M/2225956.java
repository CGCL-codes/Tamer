package com.koutra.dist.proc.designer.model.pipeline;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import com.koutra.dist.proc.designer.model.Connection;
import com.koutra.dist.proc.designer.model.ContentType;
import com.koutra.dist.proc.designer.model.ModelElement;
import com.koutra.dist.proc.designer.model.SplitPipelineItem;
import com.koutra.dist.proc.designer.model.ValidationError;
import com.koutra.dist.proc.model.ISink;

public class SplitStreamPipelineItem extends SplitPipelineItem {

    @Override
    public ModelElement deepCopy() {
        SplitStreamPipelineItem retVal = new SplitStreamPipelineItem();
        return retVal;
    }

    @Override
    public boolean supportsInput(ContentType contentType) {
        switch(contentType) {
            case ByteStream:
                return true;
        }
        return false;
    }

    @Override
    public boolean supportsOutput(ContentType contentType) {
        switch(contentType) {
            case ByteStream:
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "SplitStream";
    }

    protected com.koutra.dist.proc.pipeline.SplitStreamPipelineItem representation;

    @Override
    public void setUpExecution() {
        representation = new com.koutra.dist.proc.pipeline.SplitStreamPipelineItem(UUID.randomUUID().toString(), getSinkConnectionsSize());
        int index = 0;
        for (Connection connection : sinkConnections) {
            ModelElement sink = (ModelElement) connection.getSink();
            sink.setUpExecution();
            ISink sinkRepresentation = (ISink) sink.getExecutionRepresentation();
            representation.hookupSink(index++, sinkRepresentation);
        }
    }

    @Override
    public Object getExecutionRepresentation() {
        if (representation == null) setUpExecution();
        return representation;
    }

    @Override
    public List<ValidationError> validate() {
        return Collections.emptyList();
    }
}
