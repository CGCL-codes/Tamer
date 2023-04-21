package pso.runner;

import java.util.TimerTask;
import pso.common.NdVector;
import pso.problem.APsoProblem;
import pso.ui.display.APsoDisplay;
import pso.variant.APsoVariant;

public abstract class APsoRunner extends TimerTask implements IPsoRunner {

    private static final long stepSleepTime = 500;

    protected APsoVariant variant;

    protected APsoProblem problem;

    protected APsoDisplay display;

    protected int iteration;

    private static final int maxIterations = pso.common.PsoConstants.PSO_VARIANT_ITERATIONS_DEFAULT;

    protected float epsilon;

    protected static final float finalEpsilon = pso.common.PsoConstants.PSO_VARIANT_EPSILON_DEFAULT;

    protected long time;

    protected static final long maxTime = pso.common.PsoConstants.PSO_VARIANT_TIME_DEFAULT;

    protected long iterationDelay;

    protected static final long iterationDelayDefault = pso.common.PsoConstants.PSO_VARIANT_ITERATION_DELAY_DEFAULT;

    protected int termination;

    protected static final int defaultTermination = pso.common.PsoConstants.PSO_VARIANT_TERMINATION_DEFAULT;

    protected Object steppingModeMutex = new Object();

    protected boolean inSteppingMode = true;

    protected Object cancelledMutex = new Object();

    protected boolean isCancelled = false;

    public APsoRunner() {
        iterationDelay = iterationDelayDefault;
        termination = defaultTermination;
    }

    @Override
    public void initialize(NdVector[] ic, NdVector[] iv) {
        variant.attachRunner(this);
        variant.setProblem(problem);
        variant.initialize(ic, iv);
        display.attachVariant(variant);
        display.attachProblem(problem);
        display.attachSwarm(variant.getSwarm());
    }

    @Override
    public final void run() {
        final float epsMax = pso.common.PsoConstants.PSO_VARIANT_EPSILON_MAX;
        boolean stepSleep = true;
        for (iteration = 0, time = 0, epsilon = epsMax; !shouldTerminate(); updateIteration(), updateTime(), updateEpsilon()) {
            if (isCancelled) {
                System.out.println("psoRunner Thread cancelled");
                return;
            }
            if (inSteppingMode) {
                stepSleep = true;
            } else {
                stepSleep = false;
                updateAll();
            }
            try {
                if (stepSleep) Thread.sleep(stepSleepTime); else Thread.sleep(iterationDelay);
            } catch (InterruptedException e) {
            }
        }
    }

    protected final boolean shouldTerminate() {
        int term = termination;
        int e = pso.common.PsoConstants.PSO_VARIANT_TERMINATION_EPS;
        int t = pso.common.PsoConstants.PSO_VARIANT_TERMINATION_TIME;
        int i = pso.common.PsoConstants.PSO_VARIANT_TERMINATION_ITER;
        if (((term & e) != 0 && epsilon <= finalEpsilon) || ((term & t) != 0 && time >= maxTime) || ((term & i) != 0 && iteration >= maxIterations)) {
            System.out.println("psoRunner Thread terminating at iteration " + iteration);
            return true;
        }
        return false;
    }

    protected final void updateEpsilon() {
        if (!inSteppingMode) {
            final int e = pso.common.PsoConstants.PSO_VARIANT_TERMINATION_EPS;
            if ((termination & e) != 0) {
                updateEpsilonPriv();
            }
        }
    }

    protected abstract void updateEpsilonPriv();

    protected final void updateTime() {
        if (!inSteppingMode) {
            final int t = pso.common.PsoConstants.PSO_VARIANT_TERMINATION_TIME;
            if ((termination & t) != 0) {
                updateTimePriv();
            }
        }
    }

    protected abstract void updateTimePriv();

    protected final void updateIteration() {
        if (!inSteppingMode) {
            final int i = pso.common.PsoConstants.PSO_VARIANT_TERMINATION_ITER;
            if ((termination & i) != 0) {
                updateIterationPriv();
            }
        }
    }

    protected void updateIterationPriv() {
        iteration++;
    }

    @Override
    public final boolean cancel() {
        isCancelled = true;
        return true;
    }

    @Override
    public final long scheduledExecutionTime() {
        return 0;
    }

    private final void updateAll() {
        variant.updateAll();
        display.repaint();
    }

    @Override
    public final void setPsoVariant(APsoVariant v) {
        variant = v;
    }

    @Override
    public final APsoVariant getPsoVariant() {
        return variant;
    }

    @Override
    public final void setPsoProblem(APsoProblem p) {
        problem = p;
    }

    @Override
    public final APsoProblem getPsoProblem() {
        return problem;
    }

    @Override
    public final void setPsoDisplay(APsoDisplay d) {
        display = d;
    }

    @Override
    public final APsoDisplay getPsoDisplay() {
        return display;
    }

    @Override
    public final void setSteppingModeEnabled(boolean enabled) {
        if (inSteppingMode && !enabled) {
        }
        inSteppingMode = enabled;
        display.setSteppingEnabled(enabled);
    }

    @Override
    public final void step() {
        display.stepUpdate();
    }

    @Override
    public int getIteration() {
        return iteration;
    }

    @Override
    public int getMaxIterations() {
        return maxIterations;
    }
}
