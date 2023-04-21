package upm.fi.oeg.ogsadai.rdfactivity;

import java.io.IOException;
import java.io.InputStream;
import uk.org.ogsadai.activity.ActivityContractName;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.extension.ResourceActivity;
import uk.org.ogsadai.activity.extension.ServiceAddresses;
import uk.org.ogsadai.activity.io.ActivityIOException;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.resource.ResourceAccessor;
import upm.fi.oeg.ogsadai.rdfactivity.exception.ActivitySPARQLException;
import upm.fi.oeg.ogsadai.rdfresource.RDFConnectionProvider;
import upm.fi.oeg.ogsadai.rdfresource.exception.RDFModelUseException;
import com.hp.hpl.jena.rdf.model.Model;

/**
 * Server side activity that connects to the local RDF resource (RDF file which
 * requires to load a Jena model. receives the SPARQL query from the client,
 * sends it to the RDF resource, gets the results and process them creating new
 * OGSA-DAI tuples.
 * 
 * @author Carlos Buil Aranda
 * @email cbuil@fi.upm.es
 * @insitution Universidad Polit�cnica de Madrid
 * 
 */
public class RDFQueryActivityLocal extends MatchedIterativeActivity implements ResourceActivity {

    /**
     * DAI logger
     */
    private static final DAILogger LOG = DAILogger.getLogger(SPARQLQuery.class);

    /** Activity input name - SPARQL expression */
    public static final String INPUT_SPARQL_EXPRESSION = "expression";

    /** Activity output name - produces lists of tuples */
    public static final String OUTPUT_SPARQL_RESULTS = "rdfdata";

    /** The RDFDataResource connection provider */
    private RDFConnectionProvider mResource;

    /** Service addresses. */
    private ServiceAddresses mServiceAddresses;

    /** The RDF repository */
    private String repository;

    /**
     * The Jena model
     */
    private Model model;

    /**
     * Constructor.
     */
    public RDFQueryActivityLocal() {
        super();
        LOG.debug("Into RDFQueryActivity, contructor");
        mContracts.add(new ActivityContractName("admire.upm.registry.activity.contract.RDFQueryLocal"));
    }

    @Override
    protected ActivityInput[] getIterationInputs() {
        return new ActivityInput[] { new TypedActivityInput(INPUT_SPARQL_EXPRESSION, String.class) };
    }

    @Override
    protected void postprocess() throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException {
        LOG.debug("Into RDFQueryActivity, postprocess");
    }

    @Override
    protected void preprocess() throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException {
        LOG.debug("Into RDFQueryActivity, preprocess");
        validateOutput(OUTPUT_SPARQL_RESULTS);
        try {
            repository = mResource.getRepository();
            model = mResource.getModel();
        } catch (RDFModelUseException e) {
            try {
                throw new ActivitySPARQLException(e.getClass().getName(), "Error accessing the Local RDF model for query");
            } catch (ActivitySPARQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    protected void processIteration(Object[] iterationData) throws ActivityProcessingException, ActivityTerminatedException, ActivityUserException {
        LOG.debug("Into RDFQueryActivity, process iteration");
        final String expression = (String) iterationData[0];
        try {
            repository = mResource.getRepository();
            model = mResource.getModel();
            InputStream queryResults = mResource.queryLocalRDF(null, expression);
            RDFUtilities.createTupleList(queryResults, getOutput(), expression, true, true);
            queryResults.close();
        } catch (IOException e) {
            throw new ActivityIOException(e);
        } catch (RDFModelUseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Class getTargetResourceAccessorClass() {
        return RDFConnectionProvider.class;
    }

    public void setTargetResourceAccessor(ResourceAccessor targetResource) {
        mResource = (RDFConnectionProvider) targetResource;
    }

    protected void cleanUp() throws Exception {
        super.cleanUp();
        LOG.debug("Into RDFQueryActivity, cleanup");
        if (mResource != null) {
            mResource.releaseRepository(repository);
        }
    }
}
