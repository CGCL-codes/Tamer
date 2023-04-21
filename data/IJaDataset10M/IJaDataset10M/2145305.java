package uk.ac.leeds.mass.fmf.fit_statistics;

import java.util.List;

/**
 * Interface implemented by the matrix GOF statistics
 *
 * @author Kirk Harland
 */
public interface IGOF {

    /**
	 * Perform the test (optional).
	 * <p>
	 * Will output the accuracy of the test on the individual cells. Some tests only calculate a
	 * global value, in which case this isn't applicable.
	 * @param calib The calibration (expected) data.
	 * @param test The test (predicted/simulated) data.
	 * @param localValues An optional 2D list which will be populated with the results of the test for each
	 * individual cell (if applicable to the test itself and not-null). 
	 * @return The global value of the test (if applicable)
	 */
    public double test(double[][] calib, double[][] test, double[][] outLocalValues);

    /**
	 * Run the test.
	 * <p>
	 * Assumes that all matricies have the same dimensions, will probably throw a NullPointerException if not.
	 *
	 * @param calib The calibration (expected) data.
	 * @param test The test (predicted/simulated) data.
	 * @return The R-squared error of the test and calibration data.
	 */
    public double test(double[][] calib, double[][] test);

    public boolean isPerfect(double testStat);
}
