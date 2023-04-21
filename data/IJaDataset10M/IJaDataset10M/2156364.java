package mipt.math.sys.num.ode.step;

import mipt.math.Number;
import mipt.math.sys.num.ode.FixedStepAlgorithm;
import mipt.math.sys.num.ode.InitialAlgorithm;
import mipt.math.sys.num.ode.StepAlgorithmContext;
import mipt.math.sys.ode.ODEProblem;

/**
 * Can 1) simply get step appropriate to some piece (interval of X)
 *  or 2) can interpolate between pieces' ends (default is not interpolate!)
 * Piece.toX is left end, piece.step is right end (if interpolation is not used, 
 *  it is the same until left_end-Double.MIN_VALUE).
 * Pieces are ordered by accending of their toX's;
 *  for x larger than all pieces' toX's, default step is used.
 * @author Evdokimov
 */
public class PiecewiseStepAlgorithm extends FixedStepAlgorithm {

    public static class XContext extends EmptyContext {

        private Number X;

        public XContext(Number x) {
            X = x;
        }

        public final Number getCurrentX() {
            return X;
        }

        public void setCurrentX(Number X) {
            this.X = X;
        }
    }

    /**
	 * toX is the left end of the piece, step is from right end.
	 */
    public static class Piece {

        private Number step, toX;

        public Piece(Number step, Number toX) {
            this.step = step;
            this.toX = toX;
        }

        public final Number getStep() {
            return step;
        }

        public final Number getToX() {
            return toX;
        }
    }

    protected Piece[] pieces;

    protected Number X0;

    protected Number X1;

    /**
	 * 
	 */
    public PiecewiseStepAlgorithm() {
    }

    /**
	 * @param problem
	 * @param fixedPointCount
	 * @param interpolate - if false, step is step function
	 */
    public PiecewiseStepAlgorithm(ODEProblem problem, int basePointCount, Number otherStep, Number otherToX, boolean interpolate) {
        this(problem, basePointCount, new Piece[] { new Piece(otherStep, otherToX) }, interpolate);
    }

    /**
	 * @param problem
	 * @param fixedPointCount
	 * @param pieces
	 * @param interpolate - if false, step is step function
	 */
    public PiecewiseStepAlgorithm(ODEProblem problem, int basePointCount, Piece[] pieces, boolean interpolate) {
        super(problem, basePointCount);
        this.pieces = pieces;
        setInterpolate(interpolate, problem);
    }

    /**
	 * @param problem
	 * @param fixedStep
	 */
    public PiecewiseStepAlgorithm(ODEProblem problem, Number baseStep, Number otherStep, Number otherToX, boolean interpolate) {
        this(problem, baseStep, new Piece[] { new Piece(otherStep, otherToX) }, interpolate);
    }

    /**
	 * @param problem
	 * @param fixedStep
	 * @param pieces
	 * @param interpolate - if false, step is step function
	 */
    public PiecewiseStepAlgorithm(ODEProblem problem, Number baseStep, Piece[] pieces, boolean interpolate) {
        super(problem, baseStep);
        this.pieces = pieces;
        setInterpolate(interpolate, problem);
    }

    public void setStep(ODEProblem problem, int fixedPointCount) {
        super.setStep(problem, fixedPointCount);
        X0 = problem.getX0();
    }

    public void setStep(ODEProblem problem, Number fixedStep) {
        super.setStep(problem, fixedStep);
        X0 = problem.getX0();
    }

    /**
	 * @return interpolate
	 */
    public final boolean isInterpolate() {
        return X1 != null;
    }

    /**
	 * @param interpolate
	 */
    public void setInterpolate(boolean interpolate, ODEProblem problem) {
        X0 = problem.getX0();
        if (interpolate) {
            X1 = problem.getX1();
        } else {
            X1 = null;
        }
    }

    /**
	 * @see mipt.math.sys.num.ode.FixedStepAlgorithm#initContext(mipt.math.sys.num.ode.InitialAlgorithm)
	 */
    public StepAlgorithmContext initContext(InitialAlgorithm alg) {
        return new XContext(X0);
    }

    /**
	 * @see mipt.math.sys.num.ode.StepAlgorithm#getStep(mipt.math.sys.num.ode.StepAlgorithmContext)
	 */
    public Number getStep(StepAlgorithmContext context) {
        if (pieces == null || pieces.length == 0) return super.getStep(context);
        Number s = this.step, x = X1, X = context.getCurrentX();
        for (int i = pieces.length - 1; i >= 0; i--) {
            int iStep = i + 1;
            Number xx = pieces[i].getToX();
            if (iStep == pieces.length) {
                if (X.compareTo(xx) >= 0) {
                    return s;
                }
            } else {
                if (X.compareTo(xx) >= 0) {
                    if (isInterpolate()) {
                        return Number.minus(pieces[iStep].getStep(), s).mult(Number.minus(X, x)).divide(Number.minus(xx, x)).add(s);
                    } else {
                        return pieces[iStep].getStep();
                    }
                } else {
                    s = pieces[iStep].getStep();
                }
            }
            x = pieces[i].getToX();
        }
        if (isInterpolate()) {
            return Number.minus(pieces[0].getStep(), s).mult(Number.minus(X, x)).divide(Number.minus(X0, x)).add(s);
        } else {
            return pieces[0].getStep();
        }
    }
}
