package de.parsemis.algorithms.gaston;

import java.util.Collection;
import de.parsemis.graph.HPGraph;
import de.parsemis.miner.environment.ThreadEnvironment;
import de.parsemis.miner.general.DataBaseGraph;
import de.parsemis.miner.general.Embedding;
import de.parsemis.miner.general.Frequency;
import de.parsemis.miner.general.HPEmbedding;

/**
 * @author Marc Woerlein (woerlein@informatik.uni-erlangen.de)
 * 
 * @param <NodeType>
 *            the type of the node labels (will be hashed and checked with
 *            .equals(..))
 * @param <EdgeType>
 *            the type of the edge labels (will be hashed and checked with
 *            .equals(..))
 */
public class LazyExtendedEmbedding_flat<NodeType, EdgeType> implements GastonEmbedding<NodeType, EdgeType> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    GastonEmbedding_flat<NodeType, EdgeType> cache = null;

    final int superNode;

    int id;

    GastonFragment<NodeType, EdgeType> frag;

    boolean needsExtension = true;

    public LazyExtendedEmbedding_flat(final GastonEmbedding_flat<NodeType, EdgeType> parent, final int superNode, final int id) {
        this.cache = parent;
        this.superNode = superNode;
        this.id = id;
    }

    public int compareTo(final GastonEmbedding<NodeType, EdgeType> arg0) {
        return get().compareTo(arg0);
    }

    public boolean freeSuperEdge(final int superEdge) {
        return get().freeSuperEdge(superEdge);
    }

    public boolean freeSuperNode(final int superNode) {
        return get().freeSuperNode(superNode);
    }

    public void freeTransient() {
    }

    public Frequency frequency() {
        return get().frequency();
    }

    public final synchronized GastonEmbedding<NodeType, EdgeType> get() {
        if (needsExtension) {
            needsExtension = false;
            int[] superNodes = cache.getSuperNodes();
            final int pid = cache.getId();
            final int dbIdx = cache.getDataBaseGraphIndex();
            final int subNode = frag.correspondingNode;
            if (subNode != HPGraph.NO_NODE) {
                if (superNodes.length <= subNode) {
                    final int[] tmp = new int[subNode + 1];
                    System.arraycopy(superNodes, 0, tmp, 0, superNodes.length);
                    for (int i = superNodes.length; i < subNode; i++) {
                        tmp[i] = HPGraph.NO_NODE;
                    }
                    superNodes = tmp;
                } else {
                    superNodes = superNodes.clone();
                }
                superNodes[subNode] = superNode;
            }
            cache = new GastonEmbedding_flat<NodeType, EdgeType>(dbIdx, superNodes, frag, id);
            id = pid;
        }
        return cache;
    }

    public DataBaseGraph<NodeType, EdgeType> getDataBaseGraph() {
        return cache.getDataBaseGraph();
    }

    public synchronized int getId() {
        return needsExtension ? id : cache.getId();
    }

    public synchronized int getParentId() {
        return needsExtension ? cache.getId() : id;
    }

    public HPGraph<NodeType, EdgeType> getSubGraph() {
        return get().getSubGraph();
    }

    public int getSubGraphEdge(final int superEdge) {
        return get().getSubGraphEdge(superEdge);
    }

    public int getSubGraphNode(final int superNode) {
        return get().getSubGraphNode(superNode);
    }

    public int getSubNode() {
        return get().getSubNode();
    }

    public HPGraph<NodeType, EdgeType> getSuperGraph() {
        return cache.getSuperGraph();
    }

    public int getSuperGraphEdge(final int subEdge) {
        return get().getSuperGraphEdge(subEdge);
    }

    public int getSuperGraphNode(final int subNode) {
        return get().getSuperGraphNode(subNode);
    }

    public int getSuperNode() {
        return superNode;
    }

    public boolean isInit() {
        return false;
    }

    public boolean overlaps(final HPEmbedding<NodeType, EdgeType> other, final Collection<NodeType> ignore) {
        return get().overlaps(other, ignore);
    }

    public void release(final ThreadEnvironment<NodeType, EdgeType> tenv) {
    }

    public void setFrag(final GastonFragment<NodeType, EdgeType> frag) {
        this.frag = frag;
    }

    public Embedding<NodeType, EdgeType> toEmbedding() {
        return get().toEmbedding();
    }
}
