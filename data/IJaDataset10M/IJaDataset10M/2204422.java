package org.servingMathematics.mathematics.impl.operators;

import org.servingMathematics.mathematics.impl.MathInvalidArgumentException;
import org.servingMathematics.mathematics.impl.numbers.MathIntegerImpl;
import org.servingMathematics.mathematics.impl.numbers.RationalImpl;
import org.servingMathematics.mathematics.impl.numbers.RealImpl;
import org.servingMathematics.mathematics.interfaces.MathNode;
import org.servingMathematics.mathematics.interfaces.MathObject;
import org.servingMathematics.mathematics.interfaces.Substitutable;
import org.servingMathematics.mathematics.interfaces.numbers.MathInteger;
import org.servingMathematics.mathematics.interfaces.numbers.MathNumber;
import org.servingMathematics.mathematics.interfaces.numbers.Rational;
import org.servingMathematics.mathematics.interfaces.numbers.Real;
import org.servingMathematics.mathematics.interfaces.operators.Abs;

/**
 * @author Phil Ramsden, <a href="mailto:d.may@imperial.ac.uk">Daniel J. R. May</a>
 * @version 0.2, 30 March 2005
 *
 * AbsImpl is a class whose instances are trees representing mathematical expressions
 * whose head is "abs".
 */
public class AbsImpl implements Abs, Substitutable {

    /**
     * Array of child nodes (one member).
     */
    protected MathNode[] children;

    /**
     * Class constructor.
     */
    public AbsImpl() {
    }

    /**
     * Class constructor specifying child nodes.
     *
     * @param children array of child nodes
     */
    public AbsImpl(MathNode[] children) {
        this.setChildren(children);
    }

    /**
     * Names of the variables associated with the expression of which this <code>MathNode</code> forms all or part.
     * These  need only be set if the expression is to be evaluated.
     */
    private String[] variables;

    /**
     * Values of the variables associated with the expression of which this <code>MathNode</code> forms all or part.
     * These  need only be set if the expression is to be evaluated.
     */
    private MathObject[] variableValues;

    /**
     * Sets the names of the variables associated with the expression of which this <code>MathNode</code> forms all or part.
     * This method need only be called if the expression is to be evaluated.
     *
     * @param variables the names of the variables associated with the expression of which this <code>MathNode</code> forms all or part
     */
    public void setVariables(String[] variables) {
        this.variables = variables;
    }

    /**
     * Sets the values of the variables associated with the expression of which this <code>MathNode</code> forms all or part.
     * This method need only be called if the expression is to be evaluated.
     *
     * @param variableValues the values of the variables associated with the expression of which this <code>MathNode</code> forms all or part
     * @see org.servingMathematics.mathematics.interfaces.MathObject
     */
    public void setVariableValues(MathObject[] variableValues) {
        this.variableValues = variableValues;
    }

    /**
     * Returns the names of the variables associated with the expression of which this <code>MathNode</code> forms all or part.
     *
     * @return the names of the variables associated with the expression of which this <code>MathNode</code> forms all or part
     */
    public String[] getVariables() {
        return this.variables;
    }

    /**
     * Returns the values of the variables associated with the expression of which this <code>MathNode</code> forms all or part.
     *
     * @return the values of the variables associated with the expression of which this <code>MathNode</code> forms all or part
     * @see MathObject
     */
    public MathObject[] getVariableValues() {
        return this.variableValues;
    }

    /**
     * Returns the MathNode type (in this case, ABS).
     *
     * @return the MathNode type (in this case, ABS)
     * @see MathNode
     */
    public int getType() {
        return MathNode.ABS;
    }

    /**
     * Returns <code>false</code>, indicating that this <code>MathNode</code> is not a <code>MathLeafNode</code>.
     *
     * @return <code>false</code>, indicating that this <code>MathNode</code> is not a <code>MathLeafNode</code>
     * @see org.servingMathematics.mathematics.interfaces.MathNode
     * @see org.servingMathematics.mathematics.interfaces.MathLeafNode
     */
    public boolean isLeaf() {
        return false;
    }

    /**
     * Returns a <code>boolean</code> indicating whether this <code>MathNode</code> contains any purely symbolic elements.
     *
     * @return a <code>boolean</code> indicating whether this <code>MathNode</code> contains any purely symbolic elements
     */
    public boolean isSymbolicExpression() {
        return children[0].isSymbolicExpression();
    }

    /**
     * Returns the value of the expression or subexpression of which this <code>MathNode</code> forms the head,
     * substituting for symbolic quantities where necessary.
     *
     * @return the value of the expression or subexpression of which this <code>MathNode</code> forms the head
     */
    public MathObject evaluate() throws Exception {
        MathObject child0Value = this.children[0].evaluate();
        if (child0Value.isMathNumber()) return (MathObject) abs((MathNumber) child0Value);
        throw new MathInvalidArgumentException("The modulus/absolute value function is only defined for MathNumbers");
    }

    /**
     * Converts this <code>MathNode</code> tree to a string.
     *
     * @return string representing this <code>MathNode</code> tree
     */
    public String toString() {
        return "abs(" + children[0].toString() + ")";
    }

    /**
	 * Returns the array of child nodes.
	 *
	 * @return the array of child nodes
	 */
    public MathNode[] getChildren() {
        return this.children;
    }

    /**
	 * Sets the array of child nodes.
	 *
	 * @param the array of child nodes
	 */
    public void setChildren(MathNode[] children) {
        this.children = children;
    }

    /**
	 * Calculates the absolute value of any type of number
	 *
	 * @param x a <code>MathNumber</code>, any implementation
	 * @return the absolute value, as an appropriately implemented <code>MathNumber</code>
	 * (<code>MathIntegerImpl</code>, <code>RationalImpl</code> or <code>RealImpl</code>)
	 * @see org.servingMathematics.mathematics.interfaces.numbers.MathNumber
	 */
    public static MathNumber abs(MathNumber x) throws Exception {
        if (x.isMathInteger()) {
            int intX = ((MathInteger) x).intValue();
            intX = (intX < 0 ? -intX : intX);
            return new MathIntegerImpl(intX);
        }
        if (x.isRational()) {
            Rational ratX = ((Rational) x).getRationalValue();
            int xNum = ratX.getNumeratorAsInt();
            int xDen = ratX.getDenominatorAsInt();
            xNum = (xNum < 0 ? -xNum : xNum);
            return new RationalImpl(xNum, xDen);
        }
        if (x.isReal()) {
            double dblX = ((Real) x).doubleValue();
            dblX = (dblX < 0.0 ? -dblX : dblX);
            return new RealImpl(dblX);
        }
        Real xRe = x.getRealPart();
        Real xIm = x.getImaginaryPart();
        Real absXSquared = (Real) PlusImpl.plus(TimesImpl.times(xRe, xRe), TimesImpl.times(xIm, xIm));
        return SqrtImpl.sqrt(absXSquared);
    }
}
