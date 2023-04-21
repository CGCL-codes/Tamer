package math;

/**
 * @author kouichi
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface DiscreteTransform {

    /**
	 * this function has side-effects .  that cause x and y to be changed into numbers which was implemented by concrete class
	 * @param x
	 * @param y
	 */
    public void transform(double[] x, double[] y);

    /**
	 * this function has side effect.  parameter reference are changed into transformed
	 * results.
	 * 
	 * @param x
	 * @param y
	 */
    public void inverse(double[] x, double[] y);
}
