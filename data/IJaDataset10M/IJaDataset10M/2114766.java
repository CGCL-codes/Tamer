package com.volantis.mcs.eclipse.ab.editors.dom;

import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.ab.editors.EditorMessages;
import com.volantis.mcs.eclipse.ab.editors.SaveCommand;
import com.volantis.mcs.eclipse.ab.editors.SaveCommandFactory;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.MarkerGeneratingErrorReporter;
import com.volantis.mcs.eclipse.ab.views.devices.DeviceRepositoryBrowserPage;
import com.volantis.mcs.eclipse.builder.wizards.projects.MCSProjectAssignmentWizard;
import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeListener;
import com.volantis.mcs.eclipse.common.odom.ODOMObservable;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoInfo;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoManager;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoMemento;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoMementoOriginator;
import com.volantis.mcs.eclipse.controls.ActionableHandler;
import com.volantis.mcs.eclipse.controls.XPathFocusable;
import com.volantis.mcs.eclipse.validation.ValidationStatus;
import com.volantis.mcs.eclipse.validation.ValidationUtils;
import com.volantis.mcs.eclipse.core.ProjectDeviceRepositoryChangeListener;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import com.volantis.mcs.eclipse.core.MCSProjectNature;
import com.volantis.mcs.eclipse.core.ProjectDeviceRepositoryProvider;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.JAXPTransformerMetaFactory;
import com.volantis.mcs.xml.xpath.DOMType;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IElementStateListener;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.DefaultJDOMFactory;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.List;

/**
 * The EditorPart implementing function that is common to all ODOM Editors.
 */
public abstract class ODOMEditorPart extends EditorPart implements IGotoMarker {

    /**
     * The prefix for resources associated with MultiPageODOMEditor.
     */
    private static final String RESOURCE_PREFIX = "ODOMEditorPart.";

    /**
     * The ODOMEditorContext for this ODOMEditorPart.
     */
    private ODOMEditorContext context;

    /**
     * The isDirty flag.
     */
    private boolean isDirty = false;

    /**
     * The root element name.
     */
    private final String rootElementName;

    /**
     * Flag allowing sub-classes to disable/enable changes to the
     * dirty status of this ODOMEditorPart.
     */
    private boolean dirtyStatusEnabled = true;

    /**
     * helper that executes UndoRedo unit of work demarcation
     */
    private ODOMUndoRedoGUIDemarcator odomUndoRedoGUIDemarcator;

    /**
     * The actionable handler.
     */
    private ActionableHandler handler;

    /**
     * The file synchronizer that will ensure that the editor is closed if its
     * file is deleted.
     */
    private FileSynchronizer synchronizer;

    /**
     * The listener that is associated with standardized element changes such
     * as deletion, moving, content replacement and dirty state changes).
     */
    private IElementStateListener listener;

    /**
     * The ProjectDeviceRepositoryChangeListener associated with this
     * ODOMEditorPart.
     */
    private ProjectDeviceRepositoryChangeListener projDeviceRepositoryChangeListener;

    /**
     * The value of an IStatus when the user has selected cancel during the
     * MCSProjectAssignmentWizard
     */
    public static final int MCS_ASSIGN_WIZARD_CANCELLED_CODE = 1;

    /**
     * Construct a new ODOMEditorPart with a null ODOMEditorContext.
     *
     * @param rootElementName The name of the root element that this overview
     *                        part will be editing. Must not be null.
     * @throws IllegalArgumentException If rootElementName is null.
     */
    public ODOMEditorPart(String rootElementName) {
        this(rootElementName, null);
    }

    /**
     * Construct a new ODOMEditorPart.
     *
     * @param rootElementName The name of the root element that this overview
     *                        part will be editing. Must not be null.
     * @param context         The ODOMEditorContext for this ODOMEditorPart.
     *                        Can be null and if it is the ODOMEditorPart will
     *                        create its context by calling
     *                        ODOMEditorContext.createODOMEditorContext.
     * @throws IllegalArgumentException If rootElementName is null.
     */
    public ODOMEditorPart(String rootElementName, ODOMEditorContext context) {
        if (rootElementName == null) {
            throw new IllegalArgumentException("Cannot be null: " + "rootElementName");
        }
        this.rootElementName = rootElementName;
        this.context = context;
    }

    public void doSave(final IProgressMonitor progressMonitor) {
        if (context.resourceExists()) {
            SaveCommand command = context.getSaveCommandFactory().createSaveCommand();
            performSaveOperation(createSaveOperation(command), progressMonitor);
        } else {
            if (isSaveAsAllowed()) {
                doSaveAs(progressMonitor);
            } else {
                Shell shell = getSite().getShell();
                String title = EditorMessages.getString("Editor.error.save." + "deleted.title");
                String msg = EditorMessages.getString("Editor.error.save." + "deleted.message");
                String type = EclipseCommonMessages.getLocalizedPolicyName(context.getPolicyType());
                String name = context.getPolicyResource().getName();
                String args[] = { type, name };
                MessageFormat format = new MessageFormat(msg);
                MessageDialog.openError(shell, title, format.format(args));
            }
        }
    }

    public Object getAdapter(Class adapterClass) {
        Object adapter = null;
        if (DeviceRepositoryBrowserPage.class.equals(adapterClass)) {
            IProject project = context.getPolicyResource().getProject();
            DeviceRepositoryAccessorManager dram = null;
            try {
                dram = new DeviceRepositoryAccessorManager(MCSProjectNature.getDeviceRepositoryName(project), new JAXPTransformerMetaFactory(), new DefaultJDOMFactory(), false);
                final DeviceRepositoryBrowserPage page = new DeviceRepositoryBrowserPage(dram);
                projDeviceRepositoryChangeListener = new ProjectDeviceRepositoryChangeListener() {

                    public void changed() {
                        try {
                            DeviceRepositoryAccessorManager dram = ProjectDeviceRepositoryProvider.getSingleton().getDeviceRepositoryAccessorManager(getFile().getProject());
                            page.updateDeviceRepositoryAccessorManager(dram);
                        } catch (RepositoryException e) {
                            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
                        }
                    }
                };
                ProjectDeviceRepositoryProvider.getSingleton().addProjectDeviceRepositoryChangeListener(projDeviceRepositoryChangeListener, getFile().getProject());
                adapter = page;
            } catch (RepositoryException e) {
                EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
            } catch (IOException e) {
                EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
            }
        } else {
            adapter = super.getAdapter(adapterClass);
        }
        return adapter;
    }

    public void doSaveAs() {
        doSaveAs(getProgressMonitor());
    }

    /**
     * doSaveAs using a given IProgressMonitor.
     *
     * @param progressMonitor The IProgressMonitor.
     */
    private void doSaveAs(final IProgressMonitor progressMonitor) {
        Shell shell = getSite().getShell();
        SaveAsDialog dialog = new SaveAsDialog(shell);
        IFile original = getFile();
        dialog.setOriginalFile(original);
        dialog.create();
        boolean cancelled = false;
        if (dialog.open() == Dialog.CANCEL) {
            if (progressMonitor != null) {
                progressMonitor.setCanceled(true);
                cancelled = true;
            }
        }
        IPath filePath = null;
        if (!cancelled) {
            filePath = dialog.getResult();
            if (filePath == null) {
                if (progressMonitor != null) {
                    progressMonitor.setCanceled(true);
                    cancelled = true;
                }
            }
        }
        if (!cancelled) {
            boolean alreadyOpen = isFileOpenInEditor(filePath);
            if (alreadyOpen && context.resourceExists()) {
                String title = EditorMessages.getString("Editor.error.saveas.problems.title");
                String msg = EditorMessages.getString("Editor.error.saveas.problems.message");
                ODOMEditorContext.showErrorDialog(title, new IllegalStateException(msg), msg, true);
            } else {
                final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(filePath);
                ValidationStatus status = ValidationUtils.checkFile(file.getLocation().toFile().getAbsolutePath(), ValidationUtils.FILE_CAN_WRITE);
                if (!status.isOK()) {
                    String title = EditorMessages.getString("Editor.error." + "saveas.problems.title");
                    ErrorDialog.openError(shell, title, null, status);
                } else {
                    SaveCommandFactory factory = context.getSaveCommandFactory();
                    SaveCommand command = factory.createSaveAsCommand(file);
                    boolean succeeded = performSaveOperation(createSaveOperation(command), progressMonitor);
                    if (succeeded) {
                        setInput(new FileEditorInput(file));
                        context.setPolicyResource(file);
                        setTitle(file.getName());
                    }
                    if (progressMonitor != null) {
                        progressMonitor.setCanceled(!succeeded);
                    }
                }
            }
        }
    }

    /**
     * Determine whether or not the file is open in the editor.
     *
     * @param filePath the full file path to check for a matching editor name.
     * @return true if the file is open already, false otherwise.
     */
    private boolean isFileOpenInEditor(IPath filePath) {
        boolean alreadyOpen = false;
        IWorkbenchWindow[] workbenchWindows = PlatformUI.getWorkbench().getWorkbenchWindows();
        final String filename = filePath.lastSegment();
        for (int i = 0; !alreadyOpen && i < workbenchWindows.length; i++) {
            IWorkbenchPage activePage = workbenchWindows[i].getActivePage();
            if (activePage != null) {
                IEditorReference[] editorReferences = activePage.getEditorReferences();
                for (int j = 0; !alreadyOpen && j < editorReferences.length; j++) {
                    IEditorReference editorReference = editorReferences[j];
                    if (filename.equals(editorReference.getName())) {
                        IFileEditorInput fileEditorInput = ((IFileEditorInput) editorReference.getEditor(true).getEditorInput());
                        if (fileEditorInput.getFile().getFullPath().equals(filePath)) {
                            alreadyOpen = true;
                        }
                    }
                }
            }
        }
        return alreadyOpen;
    }

    /**
     * Returns true if and only if this editor part is the workbenches
     * active editor part.
     *
     * @return true if and only if this IEditorPart is the active page.
     */
    protected boolean isActivePage() {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() == this;
    }

    public void gotoMarker(IMarker marker) {
        MarkerGeneratingErrorReporter errorReporter = context.getErrorReporter();
        if (errorReporter == null) {
            throw new IllegalStateException("DOMValidator is null");
        }
        XPath xPath = errorReporter.getXPath(marker);
        if (xPath != null) {
            setFocus(xPath);
        }
    }

    /**
     * Sets the focus to XPathFocusableControls using the provided xPath. This
     * is done by telling the ODOMSelectionManager to select the component
     * corresponding to the xPath. Doing this allows the Element Attributes
     * view to be configured correctly such that the required attribute can have
     * its focus set. If this is not done the Element Attributes view will
     * contain the attributes of whatever is currently selected in the design
     * window rather then the attributes you wish to edit. After selecting the
     * correct component it is then possible to set focus on the correct
     * attribute.
     *
     * @param xPath the xPath of the attribute whose focus you wish to set.
     * Must not be null.
     */
    protected void setFocus(XPath xPath) {
        try {
            XPath elementPath = xPath;
            Element root = getODOMEditorContext().getRootElement();
            if (elementPath.getDOMType() != DOMType.ELEMENT_TYPE) {
                elementPath = elementPath.getParent();
            }
            context.getODOMSelectionManager().setSelection(elementPath.selectNodes(root));
            XPathFocusable[] focusableControls = getXPathFocusableControls();
            for (int i = 0; (focusableControls != null) && (i < focusableControls.length); i++) {
                focusableControls[i].setFocus(xPath);
            }
        } catch (XPathException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }
    }

    public void init(IEditorSite editorSite, IEditorInput editorInput) throws PartInitException {
        IFile file = null;
        if (context == null) {
            file = getFile(editorInput);
            context = ODOMEditorContext.createODOMEditorContext(rootElementName, file, new UndoRedoMementoOriginator() {

                public UndoRedoMemento takeSnapshot() {
                    return UndoRedoMemento.NULLOBJ;
                }

                public void restoreSnapshot(UndoRedoInfo undoRedoInfo) {
                    ODOMEditorPart.this.restoreSnapshot(undoRedoInfo);
                }
            });
        }
        if (context != null) {
            file = getFile(editorInput);
            synchronizer = new FileSynchronizer((IFile) context.getPolicyResource());
            synchronizer.install();
            listener = new IElementStateListener() {

                public void elementDirtyStateChanged(Object element, boolean isDirty) {
                }

                public void elementContentAboutToBeReplaced(Object element) {
                }

                public void elementContentReplaced(Object element) {
                }

                public void elementDeleted(Object element) {
                    if (!ODOMEditorPart.this.isDirty()) {
                        close(true);
                    }
                }

                public void elementMoved(Object originalElement, Object movedElement) {
                }
            };
            synchronizer.addElementStateListener(listener);
            setTitle(file.getName());
            setSite(editorSite);
            setInput(editorInput);
        } else {
            String message = EditorMessages.getString(RESOURCE_PREFIX + "contextCreationFailure.message");
            MessageFormat messageFormat = new MessageFormat(message);
            String args[] = { file.getName() };
            throw new PartInitException(messageFormat.format(args));
        }
        boolean validDeviceRepository = false;
        IProject project = context.getPolicyResource().getProject();
        try {
            if (project.hasNature(MCSProjectNature.NATURE_ID)) {
                MCSProjectNature projectNature;
                projectNature = MCSProjectNature.getMCSProjectNature(project);
                validDeviceRepository = projectNature.hasValidDeviceRepository();
            }
        } catch (CoreException coreException) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), coreException);
        }
        if (validDeviceRepository == false) {
            MCSProjectAssignmentWizard wizard = new MCSProjectAssignmentWizard(project);
            Shell shell = getSite().getShell();
            WizardDialog dialog = new WizardDialog(shell, wizard);
            dialog.create();
            int action = dialog.open();
            if (action == WizardDialog.CANCEL) {
                String pluginId = ABPlugin.getDefault().getDescriptor().getUniqueIdentifier();
                Status status = new Status(Status.OK, pluginId, ODOMEditorPart.MCS_ASSIGN_WIZARD_CANCELLED_CODE, EditorMessages.getString(RESOURCE_PREFIX + "projectAssignmentWizardCancelled.reason"), null);
                PartInitException e = new PartInitException(status);
                throw e;
            }
        }
    }

    /**
     * Close the editor asynchronously.
     *
     * @param save the save parameter.
     */
    protected void close(final boolean save) {
        Display display = getSite().getShell().getDisplay();
        display.asyncExec(new Runnable() {

            public void run() {
                getSite().getPage().closeEditor(ODOMEditorPart.this, save);
            }
        });
    }

    /**
     * Get the ODOMEditorContext for this ODOMEditorPart.
     *
     * @return The ODOMEditorContext.
     */
    protected ODOMEditorContext getODOMEditorContext() {
        return context;
    }

    /**
     * Set the ODOMEditorContext for this ODOMEditorPart.
     *
     * @param context The ODOMEditorContext.
     */
    protected void setODOMEditorContext(ODOMEditorContext context) {
        this.context = context;
    }

    /**
     * Set focus to the first focusable control in the EditorPart.
     */
    public abstract void setFocus();

    public boolean isDirty() {
        return isDirty;
    }

    public boolean isSaveAsAllowed() {
        return true;
    }

    public void dispose() {
        if (synchronizer != null) {
            synchronizer.removeElementStateListener(listener);
            synchronizer.uninstall();
        }
        if (projDeviceRepositoryChangeListener != null) {
            ProjectDeviceRepositoryProvider.getSingleton().removeProjectDeviceRepositoryChangeListener(projDeviceRepositoryChangeListener, getFile().getProject());
        }
        try {
            context.dispose();
            if (handler != null) {
                handler.dispose();
                handler = null;
            }
        } finally {
            super.dispose();
        }
    }

    /**
     * Create the part control.
     *
     * @see #createPartControl(Composite)
     */
    protected abstract void createPartControlImpl(Composite parent);

    public void createPartControl(Composite parent) {
        initializeActions();
        createPartControlImpl(parent);
        context.addChangeListener(new ODOMChangeListener() {

            public void changed(ODOMObservable node, ODOMChangeEvent event) {
                setDirty(true);
            }
        });
        odomUndoRedoGUIDemarcator = new ODOMUndoRedoGUIDemarcator(context.getUndoRedoManager());
        odomUndoRedoGUIDemarcator.addFocusDrivenUndoRedoDemarcatorFor(parent);
    }

    /**
     * Creates the Undo/redo actions for the document being edited.
     * <p/>
     * The actions are registered as global handlers in this editor's action
     * bars, which are stored in the editor context and made available
     * for other parts to use. So this step needs to be done only once
     * in the lifecycle of an editor part
     * </p>
     */
    private void initializeActions() {
        if (context.getActionBars() == null) {
            IActionBars editorSiteActionBars = this.getEditorSite().getActionBars();
            if (editorSiteActionBars != null) {
                UndoRedoManager undoRedoManager = context.getUndoRedoManager();
                ODOMEditorContribution.assignUndoRedoAction(editorSiteActionBars, undoRedoManager, IWorkbenchActionConstants.UNDO);
                ODOMEditorContribution.assignUndoRedoAction(editorSiteActionBars, undoRedoManager, IWorkbenchActionConstants.REDO);
                context.setActionBars(editorSiteActionBars);
                context.setHandler(new ActionableHandler(editorSiteActionBars));
            }
        }
    }

    /**
     * Set the isDirty flag and fire a dirty change event if the status of
     * isDirty has changed. If dirtyStatusEnabled is set to false then this
     * method has no effect.
     *
     * @param isDirty The value to set isDirty to.
     */
    protected void setDirty(boolean isDirty) {
        if (dirtyStatusEnabled && this.isDirty != isDirty) {
            this.isDirty = isDirty;
            firePropertyChange(PROP_DIRTY);
        }
    }

    /**
     * Set the dirtyStatusEnabled flag. If this flag is false then the dirty
     * status of this editor part will not change. If this flag is true then
     * the dirty status of this editior part will change if there is a change
     * to the root element (i.e. any element) this ODOMEditorPart is
     * associated with.
     *
     * @param enable Flag to enable/disable the dirtyStatusEnabled flag.
     */
    protected void setDirtyStatusEnabled(boolean enable) {
        dirtyStatusEnabled = enable;
    }

    /**
     * Run a save operation.
     *
     * @param saveOperation The WorkspaceModifyOperation that will do the
     *                      save.
     * @return true If the save succeeded; false otherwise.
     */
    private boolean performSaveOperation(WorkspaceModifyOperation saveOperation, IProgressMonitor progressMonitor) {
        boolean succeeded = false;
        try {
            saveOperation.run(progressMonitor);
            succeeded = true;
            setDirty(false);
        } catch (InterruptedException e) {
        } catch (InvocationTargetException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }
        return succeeded;
    }

    /**
     * Create the WorkspaceModifyOperation that will save the resource
     * (policy) being editor.
     * <p/>
     * Editors that do not save policies directly to files on the file
     * system should override this methods with their own implementations.
     *
     * @return The WorkspaceModifyOperation that will save the policy
     *         being edited.
     */
    protected WorkspaceModifyOperation createSaveOperation(final SaveCommand saveCommand) {
        WorkspaceModifyOperation op = new WorkspaceModifyOperation() {

            protected void execute(IProgressMonitor monitor) throws CoreException, InterruptedException {
                try {
                    monitor.beginTask("Editor.save.progress", 2000);
                    saveCommand.save(new SubProgressMonitor(monitor, 1000));
                } finally {
                    monitor.done();
                }
            }
        };
        return op;
    }

    /**
     * Get the XPathFocusable controls in this EditorPart.
     *
     * @return The XPathFocusable controls.
     */
    protected abstract XPathFocusable[] getXPathFocusableControls();

    /**
     * Get the IFile associated with the policy being edited by this
     * editor.
     *
     * @return The IFile associated with the policy being edited.
     */
    private IFile getFile() {
        return getFile(getEditorInput());
    }

    /**
     * Get the IFile assoiciated with a given IEditorInput.
     *
     * @param input The IEditorInput.
     * @return The IFile associated with the given IEditorInput.
     * @throws IllegalArgumentException If an IFile cannot be obtained
     *                                  from the given IEditorInput.
     */
    protected IFile getFile(IEditorInput input) {
        IFile file = null;
        if (input instanceof IFileEditorInput) {
            file = ((IFileEditorInput) input).getFile();
        } else {
            throw new IllegalStateException("Unsupported IEditorInput: " + getEditorInput());
        }
        return file;
    }

    /**
     * Provide a progress monitor that operates on the status line.
     *
     * @return A status line based IProgressMonitor or null if no
     *         StatusLineManager was available.
     */
    private IProgressMonitor getProgressMonitor() {
        IProgressMonitor pm = null;
        IStatusLineManager manager = getStatusLineManager();
        if (manager != null) {
            pm = manager.getProgressMonitor();
        }
        return pm != null ? pm : new NullProgressMonitor();
    }

    /**
     * Get the IStatusLineManager. This method assumes that the
     * IEditorActionBarContributor is an instance of
     * EditorActionBarContributor. This method also assumes that the
     * EditorActionBarContributor will return non-null from getActionBars().
     *
     * @return The IStatusLineManager
     */
    private IStatusLineManager getStatusLineManager() {
        return context.getActionBars().getStatusLineManager();
    }

    /**
     * Non-public implementation of {@link UndoRedoMementoOriginator#restoreSnapshot}
     * as an <code>ODOMEditorPart</code> does not implement the interface
     * explicitly
     * <p/>
     * <p/>
     * This implementation sets the focus to the widget displaying
     * the odom node that was changed last as a result of applying an
     * undo/redo request.
     * It also sets the ODOM selection to the elements that have changed.
     * </p>
     * <p/>
     * <strong>NOTE</strong> : setFocusWith(XPath) must occur before
     * setting the ODOM selections, because setFocus ALSO sets the selection,
     * so if the order of calls is reversed the selection we set here
     * would be destroyed.
     * </p>
     *
     * @param undoRedoInfo provided by the UndoRedoManager
     */
    void restoreSnapshot(UndoRedoInfo undoRedoInfo) {
        List changedNodesXPaths = undoRedoInfo.getChangedNodesXPaths();
        if (!changedNodesXPaths.isEmpty()) {
            setFocus((XPath) changedNodesXPaths.get(0));
        }
        try {
            List elements = undoRedoInfo.getChangedElements(getRootElementDocument());
            if (!elements.isEmpty()) {
                context.getODOMSelectionManager().setSelection(elements);
            }
        } catch (XPathException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }
    }

    /**
     * Provide a mechanism for subclasses to get an alternative {@link Document}
     *
     * @return a {@link Document} instance.
     */
    protected Document getRootElementDocument() {
        return context.getRootElement().getDocument();
    }
}
