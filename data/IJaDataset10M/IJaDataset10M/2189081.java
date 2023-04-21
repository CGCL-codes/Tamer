package oclac.view.ui.components;

import org.w3c.dom.Document;
import oclac.view.ui.MainWindow;
import oclac.view.ui.components.graph.PortCell;
import oclac.view.ui.components.graph.TaskCell;
import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;

/**
 * This class is a graph component that displays a pipeline.
 */
public class PipelineGraph extends mxGraph {

    /**
	 * Constructor of the class.
	 * @param model The model to use for the graph.
	 */
    public PipelineGraph(mxIGraphModel model) {
        super(model);
        setAllowDanglingEdges(false);
        setGridEnabled(true);
        getSelectionModel().setSingleSelection(false);
        getSelectionModel().addListener(mxEvent.CHANGE, new mxIEventListener() {

            public void invoke(Object sender, mxEventObject evt) {
                if (PipelineGraph.this.getSelectionCount() == 1 && (PipelineGraph.this.getSelectionCell() instanceof TaskCell)) {
                    TaskCell cell = (TaskCell) PipelineGraph.this.getSelectionCell();
                    MainWindow.instance.getPreferenceBrowser().setTask(cell.getTask());
                } else MainWindow.instance.getPreferenceBrowser().setTask(null);
            }
        });
        mxCodec codec = new mxCodec();
        Document doc = mxUtils.loadDocument(PipelineGraph.class.getResource("/oclac/res/graphstyle.xml").toString());
        codec.decode(doc.getDocumentElement(), getStylesheet());
    }

    /**
	 * Returns whether two nodes can be connected.
	 * Only ports can be connected and only if one of them is an in port
	 * and the other is an out port.
	 * @return True if the nodes can be connected. Else false.
	 */
    public boolean isValidConnection(Object source, Object target) {
        if (source == target) return false;
        if (source == null) return false;
        if (target == null) return true;
        if (!(source instanceof PortCell) || !(target instanceof PortCell)) return false;
        PortCell sourcePort = (PortCell) source;
        PortCell targetPort = (PortCell) target;
        if (sourcePort.getEdgeCount() > 1 || targetPort.getEdgeCount() > 0) return false;
        if (sourcePort.getPort().getDirection() == targetPort.getPort().getDirection()) return false;
        if (sourcePort.getPort().getParent() == targetPort.getPort().getParent()) return false;
        if (sourcePort.getPort().getType() != targetPort.getPort().getType()) return false;
        return true;
    }

    /**
	 * Returns whether a cell are editable.
	 * @return Always false. Editing is disabled.
	 */
    public boolean isCellEditable(Object cell) {
        return false;
    }

    /**
	 * Returns whether cells are foldable.
	 * @return Always false. Folding is disabled.
	 */
    public boolean isCellFoldable(Object cell, boolean collapse) {
        return false;
    }

    /**
	 * Returns whether a cell is selectable.
	 * @return False if the cell is a PortCell. Else true.
	 */
    public boolean isCellSelectable(Object cell) {
        if (cell instanceof PortCell) return false;
        return true;
    }

    /**
	 * Returns whether a cell is a valid drop target for an other cell.
	 * @return False. Dropping cells into other ones is disabled.
	 */
    public boolean isValidDropTarget(Object cell, Object[] cells) {
        return false;
    }
}
