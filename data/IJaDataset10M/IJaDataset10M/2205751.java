package avisync.node.application;

import avisync.AVIException;
import avisync.model.AVIContext;
import avisync.node.AVINode;

public abstract class AVIApplication extends AVINode {

    public AVIApplication() {
        setContext(new AVIContext());
    }

    public void run() throws AVIException {
        throw new AVIException();
    }
}
