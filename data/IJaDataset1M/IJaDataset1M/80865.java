package uk.ac.imperial.ma.mathsEngine.mathematics.arithmetic;

import uk.ac.imperial.ma.mathsEngine.mathematics.ExpressionNode;

/**
 * @author <a href="mailto:d.may@imperial.ac.uk">Daniel J. R. May</a>
 * @version 0.1, 07-Nov-2005
 *
 */
public interface Minus extends ExpressionNode {

    /** */
    public ExpressionNode minus(ExpressionNode arg0, ExpressionNode arg1);
}
