package de.sonivis.tool.view.prefuse;

import de.sonivis.tool.core.eventhandling.GraphControlElement;
import de.sonivis.tool.core.metricsystem.NodeMetric;
import prefuse.Constants;
import prefuse.action.Action;
import prefuse.action.assignment.DataColorAction;
import prefuse.data.Graph;
import prefuse.util.ColorLib;

/**
 * Colors nodes according to a node metric. If the action is disabled, fill all nodes with their
 * default color.
 */
public class VisualizeNodeMetricByColorAction extends VisualizeNodeMetricAction {

    private DataColorAction nodeDataColorAction = null;

    public VisualizeNodeMetricByColorAction(GraphView graphView, NodeMetric<?> nodeMetricToVisualize) {
        super(graphView, nodeMetricToVisualize);
        try {
            nodeDataColorAction = new DataColorAction(StringIds.NODES, "", Constants.NUMERICAL, "_fillColor", ColorLib.getCoolPalette());
            nodeDataColorAction.setEnabled(false);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        nodeDataColorAction.setDataField(StringIds.PREFUSE_INTERNAL_DATA_COLOR_ACTION);
        getVisualization().putAction(StringIds.PREFUSE_INTERNAL_DATA_COLOR_ACTION, nodeDataColorAction);
        getVisualization().putAction(StringIds.NODE_METRIC_TO_COLOR, this);
    }

    public void run(double frac) {
        if (nodeMetricToVisualize != null) {
            run(StringIds.PREFUSE_INTERNAL_DATA_COLOR_ACTION, StringIds.SETUP_DEFAULT_NODE_COLOR, new String[] { StringIds.FILL_HIGHLIGHTED_NODES }, new String[] { StringIds.NODES, StringIds.NODE_SELECTION_HIGHLIGHTS });
        }
    }

    public void setNodeMetric(final NodeMetric<?> nodeMetric) {
        super.setNodeMetricToVisualize(nodeMetric);
        getVisualization().run(StringIds.NODE_METRIC_TO_COLOR);
    }

    private void setNodeDataColorScale(final GraphControlElement scale) {
        final DataColorAction action = (DataColorAction) getVisualization().getAction(StringIds.PREFUSE_INTERNAL_DATA_COLOR_ACTION);
        switch(scale) {
            case SQRT_SCALE:
                action.setScale(Constants.SQRT_SCALE);
                break;
            case LINEAR_SCALE:
                action.setScale(Constants.LINEAR_SCALE);
                break;
            case LOG_SCALE:
                action.setScale(Constants.LOG_SCALE);
                break;
            case QUANTILE_SCALE:
                action.setScale(Constants.QUANTILE_SCALE);
                break;
            default:
                break;
        }
        getVisualization().run(StringIds.NODE_METRIC_TO_COLOR);
    }

    public final void handleControlPanelEvent(final GraphControlElement element, final Object newValue) {
        switch(element) {
            case NODE_COLOR_SCALE_CHANGED:
                setNodeDataColorScale((GraphControlElement) newValue);
                break;
            case NODE_COLOR_METRIC_CHANGED:
                setNodeMetric((NodeMetric<?>) newValue);
                break;
            case ENABLE_NODE_COLOR_ACTION:
                nodeDataColorAction.setEnabled((Boolean) newValue);
                getVisualization().run(StringIds.NODE_METRIC_TO_COLOR);
                break;
            default:
                break;
        }
    }
}
