package dr.evolution.parsimony;

import dr.evolution.tree.Tree;
import dr.evolution.tree.NodeRef;

/**
 * @author rambaut
 *         Date: Jun 20, 2005
 *         Time: 4:56:34 PM
 */
public interface ParsimonyCriterion {

    /**
     * Calculates the minimum number of steps for the parsimony reconstruction for the given tree.
     * It is expected that the implementation's constructor will be set up with the characters so
     * that repeated calls can be made to this function to evaluate different trees.
     * @param tree a tree object to reconstruct the characters on
     * @return an array containing the parsimony score for each site
     */
    double[] getSiteScores(Tree tree);

    /**
     * Calculates the minimum number of steps for the parsimony reconstruction for the given tree.
     * It is expected that the implementation's constructor will be set up with the characters so
     * that repeated calls can be made to this function to evaluate different trees.
     * @param tree a tree object to reconstruct the characters on
     * @return the total score
     */
    double getScore(Tree tree);

    /**
     * Returns the reconstructed character states for a given node in the tree.
     * @param tree a tree object to reconstruct the characters on
     * @param node the node of the tree
     * @return an array containing the reconstructed states for this node
     */
    int[] getStates(Tree tree, NodeRef node);
}
