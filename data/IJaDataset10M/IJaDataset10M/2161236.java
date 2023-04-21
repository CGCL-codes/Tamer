package org.dom4j.xpath;

import junit.textui.TestRunner;
import java.util.List;
import org.dom4j.AbstractTestCase;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;

/**
 * Test harness for the valueOf() function
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision: 1.3 $
 */
public class ValueOfTest extends AbstractTestCase {

    protected static String[] paths = { "/root", "//author", "//author/@name", "/root/author[1]", "/root/author[1]/@name", "/root/author[2]", "/root/author[2]/@name", "/root/author[3]", "/root/author[3]/@name", "name()", "name(.)", "name(..)", "name(child::node())", "name(parent::*)", "name(../*)", "name(../child::node())", "local-name()", "local-name(..)", "local-name(parent::*)", "local-name(../*)", "parent::*", "name(/.)", "name(/child::node())", "name(/*)", ".", "..", "../*", "../child::node()", "/.", "/*", "*", "/child::node()" };

    public static void main(String[] args) {
        TestRunner.run(ValueOfTest.class);
    }

    public void testXPaths() throws Exception {
        Element root = document.getRootElement();
        List children = root.elements("author");
        Element child1 = (Element) children.get(0);
        testXPath(document);
        testXPath(root);
        testXPath(child1);
    }

    protected void testXPath(Node node) throws Exception {
        log("Testing XPath on: " + node);
        log("===============================");
        int size = paths.length;
        for (int i = 0; i < size; i++) {
            testXPath(node, paths[i]);
        }
    }

    protected void testXPath(Node node, String xpathExpr) throws Exception {
        try {
            XPath xpath = node.createXPath(xpathExpr);
            String value = xpath.valueOf(node);
            log("valueOf: " + xpathExpr + " is: " + value);
        } catch (Throwable e) {
            e.printStackTrace();
            assertTrue("Failed with exception: " + e, false);
        }
    }
}
