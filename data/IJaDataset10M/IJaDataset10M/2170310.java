package com.volantis.testtools.mock.ant;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.volantis.testtools.mock.generator.GeneratorLogger;
import com.volantis.testtools.mock.generator.MockObjectPlugin;
import com.volantis.testtools.mock.generator.UpToDateChecker;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MockGeneratorTask extends Task {

    List<FileSet> filesets;

    File todir;

    Path classpath;

    private boolean force;

    private String sourceEncoding = "ISO-8859-1";

    private String outputEncoding;

    public MockGeneratorTask() {
        filesets = new ArrayList<FileSet>();
    }

    public void addFileset(FileSet fileSet) {
        filesets.add(fileSet);
    }

    public void setTodir(File todir) {
        this.todir = todir;
    }

    public Path createClasspath() {
        if (this.classpath == null) {
            this.classpath = new Path(getProject());
        }
        return this.classpath.createPath();
    }

    public void setUptodatefile(File uptodateFile) {
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public void execute() throws BuildException {
        if (todir == null) {
            throw new BuildException("todir must be specified");
        }
        try {
            JavaDocBuilder qdoxBuilder = new JavaDocBuilder();
            qdoxBuilder.setEncoding(sourceEncoding);
            for (FileSet fileset : filesets) {
                DirectoryScanner scanner = fileset.getDirectoryScanner();
                File baseDir = scanner.getBasedir();
                for (String path : scanner.getIncludedFiles()) {
                    File file = new File(baseDir, path);
                    qdoxBuilder.addSource(file);
                }
            }
            JavaClass[] classes = qdoxBuilder.getClasses();
            if (classes.length != 0) {
                UpToDateChecker generatorChecker;
                if (force) {
                    generatorChecker = new UpToDateChecker(Long.MAX_VALUE);
                } else {
                    generatorChecker = new UpToDateChecker(0L);
                }
                Arrays.sort(classes);
                if (outputEncoding == null) {
                    outputEncoding = this.sourceEncoding;
                }
                String[] locations = classpath.list();
                URL[] urls = new URL[locations.length];
                for (int i = 0; i < locations.length; i++) {
                    String location = locations[i];
                    urls[i] = new File(location).toURL();
                }
                ClassLoader loader = new URLClassLoader(urls);
                GeneratorLogger generatorLogger = new GeneratorLogger() {

                    public void warn(String message) {
                        log(message, Project.MSG_WARN);
                    }

                    public void info(String message) {
                        log(message, Project.MSG_INFO);
                    }

                    public void debug(String message) {
                        log(message, Project.MSG_DEBUG);
                    }
                };
                MockObjectPlugin plugin = new MockObjectPlugin(outputEncoding, todir, generatorChecker, loader, generatorLogger);
                plugin.start(classes);
            }
        } catch (IOException e) {
            throw new BuildException(e);
        }
    }
}
