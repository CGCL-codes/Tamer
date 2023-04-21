package eu.fbk.hlt.edits.distance.cost.scheme.data;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the eu.fbk.hlt.edits.distance.cost.scheme.data package. 
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

    private static final QName _Scheme_QNAME = new QName("", "scheme");

    private static final QName _Schemes_QNAME = new QName("", "schemes");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: eu.fbk.hlt.edits.distance.cost.scheme.data
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link XMLCostScheme }
     * 
     */
    public XMLCostScheme createXMLCostScheme() {
        return new XMLCostScheme();
    }

    /**
     * Create an instance of {@link XMLCostSchemes }
     * 
     */
    public XMLCostSchemes createXMLCostSchemes() {
        return new XMLCostSchemes();
    }

    /**
     * Create an instance of {@link XMLCostSchemeConstant }
     * 
     */
    public XMLCostSchemeConstant createXMLCostSchemeConstant() {
        return new XMLCostSchemeConstant();
    }

    /**
     * Create an instance of {@link XMLCostSchemeOperation }
     * 
     */
    public XMLCostSchemeOperation createXMLCostSchemeOperation() {
        return new XMLCostSchemeOperation();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLCostScheme }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "scheme")
    public JAXBElement<XMLCostScheme> createScheme(XMLCostScheme value) {
        return new JAXBElement<XMLCostScheme>(_Scheme_QNAME, XMLCostScheme.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLCostSchemes }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "schemes")
    public JAXBElement<XMLCostSchemes> createSchemes(XMLCostSchemes value) {
        return new JAXBElement<XMLCostSchemes>(_Schemes_QNAME, XMLCostSchemes.class, null, value);
    }
}
