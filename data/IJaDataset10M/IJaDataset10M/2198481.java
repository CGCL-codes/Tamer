package x10me.opt.controlflow;

import java.lang.reflect.Constructor;
import x10me.opt.driver.CompilerPhase;
import x10me.opt.driver.OptOptions;
import x10me.opt.ir.IR;

/**
 * Driver routine for dominator computation.  This phase invokes
 * the Lengauer-Tarjan dominator calculation.
 */
public final class DominatorsPhase extends CompilerPhase {

    /**
   * Should we unfactor the CFG?
   */
    private final boolean unfactor;

    /**
   * @param unfactor Should we unfactor the CFG before computing
   * dominators?
   */
    public DominatorsPhase(IR ir, boolean unfactor) {
        super(ir);
        this.unfactor = unfactor;
    }

    /**
   * Should this phase be performed?  This is a member of a composite
   * phase, so always return true.  The parent composite phase will
   * dictate.
   * @param options controlling compiler options
   */
    public boolean shouldPerform(OptOptions options) {
        return true;
    }

    /**
   * Return a string representation of this phase
   * @return "Dominators + LpStrTree"
   */
    public String getName() {
        return "Dominators + LpStrTree";
    }

    /**
   * Should the IR be printed before and/or after this phase?
   * @param options controlling compiler options
   * @param before query control
   * @return true or false
   */
    public boolean printingEnabled(OptOptions options, boolean before) {
        return false;
    }

    public void perform() {
        ir.dominatorTree = null;
        LTDominators.perform(ir, true, unfactor);
        DominatorTree.perform(ir, true);
        LSTGraph.perform(ir);
    }
}
