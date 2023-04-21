package com.foursoft.fourever.objectmodel;

import junit.framework.TestCase;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.foursoft.fourever.objectmodel.exception.IDInUseException;

/**
 *
 */
public class IDTypeTest extends TestCase {

    private ClassPathXmlApplicationContext ctx = null;

    private ObjectModel om = null;

    private ObjectModelManager omm = null;

    private IDType it = null;

    /**
	 * @param arg0
	 */
    public IDTypeTest(String arg0) {
        super(arg0);
    }

    /**
	 * @see junit.framework.TestCase#setUp()
	 */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ctx = new ClassPathXmlApplicationContext("objectmodel/beans.xml");
        omm = (ObjectModelManager) ctx.getBean("objectmodelmanager");
        om = omm.createObjectModel();
        it = (IDType) om.getTypeByName(IDType.TYPE_NAME);
    }

    /**
	 * @see junit.framework.TestCase#tearDown()
	 */
    @Override
    protected void tearDown() throws Exception {
        ctx.close();
        super.tearDown();
    }

    /**
	 * test createInstance
	 */
    @SuppressWarnings("null")
    public void testCreateInstance() {
        Instance idi = it.createInstance(false);
        assertNotNull(idi);
        assertTrue(idi instanceof IDInstance);
        Instance idiWithId = null;
        String dummyId = "aDummyIdString123";
        try {
            idiWithId = it.createInstance(dummyId);
        } catch (IDInUseException ex) {
            fail("Id in Use Exception Occurred");
        }
        assertNotNull(idiWithId);
        assertTrue(idiWithId instanceof IDInstance);
        assertTrue(((IDInstance) idiWithId).getValue() != null);
        assertTrue(((IDInstance) idiWithId).getValue().equals(dummyId));
    }
}
