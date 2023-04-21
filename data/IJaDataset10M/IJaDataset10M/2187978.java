package ca.ucalgary.jazzconnect.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ca.ucalgary.jazzconnect.internal.IJazzConnectionInternal;
import ca.ucalgary.jazzconnect.internal.JazzUtilities;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.common.model.AttributeTypes;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.IWorkItem;

public class JazzUtilitiesAttributeTests {

    static IJazzConnectionInternal connection;

    static Random rand;

    static JazzUtilities util;

    static JazzConnectionMock mockConnection;

    static JazzUtilities mockUtil;

    IWorkItem workItem;

    List<IAttribute> attList;

    static final String attIdent = "APObjectID3";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        TestSetup setup = new TestSetup();
        connection = setup.getJazzConnection();
        rand = setup.getRandomGenerator();
        util = new JazzUtilities(connection, null);
        assert util != null : "No instance of util to test...";
        mockConnection = new JazzConnectionMock();
        mockUtil = new JazzUtilities(mockConnection, null);
        setup.turnOffLogging(util);
        setup.turnOffLogging(mockUtil);
        util.fetchProjectArea(setup.getProjectAreaName());
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        connection.logout();
    }

    private void setupAttributes() {
        util.createAttribute("APObjectID", AttributeTypes.LARGE_STRING.toString(), "APObjectID");
        util.createAttribute("APObjectID2", AttributeTypes.LARGE_STRING.toString(), "APObjectID2");
        util.createAttribute("APObjectID3", AttributeTypes.INTEGER.toString(), "APObjectID3");
        try {
            workItem = util.createWorkItem();
        } catch (TeamRepositoryException e) {
            e.printStackTrace();
        }
        attList = new LinkedList<IAttribute>();
        try {
            attList.add(util.getAttribute(attIdent));
        } catch (TeamRepositoryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getExistingAttribute() {
        try {
            IAttribute att = util.getAttribute("target");
            assertTrue("got wrong attribute", att.getIdentifier().equalsIgnoreCase("target"));
        } catch (TeamRepositoryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getNonExistingAttribute() {
        try {
            IAttribute att = util.getAttribute("" + rand.nextInt());
            assertNull("Attribute shouldn't exist", att);
        } catch (TeamRepositoryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void createExistingAttribute() {
        assertTrue("should return true since attribute exists on server", util.createAttribute("target", "insignificant", "unimportant"));
    }

    @Test
    public void createNewAttribute() {
        String id = "TestTest" + rand.nextInt();
        String type = AttributeTypes.INTEGER.toString();
        String disText = "Test " + rand.nextInt();
        assertTrue("attribute should be created --> attribute exists now --> return true", util.createAttribute(id, type, disText));
        try {
            IAttribute att = util.getAttribute(id);
            assertEquals("Identifier doesn't match", id, att.getIdentifier());
            assertEquals("Display-text doesn't match", disText, att.getDisplayName());
        } catch (TeamRepositoryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void createAttributeExceptionHandling() {
        assertFalse("due to the exception, method should return false", mockUtil.createAttribute("blub", "target", "blub"));
    }

    @Test
    public void addOneAttribute() {
        setupAttributes();
        try {
            IWorkItem workItem = util.createWorkItem();
            IAttribute att;
            List<IAttribute> attList = new LinkedList<IAttribute>();
            att = util.getAttribute("APObjectID3");
            attList.add(att);
            util.addAttributes(workItem, attList);
            assertTrue("Attribute not saved in Work Item", workItem.hasCustomAttribute(att));
        } catch (TeamRepositoryException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addAttributeList() {
        setupAttributes();
        try {
            IWorkItem workItem = util.createWorkItem();
            List<IAttribute> attList = new LinkedList<IAttribute>();
            attList.add(util.getAttribute("APObjectID"));
            attList.add(util.getAttribute("APObjectID2"));
            attList.add(util.getAttribute("APObjectID3"));
            util.addAttributes(workItem, attList);
            assertTrue("Attribute 1 not saved", workItem.hasCustomAttribute(attList.get(0)));
            assertTrue("Attribute 2 not saved", workItem.hasCustomAttribute(attList.get(1)));
            assertTrue("Attribute 3 not saved", workItem.hasCustomAttribute(attList.get(2)));
        } catch (TeamRepositoryException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setAttributeValue() {
        setupAttributes();
        util.addAttributes(workItem, attList);
        try {
            assertTrue("Can't set Attribut-Value", util.setAttributeValue(workItem, attIdent, new Integer(12)));
        } catch (TeamRepositoryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setWrongAttributeValueType() {
        setupAttributes();
        util.addAttributes(workItem, attList);
        try {
            assertFalse("Attribute of wrong type has been set successfully", util.setAttributeValue(workItem, attIdent, new String("blub")));
        } catch (TeamRepositoryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setValueOfNotExistingAttribut() {
        setupAttributes();
        util.addAttributes(workItem, attList);
        try {
            assertFalse("Was able to set an non-existing attribute!?!", util.setAttributeValue(workItem, "" + rand.nextInt(), new String("blub")));
        } catch (TeamRepositoryException e) {
        }
    }

    @Test
    public void attributeIsNotPartOfWorkItem() {
        setupAttributes();
        try {
            assertFalse("Was able to set an attribute that is not part of the WorkItem", util.setAttributeValue(workItem, attIdent, new Integer(15)));
        } catch (TeamRepositoryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getAttributeValue() {
        setupAttributes();
        util.addAttributes(workItem, attList);
        int testValue = 12;
        try {
            assertTrue("getAttribute-precondition not met", util.setAttributeValue(workItem, attIdent, new Integer(testValue)));
            assertNotNull("Previously set attribute was not set correctly", util.getAttributeValue(workItem, attIdent));
            assertEquals("Previously set attribute-value was not set correctly", testValue, ((Integer) util.getAttributeValue(workItem, attIdent)).intValue());
        } catch (TeamRepositoryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getNonExistingAttributeValue() {
        setupAttributes();
        try {
            assertNull("Got attribute that shouldn't exist", util.getAttributeValue(workItem, "" + rand.nextInt()));
        } catch (TeamRepositoryException e) {
        }
    }

    @Test
    public void testJazzClassTypes() {
        assertEquals("Got wrong class: Integer", Integer.class, util.getClassType(AttributeTypes.INTEGER));
        assertEquals("Got wrong class: Long", Long.class, util.getClassType(AttributeTypes.LONG));
        assertEquals("Got wrong class: Float", Float.class, util.getClassType(AttributeTypes.FLOAT));
        assertEquals("Got wrong class: Boolean", Boolean.class, util.getClassType(AttributeTypes.BOOLEAN));
        assertEquals("Got wrong class: String (Large)", String.class, util.getClassType(AttributeTypes.LARGE_STRING));
        assertEquals("Got wrong class: String (Small)", String.class, util.getClassType(AttributeTypes.SMALL_STRING));
        assertNull("Got non-existing class", util.getClassType("nullTest"));
        assertNull("Got class from null", util.getClassType(null));
    }

    @Test
    public void getJavaTypeClass() {
        assertEquals("Got wrong Java-Class", Integer.class.toString(), util.getClassType(new Integer(12)));
    }
}
