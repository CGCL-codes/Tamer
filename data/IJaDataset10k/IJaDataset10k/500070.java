package cc.mallet.classify;

import java.io.Serializable;
import java.util.logging.Logger;
import cc.mallet.pipe.Pipe;
import cc.mallet.types.Alphabet;
import cc.mallet.types.AugmentableFeatureVector;
import cc.mallet.types.FeatureSelection;
import cc.mallet.types.FeatureVector;
import cc.mallet.types.InfoGain;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.Labeling;
import cc.mallet.util.MalletLogger;

/**
   Decision Tree classifier.
   @author Andrew McCallum <a href="mailto:mccallum@cs.umass.edu">mccallum@cs.umass.edu</a>
 */
public class DecisionTree extends Classifier implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Logger logger = MalletLogger.getLogger(DecisionTree.class.getName());

    Node root;

    public DecisionTree(Pipe instancePipe, DecisionTree.Node root) {
        super(instancePipe);
        this.root = root;
    }

    public Node getRoot() {
        return root;
    }

    private Node getLeaf(Node node, FeatureVector fv) {
        if (node.child0 == null) return node; else if (fv.value(node.featureIndex) != 0) return getLeaf(node.child1, fv); else return getLeaf(node.child0, fv);
    }

    public Classification classify(Instance instance) {
        FeatureVector fv = (FeatureVector) instance.getData();
        assert (instancePipe == null || fv.getAlphabet() == this.instancePipe.getDataAlphabet());
        Node leaf = getLeaf(root, fv);
        return new Classification(instance, this, leaf.labeling);
    }

    public double addFeaturesClassEntropyThreshold = 0.7;

    public void induceFeatures(InstanceList ilist, boolean withFeatureShrinkage, boolean inducePerClassFeatures) {
        if (inducePerClassFeatures) {
            int numClasses = ilist.getTargetAlphabet().size();
            FeatureSelection[] pcfs = new FeatureSelection[numClasses];
            for (int j = 0; j < numClasses; j++) pcfs[j] = (FeatureSelection) ilist.getPerLabelFeatureSelection()[j].clone();
            for (int i = 0; i < ilist.size(); i++) {
                Object data = ilist.get(i).getData();
                AugmentableFeatureVector afv = (AugmentableFeatureVector) data;
                root.induceFeatures(afv, null, pcfs, ilist.getFeatureSelection(), ilist.getPerLabelFeatureSelection(), withFeatureShrinkage, inducePerClassFeatures, addFeaturesClassEntropyThreshold);
            }
        } else {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    public static class Node implements Serializable {

        private static final long serialVersionUID = 1L;

        int featureIndex;

        double infoGain;

        InstanceList ilist;

        Alphabet dictionary;

        double labelEntropy;

        Labeling labeling;

        Node parent, child0, child1;

        String name;

        public Node(InstanceList ilist, Node parent, FeatureSelection fs) {
            InfoGain ig = new InfoGain(ilist);
            this.featureIndex = ig.getMaxValuedIndexIn(fs);
            this.infoGain = ig.value(featureIndex);
            this.ilist = ilist;
            this.dictionary = ilist.getDataAlphabet();
            this.parent = parent;
            this.labeling = ig.getBaseLabelDistribution();
            this.labelEntropy = ig.getBaseEntropy();
            this.child0 = this.child1 = null;
        }

        /** The root has depth zero. */
        public int depth() {
            int depth = 0;
            Node p = parent;
            while (p != null) {
                p = p.parent;
                depth++;
            }
            return depth;
        }

        public boolean isLeaf() {
            return (child0 == null && child1 == null);
        }

        public boolean isRoot() {
            return parent == null;
        }

        public Node getFeatureAbsentChild() {
            return child0;
        }

        public Node getFeaturePresentChild() {
            return child1;
        }

        public double getSplitInfoGain() {
            return infoGain;
        }

        public Object getSplitFeature() {
            return ilist.getDataAlphabet().lookupObject(featureIndex);
        }

        public void split(FeatureSelection fs) {
            if (ilist == null) throw new IllegalStateException("Frozen.  Cannot split.");
            InstanceList ilist0 = new InstanceList(ilist.getPipe());
            InstanceList ilist1 = new InstanceList(ilist.getPipe());
            for (int i = 0; i < ilist.size(); i++) {
                Instance instance = ilist.get(i);
                FeatureVector fv = (FeatureVector) instance.getData();
                if (fv.value(featureIndex) != 0) {
                    ilist1.add(instance, ilist.getInstanceWeight(i));
                } else {
                    ilist0.add(instance, ilist.getInstanceWeight(i));
                }
            }
            logger.info("child0=" + ilist0.size() + " child1=" + ilist1.size());
            child0 = new Node(ilist0, this, fs);
            child1 = new Node(ilist1, this, fs);
        }

        public void stopGrowth() {
            if (child0 != null) {
                child0.stopGrowth();
                child1.stopGrowth();
            }
            ilist = null;
        }

        public void induceFeatures(AugmentableFeatureVector afv, FeatureSelection featuresAlreadyThere, FeatureSelection[] perClassFeaturesAlreadyThere, FeatureSelection newFeatureSelection, FeatureSelection[] perClassNewFeatureSelection, boolean withInteriorNodes, boolean addPerClassFeatures, double classEntropyThreshold) {
            if (!isRoot() && (isLeaf() || withInteriorNodes) && labelEntropy < classEntropyThreshold) {
                String name = getName();
                logger.info("Trying to add feature " + name);
                if (addPerClassFeatures) {
                    int classIndex = labeling.getBestIndex();
                    if (!perClassFeaturesAlreadyThere[classIndex].contains(name)) {
                        afv.add(name, 1.0);
                        perClassNewFeatureSelection[classIndex].add(name);
                    }
                } else {
                    throw new UnsupportedOperationException("Not yet implemented.");
                }
            }
            boolean featurePresent = afv.value(featureIndex) != 0;
            if (child0 != null && !featurePresent) child0.induceFeatures(afv, featuresAlreadyThere, perClassFeaturesAlreadyThere, newFeatureSelection, perClassNewFeatureSelection, withInteriorNodes, addPerClassFeatures, classEntropyThreshold);
            if (child1 != null && featurePresent) child1.induceFeatures(afv, featuresAlreadyThere, perClassFeaturesAlreadyThere, newFeatureSelection, perClassNewFeatureSelection, withInteriorNodes, addPerClassFeatures, classEntropyThreshold);
        }

        public String getName() {
            if (parent == null) return "root"; else if (parent.parent == null) {
                if (parent.getFeaturePresentChild() == this) return dictionary.lookupObject(parent.featureIndex).toString(); else {
                    assert (dictionary != null);
                    assert (dictionary.lookupObject(parent.featureIndex) != null);
                    return "!" + dictionary.lookupObject(parent.featureIndex).toString();
                }
            } else {
                if (parent.getFeaturePresentChild() == this) return parent.getName() + "&" + dictionary.lookupObject(parent.featureIndex).toString(); else return parent.getName() + "&!" + dictionary.lookupObject(parent.featureIndex).toString();
            }
        }

        public void print() {
            if (child0 == null) System.out.println(getName() + ": " + labeling.getBestLabel()); else {
                child0.print();
                child1.print();
            }
        }
    }
}
