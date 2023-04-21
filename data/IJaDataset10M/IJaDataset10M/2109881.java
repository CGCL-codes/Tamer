package nz.ac.waikato.jdsl.core.api;

/**
 * A partially-ordered container that allows for
 * removal of the element with highest <i>priority</i>.  The names of
 * the methods (e.g., min()) assume that high priorities are
 * numerically low, but nothing except the names requires this
 * convention.
 * <p>
 * The interface makes no assumptions about how comparisons are made,
 * or how the elements are ordered.  The priority of two elements may
 * be hardcoded or chosen dynamicaly, depending upon the specific
 * implementation.
 * <p>
 * Note that there is no InspectablePriorityQueue.
 *   
 * @version JDSL 2.1.1 
 * @author Mark Handy
 * @author Andrew Schwerin
 * @author Luca Vismara
 */
public interface PriorityQueue extends KeyBasedContainer {

    /** 
   * Allows access to element with highest priority without removing
   * it from the priority queue.<p>
   *
   * @return the locator to the element with highest priority in the
   * priority queue
   * @exception EmptyContainerException if the priority queue is empty
   */
    public Locator min() throws EmptyContainerException;

    /** 
   * Pops the highest-priority element off the priority queue and
   * updates the priority queue.<p>
   *
   * @return an element with highest priority in the priority queue
   * @exception EmptyContainerException if the priority queue is empty
   */
    public Object removeMin() throws EmptyContainerException;
}
