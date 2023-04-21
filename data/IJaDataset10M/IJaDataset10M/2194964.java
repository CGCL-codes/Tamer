package weka.classifiers.trees.j48;

import weka.core.Attribute;
import weka.core.Instances;
import weka.core.RevisionUtils;
import weka.core.Utils;
import java.util.Enumeration;

/**
 * Class for selecting a NB tree split.
 *
 * @author Mark Hall (mhall@cs.waikato.ac.nz)
 * @version $Revision: 1.5 $
 */
public class NBTreeModelSelection extends ModelSelection {

    /** for serialization */
    private static final long serialVersionUID = 990097748931976704L;

    /** Minimum number of objects in interval. */
    private int m_minNoObj;

    /** All the training data */
    private Instances m_allData;

    /**
   * Initializes the split selection method with the given parameters.
   *
   * @param minNoObj minimum number of instances that have to occur in at least two
   * subsets induced by split
   * @param allData FULL training dataset (necessary for
   * selection of split points).
   */
    public NBTreeModelSelection(int minNoObj, Instances allData) {
        m_minNoObj = minNoObj;
        m_allData = allData;
    }

    /**
   * Sets reference to training data to null.
   */
    public void cleanup() {
        m_allData = null;
    }

    /**
   * Selects NBTree-type split for the given dataset.
   */
    public final ClassifierSplitModel selectModel(Instances data) {
        double globalErrors = 0;
        double minResult;
        double currentResult;
        NBTreeSplit[] currentModel;
        NBTreeSplit bestModel = null;
        NBTreeNoSplit noSplitModel = null;
        int validModels = 0;
        boolean multiVal = true;
        Distribution checkDistribution;
        Attribute attribute;
        double sumOfWeights;
        int i;
        try {
            noSplitModel = new NBTreeNoSplit();
            noSplitModel.buildClassifier(data);
            if (data.numInstances() < 5) {
                return noSplitModel;
            }
            globalErrors = noSplitModel.getErrors();
            if (globalErrors == 0) {
                return noSplitModel;
            }
            checkDistribution = new Distribution(data);
            if (Utils.sm(checkDistribution.total(), m_minNoObj) || Utils.eq(checkDistribution.total(), checkDistribution.perClass(checkDistribution.maxClass()))) {
                return noSplitModel;
            }
            if (m_allData != null) {
                Enumeration enu = data.enumerateAttributes();
                while (enu.hasMoreElements()) {
                    attribute = (Attribute) enu.nextElement();
                    if ((attribute.isNumeric()) || (Utils.sm((double) attribute.numValues(), (0.3 * (double) m_allData.numInstances())))) {
                        multiVal = false;
                        break;
                    }
                }
            }
            currentModel = new NBTreeSplit[data.numAttributes()];
            sumOfWeights = data.sumOfWeights();
            for (i = 0; i < data.numAttributes(); i++) {
                if (i != (data).classIndex()) {
                    currentModel[i] = new NBTreeSplit(i, m_minNoObj, sumOfWeights);
                    currentModel[i].setGlobalModel(noSplitModel);
                    currentModel[i].buildClassifier(data);
                    if (currentModel[i].checkModel()) {
                        validModels++;
                    }
                } else {
                    currentModel[i] = null;
                }
            }
            if (validModels == 0) {
                return noSplitModel;
            }
            minResult = globalErrors;
            for (i = 0; i < data.numAttributes(); i++) {
                if ((i != (data).classIndex()) && (currentModel[i].checkModel())) {
                    if (currentModel[i].getErrors() < minResult) {
                        bestModel = currentModel[i];
                        minResult = currentModel[i].getErrors();
                    }
                }
            }
            if (((globalErrors - minResult) / globalErrors) < 0.05) {
                return noSplitModel;
            }
            return bestModel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
   * Selects NBTree-type split for the given dataset.
   */
    public final ClassifierSplitModel selectModel(Instances train, Instances test) {
        return selectModel(train);
    }

    /**
   * Returns the revision string.
   * 
   * @return		the revision
   */
    public String getRevision() {
        return RevisionUtils.extract("$Revision: 1.5 $");
    }
}
