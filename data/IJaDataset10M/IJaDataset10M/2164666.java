package com.thesett.aima.logic.fol;

/**
 * VariableVisitor provides a visitor pattern over variables.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Visit a variable.
 * </table></pre>
 *
 * @author Rupert Smith
 */
public interface VariableVisitor extends TermVisitor {

    /**
     * Visits a variable.
     *
     * @param variable The variable to visit.
     */
    public void visit(Variable variable);
}
