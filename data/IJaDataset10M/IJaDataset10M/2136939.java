package gov.nih.nlm.ncbi.soap.eutils.epost;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.nih.nlm.ncbi.soap.eutils.epost package. 
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

    private static final QName _Id_QNAME = new QName("http://www.ncbi.nlm.nih.gov/soap/eutils/epost", "id");

    private static final QName _WebEnv_QNAME = new QName("http://www.ncbi.nlm.nih.gov/soap/eutils/epost", "WebEnv");

    private static final QName _Tool_QNAME = new QName("http://www.ncbi.nlm.nih.gov/soap/eutils/epost", "tool");

    private static final QName _Db_QNAME = new QName("http://www.ncbi.nlm.nih.gov/soap/eutils/epost", "db");

    private static final QName _Email_QNAME = new QName("http://www.ncbi.nlm.nih.gov/soap/eutils/epost", "email");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.nih.nlm.ncbi.soap.eutils.epost
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link EPostRequest }
     * 
     */
    public EPostRequest createEPostRequest() {
        return new EPostRequest();
    }

    /**
     * Create an instance of {@link EPostResult }
     * 
     */
    public EPostResult createEPostResult() {
        return new EPostResult();
    }

    /**
     * Create an instance of {@link InvalidIdListType }
     * 
     */
    public InvalidIdListType createInvalidIdListType() {
        return new InvalidIdListType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ncbi.nlm.nih.gov/soap/eutils/epost", name = "id")
    public JAXBElement<String> createId(String value) {
        return new JAXBElement<String>(_Id_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ncbi.nlm.nih.gov/soap/eutils/epost", name = "WebEnv")
    public JAXBElement<String> createWebEnv(String value) {
        return new JAXBElement<String>(_WebEnv_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ncbi.nlm.nih.gov/soap/eutils/epost", name = "tool")
    public JAXBElement<String> createTool(String value) {
        return new JAXBElement<String>(_Tool_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ncbi.nlm.nih.gov/soap/eutils/epost", name = "db")
    public JAXBElement<String> createDb(String value) {
        return new JAXBElement<String>(_Db_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ncbi.nlm.nih.gov/soap/eutils/epost", name = "email")
    public JAXBElement<String> createEmail(String value) {
        return new JAXBElement<String>(_Email_QNAME, String.class, null, value);
    }
}
