package server.shortestPathBetweenness.executions;

import javolution.util.FastList;
import javolution.util.Index;
import algorithms.centralityAlgorithms.betweenness.brandes.AbsGreedyBetweeness;
import algorithms.centralityAlgorithms.betweenness.brandes.preprocessing.DataWorkshop;
import server.common.LoggingManager;
import server.common.ServerConstants.Algorithm;
import server.common.ServerConstants.Bound;
import server.execution.AbstractExecution;
import topology.EdgeInterface;

public class FindCentralVerticesExecution extends AbstractExecution {

    private FastList<Index> m_candidates = null;

    private int[] m_givenVertices = null;

    private EdgeInterface<Index>[] m_givenEdges = null;

    private DataWorkshop m_dataWorkshop = null;

    private Algorithm m_alg;

    private Bound m_boundType;

    private double m_bound = 0;

    private Object[] m_centralVertices = null;

    public FindCentralVerticesExecution(FastList<Index> candidates, int[] givenVertices, EdgeInterface<Index>[] givenEdges, DataWorkshop dataWorkshop, Algorithm alg, Bound boundType, double bound) {
        m_candidates = candidates;
        m_givenVertices = givenVertices;
        m_givenEdges = givenEdges;
        m_dataWorkshop = dataWorkshop;
        m_alg = alg;
        m_boundType = boundType;
        m_bound = bound;
    }

    @Override
    public Object[] getResult() {
        return m_centralVertices;
    }

    @Override
    public void run() {
        try {
            Index[] cv = AbsGreedyBetweeness.findVertices(m_candidates, m_givenVertices, m_givenEdges, m_dataWorkshop, m_alg, m_boundType, m_bound, this, 1);
            m_centralVertices = new Object[cv.length];
            int i = 0;
            for (Index v : cv) {
                m_centralVertices[i++] = new Integer(((Index) v).intValue());
            }
        } catch (Exception ex) {
            LoggingManager.getInstance().writeSystem(ex.getMessage(), "FindCentralVerticesExecution", "run", ex);
        }
    }
}
