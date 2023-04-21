package de.htwg.flowchartgenerator.editor;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import de.htwg.flowchartgenerator.ast.model.FNode;
import de.htwg.flowchartgenerator.ast.model.INode;
import de.htwg.flowchartgenerator.controller.MacCabeAnalyzer;
import de.htwg.flowchartgenerator.editor.FlowChartOutlinePage.OutlineModel;
import de.htwg.flowchartgenerator.utils.NodeNormalizer;
import de.htwg.flowchartgenerator.utils.Statics;

/**
 * The Editor. Uses a GraphViewer from the zest library as control to paint the
 * graph.
 * 
 * @author Aldi Alimucaj
 * 
 */
public class FlowChartEditor extends EditorPart implements IAdaptable {

    private GraphViewer graphicalViewer;

    private EditDomain editDomain;

    private Graph g;

    private INode nodes = null;

    private FlowChartOutlinePage outlinePage;

    private OutlineModel outlineModel;

    public Graph createZestView(Composite parent) {
        IGraphBuilder graphBuilder = GraphBuilderFactory.getInstance();
        g = (Graph) graphicalViewer.getControl();
        graphBuilder.createView(g, nodes);
        outlineModel = new OutlineModel(g.getNodes().size(), g.getConnections().size());
        GraphLayoutAlgorithm gla = new GraphLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
        g.setLayoutAlgorithm(gla, true);
        int height = (g.getNodes().size() * 50 + 100 > 600) ? g.getNodes().size() * 50 + 100 : 600;
        g.setPreferredSize(600, height);
        g.addMouseListener(new GraphMouseListener());
        GraphViewContainer.setGf(graphicalViewer);
        return g;
    }

    @Override
    public Object getAdapter(Class adapter) {
        if (adapter == EditPartViewer.class) {
            return graphicalViewer;
        } else if (adapter == EditDomain.class) {
            return getEditDomain();
        } else if (adapter == IWorkbenchPage.class) {
            return getSite().getPage();
        } else if (adapter == ISelectionProvider.class) {
            return this.getSite().getSelectionProvider();
        } else if (adapter == FlowChartEditor.class) {
            return this;
        } else if (adapter == IContentOutlinePage.class) {
            if (outlinePage == null) {
                outlinePage = new FlowChartOutlinePage(outlineModel);
            }
            return outlinePage;
        }
        return super.getAdapter(adapter);
    }

    public INode getNodes() {
        return nodes;
    }

    @Override
    public void createPartControl(Composite parent) {
        graphicalViewer = getGraphicalViewer(parent);
        this.g = createZestView(parent);
    }

    public EditDomain getEditDomain() {
        if (editDomain == null) {
            editDomain = new DefaultEditDomain(this);
        }
        return editDomain;
    }

    public GraphViewer getGraphicalViewer(Composite parent) {
        if (graphicalViewer == null) {
            graphicalViewer = new GraphViewer(parent, ZestStyles.NONE);
        }
        return graphicalViewer;
    }

    public GraphViewer getGraphicalViewer() {
        if (graphicalViewer != null) {
            return graphicalViewer;
        }
        throw new NullPointerException();
    }

    /**
	 * Sets the input from the persisted file to the editor. It retrieves an
	 * object of type INode and initiates further actions.
	 */
    @Override
    protected void setInput(IEditorInput input) {
        super.setInput(input);
        if (null != input) {
            if (input instanceof FileEditorInput) {
                FileEditorInput f = ((FileEditorInput) input);
                try {
                    FileInputStream fis = new FileInputStream(f.getURI().getPath());
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    INode myDeserializedObject = (INode) ois.readObject();
                    ois.close();
                    nodes = NodeNormalizer.normalize(myDeserializedObject);
                } catch (Exception e) {
                    e.printStackTrace();
                    nodes = new FNode(e.getClass().toString(), ASTNode.EXPRESSION_STATEMENT);
                }
            } else {
                FileStoreEditorInput f = ((FileStoreEditorInput) input);
                try {
                    FileInputStream fis = new FileInputStream(f.getURI().getPath());
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    INode myDeserializedObject = (INode) ois.readObject();
                    ois.close();
                    nodes = NodeNormalizer.normalize(myDeserializedObject);
                } catch (Exception e) {
                    e.printStackTrace();
                    nodes = new FNode();
                }
            }
        } else {
            System.err.println("Input is null");
        }
    }

    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setSite(site);
        setInput(input);
    }

    @Override
    public void setFocus() {
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }
}
