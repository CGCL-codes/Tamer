package preprocessing.visual.presentationtier.view;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxUndoableEdit.mxUndoableChange;
import com.mxgraph.view.mxGraph;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.TreeMap;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import preprocessing.storage.SimplePreprocessingStorage;
import preprocessing.visual.businesstier.IO.Component;
import preprocessing.visual.presentationtier.controller.StorageController;
import preprocessing.visual.presentationtier.controller.Validator;
import preprocessing.visual.presentationtier.view.BoxInputComponents;
import preprocessing.visual.presentationtier.view.BoxOutputComponents;
import preprocessing.visual.presentationtier.view.ComponentDialog;

/**
 *
 * @author Milos Kovalcik. 
 * Class that fills main window
 */
public class WorkSpace extends JPanel {

    Component component;

    private Validator validator;

    public mxUndoManager undoManager;

    public TreeMap<String, Component> ComponentsMap = new TreeMap<String, Component>();

    public mxGraph graph;

    public Object parent;

    public mxGraphComponent graphComponent;

    public mxIGraphModel model;

    public JTextArea stateArea;

    protected mxIEventListener undoHandler = new mxIEventListener() {

        public void invoke(Object source, mxEventObject evt) {
            undoManager.undoableEditHappened((mxUndoableEdit) evt.getProperty("edit"));
        }
    };

    /**
 * Adds left toolbar, boxes of components, status line and graph work space. Creates and sets jgraph.
 */
    public WorkSpace() {
        graph = new mxGraph();
        parent = graph.getDefaultParent();
        graphComponent = new mxGraphComponent(graph);
        undoManager = new mxUndoManager();
        this.setLayout(new BorderLayout());
        JPanel tabsPanel = new JPanel();
        tabsPanel.setPreferredSize(new Dimension(200, 70));
        JTabbedPane tabbedPane = new JTabbedPane();
        tabsPanel.setLayout(new BorderLayout());
        tabsPanel.add(tabbedPane, BorderLayout.NORTH);
        this.add(tabsPanel, BorderLayout.NORTH);
        BoxInputComponents boxInputComponents = new BoxInputComponents(BoxLayout.X_AXIS);
        boxInputComponents.setName("Load data");
        tabbedPane.addTab(boxInputComponents.getName(), boxInputComponents);
        BoxCopyComponents boxCopyComponents = new BoxCopyComponents(BoxLayout.X_AXIS);
        boxCopyComponents.setName("Copy");
        tabbedPane.addTab(boxCopyComponents.getName(), boxCopyComponents);
        BoxNormalizationComponents boxNormalizationComponents = new BoxNormalizationComponents(BoxLayout.X_AXIS);
        boxNormalizationComponents.setName("Normalization");
        tabbedPane.addTab(boxNormalizationComponents.getName(), boxNormalizationComponents);
        BoxImportComponents boxImportComponents = new BoxImportComponents(BoxLayout.X_AXIS);
        boxImportComponents.setName("Import");
        tabbedPane.addTab(boxImportComponents.getName(), boxImportComponents);
        BoxOutlierDetectionComponents boxOutlierDetectionComponents = new BoxOutlierDetectionComponents(BoxLayout.X_AXIS);
        boxOutlierDetectionComponents.setName("Outlier Detection");
        tabbedPane.addTab(boxOutlierDetectionComponents.getName(), boxOutlierDetectionComponents);
        BoxDataReductionComponents boxDataReductionComponents = new BoxDataReductionComponents(BoxLayout.X_AXIS);
        boxDataReductionComponents.setName("Data Reduction");
        tabbedPane.addTab(boxDataReductionComponents.getName(), boxDataReductionComponents);
        BoxOutputComponents boxOutputComponents = new BoxOutputComponents(BoxLayout.X_AXIS);
        boxOutputComponents.setName("Save data");
        tabbedPane.addTab(boxOutputComponents.getName(), boxOutputComponents);
        graphComponent.setConnectable(true);
        graphComponent.setToolTips(true);
        graph.setMultigraph(false);
        graph.setAllowDanglingEdges(false);
        graphComponent.setConnectable(true);
        graphComponent.setToolTips(true);
        graphComponent.getGraph().setCellsEditable(false);
        graph.getModel().addListener(mxEvent.UNDO, undoHandler);
        graph.getView().addListener(mxEvent.UNDO, undoHandler);
        mxIEventListener undoHandler = new mxIEventListener() {

            public void invoke(Object source, mxEventObject evt) {
                List<mxUndoableChange> changes = ((mxUndoableEdit) evt.getProperty("edit")).getChanges();
                graph.setSelectionCells(graph.getSelectionCellsForChanges(changes));
            }
        };
        undoManager.addListener(mxEvent.UNDO, undoHandler);
        undoManager.addListener(mxEvent.REDO, undoHandler);
        new mxRubberband(graphComponent);
        new mxKeyboardHandler(graphComponent);
        graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent e) {
                mxCell cell = (mxCell) graphComponent.getCellAt(e.getX(), e.getY());
                if (cell != null) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        try {
                            if ((ComponentsMap.get(cell.getId())).getPanel() != null) {
                                new ComponentDialog(e.getX(), e.getY(), (ComponentsMap.get(cell.getId())).getPanel());
                            }
                        } catch (java.lang.NullPointerException nullEx) {
                            System.out.println("Right-clicked graph object does not have panel.");
                        }
                    }
                }
            }
        });
        graph.getModel().addListener(mxEvent.CHANGE, new mxIEventListener() {

            public void invoke(Object sender, mxEventObject evt) {
                validator = new Validator(graph, ComponentsMap, stateArea);
                boolean b = validator.validuj();
                validator.restCells.clear();
            }
        });
        this.add(graphComponent, BorderLayout.CENTER);
        stateArea = new JTextArea(5, 20);
        stateArea.setEditable(false);
        stateArea.scrollRectToVisible(null);
        this.add(stateArea, BorderLayout.SOUTH);
    }
}
