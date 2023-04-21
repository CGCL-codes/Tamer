package com.volantis.synergetics.domvisitor;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.Text;
import org.jdom.input.DefaultJDOMFactory;
import org.jdom.input.JDOMFactory;
import org.jdom.input.SAXBuilder;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * Tests the {@link Walker} class, including handing of the inclusion mask and
 * visitor actions.
 */
public class WalkerTestCase extends TestCaseAbstract {

    private Document document;

    private Walker walker;

    private int numberOfElements;

    private int actualElementCount;

    private int numberOfAttributes;

    private int actualAttributeCount;

    private int numberOfTexts;

    private int actualTextCount;

    private int numberOfDesks;

    private int numberOfDeskAttributes;

    private int numberOfAssets;

    private boolean seenGuildford;

    private int numberOfElementsBeforeGuildford;

    private int numberOfAttributesBeforeGuildford;

    private int numberOfElementsBeforeCDATA;

    private Visitor.Action specialAction;

    /**
     * This factory tracks the number of attributes, elements and text nodes
     * created. It also records the number of elements created before the
     * first CDATA is created.
     */
    private JDOMFactory factory = new DefaultJDOMFactory() {

        public Attribute attribute(String s, String s1) {
            numberOfAttributes++;
            return super.attribute(s, s1);
        }

        public Attribute attribute(String s, String s1, int i) {
            numberOfAttributes++;
            return super.attribute(s, s1, i);
        }

        public Attribute attribute(String s, String s1, int i, Namespace namespace) {
            numberOfAttributes++;
            return super.attribute(s, s1, i, namespace);
        }

        public Attribute attribute(String s, String s1, Namespace namespace) {
            numberOfAttributes++;
            return super.attribute(s, s1, namespace);
        }

        public Element element(String s) {
            numberOfElements++;
            return super.element(s);
        }

        public Element element(String s, String s1) {
            numberOfElements++;
            return super.element(s, s1);
        }

        public Element element(String s, String s1, String s2) {
            numberOfElements++;
            return super.element(s, s1, s2);
        }

        public Element element(String s, Namespace namespace) {
            numberOfElements++;
            return super.element(s, namespace);
        }

        public Text text(String s) {
            numberOfTexts++;
            return super.text(s);
        }

        public CDATA cdata(String s) {
            if (numberOfElementsBeforeCDATA == 0) {
                numberOfElementsBeforeCDATA = numberOfElements;
            }
            numberOfTexts++;
            return super.cdata(s);
        }
    };

    /**
     * This is the base visitor to be extended by the various tests as needed.
     *
     * <p>Note that this visitor explicitly does not extend
     * {@link SimpleVisitor} since any nextHeader methods for non-Element node
     * types must always be implemented to perform a fail. Specific nextHeader
     * methods should be overridden as needed by individual tests.</p>
     */
    private class BaseVisitTrackingVisitor implements Visitor {

        public Visitor.Action visit(Element element) {
            actualElementCount++;
            return Visitor.Action.CONTINUE;
        }

        public Visitor.Action visit(Attribute attribute) {
            fail("An attribute has been visited (" + attribute.getName() + ")");
            return null;
        }

        public Visitor.Action visit(Text text) {
            fail("A text node has been visited (" + text.getTextTrim() + ")");
            return null;
        }
    }

    protected void setUp() throws Exception {
        super.setUp();
        SAXBuilder builder = new SAXBuilder();
        builder.setFactory(factory);
        XMLFilter filter = new XMLFilterImpl() {

            public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException {
                if ("desk".equals(localName)) {
                    numberOfDesks++;
                    numberOfDeskAttributes += attributes.getLength();
                } else if ("asset".equals(localName)) {
                    numberOfAssets++;
                }
                if (!seenGuildford && "city".equals(localName) && "guildford".equals(attributes.getValue("name"))) {
                    seenGuildford = true;
                    numberOfElementsBeforeGuildford = numberOfElements;
                    numberOfAttributesBeforeGuildford = numberOfAttributes;
                }
                super.startElement(namespaceURI, localName, qName, attributes);
            }
        };
        builder.setXMLFilter(filter);
        builder.setIgnoringElementContentWhitespace(true);
        walker = new Walker();
        numberOfAssets = 0;
        numberOfDesks = 0;
        numberOfDeskAttributes = 0;
        numberOfElements = 0;
        numberOfAttributes = 0;
        numberOfElementsBeforeGuildford = 0;
        numberOfAttributesBeforeGuildford = 0;
        seenGuildford = false;
        numberOfElementsBeforeCDATA = 0;
        actualElementCount = 0;
        actualAttributeCount = 0;
        actualTextCount = 0;
        specialAction = null;
        document = builder.build(getClass().getResourceAsStream("WalkerTestCase.xml"));
    }

    public void test_FIX_THIS_TESTCASE() {
    }

    /**
     * Verifies that a full visitation can be performed that does not include
     * any non-Element node types and visits all elements.
     */
    public void testVisitFullNoMask() throws Exception {
        Visitor visitor = new BaseVisitTrackingVisitor();
        walker.visit(document.getRootElement(), visitor);
        assertEquals("Number of elements not as", numberOfElements, actualElementCount);
    }

    /**
     * Verifies that a full visitation can be performed that only includes
     * elements and attributes.
     */
    public void testVisitFullWithAttributes() throws Exception {
        Visitor visitor = new BaseVisitTrackingVisitor() {

            public Visitor.Action visit(Attribute attribute) {
                actualAttributeCount++;
                return Visitor.Action.CONTINUE;
            }
        };
        walker.visit(document.getRootElement(), visitor, Walker.ATTRIBUTES);
        assertEquals("Number of attributes not as", numberOfAttributes, actualAttributeCount);
    }

    /**
     * Verifies that a full visitation can be performed that only includes
     * elements and text nodes (both normal and CDATA).
     */
    public void testVisitFullWithText() throws Exception {
        Visitor visitor = new BaseVisitTrackingVisitor() {

            public Visitor.Action visit(Text text) {
                actualTextCount++;
                return Visitor.Action.CONTINUE;
            }
        };
        walker.visit(document.getRootElement(), visitor, Walker.TEXT);
        assertEquals("Number of text nodes not as", numberOfTexts, actualTextCount);
    }

    /**
     * Verifies that a full visitation can be performed that includes
     * elements, attributes and text nodes (both normal and CDATA).
     */
    public void testVisitFullWithAttributesAndText() throws Exception {
        Visitor visitor = new BaseVisitTrackingVisitor() {

            public Visitor.Action visit(Attribute attribute) {
                actualAttributeCount++;
                return Visitor.Action.CONTINUE;
            }

            public Visitor.Action visit(Text text) {
                actualTextCount++;
                return Visitor.Action.CONTINUE;
            }
        };
        walker.visit(document.getRootElement(), visitor, Walker.TEXT | Walker.ATTRIBUTES);
        assertEquals("Number of attributes not as", numberOfAttributes, actualAttributeCount);
        assertEquals("Number of text nodes not as", numberOfTexts, actualTextCount);
    }

    /**
     * Verifies that a partial visitation can be performed that only includes
     * elements, skipping the attributes and content of a specific element.
     */
    public void testVisitSkipDesksElement() throws Exception {
        Visitor visitor = new BaseVisitTrackingVisitor() {

            public Visitor.Action visit(Element element) {
                Visitor.Action action = Visitor.Action.CONTINUE;
                actualElementCount++;
                if (element.getName().equals("desks")) {
                    action = specialAction = Visitor.Action.SKIP;
                }
                return action;
            }
        };
        walker.visit(document.getRootElement(), visitor);
        assertEquals("Number of elements visited not as", numberOfElements - numberOfDesks, actualElementCount);
        assertSame("Special action not as expected", Visitor.Action.SKIP, specialAction);
    }

    /**
     * Verifies that a partial visitation can be performed that only includes
     * elements and attributes, skipping all the attributes of a specific set
     * of elements.
     */
    public void testVisitSkipDeskAttributes() throws Exception {
        Visitor visitor = new BaseVisitTrackingVisitor() {

            public Visitor.Action visit(Attribute attribute) {
                Visitor.Action action = Visitor.Action.CONTINUE;
                if ("desk".equals(attribute.getParent().getName())) {
                    action = specialAction = Visitor.Action.SKIP;
                } else {
                    actualAttributeCount++;
                }
                return action;
            }
        };
        walker.visit(document.getRootElement(), visitor, Walker.ATTRIBUTES);
        assertEquals("Number of elements visited not as", numberOfElements, actualElementCount);
        assertEquals("Number of attributes visited not as", numberOfAttributes - numberOfDeskAttributes, actualAttributeCount);
        assertSame("Special action not as expected", Visitor.Action.SKIP, specialAction);
    }

    /**
     * Verifies that a partial visitation can be performed that only includes
     * elements and text, skipping the subsequent siblings of the text node's
     * container element (the siblings are assumed to all be "asset" elements).
     */
    public void testVisitSkipText() throws Exception {
        Visitor visitor = new BaseVisitTrackingVisitor() {

            public Visitor.Action visit(Text text) {
                Visitor.Action action = Visitor.Action.CONTINUE;
                if (text instanceof CDATA) {
                    action = specialAction = Visitor.Action.SKIP;
                }
                return action;
            }
        };
        walker.visit(document.getRootElement(), visitor, Walker.TEXT);
        assertEquals("Number of elements visited not as", numberOfElements - numberOfAssets, actualElementCount);
        assertSame("Special action not as expected", Visitor.Action.SKIP, specialAction);
    }

    /**
     * Verifies that a partial visitation can be performed that only includes
     * elements, stopping when a specific element is found.
     */
    public void testVisitStopElement() throws Exception {
        Visitor visitor = new BaseVisitTrackingVisitor() {

            public Visitor.Action visit(Element element) {
                Visitor.Action action = Visitor.Action.CONTINUE;
                if ("city".equals(element.getName()) && "guildford".equals(element.getAttributeValue("name"))) {
                    action = specialAction = Visitor.Action.STOP;
                } else {
                    actualElementCount++;
                }
                return action;
            }
        };
        walker.visit(document.getRootElement(), visitor);
        assertEquals("Number of elements visited not as", numberOfElementsBeforeGuildford, actualElementCount);
        assertSame("Special action not as expected", Visitor.Action.STOP, specialAction);
    }

    /**
     * Verifies that a partial visitation can be performed that only includes
     * elements and attributes, stopping when a specific attribute is found.
     */
    public void testVisitStopAttribute() throws Exception {
        Visitor visitor = new BaseVisitTrackingVisitor() {

            public Visitor.Action visit(Attribute attribute) {
                Visitor.Action action = Visitor.Action.CONTINUE;
                if ("name".equals(attribute.getName()) && "guildford".equals(attribute.getValue()) && "city".equals(attribute.getParent().getName())) {
                    action = specialAction = Visitor.Action.STOP;
                } else {
                    actualAttributeCount++;
                }
                return action;
            }
        };
        walker.visit(document.getRootElement(), visitor, Walker.ATTRIBUTES);
        assertEquals("Number of attributes visited not as", numberOfAttributesBeforeGuildford, actualAttributeCount);
        assertSame("Special action not as expected", Visitor.Action.STOP, specialAction);
    }

    /**
     * Verifies that a partial visitation can be performed that only includes
     * elements and text nodes, stopping when a CDATA text node is found.
     */
    public void testVisitStopText() throws Exception {
        Visitor visitor = new BaseVisitTrackingVisitor() {

            public Visitor.Action visit(Text text) {
                Visitor.Action action = Visitor.Action.CONTINUE;
                if (text instanceof CDATA) {
                    action = specialAction = Visitor.Action.STOP;
                }
                return action;
            }
        };
        walker.visit(document.getRootElement(), visitor, Walker.TEXT);
        assertEquals("Number of elements visited not as", numberOfElementsBeforeCDATA, actualElementCount);
        assertSame("Special action not as expected", Visitor.Action.STOP, specialAction);
    }
}
