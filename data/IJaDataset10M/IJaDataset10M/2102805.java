package edu.hawaii.myisern.collaborations.jaxb;

import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the edu.hawaii.myisern.collaborations.jaxb package. 
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

    private static final QName _CollaboratingOrganization_QNAME = new QName("", "Collaborating-Organization");

    private static final QName _Year_QNAME = new QName("", "Year");

    private static final QName _Description_QNAME = new QName("", "Description");

    private static final QName _OutcomeType_QNAME = new QName("", "Outcome-Type");

    private static final QName _Name_QNAME = new QName("", "Name");

    private static final QName _CollaborationType_QNAME = new QName("", "Collaboration-Type");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: edu.hawaii.myisern.collaborations.jaxb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Collaborating-Organization")
    public JAXBElement<String> createCollaboratingOrganization(String value) {
        return new JAXBElement<String>(_CollaboratingOrganization_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link CollaboratingOrganizations }
     * 
     */
    public CollaboratingOrganizations createCollaboratingOrganizations() {
        return new CollaboratingOrganizations();
    }

    /**
     * Create an instance of {@link Collaboration }
     * 
     */
    public Collaboration createCollaboration() {
        return new Collaboration();
    }

    /**
     * Create an instance of {@link Collaborations }
     * 
     */
    public Collaborations createCollaborations() {
        return new Collaborations();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Collaboration-Type")
    public JAXBElement<String> createCollaborationType(String value) {
        return new JAXBElement<String>(_CollaborationType_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link CollaborationTypes }
     * 
     */
    public CollaborationTypes createCollaborationTypes() {
        return new CollaborationTypes();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Description")
    public JAXBElement<String> createDescription(String value) {
        return new JAXBElement<String>(_Description_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Name")
    public JAXBElement<String> createName(String value) {
        return new JAXBElement<String>(_Name_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Outcome-Type")
    public JAXBElement<String> createOutcomeType(String value) {
        return new JAXBElement<String>(_OutcomeType_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link OutcomeTypes }
     * 
     */
    public OutcomeTypes createOutcomeTypes() {
        return new OutcomeTypes();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Year")
    public JAXBElement<BigInteger> createYear(BigInteger value) {
        return new JAXBElement<BigInteger>(_Year_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link Years }
     * 
     */
    public Years createYears() {
        return new Years();
    }
}
