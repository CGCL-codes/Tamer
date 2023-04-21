package org.suse.swamp.designer.editors.process;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.gef.*;
import org.eclipse.gef.dnd.*;
import org.eclipse.gef.editparts.*;
import org.eclipse.gef.palette.*;
import org.eclipse.gef.requests.*;
import org.eclipse.gef.ui.actions.*;
import org.eclipse.gef.ui.palette.*;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.*;
import org.eclipse.gef.ui.parts.*;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.actions.*;
import org.eclipse.ui.dialogs.*;
import org.eclipse.ui.part.*;
import org.eclipse.ui.views.contentoutline.*;
import org.suse.swamp.designer.editors.*;
import org.suse.swamp.designer.editors.xml.*;
import org.suse.swamp.designer.factory.*;
import org.suse.swamp.designer.model.*;
import org.suse.swamp.designer.part.*;

/**
 * A graphical editor with flyout palette that can edit .process files. The
 * binding between the .process file extension and this editor is done in
 * plugin.xml
 * 
 * @author Mahmoud Mahran
 */
public class ProcessEditor extends GraphicalEditorWithFlyoutPalette {

    /** This is the root of the editor's model. */
    private ShapesDiagram diagram;

    private static PaletteRoot PALETTE_MODEL;

    protected SWAMPMultiPageEditor parentEditor = null;

    /** Create a new ShapesEditor instance. This is called by the Workspace. */
    public ProcessEditor() {
        setEditDomain(new DefaultEditDomain(this));
    }

    public ProcessEditor(SWAMPMultiPageEditor parentEditor) {
        this.parentEditor = parentEditor;
        DefaultEditDomain defaultEditDomain = parentEditor.getEditDomain();
        setEditDomain(defaultEditDomain);
    }

    /**
     * Configure the graphical viewer before it receives contents.
     * <p>
     * This is the place to choose an appropriate RootEditPart and
     * EditPartFactory for your editor. The RootEditPart determines the behavior
     * of the editor's "work-area". For example, GEF includes zoomable and
     * scrollable root edit parts. The EditPartFactory maps model elements to
     * edit parts (controllers).
     * </p>
     * 
     * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
     */
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setEditPartFactory(new ShapesEditPartFactory());
        viewer.setRootEditPart(new ScalableFreeformRootEditPart());
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
    }

    public void commandStackChanged(EventObject event) {
        firePropertyChange(IEditorPart.PROP_DIRTY);
        super.commandStackChanged(event);
    }

    private void createOutputStream(OutputStream os) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(getModel());
        oos.close();
    }

    protected PaletteViewerProvider createPaletteViewerProvider() {
        return new PaletteViewerProvider(getEditDomain()) {

            protected void configurePaletteViewer(PaletteViewer viewer) {
                super.configurePaletteViewer(viewer);
                viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));
            }
        };
    }

    private TransferDropTargetListener createTransferDropTargetListener() {
        return new TemplateTransferDropTargetListener(getGraphicalViewer()) {

            protected CreationFactory getFactory(Object template) {
                return new SimpleFactory((Class) template);
            }
        };
    }

    public void doSave(IProgressMonitor monitor) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(diagram);
            oos.close();
            System.out.println("This is the file name before saving " + ((IFileEditorInput) getEditorInput()).getFile().getName());
            IFile file = ((IFileEditorInput) getEditorInput()).getFile();
            file.setContents(new ByteArrayInputStream(out.toByteArray()), true, false, monitor);
            getCommandStack().markSaveLocation();
        } catch (CoreException ce) {
            ce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void doSaveAs() {
        Shell shell = getSite().getWorkbenchWindow().getShell();
        SaveAsDialog dialog = new SaveAsDialog(shell);
        dialog.setOriginalFile(((IFileEditorInput) getEditorInput()).getFile());
        dialog.open();
        IPath path = dialog.getResult();
        if (path != null) {
            final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
            try {
                new ProgressMonitorDialog(shell).run(false, false, new WorkspaceModifyOperation() {

                    public void execute(final IProgressMonitor monitor) {
                        try {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            createOutputStream(out);
                            file.create(new ByteArrayInputStream(out.toByteArray()), true, monitor);
                        } catch (CoreException ce) {
                            ce.printStackTrace();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }
                });
                setInput(new FileEditorInput(file));
                getCommandStack().markSaveLocation();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            } catch (InvocationTargetException ite) {
                ite.printStackTrace();
            }
        }
    }

    public Object getAdapter(Class type) {
        if (type == IContentOutlinePage.class) return new ShapesOutlinePage(new TreeViewer());
        return super.getAdapter(type);
    }

    ShapesDiagram getModel() {
        return diagram;
    }

    protected FlyoutPreferences getPalettePreferences() {
        return ShapesEditorPaletteFactory.createPalettePreferences();
    }

    protected PaletteRoot getPaletteRoot() {
        if (PALETTE_MODEL == null) PALETTE_MODEL = ShapesEditorPaletteFactory.createPalette();
        return PALETTE_MODEL;
    }

    private void handleLoadException(Exception e) {
        System.err.println("** Load failed. Using default model. **");
        e.printStackTrace();
        diagram = new ShapesDiagram();
    }

    /**
     * Set up the editor's inital content (after creation).
     * 
     * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#initializeGraphicalViewer()
     */
    protected void initializeGraphicalViewer() {
        super.initializeGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setContents(getModel());
        viewer.addDropTargetListener(createTransferDropTargetListener());
    }

    public boolean isSaveAsAllowed() {
        return true;
    }

    protected void setInput(IEditorInput input) {
        input = DesignerContentProvider.INSTANCE.getSWAMPgraphicalEditorInput(input);
        super.setInput(input);
        try {
            IFile file = ((IFileEditorInput) input).getFile();
            ObjectInputStream in = new ObjectInputStream(file.getContents());
            diagram = (ShapesDiagram) in.readObject();
            in.close();
            setPartName(file.getName());
        } catch (Exception e) {
            System.err.println("** Load failed. Using default model. **");
            e.printStackTrace();
            diagram = new ShapesDiagram();
        }
    }

    public class ShapesOutlinePage extends ContentOutlinePage {

        /**
         * Create a new outline page for the shapes editor.
         * 
         * @param viewer
         *            a viewer (TreeViewer instance) used for this outline page
         * @throws IllegalArgumentException
         *             if editor is null
         */
        public ShapesOutlinePage(EditPartViewer viewer) {
            super(viewer);
        }

        public void createControl(Composite parent) {
            getViewer().createControl(parent);
            getViewer().setEditDomain(getEditDomain());
            getViewer().setEditPartFactory(new ShapesTreeEditPartFactory());
            ContextMenuProvider cmProvider = new ShapesEditorContextMenuProvider(getViewer(), getActionRegistry());
            getViewer().setContextMenu(cmProvider);
            getSite().registerContextMenu("org.suse.swamp.editor.shapes.contextmenu", cmProvider, getSite().getSelectionProvider());
            getSelectionSynchronizer().addViewer(getViewer());
            getViewer().setContents(getModel());
        }

        public void dispose() {
            getSelectionSynchronizer().removeViewer(getViewer());
            super.dispose();
        }

        public Control getControl() {
            return getViewer().getControl();
        }

        /**
         * @see org.eclipse.ui.part.IPageBookViewPage#init(org.eclipse.ui.part.IPageSite)
         */
        public void init(IPageSite pageSite) {
            super.init(pageSite);
            ActionRegistry registry = getActionRegistry();
            IActionBars bars = pageSite.getActionBars();
            String id = ActionFactory.UNDO.getId();
            bars.setGlobalActionHandler(id, registry.getAction(id));
            id = ActionFactory.REDO.getId();
            bars.setGlobalActionHandler(id, registry.getAction(id));
            id = ActionFactory.DELETE.getId();
            bars.setGlobalActionHandler(id, registry.getAction(id));
        }
    }
}
