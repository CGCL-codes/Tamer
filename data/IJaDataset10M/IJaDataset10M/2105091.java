package mipt.math.sys.num.ode;

import mipt.math.Number;
import mipt.math.array.Vector;
import mipt.math.sys.num.Method;
import mipt.math.sys.ode.ODEProblem;

/**
 * Default implementation of adaptive step control: 1) computation of monitoring parameter
 *   delta ("method local error" or "solution change speed") is for subclasses; 2) step
 *   computation by delta is for StepMethod delegate (must be set or created by subclass!)
 * Stores accuracy to achieve and some step values: minimum, maximum (may be null), initial 
 * Can treat accuracy as relative (by default), scaling the delta by values proposed by
 *   ScaleForStepMethod (if it is not set, FullScaleForStepMethod is created)
 * Your can set FixedStepMethod (inner class) as StepMethod to make this class compute only 
 *  error (discrepancy) by subclass'es calcDelta() method and by ScaleMethod.
 * Note: It is better to change accuracy according to the InitialMethod's order
 * Note: dont' forget to override initContext if you need another data in it 
 * @author evdokimov
 */
public abstract class AdaptStepAlgorithm implements StepAlgorithm {

    public static Number MINIMUM_STEP = Number.createScalar(1.e-08);

    protected double accuracy = 1e-4;

    protected boolean isAccuracyRelative = true;

    protected Number initialStep;

    protected Number minimumStep;

    protected Number maximumStep;

    protected double delta = Double.NaN;

    protected StepMethod stepMethod = null;

    protected ScaleForStepMethod scaleMethod = null;

    public static class FixedStepMethod extends FixedStepAlgorithm implements StepMethod {

        public FixedStepMethod() {
            super();
        }

        public FixedStepMethod(double stepValue) {
            super(stepValue);
        }

        public FixedStepMethod(ODEProblem problem, int fixedPointCount) {
            super(problem, fixedPointCount);
        }

        public FixedStepMethod(ODEProblem problem, Number fixedStep) {
            super(problem, fixedStep);
        }

        /**
		 * @see mipt.math.sys.num.ode.StepMethod#calcStep(double, double, mipt.math.sys.num.ode.StepAlgorithmContext)
		 */
        public final Number calcStep(double accuracy, double delta, StepAlgorithmContext context) {
            return getStep(context);
        }
    }

    /**
	 * 
	 */
    public AdaptStepAlgorithm() {
    }

    /**
	 * 
	 */
    public AdaptStepAlgorithm(double accuracy, Number approxStep, StepMethod stepMethod) {
        setAccuracy(accuracy);
        setDefaultSteps(approxStep);
        setStepMethod(stepMethod);
    }

    /**
	 * 
	 */
    public AdaptStepAlgorithm(double accuracy, boolean isAccuracyRelative, Number initialStep, Number minimumStep, Number maximumStep, StepMethod stepMethod, ScaleForStepMethod scaleMethod) {
        setAccuracy(accuracy, isAccuracyRelative);
        setInitialStep(initialStep);
        setMinimumStep(minimumStep);
        setMaximumStep(maximumStep);
        setStepMethod(stepMethod);
        setScaleMethod(scaleMethod);
    }

    /**
	 * Return delta - current error estimate (can be used not only for step control)
	 * Call after getStep()
	 */
    public final double getErrorDelta() {
        return delta;
    }

    /**
	 * @see mipt.math.sys.num.ode.StepAlgorithm#getStep(mipt.math.sys.num.ode.StepAlgorithmContext, mipt.math.Number)
	 */
    public Number getStep(StepAlgorithmContext context) {
        if (context.getOldStep() == null) {
            if (getStepMethod() instanceof FixedStepMethod) initialStep = getStepMethod().calcStep(accuracy, delta, context);
            return initialStep;
        }
        Vector deltaVector = calcDelta(context);
        delta = getScaleMethod().calcDelta(deltaVector, context);
        Number step = getStepMethod().calcStep(accuracy, delta, context);
        context.setSolution(deltaVector);
        if (minimumStep != null && step.compareTo(minimumStep) < 0) step = minimumStep; else if (step.compareTo(MINIMUM_STEP) < 0) step = MINIMUM_STEP;
        if (maximumStep != null && step.compareTo(maximumStep) > 0) step = maximumStep;
        if (step.equals(context.getOldStep())) context.setShouldRepeatStep(false);
        return step;
    }

    /**
	 * 
	 */
    protected abstract Vector calcDelta(StepAlgorithmContext context);

    /**
	 * Is often overriden
	 * @see mipt.math.sys.num.ode.StepAlgorithm#initContext()
	 */
    public StepAlgorithmContext initContext(InitialAlgorithm alg) {
        return new DefaultStepAlgorithmContext();
    }

    /**
	 * @see mipt.math.sys.num.Algorithm#setMethod(mipt.math.sys.num.Method)
	 */
    public void setMethod(Method method) {
        if (method instanceof StepMethod) setStepMethod((StepMethod) method); else setScaleMethod((ScaleForStepMethod) method);
    }

    /**
	 * Can be overriden!
	 */
    public StepMethod getStepMethod() {
        return stepMethod;
    }

    /**
	 * No StepMethod by default (if subclass does not override)
	 */
    public void setStepMethod(StepMethod stepMethod) {
        this.stepMethod = stepMethod;
    }

    /**
	 * @return ScaleForStepMethod
	 */
    public ScaleForStepMethod getScaleMethod() {
        if (scaleMethod == null) scaleMethod = initScaleMethod();
        return scaleMethod;
    }

    /**
	 * 
	 */
    protected ScaleForStepMethod initScaleMethod() {
        return new FullScaleForStepMethod();
    }

    /**
	 * By default FullScaleForStepMethod
	 */
    public void setScaleMethod(ScaleForStepMethod scaleMethod) {
        this.scaleMethod = scaleMethod;
    }

    /**
	 * 
	 */
    public final void setAccuracy(double accuracy, boolean isAccuracyRelative) {
        setAccuracy(accuracy);
        setAccuracyRelative(isAccuracyRelative);
    }

    /**
	 * 
	 */
    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    /**
	 * 
	 */
    public void setAccuracyRelative(boolean isAccuracyRelative) {
        this.isAccuracyRelative = isAccuracyRelative;
    }

    /**
	 * Must be set
	 */
    public void setInitialStep(Number initialStep) {
        this.initialStep = initialStep;
    }

    /**
	 * Can be null
	 */
    public void setMaximumStep(Number maximumStep) {
        this.maximumStep = maximumStep;
    }

    /**
	 * Can be null
	 */
    public void setMinimumStep(Number minimumStep) {
        this.minimumStep = minimumStep;
    }

    /**
	 * Set all three steps: the given one become initial, minimum = initial/100,
	 *  minimum = initial*10
	 */
    public void setDefaultSteps(Number averageStep) {
        setInitialStep(averageStep);
        setMinimumStep(averageStep.copy().mult(0.01));
        setMaximumStep(averageStep.copy().mult(10.0));
    }
}
