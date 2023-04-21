package net.sf.magicmap.core.model.graph.impl;

import net.sf.magicmap.client.model.location.jung.JungNodePlacer;
import net.sf.magicmap.client.model.location.INodePlacer;
import net.sf.magicmap.client.model.node.IMagicEdge;
import net.sf.magicmap.client.model.node.INode;
import net.sf.magicmap.core.model.graph.INodeGraph;
import net.sf.magicmap.core.model.node.InterceptableNodeModel;
import java.util.Set;

/**
 * <p>
 * Class BaseNodeGraph ZUSAMMENFASSUNG
 * </p>
 * <p>
 * DETAILS
 * </p>
 *
 * @author Jan Friderici
 *         Date: 01.01.2008
 *         Time: 15:36:32
 */
public class BaseNodeGraph implements INodeGraph {

    private final JungNodePlacer jungNodePlacer;

    private final InterceptableNodeModel nodeModel;

    /**
     *
     * @param jungNodePlacer
     * @param nodeModel
     */
    public BaseNodeGraph(JungNodePlacer jungNodePlacer, InterceptableNodeModel nodeModel) {
        this.jungNodePlacer = jungNodePlacer;
        this.nodeModel = nodeModel;
    }

    /**
     * 
     * @param source
     * @param target
     * @return
     */
    public IMagicEdge getEdge(INode source, INode target) {
        return null;
    }

    /**
     *
     * @param node
     * @return
     */
    public Set<IMagicEdge> getEdges(INode node) {
        return null;
    }

    /**
     *
     * @param node
     * @return
     */
    public Set<IMagicEdge> getOutEdges(INode node) {
        return null;
    }

    /**
     *
     * @param node
     * @return
     */
    public Set<IMagicEdge> getInEdges(INode node) {
        return null;
    }

    /**
     *
     * @return
     */
    public Iterable<IMagicEdge> getEdges() {
        return null;
    }

    /**
     *
     * @return
     */
    public JungNodePlacer getNodePlacer() {
        return jungNodePlacer;
    }
}
