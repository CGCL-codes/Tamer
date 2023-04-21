package jmbench.tools.stability.tests;

import jmbench.interfaces.BenchmarkMatrix;
import jmbench.interfaces.DetectedException;
import jmbench.interfaces.MatrixProcessorInterface;
import jmbench.interfaces.RuntimePerformanceFactory;
import jmbench.tools.OutputError;
import jmbench.tools.stability.StabilityBenchmark;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.MatrixFeatures;

/**
 * @author Peter Abeles
 */
public class SolverAccuracy extends SolverCommon {

    public SolverAccuracy(long randomSeed, RuntimePerformanceFactory factory, MatrixProcessorInterface operation, int totalTrials, double breakingPoint, int minLength, int maxLength, boolean linearSolver) {
        super(randomSeed, factory, operation, totalTrials, breakingPoint, minLength, maxLength, linearSolver);
    }

    public SolverAccuracy() {
    }

    @Override
    public void performTest() {
        for (int i = 0; i < totalTrials; i++) {
            int m, n;
            if (isLinearSolver) {
                m = n = rand.nextInt(maxLength - minLength) + minLength;
            } else {
                m = rand.nextInt(maxLength - minLength) + minLength;
                n = minLength;
                if (m > minLength) n += rand.nextInt(m - minLength);
            }
            createMatrix(m, n, 1);
            evaluateSolver();
            saveResults();
        }
    }

    private void evaluateSolver() {
        reason = OutputError.NO_ERROR;
        foundResult = Double.NaN;
        BenchmarkMatrix[] inputsB = new BenchmarkMatrix[2];
        BenchmarkMatrix[] outputB = new BenchmarkMatrix[1];
        inputsB[0] = factory.convertToLib(A);
        inputsB[1] = factory.convertToLib(b);
        try {
            operation.process(inputsB, outputB, 1);
        } catch (DetectedException e) {
            reason = OutputError.DETECTED_FAILURE;
            return;
        } catch (Exception e) {
            addUnexpectedException(e);
            reason = OutputError.UNEXPECTED_EXCEPTION;
            return;
        }
        DenseMatrix64F results[] = new DenseMatrix64F[outputB.length];
        for (int i = 0; i < results.length; i++) results[i] = factory.convertToEjml(outputB[i]);
        DenseMatrix64F x = results[0];
        if (MatrixFeatures.hasUncountable(x)) {
            reason = OutputError.UNCOUNTABLE;
            return;
        }
        foundResult = StabilityBenchmark.residualErrorMetric(A, x, b);
        if (Double.isNaN(foundResult) || Double.isInfinite(foundResult)) {
            reason = OutputError.LARGE_ERROR;
            return;
        }
    }

    @Override
    public String getTestName() {
        if (isLinearSolver) return "Linear accuracy"; else return "Least squares accuracy";
    }

    @Override
    public String getFileName() {
        if (isLinearSolver) return "LinearAccuracy"; else return "LeastSquaresAccuracy";
    }
}
