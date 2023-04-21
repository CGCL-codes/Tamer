package org.htmlparser.tests.visitorsTests;

import java.util.HashMap;
import java.util.Map;
import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.tests.ParserTestCase;
import org.htmlparser.visitors.NodeVisitor;

public class NodeVisitorTest extends ParserTestCase {

    static {
        System.setProperty("org.htmlparser.tests.visitorsTests.NodeVisitorTest", "NodeVisitorTest");
    }

    public NodeVisitorTest(String name) {
        super(name);
    }

    public void testVisitTag() throws Exception {
        ParameterVisitor visitor = new ParameterVisitor();
        createParser("<input>" + "<param name='key1'>value1</param>" + "<param name='key2'>value2</param>" + "</input>");
        parser.visitAllNodesWith(visitor);
        assertEquals("value of key1", "value1", visitor.getValue("key1"));
        assertEquals("value of key2", "value2", visitor.getValue("key2"));
    }

    class ParameterVisitor extends NodeVisitor {

        Map paramsMap = new HashMap();

        String lastKeyVisited;

        public String getValue(String key) {
            return (String) paramsMap.get(key);
        }

        public void visitStringNode(Text stringNode) {
            paramsMap.put(lastKeyVisited, stringNode.getText());
        }

        public void visitTag(Tag tag) {
            if (tag.getTagName().equals("PARAM")) {
                lastKeyVisited = tag.getAttribute("NAME");
            }
        }
    }
}
