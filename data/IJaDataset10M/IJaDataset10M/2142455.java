package net.sf.sanity4j.maven.plugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Locale;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import net.sf.sanity4j.util.QaLogger;
import net.sf.sanity4j.util.QaLoggerMavenImpl;
import net.sf.sanity4j.workflow.QAProcessor;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.Site;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;

/**
 * <p>
 * This class is a Maven plugin that runs Sanity4J against a project's source
 * code and classes.
 * </p>
 *
 * <p>
 * This task simply configures up a {@link QAProcessor} and then runs it in the
 * {@link #executeReport()} method.
 * </p>
 *
 * @goal sanity4j
 *
 * @author Darian Bridge
 * @since Sanity4J 1.0
 *
 */
public class RunQAMojo extends AbstractMavenReport {

    /** Mojo and goal to download historical coverage stats. */
    private static final String DOWNLOAD_SINGLE_MOJO = "org.codehaus.mojo:wagon-maven-plugin:1.0-beta-3:download-single";

    /**
     * <i>Maven Internal</i>: Project to interact with.
     *
     * @parameter expression="${project}"
     * @readonly
     */
    private MavenProject project;

    /**
     * The projects in the reactor for aggregation report.
     * 
     * @parameter expression="${reactorProjects}"
     * @readonly
     */
    private List<MavenProject> reactorProjects;

    /**
     * Flag to indicate that this mojo should run it in a multi module way.
     *
     * @parameter
     */
    private boolean aggregate;

    /**
     * <i>Maven Internal</i>: The Doxia Site Renderer.
     *
     * @component
     */
    private Renderer siteRenderer;

    /**
     * The source file paths for analysis.
     *
     * @parameter
     */
    private String[] sources;

    /**
     * The class file paths for analysis.
     *
     * @parameter
     */
    private String[] classes;

    /**
     * The library file paths for analysis.
     *
     * @parameter
     */
    private String[] libraries;

    /**
     * The location of the directory containing the various tools.
     *
     * @parameter
     * @required
     */
    private String productsDir;

    /**
     * The directory in which to place report output.
     *
     * @parameter default-value="${project.reporting.outputDirectory}/sanity4j"
     */
    private String reportDir;

    /**
     * The file containing the jUnit coverage data.
     *
     * @parameter
     *            default-value="${project.build.directory}/cobertura/cobertura.ser"
     */
    private String coverageDataFile;

    /**
     * The file containing the merged jUnit coverage data.
     *
     * @parameter
     *            default-value="${project.build.directory}/cobertura/cobertura-merged.ser"
     */
    private String coverageMergeDataFile;

    /**
     * The summary data file, if used.
     *
     * @parameter default-value=
     *            "${project.reporting.outputDirectory}/${project.name}_analyse_summary.csv"
     */
    private String summaryDataFile;

    /**
     * The path location to the external properties file (sanity4j.properties).
     * 
     * @parameter default-value= ""
     */
    private String externalPropertiesPath;

    /**
     * The temporary directory.
     *
     * @parameter default-value="${project.build.directory}/sanity4jAnalysis"
     */
    private File tempDir;

    /**
     * The java runtime to use when running external tasks.
     *
     * @parameter
     */
    private final String javaRuntime = QAProcessor.DEFAULT_JAVA_RUNTIME;

    /**
     * If true, the raw tool output is included in the report directory.
     *
     * @parameter
     */
    private final boolean includeToolOutput = false;

    /**
     * The number of threads to use to run the tools and produce the report
     * output.
     *
     * @parameter
     */
    private final int numThreads = 1;

    /**
     * Whether to use historical statistics when generating sanity4j reports.
     *
     * @parameter default-value="true"
     */
    private boolean useHistory;

    /**
     * Override the default constructor to initialise the logger. No logging
     * methods should be called before this method.
     */
    public RunQAMojo() {
        QaLogger.setLogger(new QaLoggerMavenImpl(this));
    }

    /**
     * Executes this mojo, invoking the {@link QAProcessor} which has already
     * been configured by Maven using either the annotations or the pom (through
     * reflection).
     *
     * @param locale
     *            The locale
     */
    public void executeReport(final Locale locale) throws MavenReportException {
        if (aggregate && !getProject().isExecutionRoot()) {
            getLog().info(getProject().getName() + " aggregation is on, but this is not the execution root. Skipping 'sanity4j' check.");
            File index = new File(reportDir, "index.html");
            writeTextFile(index, " ");
            return;
        }
        boolean aggregating = aggregate && getProject().isExecutionRoot();
        QAProcessor sanity4j = new QAProcessor();
        if (aggregating) {
            for (MavenProject reactorProject : reactorProjects) {
                sanity4j.getConfig().addSourcePath(reactorProject.getBasedir() + "/src");
            }
        } else if (sources == null) {
            sanity4j.getConfig().addSourcePath(getProject().getBasedir() + "/src");
        } else {
            for (String source : sources) {
                sanity4j.getConfig().addSourcePath(source);
            }
        }
        boolean sourceFileDetected = false;
        for (String srcDir : sanity4j.getConfig().getSourceDirs()) {
            if (checkForSource(new File(srcDir))) {
                sourceFileDetected = true;
                break;
            }
        }
        if (!sourceFileDetected) {
            getLog().info(getProject().getName() + " contains no source code. Skipping 'sanity4j' check.");
            return;
        }
        if (aggregating) {
            for (MavenProject reactorProject : reactorProjects) {
                sanity4j.getConfig().addClassPath(reactorProject.getBasedir() + "/target/classes");
                sanity4j.getConfig().addClassPath(reactorProject.getBasedir() + "/target/test-classes");
            }
        } else if (classes == null) {
            sanity4j.getConfig().addClassPath(getProject().getBasedir() + "/target/classes");
            sanity4j.getConfig().addClassPath(getProject().getBasedir() + "/target/test-classes");
        } else {
            for (String clazz : classes) {
                sanity4j.getConfig().addClassPath(clazz);
            }
        }
        if (aggregating) {
            for (MavenProject reactorProject : reactorProjects) {
                try {
                    for (Object o : reactorProject.getTestClasspathElements()) {
                        sanity4j.getConfig().addLibraryPath(o.toString());
                    }
                } catch (DependencyResolutionRequiredException e) {
                    getLog().warn("Unable to resolve library dependencies, analysis may not be as accurate.", e);
                }
            }
        } else if (libraries == null) {
            try {
                for (Object o : getProject().getTestClasspathElements()) {
                    sanity4j.getConfig().addLibraryPath(o.toString());
                }
            } catch (DependencyResolutionRequiredException e) {
                getLog().warn("Unable to resolve library dependencies, analysis may not be as accurate.", e);
            }
        } else {
            for (String library : libraries) {
                sanity4j.getConfig().addLibraryPath(library);
            }
        }
        if (aggregating) {
            for (MavenProject reactorProject : reactorProjects) {
                sanity4j.getConfig().addCoverageDataFile(reactorProject.getBasedir() + "/target/cobertura/cobertura.ser");
            }
        } else {
            sanity4j.getConfig().setCoverageDataFile(coverageDataFile);
        }
        sanity4j.getConfig().setCoverageMergeDataFile(coverageMergeDataFile);
        sanity4j.getConfig().setIncludeToolOutput(includeToolOutput);
        sanity4j.getConfig().setJavaRuntime(javaRuntime);
        sanity4j.getConfig().setNumThreads(numThreads);
        sanity4j.getConfig().setProductsDir(productsDir);
        sanity4j.getConfig().setReportDir(reportDir);
        sanity4j.getConfig().setSummaryDataFile(summaryDataFile.replaceAll(" ", ""));
        sanity4j.getConfig().setTempDir(tempDir);
        sanity4j.getConfig().setExternalPropertiesPath(externalPropertiesPath);
        if (useHistory) {
            retrieveSanity4jStats();
        }
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        sanity4j.run();
        Thread.currentThread().setContextClassLoader(oldLoader);
    }

    /**
     * If available, retrieves sanity4j historical results from 'site' repository.
     */
    private void retrieveSanity4jStats() {
        try {
            DistributionManagement distributionManagement = project.getDistributionManagement();
            if (distributionManagement != null) {
                Site site = distributionManagement.getSite();
                if (site != null) {
                    InvocationRequest request = new DefaultInvocationRequest();
                    request.setGoals(Collections.singletonList(DOWNLOAD_SINGLE_MOJO));
                    Properties props = new Properties();
                    String siteUrl = site.getUrl();
                    props.put("wagon.url", siteUrl);
                    String dataFileName = summaryDataFile.replaceAll(" ", "");
                    props.put("wagon.fromFile", new File(dataFileName).getName());
                    props.put("wagon.toDir", project.getReporting().getOutputDirectory());
                    request.setProperties(props);
                    getLog().info("Retrieveing 'sanity4j' statistics from URL: " + props);
                    Invoker invoker = new DefaultInvoker();
                    invoker.execute(request);
                } else {
                    getLog().info("'Site' distibution management not defined. No build history will be graphed. Continuing 'sanity4j' checks.");
                }
            } else {
                getLog().info("'Site' distibution management not defined. No build history will be graphed. Continuing 'sanity4j' checks.");
            }
        } catch (Exception e) {
            getLog().error("Unable to retrieve extisting report statistics. Continuing 'sanity4j' checks.", e);
        }
    }

    /**
     * Recursive check for existence of .java file.
     *
     * @param srcFile
     *            the <code>File</code> to start .java check from
     * @return true if .java file exists; otherwise false
     */
    private boolean checkForSource(final File srcFile) {
        if (getLog().isDebugEnabled()) {
            getLog().debug("checking if '.java' file: Location: " + srcFile.getAbsolutePath());
        }
        boolean srcFileMatch = false;
        if (srcFile.isDirectory()) {
            File[] srcFiles = srcFile.listFiles();
            for (File files : srcFiles) {
                if (checkForSource(files)) {
                    srcFileMatch = true;
                    break;
                }
            }
        }
        if (srcFile.getName().endsWith(".java")) {
            srcFileMatch = true;
            getLog().debug("Found '.java' file. Remaining recursive call will be skipped");
        }
        return srcFileMatch;
    }

    /**
     * Write out some small text to a file.
     * 
     * @param file The file to write to.
     * @param text The text to write to that file.
     */
    private void writeTextFile(final File file, final String text) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(text);
            writer.close();
        } catch (IOException ex) {
            getLog().warn("Unable to write file: " + file.getName());
        }
    }

    /**
     * @see org.apache.maven.reporting.AbstractMavenReport#getProject()
     *
     * @return the maven project.
     */
    @Override
    public MavenProject getProject() {
        return project;
    }

    /**
     * @param project
     *            the maven project.
     */
    public void setProject(final MavenProject project) {
        this.project = project;
    }

    /**
     * @see org.apache.maven.reporting.AbstractMavenReport#getOutputDirectory()
     *
     * @return the output directory.
     */
    @Override
    protected String getOutputDirectory() {
        return reportDir;
    }

    /**
     * @see org.apache.maven.reporting.AbstractMavenReport#getSiteRenderer()
     *
     * @return the site renderer.
     */
    @Override
    protected Renderer getSiteRenderer() {
        return siteRenderer;
    }

    /**
     * @param siteRenderer
     *            the site renderer.
     */
    public void setSiteRenderer(final Renderer siteRenderer) {
        this.siteRenderer = siteRenderer;
    }

    /**
     * @see org.apache.maven.reporting.MavenReport#getDescription(java.util.Locale)
     *
     * @param locale
     *            the locale
     * @return the description.
     */
    public String getDescription(final Locale locale) {
        return getBundle(locale).getString("report.sanity4j.description");
    }

    /**
     * @see org.apache.maven.reporting.MavenReport#getName(java.util.Locale)
     *
     * @param locale
     *            the locale
     * @return the name.
     */
    public String getName(final Locale locale) {
        return getBundle(locale).getString("report.sanity4j.name");
    }

    /**
     * @see org.apache.maven.reporting.MavenReport#getOutputName()
     *
     * @return the output name.
     */
    public String getOutputName() {
        return "sanity4j/index";
    }

    /**
     * Indicates that this report is external so that the generated index.html
     * file is not overwritten by the default maven template.
     *
     * @see org.apache.maven.reporting.MavenReport#isExternalReport()
     *
     * @return true to indicate that the report is external.
     */
    public boolean isExternalReport() {
        return true;
    }

    /**
     * Gets the resource bundle for the report text.
     *
     * @param locale
     *            The locale for the report, must not be <code>null</code>.
     * @return The resource bundle for the requested locale.
     */
    private ResourceBundle getBundle(final Locale locale) {
        return ResourceBundle.getBundle("sanity4j-maven-report", locale, getClass().getClassLoader());
    }
}
