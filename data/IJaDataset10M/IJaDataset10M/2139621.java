package com.koutra.dist.proc.designer.editor.parts.pipeline;

import com.koutra.dist.proc.designer.editor.parts.ProcessStepTreeEditPart;
import com.koutra.dist.proc.designer.model.pipeline.LineBasedDemuxPipelineItem;

public class LineBasedDemuxPipelineItemTreeEditPart extends ProcessStepTreeEditPart {

    public LineBasedDemuxPipelineItemTreeEditPart(LineBasedDemuxPipelineItem pipelineItem) {
        setModel(pipelineItem);
    }
}
