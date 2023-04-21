package abc.weaving.matching;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Set;

/** 
 * A node in the state machine. Contains information about successors, whether it's
 * an initial/final node, and (eventually) variable binding tracking information.
 *
 *  @author Pavel Avgustinov
 */
public class SMNode implements State {

    protected int number = -1;

    protected boolean initialNode = false;

    protected boolean finalNode = false;

    protected LinkedHashSet incoming = new LinkedHashSet();

    protected LinkedHashSet outgoing = new LinkedHashSet();

    /** set of variables for which this state needs to keep strong refs */
    public LinkedHashSet needStrongRefs = new LinkedHashSet();

    /** set of variables for which we may use weak references, 
         and any disjunct containing these variables may be garbage-collected
         when the weak reference is garbage-collected */
    public LinkedHashSet collectableWeakRefs = new LinkedHashSet();

    /** set of variables for which we may use weak references,
         but disjuncts containing these cannot necessarily be garbage-collected
         when the weak reference is garbage-collected */
    public LinkedHashSet weakRefs = new LinkedHashSet();

    /** set of variables that is guaranteed to be bound at this state.
	 *   when adding a set of >1 negative bindings, we can decide based on this
	 *   information whether it is necessary to create a set of disjuncts as the return value
	 */
    public LinkedHashSet boundVars = new LinkedHashSet();

    /** The number of collectable index variables on this state. */
    public int nCollectable;

    /** the number of primitive index variables on this state. */
    public int nPrimitive;

    /** the number of weak index variables on this state. */
    public int nWeak;

    /** the number of strong index variables on this state. */
    public int nStrong;

    /** list of variables that should be used to index the disjuncts
     *   stored for this state - the index is, in general, multi-level
     *   and the variables are used in the order they appear in this
     *   list
     */
    public List indices = new LinkedList();

    private TMStateMachine hostFSA;

    public SMNode(TMStateMachine fsa, boolean isInitial, boolean isFinal) {
        hostFSA = fsa;
        initialNode = isInitial;
        finalNode = isFinal;
    }

    public void addIncomingEdge(SMEdge edge) {
        incoming.add(edge);
    }

    public void addOutgoingEdge(SMEdge edge) {
        outgoing.add(edge);
    }

    /**
     * @return Returns the finalNode.
     */
    public boolean isFinalNode() {
        return finalNode;
    }

    /**
     * @param finalNode The finalNode to set.
     */
    public void setFinal(boolean finalNode) {
        this.finalNode = finalNode;
    }

    /**
     * @return Returns the initialNode.
     */
    public boolean isInitialNode() {
        return initialNode;
    }

    /**
     * @param initialNode The initialNode to set.
     */
    public void setInitial(boolean initialNode) {
        this.initialNode = initialNode;
    }

    /**
     * Computes the closure of the node -- i.e. the set of all nodes reachable from
     * this node by a sequence of zero or more transitions. Result is stored in the
     * accumulating parameter, which should be empty at first -- if it isn't, nodes
     * that are already in it aren't expanded.
     * 
     * TODO: Could employ a cunning caching scheme, as this repeatedly calculates closures
     * for sub-nodes..
     * 
     * This version follows transitions forwards.
     * 
     * @param result Set which is populated with the elements of the closure.
     * @param epsilonOnly specify whether only epsilon transitions should be considered.
     */
    public void fillInClosure(Set result, boolean epsilonOnly) {
        fillInClosure(result, epsilonOnly, true);
    }

    /**
     * Computes the closure of the node -- i.e. the set of all nodes reachable from
     * this node by a sequence of zero or more transitions. Result is stored in the
     * accumulating parameter, which should be empty at first -- if it isn't, nodes
     * that are already in it aren't expanded.
     * 
     * TODO: Could employ a cunning caching scheme, as this repeatedly calculates closures
     * for sub-nodes..
     * @param result Set which is populated with the elements of the closure.
     * @param epsilonOnly specify whether only epsilon transitions should be considered.
     * @param forward Specify whether we follow transitions forward or backward.
     */
    public void fillInClosure(Set result, boolean epsilonOnly, boolean forward) {
        SMEdge cur;
        SMNode next;
        result.add(this);
        Iterator it;
        if (forward) it = outgoing.iterator(); else it = incoming.iterator();
        while (it.hasNext()) {
            cur = (SMEdge) it.next();
            if (!epsilonOnly || cur.getLabel() == null) {
                if (forward) next = cur.getTarget(); else next = cur.getSource();
                if (!result.contains(next)) {
                    next.fillInClosure(result, epsilonOnly, forward);
                }
            }
        }
    }

    /**
     * Duplicates all non-epsilon transitions from a node onto this node. Used for 
     * epsilon-elimination.
     * @param from the node whose transitions are to be duplicated.
     */
    public void copySymbolTransitions(SMNode from) {
        SMEdge edge;
        SMNode to;
        String label;
        Iterator it = from.outgoing.iterator();
        while (it.hasNext()) {
            edge = (SMEdge) it.next();
            label = edge.getLabel();
            to = edge.getTarget();
            if (label != null && !hasEdgeTo(to, label)) {
                hostFSA.newTransition(this, to, label);
            }
        }
    }

    /**
     * Determines whether this node has an incoming edge with a given label.
     * @param label the label to consider
     * @return
     */
    public boolean hasInEdgeWithLabel(String label) {
        SMEdge edge;
        Iterator it = incoming.iterator();
        while (it.hasNext()) {
            edge = (SMEdge) it.next();
            if (edge.getLabel() == label || (label != null && label.equals(edge.getLabel()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether there exists an edge to a node with a given label
     * @param to The target node
     * @param label the label to check for
     * @return
     */
    public boolean hasEdgeTo(SMNode to, String label) {
        SMEdge edge;
        Iterator it = outgoing.iterator();
        while (it.hasNext()) {
            edge = (SMEdge) it.next();
            if (edge.getTarget() == to && (edge.getLabel() == label || (label != null && label.equals(edge.getLabel())))) {
                return true;
            }
        }
        return false;
    }

    public void removeInEdge(SMEdge edge) {
        incoming.remove(edge);
    }

    public void removeOutEdge(SMEdge edge) {
        outgoing.remove(edge);
    }

    public Iterator getOutEdgeIterator() {
        return outgoing.iterator();
    }

    public Iterator getInEdgeIterator() {
        return incoming.iterator();
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
