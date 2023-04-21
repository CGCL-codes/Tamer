package engine.iris;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.omwg.logicalexpression.LogicalExpression;
import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.Ontology;
import org.omwg.ontology.Variable;
import org.wsml.reasoner.api.LPReasoner;
import org.wsml.reasoner.api.WSMLReasonerFactory;
import org.wsml.reasoner.api.WSMLReasonerFactory.BuiltInReasoner;
import org.wsmo.factory.Factory;
import org.wsmo.factory.LogicalExpressionFactory;
import org.wsmo.factory.WsmoFactory;
import org.wsmo.wsml.Parser;
import base.BaseReasonerTest;

public class BuiltInDateTimeTest extends BaseReasonerTest {

    private WsmoFactory wsmoFactory;

    private LogicalExpressionFactory leFactory;

    private LPReasoner wsmlReasoner;

    private BuiltInReasoner previous;

    private Parser parser;

    private Ontology ontology;

    protected void setUp() throws Exception {
        super.setUp();
        previous = BaseReasonerTest.reasoner;
        BaseReasonerTest.reasoner = WSMLReasonerFactory.BuiltInReasoner.IRIS;
        wsmlReasoner = (LPReasoner) BaseReasonerTest.getReasoner();
        wsmoFactory = Factory.createWsmoFactory(null);
        leFactory = Factory.createLogicalExpressionFactory(null);
        parser = Factory.createParser(null);
    }

    public void testRun() throws Exception {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("files/builtInDateTimeTest.wsml");
        assertNotNull(is);
        ontology = (Ontology) parser.parse(new InputStreamReader(is))[0];
        String nsp = ontology.getDefaultNamespace().getIRI().toString();
        wsmlReasoner.registerOntology(ontology);
        String query = "?x memberOf Child";
        LogicalExpression qExpression = leFactory.createLogicalExpression(query, ontology);
        Set<Map<Variable, Term>> expected = new HashSet<Map<Variable, Term>>();
        Map<Variable, Term> binding = new HashMap<Variable, Term>();
        binding.put(leFactory.createVariable("x"), wsmoFactory.createIRI(nsp + "Anna"));
        expected.add(binding);
        binding.put(leFactory.createVariable("x"), wsmoFactory.createIRI(nsp + "Chris"));
        expected.add(binding);
        System.out.println("WSML Query LE:");
        System.out.println(qExpression.toString());
        System.out.println("\n\nExpecting " + expected.size() + " results...\n");
        Set<Map<Variable, Term>> result = wsmlReasoner.executeQuery(qExpression);
        System.out.println("Found < " + result.size() + " > results to the query:\n");
        assertEquals(expected.size(), result.size());
        Set<String> resultSet = new HashSet<String>(2);
        int i = 0;
        for (Map<Variable, Term> resultBinding : result) {
            System.out.println("result binding (" + (i++) + "): " + resultBinding.toString());
            resultSet.add(resultBinding.entrySet().iterator().next().getValue().toString());
        }
        assertTrue(resultSet.contains(nsp + "Anna"));
        assertTrue(resultSet.contains(nsp + "Chris"));
        wsmlReasoner.deRegister();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        resetReasoner(previous);
    }
}
