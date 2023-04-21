package fr.jussieu.gla.wasa.core;

/**
 * Internally used by <tt>Engine</tt> for storing <tt>Var</tt>s marked 'Tabu'.
 * @author Laurent Caillette
 * @author Florent Selva
 * @version $Revision: 1.9 $ $Date: 2002/04/17 15:24:05 $
 */
public class TabuList {

    private final Var[] vars;

    private int head = 0;

    public TabuList(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity < 1");
        }
        vars = new Var[capacity + 1];
    }

    public int getCapacity() {
        return vars.length - 1;
    }

    /**
   * Returns true if <tt>var</tt> is in the <tt>TabuList</tt>.
   * @return True if <tt>var</tt> is in the <tt>TabuList</tt>
   */
    public boolean contains(Var var) {
        for (int i = 0; i < vars.length; i++) {
            if (vars[i] == var) {
                return true;
            }
        }
        return false;
    }

    /**
   * Returns the size of the <tt>TabuList</tt>.
   * @return The size of the <tt>TabuList</tt>
   */
    public int getSize() {
        Var[] usedVars = getVars();
        int size = 0;
        for (int i = 0; i < usedVars.length; i++) {
            if (usedVars[i] != null) {
                size++;
            }
        }
        return size;
    }

    /**
   * Adds <tt>var</tt> in the <tt>TabuList</tt>.
   */
    public void put(Var var) throws CloneNotSupportedException {
        if (head >= vars.length) {
            head = 0;
        }
        vars[head] = var;
    }

    /**
   * Clears the <tt>TabuList</tt>.
   */
    public void clear() {
        head = 0;
        for (int i = 0; i < vars.length; i++) {
            vars[i] = null;
        }
    }

    /**
   * Executes one step ( corresponding to the engine step ) on the
   * <tt>TabuList</tt>, each <tt>Var</tt> going out of the list after
   * {@link #getsize} steps.
   */
    public void step() {
        head++;
        if (head >= vars.length) {
            head = 0;
        }
        vars[head] = null;
    }

    /**
 * Used by {@link fr.jussieu.gla.wasa.monitor.event.NotifyingAlgorithm} for
 * displaying TabuList content.
 * @return An array of <tt>Var</tt>s where first slots represent
 *     most recent TabuList slots.
 */
    public Var[] getVars() {
        Var[] orderedVars = new Var[vars.length - 1];
        int orderedVarsIndex = orderedVars.length - 1;
        int varsIndex = head;
        for (int i = 0; i < vars.length; i++) {
            if (varsIndex != head) {
                orderedVars[orderedVarsIndex] = vars[varsIndex];
                orderedVarsIndex--;
            }
            varsIndex++;
            if (varsIndex >= vars.length) {
                varsIndex = 0;
            }
        }
        return orderedVars;
    }
}
