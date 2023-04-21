package com.thesett.aima.logic.fol.prolog.expressions;

import com.thesett.aima.logic.fol.DoubleLiteral;
import com.thesett.aima.logic.fol.IntLiteral;
import com.thesett.aima.logic.fol.NumericType;
import com.thesett.aima.logic.fol.Term;

/**
 * UMinus implements the unary minus operator '-' in Prolog.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Negate a number.
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class UMinus extends UnaryArithmeticOperator {

    /**
     * Creates a new '-' expression operator.
     *
     * @param name      The interned name of the operator.
     * @param arguments The arguments, of which there must be two.
     */
    public UMinus(int name, Term[] arguments) {
        super(name, arguments);
    }

    /**
     * Evaluates the arithmetic operator on its numeric argument.
     *
     * @param  firstNumber The first argument.
     *
     * @return The result of performing the arithmetic operator on its argument.
     */
    protected NumericType evaluate(NumericType firstNumber) {
        if (firstNumber.isInteger()) {
            return new IntLiteral(-firstNumber.intValue());
        } else {
            return new DoubleLiteral(-firstNumber.doubleValue());
        }
    }
}
