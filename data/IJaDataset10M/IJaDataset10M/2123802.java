package preprocessing.automatic.weka.classifiers.trees.j48;

import java.util.*;
import preprocessing.automatic.weka.core.*;

/**
 * Class for selecting a C4.5-like binary (!) split for a given dataset.
 *
 * @author Eibe Frank (eibe@cs.waikato.ac.nz)
 * @version $Revision: 1.8 $
 */
public class BinC45ModelSelection extends ModelSelection {

    /** Minimum number of instances in interval. */
    private int m_minNoObj;

    /** The FULL training dataset. */
    private Instances m_allData;

    /**
   * Initializes the split selection method with the given parameters.
   *
   * @param minNoObj minimum number of instances that have to occur in
   * at least two subsets induced by split
   * @param allData FULL training dataset (necessary for selection of
   * split points).  
   */
    public BinC45ModelSelection(int minNoObj, Instances allData) {
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
   * Selects C4.5-type split for the given dataset.
   */
    public final ClassifierSplitModel selectModel(Instances data) {
        double minResult;
        double currentResult;
        BinC45Split[] currentModel;
        BinC45Split bestModel = null;
        NoSplit noSplitModel = null;
        double averageInfoGain = 0;
        int validModels = 0;
        boolean multiVal = true;
        Distribution checkDistribution;
        double sumOfWeights;
        int i;
        try {
            checkDistribution = new Distribution(data);
            noSplitModel = new NoSplit(checkDistribution);
            if (Utils.sm(checkDistribution.total(), 2 * m_minNoObj) || Utils.eq(checkDistribution.total(), checkDistribution.perClass(checkDistribution.maxClass()))) return noSplitModel;
            Enumeration enu = data.enumerateAttributes();
            while (enu.hasMoreElements()) {
                Attribute attribute = (Attribute) enu.nextElement();
                if ((attribute.isNumeric()) || (Utils.sm((double) attribute.numValues(), (0.3 * (double) m_allData.numInstances())))) {
                    multiVal = false;
                    break;
                }
            }
            currentModel = new BinC45Split[data.numAttributes()];
            sumOfWeights = data.sumOfWeights();
            for (i = 0; i < data.numAttributes(); i++) {
                if (i != (data).classIndex()) {
                    currentModel[i] = new BinC45Split(i, m_minNoObj, sumOfWeights);
                    currentModel[i].buildClassifier(data);
                    if (currentModel[i].checkModel()) if ((data.attribute(i).isNumeric()) || (multiVal || Utils.sm((double) data.attribute(i).numValues(), (0.3 * (double) m_allData.numInstances())))) {
                        averageInfoGain = averageInfoGain + currentModel[i].infoGain();
                        validModels++;
                    }
                } else currentModel[i] = null;
            }
            if (validModels == 0) return noSplitModel;
            averageInfoGain = averageInfoGain / (double) validModels;
            minResult = 0;
            for (i = 0; i < data.numAttributes(); i++) {
                if ((i != (data).classIndex()) && (currentModel[i].checkModel())) if ((currentModel[i].infoGain() >= (averageInfoGain - 1E-3)) && Utils.gr(currentModel[i].gainRatio(), minResult)) {
                    bestModel = currentModel[i];
                    minResult = currentModel[i].gainRatio();
                }
            }
            if (Utils.eq(minResult, 0)) return noSplitModel;
            bestModel.distribution().addInstWithUnknown(data, bestModel.attIndex());
            bestModel.setSplitPoint(m_allData);
            return bestModel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
   * Selects C4.5-type split for the given dataset.
   */
    public final ClassifierSplitModel selectModel(Instances train, Instances test) {
        return selectModel(train);
    }
}
