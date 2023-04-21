package org.imsglobal.qti.dom;

import org.w3c.dom.Element;

/**
 * <p>This represents a randomInteger element from QTI v2.0.</p>
 * 
 * <blockquote cite="http://www.imsglobal.org/question/qti_v2p0/imsqti_infov2p0.html#element10396">
 * Selects a random integer from the specified range [min,max] 
 * satisfying min + step * n for some integer n. For example, 
 * with min=2, max=11 and step=3 the values {2,5,8,11} are possible.
 * </blockquote>
 *
 * @author <a href="mailto:d.may@imperial.ac.uk">Daniel J. R. May</a>
 * @version 0.1, 27 September 2005
 * @see <a href="http://www.imsglobal.org/question/qti_v2p0/imsqti_infov2p0.html">QTI 2.0 Information Model</a>
 */
public interface RandomInteger extends Expression, Element {

    /**
	 * @return the minimum value allowed.
	 */
    public int getMin();

    /**
	 * @param min the minimum value allowed.
	 */
    public void setMin(int min);

    /**
	 * @return the maximum value allowed.
	 */
    public int getMax();

    /**
	 * @param max the maximum valued allowed.
	 */
    public void setMax(int max);

    /**
	 * @return the step.
	 */
    public int getStep();

    /**
	 * @param step the step.
	 */
    public void setStep(int step);
}
