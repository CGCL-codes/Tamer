package variant.rule;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.Variable;
import org.wsml.reasoner.api.WSMLReasonerFactory;
import org.wsml.reasoner.api.WSMLReasonerFactory.BuiltInReasoner;
import org.wsml.reasoner.api.inconsistency.InconsistencyException;
import base.BaseReasonerTest;

public class MinsInconsistencyTestWithFSymbol extends BaseReasonerTest {

    private static final String ONTOLOGY_FILE = "files/InconsistencyTestWithFSymbol.wsml";

    BuiltInReasoner previous;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        previous = BaseReasonerTest.reasoner;
        resetReasoner(WSMLReasonerFactory.BuiltInReasoner.MINS);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        resetReasoner(previous);
    }

    /**
     * fails because of design of API
     * @throws Exception
     */
    public void testInconsistency() throws Exception {
        String query = "?x memberOf ?y";
        Set<Map<Variable, Term>> expected = new HashSet<Map<Variable, Term>>();
        try {
            setupScenario(ONTOLOGY_FILE);
            performQuery(query, expected);
        } catch (InconsistencyException e) {
            System.out.println("Inconsistent!~ Correct!");
        }
        System.out.println("Finished query.");
    }
}
