package org.apache.ws.jaxme.xs;

import org.apache.ws.jaxme.xs.xml.XsQName;

/** <p>Interface of a schema attribute.</p>
 *
 * @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a>
 */
public interface XSAttribute extends XSOpenAttrs, XSAttributable {

    /** <p>Returns whether the attribute is global.</p>
   */
    public boolean isGlobal();

    /** <p>Returns the attributes name. Note, that an attribute
   * always has a name, unlike types.</p>
   */
    public XsQName getName();

    /** <p>Returns the attributes type.</p>
   */
    public XSType getType();

    /** <p>Returns whether the attribute is optional.</p>
   */
    public boolean isOptional();

    /** <p>Returns the attributes set of annotations.</p>
   */
    public XSAnnotation[] getAnnotations();

    /** <p>Returns the attributes "default" value or null, if no such
   * attribute is set.</p>
   */
    public String getDefault();

    /** <p>Returns the attributes "fixed" value or null, if no such
   * attribute is set.</p>
   */
    public String getFixed();
}
