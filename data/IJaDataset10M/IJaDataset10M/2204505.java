package com.sodad.weka.classifiers.bayes.net.search.ci;

import com.sodad.weka.classifiers.bayes.BayesNet;
import com.sodad.weka.classifiers.bayes.net.ParentSet;
import com.sodad.weka.classifiers.bayes.net.search.local.LocalScoreSearchAlgorithm;
import com.sodad.weka.core.Instances;
import com.sodad.weka.core.RevisionUtils;

/** 
 <!-- globalinfo-start -->
 * The CISearchAlgorithm class supports Bayes net structure search algorithms that are based on conditional independence test (as opposed to for example score based of cross validation based search algorithms).
 * <p/>
 <!-- globalinfo-end -->
 *
 <!-- options-start -->
 * Valid options are: <p/>
 * 
 * <pre> -mbc
 *  Applies a Markov Blanket correction to the network structure, 
 *  after a network structure is learned. This ensures that all 
 *  nodes in the network are part of the Markov blanket of the 
 *  classifier node.</pre>
 * 
 * <pre> -S [BAYES|MDL|ENTROPY|AIC|CROSS_CLASSIC|CROSS_BAYES]
 *  Score type (BAYES, BDeu, MDL, ENTROPY and AIC)</pre>
 * 
 <!-- options-end -->
 *
 * @author Remco Bouckaert (rrb@xm.co.nz)
 * @version $Revision: 1.7 $
 */
public class CISearchAlgorithm extends LocalScoreSearchAlgorithm {

    /** for serialization */
    static final long serialVersionUID = 3165802334119704560L;

    BayesNet m_BayesNet;

    Instances m_instances;

    /**
	 * Returns a string describing this object
	 * @return a description of the classifier suitable for
	 * displaying in the explorer/experimenter gui
	 */
    public String globalInfo() {
        return "The CISearchAlgorithm class supports Bayes net structure " + "search algorithms that are based on conditional independence " + "test (as opposed to for example score based of cross validation " + "based search algorithms).";
    }

    /** IsConditionalIndependent tests whether two nodes X and Y are independent
	 *  given a set of variables Z. The test compares the score of the Bayes network
	 * with and without arrow Y->X where all nodes in Z are parents of X.
	 * @param iAttributeX - index of attribute representing variable X
	 * @param iAttributeY - index of attribute representing variable Y
 	 * @param iAttributesZ - array of integers representing indices of attributes in set Z
 	 * @param nAttributesZ - cardinality of Z
 	 * @return true if X and Y conditionally independent given Z 
	 */
    protected boolean isConditionalIndependent(int iAttributeX, int iAttributeY, int[] iAttributesZ, int nAttributesZ) {
        ParentSet oParentSetX = m_BayesNet.getParentSet(iAttributeX);
        while (oParentSetX.getNrOfParents() > 0) {
            oParentSetX.deleteLastParent(m_instances);
        }
        for (int iAttributeZ = 0; iAttributeZ < nAttributesZ; iAttributeZ++) {
            oParentSetX.addParent(iAttributesZ[iAttributeZ], m_instances);
        }
        double fScoreZ = calcNodeScore(iAttributeX);
        double fScoreZY = calcScoreWithExtraParent(iAttributeX, iAttributeY);
        if (fScoreZY <= fScoreZ) {
            return true;
        }
        return false;
    }

    /**
	 * Returns the revision string.
	 * 
	 * @return		the revision
	 */
    public String getRevision() {
        return RevisionUtils.extract("$Revision: 1.7 $");
    }
}