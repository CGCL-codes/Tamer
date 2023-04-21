package variant.flight;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;
import org.omwg.logicalexpression.LogicalExpression;
import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.Instance;
import org.omwg.ontology.Ontology;
import org.omwg.ontology.Variable;
import org.wsml.reasoner.api.LPReasoner;
import org.wsml.reasoner.api.WSMLReasonerFactory;
import org.wsml.reasoner.api.WSMLReasonerFactory.BuiltInReasoner;
import org.wsmo.wsml.Parser;
import base.BaseReasonerTest;

/**
 * Interface or class description
 * 
 * <pre>
 *   Created on 20.04.2006
 *   Committed by $Author: graham $
 *   $Source: /home/richi/temp/w2r/wsml2reasoner/test/variant/flight/RegisterMultipleTimes.java,v $,
 * </pre>
 * 
 * @author Holger lausen, Martin Tanler
 * 
 * @version $Revision: 1.2 $ $Date: 2007-08-24 16:26:13 $
 */
public class RegisterMultipleTimes extends BaseReasonerTest {

    Parser parser = org.wsmo.factory.Factory.createParser(null);

    LPReasoner reasoner;

    BuiltInReasoner previous;

    String file1 = "files/RegisterMultipleTimes1.wsml";

    String file2 = "files/RegisterMultipleTimes2.wsml";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setUpFactories();
        previous = BaseReasonerTest.reasoner;
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        resetReasoner(previous);
    }

    /**
     * This test failed since WSMO4J uses statics in its default factory
     * Now all new objects are created with a unique URI, sort of waste
     * of resources, but works.
     * 
     * @throws Exception
     */
    public void clearDeregistration() throws Exception {
        reasoner = (LPReasoner) BaseReasonerTest.getReasoner();
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(file1);
        assertNotNull(is);
        Ontology ont0 = (Ontology) parser.parse(new InputStreamReader(is))[0];
        InputStream is1 = this.getClass().getClassLoader().getResourceAsStream(file2);
        assertNotNull(is1);
        Ontology ont1 = (Ontology) parser.parse(new InputStreamReader(is1))[0];
        LogicalExpression query = leFactory.createLogicalExpression("?x memberOf ?y", ont0);
        Set<Map<Variable, Term>> result = null;
        for (int i = 0; i < 50; i++) {
            reasoner.registerOntology(ont0);
            result = reasoner.executeQuery(query);
            reasoner.deRegister();
            assertEquals("failed in run:" + i, 1, result.size());
            reasoner.registerOntology(ont1);
            result = reasoner.executeQuery(query);
            reasoner.deRegister();
            assertEquals("failed in run:" + i, 1, result.size());
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void removeInstances() throws Exception {
        reasoner = (LPReasoner) BaseReasonerTest.getReasoner();
        String ns = "http://ex1.org#";
        String test = "namespace _\"" + ns + "\" \n" + "ontology o1 \n" + "concept c \n" + "instance i1 memberOf c \n " + "instance i2 memberOf c \n ";
        Ontology o = (Ontology) parser.parse(new StringBuffer(test))[0];
        LogicalExpression query = leFactory.createLogicalExpression("?x memberOf ?y", o);
        Instance instance = o.findInstance(wsmoFactory.createIRI(ns + "i1"));
        for (int i = 0; i < 10; i++) {
            Set<Map<Variable, Term>> result = null;
            reasoner.registerOntology(o);
            result = reasoner.executeQuery(query);
            reasoner.deRegister();
            assertEquals("failure in run " + i, 2, o.listInstances().size());
            assertEquals("failure in run " + i, 2, result.size());
            o.removeInstance(instance);
            reasoner.registerOntology(o);
            result = reasoner.executeQuery(query);
            reasoner.deRegister();
            assertEquals("failure in run " + i, 1, o.listInstances().size());
            assertEquals("failure in run " + i, 1, result.size());
            o.addInstance(instance);
        }
    }

    public void testFlightReasoners() throws Exception {
        resetReasoner(WSMLReasonerFactory.BuiltInReasoner.IRIS_STRATIFIED);
        clearDeregistration();
        removeInstances();
        resetReasoner(WSMLReasonerFactory.BuiltInReasoner.MINS);
        clearDeregistration();
        removeInstances();
        if (exists("org.wsml.reasoner.builtin.kaon2.Kaon2Facade")) {
            resetReasoner(WSMLReasonerFactory.BuiltInReasoner.KAON2);
            clearDeregistration();
            removeInstances();
        }
    }
}
