package sc.model;

import higraph.model.abstractClasses.AbstractEdge;

public class EdgeSC extends AbstractEdge<NodePayloadSC, EdgePayloadSC, WholeGraphSC, SubGraphSC, NodeSC, EdgeSC> {

    protected EdgeSC(NodeSC source, NodeSC target, EdgePayloadSC label, WholeGraphSC higraph) {
        super(source, target, label, higraph);
    }

    @Override
    protected EdgeSC getThis() {
        return this;
    }
}
