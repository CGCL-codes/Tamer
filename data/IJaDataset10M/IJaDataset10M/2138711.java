package pl.swmud.ns.swaedit.names;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the pl.swmud.ns.swaedit.names package. 
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

    private static final QName _Names_QNAME = new QName("http://swmud.pl/ns/swaedit/names", "names");

    private static final QName _NamesName_QNAME = new QName("http://swmud.pl/ns/swaedit/names", "name");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: pl.swmud.ns.swaedit.names
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Names }
     * 
     */
    public Names createNames() {
        return new Names();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Names }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://swmud.pl/ns/swaedit/names", name = "names")
    public JAXBElement<Names> createNames(Names value) {
        return new JAXBElement<Names>(_Names_QNAME, Names.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://swmud.pl/ns/swaedit/names", name = "name", scope = Names.class)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createNamesName(String value) {
        return new JAXBElement<String>(_NamesName_QNAME, String.class, Names.class, value);
    }
}
