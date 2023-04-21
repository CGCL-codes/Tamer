package com.thesett.aima.logic.fol.vam.vam2p.instructions;

import com.thesett.common.util.visitor.Visitor;

/**
 * Void is a VAM2P instruction.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Encode a VAM2P instruction.
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class Void extends VAM2PInstruction {

    /** Creates a VAM2P instruction. */
    public Void() {
        super(VAM2PInstructionSet.Void);
    }

    /** {@inheritDoc} */
    public void accept(Visitor<VAM2PInstruction> visitor) {
        if (visitor instanceof VoidVisitor) {
            ((VoidVisitor) visitor).visit(this);
        } else {
            super.accept(visitor);
        }
    }
}
