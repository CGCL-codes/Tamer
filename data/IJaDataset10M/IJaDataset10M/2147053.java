package data.riksdagen.se.votering;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;
import org.w3._2001.xmlschema.Adapter1;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the data.riksdagen.se.votering package. 
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

    private static final QName _Rost_QNAME = new QName("", "rost");

    private static final QName _IntressentId_QNAME = new QName("", "intressent_id");

    private static final QName _Valkrets_QNAME = new QName("", "valkrets");

    private static final QName _Rm_QNAME = new QName("", "rm");

    private static final QName _Iort_QNAME = new QName("", "iort");

    private static final QName _Namn_QNAME = new QName("", "namn");

    private static final QName _Punkt_QNAME = new QName("", "punkt");

    private static final QName _Fornamn_QNAME = new QName("", "fornamn");

    private static final QName _Beteckning_QNAME = new QName("", "beteckning");

    private static final QName _Kon_QNAME = new QName("", "kon");

    private static final QName _Efternamn_QNAME = new QName("", "efternamn");

    private static final QName _Fodd_QNAME = new QName("", "fodd");

    private static final QName _VoteringId_QNAME = new QName("", "votering_id");

    private static final QName _Avser_QNAME = new QName("", "avser");

    private static final QName _Valkretsnummer_QNAME = new QName("", "valkretsnummer");

    private static final QName _Parti_QNAME = new QName("", "parti");

    private static final QName _Banknummer_QNAME = new QName("", "banknummer");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: data.riksdagen.se.votering
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Votering }
     * 
     */
    public Votering createVotering() {
        return new Votering();
    }

    /**
     * Create an instance of {@link Dokvotering }
     * 
     */
    public Dokvotering createDokvotering() {
        return new Dokvotering();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "rost")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createRost(String value) {
        return new JAXBElement<String>(_Rost_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "intressent_id")
    @XmlJavaTypeAdapter(Adapter1.class)
    public JAXBElement<Integer> createIntressentId(Integer value) {
        return new JAXBElement<Integer>(_IntressentId_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "valkrets")
    public JAXBElement<String> createValkrets(String value) {
        return new JAXBElement<String>(_Valkrets_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "rm")
    public JAXBElement<String> createRm(String value) {
        return new JAXBElement<String>(_Rm_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "iort")
    public JAXBElement<String> createIort(String value) {
        return new JAXBElement<String>(_Iort_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "namn")
    public JAXBElement<String> createNamn(String value) {
        return new JAXBElement<String>(_Namn_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "punkt")
    @XmlJavaTypeAdapter(Adapter1.class)
    public JAXBElement<Integer> createPunkt(Integer value) {
        return new JAXBElement<Integer>(_Punkt_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "fornamn")
    public JAXBElement<String> createFornamn(String value) {
        return new JAXBElement<String>(_Fornamn_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "beteckning")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createBeteckning(String value) {
        return new JAXBElement<String>(_Beteckning_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "kon")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createKon(String value) {
        return new JAXBElement<String>(_Kon_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "efternamn")
    public JAXBElement<String> createEfternamn(String value) {
        return new JAXBElement<String>(_Efternamn_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "fodd")
    @XmlJavaTypeAdapter(Adapter1.class)
    public JAXBElement<Integer> createFodd(Integer value) {
        return new JAXBElement<Integer>(_Fodd_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "votering_id")
    public JAXBElement<String> createVoteringId(String value) {
        return new JAXBElement<String>(_VoteringId_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "avser")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAvser(String value) {
        return new JAXBElement<String>(_Avser_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "valkretsnummer")
    @XmlJavaTypeAdapter(Adapter1.class)
    public JAXBElement<Integer> createValkretsnummer(Integer value) {
        return new JAXBElement<Integer>(_Valkretsnummer_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "parti")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createParti(String value) {
        return new JAXBElement<String>(_Parti_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "banknummer")
    @XmlJavaTypeAdapter(Adapter1.class)
    public JAXBElement<Integer> createBanknummer(Integer value) {
        return new JAXBElement<Integer>(_Banknummer_QNAME, Integer.class, null, value);
    }
}
