package server.degree;

import java.util.Iterator;
import common.IndexEdge;
import algorithms.centralityAlgorithms.degree.AbsGreedyDegree;
import algorithms.centralityAlgorithms.degree.DegreeAlgorithm;
import javolution.util.FastList;
import javolution.util.Index;
import server.common.DataBase;
import server.common.DummyProgress;
import server.common.LoggingManager;
import server.common.ServerConstants;
import server.common.ServerConstants.Algorithm;
import server.common.ServerConstants.Bound;
import server.degree.executions.DegreeAlgorithmExecution;
import server.degree.executions.FindCentralEdgesExecution;
import server.degree.executions.FindCentralVerticesExecution;
import server.execution.AbstractExecution;
import topology.EdgeInterface;
import topology.GraphInterface;

public class DegreeController {

    public static final String ALIAS = "Degree";

    /** Creates Degree algorithm with the given network.
	 * @param network index
	 * @return Index of the algorithm in the Database */
    public int create(int netID) {
        GraphInterface<Index> graph = DataBase.getNetwork(netID).getGraph();
        DegreeAlgorithm degree = null;
        if (graph != null) {
            int[] vertices = new int[graph.getNumberOfVertices()];
            int i = 0;
            Iterator<Index> vs = graph.getVertices();
            while (vs.hasNext()) {
                vertices[i++] = vs.next().intValue();
            }
            try {
                degree = new DegreeAlgorithm(vertices, graph, new DummyProgress(), 1);
            } catch (Exception ex) {
                LoggingManager.getInstance().writeSystem("An exception has occured while creating DegreeAlgorithm:\n" + ex.getMessage() + "\n" + ex.getStackTrace(), DegreeController.ALIAS, "create", ex);
            }
        }
        int algID = DataBase.putAlgorithm(degree);
        DataBase.putNetworkOfAlgorithm(algID, netID);
        return algID;
    }

    /** Starts an execution that creates Degree algorithm with the given network.
	 * @param network index
	 * @return Execution index in the Database */
    public int createAsynch(int netID) {
        LoggingManager.getInstance().writeTrace("Starting creating DegreeAlgorithm.", DegreeController.ALIAS, "createAsynch", null);
        AbstractExecution exe = new DegreeAlgorithmExecution(netID);
        int exeID = DataBase.putExecution(exe);
        exe.setID(exeID);
        Thread t = new Thread(exe);
        t.start();
        LoggingManager.getInstance().writeTrace(ServerConstants.RETURNING_TO_CLIENT, DegreeController.ALIAS, "createAsynch", null);
        return exeID;
    }

    /** Returns the degree of the given vertex in the given Degree algorithm instance.
	 * @param algorithm index
	 * @param vertex
	 * @return degree */
    public double getDegree(int algID, int v) {
        return ((DegreeAlgorithm) DataBase.getAlgorithm(algID)).getDegree(v);
    }

    /** Returns an array of degrees of the given vertices in the given Degree algorithm instance.
	 * The order of the degree values in the array corresponds to the order of the given vertices.
	 * @param algorithm index
	 * @param array of vertices
	 * @return array of degree values */
    public Object[] getDegree(int algID, int[] vertices) {
        Object[] degreeValues = new Object[vertices.length];
        DegreeAlgorithm dAlg = (DegreeAlgorithm) DataBase.getAlgorithm(algID);
        for (int i = 0; i < vertices.length; i++) degreeValues[i] = dAlg.getDegree(vertices[i]);
        return degreeValues;
    }

    /** Returns an array of degree values of all vertices in the given Degree algorithm instance.
	 * The order of the degree values in the array corresponds to the order of the vertices in the graph.
	 * @param algorithm index
	 * @return array of degree values */
    public Object[] getDegree(int algID) {
        DegreeAlgorithm dAlg = (DegreeAlgorithm) DataBase.getAlgorithm(algID);
        Iterator<Index> d = dAlg.getDegrees().values().iterator();
        Object[] dVals = new Object[dAlg.getDegrees().size()];
        int i = 0;
        while (d.hasNext()) dVals[i] = new Integer(d.next().intValue());
        return dVals;
    }

    /** Returns the sum of degree values of the given vertices in the given Degree instance.
	 * @param algorithm index
	 * @param array of vertices
	 * @return degree value */
    public int getSumGroup(int algID, Object[] vertices, Object[] edges) {
        DegreeAlgorithm degreeAlgorithm = (DegreeAlgorithm) DataBase.getAlgorithm(algID);
        int gDegree = 0;
        try {
            for (int i = 0; i < vertices.length; i++) {
                gDegree += degreeAlgorithm.getDegree(((Integer) vertices[i]).intValue());
            }
            for (int v = 0; v < edges.length; v = v + 2) {
                gDegree += 2;
            }
        } catch (RuntimeException ex) {
            LoggingManager.getInstance().writeSystem("A RuntimeException has occured during getSumGroup.", DegreeController.ALIAS, "getSumGroup", ex);
        }
        LoggingManager.getInstance().writeTrace("Finishing getSumGroup.", DegreeController.ALIAS, "getSumGroup", null);
        return gDegree;
    }

    /** Searches for deployment of vertices (using TopK algorithm) according to given parameters.
     * @param Degree algorithm index
     * @param k is the size of the desired deployment
     * @param vertex candidates for the deployment (can be an empty list)
     * @param givenVertices are the already deployed vertices (can be an empty list)
     * @param givenLinks are the already deployed links (can be an empty list)
     * @return array of vertices */
    public Object[] getCentralVertices(int algID, int k, Object[] candidatesObj, Object[] givenVerticesObj, Object[] givenEdgesObj) {
        DegreeAlgorithm degreeAlgorithm = (DegreeAlgorithm) DataBase.getAlgorithm(algID);
        GraphInterface<Index> graph = DataBase.getNetwork(DataBase.getNetworkOfAlgorithm(algID)).getGraph();
        FastList<Index> candidates = new FastList<Index>();
        if (candidatesObj != null) {
            for (int v = 0; v < candidatesObj.length; v++) candidates.add(Index.valueOf(((Integer) candidatesObj[v]).intValue()));
        }
        int[] givenVertices = new int[givenVerticesObj.length];
        for (int i = 0; i < givenVertices.length; i++) givenVertices[i] = ((Integer) givenVerticesObj[i]).intValue();
        EdgeInterface<Index>[] givenEdges = new IndexEdge[givenEdgesObj.length / 2];
        for (int v = 0, i = 0; v < givenEdgesObj.length; v = v + 2, i++) {
            EdgeInterface<Index> e = graph.getEdge(Index.valueOf(((Integer) givenEdgesObj[v]).intValue()), Index.valueOf(((Integer) givenEdgesObj[v + 1]).intValue()));
            givenEdges[i] = e;
        }
        Object[] centralVertices = null;
        try {
            Index[] cv = AbsGreedyDegree.findVertices(degreeAlgorithm, Algorithm.TopK, candidates, givenVertices, givenEdges, Bound.GroupSize, k, new DummyProgress(), 1);
            centralVertices = new Object[cv.length];
            int i = 0;
            for (Index v : cv) {
                centralVertices[i++] = new Integer(((Index) v).intValue());
            }
        } catch (Exception ex) {
            LoggingManager.getInstance().writeSystem(ex.getMessage(), DegreeController.ALIAS, "getCentralVertices", ex);
        }
        return centralVertices;
    }

    /** Searches for deployment of edges (using TopK algorithm) according to given parameters.
     * @param Degree algorithm index
     * @param k is the size of the desired deployment
     * @param edge candidates for the deployment (can be an empty list)
     * @param givenVertices are the already deployed vertices (can be an empty list)
     * @param givenLinks are the already deployed links (can be an empty list)
     * @return array of edges
     * In the candidates array and in the result array every two successive elements represent an edge.
     * Namely, A[0]-A[1], A[2]-A[3], A[4]-A[5] represent 3 edges. */
    public Object[] getCentralEdges(int algID, int k, Object[] candidatesObj, Object[] givenVerticesObj, Object[] givenEdgesObj) {
        DegreeAlgorithm degreeAlgorithm = (DegreeAlgorithm) DataBase.getAlgorithm(algID);
        GraphInterface<Index> graph = DataBase.getNetwork(DataBase.getNetworkOfAlgorithm(algID)).getGraph();
        FastList<EdgeInterface<Index>> candidates = new FastList<EdgeInterface<Index>>();
        if (candidatesObj != null) {
            for (int v = 0; v < candidatesObj.length; v = v + 2) {
                EdgeInterface<Index> e = graph.getEdge(Index.valueOf(((Integer) candidatesObj[v]).intValue()), Index.valueOf(((Integer) candidatesObj[v + 1]).intValue()));
                candidates.add(e);
            }
        }
        int[] givenVertices = new int[givenVerticesObj.length];
        for (int i = 0; i < givenVertices.length; i++) givenVertices[i] = ((Integer) givenVerticesObj[i]).intValue();
        EdgeInterface<Index>[] givenEdges = new IndexEdge[givenEdgesObj.length / 2];
        for (int v = 0, i = 0; v < givenEdgesObj.length; v = v + 2, i++) {
            EdgeInterface<Index> e = graph.getEdge(Index.valueOf(((Integer) givenEdgesObj[v]).intValue()), Index.valueOf(((Integer) givenEdgesObj[v + 1]).intValue()));
            givenEdges[i] = e;
        }
        Object[] centralEdges = null;
        try {
            EdgeInterface<Index>[] ce = AbsGreedyDegree.findEdges(degreeAlgorithm, Algorithm.TopK, candidates, givenVertices, givenEdges, Bound.GroupSize, k, new DummyProgress(), 1);
            centralEdges = new Object[ce.length];
            int i = 0;
            for (EdgeInterface<Index> e : ce) {
                centralEdges[i++] = new Integer(((Index) e.getV0()).intValue());
                centralEdges[i++] = new Integer(((Index) e.getV1()).intValue());
            }
        } catch (Exception ex) {
            LoggingManager.getInstance().writeSystem(ex.getMessage(), DegreeController.ALIAS, "getCentralEdges", ex);
        }
        return centralEdges;
    }

    /** Starts an execution which searches for deployment of vertices (using TopK algorithm) according to given parameters.
     * @param Degree algorithm index
     * @param k is the size of the desired deployment
     * @param candidates for the deployment (can be an empty list)
     * @param givenVertices are the already deployed vertices (can be an empty list)
     * @param givenLinks are the already deployed links (can be an empty list)
     * @return execution index */
    public int getCentralVerticesAsynch(int algID, int k, Object[] candidatesObj, Object[] givenVerticesObj, Object[] givenEdgesObj) {
        LoggingManager.getInstance().writeTrace("Starting searching central vertices.", DegreeController.ALIAS, "getCentralVerticesAsynch", null);
        DegreeAlgorithm degreeAlgorithm = (DegreeAlgorithm) DataBase.getAlgorithm(algID);
        GraphInterface<Index> graph = DataBase.getNetwork(DataBase.getNetworkOfAlgorithm(algID)).getGraph();
        FastList<Index> candidates = new FastList<Index>();
        if (candidatesObj != null) {
            for (int v = 0; v < candidatesObj.length; v++) candidates.add(Index.valueOf(((Integer) candidatesObj[v]).intValue()));
        }
        int[] givenVertices = new int[givenVerticesObj.length];
        for (int i = 0; i < givenVertices.length; i++) givenVertices[i] = ((Integer) givenVerticesObj[i]).intValue();
        EdgeInterface<Index>[] givenEdges = new IndexEdge[givenEdgesObj.length / 2];
        for (int v = 0, i = 0; v < givenEdgesObj.length; v = v + 2, i++) {
            EdgeInterface<Index> e = graph.getEdge(Index.valueOf(((Integer) givenEdgesObj[v]).intValue()), Index.valueOf(((Integer) givenEdgesObj[v + 1]).intValue()));
            givenEdges[i] = e;
        }
        AbstractExecution exe = new FindCentralVerticesExecution(degreeAlgorithm, Algorithm.TopK, candidates, givenVertices, givenEdges, Bound.GroupSize, k);
        int exeID = DataBase.putExecution(exe);
        exe.setID(exeID);
        Thread t = new Thread(exe);
        t.start();
        LoggingManager.getInstance().writeTrace(ServerConstants.RETURNING_TO_CLIENT, DegreeController.ALIAS, "getCentralVerticesAsynch", null);
        return exeID;
    }

    /** Starts an execution which searches for deployment of edges (using TopK algorithm) according to given parameters.
     * @param Degree algorithm index
     * @param k is the size of the desired deployment
     * @param edge candidates for the deployment (can be an empty list)
     * @param givenVertices are the already deployed vertices (can be an empty list)
     * @param givenLinks are the already deployed links (can be an empty list)
     * @return execution index
     * In the candidates array and in the result array every two successive elements represent an edge.
     * Namely, A[0]-A[1], A[2]-A[3], A[4]-A[5] represent 3 edges. */
    public int getCentralEdgesAsynch(int algID, int k, Object[] candidatesObj, Object[] givenVerticesObj, Object[] givenEdgesObj) {
        LoggingManager.getInstance().writeTrace("Starting searching central edges.", DegreeController.ALIAS, "getCentralEdgesAsynch", null);
        DegreeAlgorithm degreeAlgorithm = (DegreeAlgorithm) DataBase.getAlgorithm(algID);
        GraphInterface<Index> graph = DataBase.getNetwork(DataBase.getNetworkOfAlgorithm(algID)).getGraph();
        FastList<EdgeInterface<Index>> candidates = new FastList<EdgeInterface<Index>>();
        if (candidatesObj != null) {
            for (int v = 0; v < candidatesObj.length; v = v + 2) {
                EdgeInterface<Index> e = graph.getEdge(Index.valueOf(((Integer) candidatesObj[v]).intValue()), Index.valueOf(((Integer) candidatesObj[v + 1]).intValue()));
                candidates.add(e);
            }
        }
        int[] givenVertices = new int[givenVerticesObj.length];
        for (int i = 0; i < givenVertices.length; i++) givenVertices[i] = ((Integer) givenVerticesObj[i]).intValue();
        EdgeInterface<Index>[] givenEdges = new IndexEdge[givenEdgesObj.length / 2];
        for (int v = 0, i = 0; v < givenEdgesObj.length; v = v + 2, i++) {
            EdgeInterface<Index> e = graph.getEdge(Index.valueOf(((Integer) givenEdgesObj[v]).intValue()), Index.valueOf(((Integer) givenEdgesObj[v + 1]).intValue()));
            givenEdges[i] = e;
        }
        AbstractExecution exe = new FindCentralEdgesExecution(degreeAlgorithm, Algorithm.TopK, candidates, givenVertices, givenEdges, Bound.GroupSize, k);
        int exeID = DataBase.putExecution(exe);
        exe.setID(exeID);
        Thread t = new Thread(exe);
        t.start();
        LoggingManager.getInstance().writeTrace(ServerConstants.RETURNING_TO_CLIENT, DegreeController.ALIAS, "getCentralEdgesAsynch", null);
        return exeID;
    }

    /** Removes the given Degree algorithm from the Database maps.
	 * @param algorithm index
	 * @return 0 */
    public int destroy(int algID) {
        DataBase.releaseAlgorithm(algID);
        return 0;
    }
}
