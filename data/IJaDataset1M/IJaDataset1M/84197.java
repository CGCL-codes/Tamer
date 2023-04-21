package com.thesett.aima.logic.fol.vam.vamai.instructions;

/**
 * VoidVisitor is a visitor for a vam ai instruction.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Visit a vam ai instruction.
 * </table></pre>
 *
 * @author Rupert Smith
 */
public interface VoidVisitor extends VAMAIInstructionVisitor {

    /**
     * Visits a vam ai instruction.
     *
     * @param instruction The instruction to visit.
     */
    public void visit(Void instruction);
}
