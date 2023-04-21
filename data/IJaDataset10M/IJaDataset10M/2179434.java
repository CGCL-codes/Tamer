package org.jaffa.dwr.services.configdomain;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.jaffa.dwr.services.configdomain package. 
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

    private static final QName _Allow_QNAME = new QName("", "allow");

    private static final QName _Param_QNAME = new QName("", "param");

    private static final QName _Dwr_QNAME = new QName("", "dwr");

    private static final QName _Init_QNAME = new QName("", "init");

    private static final QName _Convert_QNAME = new QName("", "convert");

    private static final QName _Exclude_QNAME = new QName("", "exclude");

    private static final QName _Signatures_QNAME = new QName("", "signatures");

    private static final QName _Create_QNAME = new QName("", "create");

    private static final QName _Creator_QNAME = new QName("", "creator");

    private static final QName _Include_QNAME = new QName("", "include");

    private static final QName _Converter_QNAME = new QName("", "converter");

    private static final QName _Auth_QNAME = new QName("", "auth");

    private static final QName _Filter_QNAME = new QName("", "filter");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.jaffa.dwr.services.configdomain
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Convert }
     * 
     */
    public Convert createConvert() {
        return new Convert();
    }

    /**
     * Create an instance of {@link Create }
     * 
     */
    public Create createCreate() {
        return new Create();
    }

    /**
     * Create an instance of {@link Include }
     * 
     */
    public Include createInclude() {
        return new Include();
    }

    /**
     * Create an instance of {@link Param }
     * 
     */
    public Param createParam() {
        return new Param();
    }

    /**
     * Create an instance of {@link Filter }
     * 
     */
    public Filter createFilter() {
        return new Filter();
    }

    /**
     * Create an instance of {@link Converter }
     * 
     */
    public Converter createConverter() {
        return new Converter();
    }

    /**
     * Create an instance of {@link Creator }
     * 
     */
    public Creator createCreator() {
        return new Creator();
    }

    /**
     * Create an instance of {@link Exclude }
     * 
     */
    public Exclude createExclude() {
        return new Exclude();
    }

    /**
     * Create an instance of {@link Dwr }
     * 
     */
    public Dwr createDwr() {
        return new Dwr();
    }

    /**
     * Create an instance of {@link Init }
     * 
     */
    public Init createInit() {
        return new Init();
    }

    /**
     * Create an instance of {@link Auth }
     * 
     */
    public Auth createAuth() {
        return new Auth();
    }

    /**
     * Create an instance of {@link Allow }
     * 
     */
    public Allow createAllow() {
        return new Allow();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Allow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "allow")
    public JAXBElement<Allow> createAllow(Allow value) {
        return new JAXBElement<Allow>(_Allow_QNAME, Allow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Param }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "param")
    public JAXBElement<Param> createParam(Param value) {
        return new JAXBElement<Param>(_Param_QNAME, Param.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Dwr }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "dwr")
    public JAXBElement<Dwr> createDwr(Dwr value) {
        return new JAXBElement<Dwr>(_Dwr_QNAME, Dwr.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Init }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "init")
    public JAXBElement<Init> createInit(Init value) {
        return new JAXBElement<Init>(_Init_QNAME, Init.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Convert }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "convert")
    public JAXBElement<Convert> createConvert(Convert value) {
        return new JAXBElement<Convert>(_Convert_QNAME, Convert.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exclude }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "exclude")
    public JAXBElement<Exclude> createExclude(Exclude value) {
        return new JAXBElement<Exclude>(_Exclude_QNAME, Exclude.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "signatures")
    public JAXBElement<String> createSignatures(String value) {
        return new JAXBElement<String>(_Signatures_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Create }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "create")
    public JAXBElement<Create> createCreate(Create value) {
        return new JAXBElement<Create>(_Create_QNAME, Create.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Creator }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "creator")
    public JAXBElement<Creator> createCreator(Creator value) {
        return new JAXBElement<Creator>(_Creator_QNAME, Creator.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Include }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "include")
    public JAXBElement<Include> createInclude(Include value) {
        return new JAXBElement<Include>(_Include_QNAME, Include.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Converter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "converter")
    public JAXBElement<Converter> createConverter(Converter value) {
        return new JAXBElement<Converter>(_Converter_QNAME, Converter.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Auth }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "auth")
    public JAXBElement<Auth> createAuth(Auth value) {
        return new JAXBElement<Auth>(_Auth_QNAME, Auth.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Filter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "filter")
    public JAXBElement<Filter> createFilter(Filter value) {
        return new JAXBElement<Filter>(_Filter_QNAME, Filter.class, null, value);
    }
}
