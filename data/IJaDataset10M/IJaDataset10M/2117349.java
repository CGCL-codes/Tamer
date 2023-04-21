package mulan.classifier.meta;

import java.util.logging.Level;
import java.util.logging.Logger;
import mulan.classifier.InvalidDataException;
import mulan.classifier.MultiLabelLearner;
import mulan.classifier.MultiLabelOutput;
import mulan.data.MultiLabelInstances;
import mulan.transformations.RemoveAllLabels;
import weka.clusterers.Clusterer;
import weka.core.*;
import weka.core.TechnicalInformation.Field;
import weka.core.TechnicalInformation.Type;

/**
 *
 * <!-- globalinfo-start -->
 *
 * <pre>
 * Class implementing the CBMLC (Clustering-Based Multi-label Classification) algorithm.
 * </pre>
 *
 * For more information:
 *
 * <pre>
 * Nasierding, G., Tsoumakas, G., Kouzani, Z. A., (2009) "Clustering Based Multi-Label Classification
 * for Image Annotation and Retrieval", Proc. 2009 IEEE International Conference on Systems, Man, and Cybernetics (SMC 2009),
 * pp. -----, Texas, USA, 11-14 October 2009.
 * </pre>
 *
 * <!-- globalinfo-end -->
 *
 * <!-- technical-bibtex-start --> BibTeX:
 *
 * <pre>
 * &#--;inproceedings{nasierding+tsoumakas+kouzani:2009,
 *    author =    {Nasierding, G., Tsoumakas, G. and Kouzani, Z. A.},
 *    title =     {Clustering Based Multi-Label Classification for Image Annotation and Retrieval},
 *    booktitle = {Proceedings of the 2009 IEEE International Conference on Systems, Man, and Cybernetics (SMC 2009)},
 *    year =      {2009},
 *    pages =     {-----},
 *    address =   {Texas, USA},
 *    month =     {11-14 October},
 * }
 * </pre>
 *
 * <p/> <!-- technical-bibtex-end -->
 *
 *
 * @author  Gulisong Nasierding
 * @author  Grigorios Tsoumakas
 *
 * @version $Revision: 0.02 $
 */
public class ClusteringBased extends MultiLabelMetaLearner {

    /** The number of clusters */
    private int numClusters;

    /** The multi-label learners, one for each cluster */
    private MultiLabelLearner[] multi;

    /** The clusterer to use */
    private Clusterer clusterer;

    /** The multi-label learner class to use */
    private MultiLabelLearner mlc;

    public ClusteringBased(Clusterer aClusterer, MultiLabelLearner aMultiLabelClassifier) throws Exception {
        super();
        clusterer = aClusterer;
        mlc = aMultiLabelClassifier;
    }

    public Clusterer getClusterer() {
        return clusterer;
    }

    @Override
    public void buildInternal(MultiLabelInstances trainData) throws Exception {
        Instances trainInstances = trainData.getDataSet();
        Instances removedInstances = RemoveAllLabels.transformInstances(trainData);
        clusterer.buildClusterer(removedInstances);
        numClusters = clusterer.numberOfClusters();
        MultiLabelInstances[] subsetMultiLabelInstances = new MultiLabelInstances[numClusters];
        Instances[] subsetInstances = new Instances[numClusters];
        for (int i = 0; i < numClusters; i++) {
            subsetInstances[i] = new Instances(trainInstances, 0);
            subsetMultiLabelInstances[i] = new MultiLabelInstances(subsetInstances[i], trainData.getLabelsMetaData());
        }
        for (int i = 0; i < trainInstances.numInstances(); i++) {
            int clusterOfInstance = clusterer.clusterInstance(removedInstances.instance(i));
            subsetMultiLabelInstances[clusterOfInstance].getDataSet().add(trainInstances.instance(i));
        }
        multi = new MultiLabelLearner[numClusters];
        for (int i = 0; i < numClusters; i++) {
            try {
                multi[i] = mlc.makeCopy();
                multi[i].build(subsetMultiLabelInstances[i]);
            } catch (Exception ex) {
                Logger.getLogger(ClusteringBased.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    protected MultiLabelOutput makePredictionInternal(Instance instance) throws Exception, InvalidDataException {
        Instance newInstance = RemoveAllLabels.transformInstance(instance, labelIndices);
        int cluster = clusterer.clusterInstance(newInstance);
        return multi[cluster].makePrediction(instance);
    }

    @Override
    public TechnicalInformation getTechnicalInformation() {
        TechnicalInformation result = new TechnicalInformation(Type.INPROCEEDINGS);
        result.setValue(Field.AUTHOR, "Gulisong Nasierding, Grigorios Tsoumakas, Abbas Kouzani");
        result.setValue(Field.TITLE, "Clustering Based Multi-Label Classification for Image Annotation and Retrieval");
        result.setValue(Field.BOOKTITLE, "Proc. 2009 IEEE International Conference on Systems, Man, and Cybernetics (SMC 2009)");
        result.setValue(Field.LOCATION, "Texas, USA");
        result.setValue(Field.YEAR, "2009");
        return result;
    }
}
