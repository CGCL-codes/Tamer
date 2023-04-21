package moa.classifiers;

import moa.options.IntOption;
import weka.core.Instance;

/**
 * Hoeffding Option Tree with naive Bayes learners at the leaves.
 *
 * <p>Parameters:</p> <ul>
 * <li>Same parameters as <code>HoeffdingOptionTree<code></li>
 * <li> -q : The number of instances a leaf should observe before
 * permitting Naive Bayes</li> </ul>
 *
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @version $Revision: 7 $
 */
public class HoeffdingOptionTreeNB extends HoeffdingOptionTree {

    private static final long serialVersionUID = 1L;

    public IntOption nbThresholdOption = new IntOption("nbThreshold", 'q', "The number of instances a leaf should observe before permitting Naive Bayes.", 0, 0, Integer.MAX_VALUE);

    public static class LearningNodeNB extends ActiveLearningNode {

        private static final long serialVersionUID = 1L;

        public LearningNodeNB(double[] initialClassObservations) {
            super(initialClassObservations);
        }

        @Override
        public double[] getClassVotes(Instance inst, HoeffdingOptionTree hot) {
            if (getWeightSeen() >= ((HoeffdingOptionTreeNB) hot).nbThresholdOption.getValue()) {
                return NaiveBayes.doNaiveBayesPrediction(inst, this.observedClassDistribution, this.attributeObservers);
            }
            return super.getClassVotes(inst, hot);
        }

        @Override
        public void disableAttribute(int attIndex) {
        }
    }

    public HoeffdingOptionTreeNB() {
        this.removePoorAttsOption = null;
    }

    @Override
    protected LearningNode newLearningNode(double[] initialClassObservations) {
        return new LearningNodeNB(initialClassObservations);
    }
}
