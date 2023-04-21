package org.codecompany.jeha.mojo;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codecompany.jeha.util.Utils;
import org.codehaus.classworlds.ClassRealm;
import org.codehaus.classworlds.ClassWorld;
import org.codehaus.classworlds.DuplicateRealmException;
import org.codehaus.plexus.util.FileUtils;

/**
 * Inject handler instructions into classes.
 * 
 * @phase compile
 * @goal jeha
 * @requiresDependencyResolution compile
 */
public class JehaMojo extends AbstractMojo {

    private WeavingIgnorer ignorer = new DefaultWeavingIgnorer();

    public static final String DEFAULT_INCLUDES = "**/*.class";

    public static final String DEFAULT_EXCLUDES = "";

    /**
	 * Location of class files to be woven. When none specified project build
	 * directory is used.
	 * 
	 * @parameter
	 */
    private String[] sourceDirs;

    /**
	 * List of ant-style patterns used to specify the aspects that should be
	 * excluded when weaving. When none specified all .class files in the
	 * project build directory are included.
	 * 
	 * @parameter
	 */
    private String[] includes;

    /**
	 * List of ant-style patterns used to specify the aspects that should be
	 * excluded when weaving. When none specified all .class files in the
	 * project build directory are included.
	 * 
	 * @parameter
	 */
    private String[] excludes;

    /**
	 * @parameter expression="${project}"
	 * @readonly
	 */
    private MavenProject project;

    /**
	 * @parameter default-value="true"
	 */
    private boolean surroundingCatch = true;

    /**
	 * @parameter default-value="false"
	 */
    private boolean throwException = false;

    /**
	 * @parameter default-value="org.codecompany.jeha.mojo.WeavingIgnorer"
	 */
    private String weavingIgnorer = "org.codecompany.jeha.mojo.WeavingIgnorer";

    @SuppressWarnings("unchecked")
    public void execute() throws MojoExecutionException {
        try {
            ignorer = (WeavingIgnorer) Utils.forName(weavingIgnorer).newInstance();
        } catch (Exception e) {
            getLog().warn("Could not load '" + "' class. Using default ignorer strategy.");
            ignorer = new DefaultWeavingIgnorer();
        }
        if (sourceDirs == null || sourceDirs.length == 0) {
            String directory = project.getBuild().getOutputDirectory();
            getLog().info("No source directories informed. " + "Using ${project.build.outputDirectory}: " + directory);
            sourceDirs = new String[] { directory };
        }
        getLog().info("--------------- BEGIN OF JEHA WEAVING ---------------");
        List<String> classpath = new ArrayList<String>();
        try {
            List<String> compileClasspath = project.getCompileClasspathElements();
            List<String> testClasspath = project.getTestClasspathElements();
            classpath.addAll(compileClasspath);
            classpath.addAll(testClasspath);
            Set<Artifact> artifacts = project.getDependencyArtifacts();
            for (Artifact artifact : artifacts) {
                classpath.add(artifact.getFile().getAbsolutePath());
            }
        } catch (DependencyResolutionRequiredException e) {
            throw new MojoExecutionException("Error resolving classpath elements", e);
        }
        configureClassPath();
        getLog().info("      sourceDirs=[" + getAsCsv(sourceDirs) + "]");
        getLog().info("        includes=[" + getAsCsv(includes) + "]");
        getLog().info("        excludes=[" + getAsCsv(excludes) + "]");
        getLog().info("surroundingCatch=" + surroundingCatch);
        getLog().info("  throwException=" + throwException);
        getLog().info("       classpath=" + classpath);
        getLog().info("  weavingIgnorer=" + ignorer);
        Set<String> files = getFilesToWeave(sourceDirs, includes, excludes);
        if (files.size() > 0) {
            Weaver weaver = new Weaver(getLog(), surroundingCatch, throwException, classpath, ignorer);
            for (String file : files) {
                getLog().debug("Weaving file: " + file);
                String className = classNameFromFile(sourceDirs, file);
                try {
                    weaver.transform(className, null);
                } catch (WeavingException e) {
                    throw new MojoExecutionException("Error transforming class: " + className, e);
                }
            }
        } else {
            getLog().warn("No files to weave found in directories: " + getAsCsv(sourceDirs));
        }
        getLog().info("---------------- END OF JEHA WEAVING ----------------");
    }

    /**
	 * Based on a set of sourcedirs, apply include and exclude statements and
	 * returns a set of all the files to be weaved.
	 * 
	 * @return
	 * @throws MojoExecutionException
	 */
    @SuppressWarnings("unchecked")
    private Set<String> getFilesToWeave(String[] sourceDirs, String[] includes, String[] excludes) throws MojoExecutionException {
        Set<String> result = new HashSet<String>();
        String inclusionFilter = DEFAULT_INCLUDES;
        if (includes != null && includes.length > 0) {
            inclusionFilter = getAsCsv(includes);
        } else {
            getLog().debug("Using default inclusion filter: " + DEFAULT_INCLUDES);
        }
        String exclusionFilter = DEFAULT_EXCLUDES;
        if (excludes != null && excludes.length > 0) {
            exclusionFilter = getAsCsv(excludes);
        } else {
            getLog().debug("Using default exclusion filter: " + DEFAULT_EXCLUDES);
        }
        for (String sourceDir : sourceDirs) {
            try {
                if (FileUtils.fileExists(sourceDir)) {
                    result.addAll(FileUtils.getFileNames(new File(sourceDir), inclusionFilter, exclusionFilter, true));
                }
            } catch (IOException e) {
                throw new MojoExecutionException("Error resolving sourcedirs", e);
            }
        }
        return result;
    }

    /**
	 * Convert a string array to a comma seperated list
	 * 
	 * @param strings
	 * @return
	 */
    private String getAsCsv(String[] strings) {
        StringBuilder csv = new StringBuilder();
        if (null != strings) {
            for (int i = 0; i < strings.length; i++) {
                csv.append(strings[i]);
                if (i < (strings.length - 1)) {
                    csv.append(",");
                }
            }
        }
        return csv.toString();
    }

    private String classNameFromFile(String[] sourceDirs, String fileName) {
        String className = fileName;
        Arrays.sort(sourceDirs, new StringSizeComparator());
        for (String sourceDir : sourceDirs) {
            sourceDir = new File(sourceDir).getAbsolutePath();
            if (className.startsWith(sourceDir)) {
                className = className.substring(sourceDir.length() + 1);
                break;
            }
        }
        className = className.replace('/', '.').replace('\\', '.').replaceAll("\\.class", "");
        return className;
    }

    private class StringSizeComparator implements Comparator<String> {

        public int compare(String s1, String s2) {
            int result = 0;
            String ss1 = String.valueOf(s1);
            String ss2 = String.valueOf(s2);
            if (ss1.length() > ss2.length()) {
                result = -1;
            } else if (ss1.length() < ss2.length()) {
                result = 1;
            }
            return result;
        }
    }

    @SuppressWarnings("unchecked")
    private void configureClassPath() throws MojoExecutionException {
        try {
            ClassWorld world = new ClassWorld();
            ClassRealm realm = world.newRealm("org.codecompany.jeha.plugin", Thread.currentThread().getContextClassLoader());
            ClassRealm runRealm = realm.createChildRealm("runenv");
            Set<Artifact> artifacts = project.getDependencyArtifacts();
            for (Artifact artifact : artifacts) {
                runRealm.addConstituent(artifact.getFile().toURI().toURL());
            }
            Thread.currentThread().setContextClassLoader(runRealm.getClassLoader());
        } catch (MalformedURLException e) {
            throw new MojoExecutionException("Unable to load output directory and dependencies URLs.", e);
        } catch (DuplicateRealmException e) {
            throw new MojoExecutionException("Unable to create class loding context.", e);
        }
    }
}
