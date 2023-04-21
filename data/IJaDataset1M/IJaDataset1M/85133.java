package org.imsglobal.qti.dom;

import org.w3c.dom.Element;

/**
 * <p>This represents a randomFloat element from QTI v2.0.</p>
 * 
 * <blockquote cite="http://www.imsglobal.org/question/qti_v2p0/imsqti_infov2p0.html#element10400">
 * Selects a random float from the specified range [min,max].
 * </blockquote>
 *
 * @author <a href="mailto:d.may@imperial.ac.uk">Daniel J. R. May</a>
 * @version 0.1, 17 October 2005
 * @see <a href="http://www.imsglobal.org/question/qti_v2p0/imsqti_infov2p0.html">QTI 2.0 Information Model</a>
 */
public interface RandomFloat extends Expression, Element {

    /**
	 * @return the minimum value allowed.
	 */
    public double getMin();

    /**
	 * @param min the minimum value allowed.
	 */
    public void setMin(double min);

    /**
	 * @return the maximum value allowed.
	 */
    public double getMax();

    /**
	 * @param max the maximum valued allowed.
	 */
    public void setMax(double max);
}
