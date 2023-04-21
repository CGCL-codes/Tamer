package org.plazmaforge.studio.dbdesigner.policies;

import org.eclipse.gef.requests.BendpointRequest;

public class ERBendpointRequest extends BendpointRequest {

    public ERBendpointRequest() {
    }

    public boolean isCtrlPressed() {
        return ctrlPressed;
    }

    public void setCtrlPressed(boolean flag) {
        ctrlPressed = flag;
    }

    private boolean ctrlPressed;
}
