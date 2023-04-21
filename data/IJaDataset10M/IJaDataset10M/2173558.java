package playground.gregor.grips.jaxb.EDL.evacuationarea;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the playground.gregor.grips.jaxb.EDL.evacuationarea package. 
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

    private static final QName _EvacuationArea_QNAME = new QName("esdl", "evacuationArea");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: playground.gregor.grips.jaxb.EDL.evacuationarea
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link EvacuationAreaType }
     * 
     */
    public EvacuationAreaType createEvacuationAreaType() {
        return new EvacuationAreaType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EvacuationAreaType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "esdl", name = "evacuationArea", substitutionHeadNamespace = "http://www.opengis.net/gml", substitutionHeadName = "_Feature")
    public JAXBElement<EvacuationAreaType> createEvacuationArea(EvacuationAreaType value) {
        return new JAXBElement<EvacuationAreaType>(_EvacuationArea_QNAME, EvacuationAreaType.class, null, value);
    }
}
