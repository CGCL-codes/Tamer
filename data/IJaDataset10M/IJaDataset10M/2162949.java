package org.xaware.ide.xadev.navigator;

import static org.xaware.shared.util.XAwareConstants.BIZCOMPONENT_ATTR_TYPE;
import static org.xaware.shared.util.XAwareConstants.xaNamespace;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.xaware.ide.shared.UserPrefs;
import org.xaware.ide.xadev.XA_Designer_Plugin;
import org.xaware.ide.xadev.common.ControlFactory;
import org.xaware.ide.xadev.common.ResourceUtils;
import org.xaware.ide.xadev.gui.ChangeEvent;
import org.xaware.ide.xadev.gui.ChangeListener;
import org.xaware.ide.xadev.gui.XAwareFileManagementHelper;
import org.xaware.ide.xadev.richui.editor.IReferenceModificationListener;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.XAwareException;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * Manager for Navigator View.
 * 
 **/
public class NavigatorManager implements IResourceChangeListener {

    /** QualifiedName to be used to store the persitent property. */
    public static final QualifiedName BIZCOMP_TYPE_QUALIFIED_NAME = new QualifiedName("BizComponent", "Type");

    /** Logger for XAware. */
    protected static final XAwareLogger logger = XAwareLogger.getXAwareLogger(NavigatorManager.class.getName());

    /** Holds NavigatorManager instance. */
    private static NavigatorManager refreshManager = new NavigatorManager();

    /** BizComponents list. Using Hashtable as it require thread-safe. */
    private Hashtable<IFile, ArrayList<IFile>> bizDriverVsBizComponents = null;

    /** Hashtable mapping bizComponent to it's referring bizview List. Using Hashtable as it require thread-safe. */
    private Hashtable<IFile, ArrayList<IFile>> bizCompToBizViewsHashtable = null;

    /** Hashtable for file vs listeners. */
    private Hashtable<IFile, ArrayList<ChangeListener>> fileVsListeners = new Hashtable<IFile, ArrayList<ChangeListener>>();

    /** List of listeners for reference collection changes. */
    private final ArrayList<IReferenceModificationListener> referenceModifiedListeners = new ArrayList<IReferenceModificationListener>();

    private NavigatorManager() {
        init();
    }

    /**
     * Initializes the {@link XARefreshManager}.
     */
    private void init() {
        logger.finest("Initializing manager ...");
        bizDriverVsBizComponents = new Hashtable<IFile, ArrayList<IFile>>();
        bizCompToBizViewsHashtable = new Hashtable<IFile, ArrayList<IFile>>();
        logger.finest("Registring project navigator  ...");
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
        logger.finest("Updating info from navigator ...");
        try {
            getBizViews();
        } catch (CoreException exception) {
            ControlFactory.showStackTrace("Error occured while fetching bizviews information from navigator.", exception);
        }
    }

    /** Fetches the BizDrivers & BizComponents **/
    private void getBizViews() throws CoreException {
        final IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
        for (IProject project : projects) {
            if (project.exists() && project.isOpen()) {
                handleMembers(project.members());
            }
        }
    }

    /**
     * Handles the members
     * 
     * @throws CoreException
     *             if unable to search through.
     */
    private void handleMembers(IResource members[]) throws CoreException {
        for (IResource member : members) {
            if (member instanceof IFolder) {
                handleMembers(((IFolder) member).members());
            } else if (member instanceof IFile) {
                {
                    processIFile((IFile) member);
                }
            }
        }
    }

    /**
     * @return the manager instance.
     */
    public static NavigatorManager getNavigatorManager() {
        return refreshManager;
    }

    /**
     * @return the bizDocsVsBizComps
     */
    public Hashtable<IFile, ArrayList<IFile>> getBizCompToBizViewsTable() {
        return bizCompToBizViewsHashtable;
    }

    /**
     * updates the underneath collection that maps the BizComp to it's referring bizDocuments.
     * 
     * @param bizView
     *            new BizDocument for which the collection is to be updated.
     */
    public void updateBizCompToBizViewsTable(IFile bizView) {
        try {
            XAwareFileManagementHelper helper = new XAwareFileManagementHelper(bizView.getRawLocation().toOSString(), false);
            List<String> bizAttributesValues = helper.getXABizAttributesValues(XAwareConstants.BIZDOCUMENT_ATTR_BIZCOMP);
            removeIFile(bizView, IResourceDelta.CHANGED);
            for (String bizComponentPath : bizAttributesValues) {
                IFile bizComponent = ResourceUtils.getIFile((ResourceUtils.getAbsolutePath(bizComponentPath)));
                if (bizComponent != null) {
                    ArrayList<IFile> bizViews = bizCompToBizViewsHashtable.get(bizComponent);
                    if (bizViews == null) {
                        bizViews = new ArrayList<IFile>();
                        bizCompToBizViewsHashtable.put(bizComponent, bizViews);
                    }
                    if (!bizViews.contains(bizView)) {
                        bizViews.add(bizView);
                        informReferenceModificationListeners(bizCompToBizViewsHashtable, bizComponent, bizView, false);
                    }
                }
            }
            helper.clean();
            helper = null;
        } catch (Exception exception) {
            logger.finest("Error in processing bizDocument:" + ResourceUtils.getAbsolutePath(bizView), exception);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
     */
    public void resourceChanged(final IResourceChangeEvent event) {
        logger.finest("Got Resource Changed event:" + event);
        if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
            Display.getDefault().syncExec(new Runnable() {

                public void run() {
                    try {
                        logger.finest("Removing resources for the closing project: " + event.getResource());
                        removeResources(((IProject) event.getResource()).members());
                        logger.finest("Successfully Removed resources of the closing project " + event.getResource());
                    } catch (CoreException exception) {
                        XA_Designer_Plugin.getDefault().getLog().log(new Status(IStatus.ERROR, XA_Designer_Plugin.getPLUGIN_ID(), "Failed to remove resources for " + event.getResource(), exception));
                    }
                }
            });
            return;
        }
        IResourceDelta delta = event.getDelta();
        if (delta == null) return;
        IResourceDelta addedMembers[] = delta.getAffectedChildren(IResourceDelta.ADDED);
        IResourceDelta changedMembers[] = delta.getAffectedChildren(IResourceDelta.CHANGED);
        IResourceDelta deletedMembers[] = delta.getAffectedChildren(IResourceDelta.REMOVED);
        addIResources(addedMembers, IResourceDelta.ADDED);
        addIResources(changedMembers, IResourceDelta.CHANGED);
        removeIResourceFromNavigator(deletedMembers);
    }

    /**
     * Adds the newly added resources
     * 
     * @param resourceModification
     */
    private void addIResources(IResourceDelta[] needToAdd, int resourceModification) {
        for (IResourceDelta resource : needToAdd) {
            IResource file = resource.getResource();
            if (file instanceof IFile) {
                processIFile((IFile) file);
            }
            addIResources(resource.getAffectedChildren(), resourceModification);
        }
    }

    /** Process the IFile. */
    private void processIFile(IFile file) {
        if (!file.exists()) {
            removeIFile((IFile) file, IResourceDelta.REMOVED);
        } else {
            removeIFile(file, IResourceDelta.CHANGED);
            updateBizCompToBizViewsTable(file);
            if (ResourceUtils.isBizComponent(file)) {
                processBizComponent(file);
            }
            informFileChangeListeners(file, false);
        }
    }

    /** Removes the resource the inner cache. */
    private void removeIFile(IFile iFile, int resourceModification) {
        removeBizDocument(iFile);
        removeBizComponent(iFile);
        Object result = bizDriverVsBizComponents.remove(iFile);
        if (result != null) informReferenceModificationListeners(bizDriverVsBizComponents, iFile, iFile, true);
        if (resourceModification == IResourceDelta.REMOVED) {
            informFileChangeListeners(iFile, true);
        }
    }

    /**
     * Removes the resources from Navigator.
     * 
     * @param resourceModification
     */
    private void removeIResourceFromNavigator(IResourceDelta[] deletedMembers) {
        for (IResourceDelta deletedMember : deletedMembers) {
            IResource iFile = deletedMember.getResource();
            if (deletedMember.getResource() instanceof IFile) {
                removeIFile((IFile) iFile, IResourceDelta.REMOVED);
            }
            removeIResourceFromNavigator(deletedMember.getAffectedChildren());
        }
    }

    /** Removes the bizDocument */
    public void removeBizDocument(IFile file) {
        for (IFile key : bizCompToBizViewsHashtable.keySet()) {
            ArrayList<IFile> values = bizCompToBizViewsHashtable.get(key);
            boolean removed = values.remove(file);
            if (removed) {
                informReferenceModificationListeners(bizCompToBizViewsHashtable, key, file, true);
            }
        }
    }

    /**
     * Removes the provided resources from the Data source explorer.
     * 
     * @param resources
     *            resources to be removed from data source explorer.
     * @throws CoreException
     *             Exception while removing the members.
     */
    protected void removeResources(IResource[] resources) throws CoreException {
        for (int i = 0; i < resources.length; i++) {
            if (resources[i] instanceof IFile) {
                if (ResourceUtils.isBizDocument((IFile) resources[i])) {
                    removeBizDocument((IFile) resources[i]);
                } else if (ResourceUtils.isBizComponent((IFile) resources[i])) {
                    removeBizComponent((IFile) resources[i]);
                }
                informFileChangeListeners((IFile) resources[i], true);
            }
        }
    }

    /**
     * Inform the reference modification listeners.
     * 
     * @param collection
     *            the collection that is modified.
     * @param key
     *            the file whose collection is modified.
     * @param modifiedFile
     *            file that is modified.
     * @param remove
     *            if the file is removed.
     */
    private void informReferenceModificationListeners(Hashtable<IFile, ArrayList<IFile>> collection, IFile key, IFile modifiedFile, boolean remove) {
        ArrayList<IReferenceModificationListener> needToBeRemoved = new ArrayList<IReferenceModificationListener>();
        for (IReferenceModificationListener refModificationListener : referenceModifiedListeners) {
            IFile file;
            try {
                file = refModificationListener.getIFile();
                if (key.equals(file)) {
                    if (collection == bizCompToBizViewsHashtable) {
                        if (refModificationListener.getTypeMask() == IReferenceModificationListener.REFERENCE_MODIFICATION_BIZCOMP) {
                            refModificationListener.referenceModified(bizCompToBizViewsHashtable.get(refModificationListener.getIFile()), modifiedFile, remove);
                        }
                    } else if (collection == bizDriverVsBizComponents) {
                        if (refModificationListener.getTypeMask() == IReferenceModificationListener.REFERENCE_MODIFICATION_BIZDRIVER) {
                            refModificationListener.referenceModified(bizDriverVsBizComponents.get(refModificationListener.getIFile()), modifiedFile, remove);
                        }
                    }
                }
            } catch (PartInitException partInitException) {
                logger.severe("Exception occured while sending modification.", partInitException);
                needToBeRemoved.add(refModificationListener);
            }
            referenceModifiedListeners.removeAll(needToBeRemoved);
        }
    }

    /**
     * Informs all the listeners about file changes.
     * 
     * @param file
     *            file that is modified.
     * @param remove
     *            if remove is true the file is removed
     */
    private void informFileChangeListeners(IFile file, boolean remove) {
        ArrayList<ChangeListener> arrayList = fileVsListeners.get(file);
        if (arrayList == null) return;
        for (ChangeListener changeListener : arrayList) {
            changeListener.stateChanged(new ChangeEvent(file, remove ? IResourceDelta.REMOVED : IResourceDelta.CHANGED));
        }
        if (arrayList.isEmpty() || remove) fileVsListeners.remove(file);
    }

    /**
     * @return the bizDriverVsBizComponents
     */
    public Hashtable<IFile, ArrayList<IFile>> getBizDriverVsBizComponents() {
        return bizDriverVsBizComponents;
    }

    /** Removes the bizComponent */
    public void removeBizComponent(IFile file) {
        for (IFile key : bizDriverVsBizComponents.keySet()) {
            ArrayList<IFile> values = bizDriverVsBizComponents.get(key);
            boolean removed = values.remove(file);
            if (removed) {
                informReferenceModificationListeners(bizDriverVsBizComponents, key, file, true);
            }
        }
    }

    /** Process the bizComponents for bizDriver */
    public void processBizComponent(IFile bizComponent) {
        try {
            XAwareFileManagementHelper helper = new XAwareFileManagementHelper(bizComponent.getRawLocation().toOSString(), false);
            bizComponent.setPersistentProperty(BIZCOMP_TYPE_QUALIFIED_NAME, helper.getBizCompType());
            List<String> bizAttributesValues = helper.getXABizAttributesValues(XAwareConstants.BIZCOMPONENT_ATTR_DRIVER);
            for (String bizDriverPath : bizAttributesValues) {
                IFile bizDriver = ResourceUtils.getIFile(ResourceUtils.getAbsolutePath(bizDriverPath));
                if (bizDriver != null) {
                    ArrayList<IFile> bizComponents = bizDriverVsBizComponents.get(bizDriver);
                    if (bizComponents == null) {
                        bizComponents = new ArrayList<IFile>();
                        bizDriverVsBizComponents.put(bizDriver, bizComponents);
                    }
                    if (!bizComponents.contains(bizComponent)) {
                        bizComponents.add(bizComponent);
                        informReferenceModificationListeners(bizDriverVsBizComponents, bizDriver, bizComponent, false);
                    }
                }
            }
            helper.clean();
            helper = null;
        } catch (Exception exception) {
            logger.finest("Error in processing bizcomponent:" + ResourceUtils.getAbsolutePath(bizComponent), exception);
        }
    }

    /**
     * Returns the BizComponent type.
     * 
     * @param file
     *            BizComponent file.
     * @return Returns the BizComponent type.
     * @throws XAwareException
     *             Exception while obtaining the BizComponent type.
     * @throws CoreException
     *             Exception while obtaining the BizComponent type.
     */
    public static String getBizComponentType(IFile file) throws XAwareException, CoreException {
        String bizComponentType = file.getPersistentProperty(BIZCOMP_TYPE_QUALIFIED_NAME);
        if (bizComponentType == null) {
            evaluateBizComponentType(file);
            bizComponentType = file.getPersistentProperty(BIZCOMP_TYPE_QUALIFIED_NAME);
        }
        return bizComponentType;
    }

    /**
     * Evaluates image for the BizComponent for given file depending on the type of the BizComponent; Returns the type
     * of the BizComponent.
     * 
     * @param file
     *            BizComponent file.
     * @return Returns the type of the BizComponent.
     */
    private static String evaluateBizComponentType(IFile file) throws XAwareException {
        try {
            Element rootElement = UserPrefs.getSAXBuilder().build(file.getContents()).getRootElement();
            if (rootElement == null) {
                throw new XAwareException("Root element cannot be null: Ensure file '" + file.getName() + "' is a valid BizComponent file.");
            }
            String bizComponentType = rootElement.getAttributeValue(BIZCOMPONENT_ATTR_TYPE, xaNamespace);
            file.setPersistentProperty(BIZCOMP_TYPE_QUALIFIED_NAME, bizComponentType);
            return bizComponentType;
        } catch (JDOMException e) {
            throw new XAwareException("Exception while evaluating the bizcomponent type." + e.getMessage(), e);
        } catch (IOException e) {
            throw new XAwareException("Exception while evaluating the bizcomponent type." + e.getMessage(), e);
        } catch (CoreException e) {
            throw new XAwareException("Exception while evaluating the bizcomponent type." + e.getMessage(), e);
        }
    }

    /**
     * Adds the given listener as the list of listeners for the given file.
     * 
     * @param file
     *            file for which the given listener listens for changes.
     * @param listener
     *            listener that listens to the changes of the file.
     */
    public void addFileChangeListener(IFile file, ChangeListener listener) {
        if (file == null || listener == null) return;
        boolean found = false;
        Set<IFile> keySet = fileVsListeners.keySet();
        for (IFile iFile : keySet) {
            if (iFile.equals(file)) {
                found = true;
                ArrayList<ChangeListener> existingListeners = fileVsListeners.get(iFile);
                if (!existingListeners.contains(listener)) existingListeners.add(listener);
                break;
            }
        }
        if (!found && file.exists()) {
            fileVsListeners.put(file, new ArrayList<ChangeListener>());
            fileVsListeners.get(file).add(listener);
        }
    }

    /**
     * Adds the given listener as the list of listeners for the given file.
     * 
     * @param file
     *            file for which the given listener listens for changes.
     * @param listener
     *            listener that listens to the changes of the file.
     */
    public void removeFileChangeListener(IFile file, ChangeListener listener) {
        if (file == null || listener == null) return;
        Set<IFile> keySet = fileVsListeners.keySet();
        for (IFile iFile : keySet) {
            if (iFile.equals(file)) {
                ArrayList<ChangeListener> listeners = fileVsListeners.get(iFile);
                listeners.remove(listener);
                if (listeners.isEmpty()) fileVsListeners.remove(iFile);
            }
        }
    }

    /**
     * Removes this as a listener .
     * 
     * @param listener
     */
    public void removeListener(ChangeListener listener) {
        if (listener == null) return;
        Set<IFile> keySet = fileVsListeners.keySet();
        Object needToRemove = null;
        for (IFile iFile : keySet) {
            ArrayList<ChangeListener> listeners = fileVsListeners.get(iFile);
            if (listeners.contains(listener)) listeners.remove(listener);
            if (listeners.isEmpty()) needToRemove = iFile;
        }
        if (needToRemove != null) fileVsListeners.remove(needToRemove);
    }

    /**
     * Adds the given Listener as listener to the reference collection changes.
     * 
     * @param refModificationListener
     *            listener to add.
     * @throws PartInitException
     */
    public void addReferenceModificationListener(IReferenceModificationListener refModificationListener) throws PartInitException {
        boolean found = false;
        for (IReferenceModificationListener listener : referenceModifiedListeners) {
            if (listener.getIFile().equals(refModificationListener.getIFile())) {
                found = true;
                break;
            }
        }
        if (!found) {
            referenceModifiedListeners.add(refModificationListener);
        }
    }

    /**
     * Removes the given reference modification listener.
     * 
     * @param refModificationListener
     */
    public void removeReferenceModificationListener(IReferenceModificationListener refModificationListener) {
        referenceModifiedListeners.remove(refModificationListener);
    }
}
