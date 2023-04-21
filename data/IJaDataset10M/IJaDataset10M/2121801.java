package com.koutra.dist.proc.designer.editor.parts.pipeline;

import com.koutra.dist.proc.designer.editor.parts.SplitProcessStepEditPart;
import com.koutra.dist.proc.designer.model.pipeline.SplitReaderPipelineItem;

public class SplitReaderPipelineItemEditPart extends SplitProcessStepEditPart {

    public SplitReaderPipelineItemEditPart(SplitReaderPipelineItem pipelineItem) {
        setModel(pipelineItem);
    }
}
