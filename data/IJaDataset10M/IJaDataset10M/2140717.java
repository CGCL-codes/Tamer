package playground.dressler.Interval;

import playground.dressler.ea_flow.PathStep;

public class VertexIntervalsWithCost extends VertexIntervals {

    /**
	 * flag for debug mode
	 */
    @SuppressWarnings("unused")
    private static int _debug = 0;

    /**
	 * Default Constructor Constructs an object containing only 
	 * the given interval
	 */
    public VertexIntervalsWithCost(VertexIntervalWithCost interval) {
        super(interval);
    }

    /**
	 * setter for debug mode
	 * @param debug debug mode true is on
	 */
    public static void debug(int debug) {
        VertexIntervalsWithCost._debug = debug;
    }

    /**
	 * finds the first VertexInterval within which
	 *  the node is reachable from the source
	 * @return specified VertexInterval or null if none exist
	 */
    public VertexIntervalWithCost getFirstPossibleForward() {
        VertexIntervalWithCost result = (VertexIntervalWithCost) this.getIntervalAt(0);
        while (!this.isLast(result)) {
            if (result.getReachable() && result.getPredecessor() != null) {
                return result;
            } else {
                result = (VertexIntervalWithCost) this.getNext(result);
            }
        }
        if (result.getReachable() && result.getPredecessor() != null) {
            return result;
        }
        return null;
    }

    /**
	 * calculates the first time where it is reachable 
	 * @return minimal time or Integer.MAX_VALUE if it is not reachable at all
	 */
    public int firstPossibleTime() {
        VertexIntervalWithCost test = this.getFirstPossibleForward();
        if (test != null) {
            return test.getLowBound();
        } else {
            return Integer.MAX_VALUE;
        }
    }

    /**
	 * unifies adjacent intervals, call only when you feel it is safe to do
	 * @return number of unified VertexIntervals
	 */
    public int cleanup() {
        int gain = 0;
        int timestop = getLast().getHighBound();
        VertexIntervalWithCost i, j;
        i = (VertexIntervalWithCost) getIntervalAt(0);
        while (i.getHighBound() < timestop) {
            j = (VertexIntervalWithCost) this.getIntervalAt(i.getHighBound());
            if (i.getHighBound() != j.getLowBound()) throw new RuntimeException("error in cleanup!");
            if (i.continuedBy(j)) {
                _tree.remove(i);
                _tree.remove(j);
                j = new VertexIntervalWithCost(i.getLowBound(), j.getHighBound(), i);
                _tree.insert(j);
                gain++;
            }
            i = j;
        }
        this._last = i;
        return gain;
    }
}
