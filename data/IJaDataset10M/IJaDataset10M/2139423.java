package org.wsml.reasoner.transformation;

import java.util.HashSet;
import java.util.Set;
import junit.framework.TestCase;
import org.omwg.logicalexpression.LogicalExpression;
import org.omwg.ontology.Axiom;
import org.wsml.reasoner.impl.WSMO4JManager;
import org.wsml.reasoner.transformation.le.LETestHelper;
import org.wsmo.factory.LogicalExpressionFactory;
import org.wsmo.factory.WsmoFactory;
import org.wsmo.wsml.ParserException;

public class ConstraintReplacementNormalizerTest extends TestCase {

    private ConstraintReplacementNormalizer normalizer;

    protected String ns = "http://ex.org#";

    protected WsmoFactory wsmoFactory;

    protected LogicalExpressionFactory leFactory;

    public ConstraintReplacementNormalizerTest() {
        super();
    }

    protected void setUp() throws Exception {
        super.setUp();
        WSMO4JManager wsmoManager = new WSMO4JManager();
        normalizer = new ConstraintReplacementNormalizer(wsmoManager);
        wsmoFactory = wsmoManager.getWSMOFactory();
        leFactory = wsmoManager.getLogicalExpressionFactory();
    }

    public void testInsertViolationsAxiom() {
        Set<Axiom> axioms = new HashSet<Axiom>();
        Set<Axiom> out = normalizer.normalizeAxioms(axioms);
        assertEquals(1, out.size());
        Axiom violationsAxiom = out.iterator().next();
        assertEquals("_#", violationsAxiom.getIdentifier().toString());
        Set<LogicalExpression> les = violationsAxiom.listDefinitions();
        for (LogicalExpression le : les) {
            assertTrue(le.toString().contains("/VIOLATION"));
        }
        assertEquals(5, les.size());
    }

    public void testEmptyAxiomsAreIgnored() {
        Axiom axiom1 = wsmoFactory.createAxiom(wsmoFactory.createIRI(ns + "axiom_empty"));
        Set<Axiom> axioms = new HashSet<Axiom>();
        axioms.add(axiom1);
        Set<Axiom> out = normalizer.normalizeAxioms(axioms);
        for (Axiom ax : out) {
            assertFalse(ax.getIdentifier().toString().equals(axiom1.getIdentifier().toString()));
        }
        assertEquals(1, out.size());
    }

    public void testConstraintsAreReplaced() throws ParserException {
        Axiom axiom1 = wsmoFactory.createAxiom(wsmoFactory.createIRI(ns + "axiom1"));
        Set<Axiom> axioms = new HashSet<Axiom>();
        axiom1.addDefinition(LETestHelper.buildLE("!- _\"urn:a\" memberOf _#"));
        axioms.add(axiom1);
        Set<Axiom> out = normalizer.normalizeAxioms(axioms);
        String expected = "" + AnonymousIdUtils.ANONYMOUS_PREFIX;
        assertTrue(containsString(out, expected));
        assertEquals(2, out.size());
        for (Axiom ax : out) {
            for (LogicalExpression le : ax.listDefinitions()) {
                assertTrue(checkContains(le));
            }
        }
    }

    public void testNormalizeAxiomsNormalAxiom() {
        String axiomUri = ns + "axiom_normaliser_test";
        Axiom axiom1 = wsmoFactory.createAxiom(wsmoFactory.createIRI(axiomUri));
        Set<Axiom> axioms = new HashSet<Axiom>();
        axioms.add(axiom1);
        Set<Axiom> out = normalizer.normalizeAxioms(axioms);
        assertFalse(contains(out, axiomUri));
        assertTrue(contains(out, "_#"));
        assertEquals(1, out.size());
    }

    public void testNormalizeAxioms() {
        Axiom axiom1 = wsmoFactory.createAxiom(wsmoFactory.createIRI(ns + "axiom_normaliser_test"));
        Axiom axiom2 = wsmoFactory.createAxiom(wsmoFactory.createIRI(AnonymousIdUtils.MINCARD_PREFIX));
        Axiom axiom3 = wsmoFactory.createAxiom(wsmoFactory.createIRI(AnonymousIdUtils.MAXCARD_PREFIX));
        Axiom axiom4 = wsmoFactory.createAxiom(wsmoFactory.createAnonymousID());
        Set<Axiom> axioms = new HashSet<Axiom>();
        axioms.add(axiom1);
        axioms.add(axiom2);
        axioms.add(axiom3);
        axioms.add(axiom4);
        Set<Axiom> out = normalizer.normalizeAxioms(axioms);
        assertEquals(1, out.size());
        assertFalse(axioms.equals(out));
    }

    private boolean contains(Set<Axiom> axioms, String identifier) {
        boolean found = false;
        for (Axiom ax : axioms) {
            if (ax.getIdentifier().toString().equals(identifier)) found = true;
        }
        return found;
    }

    private boolean containsString(Set<Axiom> axioms, String expected) {
        boolean found = false;
        for (Axiom ax : axioms) {
            if (ax.getIdentifier().toString().contains(expected)) found = true;
        }
        return found;
    }

    private boolean checkContains(LogicalExpression le) throws ParserException {
        if (le.toString().trim().equals("_\"http://www.wsmo.org/reasoner/VIOLATION\"\n:-\n_\"http://www.wsmo.org/reasoner/ATTR_OFTYPE\"(?v1,?v2,?v3,?v4,?v5).")) {
            return true;
        }
        if (le.toString().trim().equals("_\"http://www.wsmo.org/reasoner/VIOLATION\"\n:-\n_\"http://www.wsmo.org/reasoner/MIN_CARD\"(?v1,?v2,?v3).")) {
            return true;
        }
        if (le.toString().trim().equals("_\"http://www.wsmo.org/reasoner/VIOLATION\"\n:-\n_\"http://www.wsmo.org/reasoner/MAX_CARD\"(?v1,?v2,?v3).")) {
            return true;
        }
        if (le.toString().trim().equals("_\"http://www.wsmo.org/reasoner/VIOLATION\"\n:-\n_\"http://www.wsmo.org/reasoner/NAMED_USER\"(?v1).")) {
            return true;
        }
        if (le.toString().trim().equals("_\"http://www.wsmo.org/reasoner/VIOLATION\"\n:-\n_\"http://www.wsmo.org/reasoner/UNNAMED_USER\"(?v1).")) {
            return true;
        }
        if (le.toString().trim().equals("_\"http://www.wsmo.org/reasoner/NAMED_USER\"(_\"http://ex.org#axiom1\")\n:-\n_\"urn:a\" memberOf _#.")) {
            return true;
        }
        return false;
    }
}
