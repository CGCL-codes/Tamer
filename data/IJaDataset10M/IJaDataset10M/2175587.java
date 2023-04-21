package uk.ac.imperial.ma.metric.engine.mathematics.algebra.numbers;

import uk.ac.imperial.ma.metric.engine.mathematics.BinaryBranchExpressionNodeImpl;
import uk.ac.imperial.ma.metric.engine.mathematics.ExpressionNode;
import uk.ac.imperial.ma.metric.engine.mathematics.algebra.functions.AdditionFunction;
import uk.ac.imperial.ma.metric.engine.mathematics.set.Set;

/**
 * TODO Describe this type here.
 *
 * @author <a href="mailto:mail@daniel.may.name">Daniel J. R. May</a>
 * @version 0.1, 24 Nov 2008
 */
public class RealAdditionFunctionImpl extends BinaryBranchExpressionNodeImpl<Real, Real> implements AdditionFunction<Real, Real, Real> {

    /**
	 * Constructs.
	 *
	 * @param firstChild
	 * @param secondChild
	 */
    public RealAdditionFunctionImpl(Real firstChild, Real secondChild) {
        super(firstChild, secondChild);
    }

    /**
	 * @see uk.ac.imperial.ma.metric.engine.mathematics.BinaryBranchExpressionNodeImpl#approximate()
	 */
    @Override
    public ExpressionNode approximate() {
        return null;
    }

    /**
	 * @see uk.ac.imperial.ma.metric.engine.mathematics.BinaryBranchExpressionNodeImpl#evaluate()
	 */
    @Override
    public ExpressionNode evaluate() {
        ExpressionNode answer = evaluate(firstChild, secondChild);
        ;
        if (answer == null) {
            return this;
        } else {
            return answer;
        }
    }

    /**
	 * @see uk.ac.imperial.ma.metric.engine.mathematics.BinaryBranchExpressionNodeImpl#simplify()
	 */
    @Override
    public ExpressionNode simplify() {
        return null;
    }

    /**
	 * @see uk.ac.imperial.ma.metric.engine.mathematics.algebra.functions.BinaryFunction#getCodomain()
	 */
    @Override
    public Set<Real> getCodomain() {
        return null;
    }

    /**
	 * @see uk.ac.imperial.ma.metric.engine.mathematics.algebra.functions.BinaryFunction#getFirstDomain()
	 */
    @Override
    public Set<Real> getFirstDomain() {
        return null;
    }

    /**
	 * @see uk.ac.imperial.ma.metric.engine.mathematics.algebra.functions.BinaryFunction#getSecondDomain()
	 */
    @Override
    public Set<Real> getSecondDomain() {
        return null;
    }

    public static Real evaluate(Real a, Real b) {
        if (a instanceof Natural && b instanceof Natural) {
            return NaturalAdditionFunctionImpl.evaluate((Natural) a, (Natural) b);
        } else if (a instanceof Decimal && b instanceof Decimal) {
            return DecimalAdditionFunctionImpl.evaluate((Decimal) a, (Decimal) b);
        } else {
            return null;
        }
    }

    @Override
    public Set<Real> getRange() {
        return null;
    }
}
