package org.nakedobjects.plugins.dnd.viewer.view.help;

import java.io.IOException;
import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.plugins.dnd.HelpViewer;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;

public class ExternalHelpViewerProgram implements HelpViewer {

    private static final Logger LOG = Logger.getLogger(ExternalHelpViewerProgram.class);

    private final String program;

    public ExternalHelpViewerProgram(final String program) {
        this.program = program;
    }

    public void open(final Location location, final String name, final String description, final String help) {
        final String exec = program + " " + help;
        try {
            Runtime.getRuntime().exec(exec);
            LOG.debug("executing '" + exec + "'");
        } catch (final IOException e) {
            throw new NakedObjectException("faile to execute '" + exec + "'", e);
        }
    }
}
