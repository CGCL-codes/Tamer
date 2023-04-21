package framework.transformation;

import junit.framework.TestCase;
import org.wsml.reasoner.transformation.AnonymousIdUtils;

public class AnonymousIdUtilsTest extends TestCase {

    public void testIriGeneration() {
        String iri1 = AnonymousIdUtils.getNewAnonymousIri();
        System.out.println("First IRI: '" + iri1 + "'");
        String iri2 = AnonymousIdUtils.getNewAnonymousIri();
        System.out.println("Second IRI: '" + iri2 + "'");
        assertFalse(iri1.equals(iri2));
    }

    public void testIsAnonymousIri() {
        String iri1 = AnonymousIdUtils.getNewAnonymousIri();
        String iri2 = "urn:blah";
        assertTrue(AnonymousIdUtils.isAnonymousIri(iri1));
        assertFalse(AnonymousIdUtils.isAnonymousIri(iri2));
    }
}
