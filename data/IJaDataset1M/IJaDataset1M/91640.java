package sssvm;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import libsvm.svm_node;
import sssvm.clustermodel.SSSVMClusterModelHandler;
import sssvm.rapidminer.SSSVMLibSVMModel;
import sssvm.sampling.SamplingStrategy;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.FastExample2SparseTransform;
import com.rapidminer.example.set.SimpleExampleSet;
import com.rapidminer.example.table.DataRow;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.operator.ContainerModel;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.learner.clustering.AbstractClusterModel;
import com.rapidminer.operator.learner.functions.kernel.KernelModel;

public class SSSVMLearner {

    private String svmLearner;

    private String validator;

    private String perfEvaluator;

    private List<String> clusterers;

    private ParameterWrapper params;

    private ParameterWrapper clusterParams;

    private RuntimeHandler runtime;

    private Map<String, SSSVMClusterModelHandler> clusterModelHandler;

    private List<SamplingStrategy> samplingStrategies = new LinkedList<SamplingStrategy>();

    private List<DataRow> dataRows;

    public List<SamplingStrategy> getSamplingStrategies() {
        return samplingStrategies;
    }

    public void setSamplingStrategies(final List<SamplingStrategy> confidenceHandler) {
        samplingStrategies = confidenceHandler;
    }

    /**
	 * Returns a ExampleSet containing the instance, which should be labeled by
	 * a human expert (semi-supervised learning). The labeledExampleSet is used
	 * by a svm to predict confidences and to use probabilty-based
	 * semi-supervised learning approaches (breaking ties, ...). The
	 * unlabeledExampleSet is used by clusterer to use cluster-based
	 * semi-supervised learning approaches (kmeans, ...).
	 * 
	 * @throws Exception
	 */
    public ExampleSet queryInstances(final ExampleSet labeledExampleSet, final ExampleSet unlabeledExampleSet) throws Exception {
        dataRows = new LinkedList<DataRow>();
        final SVMLearner learner = runtime.getSVMLearner();
        learner.setParams(params);
        final IOObject[] result = learner.learn(svmLearner, perfEvaluator, labeledExampleSet);
        final KernelModel model = ((ContainerModel) result[0]).getModel(KernelModel.class);
        final SSSVMLibSVMModel m = (SSSVMLibSVMModel) model;
        final ExampleSet predictedSet = model.apply(unlabeledExampleSet);
        queryUsingConfidenceHandler(predictedSet, (SSSVMLibSVMModel) model);
        queryUsingClusterer(unlabeledExampleSet, predictedSet.getAttributes());
        Collections.sort(dataRows, new Comparator<DataRow>() {

            public int compare(final DataRow o1, final DataRow o2) {
                return (int) (o1.get(predictedSet.getAttributes().getId()) - o1.get(predictedSet.getAttributes().getId()));
            }
        });
        final MemoryExampleTable table = ExampleSetUtils.createMemoryExampleTable(predictedSet);
        for (final Object element : dataRows) {
            final DataRow row = (DataRow) element;
            table.addDataRow(row);
        }
        final SimpleExampleSet se = new SimpleExampleSet(table);
        se.getAttributes().setLabel(predictedSet.getAttributes().getLabel());
        se.getAttributes().setId(predictedSet.getAttributes().getId());
        se.getAttributes().setPredictedLabel(predictedSet.getAttributes().getPredictedLabel());
        if (se.getAttributes().getId() != null && labeledExampleSet.getAttributes().getId() != null && se.getAttributes().getId().getTableIndex() != labeledExampleSet.getAttributes().getId().getTableIndex()) {
            System.out.println();
        }
        return se;
    }

    private void queryUsingConfidenceHandler(final ExampleSet predictedSet, final SSSVMLibSVMModel model) {
        for (final SamplingStrategy strategy : samplingStrategies) {
            strategy.query(dataRows, predictedSet, model);
        }
    }

    private void queryUsingClusterer(final ExampleSet unlabeledExampleSet, final Attributes attrs) throws Exception {
        final List<Attribute> attributes = new LinkedList<Attribute>();
        for (final Attribute att : attrs) {
            attributes.add(att);
        }
        for (final String string : clusterers) {
            final String cluster = string;
            final AbstractClusterModel clusterModel = clusterSet(cluster, unlabeledExampleSet, clusterParams);
            getClusterModelHandler().get(clusterModel.getClass().getCanonicalName()).handle(dataRows, attributes, clusterModel, unlabeledExampleSet);
        }
    }

    private AbstractClusterModel clusterSet(final String cluster, final ExampleSet unlabeledExampleSet, final ParameterWrapper clusterParams) throws Exception {
        final Clusterer clusterer = runtime.getClusterer();
        final IOObject[] result = clusterer.cluster(cluster, clusterParams.getParameters(), unlabeledExampleSet);
        return (AbstractClusterModel) result[0];
    }

    public void resetLearner() {
        dataRows.clear();
    }

    public void addSamplingStrategy(final SamplingStrategy ch) {
        samplingStrategies.add(ch);
    }

    public String getSvmLearner() {
        return svmLearner;
    }

    public void setSvmLearner(final String svmLearner) {
        this.svmLearner = svmLearner;
    }

    public String getValidator() {
        return validator;
    }

    public void setValidator(final String validator) {
        this.validator = validator;
    }

    public String getPerfEvaluator() {
        return perfEvaluator;
    }

    public void setPerfEvaluator(final String perfEvaluator) {
        this.perfEvaluator = perfEvaluator;
    }

    public List<String> getClusterers() {
        return clusterers;
    }

    public void setClusterers(final List<String> clusterers) {
        this.clusterers = clusterers;
    }

    public Map<String, SSSVMClusterModelHandler> getClusterModelHandler() {
        return clusterModelHandler;
    }

    public void setClusterModelHandler(final Map<String, SSSVMClusterModelHandler> clusterModelHandler) {
        this.clusterModelHandler = clusterModelHandler;
    }

    public RuntimeHandler getRuntime() {
        return runtime;
    }

    public void setRuntime(final RuntimeHandler runtime) {
        this.runtime = runtime;
    }

    public ParameterWrapper getParams() {
        return params;
    }

    public void setParams(final ParameterWrapper params) {
        this.params = params;
    }

    public ParameterWrapper getClusterParams() {
        return clusterParams;
    }

    public void setClusterParams(final ParameterWrapper clusterParams) {
        this.clusterParams = clusterParams;
    }

    protected static svm_node[] makeNodes(final Example e, final FastExample2SparseTransform ripper) {
        final int[] nonDefaultIndices = ripper.getNonDefaultAttributeIndices(e);
        final double[] nonDefaultValues = ripper.getNonDefaultAttributeValues(e, nonDefaultIndices);
        final svm_node[] nodeArray = new svm_node[nonDefaultIndices.length];
        for (int a = 0; a < nonDefaultIndices.length; a++) {
            final svm_node node = new svm_node();
            node.index = nonDefaultIndices[a];
            node.value = nonDefaultValues[a];
            nodeArray[a] = node;
        }
        return nodeArray;
    }
}
