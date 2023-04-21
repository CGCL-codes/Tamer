package org.imsglobal.qti.dom;

import org.w3c.dom.Element;

/**
 * <p>This represents a <code>gte</code> element from QTI v2.0.</p> 
 * <blockquote cite="http://www.imsglobal.org/question/qti_v2p0/imsqti_infov2p0.html#element10470">
 * The gte operator takes two sub-expressions which must both have single
 *  cardinality and have a numerical base-type. The result is a single boolean 
 *  with a value of true if the first expression is numerically less than or 
 *  equal to the second and false if it is greater than the second. 
 *  If either sub-expression is NULL then the operator results in NULL.
 *
 * @author <a href="mailto:j.kahovec@imperial.ac.uk">Jakub Kahovec/a>
 * @version 0.1, 26 October 2005
 * @see <a href="http://www.imsglobal.org/question/qti_v2p0/imsqti_infov2p0.html">QTI 2.0 Information Model</a>
 */
public interface Gte extends Expression, Element {
}
