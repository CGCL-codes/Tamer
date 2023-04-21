package server.shortestPathBetweenness.executions;

import algorithms.centralityAlgorithms.AbsTrafficMatrix;
import algorithms.centralityAlgorithms.DefaultTrafficMatrix;
import algorithms.centralityAlgorithms.DenseTrafficMatrix;
import algorithms.centralityAlgorithms.betweenness.brandes.TrafficMatrixBC;
import algorithms.shortestPath.ShortestPathAlgorithmInterface;
import algorithms.shortestPath.ShortestPathAlgorithmInterface.ShortestPathAlg;
import javolution.util.Index;
import server.common.DataBase;
import server.common.LoggingManager;
import server.execution.AbstractExecution;
import topology.GraphInterface;

public class BrandesBCExecution extends AbstractExecution {

    private ShortestPathAlg m_shortestPathAlgorithm = ShortestPathAlgorithmInterface.DEFAULT;

    private GraphInterface<Index> m_graph = null;

    private int m_netID = -1;

    private AbsTrafficMatrix m_communicationWeights = null;

    private int m_brandesID = -1;

    /** CONSTANTS */
    private static final String NEW_LINE = "\n";

    public BrandesBCExecution(ShortestPathAlg spAlg, int netID, String communicationWeights) {
        m_shortestPathAlgorithm = spAlg;
        m_netID = netID;
        m_graph = DataBase.getNetwork(netID).getGraph();
        if (communicationWeights != null && !communicationWeights.isEmpty()) m_communicationWeights = new DenseTrafficMatrix(communicationWeights, m_graph.getNumberOfVertices()); else m_communicationWeights = new DefaultTrafficMatrix(m_graph.getNumberOfVertices());
    }

    public BrandesBCExecution(ShortestPathAlg spAlg, int netID, int tmID) {
        m_shortestPathAlgorithm = spAlg;
        m_netID = netID;
        m_graph = DataBase.getNetwork(netID).getGraph();
        if (tmID != -1) m_communicationWeights = DataBase.getTrafficMatrix(tmID); else m_communicationWeights = new DefaultTrafficMatrix(m_graph.getNumberOfVertices());
    }

    @Override
    public void run() {
        reportSuccess(AbstractExecution.PHASE_FAILURE, AbstractExecution.PHASE_NONCOMPLETE);
        boolean success = true;
        try {
            double totalPercentage = 1;
            TrafficMatrixBC brandes = null;
            if (m_graph != null) {
                double progress = getProgress();
                progress += 0.05 * totalPercentage;
                setProgress(progress);
                try {
                    brandes = new TrafficMatrixBC(m_shortestPathAlgorithm, m_graph, m_communicationWeights, this, 0.3 * 0.85 * totalPercentage);
                    brandes.run();
                } catch (Exception ex) {
                    LoggingManager.getInstance().writeSystem("An exception has occured while creating BrandesBC:\n" + ex.getMessage() + NEW_LINE + ex.getStackTrace(), "BrandesBCExecution", "run", ex);
                    success = false;
                    reportSuccess(AbstractExecution.PHASE_FAILURE, AbstractExecution.PHASE_COMPLETE);
                }
                progress += 0.2 * totalPercentage;
                reportSuccess(AbstractExecution.PHASE_SUCCESS, AbstractExecution.PHASE_COMPLETE);
                m_brandesID = DataBase.putAlgorithm(brandes);
                DataBase.putNetworkOfAlgorithm(m_brandesID, m_netID);
                success = true;
            } else {
                LoggingManager.getInstance().writeSystem("The graph has not been loaded properly. Check logs please.", "BrandesBCExecution", "run", null);
                reportSuccess(AbstractExecution.PHASE_FAILURE, AbstractExecution.PHASE_COMPLETE);
                success = false;
            }
        } catch (RuntimeException ex) {
            LoggingManager.getInstance().writeSystem("A RuntimeException has occured during creating PureUlrik.", "BrandesBCExecution", "run", ex);
            reportSuccess(AbstractExecution.PHASE_FAILURE, AbstractExecution.PHASE_COMPLETE);
            success = false;
        }
        LoggingManager.getInstance().writeTrace("Finishing creating PureUlrik.", "PureUlrikExecution", "run", null);
        if (!success) {
            LoggingManager.getInstance().writeSystem(m_methodName + " hasn't completed successfully, check logs.", "BrandesBCExecution", "run", null);
        }
    }

    @Override
    public Object getResult() {
        return m_brandesID;
    }
}
