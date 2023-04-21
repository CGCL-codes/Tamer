package variant.flight;

import org.omwg.ontology.Ontology;
import org.wsml.reasoner.api.WSMLReasoner;
import org.wsml.reasoner.api.WSMLReasonerFactory;
import org.wsml.reasoner.api.WSMLReasonerFactory.BuiltInReasoner;
import org.wsml.reasoner.api.inconsistency.InconsistencyException;
import org.wsmo.factory.Factory;
import org.wsmo.factory.LogicalExpressionFactory;
import org.wsmo.factory.WsmoFactory;
import org.wsmo.wsml.Parser;
import base.BaseReasonerTest;

/**
 * Interface or class description
 * 
 * <pre>
 *    Created on 20.04.2006
 *    Committed by $Author: graham $
 *    $Source: /home/richi/temp/w2r/wsml2reasoner/test/variant/flight/ConstraintViolationCheck.java,v $,
 * </pre>
 * 
 * @author Holger lausen
 * 
 * @version $Revision: 1.1 $ $Date: 2007-11-15 13:06:43 $
 */
public class ConstraintViolationCheck extends BaseReasonerTest {

    Parser parser;

    LogicalExpressionFactory leFactory;

    WsmoFactory wsmoFactory;

    WSMLReasoner reasoner;

    BuiltInReasoner previous;

    public void setUp() throws Exception {
        super.setUp();
        parser = Factory.createParser(null);
        leFactory = Factory.createLogicalExpressionFactory(null);
        wsmoFactory = Factory.createWsmoFactory(null);
        reasoner = BaseReasonerTest.getReasoner();
        previous = BaseReasonerTest.reasoner;
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        resetReasoner(previous);
    }

    public void constraintViolationCheck() throws Exception {
        String test = "namespace { _\"i:\"} \n" + "ontology simpsons \n" + "concept sub  \n" + "//  c impliesType (1 *)_string \n" + "instance ia memberOf sub  \n" + "axiom i definedBy !-?x memberOf sub. ";
        Ontology ont = (Ontology) parser.parse(new StringBuffer(test))[0];
        reasoner = BaseReasonerTest.getReasoner();
        try {
            reasoner.registerOntology(ont);
        } catch (InconsistencyException e) {
            System.out.println(e.getViolations().iterator().next());
        }
    }

    public void testFlightReasoners() throws Exception {
        resetReasoner(WSMLReasonerFactory.BuiltInReasoner.IRIS);
        constraintViolationCheck();
        resetReasoner(WSMLReasonerFactory.BuiltInReasoner.MINS);
        constraintViolationCheck();
        if (base.BaseReasonerTest.exists("org.wsml.reasoner.builtin.kaon2.Kaon2Facade")) {
            resetReasoner(WSMLReasonerFactory.BuiltInReasoner.KAON2);
            constraintViolationCheck();
        }
    }
}
