package org.me.calculator.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.me.calculator.client package. 
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

    private static final QName _PlusResponse_QNAME = new QName("http://calculator.me.org/", "plusResponse");

    private static final QName _Add_QNAME = new QName("http://calculator.me.org/", "add");

    private static final QName _Plus_QNAME = new QName("http://calculator.me.org/", "plus");

    private static final QName _AddResponse_QNAME = new QName("http://calculator.me.org/", "addResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.me.calculator.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PlusResponse }
     * 
     */
    public PlusResponse createPlusResponse() {
        return new PlusResponse();
    }

    /**
     * Create an instance of {@link Add }
     * 
     */
    public Add createAdd() {
        return new Add();
    }

    /**
     * Create an instance of {@link AddResponse }
     * 
     */
    public AddResponse createAddResponse() {
        return new AddResponse();
    }

    /**
     * Create an instance of {@link Plus }
     * 
     */
    public Plus createPlus() {
        return new Plus();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PlusResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://calculator.me.org/", name = "plusResponse")
    public JAXBElement<PlusResponse> createPlusResponse(PlusResponse value) {
        return new JAXBElement<PlusResponse>(_PlusResponse_QNAME, PlusResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Add }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://calculator.me.org/", name = "add")
    public JAXBElement<Add> createAdd(Add value) {
        return new JAXBElement<Add>(_Add_QNAME, Add.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Plus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://calculator.me.org/", name = "plus")
    public JAXBElement<Plus> createPlus(Plus value) {
        return new JAXBElement<Plus>(_Plus_QNAME, Plus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://calculator.me.org/", name = "addResponse")
    public JAXBElement<AddResponse> createAddResponse(AddResponse value) {
        return new JAXBElement<AddResponse>(_AddResponse_QNAME, AddResponse.class, null, value);
    }
}
