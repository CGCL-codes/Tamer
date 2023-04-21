package com.rapidminer.operator.clustering.clusterer;

import java.util.ArrayList;
import java.util.List;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.Tools;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.operator.OperatorCapability;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.UserError;
import com.rapidminer.operator.clustering.CentroidClusterModel;
import com.rapidminer.operator.clustering.ClusterModel;
import com.rapidminer.operator.learner.CapabilityProvider;
import com.rapidminer.operator.ports.metadata.CapabilityPrecondition;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeInt;
import com.rapidminer.tools.Ontology;
import com.rapidminer.tools.RandomGenerator;
import com.rapidminer.tools.math.similarity.DistanceMeasure;
import com.rapidminer.tools.math.similarity.numerical.EuclideanDistance;

/**
 * This operator represents an implementation of k-means. This operator will create a cluster attribute if not present
 * yet.
 * 
 * The implementation is according to paper of C. Elkan:
 * - Using the Triangle Inequality to Accelerate k-Means -
 * Proceedings of the Twentieth International Conference on Machine Learning (ICML-2003), Washington DC, 2003
 * 
 * @author Alexander Arimond
 */
public class FastKMeans extends RMAbstractClusterer implements CapabilityProvider {

    /** The parameter name for &quot;the maximal number of clusters&quot; */
    public static final String PARAMETER_K = "k";

    /**
	 * The parameter name for &quot;the maximal number of runs of the k method with random initialization that are
	 * performed&quot;
	 */
    public static final String PARAMETER_MAX_RUNS = "max_runs";

    /** The parameter name for &quot;the maximal number of iterations performed for one run of the k method&quot; */
    public static final String PARAMETER_MAX_OPTIMIZATION_STEPS = "max_optimization_steps";

    public FastKMeans(OperatorDescription description) {
        super(description);
        getExampleSetInputPort().addPrecondition(new CapabilityPrecondition(this, getExampleSetInputPort()));
    }

    @Override
    public ClusterModel generateClusterModel(ExampleSet exampleSet) throws OperatorException {
        int k = getParameterAsInt(PARAMETER_K);
        int maxOptimizationSteps = getParameterAsInt(PARAMETER_MAX_OPTIMIZATION_STEPS);
        int maxRuns = getParameterAsInt(PARAMETER_MAX_RUNS);
        DistanceMeasure measure = new EuclideanDistance();
        measure.init(exampleSet);
        Tools.checkAndCreateIds(exampleSet);
        Tools.onlyNonMissingValues(exampleSet, "KMeans");
        if (exampleSet.size() < k) {
            throw new UserError(this, 142, k);
        }
        Attributes attributes = exampleSet.getAttributes();
        ArrayList<String> attributeNames = new ArrayList<String>(attributes.size());
        for (Attribute attribute : attributes) attributeNames.add(attribute.getName());
        RandomGenerator generator = RandomGenerator.getRandomGenerator(this);
        double minimalIntraClusterDistance = Double.POSITIVE_INFINITY;
        CentroidClusterModel bestModel = null;
        int[] bestAssignments = null;
        for (int iter = 0; iter < maxRuns; iter++) {
            checkForStop();
            CentroidClusterModel model = new CentroidClusterModel(exampleSet, k, attributeNames, measure, getParameterAsBoolean(RMAbstractClusterer.PARAMETER_ADD_AS_LABEL), getParameterAsBoolean(RMAbstractClusterer.PARAMETER_REMOVE_UNLABELED));
            int i = 0;
            for (Integer index : generator.nextIntSetWithRange(0, exampleSet.size(), k)) {
                model.assignExample(i, getAsDoubleArray(exampleSet.getExample(index), attributes));
                i++;
            }
            model.finishAssign();
            final double[][] l = new double[exampleSet.size()][k];
            final double[] u = new double[exampleSet.size()];
            final boolean[] r = new boolean[exampleSet.size()];
            final double[][] m_old = new double[k][attributes.size()];
            final double[] s = new double[k];
            final int[] centroidAssignments = new int[exampleSet.size()];
            final DistanceMatrix centroidDistances = new DistanceMatrix(k);
            computeClusterDistances(centroidDistances, s, model, measure);
            int x = 0;
            for (Example example : exampleSet) {
                double[] exampleValues = getAsDoubleArray(example, attributes);
                double nearestDistance = measure.calculateDistance(model.getCentroidCoordinates(0), exampleValues);
                l[x][0] = nearestDistance;
                int nearestIndex = 0;
                for (int centroidIndex = 1; centroidIndex < k; centroidIndex++) {
                    if (centroidDistances.get(nearestIndex, centroidIndex) >= 2 * nearestDistance) continue;
                    final double distance = measure.calculateDistance(model.getCentroidCoordinates(centroidIndex), exampleValues);
                    l[x][centroidIndex] = distance;
                    if (distance < nearestDistance) {
                        nearestDistance = distance;
                        nearestIndex = centroidIndex;
                    }
                }
                centroidAssignments[x] = nearestIndex;
                u[x] = nearestDistance;
                r[x] = false;
                x++;
            }
            boolean stable = false;
            for (int step = 0; (step < maxOptimizationSteps) && !stable; step++) {
                computeClusterDistances(centroidDistances, s, model, measure);
                int avoidedSamples = 0;
                x = 0;
                for (Example example : exampleSet) {
                    final double[] exampleValue = getAsDoubleArray(example, attributes);
                    if (u[x] <= s[centroidAssignments[x]]) {
                        avoidedSamples++;
                    } else {
                        for (int c = 0; c < k; c++) {
                            if (c != centroidAssignments[x] && u[x] > l[x][c] && u[x] > 0.5 * centroidDistances.get(centroidAssignments[x], c)) {
                                final double d_x_c;
                                if (r[x]) {
                                    d_x_c = measure.calculateDistance(exampleValue, model.getCentroidCoordinates(centroidAssignments[x]));
                                    l[x][centroidAssignments[x]] = d_x_c;
                                    u[x] = d_x_c;
                                    r[x] = false;
                                } else {
                                    d_x_c = u[x];
                                }
                                if (d_x_c > l[x][c] && d_x_c > 0.5 * centroidDistances.get(centroidAssignments[x], c)) {
                                    final double d_x_c_new = measure.calculateDistance(exampleValue, model.getCentroidCoordinates(c));
                                    l[x][c] = d_x_c_new;
                                    if (d_x_c_new < d_x_c) {
                                        centroidAssignments[x] = c;
                                        u[x] = d_x_c_new;
                                    }
                                }
                            }
                        }
                    }
                    model.assignExample(centroidAssignments[x], exampleValue);
                    x++;
                }
                for (int c = 0; c < k; c++) {
                    m_old[c] = model.getCentroidCoordinates(c);
                }
                stable = model.finishAssign();
                final double[] mean_distances = new double[k];
                for (int c = 0; c < k; c++) {
                    mean_distances[c] = measure.calculateDistance(m_old[c], model.getCentroidCoordinates(c));
                }
                for (x = 0; x < exampleSet.size(); x++) {
                    for (int c = 0; c < k; c++) {
                        final double d = l[x][c] - mean_distances[c];
                        if (d > 0) l[x][c] = d; else l[x][c] = 0;
                    }
                    u[x] = u[x] + mean_distances[centroidAssignments[x]];
                    r[x] = true;
                }
            }
            double distanceSum = 0;
            i = 0;
            for (Example example : exampleSet) {
                double distance = measure.calculateDistance(model.getCentroidCoordinates(centroidAssignments[i]), getAsDoubleArray(example, attributes));
                distanceSum += distance * distance;
                i++;
            }
            if (distanceSum < minimalIntraClusterDistance) {
                bestModel = model;
                minimalIntraClusterDistance = distanceSum;
                bestAssignments = centroidAssignments;
            }
        }
        bestModel.setClusterAssignments(bestAssignments, exampleSet);
        if (addsClusterAttribute()) {
            Attribute cluster = AttributeFactory.createAttribute("cluster", Ontology.NOMINAL);
            exampleSet.getExampleTable().addAttribute(cluster);
            exampleSet.getAttributes().setCluster(cluster);
            int i = 0;
            for (Example example : exampleSet) {
                example.setValue(cluster, "cluster_" + bestAssignments[i]);
                i++;
            }
        }
        return bestModel;
    }

    private void computeClusterDistances(DistanceMatrix centroidDistances, double[] s, CentroidClusterModel model, DistanceMeasure measure) {
        for (int i = 0; i < model.getNumberOfClusters(); i++) {
            s[i] = Double.POSITIVE_INFINITY;
        }
        for (int i = 0; i < model.getNumberOfClusters(); i++) {
            for (int j = i + 1; j < model.getNumberOfClusters(); j++) {
                final double d = measure.calculateDistance(model.getCentroidCoordinates(i), model.getCentroidCoordinates(j));
                if (d < s[i]) {
                    s[i] = d;
                }
                if (d < s[j]) {
                    s[j] = d;
                }
                centroidDistances.set(i, j, d);
            }
        }
        for (int i = 0; i < model.getNumberOfClusters(); i++) {
            s[i] = 0.5 * s[i];
        }
    }

    private double[] getAsDoubleArray(Example example, Attributes attributes) {
        double[] values = new double[attributes.size()];
        int i = 0;
        for (Attribute attribute : attributes) {
            values[i] = example.getValue(attribute);
            i++;
        }
        return values;
    }

    @Override
    public Class<? extends ClusterModel> getClusterModelClass() {
        return CentroidClusterModel.class;
    }

    @Override
    public boolean supportsCapability(OperatorCapability capability) {
        switch(capability) {
            case BINOMINAL_ATTRIBUTES:
            case POLYNOMINAL_ATTRIBUTES:
                return false;
            default:
                return true;
        }
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        types.add(new ParameterTypeInt(PARAMETER_K, "The number of clusters which should be detected.", 2, Integer.MAX_VALUE, 2, false));
        types.add(new ParameterTypeInt(PARAMETER_MAX_RUNS, "The maximal number of runs of k-Means with random initialization that are performed.", 1, Integer.MAX_VALUE, 10, false));
        types.add(new ParameterTypeInt(PARAMETER_MAX_OPTIMIZATION_STEPS, "The maximal number of iterations performed for one run of k-Means.", 1, Integer.MAX_VALUE, 100, false));
        types.addAll(RandomGenerator.getRandomGeneratorParameters(this));
        return types;
    }
}
