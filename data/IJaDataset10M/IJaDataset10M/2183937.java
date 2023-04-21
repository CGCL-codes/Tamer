package Slee11.presentation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.ui.MarkerHelper;
import org.eclipse.emf.common.ui.ViewerPane;
import org.eclipse.emf.common.ui.editor.ProblemEditorPart;
import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLDefaultHandler;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLLoad;
import org.eclipse.emf.ecore.xmi.XMLParserPool;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.action.EditingDomainActionBarContributor;
import org.eclipse.emf.edit.ui.celleditor.AdapterFactoryTreeEditor;
import org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter;
import org.eclipse.emf.edit.ui.dnd.LocalTransfer;
import org.eclipse.emf.edit.ui.dnd.ViewerDragAdapter;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.UnwrappingSelectionProvider;
import org.eclipse.emf.edit.ui.util.EditUIMarkerHelper;
import org.eclipse.emf.edit.ui.util.EditUIUtil;
import org.eclipse.emf.edit.ui.view.ExtendedPropertySheetPage;
import org.eclipse.emf.validation.marker.MarkerUtil;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.jjflyboy.slee.descriptors.editors.DescriptorPage;
import org.jjflyboy.slee.descriptors.editors.Slee11FormToolkit;
import Slee11.provider.Slee11ItemProviderAdapterFactory;
import Slee11.util.Slee11XmlHandler;

/**
 * This is an example of a Slee11 model editor.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 */
public class Slee11Editor extends FormEditor implements IEditingDomainProvider, ISelectionProvider, IMenuListener, IViewerProvider, IGotoMarker {

    /**
	 * This keeps track of the editing domain that is used to track all changes to the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected AdapterFactoryEditingDomain editingDomain;

    /**
	 * This is the one adapter factory used for providing views of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ComposedAdapterFactory adapterFactory;

    /**
	 * This is the content outline page.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected IContentOutlinePage contentOutlinePage;

    /**
	 * This is a kludge...
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected IStatusLineManager contentOutlineStatusLineManager;

    /**
	 * This is the content outline page's viewer.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected TreeViewer contentOutlineViewer;

    /**
	 * This is the property sheet page.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected PropertySheetPage propertySheetPage;

    /**
	 * This is the viewer that shadows the selection in the content outline.
	 * The parent relation must be correctly defined for this to work.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected TreeViewer selectionViewer;

    /**
	 * This inverts the roll of parent and child in the content provider and show parents as a tree.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected TreeViewer parentViewer;

    /**
	 * This shows how a tree view works.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected TreeViewer treeViewer;

    /**
	 * This shows how a list view works.
	 * A list viewer doesn't support icons.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ListViewer listViewer;

    /**
	 * This shows how a table view works.
	 * A table can be used as a list with icons.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected TableViewer tableViewer;

    /**
	 * This shows how a tree view with columns works.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected TreeViewer treeViewerWithColumns;

    /**
	 * This keeps track of the active viewer pane, in the book.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ViewerPane currentViewerPane;

    /**
	 * This keeps track of the active content viewer, which may be either one of the viewers in the pages or the content outline viewer.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected Viewer currentViewer;

    /**
	 * This listens to which ever viewer is active.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ISelectionChangedListener selectionChangedListener;

    /**
	 * This keeps track of all the {@link org.eclipse.jface.viewers.ISelectionChangedListener}s that are listening to this editor.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected Collection<ISelectionChangedListener> selectionChangedListeners = new ArrayList<ISelectionChangedListener>();

    /**
	 * This keeps track of the selection of the editor as a whole.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ISelection editorSelection = StructuredSelection.EMPTY;

    /**
	 * The MarkerHelper is responsible for creating workspace resource markers presented
	 * in Eclipse's Problems View.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected MarkerHelper markerHelper = new EditUIMarkerHelper();

    protected DescriptorPage descriptorPage;

    protected int descriptorPageIndex;

    private DataBindingContext bindingContext;

    /**
	 * This listens for when the outline becomes active
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
    protected IPartListener partListener = new IPartListener() {

        public void partActivated(IWorkbenchPart p) {
            if (p instanceof ContentOutline) {
                if (((ContentOutline) p).getCurrentPage() == contentOutlinePage) {
                    getActionBarContributor().setActiveEditor(Slee11Editor.this);
                    setCurrentViewer(contentOutlineViewer);
                }
            } else if (p instanceof PropertySheet) {
                if (((PropertySheet) p).getCurrentPage() == propertySheetPage) {
                    getActionBarContributor().setActiveEditor(Slee11Editor.this);
                    handleActivate();
                }
            } else if (p == Slee11Editor.this) {
                if (getActivePage() == descriptorPageIndex) {
                    setCurrentViewer(descriptorPage.getViewer());
                }
                handleActivate();
            }
        }

        public void partBroughtToTop(IWorkbenchPart p) {
        }

        public void partClosed(IWorkbenchPart p) {
        }

        public void partDeactivated(IWorkbenchPart p) {
        }

        public void partOpened(IWorkbenchPart p) {
        }
    };

    /**
	 * Resources that have been removed since last activation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected Collection<Resource> removedResources = new ArrayList<Resource>();

    /**
	 * Resources that have been changed since last activation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected Collection<Resource> changedResources = new ArrayList<Resource>();

    /**
	 * Resources that have been saved.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected Collection<Resource> savedResources = new ArrayList<Resource>();

    /**
	 * Map to store the diagnostic associated with a resource.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected Map<Resource, Diagnostic> resourceToDiagnosticMap = new LinkedHashMap<Resource, Diagnostic>();

    /**
	 * Controls whether the problem indication should be updated.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected boolean updateProblemIndication = true;

    /**
	 * Adapter used to update the problem indication when resources are demanded loaded.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected EContentAdapter problemIndicationAdapter = new EContentAdapter() {

        @Override
        public void notifyChanged(Notification notification) {
            if (notification.getNotifier() instanceof Resource) {
                switch(notification.getFeatureID(Resource.class)) {
                    case Resource.RESOURCE__IS_LOADED:
                    case Resource.RESOURCE__ERRORS:
                    case Resource.RESOURCE__WARNINGS:
                        {
                            Resource resource = (Resource) notification.getNotifier();
                            Diagnostic diagnostic = analyzeResourceProblems(resource, null);
                            if (diagnostic.getSeverity() != Diagnostic.OK) {
                                resourceToDiagnosticMap.put(resource, diagnostic);
                            } else {
                                resourceToDiagnosticMap.remove(resource);
                            }
                            if (updateProblemIndication) {
                                getSite().getShell().getDisplay().asyncExec(new Runnable() {

                                    public void run() {
                                        updateProblemIndication();
                                    }
                                });
                            }
                            break;
                        }
                }
            } else {
                super.notifyChanged(notification);
            }
        }

        @Override
        protected void setTarget(Resource target) {
            basicSetTarget(target);
        }

        @Override
        protected void unsetTarget(Resource target) {
            basicUnsetTarget(target);
        }
    };

    /**
	 * This listens for workspace changes.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected IResourceChangeListener resourceChangeListener = new IResourceChangeListener() {

        public void resourceChanged(IResourceChangeEvent event) {
            IResourceDelta delta = event.getDelta();
            try {
                class ResourceDeltaVisitor implements IResourceDeltaVisitor {

                    protected ResourceSet resourceSet = editingDomain.getResourceSet();

                    protected Collection<Resource> changedResources = new ArrayList<Resource>();

                    protected Collection<Resource> removedResources = new ArrayList<Resource>();

                    public boolean visit(IResourceDelta delta) {
                        if (delta.getResource().getType() == IResource.FILE) {
                            if (delta.getKind() == IResourceDelta.REMOVED || delta.getKind() == IResourceDelta.CHANGED && delta.getFlags() != IResourceDelta.MARKERS) {
                                Resource resource = resourceSet.getResource(URI.createPlatformResourceURI(delta.getFullPath().toString(), true), false);
                                if (resource != null) {
                                    if (delta.getKind() == IResourceDelta.REMOVED) {
                                        removedResources.add(resource);
                                    } else if (!savedResources.remove(resource)) {
                                        changedResources.add(resource);
                                    }
                                }
                            }
                        }
                        return true;
                    }

                    public Collection<Resource> getChangedResources() {
                        return changedResources;
                    }

                    public Collection<Resource> getRemovedResources() {
                        return removedResources;
                    }
                }
                final ResourceDeltaVisitor visitor = new ResourceDeltaVisitor();
                delta.accept(visitor);
                if (!visitor.getRemovedResources().isEmpty()) {
                    getSite().getShell().getDisplay().asyncExec(new Runnable() {

                        public void run() {
                            removedResources.addAll(visitor.getRemovedResources());
                            if (!isDirty()) {
                                getSite().getPage().closeEditor(Slee11Editor.this, false);
                            }
                        }
                    });
                }
                if (!visitor.getChangedResources().isEmpty()) {
                    getSite().getShell().getDisplay().asyncExec(new Runnable() {

                        public void run() {
                            changedResources.addAll(visitor.getChangedResources());
                            if (getSite().getPage().getActiveEditor() == Slee11Editor.this) {
                                handleActivate();
                            }
                        }
                    });
                }
            } catch (CoreException exception) {
                SleeEditorPlugin.INSTANCE.log(exception);
            }
        }
    };

    /**
	 * Handles activation of the editor or it's associated views.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void handleActivate() {
        if (editingDomain.getResourceToReadOnlyMap() != null) {
            editingDomain.getResourceToReadOnlyMap().clear();
            setSelection(getSelection());
        }
        if (!removedResources.isEmpty()) {
            if (handleDirtyConflict()) {
                getSite().getPage().closeEditor(Slee11Editor.this, false);
            } else {
                removedResources.clear();
                changedResources.clear();
                savedResources.clear();
            }
        } else if (!changedResources.isEmpty()) {
            changedResources.removeAll(savedResources);
            handleChangedResources();
            changedResources.clear();
            savedResources.clear();
        }
    }

    /**
	 * Handles what to do with changed resources on activation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void handleChangedResources() {
        if (!changedResources.isEmpty() && (!isDirty() || handleDirtyConflict())) {
            if (isDirty()) {
                changedResources.addAll(editingDomain.getResourceSet().getResources());
            }
            editingDomain.getCommandStack().flush();
            updateProblemIndication = false;
            for (Resource resource : changedResources) {
                if (resource.isLoaded()) {
                    resource.unload();
                    try {
                        resource.load(Collections.EMPTY_MAP);
                    } catch (IOException exception) {
                        if (!resourceToDiagnosticMap.containsKey(resource)) {
                            resourceToDiagnosticMap.put(resource, analyzeResourceProblems(resource, exception));
                        }
                    }
                }
            }
            if (AdapterFactoryEditingDomain.isStale(editorSelection)) {
                setSelection(StructuredSelection.EMPTY);
            }
            updateProblemIndication = true;
            updateProblemIndication();
        }
    }

    /**
	 * Updates the problems indication with the information described in the specified diagnostic.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void updateProblemIndication() {
        if (updateProblemIndication) {
            BasicDiagnostic diagnostic = new BasicDiagnostic(Diagnostic.OK, "org.jjflyboy.slee.descriptors.editor", 0, null, new Object[] { editingDomain.getResourceSet() });
            for (Diagnostic childDiagnostic : resourceToDiagnosticMap.values()) {
                if (childDiagnostic.getSeverity() != Diagnostic.OK) {
                    diagnostic.add(childDiagnostic);
                }
            }
            int lastEditorPage = getPageCount() - 1;
            if (lastEditorPage >= 0 && getEditor(lastEditorPage) instanceof ProblemEditorPart) {
                ((ProblemEditorPart) getEditor(lastEditorPage)).setDiagnostic(diagnostic);
                if (diagnostic.getSeverity() != Diagnostic.OK) {
                    setActivePage(lastEditorPage);
                }
            } else if (diagnostic.getSeverity() != Diagnostic.OK) {
                ProblemEditorPart problemEditorPart = new ProblemEditorPart();
                problemEditorPart.setDiagnostic(diagnostic);
                problemEditorPart.setMarkerHelper(markerHelper);
                try {
                    addPage(++lastEditorPage, problemEditorPart, getEditorInput());
                    setPageText(lastEditorPage, problemEditorPart.getPartName());
                    setActivePage(lastEditorPage);
                    showTabs();
                } catch (PartInitException exception) {
                    SleeEditorPlugin.INSTANCE.log(exception);
                }
            }
            if (markerHelper.hasMarkers(editingDomain.getResourceSet())) {
                markerHelper.deleteMarkers(editingDomain.getResourceSet());
                if (diagnostic.getSeverity() != Diagnostic.OK) {
                    try {
                        markerHelper.createMarkers(diagnostic);
                    } catch (CoreException exception) {
                        SleeEditorPlugin.INSTANCE.log(exception);
                    }
                }
            }
        }
    }

    /**
	 * Shows a dialog that asks if conflicting changes should be discarded.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected boolean handleDirtyConflict() {
        return MessageDialog.openQuestion(getSite().getShell(), getString("_UI_FileConflict_label"), getString("_WARN_FileConflict"));
    }

    /**
	 * This creates a model editor.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Slee11Editor() {
        super();
        initializeEditingDomain();
    }

    /**
	 * This sets up the editing domain for the model editor.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
    protected void initializeEditingDomain() {
        adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
        adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
        adapterFactory.addAdapterFactory(new Slee11ItemProviderAdapterFactory());
        adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
        BasicCommandStack commandStack = new BasicCommandStack();
        commandStack.addCommandStackListener(new CommandStackListener() {

            public void commandStackChanged(final EventObject event) {
                getContainer().getDisplay().asyncExec(new Runnable() {

                    public void run() {
                        firePropertyChange(IEditorPart.PROP_DIRTY);
                        if (propertySheetPage != null && !propertySheetPage.getControl().isDisposed()) {
                            propertySheetPage.refresh();
                        }
                    }
                });
            }
        });
        editingDomain = new AdapterFactoryEditingDomain(adapterFactory, commandStack, new HashMap<Resource, Boolean>());
        XMLParserPool pool = new ParserPool();
        editingDomain.getResourceSet().getLoadOptions().put(XMLResource.OPTION_USE_PARSER_POOL, pool);
    }

    /**
	 * this class is needed to provide the Slee11XmlHandler.
	 * @author john
	 *
	 */
    class ParserPool extends XMLParserPoolImpl {

        @Override
        public synchronized XMLDefaultHandler getDefaultHandler(XMLResource resource, XMLLoad xmlLoad, XMLHelper helper, Map<?, ?> options) {
            return new Slee11XmlHandler(resource, helper, options);
        }

        @Override
        public synchronized void releaseDefaultHandler(XMLDefaultHandler handler, Map<?, ?> options) {
        }
    }

    /**
	 * This is here for the listener to be able to call it.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected void firePropertyChange(int action) {
        super.firePropertyChange(action);
    }

    /**
	 * This sets the selection into whichever viewer is active.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSelectionToViewer(Collection<?> collection) {
        final Collection<?> theSelection = collection;
        if (theSelection != null && !theSelection.isEmpty()) {
            Runnable runnable = new Runnable() {

                public void run() {
                    if (currentViewer != null) {
                        currentViewer.setSelection(new StructuredSelection(theSelection.toArray()), true);
                    }
                }
            };
            getSite().getShell().getDisplay().asyncExec(runnable);
        }
    }

    /**
	 * This returns the editing domain as required by the {@link IEditingDomainProvider} interface.
	 * This is important for implementing the static methods of {@link AdapterFactoryEditingDomain}
	 * and for supporting {@link org.eclipse.emf.edit.ui.action.CommandAction}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EditingDomain getEditingDomain() {
        return editingDomain;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public class ReverseAdapterFactoryContentProvider extends AdapterFactoryContentProvider {

        /**
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        public ReverseAdapterFactoryContentProvider(AdapterFactory adapterFactory) {
            super(adapterFactory);
        }

        /**
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        @Override
        public Object[] getElements(Object object) {
            Object parent = super.getParent(object);
            return (parent == null ? Collections.EMPTY_SET : Collections.singleton(parent)).toArray();
        }

        /**
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        @Override
        public Object[] getChildren(Object object) {
            Object parent = super.getParent(object);
            return (parent == null ? Collections.EMPTY_SET : Collections.singleton(parent)).toArray();
        }

        /**
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        @Override
        public boolean hasChildren(Object object) {
            Object parent = super.getParent(object);
            return parent != null;
        }

        /**
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        @Override
        public Object getParent(Object object) {
            return null;
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCurrentViewerPane(ViewerPane viewerPane) {
        if (currentViewerPane != viewerPane) {
            if (currentViewerPane != null) {
                currentViewerPane.showFocus(false);
            }
            currentViewerPane = viewerPane;
        }
        setCurrentViewer(currentViewerPane.getViewer());
    }

    /**
	 * This makes sure that one content viewer, either for the current page or the outline view, if it has focus,
	 * is the current one.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCurrentViewer(Viewer viewer) {
        if (currentViewer != viewer) {
            if (selectionChangedListener == null) {
                selectionChangedListener = new ISelectionChangedListener() {

                    public void selectionChanged(SelectionChangedEvent selectionChangedEvent) {
                        setSelection(selectionChangedEvent.getSelection());
                    }
                };
            }
            if (currentViewer != null) {
                currentViewer.removeSelectionChangedListener(selectionChangedListener);
            }
            if (viewer != null) {
                viewer.addSelectionChangedListener(selectionChangedListener);
            }
            currentViewer = viewer;
            setSelection(currentViewer == null ? StructuredSelection.EMPTY : currentViewer.getSelection());
        }
    }

    /**
	 * This returns the viewer as required by the {@link IViewerProvider} interface.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Viewer getViewer() {
        return currentViewer;
    }

    /**
	 * This creates a context menu for the viewer and adds a listener as well registering the menu for extension.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void createContextMenuFor(StructuredViewer viewer) {
        MenuManager contextMenu = new MenuManager("#PopUp");
        contextMenu.add(new Separator("additions"));
        contextMenu.setRemoveAllWhenShown(true);
        contextMenu.addMenuListener(this);
        Menu menu = contextMenu.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
        getSite().registerContextMenu(contextMenu, new UnwrappingSelectionProvider(viewer));
        int dndOperations = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
        Transfer[] transfers = new Transfer[] { LocalTransfer.getInstance() };
        viewer.addDragSupport(dndOperations, transfers, new ViewerDragAdapter(viewer));
        viewer.addDropSupport(dndOperations, transfers, new EditingDomainViewerDropAdapter(editingDomain, viewer));
    }

    /**
	 * This is the method called to load a resource into the editing domain's resource set based on the editor's input.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void createModel() {
        URI resourceURI = EditUIUtil.getURI(getEditorInput());
        Exception exception = null;
        Resource resource = null;
        try {
            resource = editingDomain.getResourceSet().getResource(resourceURI, true);
        } catch (Exception e) {
            exception = e;
            resource = editingDomain.getResourceSet().getResource(resourceURI, false);
        }
        Diagnostic diagnostic = analyzeResourceProblems(resource, exception);
        if (diagnostic.getSeverity() != Diagnostic.OK) {
            resourceToDiagnosticMap.put(resource, analyzeResourceProblems(resource, exception));
        }
        editingDomain.getResourceSet().eAdapters().add(problemIndicationAdapter);
    }

    /**
	 * Returns a diagnostic describing the errors and warnings listed in the resource
	 * and the specified exception (if any).
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Diagnostic analyzeResourceProblems(Resource resource, Exception exception) {
        if (!resource.getErrors().isEmpty() || !resource.getWarnings().isEmpty()) {
            BasicDiagnostic basicDiagnostic = new BasicDiagnostic(Diagnostic.ERROR, "org.jjflyboy.slee.descriptors.editor", 0, getString("_UI_CreateModelError_message", resource.getURI()), new Object[] { exception == null ? (Object) resource : exception });
            basicDiagnostic.merge(EcoreUtil.computeDiagnostic(resource, true));
            return basicDiagnostic;
        } else if (exception != null) {
            return new BasicDiagnostic(Diagnostic.ERROR, "org.jjflyboy.slee.descriptors.editor", 0, getString("_UI_CreateModelError_message", resource.getURI()), new Object[] { exception });
        } else {
            return Diagnostic.OK_INSTANCE;
        }
    }

    @Override
    protected void addPages() {
        createModel();
        if (!getEditingDomain().getResourceSet().getResources().isEmpty()) {
            try {
                descriptorPage = new DescriptorPage(this);
                descriptorPageIndex = addPage(descriptorPage);
            } catch (PartInitException e) {
                SleeEditorPlugin.INSTANCE.log("error creating page");
                throw new RuntimeException(e);
            }
        }
        getContainer().addControlListener(new ControlAdapter() {

            boolean guard = false;

            @Override
            public void controlResized(ControlEvent event) {
                if (!guard) {
                    guard = true;
                    hideTabs();
                    guard = false;
                }
            }
        });
        getSite().getShell().getDisplay().asyncExec(new Runnable() {

            public void run() {
                updateProblemIndication();
            }
        });
    }

    /**
	 * This is the method used by the framework to install your own controls.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
    public void createPages() {
        super.createPages();
    }

    /**
	 * If there is just one page in the multi-page editor part,
	 * this hides the single tab at the bottom.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void hideTabs() {
        if (getPageCount() <= 1) {
            setPageText(0, "");
            if (getContainer() instanceof CTabFolder) {
                ((CTabFolder) getContainer()).setTabHeight(1);
                Point point = getContainer().getSize();
                getContainer().setSize(point.x, point.y + 6);
            }
        }
    }

    /**
	 * If there is more than one page in the multi-page editor part,
	 * this shows the tabs at the bottom.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void showTabs() {
        if (getPageCount() > 1) {
            setPageText(0, getString("_UI_SelectionPage_label"));
            if (getContainer() instanceof CTabFolder) {
                ((CTabFolder) getContainer()).setTabHeight(SWT.DEFAULT);
                Point point = getContainer().getSize();
                getContainer().setSize(point.x, point.y - 6);
            }
        }
    }

    /**
	 * This is used to track the active viewer.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
    @Override
    protected void pageChange(int pageIndex) {
        super.pageChange(pageIndex);
        if (pageIndex == descriptorPageIndex) {
            setCurrentViewer(descriptorPage.getViewer());
        }
        if (contentOutlinePage != null) {
            handleContentOutlineSelection(contentOutlinePage.getSelection());
        }
    }

    /**
	 * This is how the framework determines which interfaces we implement.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public Object getAdapter(Class key) {
        if (key.equals(IContentOutlinePage.class)) {
            return showOutlineView() ? getContentOutlinePage() : null;
        } else if (key.equals(IPropertySheetPage.class)) {
            return getPropertySheetPage();
        } else if (key.equals(IGotoMarker.class)) {
            return this;
        } else {
            return super.getAdapter(key);
        }
    }

    /**
	 * This accesses a cached version of the content outliner.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
    public IContentOutlinePage getContentOutlinePage() {
        if (contentOutlinePage == null) {
            class MyContentOutlinePage extends ContentOutlinePage {

                @Override
                public void createControl(Composite parent) {
                    super.createControl(parent);
                    contentOutlineViewer = getTreeViewer();
                    contentOutlineViewer.addSelectionChangedListener(this);
                    contentOutlineViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
                    contentOutlineViewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
                    contentOutlineViewer.setInput(editingDomain.getResourceSet());
                    createContextMenuFor(contentOutlineViewer);
                    if (!editingDomain.getResourceSet().getResources().isEmpty()) {
                        contentOutlineViewer.setSelection(new StructuredSelection(editingDomain.getResourceSet().getResources().get(0)), true);
                    }
                }

                @Override
                public void makeContributions(IMenuManager menuManager, IToolBarManager toolBarManager, IStatusLineManager statusLineManager) {
                    super.makeContributions(menuManager, toolBarManager, statusLineManager);
                    contentOutlineStatusLineManager = statusLineManager;
                }

                @Override
                public void setActionBars(IActionBars actionBars) {
                    super.setActionBars(actionBars);
                    getActionBarContributor().shareGlobalActions(this, actionBars);
                }
            }
            contentOutlinePage = new MyContentOutlinePage();
            contentOutlinePage.addSelectionChangedListener(new ISelectionChangedListener() {

                public void selectionChanged(SelectionChangedEvent event) {
                    if (currentViewer != null && currentViewer == contentOutlineViewer) handleContentOutlineSelection(event.getSelection());
                }
            });
        }
        return contentOutlinePage;
    }

    /**
	 * This accesses a cached version of the property sheet.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public IPropertySheetPage getPropertySheetPage() {
        if (propertySheetPage == null) {
            propertySheetPage = new ExtendedPropertySheetPage(editingDomain) {

                @Override
                public void setSelectionToViewer(List<?> selection) {
                    Slee11Editor.this.setSelectionToViewer(selection);
                    Slee11Editor.this.setFocus();
                }

                @Override
                public void setActionBars(IActionBars actionBars) {
                    super.setActionBars(actionBars);
                    getActionBarContributor().shareGlobalActions(this, actionBars);
                }
            };
            propertySheetPage.setPropertySourceProvider(new AdapterFactoryContentProvider(adapterFactory));
        }
        return propertySheetPage;
    }

    /**
	 * This deals with how we want selection in the outliner to affect the other views.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
    public void handleContentOutlineSelection(ISelection selection) {
        if (getActivePageInstance() != null && !selection.isEmpty() && selection instanceof IStructuredSelection) {
            if (getActivePageInstance() instanceof DescriptorPage) {
                ((DescriptorPage) getActivePageInstance()).getViewer().setSelection(selection);
            }
        }
        if (currentViewerPane != null && !selection.isEmpty() && selection instanceof IStructuredSelection) {
            Iterator<?> selectedElements = ((IStructuredSelection) selection).iterator();
            if (selectedElements.hasNext()) {
                Object selectedElement = selectedElements.next();
                if (currentViewerPane.getViewer() == selectionViewer) {
                    ArrayList<Object> selectionList = new ArrayList<Object>();
                    selectionList.add(selectedElement);
                    while (selectedElements.hasNext()) {
                        selectionList.add(selectedElements.next());
                    }
                    selectionViewer.setSelection(new StructuredSelection(selectionList));
                } else {
                    if (currentViewerPane.getViewer().getInput() != selectedElement) {
                        currentViewerPane.getViewer().setInput(selectedElement);
                        currentViewerPane.setTitle(selectedElement);
                    }
                }
            }
        }
    }

    /**
	 * This is for implementing {@link IEditorPart} and simply tests the command stack.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean isDirty() {
        return ((BasicCommandStack) editingDomain.getCommandStack()).isSaveNeeded();
    }

    /**
	 * This is for implementing {@link IEditorPart} and simply saves the model file.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void doSave(IProgressMonitor progressMonitor) {
        final Map<Object, Object> saveOptions = new HashMap<Object, Object>();
        saveOptions.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
        WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {

            @Override
            public void execute(IProgressMonitor monitor) {
                boolean first = true;
                for (Resource resource : editingDomain.getResourceSet().getResources()) {
                    if ((first || !resource.getContents().isEmpty() || isPersisted(resource)) && !editingDomain.isReadOnly(resource)) {
                        try {
                            long timeStamp = resource.getTimeStamp();
                            resource.save(saveOptions);
                            if (resource.getTimeStamp() != timeStamp) {
                                savedResources.add(resource);
                            }
                        } catch (Exception exception) {
                            resourceToDiagnosticMap.put(resource, analyzeResourceProblems(resource, exception));
                        }
                        first = false;
                    }
                }
            }
        };
        updateProblemIndication = false;
        try {
            new ProgressMonitorDialog(getSite().getShell()).run(true, false, operation);
            ((BasicCommandStack) editingDomain.getCommandStack()).saveIsDone();
            firePropertyChange(IEditorPart.PROP_DIRTY);
        } catch (Exception exception) {
            SleeEditorPlugin.INSTANCE.log(exception);
        }
        updateProblemIndication = true;
        updateProblemIndication();
    }

    /**
	 * This returns whether something has been persisted to the URI of the specified resource.
	 * The implementation uses the URI converter from the editor's resource set to try to open an input stream. 
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected boolean isPersisted(Resource resource) {
        boolean result = false;
        try {
            InputStream stream = editingDomain.getResourceSet().getURIConverter().createInputStream(resource.getURI());
            if (stream != null) {
                result = true;
                stream.close();
            }
        } catch (IOException e) {
        }
        return result;
    }

    /**
	 * This always returns true because it is not currently supported.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean isSaveAsAllowed() {
        return true;
    }

    /**
	 * This also changes the editor's input.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void doSaveAs() {
        SaveAsDialog saveAsDialog = new SaveAsDialog(getSite().getShell());
        saveAsDialog.open();
        IPath path = saveAsDialog.getResult();
        if (path != null) {
            IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
            if (file != null) {
                doSaveAs(URI.createPlatformResourceURI(file.getFullPath().toString(), true), new FileEditorInput(file));
            }
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void doSaveAs(URI uri, IEditorInput editorInput) {
        (editingDomain.getResourceSet().getResources().get(0)).setURI(uri);
        setInputWithNotify(editorInput);
        setPartName(editorInput.getName());
        IProgressMonitor progressMonitor = getActionBars().getStatusLineManager() != null ? getActionBars().getStatusLineManager().getProgressMonitor() : new NullProgressMonitor();
        doSave(progressMonitor);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
    public void gotoMarker(IMarker marker) {
        try {
            String type = marker.getType();
            if (EValidator.MARKER.equals(type) || MarkerUtil.VALIDATION_MARKER_TYPE.equals(type)) {
                String uriAttribute = marker.getAttribute(EValidator.URI_ATTRIBUTE, null);
                if (uriAttribute != null) {
                    URI uri = URI.createURI(uriAttribute);
                    EObject eObject = editingDomain.getResourceSet().getEObject(uri, true);
                    if (eObject != null) {
                        setSelectionToViewer(Collections.singleton(editingDomain.getWrapper(eObject)));
                    }
                }
            }
        } catch (CoreException exception) {
            SleeEditorPlugin.INSTANCE.log(exception);
        }
    }

    /**
	 * This is called during startup.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void init(IEditorSite site, IEditorInput editorInput) {
        setSite(site);
        setInputWithNotify(editorInput);
        setPartName(editorInput.getName());
        site.setSelectionProvider(this);
        site.getPage().addPartListener(partListener);
        ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeListener, IResourceChangeEvent.POST_CHANGE);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void setFocus() {
        if (currentViewerPane != null) {
            currentViewerPane.setFocus();
        } else {
            getControl(getActivePage()).setFocus();
        }
    }

    /**
	 * This implements {@link org.eclipse.jface.viewers.ISelectionProvider}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        selectionChangedListeners.add(listener);
    }

    /**
	 * This implements {@link org.eclipse.jface.viewers.ISelectionProvider}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
        selectionChangedListeners.remove(listener);
    }

    /**
	 * This implements {@link org.eclipse.jface.viewers.ISelectionProvider} to return this editor's overall selection.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ISelection getSelection() {
        return editorSelection;
    }

    /**
	 * This implements {@link org.eclipse.jface.viewers.ISelectionProvider} to set this editor's overall selection.
	 * Calling this result will notify the listeners.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
    public void setSelection(ISelection selection) {
        editorSelection = selection;
        for (ISelectionChangedListener listener : selectionChangedListeners) {
            listener.selectionChanged(new SelectionChangedEvent(this, selection));
        }
        if (contentOutlineViewer != null && contentOutlineViewer != currentViewer) contentOutlineViewer.setSelection(selection);
        setStatusLineManager(selection);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setStatusLineManager(ISelection selection) {
        IStatusLineManager statusLineManager = currentViewer != null && currentViewer == contentOutlineViewer ? contentOutlineStatusLineManager : getActionBars().getStatusLineManager();
        if (statusLineManager != null) {
            if (selection instanceof IStructuredSelection) {
                Collection<?> collection = ((IStructuredSelection) selection).toList();
                switch(collection.size()) {
                    case 0:
                        {
                            statusLineManager.setMessage(getString("_UI_NoObjectSelected"));
                            break;
                        }
                    case 1:
                        {
                            String text = new AdapterFactoryItemDelegator(adapterFactory).getText(collection.iterator().next());
                            statusLineManager.setMessage(getString("_UI_SingleObjectSelected", text));
                            break;
                        }
                    default:
                        {
                            statusLineManager.setMessage(getString("_UI_MultiObjectSelected", Integer.toString(collection.size())));
                            break;
                        }
                }
            } else {
                statusLineManager.setMessage("");
            }
        }
    }

    /**
	 * This looks up a string in the plugin's plugin.properties file.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private static String getString(String key) {
        return SleeEditorPlugin.INSTANCE.getString(key);
    }

    /**
	 * This looks up a string in plugin.properties, making a substitution.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private static String getString(String key, Object s1) {
        return SleeEditorPlugin.INSTANCE.getString(key, new Object[] { s1 });
    }

    /**
	 * This implements {@link org.eclipse.jface.action.IMenuListener} to help fill the context menus with contributions from the Edit menu.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void menuAboutToShow(IMenuManager menuManager) {
        ((IMenuListener) getEditorSite().getActionBarContributor()).menuAboutToShow(menuManager);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EditingDomainActionBarContributor getActionBarContributor() {
        return (EditingDomainActionBarContributor) getEditorSite().getActionBarContributor();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public IActionBars getActionBars() {
        return getActionBarContributor().getActionBars();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AdapterFactory getAdapterFactory() {
        return adapterFactory;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void dispose() {
        updateProblemIndication = false;
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceChangeListener);
        getSite().getPage().removePartListener(partListener);
        adapterFactory.dispose();
        if (getActionBarContributor().getActiveEditor() == this) {
            getActionBarContributor().setActiveEditor(null);
        }
        if (propertySheetPage != null) {
            propertySheetPage.dispose();
        }
        if (contentOutlinePage != null) {
            contentOutlinePage.dispose();
        }
        super.dispose();
    }

    /**
	 * Returns whether the outline view should be presented to the user.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected boolean showOutlineView() {
        return true;
    }

    public DataBindingContext getBindingContext() {
        if (bindingContext == null) {
            bindingContext = new EMFDataBindingContext();
        }
        return bindingContext;
    }

    @Override
    protected FormToolkit createToolkit(Display display) {
        return new Slee11FormToolkit(display);
    }
}
