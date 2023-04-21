package de.fuh.xpairtise.plugin.core.resources;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import de.fuh.xpairtise.common.LogConstants;
import de.fuh.xpairtise.common.XPLog;
import de.fuh.xpairtise.common.replication.elements.ReplicatedProjectInfo;
import de.fuh.xpairtise.common.replication.elements.ReplicatedVCProjectInfo;
import de.fuh.xpairtise.plugin.core.resources.util.WorkspaceSpy;
import de.fuh.xpairtise.plugin.core.resources.vc.IVersionControlClient;
import de.fuh.xpairtise.plugin.core.resources.vc.VersionControlClientFactory;
import de.fuh.xpairtise.plugin.util.MonitorTools;

/**
 * A factory to create suitable <code>ISharedProject</code> instances for
 * given <code>IProject</code> instances.
 */
public class SharedProjectFactory {

    private static final WorkspaceSpy spy = new WorkspaceSpy();

    private SharedProjectFactory() {
    }

    /**
   * Creates a suitable <code>ISharedProject</code> instance for the project
   * described by the given <code>ReplicatedProjectInfo</code>. The created
   * type is selected based on the type of the given project.
   * 
   * @param projectInfo
   *          a <code>ReplicatedProjectInfo</code> element describing the
   *          project to create a <code>ISharedProject</code> for
   * @param monitor
   *          a progress monitor to use
   * @return a <code>ISharedProject</code> instance representing the given
   *         project
   * @throws ResourceException
   *           if the given project doesn't exist or is closed
   */
    public static ISharedProject createSharedProject(ReplicatedProjectInfo projectInfo, IProgressMonitor monitor) throws ResourceException {
        try {
            String name = projectInfo.getName();
            IVersionControlClient vcClient = null;
            monitor.beginTask("Creating shared project for project: " + name, 150);
            IProject project = spy.getProject(name);
            if (project.exists()) {
                if (!project.isOpen()) {
                    project.open(MonitorTools.subMonitorFor(monitor, 15));
                } else {
                    monitor.worked(15);
                }
                if (!spy.getProjectType(project).equals(projectInfo.getType())) {
                    if (XPLog.isDebugEnabled()) {
                        XPLog.printDebug(LogConstants.LOG_PREFIX_SHAREDPROJECTFACTORY + "Project: " + name + " exists but is of the wrong type. Deleting the old project.");
                    }
                    project.delete(true, true, MonitorTools.subMonitorFor(monitor, 10));
                } else {
                    if (projectInfo instanceof ReplicatedVCProjectInfo) {
                        vcClient = VersionControlClientFactory.getClient(project);
                        if (vcClient != null) {
                            String revision = vcClient.getProjectRevision();
                            String location = vcClient.getRepositoryLocation();
                            if (location == null || !location.equals(((ReplicatedVCProjectInfo) projectInfo).getUrl())) {
                                if (XPLog.isDebugEnabled()) {
                                    XPLog.printDebug(LogConstants.LOG_PREFIX_SHAREDPROJECTFACTORY + "Project: " + name + " has the wrong repository location. Deleting the old project.");
                                }
                                project.delete(true, true, MonitorTools.subMonitorFor(monitor, 10));
                            } else if (revision == null || !revision.equals(((ReplicatedVCProjectInfo) projectInfo).getRev())) {
                                if (XPLog.isDebugEnabled()) {
                                    XPLog.printDebug(LogConstants.LOG_PREFIX_SHAREDPROJECTFACTORY + "Project: " + name + " has the wrong revision. Updating to expected revision.");
                                }
                                vcClient.updateProject(((ReplicatedVCProjectInfo) projectInfo).getRev(), MonitorTools.subMonitorFor(monitor, 10));
                            }
                        }
                    } else {
                        monitor.worked(10);
                    }
                }
            } else {
                monitor.worked(25);
            }
            if (!project.isAccessible()) {
                if (XPLog.isDebugEnabled()) {
                    XPLog.printDebug(LogConstants.LOG_PREFIX_SHAREDPROJECTFACTORY + "Project: " + name + " doesn't exist yet. Creating it.");
                }
                if (projectInfo instanceof ReplicatedVCProjectInfo) {
                    vcClient = VersionControlClientFactory.getClientByType(projectInfo.getType());
                    if (vcClient == null) {
                        throw new ResourceException(LogConstants.LOG_PREFIX_SHAREDPROJECTFACTORY + "failed to create version control client of type: " + projectInfo.getType());
                    }
                    ReplicatedVCProjectInfo vcInfo = (ReplicatedVCProjectInfo) projectInfo;
                    monitor.setTaskName("Checking out project: " + name + " from " + vcClient.getVersionControlType());
                    vcClient.checkoutProject(vcInfo.getUrl(), name, vcInfo.getRev(), MonitorTools.subMonitorFor(monitor, 100));
                } else {
                    monitor.setTaskName("Creating project: " + name);
                    project.create(MonitorTools.subMonitorFor(monitor, 90));
                    project.open(MonitorTools.subMonitorFor(monitor, 10));
                }
            }
            vcClient = VersionControlClientFactory.getClient(project);
            if (vcClient != null) {
                if (XPLog.isDebugEnabled()) {
                    XPLog.printDebug(LogConstants.LOG_PREFIX_SHAREDPROJECTFACTORY + "creating new SharedVCProject instance for project: " + projectInfo.getName());
                }
                return new SharedVCProject(project, (ReplicatedVCProjectInfo) projectInfo, vcClient);
            } else {
                if (XPLog.isDebugEnabled()) {
                    XPLog.printDebug(LogConstants.LOG_PREFIX_SHAREDPROJECTFACTORY + "creating new SharedProject instance for project: " + projectInfo.getName());
                }
                return new SharedProject(project, projectInfo);
            }
        } catch (CoreException c) {
            throw new ResourceException(c);
        } finally {
            monitor.done();
        }
    }
}
