package de.dfki.madm.operator.clustering;

import java.util.ArrayList;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.Tools;
import com.rapidminer.example.set.SplittedExampleSet;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.UserError;
import com.rapidminer.operator.clustering.Centroid;
import com.rapidminer.operator.clustering.CentroidClusterModel;
import com.rapidminer.operator.clustering.Cluster;
import com.rapidminer.operator.clustering.ClusterModel;
import com.rapidminer.operator.clustering.clusterer.FastKMeans;
import com.rapidminer.operator.clustering.clusterer.KMeans;
import com.rapidminer.operator.clustering.clusterer.RMAbstractClusterer;
import com.rapidminer.tools.Ontology;
import com.rapidminer.tools.math.similarity.DistanceMeasure;
import de.dfki.madm.operator.KMeanspp;

public class XMeansCore extends RMAbstractClusterer {

    private ExampleSet exampleSet = null;

    private int examplesize = -1;

    private DistanceMeasure measure = null;

    private int k_min = -1;

    private int k_max = -1;

    private boolean kpp = false;

    private int maxOptimizationSteps = -1;

    private int maxRuns = -1;

    private OperatorDescription description = null;

    private Attributes attributes = null;

    private int dimension = -1;

    private int[] centroidAssignments = null;

    private String ClusteringAlgorithm = "";

    /**
	 * Initialization of X-Mean
	 * 
	 * @param eSet ExamleSet to cluster
	 * @param k_min minimal number of cluster
	 * @param k_max maximal number of cluster
	 * @param kpp using K++-Algorithem to determin the first centroids
	 * @param maxOptimizationSteps maximal optimationsteps of k-Means
	 * @param maxRuns The maximal number of runs of k-Means with random initialization that are performed.
	 * @param description
	 * @param measure MeasureType to use
	 * @param cluster_alg Clustering Algorithm to use
	 */
    public XMeansCore(ExampleSet eSet, int k_min, int k_max, boolean kpp, int maxOptimizationSteps, int maxRuns, OperatorDescription description, DistanceMeasure measure, String cluster_alg) {
        super(description);
        this.exampleSet = eSet;
        this.measure = measure;
        this.k_max = k_max;
        this.k_min = k_min;
        this.kpp = kpp;
        this.maxOptimizationSteps = maxOptimizationSteps;
        this.maxRuns = maxRuns;
        this.description = description;
        this.centroidAssignments = new int[exampleSet.size()];
        this.ClusteringAlgorithm = cluster_alg;
    }

    /**
	 * Running X-Means Algorithm
	 * 
	 * @return Clustered Model
	 * @throws OperatorException
	 */
    public ClusterModel doXMean() throws OperatorException {
        examplesize = exampleSet.size();
        measure.init(exampleSet);
        Tools.checkAndCreateIds(exampleSet);
        Tools.onlyNonMissingValues(exampleSet, "KMeans");
        if (exampleSet.size() < k_min) {
            throw new UserError(this, 142, k_min);
        }
        attributes = exampleSet.getAttributes();
        ArrayList<String> attributeNames = new ArrayList<String>(attributes.size());
        for (Attribute attribute : attributes) attributeNames.add(attribute.getName());
        CentroidClusterModel bestModel = null;
        RMAbstractClusterer KMean = null;
        if (this.ClusteringAlgorithm.equals("FastKMeans")) {
            KMean = new FastKMeans(description);
            ((FastKMeans) KMean).setPresetMeasure(measure);
        } else if (this.ClusteringAlgorithm.equals("KMeans")) {
            KMean = new KMeans(description);
            ((KMeans) KMean).setPresetMeasure(measure);
        }
        KMean.setParameter("k", k_min + "");
        KMean.setParameter("max_runs", maxRuns + "");
        KMean.setParameter("max_optimization_steps", maxOptimizationSteps + "");
        KMean.setParameter(KMeanspp.PARAMETER_USE_KPP, kpp + "");
        bestModel = (CentroidClusterModel) KMean.generateClusterModel(exampleSet);
        dimension = bestModel.getCentroid(0).getCentroid().length;
        double current_m_BIC = this.calcBIC(bestModel);
        boolean change = true;
        while (bestModel.getCentroids().size() < k_max && change) {
            change = false;
            int array_size = bestModel.getClusters().size();
            CentroidClusterModel[] Children = new CentroidClusterModel[array_size];
            CentroidClusterModel[] Parent = new CentroidClusterModel[array_size];
            SplittedExampleSet splittedSet = SplittedExampleSet.splitByAttribute(exampleSet, exampleSet.getAttributes().get("cluster"));
            if (splittedSet.getNumberOfSubsets() < array_size) {
                break;
            }
            int anz = 0;
            for (@SuppressWarnings("unused") Cluster cl : bestModel.getClusters()) {
                splittedSet.selectSingleSubset(anz);
                KMean.setParameter("k", 2 + "");
                Children[anz] = (CentroidClusterModel) KMean.generateClusterModel(splittedSet);
                KMean.setParameter("k", 1 + "");
                Parent[anz] = (CentroidClusterModel) KMean.generateClusterModel(splittedSet);
                anz++;
            }
            Double[] SaveDiffBic = new Double[array_size];
            boolean[] takeChange = new boolean[array_size];
            int change_anz = 0;
            for (int i = 0; i < Parent.length; i++) {
                double BICc = calcBIC(Children[i]);
                double BICp = calcBIC(Parent[i]);
                if (BICc > BICp) {
                    takeChange[i] = true;
                    SaveDiffBic[i] = (BICc - BICp);
                    change_anz++;
                } else {
                    takeChange[i] = false;
                }
            }
            CentroidClusterModel model = null;
            if ((change_anz + array_size) < k_max) {
                model = new CentroidClusterModel(exampleSet, (change_anz + array_size), attributeNames, measure, getParameterAsBoolean(RMAbstractClusterer.PARAMETER_ADD_AS_LABEL), getParameterAsBoolean(RMAbstractClusterer.PARAMETER_REMOVE_UNLABELED));
                int id = 0;
                for (int i = 0; i < array_size; i++) {
                    if (takeChange[i]) {
                        for (Centroid z : Children[i].getCentroids()) {
                            model.assignExample(id, z.getCentroid());
                            id++;
                        }
                    } else {
                        model.assignExample(id, Parent[i].getCentroid(0).getCentroid());
                        id++;
                    }
                }
            } else {
                model = new CentroidClusterModel(exampleSet, k_max, attributeNames, measure, getParameterAsBoolean(RMAbstractClusterer.PARAMETER_ADD_AS_LABEL), getParameterAsBoolean(RMAbstractClusterer.PARAMETER_REMOVE_UNLABELED));
                double hilf = 0;
                CentroidClusterModel hilf2 = null;
                for (int i = 0; i < (takeChange.length - 1); i++) {
                    for (int j = (i + 1); j < takeChange.length; j++) {
                        if (SaveDiffBic[j] > SaveDiffBic[i]) {
                            hilf = SaveDiffBic[j];
                            SaveDiffBic[j] = SaveDiffBic[i];
                            SaveDiffBic[i] = hilf;
                            hilf2 = Children[j];
                            Children[j] = Children[i];
                            Children[i] = hilf2;
                            hilf2 = Parent[j];
                            Parent[j] = Parent[i];
                            Parent[i] = hilf2;
                        }
                    }
                }
                int id = 0;
                int anz1 = 0;
                for (int i = 0; i < array_size; i++) {
                    if (takeChange[i]) {
                        for (Centroid z : Children[i].getCentroids()) {
                            model.assignExample(id, z.getCentroid());
                            id++;
                            anz1++;
                        }
                    } else {
                        model.assignExample(id, Parent[i].getCentroid(0).getCentroid());
                        id++;
                        anz1++;
                    }
                    if (anz1 >= k_max) break;
                }
            }
            model.finishAssign();
            model = this.assinePoints(model);
            double new_m_BIC = calcBIC(model);
            if (new_m_BIC > current_m_BIC) {
                change = true;
                bestModel = model;
                current_m_BIC = new_m_BIC;
            } else {
                model = null;
            }
        }
        if (addsClusterAttribute()) {
            Attribute cluster = AttributeFactory.createAttribute("cluster", Ontology.NOMINAL);
            exampleSet.getExampleTable().addAttribute(cluster);
            exampleSet.getAttributes().setCluster(cluster);
            int i = 0;
            for (Example example : exampleSet) {
                example.setValue(cluster, "cluster_" + centroidAssignments[i]);
                i++;
            }
        }
        return bestModel;
    }

    /**
	 * assign the Points to cluster
	 * 
	 * @param model 
	 * @return
	 */
    private CentroidClusterModel assinePoints(CentroidClusterModel model) {
        double[] values = new double[attributes.size()];
        int i = 0;
        for (Example example : exampleSet) {
            double[] exampleValues = getAsDoubleArray(example, attributes, values);
            double nearestDistance = measure.calculateDistance(model.getCentroidCoordinates(0), exampleValues);
            int nearestIndex = 0;
            int id = 0;
            for (Centroid cr : model.getCentroids()) {
                double distance = measure.calculateDistance(cr.getCentroid(), exampleValues);
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestIndex = id;
                }
                id++;
            }
            centroidAssignments[i] = nearestIndex;
            i++;
        }
        model.setClusterAssignments(centroidAssignments, exampleSet);
        return model;
    }

    /**
	 * Calculate the BIC like in the paper by Dan Pelleg and Andrew Moore
	 * 
	 * @param bestModel
	 * @return BIC of the given modell
	 */
    private double calcBIC(CentroidClusterModel bestModel) {
        double loglike = 0;
        int numCenters = bestModel.getNumberOfClusters();
        int numDimensions = bestModel.getCentroidCoordinates(0).length;
        int numParameters = (numCenters - 1) + numCenters * numDimensions + numCenters;
        for (Cluster c : bestModel.getClusters()) {
            int current_id = c.getClusterId();
            loglike += logLikelihoodEstimate(c, bestModel.getCentroidCoordinates(current_id), bestModel.getClusterAssignments(exampleSet), numCenters);
        }
        loglike -= (numParameters / 2.0) * Math.log(examplesize);
        return loglike;
    }

    private double[] getAsDoubleArray(Example example, Attributes attributes, double[] values) {
        int i = 0;
        for (Attribute attribute : attributes) {
            values[i] = example.getValue(attribute);
            i++;
        }
        return values;
    }

    private double logLikelihoodEstimate(Cluster c, double[] centroid, int[] as, int K) {
        double l = 0;
        double R = examplesize;
        double Rn = c.getNumberOfExamples();
        double M = dimension;
        double d = 0;
        double[] values = new double[attributes.size()];
        if (Rn > 1) {
            double sum = 0;
            final Attribute idAttribute = exampleSet.getAttributes().getId();
            boolean idIsNominal = idAttribute.isNominal();
            exampleSet.remapIds();
            for (Object ob : c.getExampleIds()) {
                Example example;
                if (idIsNominal) {
                    example = exampleSet.getExampleFromId(idAttribute.getMapping().mapString((String) ob));
                } else {
                    example = exampleSet.getExampleFromId(((Double) ob).intValue());
                }
                if (example == null) {
                    throw new RuntimeException("Unknown id: " + ob);
                }
                sum += Math.pow((measure.calculateDistance(centroid, getAsDoubleArray(example, attributes, values))), 2);
            }
            d = (1.0 / (Rn - K)) * sum;
            l = -(Rn / 2.0) * Math.log(2.0 * Math.PI) - ((Rn * M) / 2.0) * Math.log(d) - (Rn - K) / 2.0 + Rn * Math.log(Rn) - Rn * Math.log(R);
        }
        return l;
    }

    @Override
    public ClusterModel generateClusterModel(ExampleSet exampleSet) throws OperatorException {
        return null;
    }
}
