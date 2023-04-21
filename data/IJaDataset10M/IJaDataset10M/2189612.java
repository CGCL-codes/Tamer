package org.unitmetrics.internal.builder;

import java.util.Map;
import org.eclipse.core.commands.State;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.unitmetrics.IAdvancedUnitMetrics;
import org.unitmetrics.IMeasurementLogic;
import org.unitmetrics.UnitMetricsPlugin;
import org.unitmetrics.internal.IStratum;
import org.unitmetrics.internal.UnitMetrics;

/** 
 * The unit metrics builder is an incremental builder dedicate to remeasure
 * resource which had changed during the last build.
 *  
 * <p>
 * The builder imposes the following contract to the metrics:<ul>
 * <li>Before a resource is handled all it's child resources are handled.<li>
 * <li>Before a metric is notified/advised to recalculate, all the metrics
 *     it depends on where calculated.</li> 
 * </p>
 * <p> 
 * The builder activates and used the BuildMonitorSchedulingRule to signal 
 * a running build process and allow the scheduling of jobs require to do not
 * conflict with the build process.
 * </p>
 * <p>
 * NOTE: The dependencies of the builder are set by the 
 * 		 UnitMetricsPlugin.injectDependencies method called
 *       by the builder on startupOnInitialize() if they were not
 *       set before.
 * </p>
 * 
 * @author Martin Kersten
 */
public class UnitMetricsBuilder extends IncrementalProjectBuilder {

    private UnitMetrics unitMetrics = null;

    private static final String IS_ANALYSING = "UnitMetricsBuilder.IS_ANALYSING";

    private static final String FULL_ANALYSIS_MARKER = "UnitMetricsBuilder.FULL_ANALYSIS_MARKER";

    private static final QualifiedName FULL_ANALYSIS_PROPERTY = new QualifiedName(UnitMetricsPlugin.getPluginId(), FULL_ANALYSIS_MARKER);

    private static final AffectedResourceAccumulator resourceAccumulator = new AffectedResourceAccumulator();

    /** Exposes the state. The state should only be used to register listeners. */
    public static final State isAnalysing = new State();

    static {
        isAnalysing.setId(IS_ANALYSING);
        isAnalysing.setValue(Boolean.TRUE);
    }

    protected void startupOnInitialize() {
        super.startupOnInitialize();
        unitMetrics = (UnitMetrics) UnitMetricsPlugin.getAdvancedUnitMetrics();
    }

    /** Toggles the isAnalysing feature. */
    public static void setIsAnalysing(boolean isAnalysing) {
        if (!UnitMetricsBuilder.isAnalysing.getValue().equals(isAnalysing)) {
            UnitMetricsBuilder.isAnalysing.setValue(isAnalysing);
            if (isAnalysing) {
                Job job = new Job("Analyse affected projects") {

                    protected IStatus run(IProgressMonitor monitor) {
                        for (IProject project : resourceAccumulator.getProjects()) {
                            touchProjectForAnalysis(project, false);
                            try {
                                project.build(INCREMENTAL_BUILD, monitor);
                            } catch (CoreException e) {
                                UnitMetricsPlugin.log("Problem while building project " + project.getName(), e);
                            }
                        }
                        return Status.OK_STATUS;
                    }
                };
                job.setUser(true);
                job.schedule();
            }
        }
    }

    public static boolean isAnalysing() {
        return (Boolean) isAnalysing.getValue();
    }

    /** Marks a project for full analysis and also touches it to ensure a build process. */
    public static void markForFullAnalysis(IProject project) {
        touchProjectForAnalysis(project, true);
    }

    private static void touchProjectForAnalysis(IProject project, boolean markForFullAnalysis) {
        try {
            if (markForFullAnalysis) project.setSessionProperty(FULL_ANALYSIS_PROPERTY, Boolean.TRUE);
            project.touch(null);
        } catch (CoreException e) {
            UnitMetricsPlugin.log("Failed to mark project '" + project.getName() + "' for full build", e);
        }
    }

    /** Returns true if the project was marked for full build and sets it to no full build state. */
    private static boolean unmarkForFullAnalysis(IProject project) {
        try {
            Boolean mark = (Boolean) project.getSessionProperty(FULL_ANALYSIS_PROPERTY);
            project.setSessionProperty(FULL_ANALYSIS_PROPERTY, null);
            return Boolean.TRUE.equals(mark);
        } catch (CoreException e) {
            UnitMetricsPlugin.log("Failed to unmark project '" + project.getName() + "' of full build", e);
            return false;
        }
    }

    protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
        int priority = Thread.currentThread().getPriority();
        unitMetrics.getResultStoreLock().acquire();
        try {
            boolean isFullBuild = isFullBuild(kind);
            unitMetrics.notifyListenersAboutMeasurementBegin(getProject(), isFullBuild);
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
            performBuild(isFullBuild, monitor);
            Thread.currentThread().setPriority(priority);
            unitMetrics.notifyListenersAboutMeasurementEnd(getProject());
        } finally {
            unitMetrics.getResultStoreLock().release();
        }
        return null;
    }

    private void performBuild(boolean performFullBuild, IProgressMonitor monitor) throws CoreException {
        IResource[] affectedResources = resourceAccumulator.getAffectedResources(this, performFullBuild);
        if (isAnalysing()) analyze(monitor, affectedResources); else resourceAccumulator.accumulate(getProject(), affectedResources);
    }

    private void analyze(IProgressMonitor monitor, IResource[] affectedResources) {
        IMeasurementLogic measurementLogic = new MeasurementLogic(unitMetrics, getProject(), monitor);
        IStratum stratum = null;
        do {
            stratum = stratum != null ? stratum.next() : unitMetrics.getStratum();
            measurementLogic.performMeasurement(affectedResources, stratum.getMetrics());
        } while (stratum.hasNext());
    }

    /** Cleans the project by simply cleaning the results associated with the project.
	 * The result changes are reported and the metrics are notified about the cleansing. */
    protected void clean(IProgressMonitor monitor) throws CoreException {
        unitMetrics.cleanProject(getProject());
    }

    /** Returns the unit metrics object. */
    public IAdvancedUnitMetrics getUnitMetrics() {
        return unitMetrics;
    }

    /** Returns true if a full build should be used in the actual situation. 
	 * <p>A full build can be advised by system or if no result has bean stored
	 * about the project or the resource delat is not available due some reasons. */
    private boolean isFullBuild(int kind) {
        boolean isMarkedForFullBuild = unmarkForFullAnalysis(getProject());
        return (kind == IncrementalProjectBuilder.FULL_BUILD || isMarkedForFullBuild || !unitMetrics.isStoringResultsOfProject(getProject()) || getDelta(getProject()) == null);
    }
}
