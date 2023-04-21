package org.wsml.reasoner.ext.sql;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import junit.framework.TestCase;
import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.Ontology;
import org.omwg.ontology.Variable;
import org.wsml.reasoner.api.LPReasoner;
import org.wsml.reasoner.api.inconsistency.InconsistencyException;
import org.wsml.reasoner.ext.sql.QueryUtil;
import org.wsml.reasoner.ext.sql.ReasonerResult;
import org.wsml.reasoner.ext.sql.WSMLReasonerFacade;
import org.wsml.reasoner.impl.DefaultWSMLReasonerFactory;
import org.wsmo.common.TopEntity;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.wsml.ParserException;

public class WSMLReasonerFacadeTest extends TestCase {

    protected WSMLReasonerFacade facade;

    protected String localOntologyIRI;

    protected String testmemberOfQuery1;

    protected String testArwenExpected;

    protected Ontology loaded;

    protected void setUp() throws Exception {
        super.setUp();
        File file = new File("test/files/lordOfTheRings.wsml");
        URL ontoTestURL = file.toURI().toURL();
        localOntologyIRI = ontoTestURL.toString();
        testmemberOfQuery1 = "?x memberOf ?y";
        testArwenExpected = "?x[hasParent hasValue Elrond]";
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testExecuteWsmlQuery() {
        facade = new WSMLReasonerFacade();
        try {
            ReasonerResult res = facade.executeWsmlQuery(testmemberOfQuery1, localOntologyIRI);
            assertNotNull(res);
            ReasonerResult arwenInThere = facade.executeWsmlQuery(testArwenExpected, localOntologyIRI);
            Set<Map<Variable, Term>> r = arwenInThere.getResult();
            assertEquals(true, r.size() > 0);
            validateResult(arwenInThere);
        } catch (ParserException e) {
            fail(e.getMessage());
        } catch (InconsistencyException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        } catch (InvalidModelException e) {
            fail(e.getMessage());
        }
    }

    public void testExecuteWsmlQueryAgainstPreloadedReasoner() {
        LPReasoner reasoner = DefaultWSMLReasonerFactory.getFactory().createFlightReasoner(null);
        try {
            facade = new WSMLReasonerFacade();
            Ontology loaded = facade.loadOntology(localOntologyIRI);
            reasoner.registerOntology(loaded);
            facade.setReasoner(reasoner);
            ReasonerResult res = facade.executeWsmlQuery(testmemberOfQuery1);
            assertNotNull(res);
            ReasonerResult arwenInThere = facade.executeWsmlQuery(testArwenExpected, localOntologyIRI);
            Set<Map<Variable, Term>> r = arwenInThere.getResult();
            assertEquals(true, r.size() > 0);
            validateResult(arwenInThere);
        } catch (ParserException e) {
            fail(e.getMessage());
        } catch (InconsistencyException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        } catch (InvalidModelException e) {
            fail(e.getMessage());
        }
    }

    public void testLoadLocalOntology() {
        try {
            facade = new WSMLReasonerFacade();
            Ontology loaded = facade.loadOntology(localOntologyIRI);
            assertNotNull(loaded);
        } catch (IOException e) {
            fail(e.getMessage());
        } catch (ParserException e) {
            fail(e.getMessage());
        } catch (InvalidModelException e) {
            fail(e.getMessage());
        }
    }

    private void validateResult(ReasonerResult result) {
        Set<Map<Variable, Term>> r = result.getResult();
        for (Map<Variable, Term> row : r) {
            for (Variable var : row.keySet()) {
                assertEquals("?x", var.toString());
                Term valueForVar = row.get(var);
                String termAsString = QueryUtil.termToString(valueForVar, facade.getOntology());
                assertEquals("Arwen", termAsString);
            }
        }
    }
}
