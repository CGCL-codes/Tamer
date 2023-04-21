package it.freax.fpm.core.solver.conf;

import it.freax.fpm.core.types.ExitCodeControl;
import it.freax.fpm.core.types.RootExecution;

public class General {

    private RootExecution rootExec;

    private ExitCodeControl exitCodeControl;

    public RootExecution getRootExec() {
        return this.rootExec;
    }

    public void setRootExec(RootExecution rootExec) {
        this.rootExec = rootExec;
    }

    public ExitCodeControl getExitCodeControl() {
        return this.exitCodeControl;
    }

    public void setExitCodeControl(ExitCodeControl exitCodeControl) {
        this.exitCodeControl = exitCodeControl;
    }
}
