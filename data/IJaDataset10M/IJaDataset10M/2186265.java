package de.sonivis.tool.ontology.view.prefuse.actions.ontology;

import prefuse.action.Action;
import prefuse.data.Graph;
import de.sonivis.tool.core.datamodel.Node;
import de.sonivis.tool.core.metricsystem.AbstractMetric;
import de.sonivis.tool.ontology.view.prefuse.PrefuseTableID;
import de.sonivis.tool.ontology.view.prefuse.SonivisPrefuseMapping;

public class SetNodeMetricLabelAction extends Action {

    private Graph currentPrefuseGraph;

    private AbstractMetric<?, Node> nodeMetric;

    private SonivisPrefuseMapping mapping;

    public SetNodeMetricLabelAction(Graph prefuseGraph, AbstractMetric<?, Node> edgeMetric, SonivisPrefuseMapping mapping) {
        super();
        this.nodeMetric = edgeMetric;
        this.currentPrefuseGraph = prefuseGraph;
        this.nodeMetric = edgeMetric;
        this.mapping = mapping;
    }

    @Override
    public void run(double frac) {
        int col = currentPrefuseGraph.getNodeTable().getColumnNumber(PrefuseTableID.NODE_METRIC);
        for (final Node sonivisNode : this.mapping.getSonivisNodesMapping().keySet()) {
            int prefuseNodeId = mapping.getPrefuseNodeFromSonivisNode(sonivisNode);
            String metricValue = nodeMetric.getValueAsStringFor(sonivisNode);
            currentPrefuseGraph.getNodeTable().setString(prefuseNodeId, col, metricValue);
        }
    }
}
