package org.wsmostudio.bpmo.model.merge;

import org.wsmostudio.bpmo.model.WorkflowEntityNode;

public class SimpleMergeNode extends MergeNode {

    public WorkflowEntityNode cloneNode() {
        SimpleMergeNode clone = new SimpleMergeNode();
        cloneProperties(clone);
        return clone;
    }
}
