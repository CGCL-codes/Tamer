package net.sourceforge.cruisecontrol.builders;

import java.io.File;
import java.util.Map;
import net.sourceforge.cruisecontrol.Builder;
import net.sourceforge.cruisecontrol.CruiseControlException;
import net.sourceforge.cruisecontrol.util.Util;
import net.sourceforge.cruisecontrol.util.DateUtil;
import net.sourceforge.cruisecontrol.util.ValidationHelper;
import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * Rake builder class based on the Ant and Exec builder classes.
 * <br />
 * Attempts to mimic the behavior of Ant builds using Ruby Rake.
 *
 *
 * @author Kirk Knoernschild - Provided without any warranty
 */
public class RakeBuilder extends Builder {

    private static final Logger LOG = Logger.getLogger(RakeBuilder.class);

    private String workingDir = null;

    private String buildFile = "rakefile.rb";

    private String target = "";

    private String args;

    private long timeout = ScriptRunner.NO_TIMEOUT;

    private boolean wasValidated = false;

    public void validate() throws CruiseControlException {
        super.validate();
        ValidationHelper.assertIsSet(buildFile, "buildfile", this.getClass());
        ValidationHelper.assertIsSet(target, "target", this.getClass());
        wasValidated = true;
    }

    /**
     * build and return the results via xml.  debug status can be determined
     * from log4j category once we get all the logging in place.
     */
    public Element build(Map buildProperties) throws CruiseControlException {
        if (!wasValidated) {
            throw new IllegalStateException("This builder was never validated." + " The build method should not be getting called.");
        }
        validateBuildFileExists();
        Element buildLogElement = new Element("build");
        RakeScript script = this.getRakeScript();
        script.setBuildLogHeader(buildLogElement);
        script.setWindows(Util.isWindows());
        script.setArgs(args);
        script.setBuildFile(buildFile);
        script.setTarget(target);
        long startTime = System.currentTimeMillis();
        File workDir = workingDir != null ? new File(workingDir) : null;
        boolean scriptCompleted = false;
        scriptCompleted = new ScriptRunner().runScript(workDir, script, timeout);
        long endTime = System.currentTimeMillis();
        if (!scriptCompleted) {
            LOG.warn("Build timeout timer of " + timeout + " seconds has expired");
            buildLogElement = new Element("build");
            buildLogElement.setAttribute("error", "build timeout");
        } else if (script.getExitCode() != 0) {
            synchronized (buildLogElement) {
                buildLogElement.setAttribute("error", "Return code is " + script.getExitCode());
            }
        }
        buildLogElement.setAttribute("time", DateUtil.getDurationAsString((endTime - startTime)));
        return buildLogElement;
    }

    public Element buildWithTarget(Map properties, String buildTarget) throws CruiseControlException {
        String origTarget = target;
        try {
            target = buildTarget;
            return build(properties);
        } finally {
            target = origTarget;
        }
    }

    void validateBuildFileExists() throws CruiseControlException {
        File build = new File(buildFile);
        if (!build.isAbsolute() && workingDir != null) {
            build = new File(workingDir, buildFile);
        }
        ValidationHelper.assertExists(build, "buildfile", this.getClass());
    }

    /**
     * Set the working directory where Rake will be invoked. This parameter gets
     * set in the XML file via the workingDir attribute. The directory can
     * be relative (to the cruisecontrol current working directory) or absolute.
     *
     * @param dir
     *          the directory to make the current working directory.
     */
    public void setWorkingDir(String dir) {
        workingDir = dir;
    }

    /**
     * Set the Rake target(s) to invoke.
     *
     * @param target the target(s) name.
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * Sets the name of the build file that Rake will use.  The Rake default is
     * rakefile or Rakefile. f the rakefile is not found in the current directory,
     * rake will search parent directories for a match. The directory where the
     * Rakefile is found will become the current directory for the actions executed
     * in the Rakefile. Use this to set the rakefile for the build.
     *
     * @param buildFile the name of the build file.
     */
    public void setBuildFile(String buildFile) {
        this.buildFile = buildFile;
    }

    /**
     * @param timeout The timeout to set.
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    protected RakeScript getRakeScript() {
        return new RakeScript();
    }
}
