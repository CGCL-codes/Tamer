package upm.fi.oeg.test.rdfResourceTest.server;

import java.io.IOException;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.extension.ResourceActivity;
import uk.org.ogsadai.activity.io.BlockReader;
import uk.org.ogsadai.activity.io.LiteralBlockReader;
import uk.org.ogsadai.activity.pipeline.SimpleActivityDescriptor;
import uk.org.ogsadai.activity.pipeline.SimpleLiteral;
import uk.org.ogsadai.authorization.NullSecurityContext;
import upm.fi.oeg.ogsadai.rdfactivity.server.query.RDFDBQueryActivity;
import upm.fi.oeg.ogsadai.rdfresource.RDFResource;
import upm.fi.oeg.ogsadai.rdfresource.exception.RDFModelUseException;
import upm.fi.oeg.test.TestUtils.MockOutputPipe;
import upm.fi.oeg.test.TestUtils.TestProperties;

/**
 * @author Carlos Buil Aranda
 * @email cbuil@fi.upm.es
 * @insitution Universidad Polit�cnica de Madrid
 *
 */
public class RDFDBQueryActivityTest extends TestCase {

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
     * RDF query activity
     */
    private RDFDBQueryActivity mActivity;

    /**
     * Runs the test cases.
     * 
     * @param args
     *            Not used
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(RDFDBQueryActivityTest.class);
    }

    /**
     * Constructor.
     * 
     * @param name
     *            Test case name.
     * @throws Exception
     *             If any problems arise in reading the test properties.
     */
    public RDFDBQueryActivityTest(String name) throws Exception {
        super(name);
        mTestProperties = new TestProperties();
    }

    public void setUp() throws Exception {
        super.setUp();
        TestRDFDataResourceState testState = new TestRDFDataResourceState();
        testState.setRDFDatabaseURL(mTestProperties.getProperty(RDF_REPO));
        mResource = new RDFResource();
        mResource.initialize(testState);
        mActivity = new RDFDBQueryActivity();
        mActivity.setActivityDescriptor(new SimpleActivityDescriptor("SPARQLQueryeActivityTest"));
        ResourceActivity ra = (ResourceActivity) mActivity;
        ra.setTargetResourceAccessor(mResource.createResourceAccessor(new NullSecurityContext()));
    }

    /**
     * tests a query to the RDF repository, connected to a DB
     * 
     * @throws RDFModelUseException
     * @throws IOException
     * @throws ActivityUserException
     * @throws ActivityProcessingException
     * @throws ActivityTerminatedException
     */
    @Test
    public void testQuerySDBRepository() throws RDFModelUseException, IOException, ActivityUserException, ActivityProcessingException, ActivityTerminatedException {
        String sparqlQuery = "PREFIX dmi: <http://www.admire-project.eu/ontologies/CRISP-DMIOntology.owl#> " + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + "SELECT ?x ?y " + "WHERE {   	?x dmi:hasStructuralType ?y .	" + "}";
        BlockReader input = new LiteralBlockReader(new SimpleLiteral(sparqlQuery));
        MockOutputPipe output = new MockOutputPipe(new Object[] {});
        mActivity.addInput("expression", input);
        mActivity.addOutput("rdfdata", output);
        mActivity.process();
        Assert.assertNotNull(output);
    }

    /**
     * tests destroy the resource
     */
    @Test
    public void testDestroy() {
        mResource.destroy();
    }
}
