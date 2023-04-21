package org.objectwiz;

import java.io.Serializable;

/**
 * A fetch strategy defines which part of the object graph has to be fetched
 * and retrieved at once when the query is performed.
 *
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
public class FetchStrategy implements Serializable {

    private boolean wholeGraph;

    public static final FetchStrategy WHOLE_GRAPH = new FetchStrategy(true);

    public static final FetchStrategy DEFAULT = new FetchStrategy(false);

    protected FetchStrategy(boolean wholeGraph) {
        this.wholeGraph = wholeGraph;
    }

    public boolean isWholeGraph() {
        return wholeGraph;
    }

    @Override
    public String toString() {
        return wholeGraph ? "WHOLE_GRAPH" : "DEFAULT";
    }
}
