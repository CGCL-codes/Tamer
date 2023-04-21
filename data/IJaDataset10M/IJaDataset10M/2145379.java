package view;

import com.mxgraph.model.mxCell;
import com.mxgraph.io.mxCodec;
import com.mxgraph.swing.examples.editor.BasicGraphEditor;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Connector;
import model.Element;
import model.Iteration;
import model.type.ElementType;
import operating.FramesConfiguration;
import operating.Manager;
import operating.Strings;
import org.w3c.dom.Document;
import util.Imagem;
import view.jscroll.widgets.JScrollInternalFrame;
import view.mxGraph.SpmMxGraphComponent;
import view.mxGraph.SpmBasicGraphEditor;

/**
 *
 * @author  William Correa
 */
public class DiagramaView extends JScrollInternalFrame {

    private static final long serialVersionUID = 1L;

    private Iteration iteration;

    private SpmMxGraphComponent mxSpmGraph;

    private SpmBasicGraphEditor spmGraphEditor;

    private mxGraph mx;

    private mxGraphOutline graphOutline;

    private mxUndoManager undoManager;

    protected boolean ignore;

    private boolean modificado;

    public double zoom;

    /**
     * 
     * @param pIteration
     * @param novo
     */
    public DiagramaView(Iteration pIteration, boolean novo) {
        iteration = pIteration.clone();
        initComponents();
        setFrameIcon(new Imagem().getFrameIcon());
        setTitle(pIteration.getName() + " (" + pIteration.getId() + ")");
        setFramesConfiguration(Manager.getFramesConfiguration(getTitle()));
        setId(iteration.getId());
        zoom = getFramesConfiguration().getLastZoom();
        undoManager = new mxUndoManager();
        mx = new mxGraph();
        mx.getSelectionModel();
        mx.setAllowDanglingEdges(false);
        mx.addListener(mxEvent.REMOVE_CELLS, removeCell);
        mx.addListener(mxEvent.CELLS_TOGGLED, removeCell);
        mx.getSelectionModel().addListener("", undoHandler);
        mx.getModel().addListener(mxEvent.UNDO, undoHandler);
        mx.getView().addListener(mxEvent.UNDO, undoHandler);
        mxIEventListener selectionHandler = new mxIEventListener() {

            public void invoke(Object source, mxEventObject evt) {
                List changes = ((mxUndoableEdit) evt.getArgAt(0)).getChanges();
                mx.setSelectionCells(mx.getSelectionCellsForChanges(changes));
            }
        };
        mx.addListener(mxEvent.SELECT, selectionHandler);
        undoManager.addListener(mxEvent.UNDO, selectionHandler);
        undoManager.addListener(mxEvent.REDO, selectionHandler);
        mxSpmGraph = new SpmMxGraphComponent(mx);
        mxSpmGraph.getConnectionHandler().setCreateTarget(false);
        mxSpmGraph.setWheelScrollingEnabled(true);
        mxSpmGraph.setCenterZoom(true);
        mxSpmGraph.getGraphControl().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
        mx.setSwimlaneNesting(true);
        spmGraphEditor = new SpmBasicGraphEditor(Strings.appName, mxSpmGraph);
        this.jpnGraph.add(mxSpmGraph, BorderLayout.CENTER);
        graphOutline = new mxGraphOutline(spmGraphEditor.getGraphComponent());
        try {
            Manager.addNavigator(graphOutline, pIteration.getId());
        } catch (PropertyVetoException ex) {
            Logger.getLogger(DiagramaView.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!novo) {
            if (!pIteration.getXmlGraph().isEmpty()) {
                ignore = true;
                Document document = null;
                document = mxUtils.parse(pIteration.getXmlGraph());
                mxCodec codec = new mxCodec(document);
                codec.decode(document.getDocumentElement(), mx.getModel());
                ignore = false;
            }
        }
        mxSpmGraph.zoom(zoom);
        mxSpmGraph.setZoomPolicy(getFramesConfiguration().getLastZoomPolicy());
        mxSpmGraph.getViewport().setViewPosition(getFramesConfiguration().getLastView());
    }

    /**
     * 
     * @return
     */
    public Iteration getIteration() {
        return iteration;
    }

    /**
     * 
     * @param iteration
     */
    public void setIteration(Iteration iteration) {
        this.iteration = iteration;
    }

    /**
     * 
     * @return
     */
    public SpmMxGraphComponent getMxSpmGraph() {
        return mxSpmGraph;
    }

    /**
     * 
     * @param mxSpmGraph
     */
    public void setMxSpmGraph(SpmMxGraphComponent mxSpmGraph) {
        this.mxSpmGraph = mxSpmGraph;
    }

    /**
     * 
     * @return
     */
    public BasicGraphEditor getSpmGraphEditor() {
        return spmGraphEditor;
    }

    /**
     * 
     * @param basicGraphEditor
     */
    public void setSpmGraphEditor(SpmBasicGraphEditor basicGraphEditor) {
        this.spmGraphEditor = basicGraphEditor;
    }

    /**
     * 
     * @return
     */
    public mxGraph getMx() {
        return mx;
    }

    /**
     * 
     * @param mx
     */
    public void setMx(mxGraph mx) {
        this.mx = mx;
    }

    /**
     * 
     * @param modificado
     */
    private void setModificado(boolean pModificado) {
        String tittle = "";
        if (pModificado) {
            tittle = "* - " + iteration.getName() + " (" + iteration.getId() + ")";
        } else {
            tittle = iteration.getName() + " (" + iteration.getId() + ")";
        }
        setTitle(tittle);
        if (isVisible()) {
            getAssociatedButton().setText(tittle);
        }
        if (pModificado) {
            Manager.setModified(pModificado);
        }
        modificado = pModificado;
    }

    /**
     * 
     * @return
     */
    @Override
    public boolean salvar() {
        mxCodec codec = new mxCodec();
        String xml = mxUtils.getXml(codec.encode(mx.getModel()));
        iteration.setXmlGraph(xml);
        Manager.saveIteration(iteration, iteration.getPhaseID());
        setModificado(false);
        return true;
    }

    /**
     * 
     */
    @Override
    public boolean fechar() {
        boolean podeFechar = false;
        if (!modificado) {
            Manager.removeNavigator(iteration.getId());
            Manager.removeFrame(iteration.getId());
            podeFechar = true;
        } else {
            Object[] options = { "Sim", "Não", "Cancelar" };
            int option = 0;
            option = JOptionPane.showOptionDialog(null, "Deseja salvar as alterações em " + iteration.getName() + "?", Strings.appName, JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
            if (option == 0) {
                salvar();
                Manager.reloadProjectTree();
                podeFechar = true;
            } else if (option == 1) {
                podeFechar = true;
            } else {
                podeFechar = false;
            }
        }
        updateFramesConfiguration();
        if (podeFechar) {
            Manager.removeNavigator(iteration.getId());
            Manager.removeFrame(iteration.getId());
            dispose();
        }
        return podeFechar;
    }

    /**
     * 
     */
    private void updateFramesConfiguration() {
        FramesConfiguration configFrame = new FramesConfiguration();
        configFrame.setTitle(iteration.getName() + " (" + iteration.getId() + ")");
        configFrame.setBounds(getBounds());
        configFrame.setLastView(mxSpmGraph.getViewport().getViewPosition());
        configFrame.setLastZoom(zoom);
        configFrame.setLastZoomPolicy(mxSpmGraph.getZoomPolicy());
        configFrame.setMaximum(isMaximum());
        setFramesConfiguration(configFrame);
        Manager.updateFramesConfiguration(configFrame);
    }

    /**
     * 
     * @param element
     */
    public void adicionarElement(Element element) {
        if (element.getType().equals(ElementType.Connector)) {
            iteration.addConector((Connector) element.clone());
        } else {
            iteration.addElement(element.clone());
        }
        setModificado(true);
    }

    /**
     * 
     * @param elementID
     * @return
     */
    public boolean removeElementFromIteration(String elementID) {
        boolean removed = false;
        removed = iteration.removeElement(elementID);
        iteration.removeConnector(elementID);
        setModificado(true);
        return removed;
    }

    /**
     * 
     * @param elementID
     * @return
     */
    public boolean removeElementFromGraph(String elementID) {
        boolean removed = false;
        mx.selectAll();
        Object[] cells = mx.getSelectionCells();
        for (int i = 0; i < cells.length; i++) {
            if (((mxCell) cells[i]).getId().equals(elementID)) {
                Object[] lls = { cells[i] };
                mx.removeCells(lls);
                removed = true;
                break;
            }
        }
        mx.removeSelectionCells(cells);
        return removed;
    }

    /**
     * 
     * @param element
     * @return
     */
    public boolean updateElementInIteration(Element element) {
        boolean updated = false;
        updated = iteration.updateElement(element);
        setModificado(true);
        return updated;
    }

    /**
     * 
     * @param element
     * @return
     */
    public boolean updateElementInGraph(Element element) {
        boolean updated = false;
        String elementID = element.getId();
        mx.selectAll();
        Object[] cells = mx.getSelectionCells();
        for (int i = 0; i < cells.length; i++) {
            if (((mxCell) cells[i]).getId().equals(elementID)) {
                ((mxCell) cells[i]).setValue(element.getName());
                updated = true;
                break;
            }
            if (((mxCell) cells[i]).getChildCount() > 0) {
                for (int j = 0; j < ((mxCell) cells[i]).getChildCount(); j++) {
                    if (((mxCell) cells[i]).getChildAt(j).getId().equals(elementID)) {
                        ((mxCell) cells[i]).getChildAt(j).setValue(element.getName());
                    }
                }
            }
        }
        mx.removeSelectionCells(cells);
        if (element.getType().equals(ElementType.ProcessRole)) {
            updated = true;
        }
        return updated;
    }

    /**
     * 
     * @param newZoom 
     */
    public void setZoom(double newZoom) {
        setModificado(true);
        zoom = newZoom;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jpFundo = new javax.swing.JPanel();
        jpnGraph = new javax.swing.JPanel();
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(operating.ModelingApp.class).getContext().getResourceMap(DiagramaView.class);
        setTitle(resourceMap.getString("Form.title"));
        setMinimumSize(new java.awt.Dimension(550, 235));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {

            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameActivated(evt);
            }

            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }

            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }

            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }

            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }

            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }

            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {

            public void componentMoved(java.awt.event.ComponentEvent evt) {
                formComponentMoved(evt);
            }

            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        jpnGraph.setLayout(new java.awt.BorderLayout());
        javax.swing.GroupLayout jpFundoLayout = new javax.swing.GroupLayout(jpFundo);
        jpFundo.setLayout(jpFundoLayout);
        jpFundoLayout.setHorizontalGroup(jpFundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jpnGraph, javax.swing.GroupLayout.DEFAULT_SIZE, 738, Short.MAX_VALUE));
        jpFundoLayout.setVerticalGroup(jpFundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jpnGraph, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jpFundo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jpFundo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        setBounds(10, 10, 748, 460);
    }

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
        Manager.setBasicGraphEditor(spmGraphEditor);
        Manager.selectNavigator(iteration.getId());
    }

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
        fechar();
    }

    private void formComponentResized(java.awt.event.ComponentEvent evt) {
        setModificado(true);
        updateFramesConfiguration();
    }

    private void formComponentMoved(java.awt.event.ComponentEvent evt) {
        setModificado(true);
        updateFramesConfiguration();
    }

    private javax.swing.JPanel jpFundo;

    private javax.swing.JPanel jpnGraph;

    /**
     * 
     */
    protected mxIEventListener changeTracker = new mxIEventListener() {

        public void invoke(Object source, mxEventObject evt) {
            if (!ignore) {
                setModificado(true);
            }
        }
    };

    /**
     * 
     */
    protected mxIEventListener undoHandler = new mxIEventListener() {

        public void invoke(Object source, mxEventObject evt) {
            undoManager.undoableEditHappened((mxUndoableEdit) evt.getArgAt(0));
        }
    };

    /**
     * 
     */
    protected mxIEventListener removeCell = new mxIEventListener() {

        public void invoke(Object source, mxEventObject evt) {
        }
    };
}
