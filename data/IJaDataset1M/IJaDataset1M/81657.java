package org.tripcom.distribution.unittest;

import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openrdf.model.Statement;
import org.tripcom.distribution.util.RDF2Triples;
import static junit.framework.Assert.*;

public class RDF2TriplesTest {

    public static String mString;

    public static String eString;

    public static List<Statement> list;

    public static RDF2Triples rdf;

    @BeforeClass
    public static void setupOnlyOnce() {
        mString = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" + "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n" + "xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n" + "<rdf:Description rdf:about=\"http://de.wikipedia.org/wiki/Resource_Description_Framework\">\n" + "<dc:title>Resource Description Framework</dc:title>\n" + "<dc:publisher>Wikipedia - Die freie Enzyklopädie</dc:publisher>\n" + "</rdf:Description>\n" + "</rdf:RDF>";
        eString = "error";
        rdf = new RDF2Triples();
        list = rdf.getStatementsFromString(mString);
    }

    @Test
    public void testGetStatementsFromString() {
        RDF2Triples rdf = new RDF2Triples();
        List<Statement> statements = rdf.getStatementsFromString(mString);
        Statement s0 = statements.get(0);
        assertEquals(2, statements.size());
        assertEquals(s0.getObject().stringValue(), "Resource Description Framework");
    }

    @Test
    public void testGetStringFromList() {
        RDF2Triples rdf = new RDF2Triples();
        String stringRdf = rdf.getStringFromList(list);
        assertTrue(stringRdf.contains("\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\""));
    }
}
