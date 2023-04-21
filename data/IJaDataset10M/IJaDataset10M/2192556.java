package org.dengues.ui.editors;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import org.dengues.core.DenguesCorePlugin;
import org.dengues.core.components.IComponentsFactory;
import org.dengues.core.prefs.IDenguesPrefsConstant;
import org.dengues.core.warehouse.ENodeStatus;
import org.dengues.core.warehouse.IWarehouseNode;
import org.dengues.core.warehouse.IWarehouseView;
import org.dengues.ui.DenguesUiPlugin;
import org.dengues.ui.editors.parts.GEFEditorFreeformRootEditPart;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.actions.ToggleGridAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

/**
 * Qiang.Zhang.Adolf@gmail.com class global comment. Detailled comment <br/>
 * 
 * $Id: Dengues.epf 2008-1-7 qiang.zhang $
 * 
 */
public abstract class AbstractGenericGEFEditor extends GraphicalEditorWithFlyoutPalette {

    private boolean savePreviouslyNeeded;

    private static Point mouseLocation = new Point();

    protected final IComponentsFactory factory = DenguesCorePlugin.getDefault().getDesignerCoreService().getComponentsFactory();

    @Override
    public Control getGraphicalControl() {
        Control graphicalControl = super.getGraphicalControl();
        graphicalControl.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseUp(MouseEvent e) {
                mouseLocation.x = e.x;
                mouseLocation.y = e.y;
            }
        });
        return graphicalControl;
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "getMouseLocation".
     * 
     * @return
     */
    public static Point getMouseLocation() {
        return mouseLocation;
    }

    @Override
    public void commandStackChanged(EventObject event) {
        if (isDirty()) {
            if (!this.savePreviouslyNeeded) {
                this.savePreviouslyNeeded = true;
                firePropertyChange(IEditorPart.PROP_DIRTY);
            }
        } else {
            savePreviouslyNeeded = false;
            firePropertyChange(IEditorPart.PROP_DIRTY);
        }
        super.commandStackChanged(event);
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "getCommonKeyHandler".
     * 
     * @return
     */
    protected KeyHandler getCommonKeyHandler() {
        KeyHandler sharedKeyHandler = new KeyHandler();
        sharedKeyHandler.put(KeyStroke.getPressed(SWT.DEL, 0), getActionRegistry().getAction(ActionFactory.DELETE.getId()));
        return sharedKeyHandler;
    }

    @Override
    protected void setInput(IEditorInput input) {
        super.setInput(input);
        if (input instanceof AbstractEditorInput) {
            AbstractEditorInput processEditorInput = ((AbstractEditorInput) input);
            IWarehouseNode warehouseNode = processEditorInput.getWarehouseNode();
            if (warehouseNode != null) {
                warehouseNode.setNodeStatus(ENodeStatus.ACTIVED);
                refreshWarehouseView();
            }
        }
    }

    @Override
    public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
        super.selectionChanged(part, selection);
        if (this.equals(part)) {
            updateActions(getSelectionActions());
        }
    }

    @Override
    public void dispose() {
        if (getEditorInput() instanceof AbstractEditorInput) {
            AbstractEditorInput processEditorInput = ((AbstractEditorInput) getEditorInput());
            IWarehouseNode warehouseNode = processEditorInput.getWarehouseNode();
            if (warehouseNode != null) {
                warehouseNode.setNodeStatus(ENodeStatus.NORMAL);
                refreshWarehouseView();
            }
        }
        super.dispose();
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com AbstractGenericGEFEditor constructor comment.
     */
    public AbstractGenericGEFEditor() {
        setEditDomain(new DefaultEditDomain(this));
    }

    public GraphicalViewer getViewer() {
        return getGraphicalViewer();
    }

    private boolean dirtyState = false;

    protected GEFEditorFreeformRootEditPart root;

    protected ScrollingGraphicalViewer viewer;

    @Override
    public boolean isDirty() {
        return dirtyState || getCommandStack().isDirty();
    }

    public void setDirty(boolean dirty) {
        dirtyState = dirty;
        if (dirtyState) {
            firePropertyChange(IEditorPart.PROP_DIRTY);
        }
    }

    @Override
    protected FlyoutPreferences getPalettePreferences() {
        return GEFEditorUtils.createPalettePreferences();
    }

    @Override
    public CommandStack getCommandStack() {
        return super.getCommandStack();
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "getAction".
     * 
     * @param actionID
     * @return
     */
    public IAction getAction(String actionID) {
        return getActionRegistry().getAction(actionID);
    }

    @Override
    protected PaletteViewerProvider createPaletteViewerProvider() {
        return GEFEditorUtils.createPaletteViewerProvider(getEditDomain());
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "refreshWarehouseView".
     */
    protected void refreshWarehouseView() {
        IViewPart findView = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(IWarehouseView.VIEW_ID);
        if (findView instanceof IWarehouseView) {
            ((IWarehouseView) findView).refresh(((AbstractEditorInput) getEditorInput()).getWarehouseNode().getParent());
        }
    }

    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        viewer = (ScrollingGraphicalViewer) getGraphicalViewer();
        root = new GEFEditorFreeformRootEditPart(getEditorInput());
        viewer.setRootEditPart(root);
        root.setCommandStack(getCommandStack());
        List<String> zoomLevels = new ArrayList<String>();
        zoomLevels.add(ZoomManager.FIT_ALL);
        zoomLevels.add(ZoomManager.FIT_WIDTH);
        zoomLevels.add(ZoomManager.FIT_HEIGHT);
        root.getZoomManager().setZoomLevelContributions(zoomLevels);
        IAction zoomIn = new ZoomInAction(root.getZoomManager());
        IAction zoomOut = new ZoomOutAction(root.getZoomManager());
        getActionRegistry().registerAction(zoomIn);
        getActionRegistry().registerAction(zoomOut);
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_SPACING, new Dimension(getGrid(), getGrid()));
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, new Boolean(true));
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, new Boolean(true));
        IAction showGrid = new ToggleGridAction(getGraphicalViewer());
        getActionRegistry().registerAction(showGrid);
    }

    @Override
    protected void initializeGraphicalViewer() {
        super.initializeGraphicalViewer();
        getGraphicalViewer().addDropTargetListener(createTransferDropTargetListener());
    }

    /**
     * Create a transfer drop target listener. When using a CombinedTemplateCreationEntry tool in the palette, this will
     * enable model element creation by dragging from the palette.
     * 
     * @see #createPaletteViewerProvider()
     */
    private TransferDropTargetListener createTransferDropTargetListener() {
        return new TemplateTransferDropTargetListener(getGraphicalViewer()) {

            @Override
            protected CreationFactory getFactory(Object template) {
                return new SimpleFactory((Class) template);
            }
        };
    }

    @Override
    public Object getAdapter(Class type) {
        if (type == ZoomManager.class) {
            return getGraphicalViewer().getProperty(ZoomManager.class.toString());
        }
        return super.getAdapter(type);
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "getGrid".
     * 
     * @return
     */
    public static int getGrid() {
        return DenguesUiPlugin.getDefault().getPreferenceStore().getInt(IDenguesPrefsConstant.DESIGNER_EDITOR_GRID);
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "getDefaultSize".
     * 
     * @return
     */
    public static Dimension getDefaultSize() {
        int h = DenguesUiPlugin.getDefault().getPreferenceStore().getInt(IDenguesPrefsConstant.DESIGNER_COMPONENT_HEIGHT);
        int w = DenguesUiPlugin.getDefault().getPreferenceStore().getInt(IDenguesPrefsConstant.DESIGNER_COMPONENT_WITH);
        return new Dimension(w * getGrid(), h * getGrid());
    }
}
