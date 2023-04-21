package net.java.amateras.uml;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import net.java.amateras.uml.action.AbstractUMLEditorAction;
import net.java.amateras.uml.action.OpenOutlineViewAction;
import net.java.amateras.uml.action.OpenPropertyViewAction;
import net.java.amateras.uml.action.SaveAsImageAction;
import net.java.amateras.uml.dnd.UMLDropTargetListenerFactory;
import net.java.amateras.uml.model.AbstractUMLEntityModel;
import net.java.amateras.uml.model.AbstractUMLModel;
import net.java.amateras.uml.model.RootModel;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.PrintAction;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.GraphicalEditorWithPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 * GEF���g�p�����G�f�B�^�B
 * 
 * @author Takahiro Shida.
 */
public abstract class DiagramEditor extends GraphicalEditorWithPalette implements IPropertyChangeListener, IResourceChangeListener {

    private boolean savePreviouslyNeeded = false;

    private AbstractUMLEditorAction openOutlineAction = null;

    private AbstractUMLEditorAction openPropertyAction = null;

    private AbstractUMLEditorAction saveAsImageAction = null;

    private boolean needViewerRefreshFlag = true;

    private KeyHandler sharedKeyHandler;

    public DiagramEditor() {
        super();
        setEditDomain(new DefaultEditDomain(this));
        getActionRegistry().registerAction(new UndoRetargetAction());
        getActionRegistry().registerAction(new RedoRetargetAction());
        getActionRegistry().registerAction(new DeleteRetargetAction());
        UMLPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(this);
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
    }

    protected void initializeGraphicalViewer() {
        GraphicalViewer viewer = getGraphicalViewer();
        IFile file = ((IFileEditorInput) getEditorInput()).getFile();
        RootModel root = null;
        if (file.exists()) {
            try {
                root = DiagramSerializer.deserialize(file.getContents());
                validateModel(root);
            } catch (Exception ex) {
                UMLPlugin.logException(ex);
            }
        }
        if (root == null) {
            root = createInitializeModel();
        }
        viewer.setContents(root);
        addDndSupport(viewer, getDiagramType());
        applyPreferences();
    }

    private void refreshGraphicalViewer() {
        IEditorInput input = getEditorInput();
        if (input instanceof IFileEditorInput) {
            try {
                IFile file = ((IFileEditorInput) input).getFile();
                GraphicalViewer viewer = getGraphicalViewer();
                RootModel newRoot = null;
                try {
                    newRoot = DiagramSerializer.deserialize(file.getContents());
                } catch (Exception ex) {
                    UMLPlugin.logException(ex);
                    return;
                }
                RootModel root = (RootModel) viewer.getContents().getModel();
                root.copyFrom(newRoot);
            } catch (Exception ex) {
                UMLPlugin.logException(ex);
            }
        }
    }

    public void resourceChanged(final IResourceChangeEvent event) {
        if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
            final IEditorInput input = getEditorInput();
            if (input instanceof IFileEditorInput) {
                Display.getDefault().asyncExec(new Runnable() {

                    public void run() {
                        IFile file = ((IFileEditorInput) input).getFile();
                        if (!file.exists()) {
                            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                            page.closeEditor(DiagramEditor.this, false);
                        } else {
                            if (!getPartName().equals(file.getName())) {
                                setPartName(file.getName());
                            }
                            if (needViewerRefreshFlag) {
                                refreshGraphicalViewer();
                            } else {
                                needViewerRefreshFlag = true;
                            }
                        }
                    }
                });
            }
        }
    }

    public void propertyChange(PropertyChangeEvent event) {
        applyPreferences();
    }

    protected void applyPreferences() {
        IPreferenceStore store = UMLPlugin.getDefault().getPreferenceStore();
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, new Boolean(store.getBoolean(UMLPlugin.PREF_SHOW_GRID)));
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, new Boolean(store.getBoolean(UMLPlugin.PREF_SHOW_GRID)));
        int gridSize = store.getInt(UMLPlugin.PREF_GRID_SIZE);
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_SPACING, new Dimension(gridSize, gridSize));
        getGraphicalViewer().setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED, new Boolean(store.getBoolean(UMLPlugin.PREF_SNAP_GEOMETRY)));
    }

    /**
	 * �ȑO�̃o�[�W�����ō쐬���ꂽ�t�@�C���������C������.
	 * -v2.0 �e�q�֌W�̒ǉ�
	 */
    private void validateModel(AbstractUMLEntityModel parent) {
        List children = parent.getChildren();
        for (Iterator iter = children.iterator(); iter.hasNext(); ) {
            AbstractUMLModel element = (AbstractUMLModel) iter.next();
            if (element.getParent() == null) {
                element.setParent(parent);
            }
            if (element instanceof AbstractUMLEntityModel) {
                validateModel((AbstractUMLEntityModel) element);
            }
        }
    }

    /**
	 * ���[�h�Ɏ��s�����ꍇ�̏���f����ԋp����.
	 * @return 
	 */
    protected abstract RootModel createInitializeModel();

    /**
	 * �G�f�B�^�̃^�C�v��ԋp����. (ex class)
	 * @return
	 */
    protected abstract String getDiagramType();

    /**
	 * Diagram���f����ԋp����.
	 * @return
	 */
    public static final RootModel getActiveDiagramModel() {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (window != null) {
            IWorkbenchPage page = window.getActivePage();
            if (page != null) {
                IEditorPart editor = window.getActivePage().getActiveEditor();
                return (RootModel) editor.getAdapter(RootModel.class);
            }
        }
        return null;
    }

    /**
	 * @param viewer
	 */
    private void addDndSupport(GraphicalViewer viewer, String type) {
        IExtensionPoint point = Platform.getExtensionRegistry().getExtensionPoint(UMLPlugin.PLUGIN_ID, "dnd");
        IExtension[] extensions = point.getExtensions();
        for (int i = 0; i < extensions.length; i++) {
            IExtension extension = extensions[i];
            IConfigurationElement[] elements = extension.getConfigurationElements();
            for (int j = 0; j < elements.length; j++) {
                IConfigurationElement element = elements[j];
                try {
                    Object object = element.createExecutableExtension("class");
                    if (object instanceof UMLDropTargetListenerFactory) {
                        UMLDropTargetListenerFactory factory = (UMLDropTargetListenerFactory) object;
                        if (factory.accept(type)) {
                            TransferDropTargetListener targetListener = factory.getDropTargetListener(viewer);
                            viewer.addDropTargetListener(targetListener);
                        }
                    }
                } catch (CoreException e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }
    }

    protected abstract EditPartFactory createEditPartFactory();

    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setEditPartFactory(createEditPartFactory());
        ScalableRootEditPart rootEditPart = new ScalableRootEditPart();
        viewer.setRootEditPart(rootEditPart);
        ZoomManager manager = rootEditPart.getZoomManager();
        double[] zoomLevels = new double[] { 0.25, 0.5, 0.75, 1.0, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0, 10.0, 20.0 };
        manager.setZoomLevels(zoomLevels);
        ArrayList zoomContributions = new ArrayList();
        zoomContributions.add(ZoomManager.FIT_ALL);
        zoomContributions.add(ZoomManager.FIT_HEIGHT);
        zoomContributions.add(ZoomManager.FIT_WIDTH);
        manager.setZoomLevelContributions(zoomContributions);
        getActionRegistry().registerAction(new ZoomInAction(manager));
        getActionRegistry().registerAction(new ZoomOutAction(manager));
        getGraphicalViewer().setKeyHandler(new GraphicalViewerKeyHandler(getGraphicalViewer()));
        String menuId = this.getClass().getName() + ".EditorContext";
        MenuManager menuMgr = new MenuManager(menuId, menuId);
        openPropertyAction = new OpenPropertyViewAction(viewer);
        openOutlineAction = new OpenOutlineViewAction(viewer);
        saveAsImageAction = new SaveAsImageAction(viewer);
        createDiagramAction(viewer);
        getSite().registerContextMenu(menuId, menuMgr, viewer);
        PrintAction printAction = new PrintAction(this);
        printAction.setImageDescriptor(UMLPlugin.getImageDescriptor("icons/print.gif"));
        getActionRegistry().registerAction(printAction);
        final DeleteAction deleteAction = new DeleteAction((IWorkbenchPart) this);
        deleteAction.setSelectionProvider(getGraphicalViewer());
        getActionRegistry().registerAction(deleteAction);
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                deleteAction.update();
            }
        });
        menuMgr.add(new Separator("edit"));
        menuMgr.add(getActionRegistry().getAction(ActionFactory.DELETE.getId()));
        menuMgr.add(getActionRegistry().getAction(ActionFactory.UNDO.getId()));
        menuMgr.add(getActionRegistry().getAction(ActionFactory.REDO.getId()));
        menuMgr.add(new Separator("zoom"));
        menuMgr.add(getActionRegistry().getAction(GEFActionConstants.ZOOM_IN));
        menuMgr.add(getActionRegistry().getAction(GEFActionConstants.ZOOM_OUT));
        fillDiagramPopupMenu(menuMgr);
        menuMgr.add(new Separator("print"));
        menuMgr.add(saveAsImageAction);
        menuMgr.add(printAction);
        menuMgr.add(new Separator("views"));
        menuMgr.add(openPropertyAction);
        menuMgr.add(openOutlineAction);
        menuMgr.add(new Separator("generate"));
        menuMgr.add(new Separator("additions"));
        viewer.setContextMenu(menuMgr);
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer).setParent(getCommonKeyHandler()));
    }

    /**
	 * �^�C�v�ɓ��������A�N�V���������.
	 * @param viewer
	 */
    protected abstract void createDiagramAction(GraphicalViewer viewer);

    /**
	 * �A�N�V�������R���e�L�X�g���j���[�ɐݒ肷��.
	 * @param manager
	 */
    protected abstract void fillDiagramPopupMenu(MenuManager manager);

    protected void setInput(IEditorInput input) {
        super.setInput(input);
        setPartName(input.getName());
    }

    public void doSave(IProgressMonitor monitor) {
        try {
            IEditorInput input = getEditorInput();
            if (input instanceof IFileEditorInput) {
                needViewerRefreshFlag = false;
                IFile file = ((IFileEditorInput) input).getFile();
                file.setContents(DiagramSerializer.serialize((RootModel) getGraphicalViewer().getContents().getModel()), true, true, monitor);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        getCommandStack().markSaveLocation();
    }

    public void doSaveAs() {
        doSave(new NullProgressMonitor());
    }

    public boolean isSaveAsAllowed() {
        return true;
    }

    public void dispose() {
        UMLPlugin.getDefault().getPreferenceStore().removePropertyChangeListener(this);
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
        super.dispose();
    }

    public void commandStackChanged(EventObject event) {
        if (isDirty()) {
            if (!savePreviouslyNeeded()) {
                setSavePreviouslyNeeded(true);
                firePropertyChange(IEditorPart.PROP_DIRTY);
            }
        } else {
            setSavePreviouslyNeeded(false);
            firePropertyChange(IEditorPart.PROP_DIRTY);
        }
        super.commandStackChanged(event);
    }

    private void setSavePreviouslyNeeded(boolean value) {
        this.savePreviouslyNeeded = value;
    }

    private boolean savePreviouslyNeeded() {
        return this.savePreviouslyNeeded;
    }

    /**
	 * �G���e�B�e�B�p��PaletteEntry���쐬���܂��B
	 * 
	 * @param itemName �p���b�g�ɕ\������A�C�e����
	 * @param clazz ���f���̃N���X
	 * @param icon �p���b�g�ɕ\������A�C�R���̃p�X
	 * @return PaletteEntry
	 */
    protected PaletteEntry createEntityEntry(String itemName, Class clazz, String icon) {
        CreationToolEntry entry = new CreationToolEntry(itemName, itemName, new SimpleFactory(clazz), UMLPlugin.getImageDescriptor(icon), UMLPlugin.getImageDescriptor(icon));
        return entry;
    }

    /**
	 * �R�l�N�V�����p��PaletteEntry���쐬���܂��B
	 * 
	 * @param itemName �p���b�g�ɕ\������A�C�e����
	 * @param clazz ���f���̃N���X
	 * @param icon �p���b�g�ɕ\������A�C�R���̃p�X
	 * @return PaletteEntry
	 */
    protected PaletteEntry createConnectionEntry(String itemName, Class clazz, String icon) {
        ConnectionCreationToolEntry entry = new ConnectionCreationToolEntry(itemName, itemName, new SimpleFactory(clazz), UMLPlugin.getImageDescriptor(icon), UMLPlugin.getImageDescriptor(icon));
        return entry;
    }

    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        super.selectionChanged(part, selection);
        if (selection instanceof IStructuredSelection) {
            openPropertyAction.update((IStructuredSelection) selection);
            openOutlineAction.update((IStructuredSelection) selection);
            saveAsImageAction.update((IStructuredSelection) selection);
            updateDiagramAction(selection);
        }
    }

    /**
	 * Returns the KeyHandler with common bindings for both the Outline and Graphical Views.
	 * For example, delete is a common action.
	 */
    protected KeyHandler getCommonKeyHandler() {
        if (sharedKeyHandler == null) {
            sharedKeyHandler = new KeyHandler();
            sharedKeyHandler.put(KeyStroke.getPressed(SWT.F2, 0), getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));
        }
        return sharedKeyHandler;
    }

    /**
	 * �A�N�V�������X�V����.
	 * @param selection
	 */
    protected abstract void updateDiagramAction(ISelection selection);

    public Object getAdapter(Class type) {
        if (type == ZoomManager.class) {
            return ((ScalableRootEditPart) getGraphicalViewer().getRootEditPart()).getZoomManager();
        }
        if (type == IContentOutlinePage.class) {
            return new UMLContentOutlinePage();
        }
        if (type == RootModel.class) {
            return getGraphicalViewer().getContents().getModel();
        }
        if (type == CommandStack.class) {
            return getCommandStack();
        }
        return super.getAdapter(type);
    }

    /**
	 * UML�G�f�B�^�̃A�E�g���C���y�[�W�B
	 * �_�C�A�O�����̃T���l�C����\�����܂��B
	 */
    private class UMLContentOutlinePage implements IContentOutlinePage {

        private Canvas canvas;

        private ScrollableThumbnail thumbnail;

        private DisposeListener disposeListener;

        public void createControl(Composite parent) {
            this.canvas = new Canvas(parent, SWT.BORDER);
            LightweightSystem lws = new LightweightSystem(canvas);
            ScalableRootEditPart rootEditPart = (ScalableRootEditPart) getGraphicalViewer().getRootEditPart();
            this.thumbnail = new ScrollableThumbnail((Viewport) rootEditPart.getFigure());
            this.thumbnail.setSource(rootEditPart.getLayer(LayerConstants.PRINTABLE_LAYERS));
            lws.setContents(thumbnail);
            disposeListener = new DisposeListener() {

                public void widgetDisposed(DisposeEvent e) {
                    if (thumbnail != null) {
                        thumbnail.deactivate();
                        thumbnail = null;
                    }
                }
            };
            getGraphicalViewer().getControl().addDisposeListener(disposeListener);
        }

        public Control getControl() {
            return this.canvas;
        }

        public void dispose() {
            if (getGraphicalViewer().getControl() != null && !getGraphicalViewer().getControl().isDisposed()) {
                getGraphicalViewer().getControl().removeDisposeListener(disposeListener);
            }
        }

        public void setActionBars(IActionBars actionBars) {
        }

        public void setFocus() {
        }

        public void addSelectionChangedListener(ISelectionChangedListener listener) {
        }

        public ISelection getSelection() {
            return null;
        }

        public void removeSelectionChangedListener(ISelectionChangedListener listener) {
        }

        public void setSelection(ISelection selection) {
        }
    }
}
