package preprocessing.automatic.weka.classifiers.trees.j48;

import preprocessing.automatic.weka.core.Instances;
import preprocessing.automatic.weka.core.UnsupportedClassTypeException;
import preprocessing.automatic.weka.core.UnsupportedAttributeTypeException;
import game.weka.core.Utils;

/**
 * Class for handling a tree structure that can
 * be pruned using C4.5 procedures.
 *
 * @author Eibe Frank (eibe@cs.waikato.ac.nz)
 * @version $Revision: 1.11 $
 */
public class C45PruneableClassifierTree extends ClassifierTree {

    /** True if the tree is to be pruned. */
    boolean m_pruneTheTree = false;

    /** The confidence factor for pruning. */
    float m_CF = 0.25f;

    /** Is subtree raising to be performed? */
    boolean m_subtreeRaising = true;

    /** Cleanup after the tree has been built. */
    boolean m_cleanup = true;

    /**
   * Constructor for pruneable tree structure. Stores reference
   * to associated training data at each node.
   *
   * @param toSelectLocModel selection method for local splitting model
   * @param pruneTree true if the tree is to be pruned
   * @param cf the confidence factor for pruning
   * @exception Exception if something goes wrong
   */
    public C45PruneableClassifierTree(ModelSelection toSelectLocModel, boolean pruneTree, float cf, boolean raiseTree, boolean cleanup) throws Exception {
        super(toSelectLocModel);
        m_pruneTheTree = pruneTree;
        m_CF = cf;
        m_subtreeRaising = raiseTree;
        m_cleanup = cleanup;
    }

    /**
   * Method for building a pruneable classifier tree.
   *
   * @exception Exception if something goes wrong
   */
    public void buildClassifier(Instances data) throws Exception {
        if (data.classAttribute().isNumeric()) throw new UnsupportedClassTypeException("Class is numeric!");
        if (data.checkForStringAttributes()) {
            throw new UnsupportedAttributeTypeException("Cannot handle string attributes!");
        }
        data = new Instances(data);
        data.deleteWithMissingClass();
        buildTree(data, m_subtreeRaising);
        collapse();
        if (m_pruneTheTree) {
            prune();
        }
        if (m_cleanup) {
            cleanup(new Instances(data, 0));
        }
    }

    /**
   * Collapses a tree to a node if training error doesn't increase.
   */
    public final void collapse() {
        double errorsOfSubtree;
        double errorsOfTree;
        int i;
        if (!m_isLeaf) {
            errorsOfSubtree = getTrainingErrors();
            errorsOfTree = localModel().distribution().numIncorrect();
            if (errorsOfSubtree >= errorsOfTree - 1E-3) {
                m_sons = null;
                m_isLeaf = true;
                m_localModel = new NoSplit(localModel().distribution());
            } else for (i = 0; i < m_sons.length; i++) son(i).collapse();
        }
    }

    /**
   * Prunes a tree using C4.5's pruning procedure.
   *
   * @exception Exception if something goes wrong
   */
    public void prune() throws Exception {
        double errorsLargestBranch;
        double errorsLeaf;
        double errorsTree;
        int indexOfLargestBranch;
        C45PruneableClassifierTree largestBranch;
        int i;
        if (!m_isLeaf) {
            for (i = 0; i < m_sons.length; i++) son(i).prune();
            indexOfLargestBranch = localModel().distribution().maxBag();
            if (m_subtreeRaising) {
                errorsLargestBranch = son(indexOfLargestBranch).getEstimatedErrorsForBranch((Instances) m_train);
            } else {
                errorsLargestBranch = Double.MAX_VALUE;
            }
            errorsLeaf = getEstimatedErrorsForDistribution(localModel().distribution());
            errorsTree = getEstimatedErrors();
            if (Utils.smOrEq(errorsLeaf, errorsTree + 0.1) && Utils.smOrEq(errorsLeaf, errorsLargestBranch + 0.1)) {
                m_sons = null;
                m_isLeaf = true;
                m_localModel = new NoSplit(localModel().distribution());
                return;
            }
            if (Utils.smOrEq(errorsLargestBranch, errorsTree + 0.1)) {
                largestBranch = son(indexOfLargestBranch);
                m_sons = largestBranch.m_sons;
                m_localModel = largestBranch.localModel();
                m_isLeaf = largestBranch.m_isLeaf;
                newDistribution(m_train);
                prune();
            }
        }
    }

    /**
   * Returns a newly created tree.
   *
   * @exception Exception if something goes wrong
   */
    protected ClassifierTree getNewTree(Instances data) throws Exception {
        C45PruneableClassifierTree newTree = new C45PruneableClassifierTree(m_toSelectModel, m_pruneTheTree, m_CF, m_subtreeRaising, m_cleanup);
        newTree.buildTree((Instances) data, m_subtreeRaising);
        return newTree;
    }

    /**
   * Computes estimated errors for tree.
   */
    private double getEstimatedErrors() {
        double errors = 0;
        int i;
        if (m_isLeaf) return getEstimatedErrorsForDistribution(localModel().distribution()); else {
            for (i = 0; i < m_sons.length; i++) errors = errors + son(i).getEstimatedErrors();
            return errors;
        }
    }

    /**
   * Computes estimated errors for one branch.
   *
   * @exception Exception if something goes wrong
   */
    private double getEstimatedErrorsForBranch(Instances data) throws Exception {
        Instances[] localInstances;
        double errors = 0;
        int i;
        if (m_isLeaf) return getEstimatedErrorsForDistribution(new Distribution(data)); else {
            Distribution savedDist = localModel().m_distribution;
            localModel().resetDistribution(data);
            localInstances = (Instances[]) localModel().split(data);
            localModel().m_distribution = savedDist;
            for (i = 0; i < m_sons.length; i++) errors = errors + son(i).getEstimatedErrorsForBranch(localInstances[i]);
            return errors;
        }
    }

    /**
   * Computes estimated errors for leaf.
   */
    private double getEstimatedErrorsForDistribution(Distribution theDistribution) {
        if (Utils.eq(theDistribution.total(), 0)) return 0; else return theDistribution.numIncorrect() + Stats.addErrs(theDistribution.total(), theDistribution.numIncorrect(), m_CF);
    }

    /**
   * Computes errors of tree on training data.
   */
    private double getTrainingErrors() {
        double errors = 0;
        int i;
        if (m_isLeaf) return localModel().distribution().numIncorrect(); else {
            for (i = 0; i < m_sons.length; i++) errors = errors + son(i).getTrainingErrors();
            return errors;
        }
    }

    /**
   * Method just exists to make program easier to read.
   */
    private ClassifierSplitModel localModel() {
        return (ClassifierSplitModel) m_localModel;
    }

    /**
   * Computes new distributions of instances for nodes
   * in tree.
   *
   * @exception Exception if something goes wrong
   */
    private void newDistribution(Instances data) throws Exception {
        Instances[] localInstances;
        localModel().resetDistribution(data);
        m_train = data;
        if (!m_isLeaf) {
            localInstances = (Instances[]) localModel().split(data);
            for (int i = 0; i < m_sons.length; i++) son(i).newDistribution(localInstances[i]);
        } else {
            if (!Utils.eq(data.sumOfWeights(), 0)) {
                m_isEmpty = false;
            }
        }
    }

    /**
   * Method just exists to make program easier to read.
   */
    private C45PruneableClassifierTree son(int index) {
        return (C45PruneableClassifierTree) m_sons[index];
    }
}
