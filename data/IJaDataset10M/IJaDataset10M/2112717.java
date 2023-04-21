package de.fuh.xpairtise.plugin.core.resources.vc;

import java.util.HashMap;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import de.fuh.xpairtise.common.Constants;
import de.fuh.xpairtise.common.LogConstants;
import de.fuh.xpairtise.common.XPLog;
import de.fuh.xpairtise.plugin.core.resources.ResourceException;
import de.fuh.xpairtise.plugin.core.resources.util.WorkspaceSpy;

/**
 * This class provides static factory methods to produce appropriate
 * <code>IVersionControlClient</code> instances for given projects or
 * repository provider IDs.
 */
public class VersionControlClientFactory {

    private static final WorkspaceSpy spy = new WorkspaceSpy();

    private static final HashMap<IProject, IVersionControlClient> instances = new HashMap<IProject, IVersionControlClient>();

    private static final String EXTENSION = "de.fuh.xpairtise.vcClients";

    private VersionControlClientFactory() {
    }

    /**
   * Returns an appropriate <code>IVersionControlClient</code> matching the
   * <code>RepositoryProvider</code> associated with the given project if the
   * type is supported. The returned instance will already be associated with
   * the project, i.e. fully initialized.
   * 
   * @param project
   *          the project to return an <code>IVersionControlClient</code> for
   * @return the requested <code>IVersionControlClient</code> or
   *         <code>null</code>
   */
    public static IVersionControlClient getClient(IProject project) {
        if (!project.isAccessible()) {
            return null;
        }
        if (!project.isOpen()) {
            try {
                project.open(null);
            } catch (CoreException c) {
                if (XPLog.isDebugEnabled()) {
                    XPLog.printDebug(LogConstants.LOG_PREFIX_VERSIONCONTROLCLIENTFACTORY + "Failed to open project: " + project.getName() + ". Reason: " + c.getMessage());
                }
            }
        }
        String providerID = spy.getTeamProviderID(project);
        if (providerID != null) {
            IVersionControlClient client = instances.get(project);
            if (client != null) {
                if (client.getTeamProviderID().equals(providerID)) {
                    return client;
                } else {
                    instances.remove(project);
                }
            }
            client = getClientByType(providerID);
            if (client != null) {
                try {
                    client.init(project);
                    instances.put(project, client);
                    return client;
                } catch (ResourceException r) {
                    if (XPLog.isDebugEnabled()) {
                        XPLog.printDebug(LogConstants.LOG_PREFIX_VERSIONCONTROLCLIENTFACTORY + "Failed to create version control client for project: " + project.getName() + ", reason: " + r.getMessage());
                    }
                }
            }
        }
        return null;
    }

    /**
   * Returns an appropriate <code>IVersionControlClient</code> matching the
   * <code>RepositoryProvider</code> associated with the given project if the
   * type is supported. The returned instance will already be associated with
   * the project, i.e. fully initialized.
   * 
   * @param projectName
   *          the name of the project to return an
   *          <code>IVersionControlClient</code> for
   * @return the requested <code>IVersionControlClient</code> or
   *         <code>null</code>
   */
    public static IVersionControlClient getClientByName(String projectName) {
        IProject project = spy.getProject(projectName);
        if (project.isAccessible()) {
            return getClient(project);
        }
        return null;
    }

    /**
   * Returns an appropriate <code>IVersionControlClient</code> matching the
   * given type if a matching type exists. The returned instance is not yet
   * associated with any project, i.e. not completely initialized, but can still
   * be used to check out projects.
   * 
   * @param type
   *          the type identifying the requested client. Must match one of the
   *          "providerType" and "providerID" fields as specified by the
   *          de.fuh.xpairtise.vcClients extension point.
   * @return the requested <code>IVersionControlClient</code> or
   *         <code>null</code>
   */
    public static IVersionControlClient getClientByType(String type) {
        IConfigurationElement element = getExtension(type);
        if (element != null) {
            try {
                return (IVersionControlClient) element.createExecutableExtension("class");
            } catch (CoreException c) {
                if (XPLog.isDebugEnabled()) {
                    XPLog.printDebug(LogConstants.LOG_PREFIX_VERSIONCONTROLCLIENTFACTORY + "Failed to create instance of class: " + element.getAttribute("class"));
                }
            }
        }
        return null;
    }

    /**
   * Checks whether the given project type is known, i.e. can be handled by this
   * installation.
   * 
   * @param type
   *          the project type identifier to check for
   * @return true if the given project type is known, false otherwise
   */
    public static boolean isTypeKnown(String type) {
        return type.equals(Constants.PROJECTTYPE_NOVC) || (getExtension(type) != null);
    }

    private static IConfigurationElement getExtension(String type) {
        for (IConfigurationElement element : Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION)) {
            if (element.getAttribute("providerID").equals(type) || element.getAttribute("providerType").equals(type)) {
                return element;
            }
        }
        return null;
    }
}
