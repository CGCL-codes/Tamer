package com.foursoft.fourever.objectmodel;

import java.util.HashSet;
import java.util.Set;
import junit.framework.TestCase;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.foursoft.fourever.objectmodel.exception.TargetIndexOutOfBoundsException;
import com.foursoft.fourever.objectmodel.exception.TypeMismatchException;

/**
 * Test case for observer interface
 * 
 */
public class InstanceObserverTest extends TestCase {

    private ClassPathXmlApplicationContext ctx = null;

    private ObjectModelManager omm;

    private ObjectModel om;

    private EntityType hund;

    private EntityInstance einhund;

    private CompositeBinding name;

    private StringType st;

    private StringInstance defaultname;

    private EntityType mensch;

    private EntityInstance jan;

    private ReferenceBinding herrchen;

    private DummyObserver do1;

    private DummyObserver do2;

    private DummyObserver do3;

    private DummyObserver do4;

    private DummyObserver do5;

    private DummyObserver do6;

    private DummyObserver do7;

    private DummyObserver do8;

    /**
	 * @see junit.framework.TestCase#setUp()
	 */
    @Override
    protected void setUp() throws Exception {
        ctx = new ClassPathXmlApplicationContext("objectmodel/beans.xml");
        omm = (ObjectModelManager) ctx.getBean("objectmodelmanager");
        om = omm.createObjectModel();
        try {
            hund = om.createEntityType("Hund", "Vierbeiniges anhaengliches Saeugetier");
            st = (StringType) om.getTypeByName(StringType.TYPE_NAME);
            defaultname = (StringInstance) st.createInstance(false);
            defaultname.setValue("Hund");
            name = om.createSimpleCompositeBinding("name", true, hund, "test", defaultname, st);
            mensch = om.createEntityType("Mensch", "Bester Freund des Hundes");
            Set<CompositeBinding> targets = new HashSet<CompositeBinding>();
            targets.add(om.createCompositeBinding("menschBind", 0, 20, null, "test", null, mensch));
            herrchen = om.createReferenceBinding("herrchen", 0, 1, hund, "test", targets, om);
            einhund = (EntityInstance) hund.createInstance(true);
            ((StringInstance) einhund.getOutgoingLink(name).getTarget(0)).setValue("Bello");
            jan = (EntityInstance) mensch.createInstance(false);
        } catch (Exception e) {
            fail();
        }
        registerListeners();
    }

    void registerListeners() {
        do1 = new DummyObserver();
        do2 = new DummyObserver();
        do3 = new DummyObserver();
        do4 = new DummyObserver();
        do5 = new DummyObserver();
        do6 = new DummyObserver();
        do7 = new DummyObserver();
        do8 = new DummyObserver();
        om.register(do1, InstanceObserver.INSTANCE_CREATED);
        om.register(do2, InstanceObserver.INSTANCE_REMOVED);
        om.register(do3, InstanceObserver.VALUE_CHANGED);
        om.register(do4, InstanceObserver.TARGET_LINKED);
        om.register(do5, InstanceObserver.TARGET_UNLINKED);
        om.register(do6, InstanceObserver.SOURCE_LINKED);
        om.register(do7, InstanceObserver.SOURCE_UNLINKED);
        om.register(do8, InstanceObserver.ALL_ASPECTS);
    }

    /**
	 * @see junit.framework.TestCase#tearDown()
	 */
    @Override
    protected void tearDown() throws Exception {
        einhund.remove();
        defaultname.remove();
        omm.removeObjectModel(om);
        ctx.close();
        super.tearDown();
    }

    /**
     *
     */
    public void testInstanceCreated() {
        EntityInstance hugo = (EntityInstance) mensch.createInstance(false);
        assertTrue(do1.wasNotified);
        assertFalse(do2.wasNotified);
        assertFalse(do3.wasNotified);
        assertFalse(do4.wasNotified);
        assertFalse(do5.wasNotified);
        assertFalse(do6.wasNotified);
        assertFalse(do7.wasNotified);
        assertTrue(do8.wasNotified);
        assertEquals(InstanceObserver.INSTANCE_CREATED, do1.aspect);
        assertEquals(InstanceObserver.INSTANCE_CREATED, do8.aspect);
        assertEquals(-1, do1.lastIndexOfChange);
        assertEquals(-1, do8.lastIndexOfChange);
        assertEquals(hugo, do1.instance);
        assertEquals(hugo, do8.instance);
        assertNull(do1.link);
        assertNull(do8.link);
    }

    /**
	 * @throws TypeMismatchException
	 * 
	 */
    public void testInstanceRemoved() throws TypeMismatchException {
        einhund.remove();
        assertFalse(do1.wasNotified);
        assertTrue(do2.wasNotified);
        assertFalse(do3.wasNotified);
        assertFalse(do4.wasNotified);
        assertTrue(do5.wasNotified);
        assertFalse(do6.wasNotified);
        assertTrue(do7.wasNotified);
        assertTrue(do8.wasNotified);
        assertEquals(InstanceObserver.INSTANCE_REMOVED, do2.aspect);
        assertNull(do2.link);
        assertEquals(InstanceObserver.SOURCE_UNLINKED, do7.aspect);
        assertEquals(einhund.getOutgoingLink("name"), do7.link);
        assertEquals(einhund, do5.instance);
        assertEquals(0, do5.lastIndexOfChange);
        assertEquals("Bello", ((StringInstance) do7.instance).getValue());
    }

    /**
	 * @throws TargetIndexOutOfBoundsException
	 * @throws TypeMismatchException
	 * 
	 */
    public void testValueChanged() throws TargetIndexOutOfBoundsException, TypeMismatchException {
        ((StringInstance) einhund.getOutgoingLink("name").getTarget(0)).setValue("Waldi");
        assertFalse(do1.wasNotified);
        assertFalse(do2.wasNotified);
        assertTrue(do3.wasNotified);
        assertFalse(do4.wasNotified);
        assertFalse(do5.wasNotified);
        assertFalse(do6.wasNotified);
        assertFalse(do7.wasNotified);
        assertTrue(do8.wasNotified);
        assertEquals(InstanceObserver.VALUE_CHANGED, do3.aspect);
        assertEquals(einhund.getOutgoingLink("name").getTarget(0), do3.instance);
        assertEquals(-1, do3.lastIndexOfChange);
        assertNull(do3.link);
        assertEquals(-1, do8.lastIndexOfChange);
    }

    /**
	 * @throws TypeMismatchException
	 * 
	 */
    public void testLinked() throws TypeMismatchException {
        try {
            einhund.getOutgoingLink(herrchen).linkTarget(jan);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
        assertFalse(do1.wasNotified);
        assertFalse(do2.wasNotified);
        assertFalse(do3.wasNotified);
        assertTrue(do4.wasNotified);
        assertFalse(do5.wasNotified);
        assertTrue(do6.wasNotified);
        assertFalse(do7.wasNotified);
        assertTrue(do8.wasNotified);
        assertEquals(InstanceObserver.TARGET_LINKED, do4.aspect);
        assertEquals(einhund, do4.instance);
        assertEquals(einhund.getOutgoingLink(herrchen), do4.link);
        assertEquals(0, do4.lastIndexOfChange);
        assertEquals(InstanceObserver.SOURCE_LINKED, do6.aspect);
        assertEquals(jan, do6.instance);
        assertEquals(jan.getIncomingLinks(herrchen).next(), do6.link);
        assertEquals(-1, do6.lastIndexOfChange);
    }

    /**
	 * @throws TypeMismatchException
	 * 
	 */
    public void testUnlinked() throws TypeMismatchException {
        testLinked();
        registerListeners();
        assertNotNull(einhund.getOutgoingLink(herrchen));
        assertTrue(einhund.getOutgoingLink(herrchen).isTarget(jan));
        einhund.getOutgoingLink(herrchen).unLinkTarget(jan);
        assertFalse(einhund.getOutgoingLink(herrchen).isTarget(jan));
        assertFalse(do1.wasNotified);
        assertFalse(do2.wasNotified);
        assertFalse(do3.wasNotified);
        assertFalse(do4.wasNotified);
        assertTrue(do5.wasNotified);
        assertFalse(do6.wasNotified);
        assertTrue(do7.wasNotified);
        assertTrue(do8.wasNotified);
        assertEquals(InstanceObserver.TARGET_UNLINKED, do5.aspect);
        assertEquals(einhund, do5.instance);
        assertEquals(einhund.getOutgoingLink(herrchen), do5.link);
        assertEquals(0, do5.lastIndexOfChange);
        assertEquals(InstanceObserver.SOURCE_UNLINKED, do7.aspect);
        assertEquals(jan, do7.instance);
        assertEquals(einhund.getOutgoingLink(herrchen), do7.link);
        assertEquals(-1, do7.lastIndexOfChange);
    }

    /**
	 * Dummy implementation of the InstanceObserver interface
	 */
    public class DummyObserver implements InstanceObserver {

        /** observed instance */
        protected Instance instance;

        /** observed aspect */
        protected int aspect;

        /** changed link */
        protected Link link;

        /** notification flag */
        protected boolean wasNotified;

        /** last index of change */
        protected int lastIndexOfChange;

        /**
		 * Constructor
		 */
        public DummyObserver() {
            instance = null;
            aspect = -1;
            link = null;
            wasNotified = false;
        }

        /**
		 * @see com.foursoft.fourever.objectmodel.InstanceObserver#update(com.foursoft.fourever.objectmodel.Instance,
		 *      int, com.foursoft.fourever.objectmodel.Link,
		 *      com.foursoft.fourever.objectmodel.Instance, int)
		 */
        public void update(Instance instance1, int aspect1, Link link1, Instance target, int indexOfChange) {
            this.instance = instance1;
            this.aspect = aspect1;
            this.link = link1;
            wasNotified = true;
            lastIndexOfChange = indexOfChange;
        }
    }
}
