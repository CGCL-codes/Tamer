package org.fh.auge.chart.views;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.PrintAction;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.SelectAllAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.fh.auge.chart.model.Block;
import org.fh.auge.chart.model.BlockContextMenuProvider;
import org.fh.auge.chart.model.BlockEditPartFactory;
import org.fh.auge.chart.model.ChartItem;
import org.fh.auge.chart.model.ChartLine;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view shows data obtained from the model. The
 * sample creates a dummy model on the fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be presented in the view. Each view can present the
 * same model objects using different labels and icons, if needed. Alternatively, a single label provider can be shared
 * between views in order to ensure that objects of the same type are presented in the same way everywhere.
 * <p>
 */
public class CopyOfSampleView extends ViewPart {

    private GraphicalViewer viewer;

    private UndoAction undoAction;

    private RedoAction redoAction;

    private DeleteAction deleteAction;

    /**
     * The constructor.
     */
    public CopyOfSampleView() {
    }

    /**
     * This is a callback that will allow us to create the viewer and initialize it.
     */
    public void createPartControl(Composite parent) {
        viewer = new ScrollingGraphicalViewer();
        viewer.setRootEditPart(new ScalableRootEditPart());
        viewer.setEditPartFactory(new BlockEditPartFactory());
        viewer.setEditDomain(new EditDomain());
        viewer.createControl(parent);
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
        ActionRegistry actionRegistry = new ActionRegistry();
        createActions(actionRegistry);
        ContextMenuProvider cmProvider = new BlockContextMenuProvider(viewer, actionRegistry);
        viewer.setContextMenu(cmProvider);
        Block b = new Block();
        b.addChild(new ChartItem());
        viewer.setContents(b);
        System.err.println("!!! " + viewer.getContents().getChildren().get(0));
        viewer.select((EditPart) viewer.getContents().getChildren().get(0));
        PaletteRoot root = new PaletteRoot();
        PaletteGroup toolGroup = new PaletteGroup("Chart Tools");
        List toolList = new ArrayList();
        ToolEntry tool = new SelectionToolEntry();
        toolList.add(tool);
        root.setDefaultEntry(tool);
        tool = new MarqueeToolEntry();
        toolList.add(tool);
        toolGroup.addAll(toolList);
        PaletteGroup templateGroup = new PaletteGroup("Templates");
        List templateList = new ArrayList();
        CombinedTemplateCreationEntry entry = new CombinedTemplateCreationEntry("Rect", "Rect", new ChartItem(), new SimpleFactory(ChartItem.class), ImageDescriptor.getMissingImageDescriptor(), ImageDescriptor.getMissingImageDescriptor());
        CombinedTemplateCreationEntry entry1 = new CombinedTemplateCreationEntry("Line", "Line", new ChartLine(), new SimpleFactory(ChartLine.class), ImageDescriptor.getMissingImageDescriptor(), ImageDescriptor.getMissingImageDescriptor());
        templateList.add(entry);
        templateList.add(entry1);
        templateGroup.addAll(templateList);
        List rootList = new ArrayList();
        rootList.add(toolGroup);
        rootList.add(templateGroup);
        root.addAll(rootList);
        final PaletteViewer v = new PaletteViewer();
        v.setPaletteRoot(root);
        v.createControl(parent);
        v.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                System.err.println("selectionChanged " + v.getActiveTool());
            }
        });
        deleteAction.setSelectionProvider(viewer);
        viewer.getEditDomain().setPaletteViewer(v);
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                System.err.println(event.getSelection());
                deleteAction.update();
            }
        });
        viewer.getEditDomain().getCommandStack().addCommandStackEventListener(new CommandStackEventListener() {

            public void stackChanged(CommandStackEvent event) {
                undoAction.setEnabled(viewer.getEditDomain().getCommandStack().canUndo());
                redoAction.setEnabled(viewer.getEditDomain().getCommandStack().canRedo());
            }
        });
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus() {
    }

    protected void createActions(ActionRegistry actionRegistry) {
        ActionRegistry registry = actionRegistry;
        undoAction = new UndoAction(this);
        registry.registerAction(undoAction);
        redoAction = new RedoAction(this);
        registry.registerAction(redoAction);
        deleteAction = new DeleteAction(this);
        registry.registerAction(deleteAction);
        deleteAction.setEnabled(true);
        registry.registerAction(new PrintAction(this));
    }

    @Override
    public Object getAdapter(Class adapter) {
        if (adapter == CommandStack.class) {
            return viewer.getEditDomain().getCommandStack();
        }
        return super.getAdapter(adapter);
    }
}
