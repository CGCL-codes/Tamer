package com.opendicom.diago.webservices;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.opendicom.diago.webservices package. 
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

    private static final QName _Exception_QNAME = new QName("http://webservices.diago.opendicom.com/", "Exception");

    private static final QName _AltaEstudioResponse_QNAME = new QName("http://webservices.diago.opendicom.com/", "AltaEstudioResponse");

    private static final QName _AltaEstudio_QNAME = new QName("http://webservices.diago.opendicom.com/", "AltaEstudio");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.opendicom.diago.webservices
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AltaEstudio }
     * 
     */
    public AltaEstudio createAltaEstudio() {
        return new AltaEstudio();
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link AltaEstudioResponse }
     * 
     */
    public AltaEstudioResponse createAltaEstudioResponse() {
        return new AltaEstudioResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservices.diago.opendicom.com/", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<Exception>(_Exception_QNAME, Exception.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AltaEstudioResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservices.diago.opendicom.com/", name = "AltaEstudioResponse")
    public JAXBElement<AltaEstudioResponse> createAltaEstudioResponse(AltaEstudioResponse value) {
        return new JAXBElement<AltaEstudioResponse>(_AltaEstudioResponse_QNAME, AltaEstudioResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AltaEstudio }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservices.diago.opendicom.com/", name = "AltaEstudio")
    public JAXBElement<AltaEstudio> createAltaEstudio(AltaEstudio value) {
        return new JAXBElement<AltaEstudio>(_AltaEstudio_QNAME, AltaEstudio.class, null, value);
    }
}
