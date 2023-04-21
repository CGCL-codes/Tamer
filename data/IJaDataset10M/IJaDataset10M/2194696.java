package com.alexmcchesney.poster.inserts.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.alexmcchesney.poster.inserts.jaxb package. 
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

    private static final QName _Start_QNAME = new QName("", "start");

    private static final QName _End_QNAME = new QName("", "end");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.alexmcchesney.poster.inserts.jaxb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Insert }
     * 
     */
    public Insert createInsert() {
        return new Insert();
    }

    /**
     * Create an instance of {@link Inserts }
     * 
     */
    public Inserts createInserts() {
        return new Inserts();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "start")
    public JAXBElement<String> createStart(String value) {
        return new JAXBElement<String>(_Start_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "end")
    public JAXBElement<String> createEnd(String value) {
        return new JAXBElement<String>(_End_QNAME, String.class, null, value);
    }
}
