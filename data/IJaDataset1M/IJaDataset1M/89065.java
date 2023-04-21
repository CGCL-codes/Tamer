package com.inature.oce.xmlj.core.votes;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.inature.oce.xmlj.core.votes package. 
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

    private static final QName _Votes_QNAME = new QName("http://www.inature.com/oce/xmlj/core/votes.xsd", "votes");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.inature.oce.xmlj.core.votes
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link OCEVotes }
     * 
     */
    public OCEVotes createOCEVotes() {
        return new OCEVotes();
    }

    /**
     * Create an instance of {@link OCELocalizedName }
     * 
     */
    public OCELocalizedName createOCELocalizedName() {
        return new OCELocalizedName();
    }

    /**
     * Create an instance of {@link OCEVote }
     * 
     */
    public OCEVote createOCEVote() {
        return new OCEVote();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OCEVotes }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inature.com/oce/xmlj/core/votes.xsd", name = "votes")
    public JAXBElement<OCEVotes> createVotes(OCEVotes value) {
        return new JAXBElement<OCEVotes>(_Votes_QNAME, OCEVotes.class, null, value);
    }
}
