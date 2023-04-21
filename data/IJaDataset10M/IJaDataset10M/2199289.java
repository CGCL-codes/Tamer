package com.ohua.checkpoint.framework.operatorcheckpoints;

import com.ohua.engine.exceptions.Assertion;
import com.ohua.engine.flowgraph.elements.operator.PortID;

public class ProcessControllerCheckpoint extends AbstractCheckPoint {

    @Override
    public final int getOperatorLevel() {
        return 0;
    }

    @Override
    public final int getPortLevel(PortID id) {
        Assertion.impossible();
        return -1;
    }
}
