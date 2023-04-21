package net.sourceforge.basher.maven.plugins.report;

import java.util.*;
import java.io.File;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.apache.maven.project.MavenProject;

/**
 * Creates a report from a initial Basher run.
 *
 * @author Johan Lindquist
 * @version $Revision$
 * @goal report
 * @execute phase="test" lifecycle="basher"
 */
public class BasherReportMojo extends AbstractMavenReport {

    /**
     * Location where generated html will be created.
     *
     * @parameter expression="${project.reporting.outputDirectory}"
     */
    private File outputDirectory;

    /**
     * Doxia Site Renderer
     *
     * @component
     */
    private Renderer siteRenderer;

    /**
     * Maven Project
     *
     * @parameter expression="${project}"
     * @required @readonly
     */
    private MavenProject project;

    /**
     * Directory containing the XML report files that will be parsed and rendered to HTML format.
     *
     * @parameter default-value="${project.reporting.outputDirectory}/basher-reports"
     */
    private File reportsDirectory;

    /**
     * The name of the directory to use for the report.
     *
     * @parameter expression="${outputName}" default-value="basher-report"
     * @required
     */
    private String outputName;

    protected Renderer getSiteRenderer() {
        return siteRenderer;
    }

    protected String getOutputDirectory() {
        return outputDirectory.getAbsolutePath();
    }

    protected MavenProject getProject() {
        return project;
    }

    protected void executeReport(final Locale locale) throws MavenReportException {
        if (reportsDirectory == null) {
        }
        BasherReportGenerator report = new BasherReportGenerator(reportsDirectory, locale);
        report.doGenerateReport(getBundle(locale), getSink());
    }

    public String getOutputName() {
        return outputName;
    }

    public String getName(final Locale locale) {
        return getBundle(locale).getString("report.basher.name");
    }

    public String getDescription(final Locale locale) {
        return getBundle(locale).getString("report.basher.description");
    }

    private ResourceBundle getBundle(final Locale locale) {
        return ResourceBundle.getBundle("basher-report", locale, this.getClass().getClassLoader());
    }
}
