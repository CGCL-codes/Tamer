package org.mcisb.bioinformatics.algorithms.blast.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.mcisb.bioinformatics.algorithms.blast.model package. 
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

    private static final QName _EBIApplicationError_QNAME = new QName("http://www.ebi.ac.uk/schema", "EBIApplicationError");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.mcisb.bioinformatics.algorithms.blast.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TSequence }
     * 
     */
    public TSequence createTSequence() {
        return new TSequence();
    }

    /**
     * Create an instance of {@link TMatchSeq }
     * 
     */
    public TMatchSeq createTMatchSeq() {
        return new TMatchSeq();
    }

    /**
     * Create an instance of {@link THeader }
     * 
     */
    public THeader createTHeader() {
        return new THeader();
    }

    /**
     * Create an instance of {@link TTimeInfo }
     * 
     */
    public TTimeInfo createTTimeInfo() {
        return new TTimeInfo();
    }

    /**
     * Create an instance of {@link TProgram }
     * 
     */
    public TProgram createTProgram() {
        return new TProgram();
    }

    /**
     * Create an instance of {@link TAlignments }
     * 
     */
    public TAlignments createTAlignments() {
        return new TAlignments();
    }

    /**
     * Create an instance of {@link TSSSR }
     * 
     */
    public TSSSR createTSSSR() {
        return new TSSSR();
    }

    /**
     * Create an instance of {@link THit }
     * 
     */
    public THit createTHit() {
        return new THit();
    }

    /**
     * Create an instance of {@link TParameters }
     * 
     */
    public TParameters createTParameters() {
        return new TParameters();
    }

    /**
     * Create an instance of {@link TQuerySeq }
     * 
     */
    public TQuerySeq createTQuerySeq() {
        return new TQuerySeq();
    }

    /**
     * Create an instance of {@link THits }
     * 
     */
    public THits createTHits() {
        return new THits();
    }

    /**
     * Create an instance of {@link TIteration }
     * 
     */
    public TIteration createTIteration() {
        return new TIteration();
    }

    /**
     * Create an instance of {@link TCmdLine }
     * 
     */
    public TCmdLine createTCmdLine() {
        return new TCmdLine();
    }

    /**
     * Create an instance of {@link TDatabase }
     * 
     */
    public TDatabase createTDatabase() {
        return new TDatabase();
    }

    /**
     * Create an instance of {@link TIterations }
     * 
     */
    public TIterations createTIterations() {
        return new TIterations();
    }

    /**
     * Create an instance of {@link TSequences }
     * 
     */
    public TSequences createTSequences() {
        return new TSequences();
    }

    /**
     * Create an instance of {@link EBIApplicationResult }
     * 
     */
    public EBIApplicationResult createEBIApplicationResult() {
        return new EBIApplicationResult();
    }

    /**
     * Create an instance of {@link TDatabases }
     * 
     */
    public TDatabases createTDatabases() {
        return new TDatabases();
    }

    /**
     * Create an instance of {@link TAlignment }
     * 
     */
    public TAlignment createTAlignment() {
        return new TAlignment();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ebi.ac.uk/schema", name = "EBIApplicationError")
    public JAXBElement<String> createEBIApplicationError(String value) {
        return new JAXBElement<String>(_EBIApplicationError_QNAME, String.class, null, value);
    }
}
