package org.nakedobjects.viewer.skylark;

import org.nakedobjects.utility.NakedObjectRuntimeException;
import java.io.IOException;
import org.apache.log4j.Logger;

public class ExternalHelpViewerProgram implements HelpViewer {

    private static final Logger LOG = Logger.getLogger(ExternalHelpViewerProgram.class);

    private final String program;

    public ExternalHelpViewerProgram(String program) {
        this.program = program;
    }

    public void open(Location location, String name, String description, String help) {
        String exec = program + " " + help;
        try {
            Runtime.getRuntime().exec(exec);
            LOG.debug("executing '" + exec + "'");
        } catch (IOException e) {
            throw new NakedObjectRuntimeException("faile to execute '" + exec + "'", e);
        }
    }
}
