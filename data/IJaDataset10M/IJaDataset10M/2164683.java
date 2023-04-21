package mipt.math.array.spectrum;

import mipt.math.Number;
import mipt.math.array.Matrix;
import mipt.math.sys.num.Method;

/**
 * There are some methods that could determine whole matrix spectrum. This interface helps
 * to external objects to pick out only bounds values.
 *
 * @author Korchak Anton
 */
public abstract class SpectrumBoundsMethod implements Method {

    /**
	 * The target matrix. Matrix to calculate (min and max module) eigen values of.
	 */
    protected Matrix matrix = null;

    /**
	 * Setting matrix to calculate max module eigen value of.
	 * @param matrix - matrix to calculate max module eigen value of.
	 */
    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    /**
	 * Calculation of module of eigen value with min module.
	 * @return module of eigen value of matrix with min module.
	 */
    public Number getEigenValueMinModule() {
        Number value = getMinEigenValue();
        return Number.createNumber(value, value.modulusValue());
    }

    /**
	 * Calculation of module of eigen value with max module.
	 * @return module of eigen value of matrix with max module.
	 */
    public Number getEigenValueMaxModule() {
        Number value = getMaxEigenValue();
        return Number.createNumber(value, value.modulusValue());
    }

    /**
	 * Calculation of eigen value of matrix with max module.
	 * !!! In real it returns signed value.
	 * @return eigen value of matrix with max module.
	 * @see #getEigenValueMaxMoudle()
	 */
    public abstract Number getMaxEigenValue();

    /**
	 * Calculation of eigen value of matrix with min module.
	 * Very slow method. It spends a lof of time to inverse the
	 * matrix.
	 * !!! In real it returns signed value.
	 * @return eigen value of matrix with min module.
	 * @see #getEigenValueMinMoudle()
	 */
    public abstract Number getMinEigenValue();
}
