package org.ocl4java.ocl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.DirSet;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.ocl4java.Constraint;

/**
 *
 * @author <a href="matilto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class OCLCodeGeneratorAntTask extends MatchingTask {

    /**
     * Must be 'java', 'exception', 'jass', 'handler', 'system.err' or 'commons.'+('err'/'warn'/'debug'.
     *  @see {@link ASSERTIONSTYLE}
     */
    private ASSERTIONSTYLE myAssertionStyle;

    /**
     * the directory that the modified source is to be written to.
     */
    private File myTargetDir;

    /**
     * the directory that the unmodified source is to be reat from.
     */
    private File myInputDir;

    /**
     * Directories to scan for sources, needed
     * to get the model-information for the OCL-compiler.
     */
    private DirSet mySourceDirs;

    /**
     * jar-files contaning source-code to scan for sources, needed
     * to get the model-information for the OCL-compiler.
     */
    private FileSet mySourceJars;

    /**
     * Classpath to use if we need to fall back to reflection.
     */
    private Path myClassPath;

    /**
     * @return Returns the mySourceDir.
     * @see #mySourceDir
     */
    public final File getInputDir() {
        return myInputDir;
    }

    /**
     * @param sourceDir The mySourceDir to set.
     * @see #mySourceDir
     */
    @Constraint("post: myInputDir = sourceDir")
    public final void setInputDir(final File sourceDir) {
        this.myInputDir = sourceDir;
    }

    /**
     * @return Returns the targetDir.
     * @see #myTargetDir
     */
    public final File getTargetDir() {
        return myTargetDir;
    }

    /**
     * @param targetDir The targetDir to set.
     * @see #myTargetDir
     */
    @Constraint("post: myTargetDir = targetDir")
    public final void setTargetDir(final File targetDir) {
        this.myTargetDir = targetDir;
    }

    /**
     * Constructor.
     */
    public OCLCodeGeneratorAntTask() {
        super();
    }

    /**
     * @return Returns the sourceDirs.
     */
    public final DirSet getSourceDirs() {
        return mySourceDirs;
    }

    /**
     * @param sourceDirs The sourceDirs to set.
     */
    @Constraint("post: mySourceDirs = sourceDirs")
    public final void setSourceDirs(final DirSet sourceDirs) {
        this.mySourceDirs = sourceDirs;
    }

    /**
     * @param sourceDirs The sourceJars to set.
     */
    public final void addSourceDirs(final DirSet sourceDirs) {
        if (getSourceDirs() != null) {
            throw new IllegalArgumentException(Messages.getString("OCLCodeGeneratorAntTask.Problem-OnlyOneSourcedirAllowed"));
        }
        this.mySourceDirs = sourceDirs;
    }

    /**
     * @return Returns the sourceJars.
     */
    public final FileSet getSourceJars() {
        return mySourceJars;
    }

    /**
     * @param sourceJars The sourceJars to set.
     */
    public final void setSourceJars(final FileSet sourceJars) {
        this.mySourceJars = sourceJars;
    }

    @Constraint("post: ( self.mySourceJars = sourceJars ) or ( self.mySourceJars@pre.toString().size>0)")
    public final void addSourceJars(final FileSet sourceJars) {
        if (getSourceJars() != null) {
            throw new IllegalArgumentException(Messages.getString("OCLCodeGeneratorAntTask.Problem-OnlyOneSourcejarsAllowed"));
        }
        this.mySourceJars = sourceJars;
        return;
    }

    /**
     * So our job.
     * @see org.apache.tools.ant.Task#execute()
     */
    @Override
    @Constraint("post: self.myTargetDir.exists()=true")
    public void execute() {
        super.execute();
        Collection<File> sources = new LinkedList<File>();
        FileSet jars = getSourceJars();
        if (jars != null) {
            DirectoryScanner directoryScanner = jars.getDirectoryScanner(getProject());
            String[] files = directoryScanner.getIncludedFiles();
            for (int i = 0; i < files.length; i++) {
                File f = new File(jars.getDir(getProject()), files[i]);
                System.err.println("jar: '" + files[i] + "'->" + f.getAbsolutePath());
                sources.add(f);
            }
        }
        DirSet dirs = getSourceDirs();
        if (dirs != null) {
            DirectoryScanner directoryScanner = dirs.getDirectoryScanner(getProject());
            String[] files = directoryScanner.getIncludedDirectories();
            for (int i = 0; i < files.length; i++) {
                File f = new File(dirs.getDir(getProject()), files[i]);
                System.err.println("dir: '" + files[i] + "'->" + f.getAbsolutePath());
                sources.add(f);
            }
        }
        sources.add(getInputDir());
        final OCLCodeGenerator generator = new OCLCodeGenerator(sources);
        if (getAssertionStyle() == null) {
            generator.setAssertionStyle(getAssertionStyle());
        }
        Path p = getClasspath();
        if (p != null) {
            String[] l = p.list();
            URL[] cp = new URL[l.length];
            for (int i = 0; i < l.length; i++) {
                try {
                    cp[i] = new URL(l[i]);
                } catch (MalformedURLException e) {
                    try {
                        cp[i] = new File(l[i]).toURL();
                    } catch (MalformedURLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            generator.setClassPath(cp);
        }
        DirectoryScanner input = getDirectoryScanner(getInputDir());
        String[] inputFiles = input.getIncludedFiles();
        for (int i = 0; i < inputFiles.length; i++) {
            final File f = new File(getInputDir(), inputFiles[i]);
            if (!f.exists()) {
                throw new IllegalArgumentException("input-file '" + f.getAbsolutePath() + "' (relative path='" + inputFiles[i] + "')does not exist!");
            }
            if (!f.isFile()) {
                throw new IllegalArgumentException("input-file '" + f.getAbsolutePath() + "' is no file!");
            }
            try {
                generator.generateForSource(f, getTargetDir());
            } catch (Throwable e) {
                System.err.println(Messages.getString("Message-HowToReportBug"));
                e.printStackTrace();
            }
        }
    }

    /**
     * @return Either "java" or "exception"
     */
    public final ASSERTIONSTYLE getAssertionStyle() {
        return myAssertionStyle;
    }

    /**
     * @param assertionStyle Either "java" or "exception"
     */
    public final void setAssertionStyle(final String assertionStyle) {
        if (assertionStyle != null && ASSERTIONSTYLE.getByParameterValue(assertionStyle) == null) {
            throw new IllegalArgumentException(Messages.getString("Problem-IllegalAssertionStyle", Arrays.toString(ASSERTIONSTYLE.values()), assertionStyle));
        }
        this.myAssertionStyle = ASSERTIONSTYLE.getByParameterValue(assertionStyle);
    }

    /**
     * @return an empty Path-instance
     */
    public Path createClasspath() {
        Path p = new Path(getProject());
        setClasspath(p);
        return p;
    }

    /**
     * @return Returns the classPath.
     */
    public final Path getClasspath() {
        return myClassPath;
    }

    /**
     * @param classPath The classPath to set.
     */
    public final void setClasspath(final Path classPath) {
        this.myClassPath = classPath;
    }
}
