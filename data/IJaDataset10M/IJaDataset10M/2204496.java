package com.googlecode.wargo;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import java.io.File;

public class WarGoAntTask extends Task {

    private final WarGo warGo = new WarGo() {

        protected void writeLogMessage(String message) {
            WarGoAntTask.this.log(message);
        }
    };

    public void setWar(File war) {
        this.warGo.setWar(war);
    }

    public void setOut(File out) {
        this.warGo.setOut(out);
    }

    public void setProvider(String provider) {
        this.warGo.setProvider(provider);
    }

    public void setVerbose(boolean verbose) {
        this.warGo.setVerbose(verbose);
    }

    public void setDebug(boolean debug) {
        this.warGo.setDebug(debug);
    }

    public void execute() throws BuildException {
        try {
            this.warGo.execute();
        } catch (RuntimeException e) {
            throw new BuildException(e);
        }
    }
}
