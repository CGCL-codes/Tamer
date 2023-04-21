package org.jikesrvm.compilers.opt.regalloc;

import org.jikesrvm.compilers.opt.ir.IR;
import org.jikesrvm.compilers.opt.ir.Register;

/**
 * This class helps manage register preferences for coalescing and
 * register allocation.
 */
public abstract class GenericRegisterPreferences {

    /**
   * The main backing data structure;
   */
    private final CoalesceGraph graph = new CoalesceGraph();

    /**
   * Add a affinity of weight w between two registers.
   */
    protected void addAffinity(int w, Register r1, Register r2) {
        graph.addAffinity(w, r1, r2);
    }

    /**
   * Set up register preferences for an IR. This is machine-dependent.
   */
    public abstract void initialize(IR ir);

    /**
   * Return the backing graph holding the preferences.
   */
    public CoalesceGraph getGraph() {
        return graph;
    }
}
