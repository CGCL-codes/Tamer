package com.hp.hpl.mars.portal.util;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.mars.portal.Portal;

public class LiteralWrapperTest {

    Model model = ModelFactory.createDefaultModel();

    Portal portal = new Portal();

    @Test
    public void sparqlPlainLiteral() {
        Literal l = model.createLiteral("aa");
        LiteralWrapper target = new LiteralWrapper(l, portal);
        assertEquals("\"aa\"", target.getSparql());
        assertEquals("aa", target.getStringValue());
        assertEquals("aa", target.toString());
    }

    @Test
    public void sparqlTypedLiteral() {
        Literal l = model.createTypedLiteral("2006-11-05", XSDDatatype.XSDdate);
        LiteralWrapper target = new LiteralWrapper(l, portal);
        assertEquals("\"2006-11-05\"^^<http://www.w3.org/2001/XMLSchema#date>", target.getSparql());
        assertEquals("2006-11-05", target.getStringValue());
        assertEquals("2006-11-05^^http://www.w3.org/2001/XMLSchema#date", target.toString());
    }

    @Test
    public void sparqlEscapediteral() {
        Literal l = model.createLiteral("\r\n\"\\");
        LiteralWrapper target = new LiteralWrapper(l, portal);
        assertEquals("\"\\r\\n\\\"\\\\\"", target.getSparql());
        assertEquals("\r\n\"\\", target.getStringValue());
        assertEquals("\r\n\"\\", target.toString());
    }

    @Test
    public void sparqlLanguageLiteral() {
        Literal l = model.createLiteral("Don't immanentize the eschaton", "en");
        LiteralWrapper target = new LiteralWrapper(l, portal);
        assertEquals("\"Don't immanentize the eschaton\"@en", target.getSparql());
        assertEquals("Don't immanentize the eschaton", target.getStringValue());
        assertEquals("Don't immanentize the eschaton@en", target.toString());
    }
}
