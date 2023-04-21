package com.global360.sketchpadbpmn.documents;

import com.global360.sketchpadbpmn.WorkflowProcess;
import com.global360.sketchpadbpmn.documents.idmanager.BpmnId;

public abstract class CallImplementation extends SketchpadDatum {

    protected WorkflowProcess calledProcess = null;

    public CallImplementation() {
        super(null);
    }

    public CallImplementation(BPMNWorkflowDocument document) {
        super(document);
    }

    public WorkflowProcess getCalledProcess() {
        return calledProcess;
    }

    public void setCalledProcess(WorkflowProcess calledProcess) {
        this.calledProcess = calledProcess;
    }

    public BpmnId getCalledProcessId() {
        BpmnId result = null;
        if (this.calledProcess != null) result = this.calledProcess.getId();
        return result;
    }
}
