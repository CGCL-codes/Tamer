package net.sourceforge.squirrel_sql.plugins.dbdiff.gui;

import java.io.IOException;
import net.sourceforge.squirrel_sql.fw.util.log.ILogger;
import net.sourceforge.squirrel_sql.fw.util.log.LoggerController;

/**
 * A DiffPresentation implementation that creates a script from both schemas to be compared, then launches a
 * diff window or configurable external diff tool to compare the schema definitions side-by-side.
 */
public class ExternalToolSideBySideDiffPresentation extends AbstractSideBySideDiffPresentation {

    /** Logger for this class. */
    private static final ILogger s_log = LoggerController.createLogger(ExternalToolSideBySideDiffPresentation.class);

    /**
	 * @see net.sourceforge.squirrel_sql.plugins.dbdiff.gui.AbstractSideBySideDiffPresentation#
	 *      executeDiff(java.lang.String, java.lang.String)
	 */
    @Override
    protected void executeDiff(String script1Filename, String script2Filename) throws IOException {
        final String toolCommand = preferenceBean.getGraphicalToolCommand() + " " + script1Filename + " " + script2Filename;
        if (s_log.isInfoEnabled()) {
            s_log.info("Launching external diff tool with the following command: " + toolCommand);
        }
        Runtime.getRuntime().exec(toolCommand);
    }
}
