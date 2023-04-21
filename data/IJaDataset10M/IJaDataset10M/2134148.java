package uima.taes.interestingness.graphHelpers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import uima.taes.interestingness.LFUCache;
import uima.types.EntityPair;
import Jama.Matrix;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JFSIndexRepository;
import org.apache.uima.jcas.JCas;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.EdgeWeightLabeller;
import edu.uci.ics.jung.graph.impl.SimpleUndirectedSparseVertex;
import edu.uci.ics.jung.graph.impl.UndirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.UndirectedSparseGraph;
import edu.uci.ics.jung.utils.UserDataContainer;

public class EntityNetworkHelper {

    protected UndirectedSparseGraph graph = new UndirectedSparseGraph();

    protected HashMap vertices = new HashMap();

    protected static UserDataContainer.CopyAction.Clone cloner = new UserDataContainer.CopyAction.Clone();

    protected EdgeWeightLabeller labeller = EdgeWeightLabeller.getLabeller(graph);

    protected static String MATCHES = "matches";

    protected static String INT_MATCHES = "intMatches";

    protected boolean destroyRelevancyFeature;

    protected HashMap distances = new HashMap();

    protected int refreshRate;

    protected static String NAME = "name";

    protected HashSet newEdges[];

    protected Matrix katzScores;

    protected HashMap katzMatrixIndexes;

    protected static String BETA = "beta";

    protected double beta = 0.005;

    protected HashMap relationships = new HashMap();

    protected HashSet uninterestingRelationships = new HashSet();

    protected int topK;

    protected Matrix[] adjacencyMatrix;

    protected boolean needAdjacency;

    protected LFUCache cache;

    protected LFUCache vertexCache;

    protected double minSupport = 0;

    protected double minConfidence = 0;

    protected int numIntDocs = 0;

    public EntityNetworkHelper(int refreshRate, int order, int topK, boolean needAdjacency, int maxCacheSize, int maxGraphSize, double minSupport, double minConfidence) {
        this.refreshRate = refreshRate;
        newEdges = new HashSet[refreshRate];
        for (int i = 0; i < newEdges.length; i++) newEdges[i] = new HashSet();
        katzMatrixIndexes = new HashMap();
        adjacencyMatrix = new Matrix[order];
        this.topK = topK;
        this.needAdjacency = needAdjacency;
        this.cache = new LFUCache(maxCacheSize);
        this.vertexCache = new LFUCache(maxGraphSize);
        this.minSupport = minSupport;
        this.minConfidence = minConfidence;
    }

    protected class Pair implements Comparable {

        protected String entity1, entity2;

        protected int numMatches = 0;

        public Pair(String entity1, String entity2) {
            this.entity1 = entity1;
            this.entity2 = entity2;
        }

        public int compareTo(Object arg0) {
            return numMatches - ((Pair) arg0).numMatches;
        }

        public String getEntity1() {
            return entity1;
        }

        public void setEntity1(String entity1) {
            this.entity1 = entity1;
        }

        public String getEntity2() {
            return entity2;
        }

        public void setEntity2(String entity2) {
            this.entity2 = entity2;
        }

        protected void incrementNumMatch() {
            numMatches++;
        }
    }

    ;

    public void addUninterestingArticle(JCas jcas) {
        JFSIndexRepository indexes = jcas.getJFSIndexRepository();
        FSIndex termIndex = indexes.getAnnotationIndex(EntityPair.type);
        for (Iterator itr1 = termIndex.iterator(); itr1.hasNext(); ) {
            EntityPair ep = (EntityPair) itr1.next();
            String relationshipString = ep.getEntity1() + "->" + ep.getEntity2();
            Edge edge = null;
            if ((edge = (Edge) relationships.get(relationshipString)) != null) {
                Double weight = (Double) edge.getUserDatum(MATCHES);
                edge.setUserDatum(MATCHES, new Double(weight.doubleValue() + 1.0), cloner);
            }
        }
    }

    public double getNumPathsOfLength(String entity1, String entity2, int length) {
        Vertex v1 = (Vertex) vertices.get(entity1);
        Vertex v2 = (Vertex) vertices.get(entity2);
        if (v1 == null || v2 == null) return 0;
        Integer index1 = (Integer) katzMatrixIndexes.get(v1);
        Integer index2 = (Integer) katzMatrixIndexes.get(v2);
        if (index1 == null || index2 == null) return 0;
        return adjacencyMatrix[length - 1].get(index1.intValue(), index2.intValue());
    }

    public void updateRelationships(JCas jcas, double beta, String graphFilter) {
        numIntDocs++;
        boolean result1 = true;
        if (numIntDocs > refreshRate) result1 = removeOldEdges();
        boolean result2 = addNewEdges(jcas, graphFilter);
        if (result1 && result2 && needAdjacency) computeKatzScores(beta);
    }

    protected void computeAdjacencyMatrix() {
        adjacencyMatrix[0] = new Matrix(graph.getVertices().size(), graph.getVertices().size());
        katzMatrixIndexes.clear();
        int i = 0;
        for (Iterator itr = graph.getVertices().iterator(); itr.hasNext(); i++) {
            katzMatrixIndexes.put(itr.next(), new Integer(i));
        }
        for (Iterator itr = graph.getVertices().iterator(); itr.hasNext(); ) {
            Vertex v1 = (Vertex) itr.next();
            Integer index1 = (Integer) katzMatrixIndexes.get(v1);
            for (Iterator itr2 = v1.getIncidentEdges().iterator(); itr2.hasNext(); ) {
                Edge edge = (Edge) itr2.next();
                Vertex v2 = edge.getOpposite(v1);
                Integer index2 = (Integer) katzMatrixIndexes.get(v2);
                Double weight = (Double) edge.getUserDatum(INT_MATCHES);
                adjacencyMatrix[0].set(index1.intValue(), index2.intValue(), weight.doubleValue());
            }
        }
        for (i = 1; i < adjacencyMatrix.length; i++) {
            adjacencyMatrix[i] = adjacencyMatrix[i - 1].times(adjacencyMatrix[0]);
        }
    }

    protected void computeKatzScores(double beta) {
        computeAdjacencyMatrix();
        katzScores = new Matrix(graph.getVertices().size(), graph.getVertices().size());
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            katzScores = katzScores.plus(adjacencyMatrix[i].times(Math.pow(beta, i + 1)));
        }
    }

    protected boolean removeOldEdges() {
        boolean result = false;
        HashSet edgesToRemove = new HashSet();
        for (Iterator itr = newEdges[numIntDocs % refreshRate].iterator(); itr.hasNext(); ) {
            Edge edge = (Edge) itr.next();
            double support = computeSupport(edge, numIntDocs);
            if (support < minSupport) {
                edgesToRemove.add(edge);
                result = true;
            }
        }
        removeEdges(edgesToRemove);
        newEdges[numIntDocs % refreshRate].clear();
        edgesToRemove.clear();
        for (Iterator itr = relationships.keySet().iterator(); itr.hasNext(); ) {
            String relationshipString = (String) itr.next();
            Edge edge = (Edge) relationships.get(relationshipString);
            double confidence = computeConfidence(edge);
            if (confidence < minConfidence) edgesToRemove.add(edge);
        }
        return result;
    }

    protected static double computeConfidence(Edge e) {
        double intMatch = ((Double) e.getUserDatum(INT_MATCHES)).doubleValue();
        double match = ((Double) e.getUserDatum(MATCHES)).doubleValue();
        return intMatch / match;
    }

    protected static double computeSupport(Edge e, double numInt) {
        double intMatch = ((Double) e.getUserDatum(INT_MATCHES)).doubleValue();
        return intMatch / numInt;
    }

    protected void removeEdges(HashSet edgesToRemove) {
        for (Iterator itr = edgesToRemove.iterator(); itr.hasNext(); ) {
            Edge edge = (Edge) itr.next();
            if (edge == null) continue;
            Iterator verticesItr = edge.getIncidentVertices().iterator();
            Vertex v1 = (Vertex) verticesItr.next();
            Vertex v2 = (Vertex) verticesItr.next();
            String v1Name = (String) v1.getUserDatum(NAME);
            String v2Name = (String) v2.getUserDatum(NAME);
            String relationship = null;
            if (String.CASE_INSENSITIVE_ORDER.compare(v1Name, v2Name) < 0) relationship = v1Name + "->" + v2Name; else relationship = v2Name = "->" + v1Name;
            if (edge.getGraph() == null) continue;
            graph.removeEdge(edge);
            relationships.remove(relationship);
            if (v1.getIncidentEdges().isEmpty()) {
                graph.removeVertex(v1);
                vertices.remove(v1Name);
                vertexCache.remove(v1);
            }
            if (v2.getIncidentEdges().isEmpty()) {
                graph.removeVertex(v2);
                vertices.remove(v2Name);
                vertexCache.remove(v2);
            }
        }
    }

    protected boolean addNewEdges(JCas jcas, String graphFilter) {
        boolean result = false;
        JFSIndexRepository indexes = jcas.getJFSIndexRepository();
        FSIndex termIndex = indexes.getAnnotationIndex(EntityPair.type);
        EdgeWeightLabeller.getLabeller(graph);
        HashMap counts = new HashMap();
        for (Iterator itr1 = termIndex.iterator(); itr1.hasNext(); ) {
            EntityPair tf1 = (EntityPair) itr1.next();
            if (!tf1.getSentenceType().equals(graphFilter)) continue;
            String relationship = tf1.getEntity1() + "->" + tf1.getEntity2();
            if (uninterestingRelationships.contains(relationship)) {
                cache.use(relationship);
                continue;
            }
            Pair count = null;
            if ((count = (Pair) counts.get(relationship)) == null) count = new Pair(tf1.getEntity1(), tf1.getEntity2());
            count.incrementNumMatch();
            counts.put(relationship, count);
        }
        HashSet candidatesForRemoval = new HashSet();
        for (Iterator itr = counts.keySet().iterator(); itr.hasNext(); ) {
            String relationship = (String) itr.next();
            Pair pair = (Pair) counts.get(relationship);
            if (vertices.containsKey(pair.getEntity1().toLowerCase())) {
                SimpleUndirectedSparseVertex v1 = (SimpleUndirectedSparseVertex) vertices.get(pair.getEntity1().toLowerCase());
                vertexCache.use(v1);
            }
            if (vertices.containsKey(pair.getEntity2().toLowerCase())) {
                SimpleUndirectedSparseVertex v2 = (SimpleUndirectedSparseVertex) vertices.get(pair.getEntity2().toLowerCase());
                vertexCache.use(v2);
            }
        }
        for (Iterator itr = counts.keySet().iterator(); itr.hasNext(); ) {
            String relationship = (String) itr.next();
            Pair pair = (Pair) counts.get(relationship);
            SimpleUndirectedSparseVertex v1, v2;
            if (!vertices.containsKey(pair.getEntity1().toLowerCase())) {
                v1 = new SimpleUndirectedSparseVertex();
                v1.setUserDatum(NAME, pair.getEntity1().toLowerCase(), cloner);
                vertices.put(pair.getEntity1().toLowerCase(), v1);
                graph.addVertex(v1);
                Object removalCandidate = vertexCache.add(v1);
                if (removalCandidate != null) candidatesForRemoval.add(removalCandidate);
                result = true;
            } else {
                v1 = (SimpleUndirectedSparseVertex) vertices.get(pair.getEntity1().toLowerCase());
            }
            if (!vertices.containsKey(pair.getEntity2().toLowerCase())) {
                v2 = new SimpleUndirectedSparseVertex();
                v2.setUserDatum(NAME, pair.getEntity2().toLowerCase(), cloner);
                vertices.put(pair.getEntity2().toLowerCase(), v2);
                graph.addVertex(v2);
                Object removalCandidate = vertexCache.add(v2);
                if (removalCandidate != null) candidatesForRemoval.add(removalCandidate);
                result = true;
            } else {
                v2 = (SimpleUndirectedSparseVertex) vertices.get(pair.getEntity2().toLowerCase());
            }
            UndirectedSparseEdge edge = null;
            if ((edge = (UndirectedSparseEdge) relationships.get(relationship)) == null) {
                edge = new UndirectedSparseEdge(v1, v2);
                graph.addEdge(edge);
                edge.setUserDatum(INT_MATCHES, new Double(1.0), cloner);
                edge.setUserDatum(MATCHES, new Double(1.0), cloner);
                labeller.setWeight(edge, 1);
                relationships.put(relationship, edge);
                newEdges[numIntDocs % refreshRate].add(edge);
                result = true;
            } else {
                Double weight = (Double) edge.getUserDatum(INT_MATCHES);
                labeller.setWeight(edge, weight.intValue() + 1);
                edge.setUserDatum(INT_MATCHES, new Double(weight.doubleValue() + 1.0), cloner);
                weight = (Double) edge.getUserDatum(MATCHES);
                edge.setUserDatum(MATCHES, new Double(weight.doubleValue() + 1.0), cloner);
                result = true;
            }
        }
        for (Iterator itr = candidatesForRemoval.iterator(); itr.hasNext(); ) {
            SimpleUndirectedSparseVertex v = (SimpleUndirectedSparseVertex) itr.next();
            String v1Name = (String) v.getUserDatum(NAME);
            vertices.remove(v1Name);
            Set neighbors = v.getNeighbors();
            for (Iterator itr2 = neighbors.iterator(); itr2.hasNext(); ) {
                SimpleUndirectedSparseVertex neighbor = (SimpleUndirectedSparseVertex) itr2.next();
                String v2Name = (String) neighbor.getUserDatum(NAME);
                String relationship = null;
                if (String.CASE_INSENSITIVE_ORDER.compare(v1Name, v2Name) < 0) relationship = v1Name + "->" + v2Name; else relationship = v2Name + "->" + v1Name;
                relationships.remove(relationship);
            }
            if (v.getGraph() != null) graph.removeVertex(v);
            for (Iterator itr2 = neighbors.iterator(); itr2.hasNext(); ) {
                SimpleUndirectedSparseVertex neighbor = (SimpleUndirectedSparseVertex) itr2.next();
                if (neighbor.getIncidentEdges().size() == 0) {
                    String name = (String) neighbor.getUserDatum(NAME);
                    vertices.remove(name);
                    graph.removeVertex(neighbor);
                }
            }
        }
        return result;
    }

    public double getKatzScores(String entity1, String entity2) {
        Vertex v1 = (Vertex) vertices.get(entity1);
        Vertex v2 = (Vertex) vertices.get(entity2);
        if (v1 == null || v2 == null) return 0;
        Integer index1 = (Integer) katzMatrixIndexes.get(v1);
        Integer index2 = (Integer) katzMatrixIndexes.get(v2);
        if (index1 == null || index2 == null) return 0;
        return katzScores.get(index1.intValue(), index2.intValue());
    }

    public double getBothNodeExistence(String entity1, String entity2) {
        Vertex v1 = (Vertex) vertices.get(entity1);
        Vertex v2 = (Vertex) vertices.get(entity2);
        if (v1 == null || v2 == null) return 0;
        return 1;
    }

    public double getNoNodeExistence(String entity1, String entity2) {
        Vertex v1 = (Vertex) vertices.get(entity1);
        Vertex v2 = (Vertex) vertices.get(entity2);
        if (v1 == null && v2 == null) return 1;
        return 0;
    }

    public double getOnlyOneNodeExistence(String entity1, String entity2) {
        Vertex v1 = (Vertex) vertices.get(entity1);
        Vertex v2 = (Vertex) vertices.get(entity2);
        if (v1 == null && v2 == null) return 0;
        if (v1 != null && v2 != null) return 0;
        return 1;
    }

    public double getDirectlyConnected(String entity1, String entity2) {
        String relationshipString = entity1 + "->" + entity2;
        Edge edge = (Edge) relationships.get(relationshipString);
        if (edge == null) return 0; else return 1;
    }

    public double getIndirectlyConnected(String entity1, String entity2) {
        String relationshipString = entity1 + "->" + entity2;
        Edge edge = (Edge) relationships.get(relationshipString);
        if (edge == null && getKatzScores(entity1, entity2) > 0) {
            return 1;
        } else return 0;
    }

    public double getNewVertices(Collection entities) {
        int count = 0;
        for (Iterator itr = entities.iterator(); itr.hasNext(); ) {
            String entity = (String) itr.next();
            if (vertices.get(entity) != null) count++;
        }
        return count;
    }

    public double getNewEdges(Collection edges) {
        int count = 0;
        for (Iterator itr = edges.iterator(); itr.hasNext(); ) {
            String relationhip = (String) itr.next();
            if (relationships.get(relationhip) != null) count++;
        }
        return count;
    }

    public double getOldVertices(Collection entities) {
        int count = 0;
        for (Iterator itr = entities.iterator(); itr.hasNext(); ) {
            String entity = (String) itr.next();
            if (vertices.get(entity) == null) count++;
        }
        return count;
    }

    public double getOldEdges(Collection edges) {
        int count = 0;
        for (Iterator itr = edges.iterator(); itr.hasNext(); ) {
            String relationhip = (String) itr.next();
            if (relationships.get(relationhip) == null) count++;
        }
        return count;
    }

    public UndirectedSparseGraph getGraph() {
        return graph;
    }
}
