package com.bbn.vessel.author.flowEditor.views;

import com.bbn.vessel.author.graphEditor.views.GraphViews;
import com.bbn.vessel.author.graphEditor.views.View;
import com.bbn.vessel.author.graphEditor.views.ViewFactory;
import com.bbn.vessel.author.models.GraphElement;
import com.bbn.vessel.author.models.GraphNode;
import com.bbn.vessel.author.models.NodeSpec;

/**
 * <Enter the description of this type here>
 *
 * @author RTomlinson
 */
public class FlowViewFactory implements ViewFactory {

    private final ViewFactory parent;

    /**
     * @param parent the ViewFactory parent
     */
    public FlowViewFactory(ViewFactory parent) {
        this.parent = parent;
    }

    /**
     * @see com.bbn.vessel.author.graphEditor.views.ViewFactory#createView(com.bbn.vessel.author.models.GraphElement, com.bbn.vessel.author.graphEditor.views.GraphViews)
     */
    @Override
    public View createView(GraphElement graphElement, GraphViews graphViews) {
        if (graphElement instanceof GraphNode) {
            GraphNode vesselNode = (GraphNode) graphElement;
            NodeSpec nodeSpec = vesselNode.getNodeSpec();
            String name = nodeSpec == null ? "" : nodeSpec.getType();
            if (name.equals("Situation")) {
                return new SituationView(vesselNode, graphViews);
            }
            if (name.equals("Fork") || name.equals("Join")) {
                return new ForkView(vesselNode, graphViews);
            }
            if (name.equals("Branch") || name.equals("Merge")) {
                return new BranchView(vesselNode, graphViews);
            }
            if (name.equals("Start")) {
                return new StartView(vesselNode, graphViews);
            }
            if (name.equals("Stop")) {
                return new StopView(vesselNode, graphViews);
            }
            if (name.equals("StartStop")) {
                return new StartStopView(vesselNode, graphViews);
            }
            if (name.equals("End")) {
                return new EndView(vesselNode, graphViews);
            }
        }
        return parent.createView(graphElement, graphViews);
    }
}
