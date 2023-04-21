package data.worldbank.org.countries;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the data.worldbank.org.countries package. 
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

    private static final QName _CapitalCity_QNAME = new QName("http://www.worldbank.org", "capitalCity");

    private static final QName _Latitude_QNAME = new QName("http://www.worldbank.org", "latitude");

    private static final QName _Longitude_QNAME = new QName("http://www.worldbank.org", "longitude");

    private static final QName _Iso2Code_QNAME = new QName("http://www.worldbank.org", "iso2Code");

    private static final QName _Name_QNAME = new QName("http://www.worldbank.org", "name");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: data.worldbank.org.countries
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Country }
     * 
     */
    public Country createCountry() {
        return new Country();
    }

    /**
     * Create an instance of {@link IncomeLevel }
     * 
     */
    public IncomeLevel createIncomeLevel() {
        return new IncomeLevel();
    }

    /**
     * Create an instance of {@link Countries }
     * 
     */
    public Countries createCountries() {
        return new Countries();
    }

    /**
     * Create an instance of {@link LendingType }
     * 
     */
    public LendingType createLendingType() {
        return new LendingType();
    }

    /**
     * Create an instance of {@link Region }
     * 
     */
    public Region createRegion() {
        return new Region();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.worldbank.org", name = "capitalCity")
    public JAXBElement<String> createCapitalCity(String value) {
        return new JAXBElement<String>(_CapitalCity_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.worldbank.org", name = "latitude")
    public JAXBElement<String> createLatitude(String value) {
        return new JAXBElement<String>(_Latitude_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.worldbank.org", name = "longitude")
    public JAXBElement<String> createLongitude(String value) {
        return new JAXBElement<String>(_Longitude_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.worldbank.org", name = "iso2Code")
    public JAXBElement<String> createIso2Code(String value) {
        return new JAXBElement<String>(_Iso2Code_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.worldbank.org", name = "name")
    public JAXBElement<String> createName(String value) {
        return new JAXBElement<String>(_Name_QNAME, String.class, null, value);
    }
}
