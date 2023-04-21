package com.koutra.dist.proc.designer.model.template;

import java.util.Collections;
import java.util.List;
import com.koutra.dist.proc.designer.model.Connection;
import com.koutra.dist.proc.designer.model.ContentType;
import com.koutra.dist.proc.designer.model.ModelElement;
import com.koutra.dist.proc.designer.model.Template;
import com.koutra.dist.proc.designer.model.ValidationError;
import com.koutra.dist.proc.model.ISink;
import com.koutra.dist.proc.pipeline.template.IdentityStreamToStreamPipelineItemTemplate;

public class IdentityStreamTemplate extends Template {

    public IdentityStreamTemplate() {
    }

    @Override
    public ModelElement deepCopy() {
        IdentityStreamTemplate retVal = new IdentityStreamTemplate();
        if (sinkConnection != null && sinkConnection.getSink() != null) {
            retVal.sinkConnection = (Connection) sinkConnection.deepCopy();
            retVal.sinkConnection.connect(retVal, sinkConnection.getSink());
        }
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
        return "IdentityStreamTemplate";
    }

    protected IdentityStreamToStreamPipelineItemTemplate representation;

    @Override
    public void setUpExecution() {
        representation = new IdentityStreamToStreamPipelineItemTemplate();
        ModelElement sink = (ModelElement) getSinkConnection().getSink();
        sink.setUpExecution();
        ISink sinkRepresentation = (ISink) sink.getExecutionRepresentation();
        representation.hookupSink(sinkRepresentation);
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
