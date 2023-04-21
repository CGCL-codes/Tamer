package com.thesett.aima.search.util.uninformed;

import com.thesett.aima.search.Traversable;
import com.thesett.aima.search.impl.BoundedAlgorithm;

/**
 * Implements a Depth-bounded search. This procedes depth first but is bounded.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Do a depth bounded search.
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class DepthBoundedSearch<O, T extends Traversable<O>> extends DepthFirstSearch<O, T> {

    /**
     * Creates a new DepthBoundedSearch object.
     *
     * @param maxDepth The maximum search depth to search to.
     */
    public DepthBoundedSearch(int maxDepth) {
        setQueueSearchAlgorithm(new BoundedAlgorithm((float) maxDepth));
    }
}
