package org.botnode.asm;

/**
 * An edge in the control flow graph of a method body. See {@link Label Label}.
 * 
 * @author Eric Bruneton
 */
class Edge {

    /**
     * Denotes a normal control flow graph edge.
     */
    static final int NORMAL = 0;

    static final int EXCEPTION = 0x7FFFFFFF;

    int info;

    /**
     * The successor block of the basic block from which this edge originates.
     */
    Label successor;

    /**
     * The next edge in the list of successors of the originating basic block.
     * See {@link Label#successors successors}.
     */
    Edge next;
}
