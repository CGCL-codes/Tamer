package mipt.gui.graph.function;

/**
 * Piecewise parabolic function: value is on the curve y=a*x^2+b*x+c.
 * Uses three (not two!) points: data[lastN-1], xy0=data[lastN], xy1=data[lastN+1].
 * If lastN = 0, does linear interpolation.
 */
public class ParabInterpolator extends AbstractInterpolator {

    /**
	 * 
	 */
    public ParabInterpolator() {
    }

    /**
	 * @param data
	 */
    public ParabInterpolator(double[][] data) {
        super(data);
    }

    /**
	 * @see mipt.gui.graph.function.AbstractInterpolator#calculateReturnValue(double)
	 */
    protected double calculateReturnValue(double x) {
        if (lastN == 0) return y0 + (y1 - y0) * (x - x0) / (x1 - x0);
        double x_1 = data[lastN - 1][0], y_1 = data[lastN - 1][1];
        double d = x - 0.5 * (x1 + x_1), h = x1 - x_1;
        return y0 + (y1 - y_1) * d / h + (y1 - 2 * y0 + y_1) * d * d * 2. / (h * h);
    }
}
