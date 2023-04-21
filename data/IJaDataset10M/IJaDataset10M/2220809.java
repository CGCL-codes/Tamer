package org.imsglobal.qti.dom;

import org.w3c.dom.Element;

/**
 * <p>This represents a <code>or</code> element from QTI v2.0.</p> 
 * <blockquote cite="http://www.imsglobal.org/question/qti_v2p0/imsqti_infov2p0.html#element10430">
 * The or operator takes one or more sub-expressions each with a base-type 
 * of boolean and single cardinality. The result is a single boolean which 
 * is true if any of the sub-expressions are true and false if all of them are 
 * false. If one or more sub-expressions are NULL and all the others are false
 *  then the operator also results in NULL. 
 * </blockquote>
 *
 * @author <a href="mailto:j.kahovec@imperial.ac.uk">Jakub Kahovec/a>
 * @version 0.1, 26 October 2005
 * @see <a href="http://www.imsglobal.org/question/qti_v2p0/imsqti_infov2p0.html">QTI 2.0 Information Model</a>
 */
public interface Or extends Expression, Element {
}
