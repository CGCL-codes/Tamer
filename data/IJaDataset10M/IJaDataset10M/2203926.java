package weka.classifiers.meta.leveltrees.evaluator;

import java.util.Enumeration;
import weka.classifiers.trees.j48.Distribution;
import weka.classifiers.trees.j48.InfoGainSplitCrit;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;

public class MaxNumericEvaluatorJ48 extends NumericEvaluator {

    private int m_MinNumInst = 0;

    public void handleNumericAttribute(Distribution distribution, Instances data, int attributeIndex) throws Exception {
        int firstMiss;
        int next = 1;
        int last = 0;
        int splitIndex = -1;
        double currentInfoGain;
        double defaultEnt;
        double minSplit;
        Instance instance;
        int i;
        int index = 0;
        double sumOfWeights = distribution.total();
        double infoGain = java.lang.Double.NEGATIVE_INFINITY;
        InfoGainSplitCrit infoGainCrit = new InfoGainSplitCrit();
        m_Distribution = new Distribution(2, data.numClasses());
        data.sort(data.attribute(attributeIndex));
        Enumeration<Instance> enu = data.enumerateInstances();
        i = 0;
        while (enu.hasMoreElements()) {
            instance = enu.nextElement();
            if (instance.isMissing(attributeIndex)) {
                break;
            }
            m_Distribution.add(1, instance);
            i++;
        }
        firstMiss = i;
        minSplit = 0.1 * (distribution.total()) / ((double) data.numClasses());
        if (Utils.smOrEq(minSplit, m_MinNumInst)) {
            minSplit = m_MinNumInst;
        } else {
            if (Utils.gr(minSplit, 25)) {
                minSplit = 25;
            }
        }
        defaultEnt = infoGainCrit.oldEnt(m_Distribution);
        while (next < firstMiss) {
            if ((data.instance(next - 1).value(attributeIndex) + 1e-5) < data.instance(next).value(attributeIndex)) {
                m_Distribution.shiftRange(1, 0, data, last, next);
                currentInfoGain = infoGainCrit.splitCritValue(m_Distribution, sumOfWeights, defaultEnt);
                if (Utils.gr(currentInfoGain, infoGain)) {
                    infoGain = currentInfoGain;
                    splitIndex = next - 1;
                }
                index++;
                last = next;
            }
            next++;
        }
        if (index == 0) {
            throw new Exception("failed test in MaxNumericEvaluatorJ48");
        }
        m_SplitPoint = (data.instance(splitIndex + 1).value(attributeIndex) + data.instance(splitIndex).value(attributeIndex)) / 2;
        if (m_SplitPoint == data.instance(splitIndex + 1).value(attributeIndex)) {
            m_SplitPoint = data.instance(splitIndex).value(attributeIndex);
        }
        m_Distribution = new Distribution(3, data.numClasses());
        m_Distribution.addRange(0, data, 0, splitIndex + 1);
        m_Distribution.addRange(1, data, splitIndex + 1, firstMiss);
        m_Distribution.addRange(2, data, firstMiss, data.numInstances());
    }

    public boolean singleBranchTest(Distribution distribution, Instances data, int attributeIndex, int minNumObj) {
        Enumeration<Instance> enu = data.enumerateInstances();
        int firstMiss = 0;
        data.sort(data.attribute(attributeIndex));
        double same = data.instance(0).value(attributeIndex);
        int sameCount = -1;
        for (int i = 0; enu.hasMoreElements(); i++) {
            Instance instance = enu.nextElement();
            if (instance.isMissing(attributeIndex)) {
                break;
            }
            if (Utils.eq(data.instance(i).value(attributeIndex), same)) {
                sameCount++;
            }
            firstMiss++;
        }
        if (sameCount == (firstMiss - 1)) {
            return true;
        }
        double minSplit = 0.1 * (distribution.total()) / ((double) data.numClasses());
        if (Utils.smOrEq(minSplit, minNumObj)) {
            minSplit = minNumObj;
        } else {
            if (Utils.gr(minSplit, 25)) {
                minSplit = 25;
            }
        }
        if (Utils.sm((double) firstMiss, 2 * minSplit)) {
            return true;
        }
        return false;
    }

    public int whichSubset(Instance data, int attributeIndex) {
        if (data.isMissing(attributeIndex)) {
            return 2;
        } else {
            if (Utils.smOrEq(data.value(attributeIndex), m_SplitPoint)) {
                return 0;
            } else {
                return 1;
            }
        }
    }
}
