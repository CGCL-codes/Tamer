package com.sparkit.extracta.subdoc;

import com.sparkit.extracta.*;

/**
 * NodeRange represents a range of nodes of a document.
 *
 * @version 1.0
 * @author Dominik Roblek
 * @author Bostjan Vester
 * @author Dejan Pazin
 */
public class NodeRange implements Comparable {

    /**
   * The first node of the range.
   */
    protected RankedNode m_firstRankedNode;

    /**
   * The last node of the range.
   */
    protected RankedNode m_lastRankedNode;

    private Boolean m_singleton;

    private Integer m_congruence;

    /**
   * Creates a new singleton node range which contains only the specified node.
   *
   * @param rankedNode the specified node.
   */
    public NodeRange(RankedNode rankedNode) {
        this(rankedNode, rankedNode);
    }

    /**
   * Creates a new node range, which is limitied by specified nodes.
   *
   * @param firstRankedNode the first node of the range.
   * @param lastRankedNode the last node of the range.
   */
    public NodeRange(RankedNode firstRankedNode, RankedNode lastRankedNode) {
        m_firstRankedNode = firstRankedNode;
        m_lastRankedNode = lastRankedNode;
    }

    /**
   * Gets the first node of the range.
   */
    public RankedNode getFirst() {
        return m_firstRankedNode;
    }

    /**
   * Sets the first node of the range.
   */
    public void setFirst(RankedNode firstRankedNode) {
        invalidate();
        m_firstRankedNode = firstRankedNode;
    }

    /**
   * Gets the last node of the range.
   */
    public RankedNode getLast() {
        return m_lastRankedNode;
    }

    /**
   * Sets the last node of the range.
   */
    public void setLast(RankedNode lastRankedNode) {
        invalidate();
        m_lastRankedNode = lastRankedNode;
    }

    private void invalidate() {
        m_singleton = null;
        m_congruence = null;
    }

    /**
   * Returns true, if the range contains only one node.
   */
    public boolean isSingleton() {
        if (m_singleton == null) {
            boolean bSingleton = m_firstRankedNode.equals(m_lastRankedNode);
            m_singleton = new Boolean(bSingleton);
        }
        return m_singleton.booleanValue();
    }

    /**
   * Calculates congruence between the first and the last node, which at one
   * time is congruence between all nodes of the range.
   *
   * @see com.sparkit.extracta.RankedNode#getCongruence(com.sparkit.extracta.RankedNode)
   */
    public int getCongruence() {
        if (m_congruence == null) {
            int nCongruence = m_firstRankedNode.getCongruence(m_lastRankedNode);
            m_congruence = new Integer(nCongruence);
        }
        return m_congruence.intValue();
    }

    /**
   * This method compares two nodes as specified in Comparable interface.
   *
   * @see java.lang.Comparable
   */
    public int compareTo(Object obj) {
        NodeRange nodeRangeA = this;
        NodeRange nodeRangeB = (NodeRange) obj;
        if (nodeRangeA.getFirst().equals(nodeRangeB.getFirst())) {
            if (nodeRangeB.getLast().equals(nodeRangeB.getLast())) {
                return 0;
            } else if (nodeRangeA.getLast().isFollowing(nodeRangeB.getLast(), false)) {
                return -1;
            } else {
                return 1;
            }
        } else if (nodeRangeA.getFirst().isPreceding(nodeRangeB.getFirst(), false)) {
            return -1;
        } else {
            return 1;
        }
    }
}
