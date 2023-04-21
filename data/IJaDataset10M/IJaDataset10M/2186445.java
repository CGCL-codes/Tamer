package org.mates.util;

import org.mates.util.FibonacciHeap;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Random;

/**
 * Class for storing a directed graph with real-valued weighted edges.
 * This class also contains numerous useful graph algorithms.
 *
 * @author <a href="http://www.sultanik.com/" target="_blank">Evan Sultanik</a>
 */
public class Graph {

    protected double adjacency[][];

    protected int numVertices;

    /**
     * Default constructor for a graph with no elements.
     */
    public Graph() {
        adjacency = null;
        numVertices = 0;
    }

    /**
     * Constructs a new graph from an adjacency matrix.
     */
    public Graph(double adjacency[][]) {
        this.adjacency = adjacency;
        this.numVertices = adjacency.length;
    }

    /**
     * Returns the current degree distribution of the graph.  This
     * array will always be of size {@link
     * Graph#getNumVertices()}<code>+1</code>.  If there are no hosts
     * in the topology, <code>null</code> is returned.  The <i>i</i>th
     * element of the returned array will be set to the number of
     * hosts that have degree <i>i</i>.  The degree of a host is the
     * number of outgoing edges from it.  For example, if
     * <code>getDegreeDistribution()[0] == 5</code>, then there are 5
     * hosts in the topology with no outgoing edges.
     */
    public synchronized int[] getDegreeDistribution() {
        if (numVertices <= 0) return null;
        int dist[] = new int[numVertices + 1];
        int i, j;
        for (i = 0; i < numVertices; i++) {
            dist[i] = 0;
        }
        for (i = 0; i < numVertices; i++) {
            int deg = 0;
            for (j = 0; j < numVertices; j++) {
                if (adjacency[i][j] > 0) deg++;
            }
            dist[deg]++;
        }
        return dist;
    }

    /**
     * Internal function for recursively calculating the radius of the
     * graph.
     */
    private static int[] calculateRadius(double graph[][], int vertex, boolean visited[]) {
        int max[] = null;
        int d[];
        int neighbors[] = new int[graph.length];
        int num_neighbors = 0;
        for (int i = 0; i < graph.length; i++) {
            if (!visited[i] && graph[vertex][i] > 0) {
                neighbors[num_neighbors++] = i;
                visited[i] = true;
            }
        }
        for (int i = 0; i < num_neighbors; i++) {
            d = calculateRadius(graph, neighbors[i], visited);
            if (max == null || d[0] > max[0]) max = d;
        }
        if (num_neighbors > 0) max[0]++; else {
            max = new int[2];
            max[0] = 0;
            max[1] = vertex;
        }
        return max;
    }

    /**
     * Returns an array of integers representing the shortest path
     * (i.e. <em>route</em>) from <code>i</code> to
     * <code>j</code>.  The first element of the array will always
     * be <code>i</code> (i.e. the length of the returned array
     * will always be greater than or equal to 1).  If no route
     * exists between <code>i</code> and <code>j</code>, the
     * length of the route will be exactly 1.  If a route does
     * exist, the last element of the returned array will be equal
     * to <code>j</code>.
     */
    public static int[] getRoute(int predecessorMatrix[][], int i, int j) {
        int prev[] = null;
        if (i == j) {
            prev = new int[1];
        } else if (predecessorMatrix[i][j] == i) {
            prev = new int[1];
            prev[0] = i;
            return prev;
        } else {
            int p[] = getRoute(predecessorMatrix, i, predecessorMatrix[i][j]);
            prev = new int[p.length + 1];
            for (int n = 0; n < p.length; n++) prev[n] = p[n];
        }
        prev[prev.length - 1] = j;
        return prev;
    }

    private static int getNextHop(int predMatrix[][], int from, int to) {
        if (from != predMatrix[from][to]) return getNextHop(predMatrix, from, predMatrix[from][to]); else return to;
    }

    /**
     * Converts a predecessor matrix into a route table.
     */
    public static int[][] predecessorMatrixToRouteTable(int predecessorMatrix[][]) {
        int routes[][] = new int[predecessorMatrix.length][predecessorMatrix.length];
        for (int i = 0; i < routes.length; i++) {
            for (int j = 0; j < routes.length; j++) {
                routes[i][j] = getNextHop(predecessorMatrix, i, j);
            }
        }
        return routes;
    }

    /**
     * Calculates the shortest path from <code>vertex</code> to every
     * other vertex in the graph, using Dijkstra's algorithm
     * implemented with a {@link org.mates.util.FibonacciHeap}.
     */
    public synchronized SingleSourceShortestPathData singleSourceShortestPath(int vertex) {
        int idx = vertex;
        int i;
        FibonacciHeap q = new FibonacciHeap(true);
        if (idx < 0) return null;
        double d[] = new double[numVertices];
        int r[] = new int[numVertices];
        FibonacciHeap.Node nodes[] = new FibonacciHeap.Node[numVertices];
        for (i = 0; i < numVertices; i++) {
            if (i == idx) d[i] = 0; else d[i] = Double.POSITIVE_INFINITY;
            nodes[i] = q.insert(new Integer(i), d[i]);
            r[i] = -1;
        }
        while (q.size() > 0) {
            int u = ((Integer) q.extract()).intValue();
            for (int v = 0; v < numVertices; v++) {
                if (adjacency[u][v] > 0) {
                    if (d[v] > d[u] + adjacency[u][v]) {
                        d[v] = d[u] + adjacency[u][v];
                        q.changeKey(nodes[v], d[v]);
                        r[v] = u;
                    }
                }
            }
        }
        return new SingleSourceShortestPathData(d, r, idx);
    }

    /**
     * Returns the all pairs shortest path matrix for a graph,
     * calculated using Floyd and Warshall's algorithm.
     */
    public static AllPairsShortestPathData allPairsShortestPath(double graph[][]) {
        int n = graph.length;
        double d[][] = graph;
        int table[][] = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                table[i][j] = i;
                if (d[i][j] == 0) d[i][j] = Double.POSITIVE_INFINITY;
            }
            d[i][i] = 0;
        }
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (d[i][k] + d[k][j] < d[i][j]) {
                        d[i][j] = d[i][k] + d[k][j];
                        table[i][j] = table[k][j];
                    }
                }
            }
        }
        return new AllPairsShortestPathData(d, table);
    }

    /**
     * Returns the all pairs shortest path matrix for this graph,
     * calculated using Floyd and Warshall's algorithm.
     */
    public synchronized AllPairsShortestPathData allPairsShortestPath() {
        return allPairsShortestPath(getAdjacency());
    }

    /**
     * Returns the eccentricity of a vertex.  This is the length of
     * the longest geodesic (i.e. shortest path) from
     * <code>vertex</code> to any other vertex in the graph.
     */
    public synchronized double calculateEccentricity(int vertex) {
        SingleSourceShortestPathData ssspd = singleSourceShortestPath(vertex);
        double distances[];
        int max_distance = 0;
        if (ssspd == null || (distances = ssspd.getDistances()) == null) return 0;
        for (int i = 0; i < distances.length; i++) {
            if (distances[i] > distances[max_distance]) max_distance = i;
        }
        return distances[max_distance];
    }

    /**
     * Calculates the radius of the provided graph (represented by an
     * adjacency matrix) from a specific vertex's perspective.  Note
     * that this is different than the <i>eccentricity</i> of a
     * vertex; this function basically calculates the maximum depth of
     * a depth-first traversal of the graph starting from
     * <code>vertex</code>.
     *
     * @param graph an adjacency matrix representation of a graph
     * @param vertex the index of the vertex (with respect to its representation in the adjacency matrix)
     * @return the eccentricity of the vertex. <code>0</code> is returned on error.
     */
    public static int calculateRadius(double graph[][], int vertex) {
        int ri[] = calculateRadiusWithInfo(graph, vertex);
        if (ri == null) return 0; else return ri[0];
    }

    private static int[] calculateRadiusWithInfo(double graph[][], int vertex) {
        if (graph == null || vertex < 0 || vertex >= graph.length) return null;
        boolean visited[] = new boolean[graph.length];
        for (int i = 0; i < graph.length; i++) visited[i] = false;
        visited[vertex] = true;
        return calculateRadius(graph, vertex, visited);
    }

    /**
     * Calculates the diameter of a graph.  In other words, this
     * function returns the length of the longest shortest path in the
     * graph.
     */
    public static int calculateDiameter(double graph[][]) {
        int ri[] = calculateRadiusWithInfo(graph, 0);
        if (ri == null) return 0;
        ri = calculateRadiusWithInfo(graph, ri[1]);
        return ri[0];
    }

    /**
     * Calculates the diameter of this topology.
     */
    public synchronized int calculateDiameter() {
        return calculateDiameter(adjacency);
    }

    /**
     * Returns a weighted adjacency matrix representation of the
     * graph.
     */
    public synchronized double[][] getAdjacency() {
        double adj[][] = new double[numVertices][numVertices];
        int i, j;
        for (i = 0; i < numVertices; i++) {
            for (j = 0; j < numVertices; j++) {
                adj[i][j] = adjacency[i][j];
            }
        }
        return adj;
    }

    /**
     * Returns the number of vertices in the graph.
     */
    public int getNumVertices() {
        return numVertices;
    }

    /**
     * Calculates the stationary distribution for this graph using the
     * PageRank iterative improvement approximation algorithm.
     */
    public double[] calculateStationaryDistribution() {
        return calculateStationaryDistribution(getAdjacency());
    }

    /**
     * Calculates the stationary distribution of a graph (represented
     * as an adjacency matrix) using the PageRank iterative
     * improvement approximation algorithm.  This function simply
     * calls {@link
     * Graph#calculateStationaryDistribution(double[][], int,
     * double)} with <code>adjacency.length*100</code> iterations and a
     * damping factor of <code>0.15</code>.
     */
    public static double[] calculateStationaryDistribution(double adjacency[][]) {
        return calculateStationaryDistribution(adjacency, adjacency.length * 100, 0.15);
    }

    /**
     * Calculates the stationary distribution of a graph (represented
     * as an adjacency matrix) using the PageRank iterative
     * improvement approximation algorithm.
     *
     * @param adjacency the adjacency matrix representation of the graph
     * @param iterations the number of iterations to run the algorithm
     * @param damping_factor damping factor required by the PageRank algorithm
     */
    public static double[] calculateStationaryDistribution(double adjacency[][], int iterations, double damping_factor) {
        double dist[] = new double[adjacency.length];
        double num_neighbors[] = new double[adjacency.length];
        for (int i = 0; i < dist.length; i++) {
            dist[i] = 1.0 / (double) dist.length;
            num_neighbors[i] = 0;
            for (int j = 0; j < dist.length; j++) {
                if (i != j && adjacency[i][j] > 0) num_neighbors[i] += 1.0;
            }
        }
        for (int i = 0; i < iterations; i++) {
            for (int j = 0; j < dist.length; j++) {
                if (num_neighbors[j] == 0) continue;
                double give_away = dist[j] * (1.0 - damping_factor);
                dist[j] -= give_away;
                give_away /= num_neighbors[j];
                for (int k = 0; k < dist.length; k++) {
                    if (k != j && adjacency[j][k] > 0) dist[k] += give_away;
                }
            }
        }
        return dist;
    }

    /**
     * Generates a random, directed, unweighted graph.
     *
     * @param n number of vertices
     * @param p edge probability
     * @param random a {@link java.util.Random} object using which to
     * generate the random edges.
     *
     * @return the adjacency matrix representation of the graph, or
     * <code>null</code> if <code>n&le;0</code>.  The diagonal of the
     * adjacency matrix will always be <code>false</code>
     * (<em>i.e.</em> the graph will have no loops).
     */
    public static boolean[][] randomGraph(int n, double p, Random random) {
        if (n <= 0) return null;
        boolean g[][] = new boolean[n][n];
        if (p > 1.0) p = 1.0;
        if (p < 0.0) p = 0.0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) g[i][j] = false; else {
                    g[i][j] = (random.nextDouble() <= p);
                }
            }
        }
        return g;
    }

    /**
     * Generates a random, directed, unweighted graph.
     *
     * @param n number of vertices
     * @param m number of edges
     * @param random a {@link java.util.Random} object using which to
     * generate the random edges.
     *
     * @return the adjacency matrix representation of the graph, or
     * <code>null</code> if <code>n&le;0</code> or <code>n<0</code>.
     * The diagonal of the adjacency matrix will always be
     * <code>false</code> (<em>i.e.</em> the graph will have no
     * loops).
     */
    public static boolean[][] randomGraph(int n, int m, Random random) {
        if (n <= 0) return null;
        boolean g[][] = new boolean[n][n];
        int nSquared = n * n;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                g[i][j] = false;
            }
        }
        if (m > nSquared - n) m = nSquared - n;
        for (; m > 0; m--) {
            int from;
            int to;
            do {
                int newidx = random.nextInt(nSquared);
                from = newidx / n;
                to = newidx % n;
            } while (from == to || g[from][to]);
            g[from][to] = true;
        }
        return g;
    }

    /**
     * Returns the unweighted adjacency representation of this graph.
     */
    public boolean[][] getUnweightedGraph() {
        boolean g[][] = new boolean[adjacency.length][adjacency.length];
        for (int i = 0; i < adjacency.length; i++) for (int j = 0; j < adjacency.length; j++) g[i][j] = (adjacency[i][j] > 0.0);
        return g;
    }

    /**
     * Tests if a directed, unweighted graph is connected.  Runs in
     * O(n^3) time.
     *
     * @param graph the adjacency matrix representation of a graph
     * @return if the graph is connected
     */
    public static boolean isConnected(boolean graph[][]) {
        AllPairsShortestPathData apspd;
        double newgraph[][] = new double[graph.length][graph.length];
        int i, j;
        for (i = 0; i < graph.length; i++) {
            for (j = 0; j < graph.length; j++) {
                newgraph[i][j] = (graph[i][j] ? 1.0 : 0.0);
            }
        }
        double distances[][] = allPairsShortestPath(newgraph).getDistances();
        for (i = 0; i < graph.length; i++) {
            for (j = 0; j < graph.length; j++) {
                if (i != j && distances[i][j] >= Double.POSITIVE_INFINITY) return false;
            }
        }
        return true;
    }

    /**
     * Calculates the similarity between this graph and another graph.
     *
     * @see Graph#calculateSimilarity(Graph, Graph)
     */
    public double calculateSimilarity(Graph graph) {
        return calculateSimilarity(this, graph);
    }

    /**
     * Calculates the similarity between two directed, unlabeled,
     * weighted graphs, without a progress dialog.
     *
     * @see Graph#calculateSimilarity(Graph, Graph, java.awt.Component, java.lang.Object)
     */
    public static double calculateSimilarity(Graph g1, Graph g2) {
        return calculateSimilarity(g1, g2, null, null);
    }

    /**
     * Calculates the similarity between two directed, labeled,
     * weighted graphs.  "Similarity" is a concocted metric; it is not
     * formally defined within Graph Theory.  However, it is related
     * but not equal to <em>edit distance</em>.  The algorithm
     * implemented here is very na&iuml;ve.  This algorithm runs in
     * <em>O(n</em><sup>3</sup><em>)</em> time, where <em>n</em> is
     * the number of vertices of each graph.  Calculating true graph
     * edit distance is thought to be <em>NP</em>-<span
     * style="font-variant: small-caps">Complete</span>, or at least
     * <span style="font-variant:
     * small-caps">Graph-Isomorphism-Complete</span>.
     *
     * <p>The way this algorithm works is as follows:
     * <ul><li>Calculate the all pairs shortest path distance matrix
     * for each graph.</li><li>For each pair of
     * vertices<ul><li>Subtract their distance in <code>g2</code> from
     * their distance in <code>g1</code>.</li></ul></li><li>Return the
     * sum of the differences</li></ul></p>
     *
     * @return the similarity metric between the two graphs.  The
     * smaller the magnitude of the metric, the more similar the
     * graphs.  Negative numbers will never be returned.  Returns
     * {@link java.lang.Double#POSITIVE_INFINITY} if
     * <code>g1.getNumVertices() != g2.getNumVertices()</code>.
     * Returns <code>0.0</code> if neither graph has any vertices.
     */
    public static double calculateSimilarityLabeled(Graph g1, Graph g2) {
        if (g1.getNumVertices() != g2.getNumVertices()) return Double.POSITIVE_INFINITY; else if (g1.getNumVertices() == 0) return 0.0;
        double d1[][] = allPairsShortestPath(g1.getAdjacency()).getDistances();
        double d2[][] = allPairsShortestPath(g2.getAdjacency()).getDistances();
        double sum = 0.0;
        for (int i = 0; i < g1.getNumVertices(); i++) {
            for (int j = 0; j < g1.getNumVertices(); j++) {
                sum += d1[i][j] - d2[i][j];
            }
        }
        return sum;
    }

    /**
     * Calculates the similarity between two directed, unlabeled,
     * weighted graphs.  "Similarity" is a concocted metric; it is not
     * formally defined within Graph Theory.  However, it is related
     * but not equal to <em>edit distance</em>.  The algorithm
     * implemented here is very na&iuml;ve.  This algorithm runs in
     * <em>O(n</em>!<em>n</em><sup>2</sup><em>)</em> time, where
     * <em>n</em> is the number of vertices of each graph.
     * Calculating true graph edit distance is thought to be
     * <em>NP</em>-<span style="font-variant:
     * small-caps">Complete</span>, or at least <span
     * style="font-variant:
     * small-caps">Graph-Isomorphism-Complete</span>.
     *
     * <p>The way this algorithm works is as follows: <ul><li>For each
     * bijective mapping between the vertices of each graph
     * (<em>i.e.</em> for every possible labeling of the graphs)</li>
     * <ul><li>For each pair of vertices</li> <ul><li>Sum the
     * differences of the weight of the edge in <code>g1</code> with
     * the weight of the equivalent edge in the permutation of
     * <code>g2</code></li></ul></ul> <li>Return the minimum
     * sum</li></ul></p>
     *
     * @param parent if not <code>null</code>, a modal progress dialog will be displayed.
     * @param message if <code>parent</code> is not <code>null</code>, the message for the progress dialog.
     *
     * @return the similarity metric between the two graphs.  The
     * smaller the magnitude of the metric, the more similar the
     * graphs.  Negative numbers will never be returned.  Returns
     * {@link java.lang.Double#POSITIVE_INFINITY} if
     * <code>g1.getNumVertices() != g2.getNumVertices()</code>.
     * Returns <code>0.0</code> if neither graph has any vertices.
     */
    public static double calculateSimilarity(Graph g1, Graph g2, java.awt.Component parent, Object message) {
        double similarity = 0.0;
        if (g1.getNumVertices() != g2.getNumVertices()) return Double.POSITIVE_INFINITY; else if (g1.getNumVertices() == 0) return 0.0;
        double a1[][] = g1.getAdjacency();
        double a2[][] = g2.getAdjacency();
        PermutationGenerator generator = new PermutationGenerator(g1.getNumVertices());
        double minSum = Double.POSITIVE_INFINITY;
        javax.swing.ProgressMonitor monitor = null;
        if (parent != null) {
            monitor = new javax.swing.ProgressMonitor(parent, message, null, 0, generator.getTotal().intValue());
            monitor.setMillisToDecideToPopup(0);
        }
        while (generator.hasMore()) {
            int mapping[] = generator.getNext();
            double sum = 0.0;
            for (int i = 0; i < a1.length; i++) {
                for (int j = 0; j < a2.length; j++) {
                    if (i == j) continue;
                    sum += Math.abs(a1[i][j] - a2[mapping[i]][mapping[j]]);
                }
            }
            if (sum < minSum) minSum = sum;
            if (monitor != null) {
                monitor.setProgress(generator.getTotal().subtract(generator.getNumLeft()).intValue());
            }
        }
        return minSum;
    }

    /**
     * Tests if a directed, unweighted graph is biconnected.  Runs in
     * O(n^3) time.
     *
     * @param graph the adjacency matrix representation of a graph
     * @return if the graph is biconnected
     */
    public static boolean isBiconnected(boolean graph[][]) {
        boolean tmpgraph[][] = new boolean[graph.length - 1][graph.length - 1];
        int r, c;
        if (!isConnected(graph)) return false;
        for (int i = 0; i < graph.length; i++) {
            for (int row = 0; row < graph.length; row++) {
                for (int col = 0; col < graph.length; col++) {
                    if (row != i && col != i) {
                        r = row;
                        if (row > i) r--;
                        c = col;
                        if (col > i) c--;
                        tmpgraph[r][c] = graph[row][col];
                    }
                }
            }
            if (!isConnected(tmpgraph)) return false;
        }
        return true;
    }

    /**
     * Randomly relabels a graph.  Note that this function <em>does
     * not</em> modify the graph in-place; it returnes the relabeled
     * graph as a new object.
     *
     * @param graph adjacency matrix representation of a graph
     * @param random a {@link java.util.Random} object using which to
     * generate the random relabeling.
     * @return the relabeled graph
     */
    public static boolean[][] randomlyRelabel(boolean graph[][], Random random) {
        boolean newgraph[][] = new boolean[graph.length][graph.length];
        int mapping[] = new int[graph.length];
        int i, j;
        boolean already_exists;
        for (i = 0; i < graph.length; i++) {
            do {
                mapping[i] = random.nextInt(graph.length);
                already_exists = false;
                for (j = 0; j < i; j++) {
                    if (mapping[j] == mapping[i]) {
                        already_exists = true;
                        break;
                    }
                }
            } while (already_exists);
        }
        for (i = 0; i < graph.length; i++) for (j = 0; j < graph.length; j++) newgraph[mapping[i]][mapping[j]] = graph[i][j];
        return newgraph;
    }

    /**
     * Create a random graph with <code>n</code> vertices and
     * <code>m</code> articulation vertices.  This function is
     * implementing using S. M. Selkow's algorithm from Discrete
     * Mathematics 185 (1998) page 191.
     *
     * @param n number of vertices in the graph
     * @param m number of articulation vertices
     * @param random a {@link java.util.Random} object using which to
     * generate the random graph.
     * 
     * @return a random graph with n vertices and m articulation vertices
     */
    public static boolean[][] createRandomGraphWithArticulationPoints(int n, int m, Random random) {
        boolean graph[][];
        boolean complete_graph[][];
        int selected[] = new int[m];
        boolean already_selected;
        int i, j;
        if (m > n - 2) m = n - 2;
        while (true) {
            graph = randomGraph(n - m, 0.5, random);
            if (isBiconnected(graph)) break;
        }
        for (i = 0; i < m; i++) {
            do {
                selected[i] = random.nextInt(n - m + i);
                already_selected = false;
                for (j = 0; j < i; j++) {
                    if (selected[j] == selected[i]) already_selected = true;
                }
            } while (already_selected);
        }
        complete_graph = new boolean[n][n];
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                if (i < n - m && j < n - m) complete_graph[i][j] = graph[i][j]; else complete_graph[i][j] = false;
            }
        }
        for (i = 0; i < m; i++) {
            do {
                complete_graph[selected[i]][n - m + i] = random.nextBoolean();
                complete_graph[n - m + i][selected[i]] = complete_graph[selected[i]][n - m + i];
            } while (!complete_graph[selected[i]][n - m + i]);
        }
        complete_graph = randomlyRelabel(complete_graph, random);
        return complete_graph;
    }
}
