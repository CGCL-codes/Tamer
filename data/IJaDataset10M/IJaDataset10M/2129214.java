package upm.fi.oeg.test.rdfResourceTest.server.resource;

import junit.framework.Assert;
import junit.framework.TestCase;
import uk.org.ogsadai.test.TestProperties;
import uk.org.ogsadai.test.TestPropertyNotFoundException;
import upm.fi.oeg.ogsadai.rdfresource.RDFResource;
import upm.fi.oeg.test.rdfResourceTest.server.TestRDFDataResourceState;

/**
 * @author Carlos Buil Aranda
 * @email cbuil@fi.upm.es
 * @insitution Universidad Polit�cnica de Madrid
 *
 */
public class RDFResourceTest extends TestCase {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) Universidad Politecnica de Madrid, 2009-2010.";

    /** Test properties. */
    private TestProperties mTestProperties;

    /** RDF repository property name. */
    private final String RDF_REPO = "rdf.data.url";

    /** OWL ontology property name. */
    private final String OWL_ONTO = "owl.model.url";

    /**
     * the RDF resource
     */
    private RDFResource mResource;

    /**
     * Runs the test cases.
     * 
     * @param args
     *            Not used
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(RDFResourceTest.class);
    }

    /**
     * Constructor.
     * 
     * @param name
     *            Test case name.
     * @throws Exception
     *             If any problems arise in reading the test properties.
     */
    public RDFResourceTest(String name) throws Exception {
        super(name);
        mTestProperties = new TestProperties();
    }

    public void setUp() throws Exception {
        super.setUp();
        TestRDFDataResourceState testState = new TestRDFDataResourceState();
        testState.setRDFDatabaseURL(mTestProperties.getProperty(RDF_REPO));
        mResource = new RDFResource();
        mResource.initialize(testState);
    }

    /**
     * test initialise
     * @throws TestPropertyNotFoundException
     */
    public void testInitialize() throws TestPropertyNotFoundException {
        RDFResource rdfResource = new RDFResource();
        TestRDFDataResourceState testState = new TestRDFDataResourceState();
        testState.setRDFDatabaseURL(mTestProperties.getProperty(RDF_REPO));
        rdfResource.initialize(testState);
    }

    /**
     * tests get the repository
     */
    public void testGetRepository() {
        Assert.assertNotNull(mResource.getRepository(null));
    }

    /**
     * tests destroy the resource
     */
    public void testDestroy() {
        mResource.destroy();
    }
}
