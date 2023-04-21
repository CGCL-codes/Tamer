package net.sundell.snax;

import static org.junit.Assert.assertEquals;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.StartElement;
import static net.sundell.snax.TestUtils.*;
import org.junit.Test;

public class TestSelectors {

    private static XMLInputFactory factory = XMLInputFactory.newInstance();

    @Test
    public void testElements() throws Exception {
        final TestHandler foo = new TestHandler();
        SNAXParser<?> parser = SNAXParser.createParser(factory, new NodeModelBuilder<Object>() {

            {
                elements("xml", "foo", "bar").attach(foo);
            }
        }.build());
        parser.parse(new StringReader("<xml><foo><bar/></foo></xml>"), null);
        assertEquals("bar", foo.elementName);
        final TestHandler bar = new TestHandler();
        parser = SNAXParser.createParser(factory, new NodeModelBuilder<Object>() {

            {
                element("xml").elements("foo", "bar").attach(bar);
            }
        }.build());
        parser.parse(new StringReader("<xml><foo><bar/></foo></xml>"), null);
        assertEquals("bar", bar.elementName);
    }

    @Test
    public void testRegexElementSelectors() throws Exception {
        final TestHandler foo = new TestHandler();
        SNAXParser<?> parser = SNAXParser.createParser(factory, new NodeModelBuilder<Object>() {

            {
                element("xml").element(new ElementFilter() {

                    @Override
                    public boolean test(StartElement element) {
                        return Pattern.matches("foo.*", element.getName().getLocalPart());
                    }
                }).attach(foo);
            }
        }.build());
        parser.parse(new StringReader("<xml><foozle /></xml>"), null);
        assertEquals("foozle", foo.elementName);
        final TestHandler bar = new TestHandler();
        parser = SNAXParser.createParser(factory, new NodeModelBuilder<Object>() {

            {
                element(new ElementFilter() {

                    @Override
                    public boolean test(StartElement element) {
                        return Pattern.matches(".*l", element.getName().getLocalPart());
                    }
                }).attach(bar);
            }
        }.build());
        parser.parse(new StringReader("<xml><foozle /></xml>"), null);
        assertEquals("xml", bar.elementName);
    }

    @Test
    public void testChild() throws Exception {
        final TestCHandler foo = new TestCHandler();
        SNAXParser<?> parser = SNAXParser.createParser(factory, new NodeModelBuilder<Object>() {

            {
                element("xml").element("foo").child().attach(foo);
            }
        }.build());
        parser.parse(new StringReader("<xml><foo><bar>BAR</bar></foo></xml>"), null);
        assertEquals("BAR", foo.contents);
        parser.parse(new StringReader("<xml><foo><baz>BAZ</baz></foo></xml>"), null);
        assertEquals("BAZ", foo.contents);
    }

    @Test
    public void testChildWithConstraints() throws Exception {
        final TestCHandler foo = new TestCHandler();
        SNAXParser<?> parser = SNAXParser.createParser(factory, new NodeModelBuilder<Object>() {

            {
                element("xml").element("foo").child(with("id").equalTo("val1")).attach(foo);
            }
        }.build());
        parser.parse(new StringReader("<xml><foo><baz id='val1'>YES</baz><bar>NO</bar></foo></xml>"), null);
        assertEquals("YES", foo.contents);
    }

    @Test
    public void testDescendant() throws Exception {
        final TestCHandler foo = new TestCHandler();
        SNAXParser<?> parser = SNAXParser.createParser(factory, new NodeModelBuilder<Object>() {

            {
                element("xml").descendant().attach(foo);
            }
        }.build());
        parser.parse(new StringReader("<xml><foo>YES</foo></xml>"), null);
        assertEquals("YES", foo.contents);
    }

    @Test
    public void testDeepDescendant() throws Exception {
        final TestCHandler foo = new TestCHandler();
        SNAXParser<?> parser = SNAXParser.createParser(factory, new NodeModelBuilder<Object>() {

            {
                element("xml").descendant().element("bar").attach(foo);
            }
        }.build());
        parser.parse(new StringReader("<xml><foo><bar>YES</bar></foo></xml>"), null);
        assertEquals("YES", foo.contents);
        foo.contents = "";
        parser.parse(new StringReader("<xml><foo><baz><bar>YES!</bar></baz></foo></xml>"), null);
        assertEquals("YES!", foo.contents);
    }

    @Test
    public void testDescendantWithConstraints() throws Exception {
        final TestCHandler foo = new TestCHandler();
        SNAXParser<?> parser = SNAXParser.createParser(factory, new NodeModelBuilder<Object>() {

            {
                element("xml").descendant(with("id").equalTo("test1")).attach(foo);
            }
        }.build());
        parser.parse(new StringReader("<xml><foo><bar id='test1'>YES</bar><bar id='test2'>NO</bar></foo></xml>"), null);
        assertEquals("YES", foo.contents);
        foo.contents = "";
        parser.parse(new StringReader("<xml><foo><baz><bar id='test1'>YES</bar></baz></foo></xml>"), null);
        assertEquals("YES", foo.contents);
        foo.contents = "";
        parser.parse(new StringReader("<xml><foo><bar><bar id='test1'>YES</bar></bar></foo></xml>"), null);
        assertEquals("YES", foo.contents);
    }

    @Test
    public void testDescendantFromRoot() throws Exception {
        final TestCHandler foo = new TestCHandler();
        SNAXParser<?> parser = SNAXParser.createParser(factory, new NodeModelBuilder<Object>() {

            {
                descendant().element("foo").attach(foo);
            }
        }.build());
        parser.parse(new StringReader("<xml><foo>YES</foo></xml>"), null);
        assertEquals("YES", foo.contents);
    }

    @Test
    public void testDescendantWithMultiplePaths() throws Exception {
        final TestCHandler foo = new TestCHandler();
        final TestCHandler bar = new TestCHandler();
        SNAXParser<?> parser = SNAXParser.createParser(factory, new NodeModelBuilder<Object>() {

            {
                descendant().element("foo").attach(foo);
                descendant().element("bar").attach(bar);
            }
        }.build());
        parser.parse(new StringReader("<xml><foo>YES</foo><bar>YES2</bar></xml>"), null);
        assertEquals("YES", foo.contents);
        assertEquals("YES2", bar.contents);
    }

    @Test
    public void testDescendantWithMultipleQualifyingNodes() throws Exception {
        final TestMultiHandler foo = new TestMultiHandler();
        SNAXParser<?> parser = SNAXParser.createParser(factory, new NodeModelBuilder<Object>() {

            {
                descendant(with("foo").equalTo("bar")).attach(foo);
            }
        }.build());
        parser.parse(new StringReader("<xml><e1 foo='bar'><e2 foo='bar'/></e1></xml>"), null);
        assertEquals(2, foo.elementNames.size());
        assertEquals("e1", foo.elementNames.get(0));
        assertEquals("e2", foo.elementNames.get(1));
        foo.elementNames.clear();
        parser.parse(new StringReader("<e1 foo='bar'><x><e2 foo='bar'/></x></e1>"), null);
        assertEquals(2, foo.elementNames.size());
        assertEquals("e1", foo.elementNames.get(0));
        assertEquals("e2", foo.elementNames.get(1));
    }

    @Test
    public void testMultipleChildRules() throws Exception {
        final TestCHandler foo = new TestCHandler();
        final TestCHandler bar = new TestCHandler();
        SNAXParser<?> parser = SNAXParser.createParser(factory, new NodeModelBuilder<Object>() {

            {
                child().element("foo").attach(foo);
                child().element("bar").attach(bar);
            }
        }.build());
        parser.parse(new StringReader("<xml><foo>YES</foo><bar>YES2</bar></xml>"), null);
        assertEquals("YES", foo.contents);
        assertEquals("YES2", bar.contents);
    }

    @Test
    public void testNamedDescendantAndChild() throws Exception {
        final TestCHandler foo = new TestCHandler();
        final TestCHandler bar = new TestCHandler();
        SNAXParser<?> parser = SNAXParser.createParser(factory, new NodeModelBuilder<Object>() {

            {
                descendant("bar").attach(bar);
                child().element("foo").attach(foo);
            }
        }.build());
        parser.parse(new StringReader("<xml><foo>YES</foo><bar>YES2</bar></xml>"), null);
        assertEquals("YES", foo.contents);
        assertEquals("YES2", bar.contents);
    }

    @Test
    public void testNamedDescendantAndChild2() throws Exception {
        final TestCHandler foo = new TestCHandler();
        final TestCHandler bar = new TestCHandler();
        SNAXParser<?> parser = SNAXParser.createParser(factory, new NodeModelBuilder<Object>() {

            {
                child().element("foo").attach(foo);
                descendant().element("bar").attach(bar);
            }
        }.build());
        parser.parse(new StringReader("<xml><foo>YES</foo><x><bar>YES2</bar></x></xml>"), null);
        assertEquals("YES", foo.contents);
        assertEquals("YES2", bar.contents);
    }

    @Test
    public void testDescendantMasking() throws Exception {
        final TestCHandler foo = new TestCHandler();
        final TestCHandler bar = new TestCHandler();
        SNAXParser<?> parser = SNAXParser.createParser(factory, new NodeModelBuilder<Object>() {

            {
                element("xml").descendant("bar").attach(bar);
                descendant("bar").attach(foo);
            }
        }.build());
        parser.parse(new StringReader("<xml><bar>YES2</bar></xml>"), null);
        assertEquals("", foo.contents);
        assertEquals("YES2", bar.contents);
    }

    @Test
    public void testDescendantNodeOrdering() throws Exception {
        final TestMultiHandler handler = new TestMultiHandler();
        SNAXParser<?> parser = SNAXParser.createParser(factory, new NodeModelBuilder<Object>() {

            {
                descendant().attach(handler);
            }
        }.build());
        parser.parse(new StringReader("<a><b><c/></b><d><e><f/></e><g/></d></a>"), null);
        assertEquals(7, handler.elementNames.size());
        assertEquals("a", handler.elementNames.get(0));
        assertEquals("b", handler.elementNames.get(1));
        assertEquals("c", handler.elementNames.get(2));
        assertEquals("d", handler.elementNames.get(3));
        assertEquals("e", handler.elementNames.get(4));
        assertEquals("f", handler.elementNames.get(5));
        assertEquals("g", handler.elementNames.get(6));
    }

    @Test
    public void testNamedDescendant() throws Exception {
        final TestMultiHandler handler = new TestMultiHandler();
        SNAXParser<?> parser = SNAXParser.createParser(factory, new NodeModelBuilder<Object>() {

            {
                descendant("x").attach(handler);
            }
        }.build());
        parser.parse(new StringReader("<foo><x>1</x><y><x/><z/></y><x><x/></x></foo>"), null);
        assertEquals(4, handler.elementNames.size());
        for (String s : handler.elementNames) {
            assertEquals("x", s);
        }
    }

    @Test
    public void testExplicitSelectorPriority() throws Exception {
        final TestHandler foo = new TestHandler();
        final TestHandler bar = new TestHandler();
        SNAXParser<?> parser = SNAXParser.createParser(factory, new NodeModelBuilder<Object>() {

            {
                elements("foo", "bar").attach(foo);
                descendant("bar").attach(bar);
            }
        }.build());
        parser.parse(new StringReader("<foo><bar/></foo>"), null);
        assertEquals("bar", foo.elementName);
        assertEquals("", bar.elementName);
    }

    @Test
    public void testDescendantFilter() throws Exception {
        final TestMultiHandler handler = new TestMultiHandler();
        SNAXParser<?> parser = SNAXParser.createParser(factory, new NodeModelBuilder<Object>() {

            {
                descendant(new ElementFilter() {

                    Pattern p = Pattern.compile("\\d");

                    @Override
                    public boolean test(StartElement element) {
                        return p.matcher(element.getName().getLocalPart()).find();
                    }
                }).attach(handler);
            }
        }.build());
        parser.parse(new StringReader("<foo1><bar/><b2ar/><ba3r/><bar/></foo1>"), null);
        assertEquals(3, handler.elementNames.size());
        assertEquals("foo1", handler.elementNames.get(0));
        assertEquals("b2ar", handler.elementNames.get(1));
        assertEquals("ba3r", handler.elementNames.get(2));
    }
}
