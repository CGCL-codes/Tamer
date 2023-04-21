package ch.elca.leaf.buildsystem.eclipse.propgen;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.ZipFileSet;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import ch.elca.leaf.buildsystem.buildgen.BuildGenerator;
import ch.elca.leaf.buildsystem.buildgen.ShortcutGenerator;
import ch.elca.leaf.buildsystem.eclipse.util.EclipsePluginHelper;
import ch.elca.leaf.buildsystem.eclipse.util.VelocityHelper;
import ch.elca.leaf.buildsystem.model.Attribute;
import ch.elca.leaf.buildsystem.model.ComponentResolver;
import ch.elca.leaf.buildsystem.model.ExecutionUnit;
import ch.elca.leaf.buildsystem.model.Module;
import ch.elca.leaf.buildsystem.model.Plugin;
import ch.elca.leaf.buildsystem.model.ProjectRepository;
import ch.elca.leaf.buildsystem.propgen.AbstractPropertyGenerator;
import ch.elca.leaf.buildsystem.propgen.CompilePropertyGenerator;

/**
 * Eclipse plugin. Features: <ul><li> generation of .project and .classpath for
 * each module.</li><li> generation of .checkstyle</li>
 *
 * Supports Eclipse Java and PDE project types.
 *
 * <script type="text/javascript">printFileStatus ("$Source:
 * /cvsroot/leafjava3/prototype/build/buildsystem/eclipse/java/ch/elca/leaf/buildsystem/eclipse/propgen/EclipsePluginPropertyGenerator.java,v
 * $", "$Revision: 223 $", "$Date: 2005-05-19 13:17:45 -0400 (Thu, 19 May 2005) $", "$Author: yma $" );
 * </script>
 *
 * @author Jacques-Olivier Haenni (JOH)
 * @author Yves Martin (YMA)
 * @version $Revision: 223 $
 */
public class EclipsePluginPropertyGenerator extends AbstractPropertyGenerator implements BuildGenerator {

    /** Generator name "eclipse". */
    public static final String GENERATOR_NAME = "eclipse";

    /** List of the external tools to install. */
    private static final String ECLIPSE_TOOL_ATTR = "eclipse.tools";

    /** The property for the project name prefix to use. */
    private static final String TOOL_PREFIX = "eclipse.prefix";

    /** The default project name prefix. */
    private static final String DEFAULT_PREFIX = "Honeydew_";

    /**
     * Name of the optional module attribute specifying the type of the
     * corresponding Eclipse project.
     */
    private static final String ECLIPSE_PROJECT_TYPE_ATTR = "eclipse.project.type";

    /** Default value for the Eclipse project type. */
    private static final String DEFAULT_ECLIPSE_PROJECT_TYPE = "java";

    /** Name of the Eclipse classpath file. */
    private static final String CLASSPATH_FILE_NAME = ".classpath";

    /** Name of the Velocity template for the classpath file. */
    private static final String CLASSPATH_TEMPLATE = "templates/eclipse.classpath.vm";

    /** Name of the Eclipse project file. */
    private static final String PROJECT_FILE_NAME = ".project";

    /** Name of the Velocity template for the project file. */
    private static final String PROJECT_TEMPLATE = "templates/eclipse.project.vm";

    /** Name of the Eclipse checkstyle file. */
    private static final String CHECKSTYLE_FILE_NAME = ".checkstyle";

    /** Name of the Velocity template for the checkstyle file. */
    private static final String CHECKSTYLE_TEMPLATE = "templates/eclipse.checkstyle.vm";

    /** Name of the Velocity template for the xxx.launch file. */
    private static final String TOOL_LAUNCH_TEMPLATE = "templates/eclipse.external_tool.launch.vm";

    /**
     * Marker attribute for binary modules. Warning: synchronized with
     * BinaryReleaseTask.BINARY_MARKER.
     */
    private static final String BINRELEASE_BINARY_MARKER = "binrelease.binary";

    /**
     * Pattern to look for to know if sources are available.
     */
    private static final String SOURCE_PATTERN = "**/*.java";

    /**
     * Directory where source zip are expected. TBD: make it configurable or at
     * least use the project.lib.directory property.
     */
    private static final String SOURCE_ZIP_DIRECTORY = "lib/src/";

    /**
     * End of file name to determine the file where sources for a third party
     * jar can be found.
     */
    private static final String SOURCE_ZIP_POSTFIX = "-src.zip";

    /** Set of the allows Eclipse project types. */
    private Set m_allowedProjectTypes = new HashSet();

    /** Velocity helper */
    private VelocityHelper m_velocityHelper;

    /** Eclipse plugin helper */
    private EclipsePluginHelper m_helper;

    /** Eclipse project prefix */
    private String m_prefix = DEFAULT_PREFIX;

    /** External tools list to generate */
    private Map m_externalTools = new HashMap();

    /**
     * Default generator constructor.
     */
    public EclipsePluginPropertyGenerator() {
        m_allowedProjectTypes.add(DEFAULT_ECLIPSE_PROJECT_TYPE);
        m_allowedProjectTypes.add("pde");
        m_velocityHelper = new VelocityHelper();
        m_helper = new EclipsePluginHelper(m_velocityHelper);
    }

    /**
     * Setter called before generation to enable logging.
     *
     * @param project the Ant <code>Project</code> instance
     * @param task the Ant <code>Task</code> instance
     */
    public void setProjectLogger(Project project, Task task) {
        super.setProjectLogger(project, task);
        m_velocityHelper.setProjectLogger(project, task);
    }

    /**
     * Generates needed properties and analyzes defined attributes.
     *
     * @param p the project <code>Properties</code>
     * @return <code>true</code> (no condition)
     */
    public boolean generate(Properties p) {
        ProjectRepository pr = ProjectRepository.getInstance();
        Plugin eclipsePlugin = pr.getPlugin(GENERATOR_NAME);
        if (eclipsePlugin == null) {
            String message = "No plugin named " + GENERATOR_NAME + " has been found.";
            throw new BuildException(message);
        }
        Iterator attrIt = eclipsePlugin.getAttributesByName(TOOL_PREFIX);
        if (attrIt.hasNext()) {
            Attribute attribute = (Attribute) attrIt.next();
            m_prefix = attribute.getValue();
        } else {
            p.setProperty(TOOL_PREFIX, DEFAULT_PREFIX);
        }
        attrIt = eclipsePlugin.getAttributesByName(ECLIPSE_TOOL_ATTR);
        while (attrIt.hasNext()) {
            Attribute attribute = (Attribute) attrIt.next();
            String antTargetList = attribute.getValue();
            StringTokenizer tokenizer = new StringTokenizer(antTargetList, LIST_SEPARATOR);
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken().trim();
                m_externalTools.put(token, m_prefix + token);
            }
            attribute.markSilent();
        }
        pr.addBuildGenerator(this);
        return true;
    }

    /**
     * Generates the .classpath and .project files for each module. Registers
     * builders and external tools.
     *
     * @param buildFile unused <code>PrintWriter</code>
     * @return <code>true</code>
     */
    public boolean appendContent(PrintWriter buildFile) {
        ProjectRepository pr = ProjectRepository.getInstance();
        Properties p = pr.getProperties();
        long timestampBefore = System.currentTimeMillis();
        boolean commonsAttributesPlugin = (pr.getPlugin("commons-attributes") != null);
        boolean checkstylePlugin = (pr.getPlugin("checkstyle") != null);
        Iterator moduleIt = pr.getModuleIterator();
        while (moduleIt.hasNext()) {
            Module module = (Module) moduleIt.next();
            if (checkModuleMarker(module, BINRELEASE_BINARY_MARKER, false)) {
                continue;
            }
            File moduleDir = module.getFilePath();
            if (!moduleDir.isDirectory()) {
                log("Cannot find directory for module " + module.getModuleName(), Project.MSG_WARN);
                continue;
            }
            List builders = new ArrayList();
            if (commonsAttributesPlugin && checkModuleMarker(module, "commons-attributes.enable", false)) {
                final String BUILDER_NAME = m_prefix + "Commons Attributes Builder.launch";
                builders.add(BUILDER_NAME);
                generateBuilder(module, "commons-attributes.module", BUILDER_NAME, pr);
            }
            log("Beginning to generate Eclipse file for module " + module.getModuleName() + ".", Project.MSG_DEBUG);
            generateEclipseClasspath(module, p);
            generateEclipseProject(module, p, builders);
            if (checkstylePlugin) {
                generateEclipseCheckstyle(module, p);
            }
        }
        generateExternalTools(p);
        long timestampAfter = System.currentTimeMillis();
        log("Eclipse file generation (milliseconds): " + (timestampAfter - timestampBefore), Project.MSG_VERBOSE);
        return true;
    }

    /**
     * Generate the .classpath path for a given module.
     *
     * @param module the <code>Module</code> to process
     * @param p the generated project <code>Properties</code>
     */
    private void generateEclipseClasspath(Module module, Properties p) {
        List jarList = new ArrayList();
        List moduleList = new ArrayList();
        addJarDependencies(module, jarList);
        addBinaryModuleJarDependencies(module, jarList);
        for (Iterator it = module.getModuleDependencies(); it.hasNext(); ) {
            Module aModule = (Module) it.next();
            boolean isBinaryModule = checkModuleMarker(aModule, BINRELEASE_BINARY_MARKER, false);
            if (!isBinaryModule) {
                moduleList.add(aModule.getModuleName());
            }
        }
        List sourcesList = new ArrayList();
        boolean noSource = checkModuleMarker(module, CompilePropertyGenerator.COMPILE_NOSOURCES, false);
        if (!noSource) {
            File sourceDir = CompilePropertyGenerator.createSourcesFile(module);
            if (sourceDir.exists() && sourceDir.isDirectory()) {
                sourcesList.add(CompilePropertyGenerator.getSourceDirectory());
            }
        }
        if (ProjectRepository.getInstance().getPlugin("resources") != null) {
            String directories = p.getProperty("resources.directories");
            StringTokenizer tokenizer = new StringTokenizer(directories, LIST_SEPARATOR);
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken().trim();
                File aDir = new File(module.getFilePath(), token);
                if (aDir.isDirectory()) {
                    sourcesList.add(token);
                }
            }
        }
        String projectType = getEclipseProjectType(module);
        String exclusionPattern = "**/CVS/";
        Context context = new VelocityContext();
        context.put("srcList", sourcesList);
        context.put("jarList", jarList);
        context.put("moduleList", moduleList);
        context.put("projectType", projectType);
        context.put("exclusionPattern", exclusionPattern);
        if (checkModuleMarker(module, CompilePropertyGenerator.COMPILE_NOCLASSES, false)) {
            context.put("hasClasses", Boolean.FALSE);
        } else {
            context.put("hasClasses", Boolean.TRUE);
        }
        File modulePath = module.getFilePath();
        File file = new File(modulePath, CLASSPATH_FILE_NAME);
        m_velocityHelper.writeDataToFile(file, CLASSPATH_TEMPLATE, context);
    }

    /**
     * This method is used to add jar dependecy lines of dependent binary
     * modules to the given jar list.
     *
     * @param module Is the module which could have dependencie to binary
     * module.
     * @param jarList Is the list where the jar properties are be added.
     */
    private void addBinaryModuleJarDependencies(Module module, List jarList) {
        for (Iterator it = module.getModuleDependencies(); it.hasNext(); ) {
            Module dependentModule = (Module) it.next();
            if (!checkModuleMarker(dependentModule, BINRELEASE_BINARY_MARKER, false)) {
                continue;
            }
            addJarDependencies(dependentModule, jarList);
            File javaDirectory = CompilePropertyGenerator.createSourcesFile(dependentModule);
            boolean javaDirectoryExists = javaDirectory.exists() && javaDirectory.isDirectory();
            Iterator itEu = dependentModule.getEuIterator();
            while (itEu.hasNext()) {
                String dependentModuleEu = (String) itEu.next();
                String jarFileName = CompilePropertyGenerator.createJarName(dependentModule, dependentModuleEu);
                File jarFile = ProjectRepository.getInstance().resolveFile(jarFileName);
                if (jarFile.exists()) {
                    JarProperty jarProperty = new JarProperty();
                    jarProperty.setBinaryPath(jarFile);
                    if (javaDirectoryExists) {
                        jarProperty.setSourcePath(javaDirectory);
                    }
                    if (!jarList.contains(jarProperty)) {
                        jarList.add(jarProperty);
                    }
                }
            }
            addBinaryModuleJarDependencies(dependentModule, jarList);
        }
    }

    /**
     * Look for a source zip file associated to a third-party jar file.
     *
     * @param jarName the name of the jar file to consider
     * @return the location of the source zip if found, or <code>null</code>.
     */
    private File checkForSourceZip(String jarName) {
        File result = null;
        String expectedZip;
        int dotPos = jarName.lastIndexOf('.');
        if (dotPos > 0) {
            expectedZip = SOURCE_ZIP_DIRECTORY + jarName.substring(0, dotPos) + SOURCE_ZIP_POSTFIX;
        } else {
            expectedZip = SOURCE_ZIP_DIRECTORY + jarName + SOURCE_ZIP_POSTFIX;
        }
        ProjectRepository pr = ProjectRepository.getInstance();
        String testZip = (String) pr.resolveComponent(ComponentResolver.TYPE_JAR, expectedZip);
        if (testZip != null) {
            File zipFile = pr.resolveFile(testZip);
            if (zipFile.exists()) {
                result = zipFile;
            }
        }
        return result;
    }

    /**
     * Checks if a jar file contains at least one source file.
     *
     * @param jarFile a <code>File</code> value
     * @return a <code>boolean</code> value
     */
    private boolean checkForSources(File jarFile) {
        ZipFileSet fs = new ZipFileSet();
        fs.setSrc(jarFile);
        fs.setIncludes(SOURCE_PATTERN);
        ProjectRepository pr = ProjectRepository.getInstance();
        DirectoryScanner ds = fs.getDirectoryScanner(pr.getBootstrapper());
        ds.scan();
        String[] selectedSources = ds.getIncludedFiles();
        return (selectedSources.length > 0);
    }

    /**
     * This method adds all jar dependencies of a module in a given list.
     *
     * @param module Is the module where jar dependencies are get from.
     * @param jarList Is the list where the jar properties are be added.
     */
    private void addJarDependencies(Module module, List jarList) {
        ProjectRepository pr = ProjectRepository.getInstance();
        for (Iterator it = module.getModuleJars(); it.hasNext(); ) {
            String jarName = (String) it.next();
            File jarFile = pr.resolveFile(jarName);
            JarProperty jarProperty = new JarProperty();
            jarProperty.setBinaryPath(jarFile);
            File sourceZip = checkForSourceZip(jarFile.getName());
            if (sourceZip != null) {
                jarProperty.setSourcePath(sourceZip);
            } else if (checkForSources(jarFile)) {
                jarProperty.setSourcePath(jarFile);
            }
            if (!jarList.contains(jarProperty)) {
                jarList.add(jarProperty);
            }
        }
    }

    /**
     * Generate the .project file for a given module.
     *
     * @param module the <code>Module</code> to process
     * @param p the generated project <code>Properties</code>
     * @param builders list of builder name to use
     */
    private void generateEclipseProject(Module module, Properties p, List builders) {
        String projectName = module.getModuleName();
        String projectComment = module.getDescription();
        if (projectComment == null) {
            projectComment = projectName;
        }
        String projectType = getEclipseProjectType(module);
        Context context = new VelocityContext();
        context.put("projectName", projectName);
        context.put("projectComment", projectComment);
        context.put("projectType", projectType);
        context.put("builders", builders);
        if (!checkModuleMarker(module, CompilePropertyGenerator.COMPILE_NOCLASSES, false)) {
            String compileClassDir = p.getProperty(CompilePropertyGenerator.COMPILE_CLASSES + projectName);
            ProjectRepository pr = ProjectRepository.getInstance();
            File absClassDir = pr.resolveFile(compileClassDir);
            context.put("compileClasses", absClassDir.getAbsolutePath());
        }
        File modulePath = module.getFilePath();
        File file = new File(modulePath, PROJECT_FILE_NAME);
        m_velocityHelper.writeDataToFile(file, PROJECT_TEMPLATE, context);
    }

    /**
     * Generate the .checkstyle file for a given module.
     *
     * @param module the <code>Module</code> to process
     * @param p the generated project <code>Properties</code>
     */
    private void generateEclipseCheckstyle(Module module, Properties p) {
        Context context = new VelocityContext();
        File modulePath = module.getFilePath();
        File file = new File(modulePath, CHECKSTYLE_FILE_NAME);
        m_velocityHelper.writeDataToFile(file, CHECKSTYLE_TEMPLATE, context);
    }

    /**
     * Generate the external tools configuration.
     * @param p project <code>Properties</code>
     */
    private void generateExternalTools(Properties p) {
        if (m_externalTools.size() != 0) {
            File workspacePath = getWorkspacePath(p);
            m_helper.checkWorkspacePath(workspacePath);
            Iterator mapIt = m_externalTools.entrySet().iterator();
            while (mapIt.hasNext()) {
                Map.Entry entry = (Map.Entry) mapIt.next();
                String antTarget = (String) entry.getKey();
                String name = (String) entry.getValue();
                generateExternalTool(antTarget, name, workspacePath);
            }
        }
    }

    /**
     * Generate a xxx.launch file for an external tool. Registered shortcuts
     * are used to publish them in Eclipse. If the shortcut does not depends on
     * execution unit, then only one tool is created and the current Eclipse
     * project is used as target module. If execution unit is needed, all
     * shortcuts are generated as a tool.
     *
     * @param target the generic Ant target name
     * @param name the tool name
     * @param workspacePath the workspace directory
     */
    private void generateExternalTool(String target, String name, File workspacePath) {
        String baseDir = ProjectRepository.getInstance().getBootstrapper().getBaseDir().getAbsolutePath();
        List historyTools = new ArrayList();
        ShortcutGenerator shortcut = lookForShortcut(target);
        if (shortcut == null) {
            Context context = new VelocityContext();
            context.put("baseDir", baseDir);
            context.put("antTarget", target);
            context.put("moduleTarget", "false");
            context.put("isBuilder", "false");
            File file = m_helper.createEclipseLaunchFile(name, workspacePath);
            m_velocityHelper.writeDataToFile(file, TOOL_LAUNCH_TEMPLATE, context);
            historyTools.add(name);
        } else {
            List params = shortcut.getParameters();
            if (params.contains("eu")) {
                generateShortcutTools(historyTools, shortcut, name, baseDir, workspacePath);
            } else if (params.contains("module")) {
                Context context = new VelocityContext();
                context.put("baseDir", baseDir);
                context.put("antTarget", target);
                context.put("moduleTarget", "true");
                context.put("isBuilder", "false");
                File file = m_helper.createEclipseLaunchFile(name, workspacePath);
                m_velocityHelper.writeDataToFile(file, TOOL_LAUNCH_TEMPLATE, context);
                historyTools.add(name);
            }
        }
        m_helper.addToolsToLaunchHistory(historyTools, workspacePath, "run");
    }

    /**
     * Get the shortcut generated for the given Ant target.
     *
     * @param targetName the Ant target name
     * @return the corresponding <code>ShortcutGenerator</code> or
     * <code>null</code>
     */
    private ShortcutGenerator lookForShortcut(String targetName) {
        ShortcutGenerator result = null;
        List generators = ProjectRepository.getInstance().getBuildGenerators();
        Iterator it = generators.iterator();
        while (it.hasNext()) {
            Object aGen = it.next();
            if (aGen instanceof ShortcutGenerator) {
                ShortcutGenerator shortcut = (ShortcutGenerator) aGen;
                if (shortcut.getTargetName().equals(targetName)) {
                    result = shortcut;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Generate shortcut external tools for needed module execution units.
     *
     * @param history the tool history to fill
     * @param shortcut the target shortcut to generate
     * @param toolName the external tool name
     * @param baseDir the project base directory
     * @param workspacePath the Eclipse workspace directory
     */
    private void generateShortcutTools(List history, ShortcutGenerator shortcut, String toolName, String baseDir, File workspacePath) {
        ProjectRepository pr = ProjectRepository.getInstance();
        Iterator moduleIt = pr.getModuleIterator();
        while (moduleIt.hasNext()) {
            Module aModule = (Module) moduleIt.next();
            Iterator euIt = aModule.getEuIterator();
            while (euIt.hasNext()) {
                String aEu = (String) euIt.next();
                if (!shortcut.isGenerated(aModule.getModuleName(), aEu)) {
                    continue;
                }
                String targetSuffix = "." + aModule.getModuleName();
                if (!ExecutionUnit.NONE.equals(aEu)) {
                    targetSuffix += "." + aEu;
                }
                String shortcutName = toolName.replaceAll("\\.module\\.eu", targetSuffix);
                Context context = new VelocityContext();
                context.put("baseDir", baseDir);
                context.put("antTarget", shortcut.getTargetName() + targetSuffix);
                context.put("moduleTarget", "false");
                context.put("isBuilder", "false");
                File file = m_helper.createEclipseLaunchFile(shortcutName, workspacePath);
                m_velocityHelper.writeDataToFile(file, TOOL_LAUNCH_TEMPLATE, context);
                history.add(shortcutName);
            }
        }
    }

    /**
     * Generate the xxx.launch file for a builder.
     *
     * @param module the concerned module
     * @param target the Ant target name
     * @param name the Eclipse builder name
     * @param pr the <code>ProjectRepository</code> instance
     */
    private void generateBuilder(Module module, String target, String name, ProjectRepository pr) {
        String baseDir = pr.getBootstrapper().getBaseDir().getAbsolutePath();
        Context context = new VelocityContext();
        context.put("baseDir", baseDir);
        context.put("antTarget", target);
        context.put("moduleTarget", Boolean.toString(target.endsWith(".module")));
        context.put("isBuilder", "true");
        File file = m_helper.createEclipseBuilderFile(module, name);
        m_velocityHelper.writeDataToFile(file, TOOL_LAUNCH_TEMPLATE, context);
    }

    /**
     * Get the workspace path from the plugin attribute.
     * @param p project <code>Properties</code>
     * @return the <code>File</code> location for workspace
     */
    private File getWorkspacePath(Properties p) {
        String workspaceValue = p.getProperty(EclipsePluginHelper.ECLIPSE_WORKSPACE_PATH_ATTR);
        if (workspaceValue == null) {
            String message = "The attribute " + EclipsePluginHelper.ECLIPSE_WORKSPACE_PATH_ATTR + " is not specified.";
            throw new BuildException(message);
        }
        ProjectRepository pr = ProjectRepository.getInstance();
        return pr.resolveFile(workspaceValue);
    }

    /**
     * Returns the Eclipse project type for a given module.
     *
     * @param module a <code>Module</code> instance
     * @return the module project type
     */
    private String getEclipseProjectType(Module module) {
        String type = null;
        Iterator it = module.getAttributesByName(ECLIPSE_PROJECT_TYPE_ATTR);
        while (it.hasNext()) {
            if (type != null) {
                String message = "Attribute '" + ECLIPSE_PROJECT_TYPE_ATTR + "' appears more than once in module " + module.getModuleName();
                throw new BuildException(message);
            }
            Attribute attribute = (Attribute) it.next();
            type = attribute.getValue();
        }
        if (type == null) {
            type = DEFAULT_ECLIPSE_PROJECT_TYPE;
        }
        if (!m_allowedProjectTypes.contains(type)) {
            throw new BuildException("Invalid eclipse project type in module " + module.getModuleName());
        }
        return type;
    }

    /**
     * Declare the generator name.
     *
     * @return from <code>GENERATOR_NAME</code>
     */
    public String getGeneratorName() {
        return GENERATOR_NAME;
    }

    /**
     * Declare genertor dependencies.
     *
     * @return a <code>Set</code> with <code>compile</code>
     */
    public Set getDependencies() {
        Set result = new HashSet();
        result.add(CompilePropertyGenerator.GENERATOR_NAME);
        return result;
    }
}
