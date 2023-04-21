package nz.ac.vuw.ecs.kcassell.cluster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import nz.ac.vuw.ecs.kcassell.callgraph.CallGraphLink;
import nz.ac.vuw.ecs.kcassell.callgraph.JavaCallGraph;
import nz.ac.vuw.ecs.kcassell.logging.UtilLogger;
import nz.ac.vuw.ecs.kcassell.similarity.ClusterDistanceUtils;
import nz.ac.vuw.ecs.kcassell.similarity.CzibulaDistanceCalculator;
import nz.ac.vuw.ecs.kcassell.similarity.DistanceCalculatorEnum;
import nz.ac.vuw.ecs.kcassell.similarity.DistanceCalculatorIfc;
import nz.ac.vuw.ecs.kcassell.similarity.DistanceMatrix;
import nz.ac.vuw.ecs.kcassell.similarity.IdentifierDistanceCalculator;
import nz.ac.vuw.ecs.kcassell.similarity.IntraClassDistanceCalculator;
import nz.ac.vuw.ecs.kcassell.similarity.JDeodorantDistanceCalculator;
import nz.ac.vuw.ecs.kcassell.similarity.LevenshteinDistanceCalculator;
import nz.ac.vuw.ecs.kcassell.similarity.LocalNeighborhoodDistanceCalculator;
import nz.ac.vuw.ecs.kcassell.similarity.SimonDistanceCalculator;
import nz.ac.vuw.ecs.kcassell.similarity.VectorSpaceModelCalculator;
import nz.ac.vuw.ecs.kcassell.utils.ApplicationParameters;
import nz.ac.vuw.ecs.kcassell.utils.EclipseUtils;
import nz.ac.vuw.ecs.kcassell.utils.ParameterConstants;
import nz.ac.vuw.ecs.kcassell.utils.RefactoringConstants;
import org.eclipse.jdt.core.JavaModelException;
import edu.uci.ics.jung.algorithms.shortestpath.MinimumSpanningForest;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * An agglomerative clusterer that makes use of a distance matrix to determine
 * which objects to combine/cluster.
 * @author Keith
 *
 */
public class MatrixBasedAgglomerativeClusterer implements ClustererIfc<String> {

    /** Calculates distances between nodes. */
    protected DistanceCalculatorIfc<String> distanceCalculator = null;

    /** Keeps track of how many clustering steps have occurred. */
    protected int previousIteration = 0;

    /** The distances between the existing clusters and/or individuals */
    protected DistanceMatrix<String> distanceMatrix = null;

    /** The distances between the original individuals */
    protected DistanceMatrix<String> originalMatrix = null;

    /** Indicates how to determine whether groups should be combined, e.g.
	 * single link (based on the nearest members of the group).	 */
    protected ClusterCombinationEnum whichLink = ClusterCombinationEnum.AVERAGE_LINK;

    /** This keeps track of the clusters that have been seen.  The key
	 * is the cluster name; the value is the cluster.  "Elements"
	 * (clusters of one) will have a handle key and a null value. */
    protected HashMap<String, MemberCluster> clusterHistory = new HashMap<String, MemberCluster>();

    protected static final UtilLogger logger = new UtilLogger("MatrixBasedAgglomerativeClusterer");

    /**
     * For use by subclasses.
     */
    protected MatrixBasedAgglomerativeClusterer() {
    }

    /**
     * Given a list of (nonclustered) objects and a calculator to calculate
     * the distances between them, initialize the clusterer by building
     * the distance matrix.
     * @param elements a collection of things to be clustered
     * @param calc calculates the distances between objects
     */
    public MatrixBasedAgglomerativeClusterer(List<String> elements, DistanceCalculatorIfc<String> calc) {
        distanceCalculator = calc;
        ApplicationParameters parameters = ApplicationParameters.getSingleton();
        String linkage = parameters.getParameter(ParameterConstants.LINKAGE_KEY, ClusterCombinationEnum.AVERAGE_LINK.toString());
        whichLink = ClusterCombinationEnum.valueOf(linkage);
        for (String element : elements) {
            clusterHistory.put(element, null);
        }
        buildDistanceMatrix(elements);
        originalMatrix = distanceMatrix;
        logger.info(distanceMatrix.toString());
    }

    /**
     * Given a list of (nonclustered) objects and a calculator to calculate
     * the distances between them, initialize the clusterer by building
     * the distance matrix.
     * @param elements a collection of things to be clustered
     * @param calc calculates the distances between objects
     * @param linkage e.g. single link
     */
    public MatrixBasedAgglomerativeClusterer(List<String> elements, DistanceCalculatorIfc<String> calc, String linkage) {
        distanceCalculator = calc;
        whichLink = ClusterCombinationEnum.valueOf(linkage);
        for (String element : elements) {
            clusterHistory.put(element, null);
        }
        buildDistanceMatrix(elements);
        originalMatrix = distanceMatrix;
        logger.info(distanceMatrix.toString());
    }

    /**
	 * Use the distance calculator to fill in the distance matrix
	 * for the elements provided.
	 * @param elements usually the handles for the class members
	 */
    protected void buildDistanceMatrix(List<String> elements) {
        distanceMatrix = new DistanceMatrix<String>(elements);
        for (int row = 0; row < elements.size(); row++) {
            String obj1 = elements.get(row);
            for (int col = 0; col <= row; col++) {
                String obj2 = elements.get(col);
                Number distance = calculateDistance(obj1, obj2);
                if (distance.equals(RefactoringConstants.UNKNOWN_DISTANCE)) {
                    distance = 1.0;
                } else {
                    distance = Math.max(0.0, distance.doubleValue());
                }
                distanceMatrix.setDistance(obj1, obj2, distance);
            }
        }
    }

    public DistanceMatrix<String> getDistanceMatrix() {
        return distanceMatrix;
    }

    /**
	 * @return the identifiers of the clusters
	 */
    public Collection<String> getClusters() {
        return distanceMatrix.getHeaders();
    }

    /**
	 * @return the clusters
	 */
    public Collection<MemberCluster> getMemberClusters() {
        List<String> headers = distanceMatrix.getHeaders();
        ArrayList<MemberCluster> memberClusters = new ArrayList<MemberCluster>();
        for (String header : headers) {
            MemberCluster cluster = clusterHistory.get(header);
            if (cluster == null) {
                cluster = new MemberCluster();
                cluster.addElement(header);
            }
            memberClusters.add(cluster);
        }
        return memberClusters;
    }

    /**
	 * @return the clusterHistory
	 */
    public HashMap<String, MemberCluster> getClusterHistory() {
        return clusterHistory;
    }

    public ClusterCombinationEnum getWhichLink() {
        return whichLink;
    }

    public void setWhichLink(ClusterCombinationEnum whichLink) {
        this.whichLink = whichLink;
    }

    /**
     * Form clusters by combining nodes.  Quit when there is a single cluster
     * @return the cluster of everything.
     */
    public MemberCluster getSingleCluster() {
        MemberCluster cluster = null;
        try {
            while (continueClustering()) {
                cluster = clusterOnce();
                previousIteration++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cluster;
    }

    /**
     * Form clusters by combining nodes.  Two objects should be combined
     * for each iteration.  The total number of cluster steps that should be
     * performed is determined by a user preference/parameter.  By default,
     * everything will be put into a single cluster.
     * @return a collection of all clusters (some will be single objects)
     */
    public Collection<String> cluster() {
        Collection<String> clusters = null;
        List<String> headers = distanceMatrix.getHeaders();
        int numHeaders = headers.size();
        if (numHeaders > 0) {
            ApplicationParameters params = ApplicationParameters.getSingleton();
            int iterations = params.getIntParameter(ParameterConstants.AGGLOMERATION_CLUSTERS_KEY, numHeaders - 1);
            clusters = cluster(iterations);
        }
        return clusters;
    }

    /**
     * Form clusters by combining nodes.  Two nodes should be combined
     * for each iteration.
     * @param iteration the total number of cluster steps that should be
     * performed
     * @return a collection of all clusters (some will be single nodes)
     */
    public Collection<String> cluster(int iteration) {
        int numIterations = iteration - previousIteration;
        for (int i = 0; i < numIterations && continueClustering(); i++) {
            clusterOnce();
            previousIteration++;
        }
        return getClusters();
    }

    /**
	 * Add a new level of clustering
	 */
    protected MemberCluster clusterOnce() {
        Distance<String> nearest = distanceMatrix.findNearest();
        MemberCluster cluster = createCluster(nearest);
        modifyMatrix(cluster);
        return cluster;
    }

    /**
	 * Creates a cluster from two identifiers (each of which may represent
	 * one or more elements).
	 * @param neighbors the two objects and the distance between them
	 * @return the new cluster
	 */
    protected MemberCluster createCluster(Distance<String> neighbors) {
        MemberCluster cluster = new MemberCluster();
        String near1 = neighbors.getFirst();
        String near2 = neighbors.getSecond();
        logger.info("createCluster from " + near1 + ", " + near2);
        addChildToCluster(cluster, near1);
        addChildToCluster(cluster, near2);
        Number distance = neighbors.getDistance();
        cluster.setDistance(distance.doubleValue());
        String comment = "dist. = " + distance;
        cluster.setComment(comment);
        String clusterName = nameCluster(cluster, (near1.compareTo(near2) < 0) ? near1 : near2);
        clusterHistory.put(clusterName, cluster);
        return cluster;
    }

    /**
	 * Adds to a cluster based on an identifier (which may represent
	 * one or more elements).
	 * @param cluster the existing cluster
	 * @param childName the identifier of the addition
	 * @return the enlarged cluster
	 */
    protected void addChildToCluster(MemberCluster cluster, String childName) {
        MemberCluster childCluster = clusterHistory.get(childName);
        if (childCluster == null) {
            cluster.addElement(childName);
        } else {
            cluster.addCluster(childCluster);
        }
    }

    /**
	 * Create a name for the new cluster by combining the provided name
	 * with the iteration number
	 * @param cluster the cluster to be named
	 * @param name the name to use as the basis of the new name
	 */
    protected String nameCluster(MemberCluster cluster, String name) {
        int indexPlus = name.indexOf("+");
        if (indexPlus >= 0) {
            name = name.substring(0, indexPlus);
        }
        name += "+" + (previousIteration + 1);
        cluster.setClusterName(name);
        return name;
    }

    /**
	 * Revises the distanceMatrix after a clustering step by removing the rows
	 * and columns for the elements that were merged, and creating a row for
	 * the newly formed cluster
	 * @param cluster
	 * @return
	 */
    protected DistanceMatrix<String> modifyMatrix(MemberCluster cluster) {
        String clusterName = cluster.getClusterName();
        List<String> headers = getNewHeaders(cluster);
        DistanceMatrix<String> newMatrix = new DistanceMatrix<String>(headers);
        int numElements = headers.size();
        for (int row = 0; row < numElements - 1; row++) {
            String obj1 = headers.get(row);
            for (int col = 0; col <= row; col++) {
                String obj2 = headers.get(col);
                Number distance = distanceMatrix.getDistance(obj1, obj2);
                newMatrix.setDistance(obj1, obj2, distance);
            }
        }
        for (int col = 0; col < numElements; col++) {
            String obj2 = headers.get(col);
            Number distance = calculateDistance(clusterName, obj2);
            newMatrix.setDistance(clusterName, obj2, distance);
        }
        distanceMatrix = newMatrix;
        return distanceMatrix;
    }

    /**
	 * Returns the names of the current clusters
	 * @param cluster the most recently created cluster
	 * @return the names of the current clusters
	 */
    protected List<String> getNewHeaders(MemberCluster cluster) {
        String clusterName = cluster.getClusterName();
        Set<?> children = cluster.getChildren();
        List<String> headers = new ArrayList<String>(distanceMatrix.getHeaders());
        for (Object child : children) {
            if (child instanceof MemberCluster) {
                MemberCluster childCluster = (MemberCluster) child;
                headers.remove(childCluster.getClusterName());
            } else {
                headers.remove(child);
            }
        }
        headers.add(clusterName);
        return headers;
    }

    protected boolean continueClustering() {
        List<String> headers = distanceMatrix.getHeaders();
        return headers.size() > 1;
    }

    /**
	 * Clusters the members of the indicated class using the distance
	 * calculator specified in the user preferences/parameters.
	 * @param classHandle the Eclipse handle of the class to cluster
	 * @return the fully agglomerated cluster
	 */
    public static MemberCluster cluster(String classHandle) {
        MemberCluster cluster = null;
        try {
            DistanceCalculatorIfc<String> calc = setUpSpecifiedCalculator(classHandle);
            List<String> memberHandles = EclipseUtils.getFilteredMemberHandles(classHandle);
            MatrixBasedAgglomerativeClusterer clusterer = new MatrixBasedAgglomerativeClusterer(memberHandles, calc);
            cluster = clusterer.getSingleCluster();
            System.out.println("Final cluster:\n" + cluster.toNestedString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cluster;
    }

    /**
	 * Calculates the distance between the objects corresponding
	 * to the identifiers.  When an identifier corresponds to a cluster,
	 * we calculate all distances for the objects within that cluster
	 * and return the smallest.
	 * We ignore the case of the letters in the comparison.
	 * @return between 0 (identical) and 1 (completely different)
	 */
    protected Number calculateDistance(String s1, String s2) {
        Double result = (whichLink == ClusterCombinationEnum.SINGLE_LINK) ? 1.0 : 0.0;
        if (s1 != null && s2 != null) {
            DistanceCalculatorEnum calcType = distanceCalculator.getType();
            if (!DistanceCalculatorEnum.usesHandles(calcType)) {
                s1 = EclipseUtils.getNameFromHandle(s1);
                s2 = EclipseUtils.getNameFromHandle(s2);
            }
            MemberCluster cluster1 = clusterHistory.get(s1);
            MemberCluster cluster2 = clusterHistory.get(s2);
            if (cluster1 == null) {
                if (cluster2 == null) {
                    result = calculateDistanceBetweenIndividuals(s1, s2, result);
                } else {
                    result = getDistanceToGroup(s1, cluster2, result);
                }
            } else {
                if (cluster2 == null) {
                    result = getDistanceToGroup(s2, cluster1, result);
                } else {
                    Set<String> ids1 = cluster1.getElements();
                    for (String id1 : ids1) {
                        Double distance = getDistanceToGroup(id1, cluster2, result);
                        if ((whichLink == ClusterCombinationEnum.SINGLE_LINK) && distance.compareTo(result) < 0) {
                            result = distance;
                        } else if ((whichLink == ClusterCombinationEnum.COMPLETE_LINK) && distance.compareTo(result) > 0) {
                            result = distance;
                        } else if ((whichLink == ClusterCombinationEnum.AVERAGE_LINK)) {
                            result += distance;
                        }
                    }
                    if ((whichLink == ClusterCombinationEnum.AVERAGE_LINK)) {
                        result /= ids1.size();
                    }
                }
            }
        }
        return result;
    }

    private Double calculateDistanceBetweenIndividuals(String s1, String s2, Double result) {
        Number nDistance = distanceCalculator.calculateDistance(s1, s2);
        if (nDistance != null) {
            result = nDistance.doubleValue();
        } else {
            System.err.println("name1 =" + s1);
        }
        return result;
    }

    /**
	 * Get the distance from the element specified
	 * to any member of the group.
	 * @param s1 the single element
	 * @param ids the group
	 * @param result the distance so far
	 * @return the distance
	 */
    protected Double getDistanceToGroup(String s1, MemberCluster cluster, Double oldResult) {
        Double newResult = null;
        if (whichLink == ClusterCombinationEnum.SINGLE_LINK) {
            newResult = ClusterDistanceUtils.singleLinkDistance(s1, cluster, originalMatrix, distanceCalculator);
            newResult = Math.min(newResult, oldResult);
        } else if (whichLink == ClusterCombinationEnum.COMPLETE_LINK) {
            newResult = ClusterDistanceUtils.completeLinkDistance(s1, cluster, originalMatrix, distanceCalculator);
            newResult = Math.max(newResult, oldResult);
        } else if ((whichLink == ClusterCombinationEnum.AVERAGE_LINK)) {
            newResult = ClusterDistanceUtils.averageLinkDistance(s1, cluster, originalMatrix, distanceCalculator);
        }
        return newResult;
    }

    protected static DistanceCalculatorIfc<String> setUpSpecifiedCalculator(String classHandle) throws Exception {
        ApplicationParameters parameters = ApplicationParameters.getSingleton();
        String sCalc = parameters.getParameter(ParameterConstants.CALCULATOR_KEY, DistanceCalculatorEnum.Levenshtein.toString());
        DistanceCalculatorIfc<String> calc = null;
        if (DistanceCalculatorEnum.Czibula.toString().equals(sCalc)) {
            calc = setUpCzibulaClustering(classHandle);
        } else if (DistanceCalculatorEnum.Identifier.toString().equals(sCalc)) {
            calc = setUpIdentifierClustering();
        } else if (DistanceCalculatorEnum.IntraClass.toString().equals(sCalc)) {
            calc = setUpIntraClassDistanceClustering(classHandle);
        } else if (DistanceCalculatorEnum.JDeodorant.toString().equals(sCalc)) {
            calc = setUpJDeodorantClustering(classHandle);
        } else if (DistanceCalculatorEnum.Levenshtein.toString().equals(sCalc)) {
            calc = setUpLevenshteinClustering();
        } else if (DistanceCalculatorEnum.LocalNeighborhood.toString().equals(sCalc)) {
            calc = setUpLocalNeighborhoodClustering(classHandle);
        } else if (DistanceCalculatorEnum.Simon.toString().equals(sCalc)) {
            calc = setUpSimonClustering(classHandle);
        } else if (DistanceCalculatorEnum.VectorSpaceModel.toString().equals(sCalc)) {
            calc = VectorSpaceModelCalculator.getCalculator(classHandle);
        }
        return calc;
    }

    /**
	 * Set up a IdentifierDistanceCalculator
	 * @return the calculator
	 */
    protected static DistanceCalculatorIfc<String> setUpIdentifierClustering() {
        DistanceCalculatorIfc<String> calc = new IdentifierDistanceCalculator();
        return calc;
    }

    /**
	 * Set up a LevenshteinDistanceCalculator
	 * @return the calculator
	 */
    protected static DistanceCalculatorIfc<String> setUpLevenshteinClustering() {
        DistanceCalculatorIfc<String> calc = new LevenshteinDistanceCalculator();
        return calc;
    }

    /**
	 * Set up an IntraClassDistanceCalculator
	 * @param classHandle the Eclipse handle for the class to be clustered
	 * @return the calculator
	 * @throws JavaModelException
	 */
    protected static DistanceCalculatorIfc<String> setUpIntraClassDistanceClustering(String classHandle) throws JavaModelException {
        JavaCallGraph callGraph = new JavaCallGraph(classHandle, EdgeType.UNDIRECTED);
        DistanceCalculatorIfc<String> calc = new IntraClassDistanceCalculator(callGraph);
        return calc;
    }

    /**
	 * Set up a CzibulaDistanceCalculator
	 * @param classHandle the Eclipse handle for the class to be clustered
	 * @return the calculator
	 * @throws JavaModelException
	 */
    protected static DistanceCalculatorIfc<String> setUpCzibulaClustering(String classHandle) throws JavaModelException {
        JavaCallGraph callGraph = new JavaCallGraph(classHandle, EdgeType.UNDIRECTED);
        DistanceCalculatorIfc<String> calc = new CzibulaDistanceCalculator(callGraph);
        return calc;
    }

    /**
	 * Set up a LocalNeighborhoodDistanceCalculator
	 * @param classHandle the Eclipse handle for the class to be clustered
	 * @return the calculator
	 * @throws JavaModelException
	 */
    protected static DistanceCalculatorIfc<String> setUpLocalNeighborhoodClustering(String classHandle) throws JavaModelException {
        JavaCallGraph callGraph = new JavaCallGraph(classHandle, EdgeType.UNDIRECTED);
        DistanceCalculatorIfc<String> calc = new LocalNeighborhoodDistanceCalculator(callGraph);
        return calc;
    }

    /**
	 * Set up a JDeodorantDistanceCalculator
	 * @param classHandle the Eclipse handle for the class to be clustered
	 * @return the calculator
	 * @throws JavaModelException
	 */
    protected static DistanceCalculatorIfc<String> setUpJDeodorantClustering(String classHandle) throws JavaModelException {
        JavaCallGraph callGraph = new JavaCallGraph(classHandle, EdgeType.UNDIRECTED);
        DistanceCalculatorIfc<String> calc = new JDeodorantDistanceCalculator(callGraph);
        return calc;
    }

    /**
	 * Set up a SimonDistanceCalculator
	 * @param classHandle the Eclipse handle for the class to be clustered
	 * @return the calculator
	 * @throws JavaModelException
	 */
    protected static DistanceCalculatorIfc<String> setUpSimonClustering(String classHandle) throws JavaModelException {
        JavaCallGraph callGraph = new JavaCallGraph(classHandle, EdgeType.UNDIRECTED);
        DistanceCalculatorIfc<String> calc = new SimonDistanceCalculator(callGraph);
        return calc;
    }

    /**
	 * Creates a minimum spanning forest from the matrix.
	 * @return the forest
	 */
    public Forest<String, CallGraphLink> createMinimumSpanningForest() {
        Graph<String, CallGraphLink> graph = new SparseMultigraph<String, CallGraphLink>();
        List<String> headers = distanceMatrix.getHeaders();
        int size = headers.size();
        for (int i = 0; i < size; i++) {
            String member1 = headers.get(i);
            graph.addVertex(member1);
        }
        CallGraphLink.CallGraphLinkFactory linkFactory = new CallGraphLink.CallGraphLinkFactory();
        HashMap<CallGraphLink, Double> edgeWeights = new HashMap<CallGraphLink, Double>();
        for (int i = 0; i < size; i++) {
            String member1 = headers.get(i);
            for (int j = 0; j < i; j++) {
                String member2 = headers.get(j);
                Number distance = distanceMatrix.getDistance(member1, member2);
                if (!RefactoringConstants.UNKNOWN_DISTANCE.equals(distance) && !distance.equals(1.0)) {
                    CallGraphLink link = linkFactory.create();
                    edgeWeights.put(link, distance.doubleValue());
                    link.setWeight(distance);
                    graph.addEdge(link, member1, member2);
                }
            }
        }
        MinimumSpanningForest<String, CallGraphLink> forest = new MinimumSpanningForest<String, CallGraphLink>(graph, new DelegateForest<String, CallGraphLink>(), null, edgeWeights);
        Forest<String, CallGraphLink> minSpanningForest = forest.getForest();
        return minSpanningForest;
    }

    /**
	 * Cluster based on the identifiers.
	 * @param handle the handle of the class whose members are to be clustered
	 * @param calc the distance calculator to use
	 * @throws JavaModelException
	 */
    public static MemberCluster clusterUsingCalculator(String handle, DistanceCalculatorIfc<String> calc) throws JavaModelException {
        List<String> names = null;
        DistanceCalculatorEnum calcType = calc.getType();
        if (DistanceCalculatorEnum.usesHandles(calcType)) {
            names = EclipseUtils.getFilteredMemberHandles(handle);
        } else {
            names = EclipseUtils.getFilteredMemberNames(handle);
        }
        MatrixBasedAgglomerativeClusterer clusterer = new MatrixBasedAgglomerativeClusterer(names, calc);
        MemberCluster cluster = clusterer.getSingleCluster();
        return cluster;
    }

    /**
	 * Cluster based on the identifiers.
	 * @param handle the handle of the class whose members are to be clustered
	 * @param calc the distance calculator to use
	 * @throws JavaModelException
	 */
    public static MemberCluster clusterUsingCalculator(String handle, DistanceCalculatorIfc<String> calc, String linkage) throws JavaModelException {
        List<String> names = null;
        DistanceCalculatorEnum calcType = calc.getType();
        if (DistanceCalculatorEnum.usesHandles(calcType)) {
            names = EclipseUtils.getFilteredMemberHandles(handle);
        } else {
            names = EclipseUtils.getFilteredMemberNames(handle);
        }
        MatrixBasedAgglomerativeClusterer clusterer = new MatrixBasedAgglomerativeClusterer(names, calc, linkage);
        MemberCluster cluster = clusterer.getSingleCluster();
        return cluster;
    }
}
