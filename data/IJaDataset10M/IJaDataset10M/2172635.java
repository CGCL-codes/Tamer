package eu.itsyourparliament.api.votes;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;
import org.w3._2001.xmlschema.Adapter1;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the eu.itsyourparliament.api.votes package. 
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

    private static final QName _Voteid_QNAME = new QName("", "voteid");

    private static final QName _Voteinfo_QNAME = new QName("", "voteinfo");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: eu.itsyourparliament.api.votes
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Votes }
     * 
     */
    public Votes createVotes() {
        return new Votes();
    }

    /**
     * Create an instance of {@link Vote }
     * 
     */
    public Vote createVote() {
        return new Vote();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "voteid")
    @XmlJavaTypeAdapter(Adapter1.class)
    public JAXBElement<Integer> createVoteid(Integer value) {
        return new JAXBElement<Integer>(_Voteid_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "voteinfo")
    public JAXBElement<String> createVoteinfo(String value) {
        return new JAXBElement<String>(_Voteinfo_QNAME, String.class, null, value);
    }
}
