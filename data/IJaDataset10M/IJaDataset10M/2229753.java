package com.koutra.dist.proc.designer.editor.parts.pipeline;

import com.koutra.dist.proc.designer.editor.parts.ProcessStepTreeEditPart;
import com.koutra.dist.proc.designer.model.pipeline.ResultSetToStreamPipelineItem;

public class ResultSetToStreamPipelineItemTreeEditPart extends ProcessStepTreeEditPart {

    public ResultSetToStreamPipelineItemTreeEditPart(ResultSetToStreamPipelineItem pipelineItem) {
        setModel(pipelineItem);
    }
}
