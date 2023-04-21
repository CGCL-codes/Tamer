package abstractTests.lp;

import helper.LPHelper;
import helper.OntologyHelper;
import helper.Results;
import junit.framework.TestCase;
import abstractTests.LP;

public abstract class AbstractAttribute3InheritingImpliesType extends TestCase implements LP {

    private static final String ONTOLOGY_FILE = "files/attribute3_inheriting_implies_type.wsml";

    private static final String NS = "http://example.com/attribute3#";

    private static final String WSML_STRING = "http://www.wsmo.org/wsml/wsml-syntax#string";

    public void testAttributeInheritance() throws Exception {
        Results r = new Results("concept", "attribute", "type");
        for (int i = 1; i <= 10; ++i) {
            for (int a = i; a >= 1; --a) {
                r.addBinding(Results.iri(NS + "c" + i), Results.iri(NS + "a" + a), Results.iri(WSML_STRING));
            }
        }
        LPHelper.executeQueryAndCheckResults(OntologyHelper.loadOntology(ONTOLOGY_FILE), "?concept[?attribute impliesType ?type]", r.get(), getLPReasoner());
    }
}
