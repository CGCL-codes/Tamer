package org.nextframework.authorization.process;

import org.nextframework.authorization.Authorization;

public class ProcessAuthorization implements Authorization {

    protected boolean canExecute;

    public boolean canGenerate() {
        return canExecute;
    }

    public void setCanExecute(boolean canGenerate) {
        this.canExecute = canGenerate;
    }
}
