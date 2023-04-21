package protoj.lang.internal.ant;

import java.io.File;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Jar;
import org.apache.tools.ant.taskdefs.Manifest;
import org.apache.tools.ant.taskdefs.Manifest.Attribute;
import protoj.core.internal.AntTarget;

/**
 * A convenience class for creating a jar file. Use the constructors to specify
 * the minimal and most widely anticipated configuration and the
 * <code>initXXX</code> methods for the less common configuration options.
 * 
 * @author Ashley Williams
 * 
 */
public final class AssembleTask {

    private AntTarget target;

    private Jar jar;

    private Manifest manifest;

    /**
	 * Accepts information widely used for configuring jar files.
	 * 
	 * @param destFile
	 *            the JAR file to create.
	 * @param basedir
	 *            the directory from which to jar the files.
	 * @param manifest
	 *            the manifest file to use. When omitted The ant API will create
	 *            one.
	 * @param includes
	 *            comma- or space-separated list of patterns of files that must
	 *            be included. All files are included when omitted.
	 * @param excludes
	 *            comma- or space-separated list of patterns of files that must
	 *            be excluded. All files are excluded when omitted.
	 */
    public AssembleTask(File destFile, File basedir, File manifest, String includes, String excludes) {
        target = new AntTarget("protoj-archive");
        jar = new Jar();
        target.addTask(jar);
        jar.setTaskName("archive");
        jar.setDestFile(destFile);
        jar.setBasedir(basedir);
        if (manifest != null) {
            jar.setManifest(manifest);
        }
        if (includes != null) {
            jar.setIncludes(includes);
        }
        if (excludes != null) {
            jar.setIncludes(excludes);
        }
    }

    /**
	 * Enables logging to the specified log file at Project.MSG_INFO level.
	 * 
	 * @param logFile
	 */
    public void initLogging(File logFile) {
        target.initLogging(logFile, Project.MSG_INFO);
    }

    /**
	 * Call repeatedly in order to add a top level attribute to the manifest.
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
    public Attribute initManifest(String name, String value) {
        manifest = createManifest();
        Attribute attribute = new Manifest.Attribute(name, value);
        manifest.addConfiguredAttribute(attribute);
        return attribute;
    }

    public Jar getJar() {
        return jar;
    }

    public void execute() {
        target.execute();
    }

    /**
	 * Lazily creates a manifest instance and adds to the jar task.
	 * 
	 * @return
	 */
    private Manifest createManifest() {
        if (manifest == null) {
            manifest = new Manifest();
            jar.addConfiguredManifest(manifest);
        }
        return manifest;
    }
}
