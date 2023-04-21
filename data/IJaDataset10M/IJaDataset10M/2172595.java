package abstractTests.lp;

import helper.LPHelper;
import helper.OntologyHelper;
import junit.framework.TestCase;
import org.wsml.reasoner.api.inconsistency.InconsistencyException;
import abstractTests.LP;

public abstract class AbstractViolation2AttributeIsWrongConcept extends TestCase implements LP {

    private static final String ONTOLOGY_FILE = "files/violation2_attribute_is_wrong_concept.wsml";

    public void testInconsistency() throws Exception {
        String query = "?x memberOf ?y";
        try {
            LPHelper.executeQuery(OntologyHelper.loadOntology(ONTOLOGY_FILE), query, getLPReasoner());
            fail("Should have thrown InconsistencyException");
        } catch (InconsistencyException e) {
        }
    }
}
