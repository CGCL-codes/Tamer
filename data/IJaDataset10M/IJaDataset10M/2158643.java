package edu.uci.ics.jung.graph.impl;

import java.util.Set;
import edu.uci.ics.jung.graph.ArchetypeEdge;
import edu.uci.ics.jung.graph.ArchetypeGraph;
import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.graph.Hyperedge;
import edu.uci.ics.jung.graph.Hypervertex;

/**
 * A HyperEdge has zero or more HyperVertices attached to it;
 * this implements that as part of an underlying HyperGraph. 
 * 
 * @author danyelf
 */
public class HyperedgeBPG extends AbstractHyperUnitBPG implements Hyperedge {

    public HyperedgeBPG() {
    }

    HyperedgeBPG(BipartiteVertex bpv, HypergraphBPG hypergraphBPG) {
        super(bpv, hypergraphBPG);
    }

    /**
	 * @see edu.uci.ics.jung.graph.ArchetypeEdge#getIncidentVertices()
	 */
    public Set getIncidentVertices() {
        return graph.translateUnderlyingVertices(vertex.getNeighbors());
    }

    /**
	 * @see edu.uci.ics.jung.graph.ArchetypeEdge#getEqualEdge(edu.uci.ics.jung.graph.ArchetypeGraph)
	 */
    public ArchetypeEdge getEqualEdge(ArchetypeGraph g) {
        HypergraphBPG bpg = (HypergraphBPG) g;
        return bpg.getEdgeCorrespondingTo(underlying_vertex());
    }

    /**
     * @deprecated As of version 1.4, renamed to getEqualEdge(g).
     */
    public ArchetypeEdge getEquivalentEdge(ArchetypeGraph g) {
        return getEqualEdge(g);
    }

    /**
	 * @see edu.uci.ics.jung.graph.ArchetypeEdge#numVertices()
	 */
    public int numVertices() {
        return vertex.degree();
    }

    /**
	 * @see edu.uci.ics.jung.graph.ArchetypeEdge#isIncident(edu.uci.ics.jung.graph.ArchetypeVertex)
	 */
    public boolean isIncident(ArchetypeVertex v) {
        HypervertexBPG hv = (HypervertexBPG) v;
        return vertex.isNeighborOf(hv.underlying_vertex());
    }

    /**
	 * @see edu.uci.ics.jung.graph.ArchetypeEdge#copy(edu.uci.ics.jung.graph.ArchetypeGraph)
	 */
    public ArchetypeEdge copy(ArchetypeGraph g) {
        HypergraphBPG hg = (HypergraphBPG) g;
        HyperedgeBPG he = new HyperedgeBPG();
        hg.addEdge(he);
        he.importUserData(this);
        return he;
    }

    /**
	 * Registers an additional vertex <code>hv3_x</code> onto this Edge.
	 */
    public void addVertex(Hypervertex hv3_x) {
        HypervertexBPG hv3 = (HypervertexBPG) hv3_x;
        BipartiteGraph bpg = (BipartiteGraph) hv3.underlying_vertex().getGraph();
        BipartiteVertex v1 = hv3.underlying_vertex();
        BipartiteVertex v2 = underlying_vertex();
        bpg.addBipartiteEdge(new BipartiteEdge(v1, v2));
    }
}
