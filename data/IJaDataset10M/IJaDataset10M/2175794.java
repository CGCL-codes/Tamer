package com.hp.hpl.jena.reasoner.rulesys.impl;

import com.hp.hpl.jena.graph.*;
import java.util.*;

/**
 * Represents one input left of a join node. The queue points to 
 * a sibling queue representing the other leg which should be joined
 * against.
 * 
 * @author <a href="mailto:der@hplb.hpl.hp.com">Dave Reynolds</a>
 * @version $Revision: 1.9 $ on $Date: 2006/03/22 13:52:24 $
 */
public class RETEQueue implements RETESinkNode, RETESourceNode {

    /** A multi-set of partially bound envionments */
    protected HashMap queue = new HashMap();

    /** A set of variable indices which should match between the two inputs */
    protected byte[] matchIndices;

    /** The sibling queue which forms the other half of the join node */
    protected RETEQueue sibling;

    /** The node that results should be passed on to */
    protected RETESinkNode continuation;

    /** 
     * Constructor. The queue is not usable until it has been bound
     * to a sibling and a continuation node.
     * @param A set of variable indices which should match between the two inputs
     */
    public RETEQueue(byte[] matchIndices) {
        this.matchIndices = matchIndices;
    }

    /** 
     * Constructor. The queue is not usable until it has been bound
     * to a sibling and a continuation node.
     * @param A List of variable indices which should match between the two inputs
     */
    public RETEQueue(List matchIndexList) {
        int len = matchIndexList.size();
        matchIndices = new byte[len];
        for (int i = 0; i < len; i++) {
            matchIndices[i] = (byte) ((Number) matchIndexList.get(i)).intValue();
        }
    }

    /**
     * Set the sibling for this node.
     */
    public void setSibling(RETEQueue sibling) {
        this.sibling = sibling;
    }

    /**
     * Set the continuation node for this node (and any sibling)
     */
    public void setContinuation(RETESinkNode continuation) {
        this.continuation = continuation;
        if (sibling != null) sibling.continuation = continuation;
    }

    /** 
     * Propagate a token to this node.
     * @param env a set of variable bindings for the rule being processed. 
     * @param isAdd distinguishes between add and remove operations.
     */
    public void fire(BindingVector env, boolean isAdd) {
        Count count = (Count) queue.get(env);
        if (count == null) {
            if (!isAdd) return;
            queue.put(env, new Count(1));
        } else {
            if (isAdd) {
                count.inc();
            } else {
                count.dec();
                if (count.getCount() == 0) {
                    queue.remove(env);
                }
            }
        }
        for (Iterator i = sibling.queue.keySet().iterator(); i.hasNext(); ) {
            Node[] candidate = ((BindingVector) i.next()).getEnvironment();
            Node[] envNodes = env.getEnvironment();
            boolean matchOK = true;
            for (int j = 0; j < matchIndices.length; j++) {
                int index = matchIndices[j];
                if (!candidate[index].sameValueAs(envNodes[index])) {
                    matchOK = false;
                    break;
                }
            }
            if (matchOK) {
                Node[] newNodes = new Node[candidate.length];
                for (int j = 0; j < candidate.length; j++) {
                    Node n = candidate[j];
                    newNodes[j] = (n == null) ? envNodes[j] : n;
                }
                BindingVector newEnv = new BindingVector(newNodes);
                continuation.fire(newEnv, isAdd);
            }
        }
    }

    /**
     * Inner class used to represent an updatable count.
     */
    protected static class Count {

        /** the count */
        int count;

        /** Constructor */
        public Count(int count) {
            this.count = count;
        }

        /** Access count value */
        public int getCount() {
            return count;
        }

        /** Increment the count value */
        public void inc() {
            count++;
        }

        /** Decrement the count value */
        public void dec() {
            count--;
        }

        /** Set the count value */
        public void setCount(int count) {
            this.count = count;
        }
    }

    /**
     * Clone this node in the network.
     * @param context the new context to which the network is being ported
     */
    public RETENode clone(Map netCopy, RETERuleContext context) {
        RETEQueue clone = (RETEQueue) netCopy.get(this);
        if (clone == null) {
            clone = new RETEQueue(matchIndices);
            netCopy.put(this, clone);
            clone.setSibling((RETEQueue) sibling.clone(netCopy, context));
            clone.setContinuation((RETESinkNode) continuation.clone(netCopy, context));
            clone.queue.putAll(queue);
        }
        return clone;
    }
}
