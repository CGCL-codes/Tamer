package com.google.code.insect.workflow.impl;

import com.google.code.insect.workflow.Transition;
import com.google.code.insect.workflow.comm.TransitionType;

public class OrSplitTransition extends Transition {

    public OrSplitTransition() {
        super();
        this.type = TransitionType.OR_SPLIT;
    }

    public OrSplitTransition(long id) {
        super(id);
        this.type = TransitionType.OR_SPLIT;
    }

    public OrSplitTransition(long id, String name) {
        super(id, name);
        this.type = TransitionType.OR_SPLIT;
    }
}
