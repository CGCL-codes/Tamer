package com.squadlimber.awesome;

import junit.framework.TestCase;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.xpath.XPath;

public class ParsingHtmlAndOgnlSpikeTest extends TestCase {

    public void testParseXhtmlElement() throws Exception {
        String xhtml = "<html><head></head><body><p>My Element Text</p></body></html>";
        Document doc = XmlUtils.toDocument(xhtml);
        Element paragraphElement = (Element) XPath.selectSingleNode(doc, "//html/body/p");
        assertEquals("My Element Text", paragraphElement.getTextTrim());
    }

    public void testParseAttributeFromXhtml() throws Exception {
        String xhtml = "<html><head></head><body><p madeUp=\"monkeys\">My Element Text</p></body></html>";
        Document doc = XmlUtils.toDocument(xhtml);
        Attribute madeUpAttribute = (Attribute) XPath.selectSingleNode(doc, "//html/body/p/@madeUp");
        assertEquals("monkeys", madeUpAttribute.getValue());
    }

    public void testParseAttributeWithNamespaceFromXhtml() throws Exception {
        String xhtml = "<html xmlns:accept='http://squadlimber.com/'><head></head><body><p accept:madeUp=\"monkeys\">My Element Text</p></body></html>";
        Document doc = XmlUtils.toDocument(xhtml);
        Attribute namespacedMadeUpAttribute = (Attribute) XPath.selectSingleNode(doc, "//html/body/p/@accept:madeUp");
        assertEquals("monkeys", namespacedMadeUpAttribute.getValue());
    }

    public void testParsingAnOgnlExpressionFromXhtml() throws Exception {
        String xhtml = "<html xmlns:accept='http://squadlimber.com/'><head></head><body><p accept:getThisValue=\"childClass.fruit\">My Element Text</p></body></html>";
        class ExampleTest {

            class SomeChildClass {

                public String fruit = "oranges";
            }

            public SomeChildClass childClass = new SomeChildClass();
        }
        ExampleTest exampleTest = new ExampleTest();
        Document doc = XmlUtils.toDocument(xhtml);
        Attribute getThisValueAttrib = (Attribute) XPath.selectSingleNode(doc, "//html/body/p/@accept:getThisValue");
        String fruit = (String) OgnlUtils.getValue(getThisValueAttrib.getValue(), exampleTest);
        assertEquals("oranges", fruit);
    }
}
