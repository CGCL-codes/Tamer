package com.thesett.aima.logic.fol.vam.vam2p.instructions;

import com.thesett.common.util.visitor.Visitor;

/**
 * Goal is a VAM2P instruction.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Encode a VAM2P instruction.
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class Goal extends VAM2PInstruction {

    /**
     * Creates a VAM2P inGoalion with the specified argument.
     *
     * @param name The name of the functor argument.
     */
    public Goal(int name) {
        super(VAM2PInstructionSet.Goal);
    }

    /** {@inheritDoc} */
    public void accept(Visitor<VAM2PInstruction> visitor) {
        if (visitor instanceof GoalVisitor) {
            ((GoalVisitor) visitor).visit(this);
        } else {
            super.accept(visitor);
        }
    }
}
