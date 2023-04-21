package org.henkels.drawcode.editors.classdiagram;

import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.actions.ActionFactory;
import org.henkels.drawcode.editors.classdiagram.graphics.visitor.CDLayoutCalculatorVisitor;
import org.henkels.drawcode.editors.classdiagram.model.ClassDiagramHandler;
import org.henkels.drawcode.editors.classdiagram.model.nodes.ICDElementVisitable;
import org.henkels.drawcode.editors.classdiagram.parts.CDPartFactory;
import org.henkels.drawcode.plugin.DrawCodePlugin;

public class ClassDiagramEditor extends GraphicalEditorWithFlyoutPalette {

    IFile file;

    private static PaletteRoot palette;

    private static String[] extensions;

    private ClassDiagramHandler csDiagram = new ClassDiagramHandler();

    public ClassDiagramEditor() {
        super();
        extensions = new String[1];
        extensions[0] = "*.nsd";
        System.out.println("NSDiagramEditor.NSDiagramEditor");
        setEditDomain(new DefaultEditDomain(this));
        getCommandStack().setUndoLimit(-1);
    }

    public void commandStackChanged(EventObject event) {
        System.out.println("NSDiagramEditor.commandStackChanged");
        firePropertyChange(IEditorPart.PROP_DIRTY);
        super.commandStackChanged(event);
    }

    protected FlyoutPreferences getPalettePreferences() {
        System.out.println("NSDiagramEditor.getPalettePreferences");
        return ClassDiagramEditorPaletteFactory.createPalettePreferences();
    }

    @Override
    protected PaletteRoot getPaletteRoot() {
        System.out.println("NSDiagramEditor.getPaletteRoot");
        if (palette == null) {
            palette = ClassDiagramEditorPaletteFactory.createPalette();
        }
        return palette;
    }

    protected void initializeGraphicalViewer() {
        super.initializeGraphicalViewer();
        System.out.println("NSDiagramEditor.getPaletteRoot");
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setContents(DrawCodePlugin.getDefault().getActiveClassDiagram().root);
        viewer.addDropTargetListener(createTransferDropTargetListener());
    }

    private org.eclipse.jface.util.TransferDropTargetListener createTransferDropTargetListener() {
        return new TemplateTransferDropTargetListener(getGraphicalViewer()) {

            protected CreationFactory getFactory(Object template) {
                return new SimpleFactory((Class) template);
            }
        };
    }

    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        System.out.println("NSDiagramEditor.configureGraphicalViewer");
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setEditPartFactory(new CDPartFactory(this));
        viewer.setRootEditPart(new ScalableFreeformRootEditPart());
        GraphicalViewerKeyHandler keyHandler = new GraphicalViewerKeyHandler(viewer);
        keyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0), getActionRegistry().getAction(ActionFactory.DELETE.getId()));
        viewer.setKeyHandler(keyHandler);
        ContextMenuProvider cmProvider = new ClassDiagramEditorContextMenuProvider(viewer, getActionRegistry());
        viewer.setContextMenu(cmProvider);
        getSite().registerContextMenu(cmProvider, viewer);
    }

    protected PaletteViewerProvider createPaletteViewerProvider() {
        System.out.println("NSDiagramEditor.createPaletteViewerProvider");
        return new PaletteViewerProvider(getEditDomain()) {

            protected void configurePaletteViewer(PaletteViewer viewer) {
                super.configurePaletteViewer(viewer);
                viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));
            }
        };
    }

    public void setInput(IEditorInput input) {
        super.setInput(input);
        System.out.println("NSDiagramEditor.setInput");
        try {
            file = ((IFileEditorInput) input).getFile();
            DrawCodePlugin.getDefault().addOpenedClassDiagram(csDiagram);
            DrawCodePlugin.getDefault().setActiveClassDiagram(csDiagram);
            DrawCodePlugin.getDefault().setActiveClassEditor(this);
            csDiagram.ReadDiagram(file.getRawLocation().toString());
            setPartName(file.getName());
        } catch (Exception e) {
            Shell shell = getEditorSite().getShell();
            System.out.println("Error opening file.");
        }
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        System.out.println("NSDiagramEditor.doSave");
        try {
            csDiagram.SaveDiagram(file.getRawLocation().toString());
            getCommandStack().markSaveLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HashMap<ICDElementVisitable, Rectangle> rectmap = null;

    public Rectangle getNodeRect(ICDElementVisitable node) {
        if (rectmap == null) {
            rectmap = new CDLayoutCalculatorVisitor().getLayout(getClassDiagramHandler().root);
        }
        Rectangle ret = rectmap.get(node);
        if (ret == null) {
            rectmap = new CDLayoutCalculatorVisitor().getLayout(getClassDiagramHandler().root);
            ret = rectmap.get(node);
        }
        return ret;
    }

    public ClassDiagramHandler getClassDiagramHandler() {
        return csDiagram;
    }

    public List<ICDElementVisitable> getChildrens(ICDElementVisitable node) {
        System.out.println("NSDiagramEditort.getChildrens");
        return csDiagram.getChildrens(node);
    }

    public void RefreshVisual() {
        rectmap = null;
        csDiagram.rooteditpart.removeChildrens();
        csDiagram.rooteditpart.refreshChildren();
    }

    public String performOpenFile() {
        getGraphicalViewer().getControl().setEnabled(false);
        FileDialog fd = new FileDialog(getSite().getWorkbenchWindow().getShell());
        fd.setFilterExtensions(extensions);
        String res = fd.open();
        fd = null;
        getGraphicalViewer().getControl().setEnabled(true);
        return res;
    }
}
