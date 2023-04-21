package test.antsnest.datastructure;

import junit.framework.*;
import antsnest.datastructure.*;
import antsnest.antinfo.*;

/**
 * Tests the AntElement framework
 * @author Chris Clohosy
 */
public class AntElementTest extends TestCase {

    /**
	 * An element
	 */
    private AntElement element;

    /**
	 * An element definition
	 */
    private Definition definition;

    /**
	 * The name of the element
	 */
    private String elementName;

    /**
	 * The ID of the element
	 */
    private String elementID;

    /**
	 * The taskname of the element
	 */
    private String taskname;

    /**
	 * Constructs a new test
	 * @param name a way of identifying the test
	 */
    public AntElementTest(String name) {
        super(name);
    }

    /**
	 * Sets up the test
	 */
    public void setUp() {
        elementName = "TestElement";
        elementID = "2";
        taskname = "tester";
        DefaultTaskDefinition def = new DefaultTaskDefinition("anyCategory", elementName);
        DefaultAttributeDefinition attDef1 = new DefaultAttributeDefinition(false, "attribute1");
        DefaultAttributeDefinition attDef2 = new DefaultAttributeDefinition(false, "attribute2");
        def.addAttribute(attDef1);
        def.addAttribute(attDef2);
        definition = def;
        element = new DefaultAntElement(definition);
        AntElement type1 = new DefaultAntElement();
        type1.setName("type1");
        AntElement type2 = new DefaultAntElement();
        type2.setName("type2");
        element.addNode(type1, 0);
        element.addNode(type2, 1);
        element.setID(elementID);
        element.setTaskname(taskname);
    }

    /**
	 * Tests whether the name was set correctly
	 */
    public void testNameSet() {
        String storedName = element.getName();
        assertTrue("The name was not set correctly, " + storedName + " != " + elementName, storedName.equals(elementName));
    }

    /**
	 * Tests the attribute is actually a node of type 'ELEMENT'
	 */
    public void testType() {
        assertTrue("The element is a node of type " + element.getType(), element.getType() == Constants.ELEMENT);
    }

    /**
	 * Tests whether the ID was set correctly
	 */
    public void testIDSet() {
        String storedID = element.getID();
        assertTrue("The ID was not set correctly, " + storedID + " != " + elementID, storedID.equals(elementID));
    }

    /**
	 * Tests whether the taskname was set correctly
	 */
    public void testTasknameSet() {
        String storedTaskName = element.getTaskname();
        assertTrue("The taskname was not set correctly, " + storedTaskName + " != " + taskname, storedTaskName.equals(taskname));
    }

    /**
	 * Tests whether the attributes were positioned correctly
	 */
    public void testAttributePositions1() {
        AntAttribute position0 = element.getAttribute(0);
        AntAttribute position1 = element.getAttribute(1);
        boolean placedRight = (position0.getName().equals("description")) && (position1.getName().equals("id"));
        assertTrue("The attributes were not placed in the correct order.", placedRight);
    }

    /**
	 * Tests whether the element swaps the attributes correctly
	 */
    public void testAttributePositions2() {
        AntAttribute position0 = element.getAttribute(0);
        AntAttribute position1 = element.getAttribute(1);
        element.removeAttribute(position1);
        element.addAttribute(position1, 0);
        boolean placedRight = (element.getAttribute(0).equals(position1)) && (element.getAttribute(1).equals(position0));
        assertTrue("The attributes were not placed in the correct order.", placedRight);
    }

    /**
	 * Tests whether the nested elements were positioned correctly
	 */
    public void testNestedElementPositions1() {
        AntNode position0 = element.getNode(0);
        AntNode position1 = element.getNode(1);
        boolean placedRight = (position0.getName().equals("type1")) && (position1.getName().equals("type2"));
        assertTrue("The nested elements were not placed in the correct positions.", placedRight);
    }

    /**
	 * Tests whether the element swaps the nested elements correctly
	 */
    public void testNestedElementPositions2() {
        AntNode position0 = element.getNode(0);
        AntNode position1 = element.getNode(1);
        element.removeNode(position1);
        element.addNode(position1, 0);
        boolean placedRight = (element.getNode(0).equals(position1)) && (element.getNode(1).equals(position0)) && (element.getNode(2) == null);
        assertTrue("The nested elements were not placed in the correct positions.", placedRight);
    }
}
