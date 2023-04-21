package org.apache.ws.jaxme.xs;

import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/** <p>A common base interface for all other schema objects.</p>
 *
 * @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a>
 */
public interface XSObject {

    /** <p>Returns the objects schema.</p>
   */
    public XSSchema getXSSchema();

    /** <p>Returns whether the object is a top-level object. This is
   * the case for the XsESchema itself and for all its childs only.</p>
   */
    public boolean isTopLevelObject();

    /** <p>Returns either of the following:
   * <ul>
   *   <li>If the object is the schema itself, returns null. The
   *     schema doesn't have a parent object.</p>
   *   <li>If the object is a top-level object, returns the
   *     schema.</p>
   *   <li>Otherwise returns the object in which the given object
   *     is embedded.</li>
   * </ul>
   */
    public XSObject getParentObject();

    /** <p>Returns the objects location.</p>
   */
    public Locator getLocator();

    /** <p>Validates the objects internal state.</p>
   */
    public void validate() throws SAXException;
}
