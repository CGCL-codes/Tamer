package mipt.math.sys.num.ode;

import java.io.OutputStreamWriter;
import java.text.NumberFormat;
import mipt.math.Number;
import mipt.math.array.DefaultVector;
import mipt.math.array.Matrix;
import mipt.math.array.RowMatrix;
import mipt.math.array.Vector;
import mipt.math.sys.num.ode.solutions.WriterODESolution;
import mipt.math.sys.ode.ODEProblem;

/**
 * Superclass for ODE solving tests: defines all parts of the solution process (solver, algorithms, solution)
 *  except the problem itself. main() method is to just call the constructor and solve()/solveAndMeasure().
 * @author Evdokimov
 */
public abstract class Test {

    /**
	 * Returns formatted time of computation in milliseconds 
	 * Note: it includes the time of writing solution to console!
	 * @param format - can be null; if not null and not DecimalFormat returns integer
	 */
    public String solveAndMeasure(NumberFormat format) {
        solve();
        long nanos = System.nanoTime();
        solve();
        double ms = (System.nanoTime() - nanos) * 0.000001;
        String time = format == null ? Double.toString(ms) : format.format(ms);
        return time;
    }

    public ODESolution solve() {
        Number.setThrowArithmeticExceptions(true);
        ODEProblem problem = getProblem();
        DefaultInitialSolver solver = new DefaultInitialSolver();
        solver.setInitialAlgorithm(getInitialAlgorithm());
        solver.setStepAlgorithm(getStepAlgorithm(problem));
        solver.setEmptySolution(getEmptySolution(problem));
        return solver.solve(problem);
    }

    protected abstract ODEProblem getProblem();

    /**
	 * To be called by subclasses. Can be overridden if non-double Numbers must be used.
	 */
    protected Number createNumber(double value) {
        return Number.createScalar(value);
    }

    /**
	 * To be called by subclasses
	 */
    protected Vector vector(double[] a) {
        Number[] elements = new Number[a.length];
        for (int i = 0; i < a.length; i++) elements[i] = createNumber(a[i]);
        return new DefaultVector(elements);
    }

    /**
	 * To be called by subclasses
	 * a[row][column]
	 */
    protected Matrix matrix(double[][] a) {
        Vector[] rows = new Vector[a.length];
        for (int i = 0; i < a.length; i++) rows[i] = vector(a[i]);
        return new RowMatrix(rows);
    }

    protected InitialAlgorithm getInitialAlgorithm() {
        return new DefaultInitialAlgorithm();
    }

    protected StepAlgorithm getStepAlgorithm(ODEProblem problem) {
        return new FixedStepAlgorithm(problem, getStepCount());
    }

    protected int getStepCount() {
        return 25;
    }

    protected MutableODESolution getEmptySolution(ODEProblem problem) {
        return new WriterODESolution(new OutputStreamWriter(System.out));
    }
}
