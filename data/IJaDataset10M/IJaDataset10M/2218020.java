package org.apache.ws.jaxme;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.bind.JAXBContext;
import org.apache.ws.jaxme.impl.JMSAXDriver;
import org.apache.ws.jaxme.impl.JMParser;
import org.xml.sax.SAXException;

/** <p>A JMManager controls the object factory (aka JAXBContext)
 * for a given document type. The document type is both identified
 * by its QName and its interface, which is extending JMElement.</p>
 */
public interface JMManager {

    /** <p>Returns a property value, which is used to configure
   * the manager. The property value is set in the configuration
   * file.</p>
   *
   * @param pName The property name
   * @return pValue The property value; null, if the property is not
   *   set.
   */
    public String getProperty(String pName);

    /** <p>Returns the {@link org.apache.ws.jaxme.impl.JAXBContextImpl},
   * that created this manager.</p>
   */
    public JAXBContext getFactory();

    /** <p>Returns the QName of the document type that this
   * Manager controls.</p>
   */
    public QName getQName();

    /** Returns the interface matching the document type.
   * Usually, this is a  a subinterface of
   * {@link JMElement}.
   * However, for support of POJO's, we should not depend
   * on this.
   */
    public Class getElementInterface();

    /** Returns an instance of the element class. Same as
   * {@link #getElementS()}, except that it throws a
   * different exception.
   */
    public Object getElementJ() throws JAXBException;

    /** Returns an instance of the element class. Same as
   * {@link #getElementJ()}, except that it throws a
   * different exception.
   */
    public Object getElementS() throws SAXException;

    /** Returns the document types handler class.
   */
    public Class getHandlerClass();

    /** Returns an instance of the document types handler class.
   */
    public JMParser getHandler() throws SAXException;

    /** Returns the document types driverr class.
   */
    public Class getDriverClass();

    /** Returns an instance of the document types driver class.
   */
    public JMSAXDriver getDriver() throws SAXException;

    /** <p>Returns the persistency class. The persistency class
   * must be able to store documents in a database, update,
   * delete or retrieve them.</p>
   */
    public Class getPmClass();
}
