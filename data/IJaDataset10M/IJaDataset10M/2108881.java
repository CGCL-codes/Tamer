package com.nhncorp.usf.core.util;

import java.util.List;
import org.dom4j.dom.DOMElement;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;

/**
 * @author Web Platform Development Team
 */
public class DomUtilTest {

    Node rootNode;

    @Before
    public void init() {
        rootNode = new DOMElement("root");
        Node child1 = new DOMElement("child1");
        Node child2 = new DOMElement("child2");
        Node child3first = new DOMElement("child3");
        Node child3second = new DOMElement("child3");
        rootNode.appendChild(child1);
        rootNode.appendChild(child2);
        rootNode.appendChild(child3first);
        rootNode.appendChild(child3second);
    }

    @Test
    public void getChildByNameTest() {
        Node testChild = DomUtil.getChildByName(rootNode, "child1");
        assertEquals("child1", testChild.getNodeName());
        Node testChild2 = DomUtil.getChildByName(rootNode, "child2");
        assertEquals("child2", testChild2.getNodeName());
        Node testChild20 = DomUtil.getChildByName(rootNode, "child20");
        assertEquals(null, testChild20);
        List<Node> children = DomUtil.getChildListByName(rootNode, "child3");
        assertEquals(2, children.size());
        assertEquals("child3", children.get(0).getNodeName());
        assertEquals("child3", children.get(1).getNodeName());
    }
}
