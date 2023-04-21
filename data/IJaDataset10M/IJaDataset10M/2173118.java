package org.cheetahworkflow.designer.model;

import java.util.ArrayList;
import java.util.List;
import org.cheetahworkflow.designer.ui.WorkflowChartEditor;

public class Chart extends Element {

    private static final long serialVersionUID = 5285480077271516424L;

    public static final String PROP_NODE = "NODE";

    public static final String PROP_RAPID_VIEW = "RAPID_VIEW";

    private List<Node> nodes = new ArrayList<Node>();

    private StartNode startNode;

    private RapidView rapidView;

    private WorkflowChartEditor editor;

    public Chart(WorkflowChartEditor editor) {
        this.editor = editor;
    }

    public void addNode(StartNode node) {
        startNode = (StartNode) node;
        node.setChart(this);
        fireStructureChange(PROP_NODE, node);
    }

    public void addNode(Node node) {
        nodes.add(node);
        node.setChart(this);
        fireStructureChange(PROP_NODE, node);
    }

    public void addNodes(List<? extends Node> nodes) {
        for (Node node : nodes) {
            addNode(node);
        }
    }

    public void removeNode(StartNode node) {
        startNode = null;
        node.setChart(null);
        fireStructureChange(PROP_NODE, node);
    }

    public void removeNode(Node node) {
        nodes.remove(node);
        node.setChart(null);
        fireStructureChange(PROP_NODE, node);
    }

    public StartNode getStartNode() {
        return startNode;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setRapidView(RapidView rapidView) {
        rapidView.setChart(this);
        this.rapidView = rapidView;
        fireStructureChange(PROP_RAPID_VIEW, rapidView);
    }

    public void removeRapidView() {
        this.rapidView = null;
        fireStructureChange(PROP_RAPID_VIEW, null);
    }

    public RapidView getRapidView() {
        return rapidView;
    }

    public WorkflowChartEditor getWorkflowChartEditor() {
        return editor;
    }
}
