package com.volantis.mcs.eclipse.ab.actions.layout;

import java.util.Arrays;
import org.jdom.Document;
import org.jdom.Element;

/**
 * testcase for DeleteActionCommand limited to jdom sorting
 * (sensitive to jdom version)
 */
public class DeleteActionTestCase extends LayoutActionCommandTestAbstract {

    private Element rootElement;

    private Element child1;

    private Element child2;

    private Element child11;

    protected void setUp() throws Exception {
        super.setUp();
        rootElement = new Element("root");
        Document jdomDoc = new Document(rootElement);
        child1 = new Element("child1");
        rootElement.addContent(child1);
        child2 = new Element("child2");
        rootElement.addContent(child2);
        child11 = new Element("child11");
        child1.addContent(child11);
    }

    /**
     * this may fail if we upgrade JDOM when the B9 behavior will be fixed
     */
    public void testJDOMisAncestorDoesTheOppositeOfWhatItSays() throws Exception {
        assertTrue("expected behavior of JDOM b9 Element.isAncestor()", child1.isAncestor(rootElement));
        assertFalse("expected behavior of JDOM b9 Element.isAncestor()", rootElement.isAncestor(child1));
    }

    /**
     * test for {@link DeleteActionCommand#sortChildrenFirst(org.jdom.Element[])}
     */
    public void testJDOMSortChildrenfirstTestCase() throws Exception {
        Element[] inputElements = { rootElement, child11, child2, child1 };
        Element[] expectedSortedElements = { child11, child2, child1, rootElement };
        DeleteActionCommand.sortChildrenFirst(inputElements);
        assertTrue("sorted arrays", Arrays.equals(expectedSortedElements, inputElements));
    }
}
