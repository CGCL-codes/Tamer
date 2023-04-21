package com.alexmcchesney.poster.templates.style.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.alexmcchesney.poster.templates.style.jaxb package. 
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

    private static final QName _TitleStyle_QNAME = new QName("", "titleStyle");

    private static final QName _Inline_QNAME = new QName("", "inline");

    private static final QName _ContentStyle_QNAME = new QName("", "contentStyle");

    private static final QName _Link_QNAME = new QName("", "link");

    private static final QName _Body_QNAME = new QName("", "body");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.alexmcchesney.poster.templates.style.jaxb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link StyleSheet }
     * 
     */
    public StyleSheet createStyleSheet() {
        return new StyleSheet();
    }

    /**
     * Create an instance of {@link StyleTemplate }
     * 
     */
    public StyleTemplate createStyleTemplate() {
        return new StyleTemplate();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "titleStyle")
    public JAXBElement<String> createTitleStyle(String value) {
        return new JAXBElement<String>(_TitleStyle_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "inline")
    public JAXBElement<String> createInline(String value) {
        return new JAXBElement<String>(_Inline_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "contentStyle")
    public JAXBElement<String> createContentStyle(String value) {
        return new JAXBElement<String>(_ContentStyle_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "link")
    public JAXBElement<String> createLink(String value) {
        return new JAXBElement<String>(_Link_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "body")
    public JAXBElement<String> createBody(String value) {
        return new JAXBElement<String>(_Body_QNAME, String.class, null, value);
    }
}
