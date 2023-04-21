package org.modss.facilitator.ui.ranking;

import javax.swing.tree.TreePath;

/**
 * Interface implemented by an object which will provide the type for a node.
 * The example is the drag and drop handler which wishes to know whether a node 
 * is a:
 * <ul>
 *  <li>
 *    LEAF
 *  </li>
 *  <li>
 *    ROOT
 *  </li>
 *  <li>
 *    CONTAINER
 *  </li>
 * </ul>
 * @see RankingConstants.
 */
interface NodeTypeProvider {

    /**
     * Provide the type (ROOT, LEAF, CONTAINER) for the provided node.
     *
     * @param path the node to get the type for.
     */
    public int getType(TreePath path);
}
