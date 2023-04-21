package org.jikesrvm.opt;

import org.jikesrvm.opt.ir.*;

/**
 * Driver routine for dominator tree computation
 *
 * @author Michael Hind
 */
public final class OPT_DominatorTreePhase extends OPT_CompilerPhase {

    /**
   * Should this phase be performed?
   * @param options controlling compiler options
   * @return true or false
   */
    public boolean shouldPerform(OPT_Options options) {
        return options.SSA || options.PRINT_DOMINATORS;
    }

    /**
   * Returns "Dominator Tree"
   * @return "Dominator Tree"
   */
    public String getName() {
        return "Dominator Tree";
    }

    /**
   * Should the IR be printed before and/or after this phase?
   * @param options controlling compiler options
   * @param before query control
   * @return true or false.
   */
    public boolean printingEnabled(OPT_Options options, boolean before) {
        return false;
    }

    /**
   * Main driver.
   *
   * @param ir the governing IR
   */
    public void perform(OPT_IR ir) {
        if (!ir.HIRInfo.dominatorsAreComputed) return;
        try {
            OPT_DominatorTree.perform(ir, true);
        } catch (OPT_OperationNotImplementedException e) {
            if (ir.options.PRINT_DOMINATORS || ir.options.PRINT_SSA) {
                OPT_Compiler.report(e.getMessage());
            }
        }
    }
}
