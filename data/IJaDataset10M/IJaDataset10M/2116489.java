package org.apache.ws.jaxme.xs;

/** <p>Interface of an xs:enumeration facet. This could be a simple string,
 * but it is not unusual, that they carry important information in their
 * <code>xs:annotation/xs:appinfo</code> section, thus the inheritance
 * from {@link XSObject}.</p>
 *
 * @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a>
 */
public interface XSEnumeration extends XSObject {

    /** <p>Returns the enumeration facets set of annotations.</p>
   */
    public XSAnnotation[] getAnnotations();

    /** <p>Returns the facets value.</p>
   */
    public String getValue();
}
