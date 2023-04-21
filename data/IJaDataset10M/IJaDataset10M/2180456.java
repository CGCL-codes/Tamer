package de.fuh.xpairtise.plugin.core.resources.vc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.internal.ccvs.core.CVSException;
import org.eclipse.team.internal.ccvs.core.CVSProviderPlugin;
import org.eclipse.team.internal.ccvs.core.CVSTag;
import org.eclipse.team.internal.ccvs.core.CVSTeamProvider;
import org.eclipse.team.internal.ccvs.core.ICVSFile;
import org.eclipse.team.internal.ccvs.core.ICVSFolder;
import org.eclipse.team.internal.ccvs.core.ICVSRemoteFolder;
import org.eclipse.team.internal.ccvs.core.ICVSRemoteResource;
import org.eclipse.team.internal.ccvs.core.ICVSRepositoryLocation;
import org.eclipse.team.internal.ccvs.core.ICVSResource;
import org.eclipse.team.internal.ccvs.core.client.Command;
import org.eclipse.team.internal.ccvs.core.client.Update;
import org.eclipse.team.internal.ccvs.core.connection.CVSRepositoryLocation;
import org.eclipse.team.internal.ccvs.core.resources.CVSEntryLineTag;
import org.eclipse.team.internal.ccvs.core.resources.CVSWorkspaceRoot;
import org.eclipse.team.internal.ccvs.core.syncinfo.FolderSyncInfo;
import org.eclipse.team.internal.ccvs.core.syncinfo.MutableFolderSyncInfo;
import org.eclipse.team.internal.ccvs.core.syncinfo.MutableResourceSyncInfo;
import org.eclipse.team.internal.ccvs.core.syncinfo.ResourceSyncInfo;
import org.eclipse.team.internal.ccvs.ui.CVSUIPlugin;
import org.eclipse.team.internal.ccvs.ui.operations.AddOperation;
import org.eclipse.team.internal.ccvs.ui.operations.CheckoutSingleProjectOperation;
import org.eclipse.team.internal.ccvs.ui.operations.ReplaceOperation;
import org.eclipse.team.internal.ccvs.ui.operations.UpdateOperation;
import org.eclipse.team.internal.ccvs.ui.repo.RepositoryManager;
import de.fuh.xpairtise.common.XPLog;
import de.fuh.xpairtise.plugin.Activator;
import de.fuh.xpairtise.plugin.core.resources.ResourceException;
import de.fuh.xpairtise.plugin.util.MonitorTools;

/**
 * CVS specific implementation of the <code>IVersionControlClient</code>
 * interface.
 */
@SuppressWarnings("restriction")
public class CVSVersionControlClient extends AbstractVersionControlClient {

    private CVSTeamProvider provider;

    private CVSWorkspaceRoot cvsWorkspaceRoot;

    private static final String HEAD = "HEAD";

    private static final String DATE = "DATE";

    private static final String VERSION = "VERSION";

    private static final String BRANCH = "BRANCH";

    private static final String TRANSIENT = "TR:";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy HH:mm:ss Z", Locale.US);

    private Date projectTimeStamp = null;

    private final RepositoryManager repositoryManager;

    private final String type = "CVS";

    /**
   * Creates a new <code>CVSVersionControlClient</code> instance.
   */
    public CVSVersionControlClient() {
        super("CVSVersionControlClient");
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
        repositoryManager = CVSUIPlugin.getPlugin().getRepositoryManager();
    }

    public void init(IProject project) throws ResourceException {
        super.init(project);
        if (super.provider instanceof CVSTeamProvider) {
            provider = (CVSTeamProvider) super.provider;
        } else {
            exception("RepositoryProvider not of type: " + CVSProviderPlugin.getTypeId());
        }
        cvsWorkspaceRoot = provider.getCVSWorkspaceRoot();
    }

    public void update(String path, String revision, IProgressMonitor monitor) throws ResourceException {
        checkInitialized();
        monitor = MonitorTools.monitorFor(monitor);
        try {
            if (XPLog.isDebugEnabled()) {
                XPLog.printDebug(logPrefix + "Updating resource: " + path + " to revision: " + revision);
            }
            IResource resource = getMemberResource(path);
            if (resource != null) {
                update(resource, stringToTag(revision), monitor);
            } else {
                exception("No such resource: " + path);
            }
        } finally {
            monitor.done();
        }
    }

    public void updateProject(String revision, IProgressMonitor monitor) throws ResourceException {
        checkInitialized();
        monitor = MonitorTools.monitorFor(monitor);
        try {
            if (XPLog.isDebugEnabled()) {
                XPLog.printDebug(logPrefix + "Updating project: " + project.getName() + " to revision: " + (revision != null ? revision : "latest."));
            }
            CVSTag tag = stringToTag(revision);
            if (tag != null && tag.getType() == CVSTag.DATE) {
                projectTimeStamp = tag.asDate();
            }
            update(project, tag, MonitorTools.subMonitorFor(monitor, 90));
            if (projectTimeStamp != null) {
                monitor.subTask("Forcing correct project time stamp.");
                forceProjectTimeStamp(project, projectTimeStamp);
            }
        } finally {
            monitor.done();
        }
    }

    public String getProjectRevision() {
        if (project == null) {
            return null;
        }
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(logPrefix + "Checking for project-wide revision of project: " + getProject().getName());
        }
        ICVSFolder root = CVSWorkspaceRoot.getCVSFolderFor(project);
        try {
            FolderSyncInfo info = root.getFolderSyncInfo();
            if (info != null) {
                CVSEntryLineTag tag = info.getTag();
                if (tag != null && !tag.equals(CVSTag.HEAD)) {
                    return tagToString(tag, false);
                }
                computeProjectRevision(null);
                return tagToString(new CVSTag(projectTimeStamp), true);
            }
        } catch (CVSException c) {
            if (XPLog.isDebugEnabled()) {
                XPLog.printDebug(logPrefix + "Failed to get sync info of project root. Reason: " + c.getMessage());
            }
        }
        return null;
    }

    public String getTeamProviderID() {
        return CVSProviderPlugin.getTypeId();
    }

    public String getVersionControlType() {
        return type;
    }

    public String getRepositoryLocation() {
        if (project == null) {
            return null;
        }
        try {
            CVSRepositoryLocation location = CVSRepositoryLocation.fromString(cvsWorkspaceRoot.getRemoteLocation().getLocation(false));
            location.setUserMuteable(true);
            String repository = location.getLocation();
            String path = cvsWorkspaceRoot.getLocalRoot().getFolderSyncInfo().getRepository();
            return repository + "," + path;
        } catch (CVSException c) {
            if (XPLog.isDebugEnabled()) {
                XPLog.printDebug(logPrefix + "Failed to get repository location of project: " + getProject().getName());
            }
        }
        return null;
    }

    public void revert(String path, IProgressMonitor monitor) throws ResourceException {
        checkInitialized();
        monitor = MonitorTools.monitorFor(monitor);
        try {
            monitor.beginTask("Reverting resource: " + path + " to local base revision.", 100);
            if (XPLog.isDebugEnabled()) {
                XPLog.printDebug(logPrefix + "Reverting resource: " + path + " to local base revision.");
            }
            IResource resource = getMemberResource(path);
            if (resource != null) {
                IResource[] resources = new IResource[] { resource };
                new ReplaceOperation(null, resources, CVSTag.BASE, true).execute(MonitorTools.subMonitorFor(monitor, 80));
                removeStickyTags(resource, CVSTag.BASE, MonitorTools.subMonitorFor(monitor, 20));
                if (projectTimeStamp != null) {
                    monitor.subTask("Forcing revision time stamp of resource: " + path);
                    forceNotNewer(CVSWorkspaceRoot.getCVSResourceFor(resource), projectTimeStamp);
                }
            } else {
                exception("No such resource: " + path);
            }
        } catch (CVSException c) {
            throw new ResourceException(c);
        } catch (InterruptedException i) {
            throw new ResourceException(i);
        } finally {
            monitor.done();
        }
    }

    public void manage(String path, IProgressMonitor monitor) throws ResourceException {
        checkInitialized();
        monitor = MonitorTools.monitorFor(monitor);
        try {
            monitor.beginTask("Adding resource: " + path + " to version control.", 100);
            if (XPLog.isDebugEnabled()) {
                XPLog.printDebug(logPrefix + "Adding resource: " + path + " to version control.");
            }
            IResource resource = project.findMember(path);
            if (resource != null && resource.isAccessible()) {
                try {
                    IResource[] resources = new IResource[] { resource };
                    new AddOperation(null, AddOperation.asResourceMappers(resources)).execute(MonitorTools.subMonitorFor(monitor, 100));
                } catch (CVSException c) {
                    throw new ResourceException(c);
                } catch (InterruptedException i) {
                    throw new ResourceException(i);
                }
            } else {
                if (XPLog.isDebugEnabled()) {
                    XPLog.printDebug(logPrefix + "No such resource: " + path);
                }
            }
        } finally {
            monitor.done();
        }
    }

    public void unmanage(String path, IProgressMonitor monitor) throws ResourceException {
        checkInitialized();
        IResource resource = project.findMember(path);
        if (resource != null) {
            unmanage(resource, monitor);
        } else {
            if (XPLog.isDebugEnabled()) {
                XPLog.printDebug(logPrefix + "No such resource: " + path);
            }
        }
    }

    @Override
    protected String[] getCommitCommentTemplates() {
        return repositoryManager != null ? repositoryManager.getCommentTemplates() : null;
    }

    @Override
    protected void setCommitCommentTemplates(String[] templates) {
        if (repositoryManager == null) {
            return;
        }
        try {
            repositoryManager.replaceAndSaveCommentTemplates(templates);
        } catch (TeamException t) {
            if (XPLog.isDebugEnabled()) {
                XPLog.printDebug(logPrefix + "Failed to replace commit comment templates. Reason: " + t.getMessage());
            }
        }
    }

    public void checkoutProject(String location, String name, String revision, IProgressMonitor monitor) throws ResourceException {
        monitor = MonitorTools.monitorFor(monitor);
        try {
            monitor.beginTask("Checking out project: " + name + " from: " + location, 100);
            if (XPLog.isDebugEnabled()) {
                XPLog.printDebug(logPrefix + "Checking out project: " + name + " from: " + location);
            }
            int index = location.indexOf(",");
            if (index == -1) {
                throw new ResourceException(logPrefix + "Invalid location: " + location);
            }
            String repository = location.substring(0, index);
            String module = location.substring(index + 1);
            ICVSRepositoryLocation repositoryLocation = CVSRepositoryLocation.fromString(repository);
            for (ICVSRepositoryLocation existingLocation : CVSProviderPlugin.getPlugin().getKnownRepositories()) {
                if (existingLocation.getMethod() == repositoryLocation.getMethod() && existingLocation.getHost().equals(repositoryLocation.getHost()) && existingLocation.getPort() == repositoryLocation.getPort() && existingLocation.getRootDirectory().equals(repositoryLocation.getRootDirectory())) {
                    if (XPLog.isDebugEnabled()) {
                        XPLog.printDebug(logPrefix + "Repositoy location: " + existingLocation.getLocation(true) + " already known. Using the existing location.");
                    }
                    repositoryLocation = existingLocation;
                    break;
                }
            }
            final CVSTag tag = stringToTag(revision);
            final ICVSRemoteFolder folder = repositoryLocation.getRemoteFolder(module, tag);
            final IProject project = spy.getProject(name);
            spy.getEclipseWorkspace().run(new IWorkspaceRunnable() {

                public void run(IProgressMonitor monitor) throws CoreException {
                    try {
                        new CheckoutSingleProjectOperation(null, folder, project, null, false).execute(MonitorTools.subMonitorFor(monitor, 100));
                    } catch (InterruptedException i) {
                        throw new CoreException(new Status(Status.ERROR, Activator.getId(), 0, "Failed to checkout: " + folder.getName(), i));
                    }
                }
            }, MonitorTools.subMonitorFor(monitor, 80));
            if (tag != null) {
                if (tag instanceof TransientCVSTag) {
                    removeStickyTags(project, tag, MonitorTools.subMonitorFor(monitor, 20));
                } else {
                    monitor.worked(20);
                }
                if (tag.getType() == CVSTag.DATE) {
                    Date date = tag.asDate();
                    monitor.subTask("Forcing correct project time stamp.");
                    forceProjectTimeStamp(project, date);
                }
            }
        } catch (CVSException s) {
            throw new ResourceException(s);
        } catch (CoreException c) {
            throw new ResourceException(c);
        } finally {
            monitor.done();
        }
    }

    @Override
    protected String getLocalBaseRevision(IResource resource) {
        if (project == null) {
            return null;
        }
        if (resource instanceof IContainer) {
            try {
                IContainer container = (IContainer) resource;
                ICVSFolder folder = CVSWorkspaceRoot.getCVSFolderFor(container);
                FolderSyncInfo info = folder.getFolderSyncInfo();
                if (info != null) {
                    CVSTag tag = info.getTag();
                    if (tag != null) {
                        return tagToString(tag, false);
                    } else {
                        return tagToString(null, true);
                    }
                }
            } catch (CVSException c) {
                if (XPLog.isDebugEnabled()) {
                    XPLog.printDebug(logPrefix + "Failed to gather sync info of folder: " + (resource.equals(project) ? project.getName() : resource.getProjectRelativePath().toString()) + ", reason: " + c.getMessage());
                }
            }
        } else {
            String revision = super.getLocalBaseRevision(resource);
            if (revision != null) {
                return tagToString(new CVSTag(revision, CVSTag.VERSION), true);
            }
        }
        return null;
    }

    @Override
    protected String getRemoteRevision(IResource resource) {
        if (project == null) {
            return null;
        }
        if (resource instanceof IContainer) {
            try {
                ICVSRemoteResource remoteResource = CVSWorkspaceRoot.getRemoteResourceFor(resource);
                if (remoteResource instanceof ICVSRemoteFolder) {
                    FolderSyncInfo info = ((ICVSRemoteFolder) remoteResource).getFolderSyncInfo();
                    if (info != null) {
                        CVSTag tag = info.getTag();
                        if (tag != null) {
                            return tagToString(tag, false);
                        } else {
                            return tagToString(null, true);
                        }
                    }
                }
            } catch (CVSException c) {
                if (XPLog.isDebugEnabled()) {
                    XPLog.printDebug(logPrefix + "Failed to get sync infó for folder: " + resource.getProjectRelativePath().toString() + ", reason: " + c.getMessage());
                }
            }
        } else {
            String revision = super.getRemoteRevision(resource);
            if (revision != null) {
                return tagToString(new CVSTag(revision, CVSTag.VERSION), true);
            }
        }
        return null;
    }

    private String tagToString(CVSTag tag, boolean markTransient) {
        StringBuffer buffer = new StringBuffer();
        if (markTransient) {
            buffer.append(TRANSIENT);
        }
        if (tag != null && tag.getType() != CVSTag.HEAD) {
            switch(tag.getType()) {
                case CVSTag.BRANCH:
                    buffer.append(BRANCH);
                    break;
                case CVSTag.VERSION:
                    buffer.append(VERSION);
                    break;
                case CVSTag.DATE:
                    buffer.append(DATE);
                    break;
            }
            buffer.append(" ");
            buffer.append(tag.getName());
        } else {
            buffer.append(HEAD);
        }
        return buffer.toString();
    }

    private CVSTag stringToTag(String string) {
        if (string != null) {
            boolean trans = false;
            if (string.startsWith(TRANSIENT)) {
                string = string.substring(TRANSIENT.length());
                trans = true;
            }
            int index = string.indexOf(" ");
            int tagType = 0;
            if (index != -1) {
                String type = string.substring(0, index);
                String name = string.substring(index + 1, string.length());
                if (type.equals(VERSION)) {
                    tagType = CVSTag.VERSION;
                } else if (type.equals(BRANCH)) {
                    tagType = CVSTag.BRANCH;
                } else if (type.equals(DATE)) {
                    tagType = CVSTag.DATE;
                }
                if (trans) {
                    return new TransientCVSTag(name, tagType);
                } else {
                    return new CVSTag(name, tagType);
                }
            }
        }
        return null;
    }

    private void update(IResource resource, CVSTag tag, IProgressMonitor monitor) throws ResourceException {
        try {
            String path = resource.equals(project) ? project.getName() : resource.getProjectRelativePath().toString();
            String revision = tag != null ? tag.toString() : HEAD;
            monitor.beginTask("Updating resource: " + path + " to revision: " + revision, 100);
            IResource[] resources = new IResource[] { resource };
            Command.LocalOption[] options = new Command.LocalOption[] { Update.RETRIEVE_ABSENT_DIRECTORIES };
            removeStickyTags(resource, null, MonitorTools.subMonitorFor(monitor, 10));
            new UpdateOperation(null, resources, options, tag).execute(MonitorTools.subMonitorFor(monitor, 70));
            if (tag != null && tag instanceof TransientCVSTag) {
                removeStickyTags(resource, tag, MonitorTools.subMonitorFor(monitor, 10));
            } else {
                monitor.worked(10);
            }
            if (projectTimeStamp != null) {
                monitor.subTask("Forcing revision time stamp of resource: " + path);
                forceNotNewer(CVSWorkspaceRoot.getCVSResourceFor(resource), projectTimeStamp);
            }
            monitor.worked(5);
            refresh(resource, MonitorTools.subMonitorFor(monitor, 10));
        } catch (InterruptedException i) {
            throw new ResourceException(i);
        } catch (CVSException c) {
            throw new ResourceException(c);
        } finally {
            monitor.done();
        }
    }

    private void removeStickyTags(IResource resource, CVSTag tag, IProgressMonitor monitor) throws ResourceException {
        String path = resource.getProjectRelativePath().toString();
        monitor = MonitorTools.monitorFor(monitor);
        try {
            monitor.beginTask("Removing sticky tags of resource: " + path, 100);
            if (resource instanceof IFile) {
                ICVSFile file = CVSWorkspaceRoot.getCVSFileFor((IFile) resource);
                ResourceSyncInfo oldInfo = file.getSyncInfo();
                if (oldInfo != null) {
                    CVSTag oldTag = oldInfo.getTag();
                    if (oldTag != null && (tag == null || oldTag.equals(tag))) {
                        monitor.subTask("Removing sticky tag from file: " + path);
                        if (XPLog.isDebugEnabled()) {
                            XPLog.printDebug(logPrefix + "Removing sticky tag from file: " + path);
                        }
                        MutableResourceSyncInfo newInfo = oldInfo.cloneMutable();
                        newInfo.setTag(null);
                        file.setSyncInfo(newInfo, ICVSFile.CLEAN);
                    }
                }
                monitor.worked(100);
            } else if (resource instanceof IContainer) {
                ICVSFolder folder = CVSWorkspaceRoot.getCVSFolderFor((IContainer) resource);
                FolderSyncInfo oldInfo = folder.getFolderSyncInfo();
                if (oldInfo != null) {
                    CVSEntryLineTag oldTag = oldInfo.getTag();
                    if (oldTag != null && oldTag.equals(tag)) {
                        monitor.subTask("Removing sticky tags from folder: " + path);
                        if (XPLog.isDebugEnabled()) {
                            XPLog.printDebug(logPrefix + "Removing sticky tag on folder: " + path);
                        }
                        MutableFolderSyncInfo newInfo = oldInfo.cloneMutable();
                        newInfo.setTag(null);
                        folder.setFolderSyncInfo(newInfo);
                    }
                }
                monitor.worked(20);
                ICVSResource[] members = folder.members(ICVSFolder.EXISTING_MEMBERS);
                for (ICVSResource member : folder.members(ICVSFolder.EXISTING_MEMBERS)) {
                    removeStickyTags(member.getIResource(), tag, MonitorTools.subMonitorFor(monitor, 80 / members.length));
                }
            }
        } catch (CVSException c) {
            throw new ResourceException(c);
        } finally {
            monitor.done();
        }
    }

    private void computeProjectRevision(IProgressMonitor monitor) {
        monitor = MonitorTools.monitorFor(monitor);
        try {
            Date newest = null;
            Date current = null;
            List<IResource> members = getProjectMemberResources();
            monitor.beginTask("Computing project time stamp of project: " + project.getName(), members.size());
            ICVSFile file;
            ResourceSyncInfo info;
            for (IResource member : members) {
                monitor.subTask("Inspecting resource: " + member.getProjectRelativePath().toString());
                if (member.getType() == IResource.FILE) {
                    file = CVSWorkspaceRoot.getCVSFileFor((IFile) member);
                    info = file.getSyncInfo();
                    if (info != null) {
                        current = info.getTimeStamp();
                        if (newest == null || (current != null && current.after(newest))) {
                            newest = current;
                        }
                    }
                }
                monitor.worked(1);
            }
            long ms = newest.getTime();
            if (ms % 60000 > 0) {
                ms = ms / 60000 * 60000 + 60000;
            }
            projectTimeStamp = new Date(ms);
        } catch (CVSException c) {
            if (XPLog.isDebugEnabled()) {
                XPLog.printDebug(logPrefix + "Failed to compute project time stamp of project: " + project.getName() + ", reason: " + c.getMessage());
            }
        } finally {
            monitor.done();
        }
    }

    private void unmanage(IResource resource, IProgressMonitor monitor) throws ResourceException {
        try {
            monitor.beginTask("Removing resource: " + resource.getProjectRelativePath().toString() + " from version control.", 100);
            if (XPLog.isDebugEnabled()) {
                XPLog.printDebug(logPrefix + "Removing resource: " + resource.getProjectRelativePath().toString() + " from version control.");
            }
            ICVSResource cvsResource = CVSWorkspaceRoot.getCVSResourceFor(resource);
            if (cvsResource != null) {
                cvsResource.unmanage(MonitorTools.subMonitorFor(monitor, 100));
            }
        } catch (CVSException c) {
            throw new ResourceException(c);
        } finally {
            monitor.done();
        }
    }

    private ICVSFile getNewestFile(ICVSFolder folder) {
        ICVSFile newestFile = null;
        ICVSFile currentFile = null;
        Date newestDate = null;
        Date currentDate = null;
        ResourceSyncInfo info = null;
        try {
            for (ICVSResource member : folder.members(ICVSFolder.EXISTING_MEMBERS)) {
                if (member instanceof ICVSFile) {
                    currentFile = (ICVSFile) member;
                } else if (member instanceof ICVSFolder) {
                    currentFile = getNewestFile((ICVSFolder) member);
                }
                if (currentFile != null) {
                    info = currentFile.getSyncInfo();
                    if (info != null) {
                        currentDate = info.getTimeStamp();
                        if (newestDate == null || (currentDate != null && currentDate.after(newestDate))) {
                            newestDate = currentDate;
                            newestFile = currentFile;
                        }
                    }
                }
            }
        } catch (CVSException c) {
            if (XPLog.isDebugEnabled()) {
                XPLog.printDebug(logPrefix + "Failed to get newest member file of folder: " + folder.getIResource().getProjectRelativePath().toString() + ", reason: " + c.getMessage());
            }
        }
        return newestFile;
    }

    private void forceProjectTimeStamp(IProject project, Date date) {
        ICVSFile file = getNewestFile(CVSWorkspaceRoot.getCVSFolderFor(project));
        if (file != null && date != null) {
            setFileTimeStamp(file, date);
        }
    }

    private void forceNotNewer(ICVSResource resource, Date date) {
        if (date != null) {
            try {
                if (resource instanceof ICVSFile) {
                    ICVSFile file = (ICVSFile) resource;
                    ResourceSyncInfo info = file.getSyncInfo();
                    if (info != null) {
                        Date current = info.getTimeStamp();
                        if (current != null && current.after(date)) {
                            setFileTimeStamp(file, date);
                        }
                    }
                } else if (resource instanceof ICVSFolder) {
                    for (ICVSResource member : ((ICVSFolder) resource).members(ICVSFolder.EXISTING_MEMBERS)) {
                        forceNotNewer(member, date);
                    }
                }
            } catch (CVSException c) {
                if (XPLog.isDebugEnabled()) {
                    XPLog.printDebug(logPrefix + ", reason: " + c.getMessage());
                }
            }
        }
    }

    private void setFileTimeStamp(ICVSFile file, Date date) {
        try {
            ResourceSyncInfo info = file.getSyncInfo();
            if (info != null) {
                Date oldDate = info.getTimeStamp();
                if (oldDate != null && !oldDate.equals(date)) {
                    MutableResourceSyncInfo newInfo = info.cloneMutable();
                    newInfo.setTimeStamp(date);
                    CVSProviderPlugin.getPlugin().getFileModificationManager().updated(file);
                    file.setSyncInfo(newInfo, ICVSFile.CLEAN);
                    file.setTimeStamp(date);
                }
            }
        } catch (CVSException c) {
            if (XPLog.isDebugEnabled()) {
                XPLog.printDebug(logPrefix + "Failed to enforce revision time stamp of resource: " + file.getIResource().getProjectRelativePath().toString() + ", reason: " + c.getMessage());
            }
        }
    }

    private static class TransientCVSTag extends CVSTag {

        private TransientCVSTag(String name, int type) {
            super(name, type);
        }
    }
}
