package com.koutra.dist.proc.designer.editor.parts.pipeline;

import com.koutra.dist.proc.designer.editor.parts.ProcessStepTreeEditPart;
import com.koutra.dist.proc.designer.model.pipeline.ReaderDemuxPipelineItem;

public class ReaderDemuxPipelineItemTreeEditPart extends ProcessStepTreeEditPart {

    public ReaderDemuxPipelineItemTreeEditPart(ReaderDemuxPipelineItem pipelineItem) {
        setModel(pipelineItem);
    }
}
