package de.fuh.xpairtise.plugin.core.resources;

import java.util.HashMap;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.team.core.subscribers.ISubscriberChangeEvent;
import org.eclipse.team.core.subscribers.ISubscriberChangeListener;
import org.eclipse.team.core.subscribers.Subscriber;
import de.fuh.xpairtise.common.LogConstants;
import de.fuh.xpairtise.common.XPLog;
import de.fuh.xpairtise.plugin.core.ClientApplication;
import de.fuh.xpairtise.plugin.core.IClientUserManager;
import de.fuh.xpairtise.plugin.core.resources.util.IVersionControlUtils;
import de.fuh.xpairtise.plugin.util.ClientXPLog;

/**
 * This class handles resource changes happening within the currently shared
 * projects.
 */
public class ResourceChangeHandler {

    private static ResourceChangeHandler instance;

    private final ClientResourceManager clientResourceManager;

    private IResourceChangeListener resourceChangeListener;

    private IClientUserManager userManager;

    private final HashMap<Subscriber, SubscriberChangeListener> subscriberChangeListeners = new HashMap<Subscriber, SubscriberChangeListener>();

    private ResourceChangeHandler() {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_RESOURCECHANGE_HANDLER + "Starting.");
        }
        clientResourceManager = ClientResourceManager.getInstance();
    }

    /**
   * Creates the singleton <code>ResourceChangeHandler</code> instance if it
   * doesn't exist.
   */
    public static void createInstance() {
        if (instance == null) {
            instance = new ResourceChangeHandler();
        }
    }

    /**
   * Gets the singleton <code>ResourceChangeHandler</code> instance if it
   * exists.
   * 
   * @return the singleton <code>ResourceChangeHandler</code> instance
   */
    public static ResourceChangeHandler getInstance() {
        return instance;
    }

    /**
   * Attaches the resource change listener.
   */
    public void attachResourceChangeListener() {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_RESOURCECHANGE_HANDLER + "Attaching resource change listener.");
        }
        userManager = ClientApplication.getInstance().getUserManager();
        if (resourceChangeListener == null) {
            resourceChangeListener = new IResourceChangeListener() {

                public void resourceChanged(IResourceChangeEvent event) {
                    try {
                        event.getDelta().accept(new ResourceDeltaVisitor());
                    } catch (CoreException c) {
                        ClientXPLog.logException(0, "ResourceChangeHandler", "Failed to handle resource change.", c, false);
                    }
                }
            };
        }
        ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeListener, IResourceChangeEvent.PRE_BUILD);
    }

    /**
   * Attaches a new SubscriberChangeListener to the given
   * <code>Subscriber</code> if none is attached yet.
   * 
   * @param subscriber
   *          the <code>Subscriber</code> to attach a SubscriberChangeListener
   *          to
   */
    public synchronized void attachSubscriberChangeListener(Subscriber subscriber) {
        if (!subscriberChangeListeners.containsKey(subscriber)) {
            if (XPLog.isDebugEnabled()) {
                XPLog.printDebug(LogConstants.LOG_PREFIX_RESOURCECHANGE_HANDLER + "Attaching new SubscriberChangeListener instance to subscriber: " + subscriber.getName());
            }
            SubscriberChangeListener listener = new SubscriberChangeListener();
            subscriber.addListener(listener);
            subscriberChangeListeners.put(subscriber, listener);
        }
    }

    /**
   * Detaches the resource change listener.
   */
    public void detachResourceChangeListener() {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_RESOURCECHANGE_HANDLER + "Detaching resource change listener");
        }
        userManager = null;
        if (resourceChangeListener != null) {
            ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceChangeListener);
        }
    }

    private void onLocalResourceChanged(IResource resource) {
        if (userManager == null || !userManager.isInSession()) {
            return;
        }
        String path = resource.getProjectRelativePath().toString();
        String project = resource.getProject().getName().toString();
        ISharedProject sharedProject = clientResourceManager.getSharedProject(project);
        if (sharedProject != null) {
            try {
                if (!sharedProject.hasResource(path)) {
                    if (userManager.isDriving()) {
                        sharedProject.registerResource(path);
                        if (resource.getType() == IResource.FILE) {
                            clientResourceManager.sendFileAdded(sharedProject, path);
                        } else {
                            clientResourceManager.sendFolderAdded(sharedProject, path);
                        }
                    } else {
                        clientResourceManager.resetLocalResource(resource);
                    }
                } else {
                    if (resource.getType() == IResource.FOLDER && !(sharedProject instanceof SharedVCProject)) {
                        return;
                    }
                    if (!sharedProject.isInSync(path)) {
                        if (userManager.isDriving()) {
                            sharedProject.registerResource(path);
                            if (resource.getType() == IResource.FILE) {
                                clientResourceManager.sendFileChanged(sharedProject, path);
                            } else {
                                clientResourceManager.sendFolderChanged(sharedProject, path);
                            }
                        } else {
                            clientResourceManager.resetLocalResource(resource);
                        }
                    }
                }
            } catch (ResourceException r) {
                logException(0, "Failed to handle local change of resource: " + path + " belonging to project: " + project, r);
            }
        } else {
            if (XPLog.isDebugEnabled()) {
                XPLog.printDebug(LogConstants.LOG_PREFIX_RESOURCECHANGE_HANDLER + "Project: " + project + " not registered.");
            }
        }
    }

    private void onLocalResourceRemoved(IResource resource) {
        if (userManager == null || !userManager.isInSession()) {
            return;
        }
        String path = resource.getProjectRelativePath().toString();
        String project = resource.getProject().getName().toString();
        ISharedProject sharedProject = clientResourceManager.getSharedProject(project);
        if (sharedProject != null) {
            try {
                if (sharedProject.hasResource(path)) {
                    if (userManager.isDriving()) {
                        sharedProject.unregisterResource(path);
                        clientResourceManager.sendResourceRemoved(sharedProject, path);
                    } else {
                        clientResourceManager.resetLocalResource(resource);
                    }
                }
            } catch (ResourceException r) {
                logException(0, "Failed to handle local removal of resource: " + path + " belonging to project: " + project, r);
            }
        } else {
            if (XPLog.isDebugEnabled()) {
                XPLog.printDebug(LogConstants.LOG_PREFIX_RESOURCECHANGE_HANDLER + "Project: " + project + " not registered.");
            }
        }
    }

    private void onVCProjectChanged(String project) {
    }

    private void logException(int errorCode, String message, Throwable e) {
        ClientXPLog.logException(errorCode, "ResourceChangeHandler", message, e, false);
    }

    private class ResourceDeltaVisitor implements IResourceDeltaVisitor {

        public boolean visit(IResourceDelta node) {
            IResource resource = node.getResource();
            int resourceType = resource.getType();
            boolean isDerived = resource.isDerived();
            int deltaKind = node.getKind();
            if (resourceType != IResource.ROOT) {
                String project = resource.getProject().getName();
                if (!clientResourceManager.isProjectRegistered(project)) {
                    return false;
                }
                if (!isDerived && (resourceType == IResource.FILE || resourceType == IResource.FOLDER)) {
                    switch(deltaKind) {
                        case (IResourceDelta.ADDED):
                            if (XPLog.isDebugEnabled()) {
                                XPLog.printDebug(LogConstants.LOG_PREFIX_RESOURCECHANGE_HANDLER + "Handling added resource: " + resource.getProjectRelativePath().toString());
                            }
                            onLocalResourceChanged(resource);
                            break;
                        case (IResourceDelta.REMOVED):
                            if (XPLog.isDebugEnabled()) {
                                XPLog.printDebug(LogConstants.LOG_PREFIX_RESOURCECHANGE_HANDLER + "Handling removed resource: " + resource.getProjectRelativePath().toString());
                            }
                            onLocalResourceRemoved(resource);
                            break;
                        case (IResourceDelta.CHANGED):
                            if (XPLog.isDebugEnabled()) {
                                XPLog.printDebug(LogConstants.LOG_PREFIX_RESOURCECHANGE_HANDLER + "Handling changed resource: " + resource.getProjectRelativePath().toString());
                            }
                            onLocalResourceChanged(resource);
                            break;
                    }
                }
            }
            return true;
        }
    }

    private class SubscriberChangeListener implements ISubscriberChangeListener {

        public void subscriberResourceChanged(ISubscriberChangeEvent[] deltas) {
            IResource resource = null;
            String project = null;
            String path = null;
            ISharedProject sharedProject = null;
            IVersionControlUtils vcUtils = null;
            for (ISubscriberChangeEvent delta : deltas) {
                resource = delta.getResource();
                path = resource.getProjectRelativePath().toString();
                project = resource.getProject().getName();
                sharedProject = clientResourceManager.getSharedProject(project);
                if (sharedProject != null) {
                    if (XPLog.isDebugEnabled()) {
                        XPLog.printDebug(LogConstants.LOG_PREFIX_RESOURCECHANGE_HANDLER + "Sync info change: " + path);
                    }
                    vcUtils = ((SharedVCProject) sharedProject).getVCUtils();
                    if (resource.equals(resource.getProject())) {
                        if (XPLog.isDebugEnabled()) {
                            XPLog.printDebug(LogConstants.LOG_PREFIX_RESOURCECHANGE_HANDLER + "Handling sync info change for project: " + project);
                        }
                        onVCProjectChanged(project);
                    } else if (resource.isAccessible() && !resource.isTeamPrivateMember()) {
                        if (sharedProject.doesTimeStampMatch(path) && vcUtils.hasLocalRevisionChanged(path)) {
                            if (!ResourcesPlugin.getWorkspace().isTreeLocked()) {
                                if (vcUtils.hasBeenManaged(path)) {
                                    if (XPLog.isDebugEnabled()) {
                                        XPLog.printDebug(LogConstants.LOG_PREFIX_RESOURCECHANGE_HANDLER + "Resource: " + path + " was added to version control.");
                                    }
                                    onLocalResourceChanged(resource);
                                }
                            } else {
                                if (XPLog.isDebugEnabled()) {
                                    XPLog.printDebug(LogConstants.LOG_PREFIX_RESOURCECHANGE_HANDLER + "Resource tree is locked. Ignoring.");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
