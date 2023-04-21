package uk.ac.mmu.manmetassembly.instructions;

import uk.ac.mmu.manmetassembly.core.BranchController;
import uk.ac.mmu.manmetassembly.core.FlagController;
import uk.ac.mmu.manmetassembly.core.SyntaxException;

/**
 * Js - Jump if sign flag set
 * @mmu:author Warren Li
 * @version 1.0
 */
public class Js extends uk.ac.mmu.manmetassembly.core.Instruction implements Jump {

    private String type;

    public Js() {
    }

    /**
     * implemented method
     *
     */
    public void execute() {
        if ((FlagController.getFlagState("SIGN"))) {
            BranchController.setBranchBool(false);
        } else {
            BranchController.setBranchBool(true);
        }
    }

    public String getDescription() {
        return type;
    }

    public String getJumpType() {
        return type;
    }

    public void validate() throws SyntaxException {
    }

    /**
     * Overrides Object's toString() method
     *
     * @return a String representing this object
     */
    public String toString() {
        return "JS";
    }
}
