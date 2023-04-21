package jmbench.tools.stability;

import jmbench.interfaces.MatrixProcessorInterface;
import jmbench.interfaces.RuntimePerformanceFactory;
import jmbench.tools.EvaluationTest;
import jmbench.tools.OutputError;
import jmbench.tools.TestResults;
import java.util.Random;

/**
 * @author Peter Abeles
 */
public abstract class StabilityTestBase extends EvaluationTest {

    protected RuntimePerformanceFactory factory;

    protected MatrixProcessorInterface operation;

    protected int totalTrials;

    protected double breakingPoint;

    protected transient Random rand;

    protected transient double foundResult;

    protected transient OutputError reason;

    protected transient StabilityTrialResults results;

    protected transient int numResults;

    protected StabilityTestBase(long randomSeed, RuntimePerformanceFactory factory, MatrixProcessorInterface operation, int totalTrials, double breakingPoint) {
        super(randomSeed);
        this.factory = factory;
        this.operation = operation;
        this.totalTrials = totalTrials;
        this.breakingPoint = breakingPoint;
    }

    public StabilityTestBase() {
    }

    public abstract void performTest();

    /**
     * The full name of the test being performed.
     *
     * @return Name of the test being performed.
     */
    public abstract String getTestName();

    /**
     * Name of the file this test should be saved to.
     *
     * @return File name.
     */
    public abstract String getFileName();

    @Override
    public void init() {
        rand = new Random(randomSeed);
        factory.configure();
    }

    @Override
    public void setupTrial() {
    }

    @Override
    public void printInfo() {
    }

    @Override
    public long getMaximumRuntime() {
        return -1;
    }

    @Override
    public TestResults evaluate() {
        results = new StabilityTrialResults();
        numResults = 0;
        performTest();
        return results;
    }

    protected void addUnexpectedException(Exception e) {
        String name = e.getClass().getSimpleName();
        for (ExceptionInfo i : results.unexpectedExceptions) {
            if (i.getShortName().compareTo(name) == 0) {
                i.numTimesThrown++;
                return;
            }
        }
        results.unexpectedExceptions.add(new ExceptionInfo(e));
    }

    protected int findMaxPow(double a) {
        for (int i = 0; true; i++) {
            double p = Math.pow(a, i);
            if (Double.isInfinite(p) || p == 0) return i;
        }
    }

    protected void saveResults() {
        results.breakingPoints.add(foundResult);
        switch(reason) {
            case NO_ERROR:
                results.numFinished++;
                break;
            case UNCOUNTABLE:
                results.numUncountable++;
                break;
            case LARGE_ERROR:
                results.numLargeError++;
                break;
            case UNEXPECTED_EXCEPTION:
                results.numUnexpectedException++;
                break;
            case DETECTED_FAILURE:
                results.numGraceful++;
                break;
            default:
                throw new RuntimeException("Unknown reason: " + reason);
        }
        System.gc();
    }

    public RuntimePerformanceFactory getFactory() {
        return factory;
    }

    public void setFactory(RuntimePerformanceFactory factory) {
        this.factory = factory;
    }

    public MatrixProcessorInterface getOperation() {
        return operation;
    }

    public void setOperation(MatrixProcessorInterface operation) {
        this.operation = operation;
    }

    public int getTotalTrials() {
        return totalTrials;
    }

    public void setTotalTrials(int totalTrials) {
        this.totalTrials = totalTrials;
    }

    public double getBreakingPoint() {
        return breakingPoint;
    }

    public void setBreakingPoint(double breakingPoint) {
        this.breakingPoint = breakingPoint;
    }
}
