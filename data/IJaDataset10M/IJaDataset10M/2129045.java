package ro.codemart.installer.packer.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.selectors.FilenameSelector;
import ro.codemart.installer.packer.ant.exception.ValidationException;
import ro.codemart.installer.packer.ant.impl.nestedjar.NestedJarPacker;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

/**
 * An Ant task that creates a distributable file (plain JAR, nested JAR, etc) according to the configuration
 *
 * @author marius.ani
 */
public class PackerTask extends MatchingTask {

    private static final Map<String, AntPacker> PACKERS = new HashMap<String, AntPacker>();

    private static final String NESTEDJAR = "nestedjar";

    static {
        PACKERS.put(NESTEDJAR, new NestedJarPacker());
    }

    private String target;

    private PackerConfiguration packerConfig = new PackerConfiguration();

    /**
     * Set the extractor main class
     *
     * @param extractorMainClass extractor main class
     */
    public void setExtractorMainClass(String extractorMainClass) {
        packerConfig.setExtractorClass(extractorMainClass);
    }

    /**
     * Return the base extractor main class
     *
     * @return main extractor class
     */
    public String getExtractorMainClass() {
        return packerConfig.getExtractorClass();
    }

    /**
     * Add the build directory containing the extractor classes
     *
     * @param classDir base directory for classes that will extract the installer
     */
    public void setExtractorClassesDir(String classDir) {
        packerConfig.setExtractorClassesDir(classDir);
    }

    /**
     * Getter for property 'baseDir'.
     *
     * @return Value for property 'baseDir'.
     */
    public String getBaseLibraryDir() {
        return packerConfig.getBaseLibraryDir();
    }

    /**
     * Setter for property 'baseDir'.
     *
     * @param baseDir Value to set for property 'baseDir'.
     */
    public void setBaseLibraryDir(String baseDir) {
        packerConfig.setBaseLibraryDir(baseDir);
    }

    /**
     * Getter for property 'target'.
     *
     * @return Value for property 'target'.
     */
    public String getDestfile() {
        return packerConfig.getDestfile();
    }

    /**
     * Setter for property 'target'.
     *
     * @param destfile Value to set for property 'target'.
     */
    public void setDestfile(String destfile) {
        packerConfig.setDestfile(destfile);
    }

    /**
     * Getter for property 'target'.
     *
     * @return Value for property 'target'.
     */
    public String getTarget() {
        return target;
    }

    /**
     * Setter for property 'target'.
     *
     * @param target Value to set for property 'target'.
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * Set the name for the "log4j" file
     *
     * @param log4jFile the name of the "log4j" file
     */
    public void setLog4jFile(String log4jFile) {
        packerConfig.setLog4jFile(log4jFile);
    }

    /**
     * Return the name of the "log4j" file
     *
     * @return the name of the "log4j" file
     */
    public String getLog4jFile() {
        return packerConfig.getLog4jFile();
    }

    /**
     * Set the name of the splashscreen image
     *
     * @param img the name of the splashscreen image
     */
    public void setSplashScreenImage(String img) {
        packerConfig.setSplashScreenImage(img);
    }

    /**
     * Return the name of the splashscreen image
     *
     * @return the name of the splashscreen image
     */
    public String getSplashScreenImage() {
        return packerConfig.getSplashScreenImage();
    }

    /**
     * Adds a fileset to the libraries fileset
     *
     * @param fs the fileset to be added to libraries fileset
     */
    public void addLibraryFileSet(FileSet fs) {
        packerConfig.getLibrariesFileSet().add(fs);
    }

    /**
     * Adds a fileset to the resources fileset
     *
     * @param fs the fileset to be added to resources fileset
     */
    public void addResourcesFileSet(FileSet fs) {
        packerConfig.getResourcesFileSet().add(fs);
    }

    /**
     * Add the default fileset for libraries
     */
    private void addDefaultLibraryFileSets() {
        addLibraryFileSet(createFileSet("**/*.*"));
    }

    /**
     * Creates a new {@code FileSet} object
     *
     * @param includes the pattern of the files to be included
     * @return the created {@code FileSet} object
     */
    private FileSet createFileSet(String includes) {
        FileSet fs = new FileSet();
        fs.setProject(getProject());
        packerConfig.setProject(getProject());
        fs.setDir(new File(getProject().getBaseDir(), packerConfig.getBaseLibraryDir()));
        fs.setIncludes(includes);
        return fs;
    }

    /**
     * Executes the task of packing all the files needed for the installer into a single executable jar
     */
    public void execute() {
        try {
            addDefaultLibraryFileSets();
            packerConfig.validateConfig();
        } catch (ValidationException e) {
            throw new BuildException("Can't package application due to configuration validation errors", e);
        }
        AntPacker packer = PACKERS.get(target);
        if (packer != null) {
            packer.pack(packerConfig);
        } else {
            throw new BuildException("Can't find any suitable packers for " + target);
        }
    }
}
