package org.ji18n.tool.bundle;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.ji18n.core.log.LogServiceFactory;
import org.ji18n.core.util.Classes;
import org.ji18n.tool.log.AntLogService;

/**
 * @version $Id: BundleTask.java 159 2008-07-03 01:28:51Z david_ward2 $
 * @author david at ji18n.org
 */
public class BundleTask extends MatchingTask {

    private boolean verbose = false;

    private File outputDirectory = new File("classes");

    private boolean generateji18nxml = true;

    private String ji18nxml = "META-INF/ji18n.xml";

    private String container = "default";

    private String repository = "Repository";

    public BundleTask() {
        super();
        setIncludes("**/*.class");
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void setGenerateji18nxml(boolean generateji18nxml) {
        this.generateji18nxml = generateji18nxml;
    }

    public void setJi18nxml(String ji18nxml) {
        this.ji18nxml = ji18nxml;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    @Override
    public void execute() throws BuildException {
        ClassLoader previous = Classes.setThreadClassLoader(getClass());
        LogServiceFactory.setLogServiceOverride(new AntLogService(this));
        try {
            BundleTool tool = new BundleTool();
            tool.setVerbose(verbose);
            DirectoryScanner ds = getDirectoryScanner(outputDirectory);
            ds.scan();
            tool.setIncludedFileNames(ds.getIncludedFiles());
            tool.setOutputDirectory(outputDirectory);
            tool.setGenerateji18nxml(generateji18nxml);
            tool.setJi18nxml(ji18nxml);
            tool.setContainer(container);
            tool.setRepository(repository);
            tool.execute();
        } catch (Throwable t) {
            t.printStackTrace();
            throw new BuildException(t, getLocation());
        } finally {
            LogServiceFactory.setLogServiceOverride(null);
            Classes.setThreadClassLoader(previous);
        }
    }
}
