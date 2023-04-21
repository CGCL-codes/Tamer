package test.concordion.api;

import org.concordion.api.Element;
import junit.framework.TestCase;

public class ElementTest extends TestCase {

    public void testCanMoveChildrenToAnotherElement() throws Exception {
        Element fred = new Element("fred");
        fred.appendChild(new Element("child1"));
        Element child2 = new Element("child2");
        fred.appendChild(child2);
        child2.appendChild(new Element("grandchild"));
        assertEquals("<fred><child1 /><child2><grandchild /></child2></fred>", fred.toXML());
        Element barney = new Element("barney");
        fred.moveChildrenTo(barney);
        assertEquals("<fred />", fred.toXML());
        assertEquals("<barney><child1 /><child2><grandchild /></child2></barney>", barney.toXML());
    }
}
