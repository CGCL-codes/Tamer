package org.imsglobal.qti.dom;

import org.w3c.dom.Element;

/**
* <p>This represents exitResponse element from QTI v2.0.</p>
 * 
 * <blockquote cite="http://www.imsglobal.org/question/qti_v2p0/imsqti_infov2p0.html#element10375">
 <p>The exit response rule terminates response processing immediately 
 (for this invocation).
 </p>
 * </blockquote>
 *
 * @author <a href="mailto:j.kahovec@imperial.ac.uk">Jakub Kahovec</a>
 * @author <a href="mailto:d.may@imperial.ac.uk">Daniel J. R. May</a>
 * @version 0.2, 18 October 2005 
 * @see <a href="http://www.imsglobal.org/question/qti_v2p0/imsqti_infov2p0.html">QTI 2.0 Information Model</a>
 */
public interface ExitResponse extends ResponseRule, Element {
}
