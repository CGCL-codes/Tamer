package abstractTests.lp;

import helper.LPHelper;
import helper.OntologyHelper;
import helper.Results;
import junit.framework.TestCase;
import abstractTests.LP;

/**
 *
 */
public abstract class AbstractRelations2SimpleUnnamed extends TestCase implements LP {

    private static final String NS = "http://example.com/relations2#";

    private static final String ONTOLOGY_FILE = "files/relations2_simple_unnamed.wsml";

    public void testDeclaredRelation() throws Exception {
        String query = "Declared(?x1,?x2)";
        Results r = new Results("x1", "x2");
        r.addBinding(Results.iri(NS + "i11"), Results.iri(NS + "i21"));
        r.addBinding(Results.iri(NS + "i11"), Results.iri(NS + "i23"));
        LPHelper.executeQueryAndCheckResults(OntologyHelper.loadOntology(ONTOLOGY_FILE), query, r.get(), getLPReasoner());
    }

    public void testNotDeclaredRelation() throws Exception {
        String query = "NotDeclared(?x1,?x2)";
        Results r = new Results("x1", "x2");
        r.addBinding(Results.iri(NS + "i12"), Results.iri(NS + "i22"));
        r.addBinding(Results.iri(NS + "i12"), Results.iri(NS + "i21"));
        LPHelper.executeQueryAndCheckResults(OntologyHelper.loadOntology(ONTOLOGY_FILE), query, r.get(), getLPReasoner());
    }

    public void testDeclaredAfterRelation() throws Exception {
        String query = "DeclaredAfter(?x1,?x2)";
        Results r = new Results("x1", "x2");
        r.addBinding(Results.iri(NS + "i13"), Results.iri(NS + "i23"));
        r.addBinding(Results.iri(NS + "i13"), Results.iri(NS + "i22"));
        LPHelper.executeQueryAndCheckResults(OntologyHelper.loadOntology(ONTOLOGY_FILE), query, r.get(), getLPReasoner());
    }
}
