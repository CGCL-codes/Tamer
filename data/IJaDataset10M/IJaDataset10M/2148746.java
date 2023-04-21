package com.mindtree.techworks.insight.releng.mvn.nsis.actions;

import java.io.File;
import java.io.IOException;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.DefaultConsumer;

/**
 * Compiles the generated NSIS script.
 *
 * @author <a href="mailto:bindul_bhowmik@mindtree.com">Bindul Bhowmik</a>
 * @version $Revision: 97 $ $Date: 2008-01-08 02:47:32 -0500 (Tue, 08 Jan 2008) $
 * @plexus.component role="com.mindtree.techworks.insight.releng.mvn.nsis.actions.NsisAction"
 *                   role-hint="compile"
 */
public class NsisCompileAction implements NsisAction, NsisScriptConstants {

    /**
	 * The make NSIS file
	 */
    private static final String MAKE_NSIS_FILE = "makensis.exe";

    public void execute(MojoInfo mojoInfo) throws NsisActionExecutionException {
        int logVerbosity = 0;
        Log log = mojoInfo.getLog();
        if (log.isErrorEnabled()) {
            logVerbosity = 1;
        }
        if (log.isWarnEnabled()) {
            logVerbosity = 2;
        }
        if (log.isInfoEnabled()) {
            logVerbosity = 3;
        }
        if (log.isDebugEnabled()) {
            logVerbosity = 4;
        }
        Commandline commandline = new Commandline();
        commandline.setExecutable(getNsisExecutablePath(mojoInfo));
        commandline.createArg().setValue("/V" + logVerbosity);
        commandline.createArg().setValue(OP_FILE_SETUP_MUI);
        commandline.setWorkingDirectory(mojoInfo.getWorkDirectory());
        log.info("About to execute: " + commandline.toString());
        try {
            int response = CommandLineUtils.executeCommandLine(commandline, new DefaultConsumer(), new DefaultConsumer());
            if (response != 0) {
                throw new NsisActionExecutionException("NSIS Compiler returned error: " + response);
            }
            File outputFile = new File(mojoInfo.getWorkDirectory(), mojoInfo.getNsisProject().getInstallerSettings().getOutFile());
            FileUtils.copyFileToDirectory(outputFile, new File(mojoInfo.getProject().getBuild().getDirectory()));
            mojoInfo.getProjectHelper().attachArtifact(mojoInfo.getProject(), "exe", "setup", new File(mojoInfo.getProject().getBuild().getDirectory(), outputFile.getName()));
        } catch (CommandLineException e) {
            log.error("Executable failed", e);
            throw new NsisActionExecutionException("Executable failed", e);
        } catch (IOException e) {
            log.error("Error copying final output", e);
            throw new NsisActionExecutionException("Error copying final output", e);
        }
    }

    /**
	 * Gets the executable path
	 */
    private String getNsisExecutablePath(MojoInfo mojoInfo) throws NsisActionExecutionException {
        Log log = mojoInfo.getLog();
        if (null != mojoInfo.getNsisPath()) {
            File nsisExecPath = new File(mojoInfo.getNsisPath(), MAKE_NSIS_FILE);
            if (!nsisExecPath.exists()) {
                log.error("The NSIS executable is not available at the requested path. [" + mojoInfo.getNsisPath().getAbsolutePath() + "]");
                throw new NsisActionExecutionException("The NSIS executable is not available at the requested path. [" + mojoInfo.getNsisPath().getAbsolutePath() + "]");
            }
            return nsisExecPath.getAbsolutePath();
        }
        return MAKE_NSIS_FILE;
    }
}
