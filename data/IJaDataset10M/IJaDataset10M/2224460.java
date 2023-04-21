package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.test.RippleTestCase;
import org.openrdf.model.vocabulary.XMLSchema;

/**
 * Author: josh
 * Date: May 3, 2008
 * Time: 1:42:20 PM
 */
public class AssertTest extends RippleTestCase {

    public void testSimple() throws Exception {
        reduce("@prefix ex: <http://example.org/assertTest/>.");
        assertReducesTo("ex:a rdf:type >>");
        assertReducesTo("ex:a rdf:type ex:Example assert >>", "ex:a");
        assertReducesTo("ex:a rdf:type >>", "ex:Example");
        assertReducesTo("ex:Example rdf:type <<", "ex:a");
        assertReducesTo("ex:a rdf:type ex:Thing assert >>", "ex:a");
        assertReducesTo("ex:a rdf:type >>", "ex:Example", "ex:Thing");
        assertReducesTo("ex:Thing rdf:type <<", "ex:a");
        modelConnection.remove(null, null, modelConnection.list());
        modelConnection.commit();
        assertReducesTo("ex:b rdf:type () assert >>", "ex:b");
        assertReducesTo("ex:b rdf:type >>", "()");
        assertReducesTo("() rdf:type <<", "ex:b");
        assertReducesTo("ex:c rdf:type (1 2) assert >>", "ex:c");
        assertReducesTo("ex:c rdf:type >> rdf:type >>", "rdf:List");
        assertReducesTo("ex:c rdf:type >>", "(1 2)");
        assertReducesTo("(1 2) rdf:type <<");
    }

    public void testLiteralObjects() throws Exception {
        reduce("@prefix ex: <http://example.org/assertTest/>.");
        modelConnection.remove(null, null, modelConnection.value(42));
        modelConnection.commit();
        assertReducesTo("ex:a ex:specialNumer >>");
        assertReducesTo("ex:a ex:specialNumer 42 assert >>", "ex:a");
        assertReducesTo("ex:a ex:specialNumer >>", "42");
        assertReducesTo("42 ex:specialNumer <<", "ex:a");
        assertReducesTo("42.0 ex:specialNumer <<");
        modelConnection.remove(null, null, modelConnection.value("something"));
        modelConnection.commit();
        assertReducesTo("ex:a rdfs:comment >>");
        assertReducesTo("ex:a rdfs:comment \"something\" assert >>", "ex:a");
        assertReducesTo("ex:a rdfs:comment >>", "\"something\"");
        assertReducesTo("\"something\" rdfs:comment <<", "ex:a");
        modelConnection.remove(null, null, modelConnection.value("something", XMLSchema.STRING));
        modelConnection.commit();
        assertReducesTo("ex:a rdfs:label >>");
        assertReducesTo("ex:a rdfs:label \"something\"^^xsd:string assert >>", "ex:a");
        assertReducesTo("ex:a rdfs:label >>", "\"something\"^^xsd:string");
        assertReducesTo("\"something\"^^xsd:string rdfs:label <<", "ex:a");
    }

    public void testImpossibleStatements() throws Exception {
        reduce("@prefix ex: <http://example.org/assertTest/>.");
        assertReducesTo("42 rdfs:comment \"foo\" assert >>", "42");
        assertReducesTo("42 rdfs:comment >>");
        assertReducesTo("\"foo\" rdfs:comment <<");
        assertReducesTo("ex:a 42 \"foo\" assert >>", "ex:a");
        assertReducesTo("ex:a 42 >>");
        assertReducesTo("\"foo\" 42 <<");
    }
}
