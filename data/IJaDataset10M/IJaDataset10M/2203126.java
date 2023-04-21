package org.modcal;

import java.util.TimerTask;

public class KillHydrus1D extends TimerTask {

    private final Process h1dProcess;

    private Boolean didIt;

    public KillHydrus1D(Process h1dProcess) {
        this.h1dProcess = h1dProcess;
        this.didIt = false;
    }

    public void run() {
        h1dProcess.destroy();
        didIt = true;
    }

    public boolean killed() {
        return this.didIt;
    }
}
