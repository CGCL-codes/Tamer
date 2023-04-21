package de.objectcode.openk.soa.connectors.openbravo.wsclient.businesspartner;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.objectcode.openk.soa.connectors.openbravo.wsclient.businesspartner package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private static final QName _OpenkException_QNAME = new QName("http://objectcode.de/openk/webservices", "OpenkException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.objectcode.openk.soa.connectors.openbravo.wsclient.businesspartner
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link EventType }
     * 
     */
    public EventType createEventType() {
        return new EventType();
    }

    /**
     * Create an instance of {@link EventResponseMessage }
     * 
     */
    public EventResponseMessage createEventResponseMessage() {
        return new EventResponseMessage();
    }

    /**
     * Create an instance of {@link VariablesSecureWS.Session.Entry }
     * 
     */
    public VariablesSecureWS.Session.Entry createVariablesSecureWSSessionEntry() {
        return new VariablesSecureWS.Session.Entry();
    }

    /**
     * Create an instance of {@link OpenkException }
     * 
     */
    public OpenkException createOpenkException() {
        return new OpenkException();
    }

    /**
     * Create an instance of {@link VariablesSecureWS.Parameter }
     * 
     */
    public VariablesSecureWS.Parameter createVariablesSecureWSParameter() {
        return new VariablesSecureWS.Parameter();
    }

    /**
     * Create an instance of {@link VariablesSecureWS }
     * 
     */
    public VariablesSecureWS createVariablesSecureWS() {
        return new VariablesSecureWS();
    }

    /**
     * Create an instance of {@link VariablesSecureWS.Session }
     * 
     */
    public VariablesSecureWS.Session createVariablesSecureWSSession() {
        return new VariablesSecureWS.Session();
    }

    /**
     * Create an instance of {@link VariablesSecureWS.Parameter.Entry }
     * 
     */
    public VariablesSecureWS.Parameter.Entry createVariablesSecureWSParameterEntry() {
        return new VariablesSecureWS.Parameter.Entry();
    }

    /**
     * Create an instance of {@link BusinessPartnerData }
     * 
     */
    public BusinessPartnerData createBusinessPartnerData() {
        return new BusinessPartnerData();
    }

    /**
     * Create an instance of {@link BusinessPartnerDataArray }
     * 
     */
    public BusinessPartnerDataArray createBusinessPartnerDataArray() {
        return new BusinessPartnerDataArray();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OpenkException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://objectcode.de/openk/webservices", name = "OpenkException")
    public JAXBElement<OpenkException> createOpenkException(OpenkException value) {
        return new JAXBElement<OpenkException>(_OpenkException_QNAME, OpenkException.class, null, value);
    }
}
