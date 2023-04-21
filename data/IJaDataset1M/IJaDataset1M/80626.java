package weka.classifiers.meta.leveltrees;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.meta.leveltrees.evaluator.Evaluator;
import weka.classifiers.meta.leveltrees.evaluator.MaxNumericEvaluatorJ48;
import weka.classifiers.meta.leveltrees.evaluator.NominalEvaluator;
import weka.classifiers.meta.leveltrees.evaluator.NumericEvaluator;
import weka.classifiers.meta.leveltrees.evaluator.RandomNumericEvaluator;
import weka.classifiers.meta.leveltrees.selector.AttributeSelector;
import weka.classifiers.trees.j48.Distribution;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.WekaException;

public class LevelTreeNode implements Serializable {

    static final byte FLG_LEAF = 0x01;

    static final byte FLG_LEVEL_TREE = 0x02;

    static final byte FLG_NUMERIC = 0x04;

    static final byte FLG_PURE = 0x08;

    private int m_AttributeIndex = 0;

    private Classifier m_Classifier = null;

    private byte m_Flags = 0x00;

    private NominalEvaluator m_NominalEvaluator = null;

    private NumericEvaluator m_NumericEvaluator = null;

    protected LevelTreeNode[] m_Children = null;

    protected Distribution m_Distribution = null;

    public void buildClassifier(Instances data, NodeParameters nodeParameters) throws Exception {
        if (!buildTree(data, nodeParameters, 0)) {
            buildTree(data, nodeParameters);
        }
    }

    protected boolean buildTree(Instances data, NodeParameters nodeParameters, int level) throws Exception {
        m_Flags = ((byte) 0x00);
        m_NominalEvaluator = (NominalEvaluator) Evaluator.makeCopy(nodeParameters.m_NomimalEvaluator);
        m_NumericEvaluator = (NumericEvaluator) Evaluator.makeCopy(nodeParameters.m_NumericEvaluator);
        m_Flags &= ~(FLG_LEAF | FLG_PURE);
        m_Flags |= FLG_LEVEL_TREE;
        m_Distribution = new Distribution(data);
        if (Utils.eq(m_Distribution.total(), m_Distribution.perClass(m_Distribution.maxClass()))) {
            m_Flags |= (FLG_LEAF | FLG_PURE);
            return true;
        }
        if (Utils.sm(data.numInstances(), nodeParameters.m_MinNumInstances * 2)) {
            m_Flags |= FLG_LEAF;
            return true;
        }
        int startLevel = level;
        ArrayList<Integer> attributeIndexes = new ArrayList<Integer>(data.numAttributes());
        for (; ; ) {
            m_AttributeIndex = nodeParameters.m_AttributeSelector.select(level);
            if (m_AttributeIndex == -1) {
                m_Distribution = new Distribution(data);
                m_AttributeIndex = nodeParameters.m_AttributeSelector.select(startLevel);
                m_Flags |= FLG_LEAF;
                for (int i = 0; i < attributeIndexes.size(); i++) {
                    Integer value = attributeIndexes.get(i);
                    nodeParameters.m_AttributeSelector.addToSet(value.intValue());
                }
                return false;
            }
            if (data.attribute(m_AttributeIndex).isNumeric() && !m_NumericEvaluator.singleBranchTest(m_Distribution, data, m_AttributeIndex, nodeParameters.m_MinNumInstances)) {
                break;
            }
            if (!singleBranchTest(data)) {
                break;
            }
            if (!data.attribute(m_AttributeIndex).isNominal()) {
                if (nodeParameters.m_AttributeSelector.removeFromSet(m_AttributeIndex)) {
                    attributeIndexes.add(new Integer(m_AttributeIndex));
                }
            }
            level++;
        }
        if (data.attribute(m_AttributeIndex).isNominal()) {
            m_Flags &= ~FLG_NUMERIC;
            m_NominalEvaluator.handleNominalAttribute(m_Distribution, data, m_AttributeIndex);
            m_Distribution = m_NominalEvaluator.getDistribution();
        } else {
            m_Flags |= FLG_NUMERIC;
            m_NumericEvaluator.handleNumericAttribute(m_Distribution, data, m_AttributeIndex);
            m_Distribution = m_NumericEvaluator.getDistribution();
        }
        Instances[] localInstances = split(data);
        m_Children = new LevelTreeNode[localInstances.length];
        for (int i = 0; i < localInstances.length; i++) {
            m_Children[i] = (localInstances[i] == null) ? null : getNewTree(localInstances[i], nodeParameters, level);
        }
        for (int i = 0; i < attributeIndexes.size(); i++) {
            Integer value = (Integer) attributeIndexes.get(i);
            nodeParameters.m_AttributeSelector.addToSet(value.intValue());
        }
        return true;
    }

    public double[] getProbs(Instance instance) throws Exception {
        if ((m_Flags & FLG_LEVEL_TREE) == FLG_LEVEL_TREE) {
            if ((m_Flags & FLG_LEAF) == FLG_LEAF) {
                return classProb(instance, -1);
            } else {
                int treeIndex = whichSubset(instance);
                if (m_Children[treeIndex] == null) {
                    return classProb(instance, treeIndex);
                } else {
                    return m_Children[treeIndex].getProbs(instance);
                }
            }
        } else {
            return m_Classifier.distributionForInstance(instance);
        }
    }

    protected LevelTreeNode getNewTree(Instances data, NodeParameters nodeParameters, int level) throws Exception {
        LevelTreeNode newTree = new LevelTreeNode();
        if (!newTree.buildTree(data, nodeParameters, level + 1)) {
            newTree.buildTree(data, nodeParameters);
        }
        return newTree;
    }

    private void buildTree(Instances data, NodeParameters nodeParameters) throws Exception {
        m_Flags &= ~(FLG_LEAF | FLG_LEVEL_TREE | FLG_PURE);
        m_Classifier = AbstractClassifier.makeCopy(nodeParameters.m_Classifier);
        m_Classifier.buildClassifier(data);
    }

    private double[] classProb(Instance instance, int theSubset) throws Exception {
        double[] probs = new double[instance.numClasses()];
        for (int i = 0; i < instance.numClasses(); i++) {
            if (theSubset > -1) {
                probs[i] = m_Distribution.prob(i, theSubset);
            } else {
                probs[i] = m_Distribution.prob(i);
            }
        }
        return probs;
    }

    private boolean singleBranchTest(Instances data) throws Exception {
        int missingCount = -1;
        double same = data.instance(0).value(m_AttributeIndex);
        int sameCount = -1;
        for (int i = 0; i < data.numInstances(); i++) {
            if (data.instance(i).isMissing(m_AttributeIndex)) {
                missingCount++;
            }
            if (Utils.eq(data.instance(i).value(m_AttributeIndex), same)) {
                sameCount++;
            }
            if ((missingCount != i) && (sameCount != i)) {
                return false;
            }
        }
        return true;
    }

    private Instances[] split(Instances data) throws Exception {
        Instances[] instances = new Instances[m_Distribution.numBags()];
        for (int i = 0; i < m_Distribution.numBags(); i++) {
            instances[i] = (m_Distribution.perBag(i) > 0) ? new Instances(data, (int) m_Distribution.perBag(i)) : null;
        }
        for (int i = 0; i < data.numInstances(); i++) {
            Instance instance = data.instance(i);
            int subset = whichSubset(instance);
            if (instances[subset] != null) {
                instances[subset].add(instance);
            } else {
                throw new WekaException("instances[" + subset + "] is null!");
            }
        }
        for (int i = 0; i < m_Distribution.numBags(); i++) {
            if (instances[i] != null) {
                instances[i].compactify();
            }
        }
        return instances;
    }

    private int whichSubset(Instance data) {
        if (data.attribute(m_AttributeIndex).isNominal()) {
            return m_NominalEvaluator.whichSubset(data, m_AttributeIndex);
        } else {
            return m_NumericEvaluator.whichSubset(data, m_AttributeIndex);
        }
    }
}
