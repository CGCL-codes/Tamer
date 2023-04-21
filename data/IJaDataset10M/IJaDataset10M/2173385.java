package de.humanfork.treemerge.tree;

import de.humanfork.treemerge.exception.PartnerNotFoundException;
import de.humanfork.treemerge.match.Match;

/**
 * Descrip additional funktions for the base node role.
 *
 * @author Ralph
 */
public interface BaseMergeNode extends MergeNode<BaseMergeNode> {

    /**
     * Checks for a match with a node in tTreeID.
     *
     * @param tTreeID the tree ID
     *
     * @return true, if has a match
     */
    boolean hasMatch(Tree<TMergeNode> tTreeID);

    /**
     * Sets the match.
     *
     * @param match the match
     */
    void setMatch(Match match);

    /**
     * Gets the match with a node in tTreeID.
     *
     * @param tTreeID the t tree ID
     *
     * @return the match
     */
    Match getMatch(Tree<TMergeNode> tTreeID);

    /**
     * Checks for partner pair.
     *
     * @return true, if this node has a match in T1 and one in T2.
     */
    boolean hasPartnerPair();

    /**
     * Gets the partner for the spezified node.
     * If node is in T1 then the partner in in T2 and the other way around.
     * @param node the node
     *
     * @return the partner for the node
     *
     * @throws PartnerNotFoundException the partner not found exception
     */
    TMergeNode getPartnerFor(TMergeNode node) throws PartnerNotFoundException;

    /**
     * Checks if this node is deleted in T1 oder T2.
     *
     * @param tTreeID the t tree ID
     *
     * @return true, if is deleted
     */
    boolean isDeleted(Tree<TMergeNode> tTreeID);

    /**
     * Flag to avoid double matching during MatchSimilarUnmatched.
     * This simulates Z_vorgemerktTT.
     *
     * @return true if an t node will match to this node
     */
    boolean isSimilarTaged();

    /**
     * Set the simlilar flag.
     * This simulates Z_vorgemerktTT.
     *
     * @param similarTag the similar tag
     */
    void setSimilarTaged(boolean similarTag);
}
