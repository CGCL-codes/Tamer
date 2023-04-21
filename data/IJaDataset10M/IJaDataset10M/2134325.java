package visreed.model;

import higraph.model.abstractTaggedClasses.AbstractTaggedEdge;

/**
 * RegexEdges are not used in this project.
 * @author Xiaoyu Guo
 */
public class VisreedEdge extends AbstractTaggedEdge<VisreedTag, VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> {

    protected VisreedEdge(VisreedNode source, VisreedNode target, VisreedEdgeLabel label, VisreedWholeGraph higraph) {
        super(source, target, label, higraph);
    }

    @Override
    protected VisreedEdge getThis() {
        return this;
    }
}
