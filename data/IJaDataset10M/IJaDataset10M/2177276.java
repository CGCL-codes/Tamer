package protoj.system;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import org.apache.tools.ant.taskdefs.Jar;
import protoj.shell.ProjectLayout;
import protoj.system.internal.ant.AssembleTask;

/**
 * Provides support for creating and otherwise manipulating one or more project
 * jar files.
 * 
 * @author Ashley Williams
 * 
 */
public final class AssembleFeature {

    /**
	 * See {@link #getAssembleTasks()}.
	 */
    private TreeMap<String, AssembleTask> assembleTasks = new TreeMap<String, AssembleTask>();

    private AssembleTask recentTask;

    private final StandardProject project;

    /**
	 * Creates with the parent project.
	 * 
	 * @param project
	 */
    public AssembleFeature(StandardProject project) {
        this.project = project;
    }

    /**
	 * This is the simplest way to add an assembly and uses a default manifest
	 * and includes all the compiled <code>.class</code> files.
	 * 
	 * @param jarName
	 * @return
	 */
    public AssembleTask addJar(String jarName) {
        return addJar(jarName, null, null);
    }

    /**
	 * By default no jar files will be created when calling {@link #assemble}.
	 * Call this repeatedly in order to specify one or more jar files to be
	 * created. See {@link ProjectLayout} to determine the directories used for
	 * the various file types.
	 * 
	 * @param project
	 *            the parent project
	 * @param jarName
	 *            the name of the jar file to be created, without the extension
	 * @param manifestName
	 *            the name of the manifest to be used, without the extension,
	 *            can be null if a default manifest is required
	 * @param includes
	 *            the ant pattern that specifies the <code>.class</code> and
	 *            resource files to be included in the jar file, can be null to
	 *            specify all files.
	 * @return
	 */
    public AssembleTask addJar(String jarName, String manifestName, String includes) {
        ProjectLayout layout = project.getLayout();
        File destFile = layout.getJar(jarName);
        File classesDir = layout.getClassesDir();
        File manifestFile = manifestName == null ? null : layout.getManifest(manifestName);
        recentTask = new AssembleTask(destFile, classesDir, manifestFile, includes);
        recentTask.initLogging(layout.getLogFile());
        assembleTasks.put(jarName, recentTask);
        return recentTask;
    }

    /**
	 * Convenience method that delegates to
	 * {@link #initExecutableJar(String, String)}, but using the current thread
	 * main class by default.
	 * 
	 * @param jarName
	 */
    public void initExecutableJar(String jarName) {
        initExecutableJar(jarName, project.getCurrentMainClass());
    }

    /**
	 * Ensure the jar with the given name is created as an executable jar with
	 * the specified main class name. The jar files in the lib directory,
	 * excluding the jar file being configured here, will be used to form the
	 * Class-Path attribute.
	 * 
	 * @param jarName
	 * @param mainClass
	 */
    public void initExecutableJar(String jarName, String mainClass) {
        ProjectLayout layout = project.getLayout();
        StringBuilder classPath = new StringBuilder();
        String[] libFiles = layout.getLibDir().list();
        for (String libFile : libFiles) {
            if (!libFile.equals(jarName) && libFile.contains(".jar")) {
                classPath.append(libFile);
                classPath.append(" ");
            }
        }
        AssembleTask assembleTask = assembleTasks.get(jarName);
        assembleTask.initManifest("Main-Class", mainClass);
        assembleTask.initManifest("Class-Path", classPath.toString());
    }

    /**
	 * Responsible for creating one or more jar files from the compiled source
	 * code. The number of jar files and their content can be configured by
	 * calls to {@link #initAssembly(String, String, String)}.
	 */
    public void assemble() {
        Collection<AssembleTask> tasks = assembleTasks.values();
        for (AssembleTask task : tasks) {
            task.execute();
        }
    }

    /**
	 * If further configuration of the jar file to be created is required, then
	 * use this method to reference the appropriate ant helper.
	 * 
	 * @param jarName
	 *            the name of the jar file, without the extension, whose helper
	 *            is required
	 * @return
	 */
    public Jar getAntJar(String jarName) {
        return assembleTasks.get(jarName).getJar();
    }

    /**
	 * The number of jars this features is configured to create.
	 * 
	 * @return
	 */
    public int getJarCount() {
        return assembleTasks.size();
    }

    /**
	 * Returns the most recently added assemble task. Useful is there is just
	 * one entry.
	 * 
	 * @return
	 */
    public AssembleTask getRecentTask() {
        return recentTask;
    }

    /**
	 * Obtains a reference to all of the assemblies for further configuration.
	 * 
	 * @return
	 */
    public Map<String, AssembleTask> getAssembleTasks() {
        return Collections.unmodifiableMap(assembleTasks);
    }
}
