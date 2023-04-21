package jopt.csp.spi.search.localsearch;

import java.util.Iterator;
import java.util.LinkedList;
import jopt.csp.spi.solver.ChoicePointDataMap;
import jopt.csp.spi.solver.ChoicePointEntryListener;
import jopt.csp.spi.solver.ChoicePointStack;

/**
 * List that is used by the {@link TabuMetaheuristic} metaheuristic to hold moves that
 * are considered 'tabu' and will not be allowed
 * 
 * @author Nick Coleman
 * @version $Revision: 1.5 $
 * @see TabuMetaheuristic
 */
public class TabuList implements ChoicePointEntryListener {

    private ChoicePointDataMap cpdata;

    private boolean conflict;

    private int maxAge;

    private int agingMoves;

    private LinkedList<TabuMove> moves;

    /**
     * Creates a new tabu list
     * 
     * @param conflict  True if list contains a set of moves that should not conflict with other moves,
     *                  False if list contains a set of moves that should not be repeated
     * @param maxAge   Maximum age that a move may be before being removed from list
     */
    public TabuList(boolean conflict, int maxAge) {
        this.conflict = conflict;
        this.maxAge = maxAge;
        this.moves = new LinkedList<TabuMove>();
        this.agingMoves = 0;
    }

    /**
     * Returns true if this list contains any part of this move
     * 
     * @param move  Move to verify in list
     * @return True if any change in move is contained within this list
     */
    public boolean contains(TabuMove move) {
        Iterator<TabuMove> moveIter = moves.iterator();
        while (moveIter.hasNext()) {
            TabuMove previous = (TabuMove) moveIter.next();
            if (previous != null) {
                if (conflict) {
                    if (previous.conflicts(move)) return true;
                } else {
                    if (previous.repeats(move)) return true;
                }
            }
        }
        return false;
    }

    /**
     * Ages all existing moves by 1 and removes any move that
     * is beyond maximum allowed age
     */
    public void age() {
        add(null);
    }

    public void reset() {
        moves.clear();
        agingMoves = 0;
    }

    public void reduce() {
        int amountToAge = Math.max(1, maxAge / 3);
        for (int i = 0; i < amountToAge; i++) {
            age();
        }
    }

    /**
     * Adds a new move to the list and ages all existing moves by 1.
     * Any move that is beyond maximum allowed age is removed.
     */
    public void add(TabuMove move) {
        moves.addFirst(move);
        if (move == null) agingMoves++;
        if (moves.size() > maxAge) {
            moves.removeLast();
            while (moves.size() > 0 && moves.getLast() == null) {
                moves.removeLast();
                agingMoves--;
            }
        }
    }

    /**
     * Returns current size of list
     */
    public int size() {
        return moves.size() - agingMoves;
    }

    @SuppressWarnings("unchecked")
    public void beforeChoicePointPopEvent() {
        if (cpdata != null) {
            if (cpdata.containsKey("m")) moves = (LinkedList<TabuMove>) cpdata.get("m");
            if (cpdata.containsKey("a")) maxAge = ((Integer) cpdata.get("a")).intValue();
        }
    }

    public void afterChoicePointPopEvent() {
    }

    public void beforeChoicePointPushEvent() {
        if (cpdata != null) {
            cpdata.put("m", moves.clone());
            cpdata.put("a", new Integer(maxAge));
        }
    }

    public void afterChoicePointPushEvent() {
        beforeChoicePointPopEvent();
    }

    /**
     * Sets the choicepoint stack associated with this list.  Can only
     * be set once
     */
    public void setChoicePointStack(ChoicePointStack cps) {
        if (this.cpdata != null && cps != null) {
            throw new IllegalStateException("Choice point stack already set for list");
        }
        if (cps == null) {
            if (cpdata != null) cpdata.close();
            this.cpdata = null;
        } else this.cpdata = cps.newDataMap(this);
    }

    public String toString() {
        return moves.toString();
    }
}
