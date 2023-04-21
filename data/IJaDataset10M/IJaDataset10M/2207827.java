package gov.nasa.jpf.search.heuristic;

/**
 * dummy heuristic state prioritizer (that leaves it all to the
 * configured comparator)
 */
public class DefaultHeuristic implements Heuristic {

    public DefaultHeuristic(HeuristicSearch hSearch) {
    }

    public int heuristicValue() {
        return 0;
    }

    public void processParent() {
    }
}
