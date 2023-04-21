package org.mandiwala.hudson;

import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.model.Project;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.archiver.ArchiverException;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Publishes selenium reports.
 */
public class SeleniumReportPublisher extends Publisher implements Serializable {

    /** The Constant DESCRIPTOR. */
    public static final Descriptor<Publisher> DESCRIPTOR = new DescriptorImpl();

    private static final long serialVersionUID = 9102381227844971957L;

    private String[] reportBasedirs;

    /**
     * Instantiates a new selenium report publisher.
     * 
     * @param reportBasedirs
     *            the report basedirs
     */
    @DataBoundConstructor
    public SeleniumReportPublisher(String reportBasedirs) {
        if (StringUtils.isNotBlank(reportBasedirs)) {
            this.reportBasedirs = reportBasedirs.split(",");
        } else {
            this.reportBasedirs = new String[] { "." };
        }
    }

    /**
     * Gets the test results.
     * 
     * @return the test results
     */
    public String getReportBasedirs() {
        return StringUtils.join(reportBasedirs, ',');
    }

    /**
     * Gets the target.
     * 
     * @param build
     *            the build
     * 
     * @return the target
     */
    public static File getTarget(AbstractBuild<?, ?> build) {
        return new File(build.getRootDir(), "selenium-report.zip");
    }

    /**
     * {@inheritDoc}
     */
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        listener.getLogger().println("Publishing Selenium reports");
        SeleniumReport report = build.getProject().getWorkspace().act(new SeleniumReportAggregator(reportBasedirs, listener));
        try {
            report.store(getTarget(build));
        } catch (ArchiverException e) {
            throw new IOException("Failed to write report. Reason: " + e.getMessage());
        }
        listener.getLogger().println("Reports published to " + getTarget(build).getAbsolutePath());
        SeleniumReportBuildAction buildAction = new SeleniumReportBuildAction(build);
        build.getActions().add(buildAction);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Action getProjectAction(Project project) {
        return new SeleniumReportProjectAction(project);
    }

    /**
     * The publisher's descriptor.
     */
    public static class DescriptorImpl extends Descriptor<Publisher> {

        /**
         * Instantiates a new descriptor impl.
         */
        public DescriptorImpl() {
            super(SeleniumReportPublisher.class);
        }

        /**
         * Checks if is applicable.
         * 
         * @param aClass
         *            the a class
         * 
         * @return true, if is applicable
         */
        public boolean isApplicable(Class<? extends AbstractProject<?, ?>> aClass) {
            return true;
        }

        /**
         * {@inheritDoc}
         */
        public SeleniumReportPublisher newInstance(StaplerRequest req) throws FormException {
            SeleniumReportPublisher instance = req.bindParameters(SeleniumReportPublisher.class, "selenium-report.");
            return instance;
        }

        /**
         * {@inheritDoc}
         */
        public String getDisplayName() {
            return PluginImpl.DISPLAY_NAME;
        }
    }

    /**
     * {@inheritDoc}
     */
    public Descriptor<Publisher> getDescriptor() {
        return DESCRIPTOR;
    }

    /**
     * {@inheritDoc}
     */
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.BUILD;
    }
}
